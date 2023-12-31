package com.fantasy.fantasyapi.apiCalls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.netty.http.client.HttpClient;

public class GetDraftAdp 
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 
     * Method returns a list of "AdpPlayers" sorted by adp
     * maxRange is number of "AdpPlayers" in the list
     * List is up-to-date pulled directly from API and returned
     * @param maxRange
     * @return List<AdpPlayer>
     */
    public List<AdpPlayer> getFilteredAdpList(int maxRange)
    {
       GetDraftAdp getAdp = new GetDraftAdp();
       String jsonString = getAdp.sendRequestGetAdp();
       List<AdpPlayer> adpList = getAdp.mapJsonToAdpPlayer(jsonString);
       List<AdpPlayer> filteredList = getAdp.adpFilter(adpList, maxRange);
       return filteredList;
    }
    
    /** 
     * Method sends HTTP request and returns "String" payload from API
     * payload is unprocessed, raw JSON
     * @return String
     */
    public String sendRequestGetAdp()
    {
        String adp = "";
        String url = "https://api.sportsdata.io/api/nfl/fantasy/json/FantasyPlayers?key=68e752bc607c47d9bfee6650e6ee9ae9";
        
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
        adp = mainBuilder
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return adp;
    }
    
    /** 
     * Method to parse JSON from sendRequestGetAdp() and form "AdpPlayer" objects
     * objects are stored in a list and returned
     * @param jsonString
     * @return List<AdpPlayer>
     */
    public List<AdpPlayer> mapJsonToAdpPlayer(String jsonString)
    { 
        // initialize list to be returned
        List<AdpPlayer> adpList = new ArrayList<AdpPlayer>();
            try 
            {
                // read json string and store in an array of AdpPlayer objects
                AdpPlayer[] adpArray = objectMapper.readValue(jsonString, AdpPlayer[].class);
                // convert array to list
                adpList = Arrays.asList(adpArray);
            }
            catch (JsonProcessingException e) 
            {   
                e.printStackTrace();
            }
        return adpList;
    }
    
    /** 
     * Method to filter given list by adp values
     * maxRange is number of "AdpPlayers" in the list
     * @param listToFilter
     * @param maxRange
     * @return List<AdpPlayer>
     */
    public List<AdpPlayer> adpFilter(List<AdpPlayer> listToFilter, int maxRange)
    {
        // initializing list to be returned
        List<AdpPlayer> filteredAdp = new ArrayList<AdpPlayer>();
        // iterate through the list
        for(AdpPlayer player : listToFilter)
        {
                // check if adp is between 0 and max range
                if(player.getAverageDraftPositionPPR() > 0 && player.getAverageDraftPositionPPR() < (maxRange+0.1))
                {
                    if(player.getPosition().toLowerCase().equals("wr") || player.getPosition().toLowerCase().equals("rb") || player.getPosition().toLowerCase().equals("te") || player.getPosition().toLowerCase().equals("qb"))
                    // add to list to be returned
                    filteredAdp.add(player);
                }
        }
        // sorts the list by ppr adp in ascending order
        Collections.sort(filteredAdp, Comparator.comparingInt(AdpPlayer::getAverageDraftPositionPPR));
        return filteredAdp;
    }
}
