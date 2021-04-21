/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;

public class FramesNotFoundException extends RuntimeException {
    private final boolean firstFrame, lastFrame;

    public FramesNotFoundException(boolean firstFrame, boolean lastFrame) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
    }

    public boolean isFirstFrame() {
        return firstFrame;
    }

    public boolean isLastFrame() {
        return lastFrame;
    }
}
