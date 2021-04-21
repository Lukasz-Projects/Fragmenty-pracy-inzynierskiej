/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;

import pl.lodz.p.abm.apmc008.services.domain.s1.ArithmeticAverageCoordinateValue;
import pl.lodz.p.abm.apmc008.valueobjects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PossiblyWalkingGaitCycles {
    final ArithmeticAverageCoordinateValue arithmeticAverageCoordinateValue;
    final Axis heightAxis = Axis.Y;
    final Marker marker;

    public PossiblyWalkingGaitCycles(ArithmeticAverageCoordinateValue averageCoordinateValue, Marker ankle){
        this.marker = ankle;
        this.arithmeticAverageCoordinateValue = averageCoordinateValue;
    }

    public PossiblyWalkingGaitCycles(ArithmeticAverageCoordinateValue avg, WalkingGaitCycleInputData walkingGaitCycleInputData) {
        arithmeticAverageCoordinateValue = avg;
        this.marker = walkingGaitCycleInputData.getAnkle();
    }

//    pick second above average, because I look for full gait cycle, not just step
    public List<Integer[]> getListOfEstimatedRanges(){
        final Marker ankleMarker = marker;
        final double average = arithmeticAverageCoordinateValue.getAverageCoordinateValue(CoordinateType.Position).get(heightAxis);
        List<Integer> belowAverageList = findBegginingsOfRangesBelowAverage(average,ankleMarker);
        List<Integer[]> rangeList = new ArrayList<>();
        final List<Coordinate> coordinatePositionList = null;
        for (int index : belowAverageList){
            final int firstAboveAverageCoordinateIndex = findAboveAverage(index,average);
            if (firstAboveAverageCoordinateIndex < 0) {
                throw new RuntimeException("Don't found - maybe too little data?");
//                break;
            }
            final int secondAboveAverageCoordinateIndex = findAboveAverage(firstAboveAverageCoordinateIndex,average);
            if (secondAboveAverageCoordinateIndex < 0)
                break;
            rangeList.add(new Integer[]{index,secondAboveAverageCoordinateIndex - 1});
        }
        return rangeList;
    }


    private int findAboveAverage(int startIndex, double average) {
        Coordinate previousCoordinate = null;
        for (int i = startIndex; i < marker.getLastFrameNumber(CoordinateType.Position); ++i) {
            Coordinate coordinate = marker.getCoordinate(i,CoordinateType.Position);
            if (previousCoordinate == null){
                previousCoordinate = coordinate;
                continue;
            }

            if (coordinate.get(heightAxis) >= average){
                if (previousCoordinate.get(heightAxis) < average){
                    return i;
                }
            }
            previousCoordinate = coordinate;
        }
        return -1;
    }

    private List<Integer> findBegginingsOfRangesBelowAverage(double average, Marker marker) {
        List<Integer> coordinateJustBelowAverageList = new LinkedList<>();
        Coordinate previousCoordinate = null, previousPreviousCoordinate = null;
        for (int i = marker.getFirstFrameNumber(CoordinateType.Position); i <= marker.getLastFrameNumber(CoordinateType.Position); ++i) {
            Coordinate coordinate = marker.getCoordinate(i,CoordinateType.Position);
            if (previousCoordinate == null){
                previousCoordinate = coordinate;
                continue;
            }

            if (previousPreviousCoordinate == null){
                previousPreviousCoordinate = previousCoordinate;
                previousCoordinate = coordinate;
                continue;
            }


            if (coordinate.get(heightAxis) < average){
                if (previousCoordinate.get(heightAxis) < average){
                    if (previousPreviousCoordinate.get(heightAxis) >= average)
                        coordinateJustBelowAverageList.add(i-1);
                }
            }

            previousPreviousCoordinate = previousCoordinate;
            previousCoordinate = coordinate;
        }
        return coordinateJustBelowAverageList;
    }
}
