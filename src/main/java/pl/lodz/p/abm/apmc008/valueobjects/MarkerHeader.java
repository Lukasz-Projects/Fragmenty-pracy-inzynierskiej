/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class MarkerHeader {
    private final String id;
    private final String name;
    private final MarkerType markerType;
    private final CoordinateType coordinateType;
    private final int columnXindex, columnYindex, columnZindex;

    private final int columnTimeIndex, columnFrameIndex;

    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof  MarkerHeader){
            final MarkerHeader markHeader = (MarkerHeader) obj;
            return this.id.equals(markHeader.id) &&
                   this.name.equals(markHeader.name) &&
                   this.markerType.equals(markHeader.markerType) &&
                   this.coordinateType.equals(markHeader.coordinateType) &&
                   this.columnXindex == markHeader.columnXindex &&
                   this.columnYindex == markHeader.columnYindex &&
                   this.columnZindex == markHeader.columnZindex;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (int)((long)this.id.hashCode() + this.coordinateType.name().hashCode())/2;
    }


    public MarkerHeader(String id, String name, MarkerType markerType, CoordinateType coordinateType, int columnXindex, int columnYindex, int columnZindex) {
        this.id = id;
        this.name = name;
        this.markerType = markerType;
        this.coordinateType = coordinateType;
        this.columnXindex = columnXindex;
        this.columnYindex = columnYindex;
        this.columnZindex = columnZindex;
        this.columnFrameIndex = 0;
        this.columnTimeIndex = 1;
    }

    public MarkerHeader(String id, String name, MarkerType markerType, CoordinateType coordinateType, int columnXindex, int columnYindex, int columnZindex, int columnFrameIndex, int columnTimeIndex) {
        this.id = id;
        this.name = name;
        this.markerType = markerType;
        this.coordinateType = coordinateType;
        this.columnXindex = columnXindex;
        this.columnYindex = columnYindex;
        this.columnZindex = columnZindex;
        this.columnFrameIndex = columnFrameIndex;
        this.columnTimeIndex = columnTimeIndex;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MarkerType getMarkerType() {
        return markerType;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public int getColumnXindex() {
        return columnXindex;
    }

    public int getColumnYindex() {
        return columnYindex;
    }

    public int getColumnZindex() {
        return columnZindex;
    }

    public int getColumnTimeIndex() {
        return columnTimeIndex;
    }

    public int getColumnFrameIndex() {
        return columnFrameIndex;
    }
}
