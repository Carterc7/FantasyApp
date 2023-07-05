package com.fantasy.fantasyapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.mongoServices.EspnPlayerService;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.repository.EspnPlayerRepository;


@RestController
@RequestMapping("/espn")
public class EspnController 
{
    @Autowired
    private EspnPlayerRepository espnPlayerRepository;

    @Autowired
    private EspnPlayerService espnPlayerService;

    /** 
     * @param playerID
     * @return Optional<EspnPlayer>
     */
    @GetMapping("player/{playerID}")
    public Optional<EspnPlayer> findPlayerByPlayerID(@PathVariable String playerID)
    {
        return espnPlayerRepository.findPlayerByPlayerID(playerID);
    }

    /** 
     * @param range
     * @return List<EspnPlayer>
     */
    @GetMapping("/list/{range}")
    public List<EspnPlayer> getFilteredEspnList(@PathVariable int range)
    {
        List<EspnPlayer> players = new ArrayList<EspnPlayer>();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        players = getAllPlayers.getFilteredPlayerList(range);
        return players;
    }
    
    /** 
     * @return List<EspnPlayer>
     */
    @GetMapping("/list/all")
    public List<EspnPlayer> getAllFilteredEspnList()
    {
        return espnPlayerRepository.findAll();
    }

     /** 
      * @return ResponseEntity<String>
      */
     @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllEspnPlayers() 
    {
        try 
        {
            espnPlayerRepository.deleteAll();
            return ResponseEntity.ok("All EspnPlayers deleted successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete EspnPlayers. Error: " + e.getMessage());
        }
    }

    /** 
     * @return ResponseEntity<String>
     */
    @PostMapping("/add-all")
    public ResponseEntity<String> addAllEspnPlayers() 
    {
        List<EspnPlayer> players = new ArrayList<EspnPlayer>();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        players = getAllPlayers.getFilteredPlayerList(300);
        try 
        {
            espnPlayerRepository.saveAll(players);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("All EspnPlayers added successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add EspnPlayers. Error: " + e.getMessage());
        }
    }

    /** 
     * @return ResponseEntity<String>
     */
    @PutMapping("/update-all")
    public ResponseEntity<String> updateEspnPlayers()
    {
        List<EspnPlayer> players = new ArrayList<EspnPlayer>();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        players = getAllPlayers.getFilteredPlayerList(300);
        for(EspnPlayer player : players)
        {
            try
            {
                espnPlayerService.updateEspnPlayer(player);
                System.out.println(player.getEspnName() + " was just updated.");
            }
            catch(Exception e)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to updated EspnPlayers. Error: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body("All EspnPlayers updated successfully.");
    }
}
