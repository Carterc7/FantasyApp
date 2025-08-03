package com.fantasy.fantasyapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.apiCalls.GetPlayerDetails;
import com.fantasy.fantasyapi.mongoServices.EspnPlayerService;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.objectModels.PlayerDetails;
import com.fantasy.fantasyapi.objectModels.PlayerGameStats;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerDetailsService {

    @Autowired
    private EspnPlayerService espnPlayerService;

    /**
     * Get player details and calculate fantasy points
     * @param espnName The ESPN name of the player
     * @return PlayerDetailsResponse containing all processed data
     * @throws IOException If API call fails
     * @throws InterruptedException If API call is interrupted
     */
    public PlayerDetailsResponse getPlayerDetails(String espnName) throws IOException, InterruptedException {
        Optional<EspnPlayer> player = espnPlayerService.findPlayerByEspnName(espnName);
        
        if (player.isEmpty()) {
            return null;
        }

        EspnPlayer espnPlayer = player.get();
        GetPlayerDetails apiCaller = new GetPlayerDetails();
        PlayerDetails playerDetails = apiCaller.getPlayerDetails(Integer.parseInt(espnPlayer.getEspnID()));

        // Clean stat strings
        cleanStatStrings(playerDetails);

        // Calculate fantasy points
        double totalFantasyPoints = calculateFantasyPoints(playerDetails.getStats());
        
        // Calculate rush average
        double rushAverage = calculateRushAverage(playerDetails.getStats());
        
        // Sort game stats by date (most recent first)
        List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList = sortGameStats(playerDetails.getGameStats());
        
        // Parse name parts
        String[] nameParts = espnName.trim().split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length >= 2 ? 
            String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length)) : "";

        return PlayerDetailsResponse.builder()
            .playerDetails(playerDetails)
            .fantasyPoints(formatFantasyPoints(totalFantasyPoints))
            .rushAverage(formatRushAverage(rushAverage))
            .gameStatsList(gameStatsList)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    }

    /**
     * Calculate total fantasy points from player stats
     */
    private double calculateFantasyPoints(PlayerDetails.Stats stats) {
        if (stats == null) return 0.0;

        double totalPoints = 0.0;
        
        try {
            // Rushing points
            if (stats.getRushing() != null) {
                int rushYds = parseOrZero(stats.getRushing().getRushYds());
                int rushTD = parseOrZero(stats.getRushing().getRushTD());
                totalPoints += (rushYds * 0.1) + (rushTD * 6);
            }

            // Receiving points (PPR format)
            if (stats.getReceiving() != null) {
                int recYds = parseOrZero(stats.getReceiving().getRecYds());
                int recTD = parseOrZero(stats.getReceiving().getRecTD());
                int receptions = parseOrZero(stats.getReceiving().getReceptions());
                totalPoints += (receptions * 1.0) + (recYds * 0.1) + (recTD * 6);
            }

            // Passing points
            if (stats.getPassing() != null) {
                int passYds = parseOrZero(stats.getPassing().getPassYds());
                int passTD = parseOrZero(stats.getPassing().getPassTD());
                totalPoints += (passYds * 0.04) + (passTD * 4);
            }
        } catch (NumberFormatException e) {
            // Log error but continue with 0 points
            System.err.println("Error parsing stats for fantasy points calculation: " + e.getMessage());
        }

        return totalPoints;
    }

    /**
     * Calculate rushing average yards per carry
     */
    private double calculateRushAverage(PlayerDetails.Stats stats) {
        if (stats == null || stats.getRushing() == null) return 0.0;

        PlayerDetails.Rushing rushing = stats.getRushing();
        
        if (rushing.getRushYds() == null || rushing.getCarries() == null ||
            rushing.getRushYds().isEmpty() || rushing.getCarries().isEmpty()) {
            return 0.0;
        }

        try {
            int yards = Integer.parseInt(rushing.getRushYds());
            int attempts = Integer.parseInt(rushing.getCarries());
            return attempts > 0 ? (double) yards / attempts : 0.0;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing rushing stats: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Sort game stats by date (most recent first)
     */
    private List<Map.Entry<String, PlayerGameStats.GameData>> sortGameStats(
            Map<String, PlayerGameStats.GameData> gameStatsMap) {
        
        List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList = 
            new ArrayList<>(gameStatsMap.entrySet());

        gameStatsList.sort((entry1, entry2) -> {
            String dateStr1 = entry1.getKey().substring(0, 8);
            String dateStr2 = entry2.getKey().substring(0, 8);
            return dateStr2.compareTo(dateStr1); // Most recent first
        });

        return gameStatsList;
    }

    /**
     * Clean stat strings by removing unnecessary decimal places
     */
    private void cleanStatStrings(PlayerDetails playerDetails) {
        if (playerDetails.getStats() == null) return;

        PlayerDetails.Stats stats = playerDetails.getStats();

        // Clean passing stats
        if (stats.getPassing() != null) {
            stats.getPassing().setPassAttempts(clean(stats.getPassing().getPassAttempts()));
            stats.getPassing().setPassCompletions(clean(stats.getPassing().getPassCompletions()));
            stats.getPassing().setPassYds(clean(stats.getPassing().getPassYds()));
            stats.getPassing().setPassTD(clean(stats.getPassing().getPassTD()));
            stats.getPassing().setIntVal(clean(stats.getPassing().getIntVal()));
        }

        // Clean rushing stats
        if (stats.getRushing() != null) {
            stats.getRushing().setCarries(clean(stats.getRushing().getCarries()));
            stats.getRushing().setRushYds(clean(stats.getRushing().getRushYds()));
            stats.getRushing().setRushTD(clean(stats.getRushing().getRushTD()));
        }

        // Clean receiving stats
        if (stats.getReceiving() != null) {
            stats.getReceiving().setReceptions(clean(stats.getReceiving().getReceptions()));
            stats.getReceiving().setRecYds(clean(stats.getReceiving().getRecYds()));
            stats.getReceiving().setRecTD(clean(stats.getReceiving().getRecTD()));
            stats.getReceiving().setTargets(clean(stats.getReceiving().getTargets()));
        }
    }

    /**
     * Clean a stat string by removing unnecessary decimal places
     */
    private String clean(String stat) {
        if (stat == null) return null;
        if (stat.contains(".")) {
            String[] parts = stat.split("\\.");
            if (parts.length > 1 && parts[1].equals("0")) {
                return parts[0]; // "1.0" -> "1"
            }
        }
        return stat;
    }

    /**
     * Parse string to integer, return 0 if parsing fails
     */
    private int parseOrZero(String val) {
        if (val == null || val.isEmpty()) return 0;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Format fantasy points to one decimal place
     */
    private String formatFantasyPoints(double points) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(points);
    }

    /**
     * Format rush average to one decimal place or "N/A"
     */
    private String formatRushAverage(double average) {
        if (average > 0) {
            DecimalFormat df = new DecimalFormat("#.#");
            return df.format(average);
        }
        return "N/A";
    }

    /**
     * Response class to hold all processed player details data
     */
    public static class PlayerDetailsResponse {
        private final PlayerDetails playerDetails;
        private final String fantasyPoints;
        private final String rushAverage;
        private final List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList;
        private final String firstName;
        private final String lastName;

        private PlayerDetailsResponse(Builder builder) {
            this.playerDetails = builder.playerDetails;
            this.fantasyPoints = builder.fantasyPoints;
            this.rushAverage = builder.rushAverage;
            this.gameStatsList = builder.gameStatsList;
            this.firstName = builder.firstName;
            this.lastName = builder.lastName;
        }

        // Getters
        public PlayerDetails getPlayerDetails() { return playerDetails; }
        public String getFantasyPoints() { return fantasyPoints; }
        public String getRushAverage() { return rushAverage; }
        public List<Map.Entry<String, PlayerGameStats.GameData>> getGameStatsList() { return gameStatsList; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private PlayerDetails playerDetails;
            private String fantasyPoints;
            private String rushAverage;
            private List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList;
            private String firstName;
            private String lastName;

            public Builder playerDetails(PlayerDetails playerDetails) {
                this.playerDetails = playerDetails;
                return this;
            }

            public Builder fantasyPoints(String fantasyPoints) {
                this.fantasyPoints = fantasyPoints;
                return this;
            }

            public Builder rushAverage(String rushAverage) {
                this.rushAverage = rushAverage;
                return this;
            }

            public Builder gameStatsList(List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList) {
                this.gameStatsList = gameStatsList;
                return this;
            }

            public Builder firstName(String firstName) {
                this.firstName = firstName;
                return this;
            }

            public Builder lastName(String lastName) {
                this.lastName = lastName;
                return this;
            }

            public PlayerDetailsResponse build() {
                return new PlayerDetailsResponse(this);
            }
        }
    }
} 