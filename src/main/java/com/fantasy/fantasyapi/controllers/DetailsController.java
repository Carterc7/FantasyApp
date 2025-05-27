package com.fantasy.fantasyapi.controllers;

import com.fantasy.fantasyapi.objectModels.PlayerDetails;
import com.fantasy.fantasyapi.apiCalls.GetPlayerDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/details")
public class DetailsController {

    private final GetPlayerDetails getPlayerDetailsService;

    public DetailsController() {
        this.getPlayerDetailsService = new GetPlayerDetails();
    }

    @GetMapping
    public ResponseEntity<?> getPlayerDetails(@RequestParam int playerId) {
        try {
            PlayerDetails playerDetails = getPlayerDetailsService.getPlayerDetails(playerId);
            if(playerDetails.espnID != null) {
                System.out.println("Player found: " + playerDetails.espnName);
            }
            return ResponseEntity.ok(playerDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch player details: " + e.getMessage());
        }
    }
}

