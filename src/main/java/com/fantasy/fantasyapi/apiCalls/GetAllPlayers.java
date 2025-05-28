package com.fantasy.fantasyapi.apiCalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import reactor.netty.http.client.HttpClient;

public class GetAllPlayers {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey;
    private final String baseUrl;

    public GetAllPlayers() {
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.load();
        } catch (Exception e) {
            System.out.println("Dotenv file not found, using system environment variables.");
        }

        this.apiKey = dotenv != null && dotenv.get("API_KEY") != null
                ? dotenv.get("API_KEY")
                : System.getenv("API_KEY");

        this.baseUrl = dotenv != null && dotenv.get("API_URL") != null
                ? dotenv.get("API_URL")
                : System.getenv("API_URL");
    }

    /**
     * Method to get a filtered "EspnPlayer" list based off current "AdpPlayer" adp
     */
    public List<EspnPlayer> getFilteredPlayerList() {
        List<EspnPlayer> players = new ArrayList<>();
        String text = sendRequestGetAllPlayers();
        try {
            players = mapJsonToPlayerObject(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    /**
     * Parse jsonString returned from sendRequestGetAllPlayers() into EspnPlayer objects
     */
    public List<EspnPlayer> mapJsonToPlayerObject(String jsonString) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode bodyNode = jsonNode.get("body");
        if (bodyNode == null || !bodyNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format. 'body' array not found.");
        }
        return Arrays.asList(objectMapper.readValue(bodyNode.toString(), EspnPlayer[].class));
    }

    /**
     * Send HTTP request to GetAllPlayers endpoint
     */
    public String sendRequestGetAllPlayers() {
        String url = baseUrl + "/getNFLPlayerList";
        String roster;

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                .build();

        WebClient mainBuilder = WebClient.builder()
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();

        roster = mainBuilder
                .get()
                .uri(url)
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return roster;
    }
}

