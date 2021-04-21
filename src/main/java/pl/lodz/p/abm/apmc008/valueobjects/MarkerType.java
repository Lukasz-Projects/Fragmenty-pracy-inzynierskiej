/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public enum MarkerType {
    Bone("Bone"),
    Bone_Marker("Bone Marker"),
    Marker("Marker");

    private String fieldText;

    MarkerType(String fieldText) {
        this.fieldText = fieldText;
    }

    String getFieldText(){
        return this.fieldText;
    }

    public static MarkerType getEnum(String value) {
        for(MarkerType v : values())
            if(v.getFieldText().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
