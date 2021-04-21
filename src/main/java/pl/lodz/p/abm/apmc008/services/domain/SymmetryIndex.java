/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain;

public final class SymmetryIndex {
    private final double xL, xR;

    public SymmetryIndex(double xL, double xR) {
        this.xL = xL;
        this.xR = xR;
    }

//  returns percentage
    public double calculate(){
        final double abs = Math.abs(xL - xR)*200;
        final double denominator = xL + xR;
        return abs / denominator;
    }
}
