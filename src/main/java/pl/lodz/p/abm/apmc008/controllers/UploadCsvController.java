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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerProperties;
import pl.lodz.p.abm.apmc008.services.repositories.MarkerService;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.MarkerType;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "/upload_source_csv.html")
public class UploadCsvController {
    private final MarkerService markerService;
    private final MarkerProperties markerProperties;

    @Autowired
    public UploadCsvController(MarkerService markerService, MarkerProperties markerProperties){
        this.markerService = markerService;
        this.markerProperties = markerProperties;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getUploadCsvForm(){
        return "upload_source_csv";
    }


    @RequestMapping(method = RequestMethod.POST, params = {"lAnkleMarkerName","rAnkleMarkerName"})
    public String getUploadCsvReceived_marker(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lAnkleMarkerName") String lAnkleMarkerName,
            @RequestParam("rAnkleMarkerName") String rAnkleMarkerName,
            @RequestParam(defaultValue = "false") boolean loadAll,
            Model model
    ) throws IOException, SQLException {
        if (loadAll) {
            if (!markerService.loadMarkerList(file))
                throw new RuntimeException("Markers not added correctly");
        } else {
            markerProperties.setLeftAnkleName(lAnkleMarkerName);
            markerProperties.setRightAnkleName(rAnkleMarkerName);
            if (!markerService.addMarker(file, lAnkleMarkerName, CoordinateType.Position, MarkerType.Marker))
                throw new RuntimeException(String.format("Marker %s not added correctly",lAnkleMarkerName));
            if (!markerService.addMarker(file, rAnkleMarkerName, CoordinateType.Position, MarkerType.Marker))
                throw new RuntimeException(String.format("Marker %s not added correctly",rAnkleMarkerName));
        }

        model.addAttribute("markerNamesAndIDs",markerService.getMarkerNamesAndIDs());
        final String[] ankleNames = {markerService.getMarkerProperties().getLeftAnkleName(), markerService.getMarkerProperties().getRightAnkleName()};
        model.addAttribute("ankleMarkerNames",ankleNames);
        return "load_summary";
    }

}
