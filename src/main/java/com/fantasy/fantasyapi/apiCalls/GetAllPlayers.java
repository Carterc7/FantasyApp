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

    /** 
     * Method to get a filtered "EspnPlayer" list based off current "AdpPlayer" adp
     * Range is amount of "EspnPlayers" returned in the list
     * @param maxRange
     * @return List<EspnPlayer>
     */
    public List<EspnPlayer> getFilteredPlayerList()
    {
       List<EspnPlayer> players = new ArrayList<EspnPlayer>();
       String text = sendRequestGetAllPlayers();
       try {
       players = mapJsonToPlayerObject(text);
       } catch (IOException e) {
        e.printStackTrace();
       }
       return players;
       
    }

    /**
     * Method to parse jsonString returned from sendRequestGetAllPlayers() and form
     * "EspnPlayer" objects
     * Returns a list of unfiltered players (~3500 total)
     * 
     * @param jsonString
     * @return List<EspnPlayer>
     * @throws IOException
     */
    public List<EspnPlayer> mapJsonToPlayerObject(String jsonString) throws IOException {
        // Read JSON payload using object mapper and store in a jsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Get the values of the "body" of the payload and store in another jsonNode
        JsonNode bodyNode = jsonNode.get("body");
        if (bodyNode == null || !bodyNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format. 'body' array not found.");
        }

        // Object mapper reads through the body jsonNode and maps each value to the
        // EspnPlayer object model, then returned
        return Arrays.asList(objectMapper.readValue(bodyNode.toString(), EspnPlayer[].class));

        // ORIGINAL RETURN STATEMENT AS ARRAY
        // return objectMapper.readValue(bodyNode.toString(), EspnPlayer[].class);
    }

    /**
     * Method to send HTTP request to "GetAllPlayers" endpoint and returns the
     * payload as a String
     * String can be parsed using the mapJsonToPlayerObject() method
     * 
     * @return String
     */
    public String sendRequestGetAllPlayers() {
        // load dotenv to get hidden variables
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("API_KEY");
        String baseUrl = dotenv.get("API_URL");
        // define request URL
        String url = baseUrl + "/getNFLPlayerList";
        // string for the payload to be stored in
        String roster = "";

        // JSON payload at this request URL is larger than default capacity
        // Create a custom ExchangeStrategies object with an increased buffer limit
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)) // 20 MB buffer
                                                                                                    // limit
                .build();

        // Create a WebClient using the custom ExchangeStrategies
        WebClient mainBuilder = WebClient.builder()
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();

        // Retrieve the JSON payload and store it in the roster string to be returned
        roster = mainBuilder
                .get()
                .uri(url)
                .header("X-RapidAPI-Key", key)
                .header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return roster;
    }

}
