/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;



import pl.lodz.p.abm.apmc008.valueobjects.Axis;
import pl.lodz.p.abm.apmc008.valueobjects.Coordinate;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.Marker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleCategorizeRangesOfCoordinates  implements CategorizeRangesOfCoordinates  {
    private final Marker marker;
    private final ArithmeticAverageCoordinateValue arithmeticAverageCoordinateValue;

    public SimpleCategorizeRangesOfCoordinates(Marker marker, ArithmeticAverageCoordinateValue arithmeticAverageCoordinateValue) {
        this.marker = marker;
        this.arithmeticAverageCoordinateValue = arithmeticAverageCoordinateValue;
    }


    @Override
    public List<Coordinate[]> getRangesAboveAndEqualAvarage(CoordinateType coordinateType, Axis axis){
        List<Coordinate[]> coordinateLists = new LinkedList<>();
        final Iterator<Map.Entry<Integer, Coordinate>> iterator = marker.getEntrySetIterator(coordinateType);
        boolean isInsideRange = false;
        Coordinate beginingCoordinate = null, previousCoordinate = null;

        final double averageCoordinate;
        switch (axis){
            case X: averageCoordinate = arithmeticAverageCoordinateValue.getAverageCoordinateValue(coordinateType).getX(); break;
            case Y: averageCoordinate = arithmeticAverageCoordinateValue.getAverageCoordinateValue(coordinateType).getY(); break;
            case Z: averageCoordinate = arithmeticAverageCoordinateValue.getAverageCoordinateValue(coordinateType).getZ(); break;
            default: throw new RuntimeException("axis is null");
        }

        while (iterator.hasNext()){
            final Map.Entry<Integer, Coordinate> entry = iterator.next();
            final Coordinate currentCoordinate = entry.getValue();
            final double currentValue = currentCoordinate.get(axis);

            if (!isInsideRange && currentValue >= averageCoordinate){
                beginingCoordinate = currentCoordinate;
                isInsideRange = true;
            } else if (isInsideRange && currentValue < averageCoordinate){
                Coordinate[] pairOfCoordinates = {beginingCoordinate, previousCoordinate};
                coordinateLists.add(pairOfCoordinates);
                beginingCoordinate = null;
                isInsideRange = false;
            }
            previousCoordinate = currentCoordinate;
        }
        if (isInsideRange){
            Coordinate[] pairOfCoordinates = {beginingCoordinate, previousCoordinate};
            coordinateLists.add(pairOfCoordinates);
        }
        return coordinateLists;
    }

    @Override
    public List<Coordinate[]> getRangesBelowAvarage(CoordinateType coordinateType, Axis axis){
        List<Coordinate[]> coordinateLists = new LinkedList<>();
        final Iterator<Map.Entry<Integer, Coordinate>> iterator = marker.getEntrySetIterator(coordinateType);
        boolean isInsideRange = false;
        Coordinate beginingCoordinate = null, previousCoordinate = null;

        final double averageCoordinate = arithmeticAverageCoordinateValue.getAverageCoordinateValue(coordinateType).get(axis);

        while (iterator.hasNext()){
            final Map.Entry<Integer, Coordinate> entry = iterator.next();
            final Coordinate currentCoordinate = entry.getValue();
            final double currentValue = currentCoordinate.get(axis);

            if (!isInsideRange && currentValue < averageCoordinate){
                beginingCoordinate = currentCoordinate;
                isInsideRange = true;
            } else if (isInsideRange && currentValue >= averageCoordinate){
                Coordinate[] pairOfCoordinates = {beginingCoordinate, previousCoordinate};
                coordinateLists.add(pairOfCoordinates);
                beginingCoordinate = null;
                isInsideRange = false;
            }
            previousCoordinate = currentCoordinate;
        }
        if (isInsideRange){
            Coordinate[] pairOfCoordinates = {beginingCoordinate, previousCoordinate};
            coordinateLists.add(pairOfCoordinates);
        }
        return coordinateLists;
    }

}
