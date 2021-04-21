/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain;

import pl.lodz.p.abm.apmc008.services.domain.s3.CalculateTimeDerivative;
import pl.lodz.p.abm.apmc008.valueobjects.Axis;
import pl.lodz.p.abm.apmc008.valueobjects.Coordinate;
import pl.lodz.p.abm.apmc008.valueobjects.DifferentiatedCoordinates;

import java.util.*;

public class LocalExtrema {
    private final static double DELTA = 0.001;
    private final CalculateTimeDerivative calculateTimeDerivative;
    private final Axis axis;

    public LocalExtrema(CalculateTimeDerivative calculateTimeDerivative, Axis axis) {
        this.calculateTimeDerivative = calculateTimeDerivative;
        this.axis = axis;
    }

    public List<Coordinate> getLocalMinima(){
        List<Coordinate> localMinimaList = new LinkedList<>();
        // populated in next method
        var diffedCoord = new TreeMap<Integer,Coordinate>();
        NavigableMap<Integer,Coordinate> closeToZeroCoordinates = createCloseToZeroCoordNavMap(diffedCoord);


        for (Map.Entry<Integer, Coordinate> closeToZeroEntry : closeToZeroCoordinates.entrySet()) {
            final Integer frame = closeToZeroEntry.getKey();
            final Double leftValue = (diffedCoord.get(frame - 1) == null ? null : diffedCoord.get(frame - 1).get(axis));
            final Double rightValue = (diffedCoord.get(frame + 1) == null ? null : diffedCoord.get(frame + 1).get(axis));
            if (leftValue == null || rightValue == null)
                continue;
            if (
                    ((leftValue < 0 || closeToZero(leftValue)) && (rightValue > 0)) ||
                            ((leftValue < 0) && (rightValue > 0 || closeToZero(rightValue)))
            ) {
                localMinimaList.add(closeToZeroEntry.getValue());
            }
        }
        return localMinimaList;
    }

    public List<Coordinate> getLocalMaxima(){
        List<Coordinate> localMaximaList = new LinkedList<>();
        // populated in next method
        var diffedCoord = new TreeMap<Integer,Coordinate>();
        NavigableMap<Integer,Coordinate> closeToZeroCoordinates = createCloseToZeroCoordNavMap(diffedCoord);


        for (Map.Entry<Integer, Coordinate> closeToZeroEntry : closeToZeroCoordinates.entrySet()) {
            final Integer frame = closeToZeroEntry.getKey();
            final Double leftValue = (diffedCoord.get(frame - 1) == null ? null : diffedCoord.get(frame - 1).get(axis));
            final Double rightValue = (diffedCoord.get(frame + 1) == null ? null : diffedCoord.get(frame + 1).get(axis));
            if (leftValue == null || rightValue == null)
                continue;
            if (
                    ((leftValue > 0) && (rightValue < 0 || closeToZero(rightValue))) || ((leftValue > 0|| closeToZero(leftValue)) && ((rightValue < 0)))
            ) {
                localMaximaList.add(closeToZeroEntry.getValue());
            }
        }
        return localMaximaList;
    }


    private final NavigableMap<Integer, Coordinate> createCloseToZeroCoordNavMap(NavigableMap<Integer, Coordinate> diffedCoord) {
        var closeToZeroCoordinates = new TreeMap<Integer, Coordinate>();
        final DifferentiatedCoordinates differentiatedCoordinates = calculateTimeDerivative.getDifferentiatedCoordinates();
        for (Coordinate coordinate : differentiatedCoordinates.getCoordinateList()) {
            if (closeToZero(coordinate.get(axis))){
                closeToZeroCoordinates.put(coordinate.getFrame(),coordinate);
            }
            diffedCoord.put(coordinate.getFrame(),coordinate);
        }
        return closeToZeroCoordinates;
    }

    private final static boolean closeToZero(double value){
        return value < DELTA && value > -DELTA;
    }
}
