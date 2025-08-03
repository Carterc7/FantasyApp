package com.fantasy.fantasyapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.draft.CsvParser;
import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.mongoServices.EspnPlayerService;
import com.fantasy.fantasyapi.objectModels.AdpPlayerCSV;
import com.fantasy.fantasyapi.objectModels.DraftPlayer;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DraftService {

    @Autowired
    private EspnPlayerService espnPlayerService;

    /**
     * Initialize a new draft with the given settings
     * @param numOfTeams Number of teams in the draft
     * @param mockTeamName User's team name
     * @param draftPosition User's draft position (1-based)
     * @param format Draft format (ppr, standard, etc.)
     * @return DraftInitializationResponse containing all draft data
     */
    public DraftInitializationResponse initializeDraft(int numOfTeams, String mockTeamName, 
                                                     int draftPosition, String format) {
        
        // Get ADP list from CSV based on format
        List<AdpPlayerCSV> adpList = loadAdpData(format);
        
        // Get all ESPN players for additional data
        List<EspnPlayer> allEspnPlayers = espnPlayerService.findAllPlayers();
        
        // Create draft players by merging ADP and ESPN data
        List<DraftPlayer> draftPlayers = mergePlayerData(adpList, allEspnPlayers);
        
        // Create fantasy teams
        List<FantasyTeam> mockTeams = createFantasyTeams(numOfTeams, mockTeamName, draftPosition);
        
        // Initialize draft state
        String userId = String.valueOf(draftPosition - 1);
        
        return DraftInitializationResponse.builder()
            .draftPlayers(draftPlayers)
            .mockTeams(mockTeams)
            .userId(userId)
            .build();
    }

    /**
     * Load ADP data from CSV file based on format
     */
    private List<AdpPlayerCSV> loadAdpData(String format) {
        CsvParser parser = new CsvParser();
        String csvToParse = getCsvFileName(format);
        return parser.parseCsv(csvToParse);
    }

    /**
     * Get the appropriate CSV file name based on format
     */
    private String getCsvFileName(String format) {
        return switch (format.toLowerCase()) {
            case "standard" -> "no-ppr.csv";
            case "half-ppr" -> "half-ppr.csv";
            case "dynasty" -> "dynasty.csv";
            case "2qb-dynasty" -> "2qb-dynasty.csv";
            default -> "ppr.csv";
        };
    }

    /**
     * Merge ADP data with ESPN player data
     */
    private List<DraftPlayer> mergePlayerData(List<AdpPlayerCSV> adpList, List<EspnPlayer> espnPlayers) {
        // Create lookup map for ESPN players
        Map<String, EspnPlayer> espnLookup = espnPlayers.stream()
            .collect(Collectors.toMap(
                p -> normalizeName(p.getEspnName()),
                Function.identity(),
                (existing, replacement) -> existing // Keep first occurrence
            ));

        // Merge data
        return adpList.stream()
            .map(adp -> {
                String normalizedAdpName = normalizeName(adp.getName());
                EspnPlayer match = espnLookup.get(normalizedAdpName);
                
                if (match != null) {
                    return new DraftPlayer(adp, match.getEspnHeadshot(), match.getTeam());
                } else {
                    return new DraftPlayer(adp, null, null); // No matching ESPN data
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Create fantasy teams for the draft
     */
    private List<FantasyTeam> createFantasyTeams(int numOfTeams, String mockTeamName, int draftPosition) {
        List<FantasyTeam> teams = new ArrayList<>();
        
        for (int i = 0; i < numOfTeams; i++) {
            String teamID = String.valueOf(i);
            String teamName = (i == (draftPosition - 1)) ? mockTeamName : "CPU Team " + (i + 1);
            teams.add(new FantasyTeam(teamID, teamName, new ArrayList<>()));
        }
        
        return teams;
    }

    /**
     * Normalize player names for matching
     */
    private String normalizeName(String name) {
        if (name == null || name.isEmpty()) return "";

        // Lowercase and clean up the name
        String cleaned = name
            .toLowerCase()
            .replaceAll("\\b(jr|sr|ii|iii|iv|v)\\b", "") // remove suffixes
            .replaceAll("[^a-z ]", "") // remove punctuation
            .replaceAll("\\s+", " ") // normalize whitespace
            .trim();

        String[] parts = cleaned.split(" ");
        if (parts.length == 0) return "";

        // Recombine the name
        StringBuilder normalized = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            normalized.append(" ").append(parts[i]);
        }

        return normalized.toString();
    }

    /**
     * Response class for draft initialization
     */
    public static class DraftInitializationResponse {
        private final List<DraftPlayer> draftPlayers;
        private final List<FantasyTeam> mockTeams;
        private final String userId;

        private DraftInitializationResponse(Builder builder) {
            this.draftPlayers = builder.draftPlayers;
            this.mockTeams = builder.mockTeams;
            this.userId = builder.userId;
        }

        // Getters
        public List<DraftPlayer> getDraftPlayers() { return draftPlayers; }
        public List<FantasyTeam> getMockTeams() { return mockTeams; }
        public String getUserId() { return userId; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private List<DraftPlayer> draftPlayers;
            private List<FantasyTeam> mockTeams;
            private String userId;

            public Builder draftPlayers(List<DraftPlayer> draftPlayers) {
                this.draftPlayers = draftPlayers;
                return this;
            }

            public Builder mockTeams(List<FantasyTeam> mockTeams) {
                this.mockTeams = mockTeams;
                return this;
            }

            public Builder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public DraftInitializationResponse build() {
                return new DraftInitializationResponse(this);
            }
        }
    }
} 