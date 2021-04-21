/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

import java.util.Iterator;
import java.util.Map;

public interface Marker {
    String getId();
    String getName();
    MarkerType getMarkerType();
    CoordinateType getCoordinateType();
    Iterator<Integer> getFrameIterator(CoordinateType requestedCoordinateType);
    Iterator<Map.Entry<Integer, Coordinate>> getEntrySetIterator(CoordinateType requestedCoordinateType);
    int getFirstFrameNumber(CoordinateType coordinateType);
    int getLastFrameNumber(CoordinateType coordinateType);
    Coordinate getCoordinate(Integer key, CoordinateType reqCoordinateType);
    RangeOfCoordinates getRangeOfCoordinates(int firstFrame,int lastFrame,CoordinateType requestedCoordinateType);
    RangeOfCoordinates getRangeOfCoordinates(CoordinateType requestedCoordinateType);
}
