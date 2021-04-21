/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2019 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.domain.s1;

import org.junit.Assert;
import pl.lodz.p.abm.apmc008.services.domain.s1.ArithmeticAverageCoordinateValue;
import pl.lodz.p.abm.apmc008.valueobjects.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArithmeticAverageCoordinateValueTest {

    @org.junit.jupiter.api.Test
    public void getAverageCoordinateValue() {
        Map<Integer, Coordinate> integerCoordinateMap = new HashMap<>();
        integerCoordinateMap.put(1, new Coordinate(0, 1, 1, 1, 1));
        integerCoordinateMap.put(2, new Coordinate(3, 2, 2, 2, 2));
        integerCoordinateMap.put(3, new Coordinate(6, 3, 3, 3, 3));
        final ArithmeticAverageCoordinateValue test = new ArithmeticAverageCoordinateValue(new NewMarkerImpl("00", "test", MarkerType.Marker, CoordinateType.Position, Optional.of(integerCoordinateMap), Optional.empty()));
        final Coordinate coordinate = test.getAverageCoordinateValue(CoordinateType.Position);
        Assert.assertEquals(2, coordinate.getFrame());
        Assert.assertEquals(2, coordinate.getX(), 0.001);
        Assert.assertEquals(2, coordinate.getY(), 0.001);
        Assert.assertEquals(2, coordinate.getZ(), 0.001);
        Assert.assertEquals(3, coordinate.getTime(), 0.001);
    }
}