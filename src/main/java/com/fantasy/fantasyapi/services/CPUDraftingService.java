package com.fantasy.fantasyapi.services;

import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.objectModels.DraftPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CPUDraftingService {

    private static final Map<String, Integer> POSITION_LIMITS = Map.of(
        "QB", 2,
        "TE", 2,
        "WR", 6,
        "RB", 6
    );

    /**
     * Select a player for CPU team based on current roster and draft round
     * @param availablePlayers List of available players
     * @param cpuTeam The CPU team making the selection
     * @param roundNumber Current draft round
     * @return Selected player or null if no valid selection
     */
    public DraftPlayer selectPlayerForCPU(List<DraftPlayer> availablePlayers, 
                                        FantasyTeam cpuTeam, 
                                        int roundNumber) {
        
        if (availablePlayers.isEmpty()) {
            return null;
        }

        List<DraftPlayer> cpuRoster = cpuTeam.getRoster();
        Map<String, Long> positionCounts = calculatePositionCounts(cpuRoster);
        
        long qbCount = positionCounts.getOrDefault("QB", 0L);
        long teCount = positionCounts.getOrDefault("TE", 0L);

        // After round 10, force pick missing positions if any
        if (roundNumber >= 10) {
            DraftPlayer forcedPick = selectForcedPositionPick(availablePlayers, positionCounts);
            if (forcedPick != null) {
                System.out.println("CPU forced to pick missing position " + 
                    forcedPick.getAdpPlayer().getPosition() + ": " + 
                    forcedPick.getAdpPlayer().getName());
                return forcedPick;
            }
        }

        // Apply position limits for rounds < 10
        List<DraftPlayer> filteredPlayers = filterByPositionLimits(availablePlayers, positionCounts, roundNumber);
        
        // Check if any position has fewer than 2 players (position of need)
        boolean needsPosition = positionCounts.values().stream().anyMatch(count -> count < 2);

        if (roundNumber > 4 && needsPosition) {
            DraftPlayer positionNeedPick = selectWithPositionNeedBoost(filteredPlayers, positionCounts, qbCount, teCount, roundNumber);
            if (positionNeedPick != null) {
                return positionNeedPick;
            }
        }

        // Default weighted selection
        return selectWithWeightedADP(filteredPlayers, qbCount, teCount, roundNumber);
    }

    /**
     * Calculate position counts for current roster
     */
    private Map<String, Long> calculatePositionCounts(List<DraftPlayer> roster) {
        return roster.stream()
            .collect(Collectors.groupingBy(
                p -> p.getAdpPlayer().getPosition(), 
                Collectors.counting()
            ));
    }

    /**
     * Select player for forced position pick (round 10+)
     */
    private DraftPlayer selectForcedPositionPick(List<DraftPlayer> availablePlayers, 
                                               Map<String, Long> positionCounts) {
        
        // Find positions missing from roster
        List<String> neededPositions = new ArrayList<>();
        for (String pos : POSITION_LIMITS.keySet()) {
            if (positionCounts.getOrDefault(pos, 0L) == 0) {
                neededPositions.add(pos);
            }
        }

        if (!neededPositions.isEmpty()) {
            // Filter for players at needed positions
            List<DraftPlayer> neededPlayers = availablePlayers.stream()
                .filter(p -> neededPositions.contains(p.getAdpPlayer().getPosition()))
                .collect(Collectors.toList());

            if (!neededPlayers.isEmpty()) {
                return neededPlayers.get(0); // Pick top player from needed positions
            }
        }
        
        return null;
    }

    /**
     * Filter players by position limits for early rounds
     */
    private List<DraftPlayer> filterByPositionLimits(List<DraftPlayer> availablePlayers, 
                                                   Map<String, Long> positionCounts, 
                                                   int roundNumber) {
        
        if (roundNumber >= 10) {
            return availablePlayers; // No limits after round 10
        }

        List<DraftPlayer> filtered = availablePlayers.stream()
            .filter(p -> {
                String pos = p.getAdpPlayer().getPosition();
                Integer limit = POSITION_LIMITS.get(pos);
                if (limit == null) {
                    return true; // positions without limits allowed always
                }
                long count = positionCounts.getOrDefault(pos, 0L);
                return count < limit;
            })
            .collect(Collectors.toList());

        // If all limits reached, fallback to original list
        return filtered.isEmpty() ? availablePlayers : filtered;
    }

    /**
     * Select player with position need boost (rounds 5+)
     */
    private DraftPlayer selectWithPositionNeedBoost(List<DraftPlayer> availablePlayers, 
                                                  Map<String, Long> positionCounts,
                                                  long qbCount, 
                                                  long teCount, 
                                                  int roundNumber) {
        
        int topN = Math.min(8, availablePlayers.size());
        List<DraftPlayer> topPlayers = availablePlayers.subList(0, topN);

        int[] weights = new int[topPlayers.size()];

        for (int i = 0; i < topPlayers.size(); i++) {
            DraftPlayer dp = topPlayers.get(i);
            String pos = dp.getAdpPlayer().getPosition();

            int rankFactor = topPlayers.size() - i;
            // Cubed weighting to more heavily favor top ADP players
            int baseWeight = rankFactor * rankFactor * rankFactor;

            // Boost weight if position is a need (less than 2 players at that position)
            if (positionCounts.getOrDefault(pos, 0L) < 2) {
                baseWeight *= 7; // Big boost for position of need
            }

            // Penalize duplicate QBs/TEs early
            if (roundNumber <= 8) {
                if ("QB".equalsIgnoreCase(pos) && qbCount > 0) {
                    baseWeight /= 2;
                } else if ("TE".equalsIgnoreCase(pos) && teCount > 0) {
                    baseWeight /= 2;
                }
            }

            weights[i] = Math.max(baseWeight, 1);
        }

        return selectWithWeights(topPlayers, weights, "CPU selected with position of need boost");
    }

    /**
     * Select player with weighted ADP (default selection)
     */
    private DraftPlayer selectWithWeightedADP(List<DraftPlayer> availablePlayers, 
                                            long qbCount, 
                                            long teCount, 
                                            int roundNumber) {
        
        int topN = Math.min(8, availablePlayers.size());
        List<DraftPlayer> topPlayers = availablePlayers.subList(0, topN);

        // Filter out QBs if already have one in early rounds
        if (roundNumber <= 4 && qbCount > 0) {
            List<DraftPlayer> filtered = topPlayers.stream()
                .filter(p -> !"QB".equalsIgnoreCase(p.getAdpPlayer().getPosition()))
                .collect(Collectors.toList());
            if (!filtered.isEmpty()) {
                topPlayers = filtered;
            }
        }

        int weightedCount = topPlayers.size();
        int[] weights = new int[weightedCount];

        for (int i = 0; i < weightedCount; i++) {
            DraftPlayer dp = topPlayers.get(i);
            String pos = dp.getAdpPlayer().getPosition();

            int rankFactor = weightedCount - i;
            // Cubed weighting
            int weight = rankFactor * rankFactor * rankFactor;

            // Penalize duplicate QBs/TEs in early rounds
            if (roundNumber <= 6) {
                if ("QB".equalsIgnoreCase(pos) && qbCount > 0) {
                    weight = (int) (weight * 0.2);
                } else if ("TE".equalsIgnoreCase(pos) && teCount > 0) {
                    weight = (int) (weight * 0.5);
                }
            }

            weights[i] = Math.max(weight, 1);
        }

        return selectWithWeights(topPlayers, weights, "CPU selected (ADP-weighted)");
    }

    /**
     * Select player using weighted random selection
     */
    private DraftPlayer selectWithWeights(List<DraftPlayer> players, int[] weights, String logMessage) {
        int totalWeight = IntStream.of(weights).sum();
        int rand = new Random().nextInt(totalWeight);

        int cumulative = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulative += weights[i];
            if (rand < cumulative) {
                DraftPlayer selected = players.get(i);
                System.out.println(logMessage + ": " + 
                    selected.getAdpPlayer().getName() + " (" + 
                    selected.getAdpPlayer().getPosition() + ")");
                return selected;
            }
        }
        
        // Fallback to first player
        return players.get(0);
    }
} 