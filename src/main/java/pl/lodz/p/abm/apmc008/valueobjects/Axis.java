/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public enum Axis {
    X,
    Y,
    Z;

    @Override
    public String toString(){
        switch (this){
            case X: return "X";
            case Y: return "Y";
            case Z: return "Z";
        };
        return "error";
    }

    public static Axis charToAxis(char axisChar){
        switch (axisChar){
            case 'x': case 'X': return Axis.X;
            case 'y': case 'Y': return Axis.Y;
            case 'z': case 'Z': return Axis.Z;
            default: throw new RuntimeException("Wrong Axis character");
        }
    }
}
