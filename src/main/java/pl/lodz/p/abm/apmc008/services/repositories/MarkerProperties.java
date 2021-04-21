/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties("csvfile")
public class MarkerProperties {
    private final String localCsvFilePath;
    private  String leftAnkleName;
    private  String rightAnkleName;

    public MarkerProperties(
            @Value("${csvfile.path}") String localCsvFilePath,
            @Value("${leftAnkleName}") String leftAnkleName,
            @Value("${rightAnkleName}") String rightAnkleName){
        this.localCsvFilePath = localCsvFilePath;
        this.leftAnkleName = leftAnkleName;
        this.rightAnkleName = rightAnkleName;
    }

    public String getLocalCsvFilePath() {
        return localCsvFilePath;
    }

    public String getLeftAnkleName() {
        return leftAnkleName;
    }

    public String getRightAnkleName() {
        return rightAnkleName;
    }

    public String getAnkleName(String urlParam){
        if (urlParam.toLowerCase().contains("left"))
            return leftAnkleName;
        else if (urlParam.toLowerCase().contains("right"))
            return rightAnkleName;
        else throw new IllegalArgumentException(String.format("urlParam %s in getAnkleName(..)",urlParam));
    }

    public void setLeftAnkleName(String leftAnkleName) {
        this.leftAnkleName = leftAnkleName;
    }

    public void setRightAnkleName(String rightAnkleName) {
        this.rightAnkleName = rightAnkleName;
    }
}
