package com.fantasy.fantasyapi.apiCalls;

import com.fantasy.fantasyapi.objectModels.PlayerDetails;
import com.fantasy.fantasyapi.objectModels.PlayerGameStats;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GetPlayerDetails {

    private static final Logger logger = Logger.getLogger(GetPlayerDetails.class.getName());

    private final String apiKey;
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public GetPlayerDetails() {
        // Try to load from .env (for local dev), then fallback to system env (Heroku)
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.load();
        } catch (Exception e) {
            logger.warning(".env file not found or failed to load, falling back to system environment variables.");
        }

        this.apiKey = getEnvVar("API_KEY", dotenv);
        this.baseUrl = getEnvVar("API_URL", dotenv);

        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private String getEnvVar(String key, Dotenv dotenv) {
        if (dotenv != null && dotenv.get(key) != null) {
            return dotenv.get(key);
        } else {
            String systemValue = System.getenv(key);
            if (systemValue == null) {
                logger.warning("Environment variable '" + key + "' is not set.");
            }
            return systemValue;
        }
    }

    public PlayerDetails getPlayerDetails(int playerId) throws IOException, InterruptedException {
        PlayerDetails details = fetchPlayerInfo(playerId);
        Map<String, PlayerGameStats.GameData> gameStats = fetchPlayerGameStats(playerId);

        if (details != null && gameStats != null) {
            details.setGameStats(gameStats); // Ensure this method exists in PlayerDetails
        }
        return details;
    }

    private PlayerDetails fetchPlayerInfo(int playerId) throws IOException, InterruptedException {
        String url = baseUrl + "/getNFLPlayerInfo?playerID=" + playerId + "&getStats=true";
        logger.info("Fetching player details from URL: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", URI.create(baseUrl).getHost())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("HTTP Response Code: " + response.statusCode());

        JsonNode root = mapper.readTree(response.body());
        JsonNode playerDataNode = root.path("body");

        if (playerDataNode.isNull() || playerDataNode.isMissingNode()) {
            logger.warning("No player data found.");
            return null;
        }

        return mapper.treeToValue(playerDataNode, PlayerDetails.class);
    }

    private Map<String, PlayerGameStats.GameData> fetchPlayerGameStats(int playerId) throws IOException, InterruptedException {
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLGamesForPlayer?playerID=" + playerId +
                "&season=2024&fantasyPoints=true&twoPointConversions=2&passYards=.04&passTD=4&passInterceptions=-2&pointsPerReception=1&carries=.2" +
                "&rushYards=.1&rushTD=6&fumbles=-2&receivingYards=.1&receivingTD=6&targets=0&defTD=6&xpMade=1&xpMissed=-1&fgMade=3&fgMissed=-3";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());
        JsonNode bodyNode = root.path("body");

        if (!bodyNode.isObject()) {
            logger.warning("Game stats body is not an object");
            return null;
        }

        Map<String, PlayerGameStats.GameData> gameStatsMap = new HashMap<>();
        bodyNode.fields().forEachRemaining(entry -> {
            try {
                gameStatsMap.put(entry.getKey(), mapper.treeToValue(entry.getValue(), PlayerGameStats.GameData.class));
            } catch (Exception e) {
                logger.warning("Failed to parse game stat for key " + entry.getKey() + ": " + e.getMessage());
            }
        });

        return gameStatsMap;
    }
}


