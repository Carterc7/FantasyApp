package com.fantasy.fantasyapi.draft;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.fantasy.fantasyapi.objectModels.AdpPlayerCSV;
import java.util.ArrayList;
import java.io.IOException;

public class CsvParser {
    public List<AdpPlayerCSV> parseCsv() {
        System.out.println("In parseCsv");
        List<AdpPlayerCSV> adpPlayerList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("adp-ppr.csv")) {
            if (inputStream == null) {
                throw new IOException("File not found in resources folder.");
            }
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> rows = reader.readAll();
            
            // Skip the header row and process the remaining rows
            for (String[] row : rows.subList(1, rows.size())) {
                if (row.length < 7) {
                    continue; // Ensure all columns are present
                }

                try {
                    int rank = Integer.parseInt(row[0]);
                    String playerName = row[1];
                    String position = row[2];
                    String positionRank = row[3]; 
                    double adpFeb4 = Double.parseDouble(row[4]);
                    double adpMar11 = Double.parseDouble(row[5]);
                    double adpChange = Double.parseDouble(row[6]);

                    adpPlayerList.add(new AdpPlayerCSV(rank, playerName, position, positionRank, adpFeb4, adpMar11, adpChange));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping row due to invalid number format: " + String.join(",", row));
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return adpPlayerList;
    }
}




