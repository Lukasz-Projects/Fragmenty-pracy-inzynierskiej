/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services;

import pl.lodz.p.abm.apmc008.valueobjects.Marker;

import java.util.List;

public class FindMarkerByID {
    final List<Marker> markerList;
    final String id;

    public FindMarkerByID(List<Marker> markerList, String id) {
        this.markerList = markerList;
        this.id = id;
    }

    public Marker findMarker(){
        for (Marker marker : markerList) {
            if (marker.getId().equals(this.id)){
                return marker;
            }
        }
        return null;
    }
}
