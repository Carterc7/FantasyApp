/*
 * Carter Campbell
 * Software Developer
 * 6/16/23
 * GET NFL Team Rosters/Players
 */
package com.fantasy.fantasyapi.apiCalls;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.model.BasePlayer;
import com.fantasy.fantasyapi.utility.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;


public class GetTeamRosters 
{
    //------------------------------------------------------------------------
    // PENDING BUGS/WHATS NEXT: ??
    // N/A
    //------------------------------------------------------------------------  

    // method to send HTTP "GET" request to API and store response in global variable
    public String sendRequest(String teamAbv)
    {
        // dynamic url to send request with specified team abv
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLTeamRoster?teamAbv=" + teamAbv;
        String roster = "";
        WebClient mainBuilder = WebClient.create();
         // Populate global roster string with JSON payload using WebClient builder
	        roster = mainBuilder
                        .get()
			.uri(url)
			.header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
			.header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
			.retrieve()
			.bodyToMono(String.class).block();

        return roster;
    }

    // method to process string JSON payload and parse down to name and id pairs that are stored in Player objects
    public List<BasePlayer> processRoster(String roster)
    {
        // ArrayLists to store substrings that contain name and id
        List<String> nameList = new ArrayList<String>();
        List<String> idList = new ArrayList<String>();
        List<String> posList = new ArrayList<String>();
        List<String> jerseyList = new ArrayList<String>();
        List<String> heightList = new ArrayList<String>();
        List<String> teamList = new ArrayList<String>();
         // String array to parse JSON payload into separate lines
         String substrings[] = roster.split(", ");
            // iterate through lines and check if contains wanted attribute, add to list
            for(int i = 0; i < substrings.length; i++)
            {
                // System.out.println(substrings[i].toString());
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
                if(substrings[i].contains("\"pos\""))
                {
                    String nameLine = "{" + substrings[i] + "}";
                    posList.add(nameLine);
                }
                if(substrings[i].contains("\"team\""))
                {
                    if(substrings[i].contains("\"body\""))
                    {
                        String substr = "{" + substrings[i].substring(9) + "}";
                        teamList.add(substr);
                    }
                     String nameLine = "{" + substrings[i] + "}";
                     teamList.add(nameLine);
                }
                if(substrings[i].contains("jerseyNum"))
                {
                    if(substrings[i].contains("roster"))
                    {
                        String substr = "{" + substrings[i].substring(12) + "}";
                        if(substr.equals("{\"jerseyNum\": \"\"}"))
                        {
                                String jerseyDefault = "{\"jerseyNum\": \"N/A\"}";
                                jerseyList.add(jerseyDefault);
                        }
                        else
                        {
                                jerseyList.add(substr);
                        }
                    }
                    if(substrings[i].equals("{\"jerseyNum\": \"\""))
                    {
                        String jerseyDefault = "{\"jerseyNum\": \"N/A\"}";
                        jerseyList.add(jerseyDefault);
                    }
                    else
                    {
                        String nameLine = substrings[i] + "}";
                        jerseyList.add(nameLine);
                    }
                }
                if(substrings[i].contains("height"))
                {
                    String nameLine = "{" + substrings[i] + "}";
                    heightList.add(nameLine);
                }
            }

            // remove error line due to format of JSON payload (DO NOT DELETE)
            jerseyList.remove(1);
            teamList.remove(1);

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
                // int counter = 0;
                // for(int i = 0; i < teamList.size(); i++)
                // {
                //     counter++;
                //      System.out.println(teamList.get(i).toString());
                // }
                // System.out.println(counter);
            //----------------------------------------------------

            // ArrayLists to store JsonNodes (actual values) for id and name
            List<JsonNode> nodeListOfNames = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfId = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfPos = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfJersey = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfHeight = new ArrayList<JsonNode>();
            List<JsonNode> nodeListOfTeam = new ArrayList<JsonNode>();

            // iterate through each line and parse out value and store in list
            for(int i = 0; i < nameList.size(); i++)
            {
                Mapper mapper = new Mapper();
                try
                {
                    JsonNode nameNode = mapper.parse(nameList.get(i));
                    nodeListOfNames.add(nameNode);

                    JsonNode idNode = mapper.parse(idList.get(i));
                    nodeListOfId.add(idNode);

                    JsonNode posNode = mapper.parse(posList.get(i));
                    nodeListOfPos.add(posNode);

                    JsonNode jerseyNode = mapper.parse(jerseyList.get(i));
                    nodeListOfJersey.add(jerseyNode);

                    JsonNode heightNode = mapper.parse(heightList.get(i));
                    nodeListOfHeight.add(heightNode);

                    JsonNode teamNode = mapper.parse(teamList.get(i));
                    nodeListOfTeam.add(teamNode);
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
            // int counter = 0;
            // for(int i = 0; i < nodeListOfJersey.size(); i++)
            // {
            //     System.out.println(nodeListOfJersey.get(i).get("jerseyNum").asText());
            //     counter++;
            // }
            // System.out.println(counter);
            //------------------------------------------------------------------------

            // iterate through node lists and create player objects mapping name and id together and store in FINAL list
            List<BasePlayer> players = new ArrayList<BasePlayer>();
            for(int i = 0; i < nodeListOfNames.size(); i++)
            {
                // create object and add live data fields
                BasePlayer player = new BasePlayer(nodeListOfNames.get(i).get("espnName").asText(),
                nodeListOfId.get(i).get("playerID").asText(), 
                nodeListOfPos.get(i).get("pos").asText(),
                nodeListOfJersey.get(i).get("jerseyNum").asText(), 
                nodeListOfHeight.get(i).get("height").asText(),
                nodeListOfTeam.get(i).get("team").asText());
                // add player object to list
                players.add(player);
            }
            // PRINT FINAL LIST OBJECTS (TEST)
            for(int i = 0; i < players.size(); i++)
            {
                System.out.println("---------------------------------------------------------------");
                System.out.println(players.get(i).getPlayerName() + ", ID: "
                 + players.get(i).getPlayerID()+ ", POS: " 
                 + players.get(i).getPos()+ ", NO: "
                 + players.get(i).getJerseyNum()+ ", HEIGHT: " 
                 + players.get(i).getHeight()+ ", TEAM: "
                 + players.get(i).getTeam());
            }
        //     System.out.println("-----------------------------------------------");
        //     System.out.println("ROSTER END");
        //     System.out.println("-----------------------------------------------");
        return players;
    }

    public List<BasePlayer> getChiefsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "KC";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getChargersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LAC";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getRaidersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LV";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBroncosRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DEN";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getPatriotsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NE";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getDolphinsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "MIA";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBillsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "BUF";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getJetsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NYJ";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getRavensRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "BAL";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBengalsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CIN";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBrownsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CLE";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getSteelersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "PIT";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getTexansRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "HOU";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getColtsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "IND";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getTitansRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "TEN";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getJaguarsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "JAX";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBearsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CHI";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getLionsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DET";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getPackersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "GB";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getVikingsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "MIN";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getCowboysRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "DAL";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getGiantsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NYG";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getEaglesRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "PHI";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getCommandersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "WSH";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getFalconsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "ATL";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getPanthersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "CAR";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getSaintsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "NO";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getBuccaneersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "TB";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getCardinalsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "ARI";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getRamsRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "LAR";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> get49ersRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "SF";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }

    public List<BasePlayer> getSeahawksRoster(List<BasePlayer> masterList)
    {
            // Params for abbreviation to be set in URL
            String teamAbv = "SEA";
            // Send get request with correct team abv
            String roster = sendRequest(teamAbv);
            // Method to sort JSON payload and store name/id pairs as player
            masterList = processRoster(roster);
            return masterList;
    }
}
