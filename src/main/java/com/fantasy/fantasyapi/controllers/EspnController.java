package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.model.EspnPlayer;
import com.fantasy.fantasyapi.repository.EspnPlayerRepository;

@RestController
@RequestMapping("/espn")
public class EspnController 
{
    @Autowired
    private EspnPlayerRepository espnPlayerRepository;

    @GetMapping("/{playerID}")
    public Optional<EspnPlayer> findAdpPlayerByPlayerID(@PathVariable String playerID)
    {
        return espnPlayerRepository.findPlayerByPlayerID(playerID);
    }

    @GetMapping("/list/{range}")
    public List<EspnPlayer> getFilteredEspnList(@PathVariable int range)
    {
        List<EspnPlayer> players = new ArrayList<EspnPlayer>();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        players = getAllPlayers.getFilteredPlayerList(range);
        return players;
    }
}
