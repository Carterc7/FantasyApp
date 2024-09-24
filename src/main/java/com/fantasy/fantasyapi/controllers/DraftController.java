package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.EspnPlayerService;
import com.fantasy.fantasyapi.mongoServices.TeamScheduleService;
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.objectModels.TeamSchedule;

import jakarta.servlet.http.HttpSession;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.SessionAttributes;
import java.util.Arrays;

import org.springframework.web.bind.annotation.ModelAttribute;

// SessionAttributes (global variables that are initialized in showDraftBoard and used through the draft)
@Controller
@SessionAttributes({ "adpList", "mockTeams", "currentTeamId", "roundNumber", "isReversed" })
public class DraftController {

    @Autowired
    UserService userService;

    @Autowired
    private EspnPlayerService espnPlayerService;

    @Autowired
    private TeamScheduleService teamScheduleService;

    // global var for total num selection to be made in a draft (end-point)
    public int numOfSelections;
    // globar var for players picked in the draft
    List<AdpPlayer> pickedPlayers = new ArrayList<AdpPlayer>();

    /**
     * Method to initialize Teams and draft format from setup.html, and then show
     * the draftBoard.
     * 
     * @param numOfTeams
     * @param mockTeamName
     * @param draftPosition
     * @param numOfRounds
     * @param model
     * @return
     */
    @PostMapping("/draft")
    public String showDraftBoard(
            @RequestParam("numOfTeams") int numOfTeams,
            @RequestParam("mockTeamName") String mockTeamName,
            @RequestParam("draftPosition") int draftPosition,
            @RequestParam("numOfRounds") int numOfRounds,
            Model model) {
        numOfSelections = numOfRounds * numOfTeams;
        // Get ADP list
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        List<AdpPlayer> adpList = getDraftAdp.getFilteredAdpList(200);
        String userId = "";
        // Set up teams
        List<FantasyTeam> mockTeams = new ArrayList<>();
        for (int i = 0; i < numOfTeams; i++) {
            // Set teamIDs and teamNames
            String teamID = String.valueOf(i);
            String teamName = "";
            if (i == (draftPosition - 1)) {
                // set user's teamName to parameter value
                teamName = mockTeamName;
                userId = teamID;
            } else {
                // set default teamName for mock teams
                teamName = "Mock Team " + (i + 1);
            }
            // Give each team an empty list of players
            mockTeams.add(new FantasyTeam(teamID, teamName, new ArrayList<AdpPlayer>()));
        }
        boolean isReversed = false;
        int currentPick = 1;
        pickedPlayers = new ArrayList<AdpPlayer>();
        model.addAttribute("numOfTeams", numOfTeams); // number of teams
        model.addAttribute("currentPick", currentPick); // Current pick number
        model.addAttribute("pickedPlayers", pickedPlayers); // List of all picked players (empty)
        model.addAttribute("adpList", adpList); // List of ADP
        model.addAttribute("isReversed", isReversed); // Set to false, draft will start in forward direction
        model.addAttribute("roundNumber", 1); // Start at round 1
        model.addAttribute("mockTeams", mockTeams); // Teams with empty player lists.
        model.addAttribute("currentTeamId", 0); // First team's turn
        model.addAttribute("userId", userId); // User's team ID used to check if it's the users turn.

        return "draftBoard";
    }

    /**
     * Method to select a player and add to selected team's roster
     * 
     * @param selectedPlayerId
     * @param userId
     * @param currentPick
     * @param adpList
     * @param mockTeams
     * @param currentTeamId
     * @param roundNumber
     * @param isReversed
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/draft/select")
    public String selectPlayer(
            @RequestParam("selectedPlayerId") String selectedPlayerId,
            @RequestParam("numOfTeams") int numOfTeams,
            @RequestParam("userId") String userId,
            @RequestParam("currentPick") int currentPick,
            @ModelAttribute("adpList") List<AdpPlayer> adpList,
            @ModelAttribute("mockTeams") List<FantasyTeam> mockTeams,
            @ModelAttribute("currentTeamId") int currentTeamId,
            @ModelAttribute("roundNumber") int roundNumber,
            @ModelAttribute("isReversed") boolean isReversed,
            HttpSession session, // Add HttpSession parameter
            Model model) {

        // Retrieve authenticatedUser from session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");

        // Check if authenticatedUser is null (not logged in)
        if (authenticatedUser == null) {
            return "redirect:/login"; // Redirect to login page if no user is authenticated
        }

        // Convert userId to match team ID format
        String currentTeamIdStr = String.valueOf(currentTeamId);

        // Check if it's the user's turn
        if (userId.equals(currentTeamIdStr)) {
            // Manually select the player for the user
            AdpPlayer selectedPlayer = adpList.stream()
                    .filter(player -> player.getPlayerID().equals(selectedPlayerId))
                    .findFirst()
                    .orElse(null);

            if (selectedPlayer != null) {
                // Add the player to the current team's roster
                mockTeams.get(currentTeamId).getRoster().add(selectedPlayer);
                pickedPlayers.add(selectedPlayer);
                // Remove the player from the ADP list
                adpList.remove(selectedPlayer);
                currentPick++;
            }
        } else {
            AdpPlayer autoSelectedPlayer = null;
            // Auto-select the player for the CPU
            if (!adpList.isEmpty()) {
                // if roundNumber is greater than 5, check to make sure CPU has players at each
                // position
                if (roundNumber >= 5) {
                    // Define the required positions for a complete roster
                    List<String> requiredPositions = Arrays.asList("QB", "RB", "WR", "TE");

                    // Retrieve the current team's roster
                    List<AdpPlayer> currentTeamRoster = mockTeams.get(currentTeamId).getRoster();

                    // Find out which positions the team still needs to draft
                    List<String> neededPositions = new ArrayList<>(requiredPositions);
                    for (AdpPlayer player : currentTeamRoster) {
                        neededPositions.remove(player.getPosition());
                    }

                    // Check if the team still needs to fill any position
                    if (!neededPositions.isEmpty()) {
                        // Prioritize drafting a player for the missing position
                        String nextNeededPosition = neededPositions.get(0); // Select the first needed position
                        autoSelectedPlayer = adpList.stream()
                                .filter(player -> player.getPosition().equals(nextNeededPosition))
                                .findFirst()
                                .orElse(null);
                    }
                }

                // If no players are available for the needed position, select one of the best
                // available players
                if (autoSelectedPlayer == null) {
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(Math.min(3, adpList.size()));
                    autoSelectedPlayer = adpList.get(randomIndex);
                }

                if (autoSelectedPlayer != null) {
                    // Add the selected player to the current team's roster
                    mockTeams.get(currentTeamId).getRoster().add(autoSelectedPlayer);
                    pickedPlayers.add(autoSelectedPlayer);
                    // Remove the player from the ADP list
                    adpList.remove(autoSelectedPlayer);
                    currentPick++;
                }
            }
        }

        // Count the total selections made
        int totalSelectionsMade = mockTeams.stream()
                .mapToInt(team -> team.getRoster().size())
                .sum();
        System.out.println("Total selections made: " + totalSelectionsMade);

        // Check if the draft is over
        if (totalSelectionsMade >= numOfSelections) {
            // Add each mock team to the user's completed mocks
            authenticatedUser.getCompletedMocks().add(mockTeams);
            // Save updated user data
            userService.updateUser(authenticatedUser);
            model.addAttribute("authenticatedUser", authenticatedUser);
            // Draft is over, show draft ended screen
            model.addAttribute("mockTeams", mockTeams);
            return "draftEnded";
        }

        // Update the model and session attributes
        model.addAttribute("adpList", adpList);
        model.addAttribute("mockTeams", mockTeams);

        // Update the draft order and direction
        if (isReversed) {
            // If we are in a reversed round, decrement the currentTeamId
            currentTeamId--;
            if (currentTeamId < 0) {
                // If the current team is the first team, toggle direction and move to next
                // round
                currentTeamId = 0; // Move to the next team in the reversed direction
                roundNumber++;
                isReversed = false; // Change direction to forward
            }
        } else {
            // If we are in a forward round, increment the currentTeamId
            currentTeamId++;
            if (currentTeamId >= mockTeams.size()) {
                // If the current team is the last team, toggle direction and move to next round
                currentTeamId = mockTeams.size() - 1; // Move to the previous team in the reversed direction
                roundNumber++;
                isReversed = true; // Change direction to reversed
            }
        }

        model.addAttribute("numOfTeams", numOfTeams); // number of teams
        model.addAttribute("pickedPlayers", pickedPlayers);
        model.addAttribute("currentPick", currentPick);
        model.addAttribute("currentTeamId", currentTeamId); // Next team's turn
        model.addAttribute("roundNumber", roundNumber); // Current round number
        model.addAttribute("isReversed", isReversed); // Whether the round is reversed or not
        model.addAttribute("userId", userId); // User's teamID

        return "draftBoard";
    }

    @PostMapping("/player-details")
    public String getPlayerByEspnName(@RequestParam("espnName") String espnName, Model model, HttpSession session) {
        // Retrieve authenticatedUser from session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser != null) {
            model.addAttribute("authenticatedUser", authenticatedUser);
        }
        Optional<EspnPlayer> player = espnPlayerService.findPlayerByEspnName(espnName);
        if (player.isPresent()) {
            EspnPlayer espnPlayer = player.get();
            // Fetch team schedule by team abbreviation
            String teamAbv = espnPlayer.getTeam();
            Optional<List<TeamSchedule>> teamSchedule = teamScheduleService.findScheduleByTeam(teamAbv);

            // Add team schedule to the model if present
            if (teamSchedule.isPresent()) {
                model.addAttribute("teamSchedule", teamSchedule.get());
            } else {
                model.addAttribute("teamSchedule", "No schedule available for this team.");
            }
            model.addAttribute("espnPlayer", espnPlayer);
            return "player-details";
        } else {
            return "error";
        }
    }

    @PostMapping("/team/player-details")
    public String getTeamPlayerByEspnName(@RequestParam("espnName") String espnName, Model model, HttpSession session) {
        // Retrieve authenticatedUser from session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser != null) {
            model.addAttribute("authenticatedUser", authenticatedUser);
        }
        Optional<EspnPlayer> player = espnPlayerService.findPlayerByEspnName(espnName);
        if (player.isPresent()) {
            EspnPlayer espnPlayer = player.get();
            // Fetch team schedule by team abbreviation
            String teamAbv = espnPlayer.getTeam();
            Optional<List<TeamSchedule>> teamSchedule = teamScheduleService.findScheduleByTeam(teamAbv);

            // Add team schedule to the model if present
            if (teamSchedule.isPresent()) {
                model.addAttribute("teamSchedule", teamSchedule.get());
            } else {
                model.addAttribute("teamSchedule", "No schedule available for this team.");
            }
            model.addAttribute("espnPlayer", espnPlayer);
            return "team-player-details";
        } else {
            return "error";
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
            model.addAttribute("completedMocks", matchingTeams);
        }
        // Add the user to the model
        model.addAttribute("authenticatedUser", authenticatedUser);
        return "userTeams";
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
        // Add the user to the model
        model.addAttribute("authenticatedUser", authenticatedUser);

        return "setup";
    }

}
