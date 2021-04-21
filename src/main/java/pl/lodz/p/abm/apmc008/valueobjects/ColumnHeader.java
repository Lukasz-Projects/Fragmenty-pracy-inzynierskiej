/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class ColumnHeader implements Comparable<ColumnHeader> {
    private final MarkerType markerType;
    private String markerName;
    private String markerID;
    private CoordinateType coordinateType;
    private Axis axis;


    public ColumnHeader(MarkerType markerType) {
        this.markerType = markerType;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(CoordinateType coordinateType) {
        this.coordinateType = coordinateType;
    }

    public Axis getAxis() {
        return axis;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    public MarkerType getMarkerType() {
        return markerType;
    }

    @Override
    public int hashCode(){
        return (int)((long)this.markerID.hashCode() + (long)this.axis.name().hashCode())/2;
    }

    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof ColumnHeader){
            final ColumnHeader columnHeader = (ColumnHeader) obj;
            boolean isIDEqual, isNameEqual, isCoordinateTypeE, isAxisEq, isMarkerTypeEq;
            isIDEqual = this.markerID.equals(columnHeader.markerID);
            isNameEqual = this.markerName.equals(columnHeader.markerName);
            isCoordinateTypeE = this.coordinateType.equals(columnHeader.coordinateType);
            isAxisEq = this.axis.equals(columnHeader.axis);
            isMarkerTypeEq = this.markerType.equals(columnHeader.markerType);
            return isIDEqual && isNameEqual && isCoordinateTypeE && isAxisEq && isMarkerTypeEq;
        }
        return false;
    }

    @Override
    public int compareTo(ColumnHeader columnHeader) {
        return (this.markerID  + this.axis.name()).compareTo(columnHeader.markerID + axis.name());
    }
}
