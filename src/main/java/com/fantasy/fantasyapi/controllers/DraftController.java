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
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.objectModels.AdpPlayer;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

// Annotate the controller with SessionAttributes
@Controller
@SessionAttributes({ "adpList", "mockTeams", "currentTeamId", "roundNumber", "isReversed" })
public class DraftController {

    @Autowired
    UserService userService;

    public int numOfSelections;
    List<AdpPlayer> pickedPlayers = new ArrayList<AdpPlayer>();

    // Initialize and store lists in the session
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
            String teamID = String.valueOf(i);
            String teamName = "";
            if (i == (draftPosition - 1)) {
                teamName = mockTeamName;
                userId = teamID;
            } else {
                teamName = "Mock Team " + (i + 1);
            }
            mockTeams.add(new FantasyTeam(teamID, teamName, new ArrayList<AdpPlayer>()));
        }
        boolean isReversed = false;
        int currentPick = 1;
        pickedPlayers = new ArrayList<AdpPlayer>();
        model.addAttribute("currentPick", currentPick);
        model.addAttribute("pickedPlayers", pickedPlayers);
        model.addAttribute("adpList", adpList);
        model.addAttribute("isReversed", isReversed);
        model.addAttribute("roundNumber", 1);
        model.addAttribute("mockTeams", mockTeams);
        model.addAttribute("currentTeamId", 0); // First team's turn
        model.addAttribute("userId", userId);

        return "draftBoard";
    }

    @PostMapping("/draft/select")
    public String selectPlayer(
            @RequestParam("selectedPlayerId") String selectedPlayerId,
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
            // Auto-select the first player for the CPU
            if (!adpList.isEmpty()) {
                AdpPlayer autoSelectedPlayer = adpList.get(0);

                // Add the player to the current CPU team's roster
                mockTeams.get(currentTeamId).getRoster().add(autoSelectedPlayer);
                pickedPlayers.add(autoSelectedPlayer);
                // Remove the player from the ADP list
                adpList.remove(autoSelectedPlayer);
                currentPick++;
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

        model.addAttribute("pickedPlayers", pickedPlayers);
        model.addAttribute("currentPick", currentPick);
        model.addAttribute("currentTeamId", currentTeamId); // Next team's turn
        model.addAttribute("roundNumber", roundNumber); // Current round number
        model.addAttribute("isReversed", isReversed); // Whether the round is reversed or not
        model.addAttribute("userId", userId);

        return "draftBoard";
    }

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
