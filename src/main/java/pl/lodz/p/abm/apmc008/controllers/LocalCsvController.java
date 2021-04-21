/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerService;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "/load_local_csv.html")
public class LocalCsvController {
    private final MarkerService markerService;

    @Autowired
    public LocalCsvController(MarkerService markerService){
        this.markerService = markerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String loadCsv(Model model) throws IOException, SQLException {
        markerService.loadMarkerListFromLocalCsv();
        model.addAttribute("markerNamesAndIDs",markerService.getMarkerNamesAndIDs());
        final String[] ankleNames = {markerService.getMarkerProperties().getLeftAnkleName(), markerService.getMarkerProperties().getRightAnkleName()};
        model.addAttribute("ankleMarkerNames",ankleNames);
        return "load_summary";
    }

}
