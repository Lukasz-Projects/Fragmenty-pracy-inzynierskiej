/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class CoordinatePair implements Comparable<CoordinatePair> {
    private final Coordinate[] coordinates;

    public CoordinatePair(Coordinate positionCoordinate, Coordinate positionCoordinate2) {
        if (positionCoordinate == null && positionCoordinate2 == null)
            throw new NullPointerException();

        if (positionCoordinate != null && positionCoordinate2 != null)
            if (positionCoordinate.getFrame() != positionCoordinate2.getFrame())
                throw new CoordinateFramesMismatchException();

        this.coordinates = new Coordinate[]{positionCoordinate,positionCoordinate2};
    }

    public int getFrame() {
        if (coordinates[0] != null)
            return coordinates[0].getFrame();

        return coordinates[1].getFrame();
    }

    public Coordinate getPositionCoordinate(){
        return coordinates[0];
    }

    public Coordinate getRotationCoordinate(){
        return coordinates[1];
    }

    public Coordinate getCoordinate(CoordinateType reqCoordinateType){
        switch (reqCoordinateType){
            case Position: return coordinates[0];
            case Rotation: return coordinates[1];
            default: throw new RuntimeException();
        }
    }

    @Override
    public int compareTo(CoordinatePair coordinatePair) {
        if (this.getFrame() < coordinatePair.getFrame())
            return -1;
        else if (this.getFrame() > coordinatePair.getFrame())
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof CoordinatePair) {
            final CoordinatePair coordinatePair = (CoordinatePair) obj;
            boolean pCoordinateEq = false;
            boolean rCoordinateEq = false;

            if (this.coordinates[0] == null && coordinatePair.coordinates[0] == null)
                pCoordinateEq = true;
            else if (this.coordinates[0] != null)
                pCoordinateEq = this.coordinates[0].equals(coordinatePair.coordinates[0]);
            else
                return false;

            if (this.coordinates[1] == null && coordinatePair.coordinates[1] == null)
                rCoordinateEq = true;
            else if (this.coordinates[1] != null)
                rCoordinateEq = this.coordinates[1].equals(coordinatePair.coordinates[1]);
            else
                return false;

            return (pCoordinateEq && rCoordinateEq);

        }
        return false;
    }

    @Override
    public int hashCode(){
        int pHashCode = 0, rHashCode = 0;
        if (coordinates[0] != null)
            pHashCode = coordinates[0].hashCode();
        if (coordinates[1] != null)
            rHashCode = coordinates[1].hashCode();
        return (int)(((long)pHashCode + rHashCode)/2);
    }

}
