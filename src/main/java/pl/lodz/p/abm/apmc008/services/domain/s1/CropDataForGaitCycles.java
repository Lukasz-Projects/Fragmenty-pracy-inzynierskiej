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

import java.util.*;

/*
* This class replaces SimpleCategorizeRangesOfCoordinates
* */

public class CropDataForGaitCycles {
    private final Marker ankle;
    private final Axis axis;
    private final ArithmeticAverageCoordinateValue arithmeticAverage;

    public CropDataForGaitCycles(Marker ankle, ArithmeticAverageCoordinateValue arithmeticAverage, Axis axis) {
        this.ankle = ankle;
        this.axis = axis;
        this.arithmeticAverage = arithmeticAverage;
    }


//    returns indexes (frames)

    /**
     * Get first and last frame of below average part
     * @return
     */
    public List<Integer[]> getListOfFramesForCycle(){
        List<Integer[]> listToReturn = new ArrayList<>();
        LinkedList<Integer> listOfIndexes = new LinkedList<>();
        final Iterator<Map.Entry<Integer, Coordinate>> ankleSetIterator = ankle.getEntrySetIterator(CoordinateType.Position);
        Map.Entry<Integer, Coordinate> mapEntry = ankleSetIterator.next();
        final double avg = arithmeticAverage.getAverageCoordinateValue(CoordinateType.Position).get(axis);
        boolean prevWasBelow, prevWasAbove;
        if (mapEntry.getValue().get(axis) > avg) {
            prevWasAbove = true;
            prevWasBelow = false;
        } else if (mapEntry.getValue().get(axis) < avg){
            prevWasAbove = false;
            prevWasBelow = true;
        } else {
            prevWasAbove = false;
            prevWasBelow = false;
        }
        while (ankleSetIterator.hasNext()) {
            final int howMany = 2;
            if (ankle.getCoordinate(howMany + mapEntry.getKey(), CoordinateType.Position) == null)
                break;
            if (!prevWasAbove && mapEntry.getValue().get(axis) > avg && areNextValuesHigher(mapEntry.getKey(),avg, howMany)){
                listOfIndexes.add(mapEntry.getKey());
                prevWasAbove = true;
                prevWasBelow = false;
            } else if (!prevWasBelow && mapEntry.getValue().get(axis) < avg && areNextValuesLower(mapEntry.getKey(), avg,howMany)){
                listOfIndexes.add(mapEntry.getKey());
                prevWasAbove = false;
                prevWasBelow = true;
            } else if (mapEntry.getValue().get(axis) == avg){
                prevWasAbove = false;
                prevWasBelow = false;
            }
            mapEntry = ankleSetIterator.next();
        }

        final Iterator<Integer> iterator = listOfIndexes.iterator();
        Integer prevFrame = iterator.next();
        if (prevFrame >= avg)
            prevFrame = iterator.next();

        while (iterator.hasNext()){
            Integer frame = iterator.next();
            listToReturn.add(new Integer[]{prevFrame,frame});
            if (iterator.hasNext())
                prevFrame = iterator.next();
        }
        return listToReturn;
    }

    private boolean areNextValuesLower(Integer startKey, double avarage, int howMany) {
        for (int i = 0; i < howMany; ++i){
            if (ankle.getCoordinate(startKey + i, CoordinateType.Position).get(axis) >= avarage)
                return false;
        }
        return true;
    }

    private boolean areNextValuesHigher(Integer startKey, double avarage, int howMany) {
        for (int i = 0; i < howMany; ++i){
            if (ankle.getCoordinate(startKey + i, CoordinateType.Position).get(axis) <= avarage)
                return false;
        }
        return true;
    }
}
