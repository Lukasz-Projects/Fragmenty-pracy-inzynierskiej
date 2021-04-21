/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

import java.util.Iterator;
import java.util.Map;

public class MarkerFrameAdapterIterator implements Iterator<Integer> {
    private final Iterator<Map.Entry<Integer,CoordinatePair>> iterator;
    private final CoordinateType coordinateType;
    private boolean hasNext;
    private boolean consumedNext;
    private Integer key;

    public MarkerFrameAdapterIterator(Iterator<Map.Entry<Integer, CoordinatePair>> iterator, CoordinateType coordinateType) {
        this.iterator = iterator;
        this.coordinateType = coordinateType;
        this.consumedNext = true;
    }

    public boolean hasNext() {
        if (!consumedNext){
            return this.hasNext;
        }

        this.hasNext = false;
        this.key = null;
        while (iterator.hasNext()){
            final Map.Entry<Integer, CoordinatePair> next = iterator.next();
            final Coordinate coordinate = next.getValue().getCoordinate(coordinateType);
            if (coordinate != null){
                this.key = next.getKey();
                this.consumedNext = false;
                this.hasNext = true;
                break;
            }
        }

        return this.hasNext;
    }

    @Override
    public Integer next() {
        if (this.consumedNext){
            this.hasNext();
        }

        if(this.hasNext){
            this.consumedNext = true;
            return this.key;
        }

        return null;
    }
}
