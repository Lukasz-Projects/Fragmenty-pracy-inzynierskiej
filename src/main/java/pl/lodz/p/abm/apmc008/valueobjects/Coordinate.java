/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class Coordinate implements Comparable<Coordinate> {
    private final double X,Y,Z;
    private final double time;
    private final int frame;

    public double get(Axis axis){
        switch (axis){
            case X: return getX();
            case Y: return getY();
            case Z: return getZ();
            default: throw new RuntimeException("Null in argument");
        }
    }


    @Override
    public boolean equals(Object obj){
        if (obj instanceof Coordinate){
            Coordinate coordinateToCompare = (Coordinate) obj;
            return time == coordinateToCompare.getTime() &&
                    frame == coordinateToCompare.getFrame() &&
                    X == coordinateToCompare.getX() &&
                    Y == coordinateToCompare.getY() &&
                    Z == coordinateToCompare.getZ();
        }
        return false;
    }

    public Double getX() {
        return X;
    }

    public Double getY() {
        return Y;
    }

    public Double getZ() {
        return Z;
    }

    public double getTime() {
        return time;
    }

    public int getFrame() {
        return frame;
    }

    public Coordinate(double time, int frame, double X, double Y, double Z){
        this.time = time;
        this.frame = frame;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }


    @Override
    public int hashCode(){
        return Integer.hashCode(this.frame);
    }

    @Override
    public int compareTo(Coordinate coordinate) {
        final int frame2 = coordinate.getFrame();
        if (this.frame > frame2)
            return 1;
        else if (this.frame < frame2)
            return -1;
        else {
            if (this.hashCode() > coordinate.hashCode())
                return 1;
            else if (this.hashCode() < coordinate.hashCode())
                return -1;
            else
                return 0;
        }
    }
}
