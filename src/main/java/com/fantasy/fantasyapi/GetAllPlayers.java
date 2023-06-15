package com.fantasy.fantasyapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/")
public class GetAllPlayers 
{
    //------------------------------------------------------------------------
    // PENDING BUGS/WHATS NEXT: ??
    // bug: idList contains 83 values, nodeListOfId only contains 80 values??
    // what's next: combine id and name value pairs into NFLPlayer objects and store in array (will be stored in database eventually?)
    //------------------------------------------------------------------------

    // WebClient used to formulate and send HTTPS requests to external API
    @Autowired
    WebClient.Builder builder;


    // Global roster variable to store JSON payload from API
    private String roster;

    @RequestMapping("getNFLRoster")
    public void getChiefsRoster()
    {
            // Params for team and abbreviation to be set in URL
            String teamID = "16";
            String teamAbv = "KC";
            String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLTeamRoster?teamID=" + teamID + "&teamAbv=" + teamAbv;

            // Populate global roster string with JSON payload using WebClient builder
		    roster = builder.build()
			.get()
			.uri(url)
			.header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
			.header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
			.retrieve()
			.bodyToMono(String.class).block();
            
            // method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    public void processRoster()
    {
        // ArrayLists to store substrings that contain name and id
        List<String> nameList = new ArrayList<String>();
        List<String> idList = new ArrayList<String>();

         // String array to parse JSON payload into separate lines
         String substrings[] = roster.split(", ");

            // iterate through lines and check if contains name or id
            for(int i = 0; i < substrings.length; i++)
            {
                if(substrings[i].contains("espnName"))
                {
                    String nameLine = "{" + substrings[i] + "}";
                    nameList.add(nameLine);
                }
                if(substrings[i].contains("playerID"))
                {
                    String nameLine = "{" + substrings[i];
                    idList.add(nameLine);
                }
            }

            //----------------------------------------------------
            // PRINT LISTS AFTER SPLITTING INTO SUBSTRING (TEST)   
            // for(int i = 0; i < idList.size(); i++)
            // {
            //     System.out.println(idList.get(i).toString());
            // }
            // for(int i = 0; i < nameList.size(); i++)
            // {
            //      System.out.println(nameList.get(i).toString());
            // }
            //----------------------------------------------------

            // ArrayLists to store JsonNodes (actual values) for id and name
            List<JsonNode> nodeListOfNames = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfId = new ArrayList<JsonNode>();

            // iterate through each line and parse out value and store in list
            for(int i = 0; i < nameList.size(); i++)
            {
                Mapper mapper = new Mapper();
                try
                {
                    JsonNode node = mapper.parse(nameList.get(i));
                    nodeListOfNames.add(node);
                }
                catch (JsonProcessingException e) 
                {
                    e.printStackTrace();
                }
            }
            for(int i = 0; i < idList.size(); i++)
            {
                Mapper mapper = new Mapper();
                try
                {
                    JsonNode node = mapper.parse(idList.get(i));
                    nodeListOfId.add(node);
                }
                catch (JsonProcessingException e) 
                {
                    e.printStackTrace();
                }
            }       

            //------------------------------------------------------------------------
            // PRINT LISTS AFTER FILTERING TO NAME AND ID VALUES (TEST)
            // for(int i = 0; i < nodeListOfNames.size(); i++)
            // {
            //     System.out.println(nodeListOfNames.get(i).get("espnName").asText());
            // }
            // for(int i = 0; i < nodeListOfId.size(); i++)
            // {
            //     System.out.println(nodeListOfId.get(i).get("playerID").asText());
            // }
            //------------------------------------------------------------------------
    }
}
