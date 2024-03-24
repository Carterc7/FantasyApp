package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.mongoServices.AdpPlayerService;
import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import com.fantasy.fantasyapi.repository.AdpPlayerRepository;

// Was @RestController before thymeleaf template
@Controller
@RequestMapping("/adp")
@CrossOrigin(origins = "http://localhost:3000")
public class AdpController 
{
    @Autowired
    AdpPlayerRepository adpPlayerRepository;

    @Autowired
    AdpPlayerService adpPlayerService;

    @GetMapping("/list")
    public String showAdpList(Model model)
    {
        List<AdpPlayer> players = new ArrayList<AdpPlayer>();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        players = getDraftAdp.getFilteredAdpList(300);
        model.addAttribute("players", players);
        return "players.html";
    }

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

    /** 
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllAdpPlayers() 
    {
        try 
        {
            adpPlayerRepository.deleteAll();
            return ResponseEntity.ok("All AdpPlayers deleted successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete AdpPlayers. Error: " + e.getMessage());
        }
    }

    /** 
     * @return ResponseEntity<String>
     */
    @PostMapping("/add-all")
    public ResponseEntity<String> addAllAdpPlayers() 
    {
        List<AdpPlayer> players = new ArrayList<AdpPlayer>();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        players = getDraftAdp.getFilteredAdpList(300);
        try 
        {
            adpPlayerRepository.saveAll(players);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("All AdpPlayers added successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add AdpPlayers. Error: " + e.getMessage());
        }
    }
    
    /** 
     * @return ResponseEntity<String>
     */
    @PutMapping("/update-all")
    public ResponseEntity<String> updateAdpPlayers()
    {
        List<AdpPlayer> players = new ArrayList<AdpPlayer>();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        players = getDraftAdp.getFilteredAdpList(300);
        for(AdpPlayer player : players)
        {
            try
            {
                adpPlayerService.updateAdpPlayer(player);
                System.out.println(player.getName() + " was just updated.");
            }
            catch(Exception e)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to updated AdpPlayers. Error: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body("All AdpPlayers updated successfully.");
    }
}
