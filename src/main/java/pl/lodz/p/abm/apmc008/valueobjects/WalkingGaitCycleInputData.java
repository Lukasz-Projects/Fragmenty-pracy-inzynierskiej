/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class WalkingGaitCycleInputData {
    private final int firstFrame, lastFrame;
    private final Marker toeOut, ankle, knee, waistF, waistB, shoulderTop;

    public WalkingGaitCycleInputData(int firstFrame, int lastFrame, Marker toeOut, Marker ankle, Marker knee, Marker waistF, Marker waistB, Marker shoulderTop) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.toeOut = toeOut;
        this.ankle = ankle;
        this.knee = knee;
        this.waistF = waistF;
        this.waistB = waistB;
        this.shoulderTop = shoulderTop;
    }

    public WalkingGaitCycleInputData(Marker toeOut, Marker ankle, Marker knee, Marker waistF, Marker waistB, Marker shoulderTop, CoordinateType coordinateType) {
        this.toeOut = toeOut;
        this.ankle = ankle;
        this.knee = knee;
        this.waistF = waistF;
        this.waistB = waistB;
        this.shoulderTop = shoulderTop;

        int[] firstFrames = new int[]{
                toeOut.getFirstFrameNumber(coordinateType),
                ankle.getFirstFrameNumber(coordinateType),
                knee.getFirstFrameNumber(coordinateType),
                waistF.getFirstFrameNumber(coordinateType),
                waistB.getFirstFrameNumber(coordinateType),
                shoulderTop.getFirstFrameNumber(coordinateType)
        };

        int[] lastFrames = new int[]{
                toeOut.getLastFrameNumber(coordinateType),
                ankle.getLastFrameNumber(coordinateType),
                knee.getLastFrameNumber(coordinateType),
                waistF.getLastFrameNumber(coordinateType),
                waistB.getLastFrameNumber(coordinateType),
                shoulderTop.getLastFrameNumber(coordinateType)
        };

        int firstFrame = firstFrames[0], lastFrame = lastFrames[0];
        for (int i = 1; i<firstFrames.length; ++i){
            if (firstFrames[i] < firstFrame)
                firstFrame = firstFrames[i];
            if (lastFrames[i]>lastFrame)
                lastFrame = lastFrames[i];
        }

        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
    }

    public WalkingGaitCycleInputData(int firstFrame, int lastFrame, WalkingGaitCycleInputData inputData){
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
        this.toeOut = inputData.getToeOut();
        this.ankle = inputData.getAnkle();
        this.knee = inputData.getKnee();
        this.waistF = inputData.getWaistF();
        this.waistB = inputData.getWaistB();
        this.shoulderTop = inputData.getShoulderTop();
    }

    public int getFirstFrame() {
        return firstFrame;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public Marker getToeOut() {
        return toeOut;
    }

    public Marker getAnkle() {
        return ankle;
    }

    public Marker getKnee() {
        return knee;
    }

    public Marker getWaistF() {
        return waistF;
    }

    public Marker getWaistB() {
        return waistB;
    }

    public Marker getShoulderTop() {
        return shoulderTop;
    }
}
