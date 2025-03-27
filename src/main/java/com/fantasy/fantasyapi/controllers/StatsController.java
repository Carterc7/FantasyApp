package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetPlayerStats;
import com.fantasy.fantasyapi.objectModels.StatsPlayer;
import com.fantasy.fantasyapi.points.PointsCalculator;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class StatsController {

    @Autowired
    private GetPlayerStats getPlayerStats;


    @GetMapping("/season/{playerName}/{teamAbv}/{seasonYear}")
    public List<StatsPlayer> getPlayerStatsForSeason(
            @PathVariable String playerName,
            @PathVariable String teamAbv,
            @PathVariable String seasonYear) {

        // Fetch stats for the player across the entire season
        List<StatsPlayer> seasonStats = getPlayerStats.getPlayerStatsForSeason(playerName, teamAbv, seasonYear);

        return seasonStats;
    }

    /**
     * @param gameID
     * @return List<StatsPlayer>
     */
    @GetMapping("/{gameID}")
    public List<StatsPlayer> getPlayerStatsByGameID(@PathVariable String gameID) {
        // initialize objects and lists
        List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
        playerStats = getPlayerStats.getPlayerStatsByGameID(gameID);
        return playerStats;
    }

    /**
     * @param gameID
     * @param playerName
     * @return StatsPlayer
     */
    @GetMapping("/player/{gameID}/{playerName}")
    public StatsPlayer getSinglePlayerStatsByGameID(@PathVariable String gameID, @PathVariable String playerName) {
        return getPlayerStats.getSinglePlayerStatsByGameID(gameID, playerName);
    }

    /**
     * @param gameID
     * @param playerName
     * @return StatsPlayer
     */
    @GetMapping("/points/{gameID}/{playerName}")
    public StatsPlayer getSinglePlayerPointsByGameID(@PathVariable String gameID, @PathVariable String playerName) {
        StatsPlayer player = new StatsPlayer();
        player = getPlayerStats.getSinglePlayerStatsByGameID(gameID, playerName);
        PointsCalculator calculatorPPR = new PointsCalculator();
        double points = calculatorPPR.calculateFantasyPoints(player);
        player.setFantasyPoints(points);
        return player;
    }

    /**
     * @param gameID
     * @return List<StatsPlayer>
     */
    @GetMapping("/points/all/{gameID}")
    public List<StatsPlayer> getPlayerPointsByGameID(@PathVariable String gameID) {
        List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
        playerStats = getPlayerStats.getPlayerStatsByGameID(gameID);
        PointsCalculator calculatorPPR = new PointsCalculator();
        for (StatsPlayer player : playerStats) {
            double points = calculatorPPR.calculateFantasyPoints(player);
            player.setFantasyPoints(points);
        }
        return playerStats;
    }
}
