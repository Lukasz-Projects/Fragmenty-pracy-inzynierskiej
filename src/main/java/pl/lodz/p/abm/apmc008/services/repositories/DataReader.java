/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.repositories;

import org.simpleflatmapper.csv.CsvParser;
import pl.lodz.p.abm.apmc008.valueobjects.Coordinate;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.MarkerHeader;

import java.io.*;
import java.util.*;

public class DataReader {

    List<List<String>> getDataRowLists(InputStream inputStream) throws IOException {
        final List<List<String>> dataRowLists = new LinkedList<>();
        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvParser.stream(reader).skip(7).forEachOrdered(strings-> {
                if (strings.length > 0 && !strings[strings.length-1].isEmpty()) {
                    dataRowLists.add(Arrays.asList(strings));
                }
            });
        }
        return dataRowLists;
    }

    public Map<Integer, Coordinate> getMarkerData(InputStream inputStream, MarkerHeader markerHeader) throws IOException {
        final Map<Integer, Coordinate> integerCoordinateMap = new TreeMap<>();
        try (Reader reader = new InputStreamReader(new BufferedInputStream(inputStream))) {
            CsvParser.stream(reader).skip(7).forEachOrdered(strings -> {
                if (strings.length > 0 && !strings[strings.length-1].isEmpty()) {
                    final Coordinate coordinate = new Coordinate(
                            Double.parseDouble(strings[markerHeader.getColumnTimeIndex()]),
                            Integer.parseInt(strings[markerHeader.getColumnFrameIndex()]),
                            Double.parseDouble(strings[markerHeader.getColumnXindex()]),
                            Double.parseDouble(strings[markerHeader.getColumnYindex()]),
                            Double.parseDouble(strings[markerHeader.getColumnZindex()])
                    );
                    integerCoordinateMap.put(coordinate.getFrame(),coordinate);
                }
            });
        }
        return integerCoordinateMap;
    }

    public Map<Integer, Coordinate>[] getMarkerData(InputStream inputStream, MarkerHeader[] markerHeaders) throws IOException {
        final Map<Integer, Coordinate> integerCoordinatePositionMap = new TreeMap<>();
        final Map<Integer, Coordinate> integerCoordinateRotationMap = new TreeMap<>();
        final MarkerHeader positionHeader, rotationHeader;
        if (markerHeaders[0].getCoordinateType().equals(CoordinateType.Position)){
            positionHeader = markerHeaders[0];
            rotationHeader = markerHeaders[1];
        } else {
            positionHeader = markerHeaders[1];
            rotationHeader = markerHeaders[0];
        }
        try (Reader reader = new InputStreamReader(new BufferedInputStream(inputStream))) {
            CsvParser.stream(reader).skip(7).forEachOrdered(strings -> {
                if (strings.length > 0 && !strings[strings.length-1].isEmpty()) {
                    final Coordinate coordinatePosition = new Coordinate(
                            Double.parseDouble(strings[positionHeader.getColumnTimeIndex()]),
                            Integer.parseInt(strings[positionHeader.getColumnFrameIndex()]),
                            Double.parseDouble(strings[positionHeader.getColumnXindex()]),
                            Double.parseDouble(strings[positionHeader.getColumnYindex()]),
                            Double.parseDouble(strings[positionHeader.getColumnZindex()])
                    );
                    integerCoordinatePositionMap.put(coordinatePosition.getFrame(),coordinatePosition);

                    final Coordinate coordinateRotation = new Coordinate(
                            Double.parseDouble(strings[rotationHeader.getColumnTimeIndex()]),
                            Integer.parseInt(strings[rotationHeader.getColumnFrameIndex()]),
                            Double.parseDouble(strings[rotationHeader.getColumnXindex()]),
                            Double.parseDouble(strings[rotationHeader.getColumnYindex()]),
                            Double.parseDouble(strings[rotationHeader.getColumnZindex()])
                    );
                    integerCoordinateRotationMap.put(coordinateRotation.getFrame(),coordinateRotation);
                }
            });
        }
        return new Map[]{integerCoordinatePositionMap,integerCoordinateRotationMap};
    }
}
