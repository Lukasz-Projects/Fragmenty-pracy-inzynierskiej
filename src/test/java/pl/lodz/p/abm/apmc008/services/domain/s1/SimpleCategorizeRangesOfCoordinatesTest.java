/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.abm.apmc008.valueobjects.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCategorizeRangesOfCoordinatesTest {
    private static Marker marker1;
    private static ArithmeticAverageCoordinateValue avg;

    @BeforeAll
    static void setUp() {
        Map<Integer, Coordinate> integerCoordinateMap = new HashMap<>();
        for (int i = 0; i < 2000; ++i) {
            integerCoordinateMap.put(i, new Coordinate(0.1 * i, i, Math.sin(Math.toRadians(i)), Math.sin(Math.toRadians(i)), Math.sin(Math.toRadians(i))));
        }
        marker1 = new NewMarkerImpl("1", "marker1", MarkerType.Marker, CoordinateType.Position,
                Optional.of(integerCoordinateMap),
                Optional.empty()
        );
        avg = new ArithmeticAverageCoordinateValue(marker1);
    }

    @Test
    void getRangesBelowAvarage() {
        CategorizeRangesOfCoordinates categorizeRangesOfCoordinates = new SimpleCategorizeRangesOfCoordinates(marker1, avg);
        StringBuilder stringBuilder;
        for (Coordinate[] coordinates : categorizeRangesOfCoordinates.getRangesBelowAvarage(CoordinateType.Position, Axis.Y)) {
            stringBuilder = new StringBuilder();
            for(int i = 0; i < coordinates.length; ++i){
                stringBuilder.append("Coordinate ");
                stringBuilder.append(i);
                stringBuilder.append(" Y value: ");
                stringBuilder.append(coordinates[i].getY());
                stringBuilder.append(" frame: ");
                stringBuilder.append(coordinates[i].getFrame());
                stringBuilder.append('\t');
            }
            System.out.println(stringBuilder);
        }
    }

    @AfterAll
    static void tearDown(){
        avg = null;
        marker1 = null;
    }
}