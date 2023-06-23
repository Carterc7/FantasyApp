package com.fantasy.fantasyapi.apiCalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import com.fantasy.fantasyapi.model.StatsPlayer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.netty.http.client.HttpClient;

public class GetPlayerStats 
{
    /**
     * Method to get all stats for players at a specific gameID
     * @param gameID
     * @return List<StatsPlayer>
     */
    public List<StatsPlayer> getPlayerStatsByGameID(String gameID)
    {
        // initialize objects and lists
        GetPlayerStats getPlayerStats = new GetPlayerStats();
        List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
        // get and store json payload for player stats at specified gameID
        String json = getPlayerStats.sendRequestGetGameStats(gameID);
        try 
        {
            // map json to objects and store in list of objects
            playerStats = getPlayerStats.mapJsonToPlayerObject(json);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to retrieve player stats at gameID: " + gameID);
        }
        return playerStats;
    }

    /** 
     * Method to get stats for a single player at a specific gameID
     * @param gameID
     * @param playerName
     * @return StatsPlayer
     */
    public StatsPlayer getSinglePlayerStatsByGameID(String gameID, String playerName)
    {
        // Initialize objects and lists
        GetPlayerStats getStats = new GetPlayerStats();
        StatsPlayer result = new StatsPlayer();
        List<StatsPlayer> players = new ArrayList<StatsPlayer>();
        // get list of players that play in specific game by ID
        players = getStats.getPlayerStatsByGameID(gameID);
        // iterate through each player and check if it matches player name
        for(StatsPlayer player : players)
        {
            if(player.getLongName().toLowerCase().trim().equals(playerName.toLowerCase().trim()))
            {
                // if match, set that player to result
                result = player;
            }
        }
        if(result.getPlayerID() == null)
        {
            System.out.println(playerName + " stats not found for game " + gameID);
        }
        return result;
    }

    /**
     * Method to send HTTP request and return JSON payload for specified gameID
     * payload is unprocessed, raw JSON
     * @param gameID
     * @return String
     */
    public String sendRequestGetGameStats(String gameID)
    {
        String jsonString = "";
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLBoxScore?gameID=" + gameID;
        
        // JSON payload at this request URL is larger than default capacity
        // Create a custom ExchangeStrategies object with an increased buffer limit
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(3 * 1024 * 1024)) // 3 MB buffer limit
                .build();

        // Create a WebClient using the custom ExchangeStrategies
        WebClient mainBuilder = WebClient.builder()
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();

        // Retrieve the JSON payload and store it in the roster string to be returned
        jsonString = mainBuilder
                .get()
                .uri(url)
                .header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
                .header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return jsonString;
    }

    /** 
     * Method to parse JSON from sendRequestGetGameStats() and form "StatsPlayer" objects
     * objects are stored in a list and returned
     * @param jsonString
     * @return List<StatsPlayer>
     * @throws IOException
     */
    public List<StatsPlayer> mapJsonToPlayerObject(String jsonString) throws IOException 
    {
        ObjectMapper objectMapper = new ObjectMapper();
        List<StatsPlayer> players = new ArrayList<StatsPlayer>();
        // Read JSON payload using object mapper and store in a jsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode bodyNode = jsonNode.get("body");
        // Get the values of the "body" of the payload and store in another jsonNode
        try{
        JsonNode playerStatsNode = bodyNode.get("playerStats");
        playerStatsNode.fields().forEachRemaining(entry -> {
            JsonNode playerDataNode = entry.getValue();
            String receptions = "";
            String recTD = "";
            String recYds = "";
            String targets = "";
            String rushYds = "";
            String carries = "";
            String rushTd = "";
            String passAttempts = "";
            String passCompletions = "";
            String passTd = "";
            String passYds = "";
            String playerID = "";
            String gameID = "";
            String team = "";
            String longName = "";
            // Extract the necessary fields from the playerDataNode
            if(playerDataNode.has("playerID"))
            {
                playerID = playerDataNode.get("playerID").asText();
            }
            if(playerDataNode.has("gameID"))
            {
                gameID = playerDataNode.get("gameID").asText();
            }
            if(playerDataNode.has("team"))
            {
                team = playerDataNode.get("team").asText();
            }
            if(playerDataNode.has("longName"))
            {
                longName = playerDataNode.get("longName").asText();
            }
            else if(playerDataNode.has("playerName"))
            {
                longName = playerDataNode.get("playerName").asText();
            }
            if(playerDataNode.has("Receiving"))
            {
                receptions = playerDataNode.path("Receiving").get("receptions").asText();
                recTD = playerDataNode.path("Receiving").get("recTD").asText();
                recYds = playerDataNode.path("Receiving").get("recYds").asText();
                targets = playerDataNode.path("Receiving").get("targets").asText();
            }

            if(playerDataNode.has("Rushing"))
            {
                rushYds = playerDataNode.path("Rushing").get("rushYds").asText();
                carries = playerDataNode.path("Rushing").get("carries").asText();
                rushTd = playerDataNode.path("Rushing").get("rushTD").asText();
            }

            if(playerDataNode.has("Passing"))
            {
                passAttempts = playerDataNode.path("Passing").get("passAttempts").asText();
                passCompletions = playerDataNode.path("Passing").get("passCompletions").asText();
                passTd = playerDataNode.path("Passing").get("passTD").asText();
                passYds = playerDataNode.path("Passing").get("passYds").asText();
            }
            // Create a new PlayerStats object and add it to the list
            StatsPlayer playerStats = new StatsPlayer(playerID, gameID, team, longName, receptions, recTD, recYds, targets, rushYds, carries, rushTd, passAttempts, passCompletions, passYds, passTd, receptions);
            players.add(playerStats);
        });
      }
        catch(NullPointerException e)
        {
            System.out.println("Player stats could not be found. Please try a different gameID or player");
        }
        return players;
    }

}
