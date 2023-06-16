/*
 * Carter Campbell
 * Software Developer
 * 6/16/23
 * GET NFL Team Rosters/Players
 */
package com.fantasy.fantasyapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.model.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/roster")
public class GetAllPlayers 
{
    //------------------------------------------------------------------------
    // PENDING BUGS/WHATS NEXT: ??
    // N/A
    //------------------------------------------------------------------------

    // WebClient used to formulate and send HTTPS requests to external API
    @Autowired
    WebClient.Builder builder;


    // Global roster variable to store JSON payload from API
    private String roster;

    // method to send HTTP "GET" request to API and store response in global variable
    public void sendRequest(String teamAbv)
    {
        // dynamic url to send request with specified team abv
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLTeamRoster?teamAbv=" + teamAbv;

            // Populate global roster string with JSON payload using WebClient builder
		    roster = builder.build()
			.get()
			.uri(url)
			.header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
			.header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
			.retrieve()
			.bodyToMono(String.class).block();
    }

    // method to process string JSON payload and parse down to name and id pairs that are stored in Player objects
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

            // iterate through node lists and create player objects mapping name and id together and store in FINAL list
            List<Player> players = new ArrayList<Player>();
            for(int i = 0; i < nodeListOfNames.size(); i++)
            {
                // create object and add live data fields
                Player player = new Player(nodeListOfNames.get(i).get("espnName").asText(), nodeListOfId.get(i).get("playerID").asText());
                // add player object to list
                players.add(player);
            }
            // PRINT FINAL LIST OBJECTS NAME AND ID (TEST)
            for(int i = 0; i < players.size(); i++)
            {
                System.out.println("-----------------------------------------------");
                System.out.println(players.get(i).getplayerName() + ", " + players.get(i).getplayerID());
            }
    }

    @RequestMapping("/chiefs")
    public void getChiefsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "KC";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/chargers")
    public void getChargersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LAC";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/raiders")
    public void getRaidersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LV";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/broncos")
    public void getBroncosRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DEN";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/patriots")
    public void getPatriotsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NE";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/dolphins")
    public void getDolphinsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "MIA";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/bills")
    public void getBillsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "BUF";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/jets")
    public void getJetsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NYJ";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/ravens")
    public void getRavensRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "BAL";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/bengals")
    public void getBengalsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CIN";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/browns")
    public void getBrownsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CLE";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/steelers")
    public void getSteelersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "PIT";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/texans")
    public void getTexansRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "HOU";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/colts")
    public void getColtsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "IND";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/titans")
    public void getTitansRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "TEN";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/jaguars")
    public void getJaguarsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "JAX";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/bears")
    public void getBearsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CHI";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/lions")
    public void getLionsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DET";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/packers")
    public void getPackersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "GB";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/vikings")
    public void getVikingsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "MIN";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/cowboys")
    public void getCowboysRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DAL";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/giants")
    public void getGiantsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NYG";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/eagles")
    public void getEaglesRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "PHI";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/commanders")
    public void getCommandersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "WSH";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/falcons")
    public void getFalconsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "ATL";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/panthers")
    public void getPanthersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CAR";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/saints")
    public void getSaintsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NO";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/buccaneers")
    public void getBuccaneersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "TB";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/cardinals")
    public void getCardinalsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "ARI";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/rams")
    public void getRamsRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LAR";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/49ers")
    public void get49ersRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "SF";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

    @RequestMapping("/seahawks")
    public void getSeahawksRoster()
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "SEA";
            // Send get request with correct team abv
            sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            processRoster();
    }

}
