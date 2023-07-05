package com.fantasy.fantasyapi.apiCalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.objectModels.StatsPlayer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import reactor.netty.http.client.HttpClient;

public class GetPlayerStats 
{
    // public static void main(String[] args)
    // {
    //     // initialize objects and lists
    //     GetPlayerStats getPlayerStats = new GetPlayerStats();
    //     List<StatsPlayer> playerStats = new ArrayList<StatsPlayer>();
    //     // get and store json payload for player stats at specified gameID
    //     String json = getPlayerStats.sendRequestGetGameStats("20221016_BUF@KC");
    //     System.out.println(json);
    // }

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
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("API_KEY");
        String baseUrl = dotenv.get("API_URL");
        String jsonString = "";
        String url = baseUrl + "/getNFLBoxScore?gameID=" + gameID;
        
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
                .header("X-RapidAPI-Key", key)
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
        List<StatsPlayer> players = new ArrayList<>();
        // Read JSON payload using object mapper and store in a jsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode bodyNode = jsonNode.get("body");
        // Get the values of the "body" of the payload and store in another jsonNode
        try 
        {
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
                String totalTackles = "";
                String defTD = "";
                String soloTackles = "";
                String tfls = "";
                String qbHits = "";
                String defInterceptions = "";
                String sacks = "";
                String passDeflections = "";
                String interceptions = "";
                double fantasyPoints = 0;
                // Extract the necessary fields from the playerDataNode
                if (playerDataNode.has("playerID")) {
                    playerID = playerDataNode.get("playerID").asText();
                }
                if (playerDataNode.has("gameID")) {
                    gameID = playerDataNode.get("gameID").asText();
                }
                if (playerDataNode.has("team")) {
                    team = playerDataNode.get("team").asText();
                }
                if (playerDataNode.has("longName")) {
                    longName = playerDataNode.get("longName").asText();
                } else if (playerDataNode.has("playerName")) {
                    longName = playerDataNode.get("playerName").asText();
                }
                // Extract "Receiving" fields
                if (playerDataNode.has("Receiving")) {
                    JsonNode receivingNode = playerDataNode.get("Receiving");
                    receptions = receivingNode.has("receptions") ? receivingNode.get("receptions").asText() : "";
                    recTD = receivingNode.has("recTD") ? receivingNode.get("recTD").asText() : "";
                    recYds = receivingNode.has("recYds") ? receivingNode.get("recYds").asText() : "";
                    targets = receivingNode.has("targets") ? receivingNode.get("targets").asText() : "";
                }
                // Extract "Rushing" fields
                if (playerDataNode.has("Rushing")) {
                    JsonNode rushingNode = playerDataNode.get("Rushing");
                    rushYds = rushingNode.has("rushYds") ? rushingNode.get("rushYds").asText() : "";
                    carries = rushingNode.has("carries") ? rushingNode.get("carries").asText() : "";
                    rushTd = rushingNode.has("rushTD") ? rushingNode.get("rushTD").asText() : "";
                }
                // Extract "Passing" fields
                if (playerDataNode.has("Passing")) {
                    JsonNode passingNode = playerDataNode.get("Passing");
                    passAttempts = passingNode.has("passAttempts") ? passingNode.get("passAttempts").asText() : "";
                    passCompletions = passingNode.has("passCompletions") ? passingNode.get("passCompletions").asText() : "";
                    passTd = passingNode.has("passTD") ? passingNode.get("passTD").asText() : "";
                    passYds = passingNode.has("passYds") ? passingNode.get("passYds").asText() : "";
                    interceptions = passingNode.has("int") ? passingNode.get("int").asText() : "";
                }
                // Extract "Defense" fields
                if (playerDataNode.has("Defense")) {
                    JsonNode defenseNode = playerDataNode.get("Defense");
                    totalTackles = defenseNode.has("totalTackles") ? defenseNode.get("totalTackles").asText() : "";
                    defTD = defenseNode.has("defTD") ? defenseNode.get("defTD").asText() : "";
                    soloTackles = defenseNode.has("soloTackles") ? defenseNode.get("soloTackles").asText() : "";
                    tfls = defenseNode.has("tfl") ? defenseNode.get("tfl").asText() : "";
                    qbHits = defenseNode.has("qbHits") ? defenseNode.get("qbHits").asText() : "";
                    defInterceptions = defenseNode.has("defensiveInterceptions") ? defenseNode.get("defensiveInterceptions").asText() : "";
                    sacks = defenseNode.has("sacks") ? defenseNode.get("sacks").asText() : "";
                    passDeflections = defenseNode.has("passDeflections") ? defenseNode.get("passDeflections").asText() : "";
                }
                // Create a new PlayerStats object and add it to the list
                StatsPlayer playerStats = new StatsPlayer(playerID, gameID, team, longName, receptions, recTD, recYds,
                        targets, rushYds, carries, rushTd, passAttempts, passCompletions, passYds, passTd, totalTackles,
                        defTD, soloTackles, tfls, qbHits, defInterceptions, sacks, passDeflections, interceptions, fantasyPoints);
                players.add(playerStats);
            });
        } 
        catch (Exception e) 
        {
            // Handle any specific exceptions or log the error message
            e.printStackTrace(); // Example: Printing the stack trace
        }
        return players;
}


}
