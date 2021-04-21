/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

import java.util.List;

public class DifferentiatedCoordinates{
    final private String markerID, markerName;
    final private Axis axis;
    final private CoordinateType coordinateType;
    final private List<Coordinate> coordinateList;


    public DifferentiatedCoordinates(String markerID, String markerName, Axis axis, CoordinateType coordinateType, List<Coordinate> coordinateList) {
        this.markerID = markerID;
        this.markerName = markerName;
        this.axis = axis;
        this.coordinateType = coordinateType;
        this.coordinateList = coordinateList;
    }

    public String getMarkerID() {
        return markerID;
    }

    public String getMarkerName() {
        return markerName;
    }

    public Axis getAxis() {
        return axis;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }
}
