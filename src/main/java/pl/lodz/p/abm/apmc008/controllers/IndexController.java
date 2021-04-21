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
import org.springframework.web.bind.annotation.GetMapping;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerService;
import pl.lodz.p.abm.apmc008.valueobjects.Marker;

import java.util.List;

@Controller
public class IndexController {
    private final MarkerService markerService;

    @Autowired
    public IndexController(MarkerService markerService) {
        this.markerService = markerService;
    }

    @GetMapping({"/index","/index.htm","/index.html"})
    public String getIndexPage(Model model){
        model.addAttribute("markerListPopulated",markerService.isMarkerListPopulated());
        return "index";
    }
}
