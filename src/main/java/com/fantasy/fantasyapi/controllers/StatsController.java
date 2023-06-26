package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetPlayerStats;
import com.fantasy.fantasyapi.model.StatsPlayer;

@RestController
@RequestMapping("/stats")
public class StatsController 
{
    @GetMapping("/{gameID}")
    public List<StatsPlayer> getPlayerStatsByGameID(@PathVariable String gameID)
    {
        // initialize objects and lists
        GetPlayerStats getPlayerStats = new GetPlayerStats();
        List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
        playerStats = getPlayerStats.getPlayerStatsByGameID(gameID);
        return playerStats;
    }
}
