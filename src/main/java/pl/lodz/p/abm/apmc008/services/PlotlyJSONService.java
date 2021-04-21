/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.abm.apmc008.services.domain.s1.*;
import pl.lodz.p.abm.apmc008.services.domain.s3.*;
import pl.lodz.p.abm.apmc008.services.domain.s4.CheckLengthOfLinearRegion;
import pl.lodz.p.abm.apmc008.services.domain.s4.DummyLinearRegionsAroundMinima;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerProperties;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerService;
import pl.lodz.p.abm.apmc008.valueobjects.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
public class PlotlyJSONService implements JSONService {
    private final MarkerService markerService;
    private final String LEFT_ANKLE_MARKER_NAME;
    private final static CoordinateType COORDINATE_TYPE = CoordinateType.Position;
    private final static MarkerType MARKER_TYPE = MarkerType.Marker;

    @Autowired
    public PlotlyJSONService(MarkerService markerService, MarkerProperties markerProperties){
        this.markerService = markerService;
        this.LEFT_ANKLE_MARKER_NAME = markerProperties.getLeftAnkleName();
    }

    private JSONObject jsonForMarkerAxis(@NotNull Marker marker, @NotNull Axis axis, double startTime, double endTime, @NotNull TraceType traceType, @NotNull TraceMode traceMode)  {
        final JSONObject trace = new JSONObject();
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final Iterator<Map.Entry<Integer, Coordinate>> entrySetIterator = marker.getEntrySetIterator(CoordinateType.Position);
        while (entrySetIterator.hasNext()) {
            final Map.Entry<Integer, Coordinate> next = entrySetIterator.next();
            final Coordinate value = next.getValue();
            if (value.getTime() >= startTime) {
                x.put(value.getTime());
                y.put(value.get(axis));
            }
            if (value.getTime() >= endTime) break;
        }
        trace.put("x", x);
        trace.put("y", y);
        trace.put("mode", traceMode.toString());
        trace.put("type", traceType.toString());
        trace.put("name", String.format("%s, %s", marker.getName(), axis.toString()));
        return trace;
    }

    @Override
    public JSONObject jsonForMarkerAxis(@NotNull String name, @NotNull Axis axis, double startTime, double endTime, @NotNull TraceType traceType, @NotNull TraceMode traceMode) throws SQLException, IOException, ClassNotFoundException {
        return jsonForMarkerAxis(
                markerService.getMarker(name,COORDINATE_TYPE,MarkerType.Marker),
                axis,
                startTime,
                endTime,
                traceType,
                traceMode
        );
    }

    @Override
    public JSONObject jsonForMarkerAxis(@NotNull String name, @NotNull Axis axis, double startTime, @NotNull TraceType traceType, @NotNull TraceMode traceMode) throws SQLException, IOException, ClassNotFoundException {
        final Marker marker = markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE);
        final int lastFrameNumber = marker.getLastFrameNumber(CoordinateType.Position);
        return jsonForMarkerAxis(
                marker,
                axis,
                startTime,
                marker.getCoordinate(lastFrameNumber,CoordinateType.Position).getTime(),
                traceType,
                traceMode
        );
    }

    private JSONObject jsonForArithmeticAverage(Marker marker, Axis axis, CoordinateType coordinateType, double startTime, double endTime, TraceType traceType){
        final JSONObject averageLineTrace = new JSONObject();
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final Coordinate averageCoordinateValue = new ArithmeticAverageCoordinateValue(marker).getAverageCoordinateValue(coordinateType);
        x.put(startTime);
        y.put(averageCoordinateValue.get(axis));
        x.put(endTime);
        y.put(averageCoordinateValue.get(axis));
        averageLineTrace.put("x", x)
                .put("y", y)
                .put("mode", "lines")
                .put("type", traceType.toString())
                .put("name", String.format("avg %s %s", marker.getName(), axis.toString()));
        return averageLineTrace;
    }

    public JSONObject jsonForArithmeticAverage(String name, Axis axis, double startTime, double endTime, TraceType traceType) throws SQLException, IOException, ClassNotFoundException {
        final Marker marker = markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE);
        return jsonForArithmeticAverage(marker,axis,CoordinateType.Position,startTime,endTime,traceType);
    }

    public JSONObject jsonForArithmeticAverage(String name, Axis axis, double startTime, TraceType traceType) throws SQLException, IOException, ClassNotFoundException {
        final Marker marker = markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE);
        final int lastFrameNumber = marker.getLastFrameNumber(CoordinateType.Position);
        return jsonForArithmeticAverage(marker,axis,CoordinateType.Position,startTime,marker.getCoordinate(lastFrameNumber,CoordinateType.Position).getTime(),traceType);
    };

    public JSONObject jsonForCategorizeRangesOfCoordinates(String name, Axis axis, double startTime, double endTime) throws SQLException, IOException, ClassNotFoundException {
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final Marker marker = markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE);
        final CategorizeRangesOfCoordinates categorizeRangesOfCoordinates = new SimpleCategorizeRangesOfCoordinates(
                marker,
                new ArithmeticAverageCoordinateValue(marker)
        );

        for (Coordinate[] coordinates : categorizeRangesOfCoordinates.getRangesAboveAndEqualAvarage(CoordinateType.Position, axis)) {
            if (coordinates[0].getTime()>=startTime && coordinates[0].getTime()<=endTime) {
                x.put(coordinates[0].getTime());
                y.put(coordinates[0].get(axis));
            } else if (coordinates[0].getTime() > endTime) break;
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.MARKERS.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("1 phase %s %s", marker.getName(), axis.toString()));
    }

    public JSONObject jsonForTimeDerivative(String name, Axis axis, double startTime, double endTime) throws SQLException, IOException, ClassNotFoundException {
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final DifferentiatedCoordinates differentiatedCoordinates = new CentralDifferenceCalculateTimeDerivative(
                markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE).getRangeOfCoordinates(CoordinateType.Position),
                axis
        ).getDifferentiatedCoordinates();
        for (Coordinate value : differentiatedCoordinates.getCoordinateList()) {
            if (value.getTime()<=endTime){
                if (value.getTime() >= startTime) {
                    x.put(value.getTime());
                    y.put(value.get(axis));
                }
            } else {
                break;
            }
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.LINES.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("time der. %s %s", name, axis.toString()));
    }

    public JSONObject jsonForExtremaInRanges(String name, Axis axis, double startTime, double endTime, boolean minima) throws SQLException, IOException, ClassNotFoundException {
        final JSONArray x = new JSONArray(), y = new JSONArray();
        var categorizeRangesOfCoordinates = new SimpleCategorizeRangesOfCoordinates(
                markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE),
                new ArithmeticAverageCoordinateValue(markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE))
        );
        final Collection<Coordinate> coordinates;
        if (minima){
            coordinates = new FindExtremaInRanges(
                    markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE), categorizeRangesOfCoordinates, COORDINATE_TYPE
            ).findMinimaInRangesBelowAverage(axis);
        } else {
            coordinates = new FindExtremaInRanges(
                    markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE), categorizeRangesOfCoordinates, COORDINATE_TYPE
            ).findMaximaInRangesAboveAndEqualAverage(axis);
        }
        for (Coordinate coordinate : coordinates) {
            if (coordinate.getTime()<=endTime) {
                if (coordinate.getTime()>=startTime) {
                    x.put(coordinate.getTime());
                    y.put(coordinate.get(axis));
                }
            } else {
                break;
            }
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.LINES.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("find minima %s %s", name, axis.toString()));
    }

    public JSONObject jsonForLocalMinima(String name, Axis axis, double startTime, double endTime, CoordinateType coordinateType) throws SQLException, IOException, ClassNotFoundException {
        final JSONArray x = new JSONArray(), y = new JSONArray();
        CropDataForGaitCycles cropDataForGaitCycles = new CropDataForGaitCycles(
                markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE),
                new ArithmeticAverageCoordinateValue(markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE)),
                axis
        );

        for (Integer[] integers : cropDataForGaitCycles.getListOfFramesForCycle()) {
            RangeOfCoordinates rangeOfCoordinates = new RangeOfCoordinates(
                    integers[0],integers[1],markerService.getMarker(name,COORDINATE_TYPE,MARKER_TYPE),coordinateType);
            LocalMinima localMinima = new DummyLocalMinima(rangeOfCoordinates);
            for (Coordinate coordinate : localMinima.getLocalMinima(axis)) {
                if (coordinate.getTime()<=endTime) {
                    if (coordinate.getTime()>=startTime) {
                        x.put(coordinate.getTime());
                        y.put(coordinate.get(axis));
                    }
                } else {
                    break;
                }
            }
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.LINES.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("localMinima %s %s", name, axis.toString()));
    }

    public JSONObject jsonForDLRAM(String ANKLE_MARKER_NAME, double startTime, double endTime) throws SQLException, IOException, ClassNotFoundException {
        final Axis axis = Axis.Y;
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final Marker ankleMarker = markerService.getMarker(ANKLE_MARKER_NAME,COORDINATE_TYPE,MARKER_TYPE);
        for (var integers : new PossiblyWalkingGaitCycles(new ArithmeticAverageCoordinateValue(ankleMarker),ankleMarker)
                .getListOfEstimatedRanges()) {
            final var rangeOfCoordinates = new RangeOfCoordinates(integers[0], integers[1], ankleMarker, CoordinateType.Position);
            if (rangeOfCoordinates.getCoordinateByFrame(rangeOfCoordinates.getLastFrame()).getTime() <= endTime) {
                if (rangeOfCoordinates.getCoordinateByFrame(rangeOfCoordinates.getFirstFrame()).getTime() >= startTime) {
                    for (LineAndCorrespondingRange region : new DummyLinearRegionsAroundMinima(rangeOfCoordinates).getRegions(axis)) {
                        final var correspondingRange = region.getCorrespondingRange();
                        final Coordinate firstCoordinate = correspondingRange.getCoordinateByFrame(correspondingRange.getFirstFrame());
                        final Coordinate lastCoordinate = correspondingRange.getCoordinateByFrame(correspondingRange.getLastFrame());
                        x.put(firstCoordinate.getTime());
                        x.put(lastCoordinate.getTime());
                        y.put(firstCoordinate.get(axis));
                        y.put(lastCoordinate.get(axis));
                    }
                }
            } else {
                break;
            }
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.MARKERS.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("linear region a. minima %s %s", LEFT_ANKLE_MARKER_NAME, axis.toString()));
    }

    // slow...
    public JSONObject jsonForCLengthOfLinearRegion(double startTime, double endTime) throws SQLException, IOException, ClassNotFoundException {
        final JSONArray x = new JSONArray(), y = new JSONArray();
        final var axis = Axis.Y;
        final var ankleMarker = markerService.getMarker(LEFT_ANKLE_MARKER_NAME,COORDINATE_TYPE,MARKER_TYPE);
        final var arithmeticAverage = new ArithmeticAverageCoordinateValue(ankleMarker);
        final var checkLengthOfLinearRegion = new CheckLengthOfLinearRegion(ankleMarker, axis);
        final var findExtremaInRanges = new FindExtremaInRanges(ankleMarker,new SimpleCategorizeRangesOfCoordinates(ankleMarker,arithmeticAverage),CoordinateType.Position);

        for (Integer[] integers : new PossiblyWalkingGaitCycles(arithmeticAverage,ankleMarker).getListOfEstimatedRanges()) {
            final RangeOfCoordinates rangeOfCoordinates = new RangeOfCoordinates(integers[0], integers[1], ankleMarker, CoordinateType.Position);
            if (rangeOfCoordinates.getCoordinateByFrame(rangeOfCoordinates.getLastFrame()).getTime() <= endTime) {
                if (rangeOfCoordinates.getCoordinateByFrame(rangeOfCoordinates.getFirstFrame()).getTime() >= startTime) {
                    List<Coordinate> lineSegment;
                    for (Coordinate coordinate : findExtremaInRanges.findMinimaInRangesBelowAverage(Axis.Y)) {
                        if (coordinate.getFrame() >= integers[0] && coordinate.getFrame() <= integers[1]) {
                            if (null != (lineSegment = checkLengthOfLinearRegion.getLineSegment(coordinate)) && lineSegment.size() > 0) {
                                final Coordinate firstCoordinate = lineSegment.get(0);
                                final Coordinate lastCoordinate = lineSegment.get(lineSegment.size() - 1);
                                x.put(firstCoordinate.getTime());
                                x.put(lastCoordinate.getTime());
                                y.put(firstCoordinate.get(axis));
                                y.put(lastCoordinate.get(axis));
                            }
                        }
                    }
                }
            } else {
                break;
            }
        }

        return new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("mode", TraceMode.MARKERS.toString())
                .put("type", TraceType.SCATTERGL.toString())
                .put("name", String.format("checkLengthOfLinearRegion %s %s", LEFT_ANKLE_MARKER_NAME, axis.toString()));
    }

    public enum TraceType {
        SCATTER {
            @Override
            public String toString() {
                return "scatter";
            }
        },
        SCATTERGL {
            @Override
            public String toString() {
                return "scattergl";
            }
        },
        POINTCLOUD {
            @Override
            public String toString() {
                return "pointcloud";
            }
        };

        public static TraceType parseType(String type){
            switch (type.toLowerCase()){
                case "scatter": return TraceType.SCATTER;
                case "scattergl": return TraceType.SCATTERGL;
                case "pointcloud": return TraceType.POINTCLOUD;
                default: throw new NoSuchElementException();
            }
        }
    }

    public enum TraceMode {
        MARKERS {
            @Override
            public String toString() {
                return "markers";
            }
        },
        LINES {
            @Override
            public String toString() {
                return "lines";
            }
        },
        MARKERS_AND_LINES {
            @Override
            public String toString() {
                return "lines+markers";
            }
        };

        public static TraceMode parseMode(String mode){
            final String[] splitMode = mode.split(" ");
            boolean markers = false, lines = false;
            for (String spl : splitMode) {
                switch (spl.toLowerCase()){
                    case "markers": markers = true; break;
                    case "lines": lines = true; break;
                    default: throw new IllegalArgumentException(spl);
                }
            }

            if (markers && lines) return TraceMode.MARKERS_AND_LINES;
            else if (markers) return TraceMode.MARKERS;
            else if (lines) return TraceMode.LINES;
            else throw new IllegalArgumentException();
        }
    }
}
