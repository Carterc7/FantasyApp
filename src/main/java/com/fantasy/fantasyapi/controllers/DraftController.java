package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fantasy.fantasyapi.apiCalls.GetPlayerDetails;
import com.fantasy.fantasyapi.draft.CsvParser;
import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.EspnPlayerService;
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.objectModels.AdpPlayerCSV;
import com.fantasy.fantasyapi.objectModels.DraftPlayer;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.objectModels.PlayerDetails;
import com.fantasy.fantasyapi.objectModels.PlayerGameStats;
import com.fantasy.fantasyapi.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

// SessionAttributes (global variables that are initialized in showDraftBoard and used through the draft)
@Controller
@SessionAttributes({ "adpList", "mockTeams", "currentTeamId", "roundNumber", "isReversed" })
public class DraftController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    // database players with values like espnHeadshot and team
    @Autowired
    private EspnPlayerService espnPlayerService;

    @PostMapping("/draft")
    public String showDraftBoard(
            @RequestParam("numOfTeams") int numOfTeams,
            @RequestParam("mockTeamName") String mockTeamName,
            @RequestParam("draftPosition") int draftPosition,
            @RequestParam("numOfRounds") int numOfRounds,
            @RequestParam("format") String format,
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        // === Add cache control headers to prevent browser back-button caching ===
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // Clear any previous draft session attributes first
        try {
            session.removeAttribute("mockTeams");
            session.removeAttribute("adpList");
            session.removeAttribute("pickedPlayers");
            System.out.println("Previous draft session attributes cleared.");
        } catch (Exception e) {
            System.err.println("Error clearing draft session attributes: " + e.getMessage());
        }
        // Debugging
        System.out.println("== GET /draft ==");
        int numOfSelections = numOfRounds * numOfTeams;

        // Get ADP list from CSV
        CsvParser parser = new CsvParser();
        String csvToParse;
        switch (format.toLowerCase()) {
            case "standard":
                csvToParse = "no-ppr.csv";
                break;
            case "half-ppr":
                csvToParse = "half-ppr.csv";
                break;
            case "dynasty":
                csvToParse = "dynasty.csv";
                break;
            case "2qb-dynasty":
                csvToParse = "2qb-dynasty.csv";
                break;
            default:
                csvToParse = "ppr.csv";
                break;
        }
        List<AdpPlayerCSV> adpList = new ArrayList<>();
        adpList.clear();
        adpList = parser.parseCsv(csvToParse);
        List<EspnPlayer> allEspnPlayers = espnPlayerService.findAllPlayers();

        // Normalize all ESPNs names as keys
        Map<String, EspnPlayer> espnLookup = allEspnPlayers.stream()
                .collect(Collectors.toMap(
                        p -> normalizeName(p.getEspnName()),
                        Function.identity(),
                        (existing, replacement) -> existing // or replacement, depending on preference
                ));

        List<DraftPlayer> draftPlayers = adpList.stream()
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

        String userId = "";
        List<FantasyTeam> mockTeams = new ArrayList<>();
        mockTeams.clear();

        for (int i = 0; i < numOfTeams; i++) {
            String teamID = String.valueOf(i);
            String teamName = (i == (draftPosition - 1)) ? mockTeamName : "Mock Team " + (i + 1);
            if (i == (draftPosition - 1))
                userId = teamID;
            mockTeams.add(new FantasyTeam(teamID, teamName, new ArrayList<>()));
        }

        boolean isReversed = false;
        int currentPick = 1;
        int roundNumber = 1;
        int currentTeamId = 0;

        // Initialize picked players list in session
        List<DraftPlayer> pickedPlayers = new ArrayList<>();
        pickedPlayers.clear();
        session.setAttribute("pickedPlayers", pickedPlayers);

        session.setAttribute("adpList", draftPlayers);
        session.setAttribute("mockTeams", mockTeams);

        model.addAttribute("numOfTeams", numOfTeams);
        model.addAttribute("numOfSelections", numOfSelections);
        model.addAttribute("draftPosition", draftPosition);
        model.addAttribute("mockTeamName", mockTeamName);
        model.addAttribute("numOfRounds", numOfRounds);
        model.addAttribute("format", format);
        model.addAttribute("pickedPlayers", pickedPlayers);
        model.addAttribute("currentPick", currentPick);
        model.addAttribute("roundNumber", roundNumber);
        model.addAttribute("currentTeamId", currentTeamId);
        model.addAttribute("isReversed", isReversed);
        model.addAttribute("userId", userId);

        return "draftBoard";
    }

    private static final Map<String, String> nicknameMap = Map.ofEntries(
    );

    private String normalizeName(String name) {
        if (name == null || name.isEmpty())
            return "";

        // Lowercase and clean up the name
        String cleaned = name
                .toLowerCase()
                .replaceAll("\\b(jr|sr|ii|iii|iv|v)\\b", "") // remove suffixes
                .replaceAll("[^a-z ]", "") // remove punctuation
                .replaceAll("\\s+", " ") // normalize whitespace
                .trim();

        String[] parts = cleaned.split(" ");
        if (parts.length == 0)
            return "";

        // Replace the first name with its full version if it's a known nickname
        String firstName = nicknameMap.getOrDefault(parts[0], parts[0]);

        // Recombine the name
        StringBuilder normalized = new StringBuilder(firstName);
        for (int i = 1; i < parts.length; i++) {
            normalized.append(" ").append(parts[i]);
        }

        return normalized.toString();
    }

    @PostMapping("/draft/select")
    @ResponseBody
    public ResponseEntity<?> selectPlayer(
            @RequestParam String selectedPlayerName,
            @RequestParam String userId,
            @RequestParam String currentTeamId,
            @RequestParam int currentPick,
            @RequestParam boolean isReversed,
            @RequestParam int roundNumber,
            @RequestParam int numOfTeams,
            @RequestParam int numOfRounds,
            HttpSession session) {

        int totalSelections = numOfTeams * numOfRounds;

        @SuppressWarnings("unchecked")
        List<DraftPlayer> adpList = (List<DraftPlayer>) session.getAttribute("adpList");

        @SuppressWarnings("unchecked")
        List<FantasyTeam> mockTeams = (List<FantasyTeam>) session.getAttribute("mockTeams");

        @SuppressWarnings("unchecked")
        List<DraftPlayer> pickedPlayers = (List<DraftPlayer>) session.getAttribute("pickedPlayers");

        if (adpList == null || mockTeams == null || pickedPlayers == null) {
            return ResponseEntity.badRequest().body("Session data missing");
        }

        FantasyTeam currentTeam = mockTeams.stream()
                .filter(team -> team.getTeamID().equals(currentTeamId))
                .findFirst()
                .orElse(null);

        if (currentTeam == null) {
            return ResponseEntity.badRequest().body("Invalid team ID");
        }

        DraftPlayer selectedPlayer;

        if (currentTeamId.equals(userId)) {
            selectedPlayer = adpList.stream()
                    .filter(player -> player.getAdpPlayer().getName().equalsIgnoreCase(selectedPlayerName))
                    .findFirst()
                    .orElse(null);

            if (selectedPlayer == null) {
                return ResponseEntity.badRequest().body("Player not found");
            }
        } else {
            if (adpList.isEmpty()) {
                return ResponseEntity.badRequest().body("No players remaining");
            }

            List<DraftPlayer> cpuRoster = currentTeam.getRoster();

            // Compute counts once for positions
            Map<String, Long> positionCounts = cpuRoster.stream()
                    .collect(Collectors.groupingBy(p -> p.getAdpPlayer().getPosition(), Collectors.counting()));

            long qbCount = positionCounts.getOrDefault("QB", 0L);
            long teCount = positionCounts.getOrDefault("TE", 0L);
            long wrCount = positionCounts.getOrDefault("WR", 0L);
            long rbCount = positionCounts.getOrDefault("RB", 0L);

            selectedPlayer = null;

            // Position max limits if round < 10
            Map<String, Integer> positionLimits = Map.of(
                    "QB", 2,
                    "TE", 2,
                    "WR", 6,
                    "RB", 6);

            boolean forcePositionPick = false;

            // After round 10, force pick missing positions if any
            if (roundNumber >= 10) {
                // Find positions missing from roster
                List<String> neededPositions = new ArrayList<>();
                for (String pos : positionLimits.keySet()) {
                    if (positionCounts.getOrDefault(pos, 0L) == 0) {
                        neededPositions.add(pos);
                    }
                }

                if (!neededPositions.isEmpty()) {
                    // Filter adpList for players at needed positions
                    List<DraftPlayer> neededPlayers = adpList.stream()
                            .filter(p -> neededPositions.contains(p.getAdpPlayer().getPosition()))
                            .collect(Collectors.toList());

                    if (!neededPlayers.isEmpty()) {
                        // Pick top player from neededPlayers (could randomize if you want)
                        selectedPlayer = neededPlayers.get(0);
                        System.out.println(
                                "CPU forced to pick missing position " + selectedPlayer.getAdpPlayer().getPosition() +
                                        ": " + selectedPlayer.getAdpPlayer().getName());
                        forcePositionPick = true;
                    }
                }
            }

            // If not forced to pick a missing position, proceed with normal weighted pick
            // logic
            if (!forcePositionPick) {
                // Filter adpList to respect position limits for rounds < 10
                List<DraftPlayer> filteredAdpList = adpList;
                if (roundNumber < 10) {
                    filteredAdpList = adpList.stream()
                            .filter(p -> {
                                String pos = p.getAdpPlayer().getPosition();
                                Integer limit = positionLimits.get(pos);
                                if (limit == null) {
                                    return true; // positions without limits allowed always
                                }
                                long count = positionCounts.getOrDefault(pos, 0L);
                                return count < limit;
                            })
                            .collect(Collectors.toList());

                    if (filteredAdpList.isEmpty()) {
                        // All limits reached, fallback to original adpList (allow any position)
                        filteredAdpList = adpList;
                    }
                }

                // Check if any position has fewer than 2 players on the CPU roster (position of
                // need)
                boolean needsPosition = positionCounts.values().stream().anyMatch(count -> count < 2);

                if (roundNumber > 4 && needsPosition) {
                    int topN = Math.min(8, filteredAdpList.size());
                    List<DraftPlayer> topPlayers = filteredAdpList.subList(0, topN);

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

                        // Penalize duplicate QBs/TEs early as before
                        if (roundNumber <= 8) {
                            if ("QB".equalsIgnoreCase(pos) && qbCount > 0) {
                                baseWeight /= 2;
                            } else if ("TE".equalsIgnoreCase(pos) && teCount > 0) {
                                baseWeight /= 2;
                            }
                        }

                        weights[i] = Math.max(baseWeight, 1);
                    }

                    int totalWeight = IntStream.of(weights).sum();
                    int rand = new Random().nextInt(totalWeight);

                    int cumulative = 0;
                    for (int i = 0; i < weights.length; i++) {
                        cumulative += weights[i];
                        if (rand < cumulative) {
                            selectedPlayer = topPlayers.get(i);
                            System.out.println("CPU selected with position of need boost: " +
                                    selectedPlayer.getAdpPlayer().getName() + " (" +
                                    selectedPlayer.getAdpPlayer().getPosition() + ")");
                            break;
                        }
                    }
                }

                // If still no player selected
                if (selectedPlayer == null) {
                    int topN = Math.min(8, filteredAdpList.size());
                    List<DraftPlayer> topPlayers = filteredAdpList.subList(0, topN);

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
                        // Cubed weighting here too
                        int weight = rankFactor * rankFactor * rankFactor;

                        if (roundNumber <= 6) {
                            if ("QB".equalsIgnoreCase(pos) && qbCount > 0) {
                                weight = (int) (weight * 0.2);
                            } else if ("TE".equalsIgnoreCase(pos) && teCount > 0) {
                                weight = (int) (weight * 0.5);
                            }
                        }

                        weights[i] = Math.max(weight, 1);
                    }

                    int totalWeight = IntStream.of(weights).sum();
                    int rand = new Random().nextInt(totalWeight);

                    int cumulative = 0;
                    for (int i = 0; i < weights.length; i++) {
                        cumulative += weights[i];
                        if (rand < cumulative) {
                            selectedPlayer = topPlayers.get(i);
                            System.out.println("CPU selected (ADP-weighted) " +
                                    selectedPlayer.getAdpPlayer().getName() + " (" +
                                    selectedPlayer.getAdpPlayer().getPosition() + ")");
                            break;
                        }
                    }
                }
            }
        }

        adpList.remove(selectedPlayer);
        currentTeam.getRoster().add(selectedPlayer);
        pickedPlayers.add(selectedPlayer);

        // Update session data
        session.setAttribute("adpList", adpList);
        session.setAttribute("mockTeams", mockTeams);
        session.setAttribute("pickedPlayers", pickedPlayers);

        currentPick++;
        if (currentPick > totalSelections) {
            Map<String, Object> endResponse = new HashMap<>();
            endResponse.put("draftEnded", true);
            return ResponseEntity.ok(endResponse);
        }

        int currentTeamIndex = -1;
        for (int i = 0; i < mockTeams.size(); i++) {
            if (mockTeams.get(i).getTeamID().equals(currentTeamId)) {
                currentTeamIndex = i;
                break;
            }
        }

        // Snake draft logic
        if (!isReversed) {
            currentTeamIndex++;
            if (currentTeamIndex >= numOfTeams) {
                currentTeamIndex = numOfTeams - 1;
                isReversed = true;
                roundNumber++;
            }
        } else {
            currentTeamIndex--;
            if (currentTeamIndex < 0) {
                currentTeamIndex = 0;
                isReversed = false;
                roundNumber++;
            }
        }

        String nextTeamId = mockTeams.get(currentTeamIndex).getTeamID();

        Map<String, Object> response = new HashMap<>();
        response.put("currentPick", currentPick);
        response.put("roundNumber", roundNumber);
        response.put("isReversed", isReversed);
        response.put("currentTeamId", nextTeamId);
        response.put("selectedPlayer", selectedPlayer);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/draft-over")
    public String getDraftResults(Model model, HttpSession session, SessionStatus status) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);

        @SuppressWarnings("unchecked")
        List<FantasyTeam> mockTeams = (List<FantasyTeam>) session.getAttribute("mockTeams");

        // Sort so user's team comes first
        mockTeams.sort((a, b) -> {
            boolean aIsMock = a.getTeamName().contains("Mock Team");
            boolean bIsMock = b.getTeamName().contains("Mock Team");
            return Boolean.compare(aIsMock, bIsMock); // false < true
        });

        model.addAttribute("completedMocks", mockTeams);
        authenticatedUser.getCompletedMocks().add(mockTeams);
        userService.updateUser(authenticatedUser);

        List<String> positions = List.of("QB", "RB", "WR", "TE");
        model.addAttribute("positions", positions);

        session.removeAttribute("mockTeams");
        session.removeAttribute("adpList");
        session.removeAttribute("pickedPlayers");
        status.setComplete();

        return "userLeague";
    }

    @GetMapping("/leave-draft")
    public String leaveDraft(HttpSession session, SessionStatus status, Model model) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);
        System.out.println("User " + authenticatedUser.getUsername() + " has left the draft...");
        System.out.println("Attempting to clean up draft session attributes...");

        try {
            session.removeAttribute("mockTeams");
            session.removeAttribute("adpList");
            session.removeAttribute("pickedPlayers");
            System.out.println("Removed session attributes.");
        } catch (Exception e) {
            System.err.println("Error while removing draft attributes from session: " + e.getMessage());
            e.printStackTrace();
        }

        status.setComplete();
        return "redirect:/";
    }

    @PostMapping("/leave-draft")
    @ResponseBody
    public void leaveDraftOnUnload(HttpSession session, SessionStatus status, Model model) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);
        System.out.println("User " + authenticatedUser.getUsername() + " has left the draft...");
        System.out.println("Attempting to clean up draft session attributes...");

        try {
            session.removeAttribute("mockTeams");
            session.removeAttribute("adpList");
            session.removeAttribute("pickedPlayers");
            System.out.println("Removed session attributes.");
        } catch (Exception e) {
            System.err.println("Error while removing draft attributes from session: " + e.getMessage());
            e.printStackTrace();
        }

        status.setComplete();
    }

    @PostMapping("/player-details")
    public String getPlayerByEspnName(@RequestParam("espnName") String espnName, Model model, HttpSession session) {
        System.out.println("In player-details");

        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);

        Optional<EspnPlayer> player = espnPlayerService.findPlayerByEspnName(espnName);
        if (player.isPresent()) {
            EspnPlayer espnPlayer = player.get();

            try {
                GetPlayerDetails apiCaller = new GetPlayerDetails();
                PlayerDetails playerDetails = apiCaller.getPlayerDetails(Integer.parseInt(espnPlayer.getEspnID()));

                // 🧼 Clean stat strings
                cleanStatStrings(playerDetails);

                double rushAverage = 0;
                boolean rushDataAvailable = false;

                PlayerDetails.Stats stats = playerDetails.getStats();
                double totalFantasyPoints = 0.0;

                if (stats != null) {
                    PlayerDetails.Rushing rushing = stats.getRushing();
                    PlayerDetails.Receiving receiving = stats.getReceiving();
                    PlayerDetails.Passing passing = stats.getPassing();

                    try {
                        if (rushing != null) {
                            int rushYds = parseOrZero(rushing.getRushYds());
                            int rushTD = parseOrZero(rushing.getRushTD());
                            totalFantasyPoints += (rushYds * 0.1) + (rushTD * 6);
                        }

                        if (receiving != null) {
                            int recYds = parseOrZero(receiving.getRecYds());
                            int recTD = parseOrZero(receiving.getRecTD());
                            int receptions = parseOrZero(receiving.getReceptions());
                            totalFantasyPoints += (receptions * 1.0) + (recYds * 0.1) + (recTD * 6);
                        }

                        if (passing != null) {
                            int passYds = parseOrZero(passing.getPassYds());
                            int passTD = parseOrZero(passing.getPassTD());
                            totalFantasyPoints += (passYds * 0.04) + (passTD * 4);
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // or use a logger
                    }
                }

                DecimalFormat df = new DecimalFormat("#.#");
                model.addAttribute("seasonFantasyPoints", df.format(totalFantasyPoints));

                if (stats != null) {
                    PlayerDetails.Rushing rushing = stats.getRushing();
                    if (rushing != null
                            && rushing.getRushYds() != null
                            && rushing.getCarries() != null
                            && !rushing.getRushYds().isEmpty()
                            && !rushing.getCarries().isEmpty()) {

                        try {
                            int yards = Integer.parseInt(rushing.getRushYds());
                            int attempts = Integer.parseInt(rushing.getCarries());
                            if (attempts > 0) {
                                rushAverage = (double) yards / attempts;
                                rushDataAvailable = true;
                            }
                        } catch (NumberFormatException e) {
                            // handle parse error, e.g. log or ignore
                            e.printStackTrace();
                        }
                    }
                }

                if (rushDataAvailable) {
                    model.addAttribute("rushAvg", df.format(rushAverage));
                } else {
                    model.addAttribute("rushAvg", "N/A");
                }

                model.addAttribute("playerDetails", playerDetails);

                Map<String, PlayerGameStats.GameData> gameStatsMap = playerDetails.getGameStats();

                List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList = new ArrayList<>(
                        gameStatsMap.entrySet());

                gameStatsList.sort((entry1, entry2) -> {
                    String dateStr1 = entry1.getKey().substring(0, 8);
                    String dateStr2 = entry2.getKey().substring(0, 8);
                    return dateStr2.compareTo(dateStr1);
                });

                model.addAttribute("gameStatsList", gameStatsList);

                String[] nameParts = espnName.trim().split(" ");
                if (nameParts.length >= 2) {
                    String firstName = nameParts[0];
                    String lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));

                    model.addAttribute("firstName", firstName);
                    model.addAttribute("lastName", lastName);
                }

                return "player-details";

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to fetch player details.");
                return "error";
            }
        } else {
            return "playerNotFound";
        }
    }

    private int parseOrZero(String val) {
        if (val == null || val.isEmpty())
            return 0;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void cleanStatStrings(PlayerDetails playerDetails) {
        if (playerDetails.getStats() == null)
            return;

        PlayerDetails.Stats stats = playerDetails.getStats();

        // Clean numeric string fields across stat categories
        if (stats.getPassing() != null) {
            stats.getPassing().setPassAttempts(clean(stats.getPassing().getPassAttempts()));
            stats.getPassing().setPassCompletions(clean(stats.getPassing().getPassCompletions()));
            stats.getPassing().setPassYds(clean(stats.getPassing().getPassYds()));
            stats.getPassing().setPassTD(clean(stats.getPassing().getPassTD()));
            stats.getPassing().setIntVal(clean(stats.getPassing().getIntVal()));
        }

        if (stats.getRushing() != null) {
            stats.getRushing().setCarries((clean(stats.getRushing().getCarries())));
            stats.getRushing().setRushYds((clean(stats.getRushing().getRushYds())));
            stats.getRushing().setRushTD(clean(stats.getRushing().getRushTD()));
        }

        if (stats.getReceiving() != null) {
            stats.getReceiving().setReceptions(clean(stats.getReceiving().getReceptions()));
            stats.getReceiving().setRecYds(clean(stats.getReceiving().getRecYds()));
            stats.getReceiving().setRecTD(clean(stats.getReceiving().getRecTD()));
            stats.getReceiving().setTargets(clean(stats.getReceiving().getTargets()));
        }
    }

    private String clean(String stat) {
        if (stat == null)
            return null;
        if (stat.contains(".")) {
            String[] parts = stat.split("\\.");
            if (parts.length > 1 && parts[1].equals("0")) {
                return parts[0]; // "1.0" -> "1"
            }
        }
        return stat;
    }

    @PostMapping("/team/player-details")
    public String getTeamPlayerByEspnName(@RequestParam("espnName") String espnName, Model model, HttpSession session) {
        System.out.println("In team-player-details");

        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);

        Optional<EspnPlayer> player = espnPlayerService.findPlayerByEspnName(espnName);
        if (player.isPresent()) {
            EspnPlayer espnPlayer = player.get();

            try {
                GetPlayerDetails apiCaller = new GetPlayerDetails();
                PlayerDetails playerDetails = apiCaller.getPlayerDetails(Integer.parseInt(espnPlayer.getEspnID()));

                cleanStatStrings(playerDetails);

                double rushAverage = 0;
                boolean rushDataAvailable = false;

                PlayerDetails.Stats stats = playerDetails.getStats();

                double totalFantasyPoints = 0.0;

                if (stats != null) {
                    PlayerDetails.Rushing rushing = stats.getRushing();
                    PlayerDetails.Receiving receiving = stats.getReceiving();
                    PlayerDetails.Passing passing = stats.getPassing();

                    try {
                        if (rushing != null) {
                            int rushYds = parseOrZero(rushing.getRushYds());
                            int rushTD = parseOrZero(rushing.getRushTD());
                            totalFantasyPoints += (rushYds * 0.1) + (rushTD * 6);
                        }

                        if (receiving != null) {
                            int recYds = parseOrZero(receiving.getRecYds());
                            int recTD = parseOrZero(receiving.getRecTD());
                            int receptions = parseOrZero(receiving.getReceptions());
                            totalFantasyPoints += (receptions * 1.0) + (recYds * 0.1) + (recTD * 6);
                        }

                        if (passing != null) {
                            int passYds = parseOrZero(passing.getPassYds());
                            int passTD = parseOrZero(passing.getPassTD());
                            totalFantasyPoints += (passYds * 0.04) + (passTD * 4);
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // or use a logger
                    }
                }

                DecimalFormat df = new DecimalFormat("#.#");
                model.addAttribute("seasonFantasyPoints", df.format(totalFantasyPoints));

                if (stats != null) {
                    PlayerDetails.Rushing rushing = stats.getRushing();
                    if (rushing != null
                            && rushing.getRushYds() != null
                            && rushing.getCarries() != null
                            && !rushing.getRushYds().isEmpty()
                            && !rushing.getCarries().isEmpty()) {

                        try {
                            int yards = Integer.parseInt(rushing.getRushYds());
                            int attempts = Integer.parseInt(rushing.getCarries());
                            if (attempts > 0) {
                                rushAverage = (double) yards / attempts;
                                rushDataAvailable = true;
                            }
                        } catch (NumberFormatException e) {
                            // handle parse error, e.g. log or ignore
                            e.printStackTrace();
                        }
                    }
                }

                if (rushDataAvailable) {
                    model.addAttribute("rushAvg", df.format(rushAverage));
                } else {
                    model.addAttribute("rushAvg", "N/A");
                }
                model.addAttribute("playerDetails", playerDetails);

                Map<String, PlayerGameStats.GameData> gameStatsMap = playerDetails.getGameStats();
                List<Map.Entry<String, PlayerGameStats.GameData>> gameStatsList = new ArrayList<>(
                        gameStatsMap.entrySet());
                gameStatsList.sort(
                        (entry1, entry2) -> entry2.getKey().substring(0, 8).compareTo(entry1.getKey().substring(0, 8)));
                model.addAttribute("gameStatsList", gameStatsList);

                String[] nameParts = espnName.trim().split(" ");
                if (nameParts.length >= 2) {
                    String firstName = nameParts[0];
                    String lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));

                    model.addAttribute("firstName", firstName);
                    model.addAttribute("lastName", lastName);
                }

                return "team-player-details";

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to fetch player details.");
                return "error";
            }
        } else {
            return "playerNotFound";
        }
    }

    /**
     * Method to show teams drafted by an authenticated user
     * 
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/user/teams")
    public String getUserTeams(HttpSession session, Model model) {
        // Retrieve the authenticated user from the session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser != null) {
            List<List<FantasyTeam>> userTeams = authenticatedUser.getCompletedMocks();
            // List to store user teams
            List<FantasyTeam> matchingTeams = new ArrayList<>();

            // iterate through each List of Teams
            for (List<FantasyTeam> teamList : userTeams) {
                // iterate through each team
                for (FantasyTeam team : teamList) {
                    // Check if the team name does not start with "Mock Team" (User's Team)
                    if (!team.getTeamName().startsWith("Mock Team")) {
                        // Add the team to the list of matching teams
                        matchingTeams.add(team);
                    }
                }
            }
            // Reverse the order of the matching teams list
            Collections.reverse(matchingTeams);
            model.addAttribute("completedMocks", matchingTeams);
            // Add the user to the model
            model.addAttribute("authenticatedUser", authenticatedUser);
        }
        return "userTeams";
    }

    @GetMapping("/user/profile")
    public String getUserProfile(HttpSession session, Model model) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser != null) {
            model.addAttribute("authenticatedUser", authenticatedUser);
        }
        return "profile.html";
    }

    @PostMapping("/user/delete-team")
    public String deleteTeam(@RequestParam("userID") String userID, @RequestParam("teamName") String teamName,
            HttpSession session, Model model) {
        // Call service to delete the team from completedMocks
        userService.deleteTeamFromMocks(userID, teamName);

        // Reload the updated user data
        Optional<User> userOptional = userRepository.findByUserID(userID);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update the session with the new user data
            session.setAttribute("authenticatedUser", user);
            model.addAttribute("authenticatedUser", user);
        }

        // Redirect to the updated teams page with the new list
        return "redirect:/user/teams";
    }

    @GetMapping("/league/teams")
    public String getUserLeague(
            @RequestParam("teamName") String teamName,
            HttpSession session,
            Model model) {
        // Retrieve the authenticated user from the session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");

        if (authenticatedUser != null) {
            // Retrieve the user's completed mocks (List<List<FantasyTeam>>)
            List<List<FantasyTeam>> userTeams = authenticatedUser.getCompletedMocks();

            // List to store the teams that match the given team name
            List<FantasyTeam> matchingTeamList = null;

            // Iterate through each List of FantasyTeam
            for (List<FantasyTeam> teamList : userTeams) {
                // Check if any team in the list matches the teamName
                for (FantasyTeam team : teamList) {
                    if (team.getTeamName().equalsIgnoreCase(teamName)) {
                        // If a match is found, store this team list
                        matchingTeamList = teamList;
                        break;
                    }
                }
                if (matchingTeamList != null) {
                    // Stop iterating if we've found the matching team list
                    break;
                }
            }

            // Add the matching teams to the model if found
            if (matchingTeamList != null) {
                model.addAttribute("completedMocks", matchingTeamList);
            } else {
                model.addAttribute("errorMessage", "No matching team found with name: " + teamName);
            }
        }

        // Add the user to the model
        model.addAttribute("authenticatedUser", authenticatedUser);
        List<String> positions = List.of("QB", "RB", "WR", "TE");
        model.addAttribute("positions", positions);

        return "userLeague";
    }

    /**
     * Method to initialize draft settings before starting draft
     * 
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/setup")
    public String setupDraft(HttpSession session, Model model) {
        // Retrieve the authenticated user from the session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            System.out.println("User not found in session");
        }
        session.removeAttribute("pickedPlayers");
        session.removeAttribute("adpList");
        session.removeAttribute("mockTeams");
        // Add the user to the model
        model.addAttribute("authenticatedUser", authenticatedUser);

        return "setup";
    }

}
