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

    /**
     * Parses a CSV file from the resources folder to a list of players.
     * 
     * @param fileName The name of the CSV file to parse.
     * @return A list of AdpPlayerCSV objects parsed from the file.
     */
    public List<AdpPlayerCSV> parseCsv(String fileName) {
        System.out.println("In parseCsv for file: " + fileName);
        List<AdpPlayerCSV> adpPlayerList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found in resources folder: " + fileName);
            }
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> rows = reader.readAll();
            reader.close();

            for (String[] row : rows.subList(1, rows.size())) { // Skip header row
                if (row.length < 7)
                    continue; // Skip rows that don't have enough data

                try {
                    // Clean up the row values (remove quotes and trim spaces)
                    String name = row[0].replaceAll("\"", "").trim(); // Name
                    String position = row[1].replaceAll("\"", "").trim(); // Position
                    String overallRankStr = row[2].replaceAll("\"", "").trim(); // Overall Rank
                    String positionalRankStr = row[3].replaceAll("\"", "").trim(); // Positional Rank
                    String tierStr = row[4].replaceAll("\"", "").trim(); // Tier
                    String attribute = row[5].replaceAll("\"", "").trim(); // Attribute
                    String auctionValueStr = row[6].replaceAll("\"", "").trim(); // Auction Value

                    // Parse the numerical values
                    double overallRank = Double.parseDouble(overallRankStr);
                    double positionalRank = Double.parseDouble(positionalRankStr);
                    double tier = Double.parseDouble(tierStr);
                    double auctionValue = Double.parseDouble(auctionValueStr);

                    // Create and add the AdpPlayerCSV object
                    adpPlayerList.add(new AdpPlayerCSV(name, position, overallRank, positionalRank, tier, attribute,
                            auctionValue));

                } catch (NumberFormatException e) {
                    // Catch any invalid number format and log the error
                    System.err.println("Skipping row due to invalid format: " + String.join(",", row));
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("Error: could not parse " + fileName);
            e.printStackTrace();
        }
        return adpPlayerList;
    }
}
