/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.repositories;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.abm.apmc008.valueobjects.CoordinateType;
import pl.lodz.p.abm.apmc008.valueobjects.Marker;
import pl.lodz.p.abm.apmc008.valueobjects.MarkerType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ImportCsvDataTest {
    private static File csvFile;
    private static Path path ;
    private static String name ;
    private static byte[] content;
    private final String contentType = "text/csv";
    private MultipartFile mfile;

    @BeforeAll
    public static void setUpAll() throws IOException{
        csvFile = new File("/home/user1/Dokumenty/pracaInz/AnalizaChodu/e01_p_global_Markers.csv");
        //        csvFile = new File("/home/user1/Dokumenty/pracaInz/pracainz/take_001 processed.csv");
        path = csvFile.toPath();
        name = csvFile.getName();
        content = Files.readAllBytes(path);
    }

    @BeforeEach
    public void setUp() {
        this.mfile = new MockMultipartFile(name, name, contentType, content);
    }

//    //TODO
//    @Test
//    public void getMarkerListMetadata() throws IOException, SQLException, ClassNotFoundException, NoSuchFieldException {
//        ImportData importData = new ImportMultipartFileData(mfile);
//        List<Marker> markerList = importData.getMarkerList();
//    }

    @Test
    public void getMarkerListContainsMarkerNames() throws IOException, SQLException, ClassNotFoundException, NoSuchFieldException {
        ImportData importData = new ImportMultipartFileData(mfile);
        assertEquals(2,importData.getMarkerList().stream()
                .filter(marker -> marker.getName().equals("Skeleton2:WaistLFront") || marker.getName().equals("Skeleton2:LAnkleOut"))
                .count());
    }

    @Test
    public void getMarkerListAnkleHasRightData() throws IOException, SQLException, ClassNotFoundException, NoSuchFieldException {
        ImportData importData = new ImportMultipartFileData(mfile);
        var markerOptional = new ImportMultipartFileData(mfile).getMarkerList().stream()
                .filter(marker -> marker.getName().equals("Skeleton2:LAnkleOut"))
                .findFirst();
        assertDoesNotThrow((ThrowingSupplier<Marker>) markerOptional::orElseThrow);
        Marker ankleMarker = markerOptional.orElseThrow();
        assertEquals(0.222152,ankleMarker.getCoordinate(6212, CoordinateType.Position).getY(),0.001);
        assertEquals(0.081759,ankleMarker.getCoordinate(21564, CoordinateType.Position).getY(),0.001);
        assertEquals(0.093715,ankleMarker.getCoordinate(21569, CoordinateType.Position).getX(),0.001);
        assertEquals(0.883879,ankleMarker.getCoordinate(32425, CoordinateType.Position).getZ(),0.001);
    }

    @Test
    public void getMarkerAnkleHasRightData() throws IOException, SQLException, ClassNotFoundException, NoSuchFieldException {
        ImportData importData = new ImportMultipartFileData(mfile);
        Marker ankleMarker = importData.getMarker("Skeleton2:LAnkleOut",CoordinateType.Position, MarkerType.Marker);
        assertNotNull(ankleMarker);
        assertEquals(0.222152,ankleMarker.getCoordinate(6212, CoordinateType.Position).getY(),0.001);
        assertEquals(0.081759,ankleMarker.getCoordinate(21564, CoordinateType.Position).getY(),0.001);
        assertEquals(0.093715,ankleMarker.getCoordinate(21569, CoordinateType.Position).getX(),0.001);
        assertEquals(0.883879,ankleMarker.getCoordinate(32425, CoordinateType.Position).getZ(),0.001);
    }

    @Test
    public void getMarkerListHeaders() throws IOException {
        final List<Marker> collected = new ImportMultipartFileData(mfile).getMarkerList().stream()
                .filter(marker -> marker.getName().equals("Skeleton2:WaistLFront") || marker.getName().equals("Skeleton2:LAnkleOut"))
                .collect(Collectors.toList());

        final Marker waistLFrontMarker = collected.stream().filter(marker -> marker.getName().equals("Skeleton2:WaistLFront")).findAny().orElseThrow();
        final Marker lAnkleMarker = collected.stream().filter(marker -> marker.getName().equals("Skeleton2:LAnkleOut")).findAny().orElseThrow();
        assertEquals("8392B20CF49E3572200000005",waistLFrontMarker.getId());
        assertEquals("8392B20CF49E3572200000021",lAnkleMarker.getId());
        assertEquals(CoordinateType.Position,waistLFrontMarker.getCoordinateType());
        assertEquals(CoordinateType.Position,lAnkleMarker.getCoordinateType())  ;
    }

    @AfterEach
    public void tearDown(){
        this.mfile = null;
    }

    @AfterAll
    public static void tearDownAll(){
        content = null;
        name = null;
        path = null;
        csvFile = null;
    }
}