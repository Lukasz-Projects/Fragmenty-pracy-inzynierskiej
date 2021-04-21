/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;


public class LineAndCorrespondingRange {
    final private LineCoefficients lineCoefficients;
    final private RangeOfCoordinates correspondingRange;

    public LineAndCorrespondingRange(LineCoefficients lineCoefficients, RangeOfCoordinates correspondingRange) {
        this.lineCoefficients = lineCoefficients;
        this.correspondingRange = correspondingRange;
    }

    public LineCoefficients getLineCoefficients() {
        return lineCoefficients;
    }

    public RangeOfCoordinates getCorrespondingRange() {
        return correspondingRange;
    }
}
