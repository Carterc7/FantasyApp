package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.objectModels.DraftPlayer;
import com.fantasy.fantasyapi.repository.UserRepository;
import com.fantasy.fantasyapi.services.PlayerDetailsService;
import com.fantasy.fantasyapi.services.DraftService;
import com.fantasy.fantasyapi.services.CPUDraftingService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

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

    @Autowired
    private PlayerDetailsService playerDetailsService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private CPUDraftingService cpuDraftingService;

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
        
        System.out.println("== GET /draft ==");
        int numOfSelections = numOfRounds * numOfTeams;

        // Initialize draft using service
        DraftService.DraftInitializationResponse draftResponse = 
            draftService.initializeDraft(numOfTeams, mockTeamName, draftPosition, format);

        // Initialize draft state
        boolean isReversed = false;
        int currentPick = 1;
        int roundNumber = 1;
        int currentTeamId = 0;

        // Initialize picked players list in session
        List<DraftPlayer> pickedPlayers = new ArrayList<>();
        session.setAttribute("pickedPlayers", pickedPlayers);
        session.setAttribute("adpList", draftResponse.getDraftPlayers());
        session.setAttribute("mockTeams", draftResponse.getMockTeams());

        // Add attributes to model
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
        model.addAttribute("userId", draftResponse.getUserId());

        return "draftBoard";
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

            // Use CPU drafting service to select player
            selectedPlayer = cpuDraftingService.selectPlayerForCPU(adpList, currentTeam, roundNumber);
            
            if (selectedPlayer == null) {
                return ResponseEntity.badRequest().body("Failed to select CPU player");
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

        try {
            PlayerDetailsService.PlayerDetailsResponse response = playerDetailsService.getPlayerDetails(espnName);
            
            if (response != null) {
                model.addAttribute("playerDetails", response.getPlayerDetails());
                model.addAttribute("seasonFantasyPoints", response.getFantasyPoints());
                model.addAttribute("rushAvg", response.getRushAverage());
                model.addAttribute("gameStatsList", response.getGameStatsList());
                model.addAttribute("firstName", response.getFirstName());
                model.addAttribute("lastName", response.getLastName());
                
                return "player-details";
            } else {
                return "playerNotFound";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch player details.");
            return "error";
        }
    }



    @PostMapping("/team/player-details")
    public String getTeamPlayerByEspnName(@RequestParam("espnName") String espnName, Model model, HttpSession session) {
        System.out.println("In team-player-details");

        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        model.addAttribute("authenticatedUser", authenticatedUser);

        try {
            PlayerDetailsService.PlayerDetailsResponse response = playerDetailsService.getPlayerDetails(espnName);
            
            if (response != null) {
                model.addAttribute("playerDetails", response.getPlayerDetails());
                model.addAttribute("seasonFantasyPoints", response.getFantasyPoints());
                model.addAttribute("rushAvg", response.getRushAverage());
                model.addAttribute("gameStatsList", response.getGameStatsList());
                model.addAttribute("firstName", response.getFirstName());
                model.addAttribute("lastName", response.getLastName());
                
                return "team-player-details";
            } else {
                return "playerNotFound";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch player details.");
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
                    // Check if the team name does not start with "CPU Team" or "Mock Team" (User's Team)
                    if (!team.getTeamName().startsWith("CPU Team") && !team.getTeamName().startsWith("Mock Team")) {
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
