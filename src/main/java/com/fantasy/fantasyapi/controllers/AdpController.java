package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.model.AdpPlayer;
import com.fantasy.fantasyapi.repository.AdpPlayerRepository;

@RestController
@RequestMapping("/adp")
public class AdpController 
{
    @Autowired
    AdpPlayerRepository adpPlayerRepository;

    /** 
     * @param playerID
     * @return Optional<AdpPlayer>
     */
    @GetMapping("/player/{playerID}")
    public Optional<AdpPlayer> findAdpPlayerByPlayerID(@PathVariable String playerID)
    {
        return adpPlayerRepository.findPlayerByPlayerID(playerID);
    }

    /** 
     * @param averageDraftPositionPPR
     * @return Optional<AdpPlayer>
     */
    @GetMapping("/ppr/{averageDraftPositionPPR}")
    public Optional<AdpPlayer> findPlayerByAverageDraftPositionPPR(@PathVariable int averageDraftPositionPPR)
    {
        return adpPlayerRepository.findPlayerByAverageDraftPositionPPR(averageDraftPositionPPR);
    }

    /** 
     * @param range
     * @return List<AdpPlayer>
     */
    @GetMapping("/list/{range}")
    public List<AdpPlayer> getFilteredAdpList(@PathVariable int range)
    {
        List<AdpPlayer> players = new ArrayList<AdpPlayer>();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        players = getDraftAdp.getFilteredAdpList(range);
        return players;
    }

    /** 
     * @return List<AdpPlayer>
     */
    @GetMapping("/list/all")
    public List<AdpPlayer> getAllFilteredAdpList()
    {
        return adpPlayerRepository.findAll();
    }
}
