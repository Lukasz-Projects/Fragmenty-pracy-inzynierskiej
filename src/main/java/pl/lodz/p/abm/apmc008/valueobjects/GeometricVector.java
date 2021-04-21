/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class GeometricVector {
    private final double A1, A2, A3;

    public GeometricVector(double A1, double A2, double A3) {
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
    }

    public GeometricVector(Coordinate c1, Coordinate c2) {
        this.A1 = c2.getX() - c1.getX();
        this.A2 = c2.getY() - c1.getY();
        this.A3 = c2.getZ() - c1.getZ();
    }

    public double dotProduct(double B1, double B2, double B3){
        return A1*B1 + A2*B2 + A3*B3;
    }

    public double dotProduct(GeometricVector B){
        return dotProduct(B.getA1(),B.getA2(),B.getA3());
    }

    public double magnitude(){
        return Math.sqrt(A1*A1 + A2*A2 + A3*A3);
    }

    public double getA1() {
        return A1;
    }

    public double getA2() {
        return A2;
    }

    public double getA3() {
        return A3;
    }
}
