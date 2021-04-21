/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.Marker;
import pl.lodz.p.abm.apmc008.valueobjects.MarkerType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarkerService {
//    private final ImportData importCsvData;
    private final List<Marker> markerList;
    private final MarkerProperties markerProperties;

    @Autowired
    public MarkerService(MarkerProperties markerProperties) {
        this.markerProperties = markerProperties;
        this.markerList = new LinkedList<>();
    }

    //  return true if added
    private boolean addMarker(ImportData importData, String name, CoordinateType coordinateType, MarkerType markerType) throws IOException {
        markerList.removeIf(marker -> marker.getName().equals(name));
        return markerList.add(importData.getMarker(name, coordinateType, markerType));
    }


    //  return true if added
    public boolean addMarkerFromLocalCsv(String name, CoordinateType coordinateType, MarkerType markerType) throws IOException {
        return addMarker(
                new ImportCsvFileData(new File(markerProperties.getLocalCsvFilePath())),
                name,
                coordinateType,
                markerType);
    }

    //  return true if added
    public boolean addMarker(MultipartFile multipartFile, String name, CoordinateType coordinateType, MarkerType markerType) throws IOException {
        return addMarker(
                new ImportMultipartFileData(multipartFile),
                name,
                coordinateType,
                markerType);
    }

    //  return true if added
    public boolean loadMarkerListFromLocalCsv() throws IOException, SQLException {
        return loadMarkerList(new ImportCsvFileData(new File(markerProperties.getLocalCsvFilePath())));
    }

    //  return true if added
    public boolean loadMarkerList(MultipartFile multipartFile) throws IOException, SQLException {
        return loadMarkerList(new ImportMultipartFileData(multipartFile));
    }

    private boolean loadMarkerList(ImportData importData) throws SQLException, IOException {
        markerList.clear();
        final boolean ret = markerList.addAll(importData.getMarkerList());
        Thread.yield();
        System.gc();
        return ret;
    }

    public MarkerProperties getMarkerProperties() {
        return markerProperties;
    }

    public List<String[]> getMarkerNamesAndIDs(){
        return   markerList.stream()
                .map(marker -> new String[]{marker.getName(),marker.getId()})
                .collect(Collectors.toList());
    }


    public List<Marker> getMarkerList() {
        return Collections.unmodifiableList(markerList);
    }

    public Marker getMarker(String name, CoordinateType coordinateType, MarkerType markerType) throws SQLException, IOException {
        return markerList.stream()
                .filter(marker -> marker.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private boolean isMarkerLoaded(String name){
        return markerList.stream()
                .anyMatch(marker -> marker.getName().equals(name));
    }

    public boolean isMarkerListPopulated(){
        boolean populated = (markerList != null) && (markerList.size() > 0);
        return populated && isMarkerLoaded(markerProperties.getRightAnkleName()) && isMarkerLoaded(markerProperties.getLeftAnkleName());
    }
}
