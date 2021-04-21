/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RangeOfCoordinates {
    final private int firstFrame, lastFrame;
    final private String markerID, markerName;
    final private CoordinateType coordinateType;
    final private List<Coordinate> coordinateList;


    public RangeOfCoordinates(int firstFrame, int lastFrame, RangeOfCoordinates range) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.coordinateType = range.getCoordinateType();
        this.coordinateList = createCopyOfCoordinateList(range);
        this.markerName = range.getMarkerName();
        this.markerID = range.markerID;
    }

    public RangeOfCoordinates(
            int firstFrame,
            int lastFrame,
            Marker marker,
            CoordinateType coordinateType
            ) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.coordinateType = coordinateType;
        this.markerName = marker.getName();
        this.markerID = marker.getId();
        this.coordinateList = createListFromMarker(marker);
    }

    private List<Coordinate> createListFromMarker(Marker marker) {
        List<Coordinate> returnList = new ArrayList<>(lastFrame-firstFrame+1);
        for (int i = firstFrame; i<=lastFrame; ++i){
            returnList.add(marker.getCoordinate(i,this.coordinateType));
        }
        return returnList;
    }

    public RangeOfCoordinates(
            int firstFrame,
            int lastFrame,
            String markerID,
            String markerName,
            CoordinateType coordinateType,
            List<Coordinate> coordinateList) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.markerID = markerID;
        this.markerName = markerName;
        this.coordinateType = coordinateType;
        this.coordinateList = coordinateList;
    }

    private List<Coordinate> createCopyOfCoordinateList(RangeOfCoordinates range) {
        final ArrayList<Coordinate> listToReturn;
        {
            if (firstFrame > lastFrame)
                throw new RuntimeException("Wrong arguments: firstFrame > lastFrame");

            List<Coordinate> tmpCoordinateList = new LinkedList<>();
            boolean foundLastFrame = false;
            for (Coordinate coordinate : range.coordinateList) {
                final int currFrame = coordinate.getFrame();
                if (currFrame >= firstFrame && currFrame <= lastFrame)
                    tmpCoordinateList.add(coordinate);
                if (currFrame == lastFrame) {
                    foundLastFrame = true;
                    break;
                }
            }

            if (!foundLastFrame) {
                throw new RuntimeException("Last frame not found");
            }

            listToReturn = new ArrayList<Coordinate>(tmpCoordinateList);
        }
        listToReturn.trimToSize();
        return listToReturn;
    }


    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    public int findIndexByFrame(int frame){
        final List<Coordinate> coordinateList = getCoordinateList();
        for (int i = 0; i< coordinateList.size(); ++i){
            if (coordinateList.get(i).getFrame() == frame)
                return i;
        }
        throw new RuntimeException("Frame not found");
    }

    public Coordinate getCoordinateByFrame(int frame){
        final List<Coordinate> coordinateList = getCoordinateList();
        for (int i = 0; i< coordinateList.size(); ++i){
            if (coordinateList.get(i).getFrame() == frame)
                return coordinateList.get(i);
        }
        throw new RuntimeException("Coordinate not found");
    }

    public int getFirstFrame() {
        return firstFrame;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public String getMarkerId() {
        return this.markerID;
    }

    public String getMarkerName(){
        return this.markerName;
    }
}
