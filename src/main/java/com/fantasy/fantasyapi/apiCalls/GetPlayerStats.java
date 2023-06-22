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
    public List<StatsPlayer> getPlayerStatsByGameID(String gameID)
    {
        GetPlayerStats getPlayerStats = new GetPlayerStats();
        String json = getPlayerStats.sendRequestGetGameStats(gameID);
        List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
        try 
        {
            playerStats = getPlayerStats.mapJsonToPlayerObject(json);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to retrieve player stats at gameID: " + gameID);
        }
        return playerStats;
    }

    // Method to send an HTTP request and retrieve a JSON payload string for player stats on a specific gameID
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

    // Method to read a jsonString and map to a EspnPlayer object model
    public List<StatsPlayer> mapJsonToPlayerObject(String jsonString) throws IOException 
    {
        ObjectMapper objectMapper = new ObjectMapper();
        List<StatsPlayer> players = new ArrayList<StatsPlayer>();
        // Read JSON payload using object mapper and store in a jsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode bodyNode = jsonNode.get("body");
        // Get the values of the "body" of the payload and store in another jsonNode
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
        return players;
    }

    public static void main(String[] args)
    {
        // GetPlayerStats getStats = new GetPlayerStats();
        // String json = getStats.sendRequestGetGameStats("20221016_BUF@KC");
        // List<StatsPlayer> players = new ArrayList<StatsPlayer>();
        // List<EspnPlayer> players2 = new ArrayList<EspnPlayer>();
        // GetAllPlayers getAllPlayers = new GetAllPlayers();
        // players2 = getAllPlayers.getFilteredPlayerList(100);
        // try 
        // {
        //     players = getStats.mapJsonToPlayerObject(json);
        // } 
        // catch (IOException e) 
        // {
        //     e.printStackTrace();
        // }
        // for(StatsPlayer player : players)
        // {
        //     String check = player.getPlayerID();
        //     for(EspnPlayer player2 : players2)
        //     {
        //         if(check.equals(player2.getPlayerID()))
        //         {
        //             System.out.println(player.getLongName() + " " + player.getRecYds() + " " + player.getRushYds());
        //         }
        //     }
        // }
    }

}
