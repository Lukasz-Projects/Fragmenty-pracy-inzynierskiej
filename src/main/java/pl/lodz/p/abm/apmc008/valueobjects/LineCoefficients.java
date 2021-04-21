/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class LineCoefficients {
    final double A, B;

    public LineCoefficients(double a, double b, double standardErrorOfRegression) {
        A = a;
        B = b;
    }

    public LineCoefficients(double a, double b) {
        A = a;
        B = b;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

}
