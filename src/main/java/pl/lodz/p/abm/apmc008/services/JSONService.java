/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.lodz.p.abm.apmc008.valueobjects.Axis;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;

public interface JSONService {
    JSONObject jsonForMarkerAxis(@NotNull String name, @NotNull Axis axis, double startTime, double endTime, @NotNull PlotlyJSONService.TraceType traceType, @NotNull PlotlyJSONService.TraceMode traceMode) throws SQLException, IOException, ClassNotFoundException;
    JSONObject jsonForMarkerAxis(@NotNull String name, @NotNull Axis axis, double startTime, PlotlyJSONService.TraceType traceType, PlotlyJSONService.TraceMode traceMode) throws SQLException, IOException, ClassNotFoundException;
}
