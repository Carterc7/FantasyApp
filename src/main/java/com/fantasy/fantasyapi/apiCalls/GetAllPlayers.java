package com.fantasy.fantasyapi.apiCalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.model.AdpPlayer;
import com.fantasy.fantasyapi.model.EspnPlayer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.netty.http.client.HttpClient;

public class GetAllPlayers 
{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<EspnPlayer> getFilteredPlayerList(int maxRange)
    {
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        List<AdpPlayer> adpList = new ArrayList<AdpPlayer>();
        List<EspnPlayer> filteredPlayerList = new ArrayList<EspnPlayer>();
        String jsonString = getAllPlayers.sendRequestGetAllPlayers();
        try 
        {
            List<EspnPlayer> allPlayersList = getAllPlayers.mapJsonToPlayerObject(jsonString);
            adpList = getDraftAdp.getFilteredAdpList(maxRange);
            for(AdpPlayer adpPlayer : adpList)
            {
                String nameCheck = adpPlayer.getName().toLowerCase();
                for(EspnPlayer espnPlayer : allPlayersList)
                {
                    if(espnPlayer.getEspnName().toLowerCase().contains(nameCheck))
                    {
                        if(espnPlayer.getPos().toLowerCase().equals("wr") || espnPlayer.getPos().toLowerCase().equals("rb") || espnPlayer.getPos().toLowerCase().equals("te") || espnPlayer.getPos().toLowerCase().equals("qb"))
                        {
                            filteredPlayerList.add(espnPlayer);
                        }
                    }
                }
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return filteredPlayerList;
    }

    // Method to read a jsonString and map to a EspnPlayer object model
    public List<EspnPlayer> mapJsonToPlayerObject(String jsonString) throws IOException 
    {
        // Read JSON payload using object mapper and store in a jsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Get the values of the "body" of the payload and store in another jsonNode
        JsonNode bodyNode = jsonNode.get("body");
        if (bodyNode == null || !bodyNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format. 'body' array not found.");
        }

        // Object mapper reads through the body jsonNode and maps each value to the EspnPlayer object model, then returned
        return Arrays.asList(objectMapper.readValue(bodyNode.toString(), EspnPlayer[].class));

        // ORIGINAL RETURN STATEMENT AS ARRAY
        // return objectMapper.readValue(bodyNode.toString(), EspnPlayer[].class);
    }

    // Method to send an HTTP request and retrieve a JSON payload string for all NFL players
    public String sendRequestGetAllPlayers()
    {
        // define request URL
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLPlayerList";
        // string for the payload to be stored in
        String roster = "";

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
        roster = mainBuilder
                .get()
                .uri(url)
                .header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
                .header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return roster;
    }

}
