/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;

import pl.lodz.p.abm.apmc008.valueobjects.Coordinate;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.Marker;

import java.util.Iterator;
import java.util.Map;

public class ArithmeticAverageCoordinateValue {
    private final Marker marker;


    public ArithmeticAverageCoordinateValue(Marker marker) {
        this.marker = marker;
    }

    /*
    algo : https://en.wikipedia.org/wiki/Kahan_summation_algorithm
     */
    public Coordinate getAverageCoordinateValue(CoordinateType coordinateType){
        double sumX = 0, sumY = 0, sumZ = 0, sumTime = 0;
        double cX = 0, cY = 0, cZ = 0, cTime = 0;
        int i = 0, sumFrame = 0;
        final Iterator<Map.Entry<Integer, Coordinate>> entrySetIterator = marker.getEntrySetIterator(coordinateType);

        while (entrySetIterator.hasNext()){
            double tX, tY, tZ , tTime;
            double yX, yY, yZ, yTime;
            final Coordinate currentCoordinate  = entrySetIterator.next().getValue();

            yX = currentCoordinate.getX() - cX;
            tX = sumX + yX;
            cX = (tX - sumX) - yX;
            sumX = tX;

            yY = currentCoordinate.getY() - cY;
            tY = sumY + yY;
            cY = (tY - sumY) - yY;
            sumY = tY;

            yZ = currentCoordinate.getZ() - cZ;
            tZ = sumZ + yZ;
            cZ = (tZ - sumZ) - yZ;
            sumZ = tZ;

            yTime = currentCoordinate.getTime() - cTime;
            tTime = sumTime + yTime;
            cTime = (tTime - sumTime) - yTime;
            sumTime = tTime;

            sumFrame += currentCoordinate.getFrame();
            ++i;
        }
        return new Coordinate(sumTime / i,sumFrame / i,sumX / i,sumY / i, sumZ / i);
    }
}
