/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.services.repositories;

import java.io.*;

public final class ImportCsvFileData extends AbstractImportFileData {
    private final File csvFile;

    public ImportCsvFileData(File csvFile){
        this.csvFile = csvFile;
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return new FileInputStream(csvFile);
    }
}
