/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.lodz.p.abm.apmc008.services.domain.MarkerHelper;
import pl.lodz.p.abm.apmc008.valueobjects.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CropDataForGaitCyclesTest {
    private static Marker marker1;
    private static ArithmeticAverageCoordinateValue avg;

    @BeforeAll
    static void setUp(){
        marker1 = MarkerHelper.createStubSinuses(2000,"marker1","1");
        avg = new ArithmeticAverageCoordinateValue(marker1);
    }

    @Test
    void testCyclesNumber(){
        final CropDataForGaitCycles cropDataForGaitCycles = new CropDataForGaitCycles(marker1, avg,Axis.Y);
        assertEquals(5, cropDataForGaitCycles.getListOfFramesForCycle().size());
    }

    @Test
    void testReturnedValues(){
        final Integer[] integers = new CropDataForGaitCycles(marker1, avg, Axis.Y).getListOfFramesForCycle().get(0);
        final double avg = new ArithmeticAverageCoordinateValue(marker1).getAverageCoordinateValue(CoordinateType.Position).getX();
        assertTrue(marker1.getCoordinate(integers[0]-1, CoordinateType.Position).get(Axis.Y) >= avg);
        assertTrue(marker1.getCoordinate(integers[0], CoordinateType.Position).get(Axis.Y) < avg);
        assertTrue(marker1.getCoordinate(integers[1]-1, CoordinateType.Position).get(Axis.Y) <= avg);
        assertTrue(marker1.getCoordinate(integers[1], CoordinateType.Position).get(Axis.Y) > avg);
    }

    @Test
    void printReturnedValues(){
        Map<Integer, Coordinate> intCoorMap = new HashMap<>();
        for (int i = 0; i < 2000; ++i) {
            double sinValue = Math.sin(Math.toRadians(i));
            if (sinValue < -0.9)
                sinValue = -0.9;
            intCoorMap.put(i, new Coordinate(0.1 * i, i, sinValue, sinValue, sinValue));
        }
        var marker2 = new NewMarkerImpl("2", "marker2", MarkerType.Marker, CoordinateType.Position,
                Optional.of(intCoorMap),
                Optional.empty()
        );
        var avg2 = new ArithmeticAverageCoordinateValue(marker2);
        for (Integer[] integers : new CropDataForGaitCycles(marker2, avg2, Axis.Y).getListOfFramesForCycle()) {
            System.out.println(integers[0] + " " + integers[1]);
        }
    }

    @AfterAll
    static void tearDown(){
        avg = null;
        marker1 = null;
    }
}