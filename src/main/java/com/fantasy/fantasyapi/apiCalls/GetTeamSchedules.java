package com.fantasy.fantasyapi.apiCalls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fantasy.fantasyapi.objectModels.TeamSchedule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import reactor.netty.http.client.HttpClient;

public class GetTeamSchedules 
{
    /** 
     * Method to get a team schedule for a specific team and season
     * returns list of schedules for specified  team
     * @param teamAbv
     * @param season
     * @return List<TeamSchedule>
     */
    public List<TeamSchedule> getTeamSchedule(String teamAbv, String season)
    {
        GetTeamSchedules getTeamSchedules = new GetTeamSchedules();
        List<TeamSchedule> scheduleForTeam = new ArrayList<TeamSchedule>();
        // create jsonString off specified parameters
        String jsonString = getTeamSchedules.sendRequestGetTeamSchedule(teamAbv, season);
        // create schedule list using jsonString and mapper method
        scheduleForTeam = getTeamSchedules.mapJsonToTeamSchedule(jsonString);
        return scheduleForTeam;
    }

    /**
     * Method sends HTTP request and returns "String" payload from API
     * payload is unprocessed, raw JSON 
     * @param teamAbv
     * @param season
     * @return String
     */
    public String sendRequestGetTeamSchedule(String teamAbv, String season)
    {
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("API_KEY");
        String baseUrl = dotenv.get("API_URL");
        String jsonString = "";
        String url = baseUrl + "/getNFLTeamSchedule?teamAbv=" + teamAbv + "&season=" + season;
        
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
     * Method to parse JSON from sendRequestGetTeamSchedule() and form "TeamSchedule" objects
     * objects are stored in a list and returned
     * @param jsonString
     * @return List<TeamSchedule>
     */
    public List<TeamSchedule> mapJsonToTeamSchedule(String jsonString)
    {
        // initializing list to be returned
        List<TeamSchedule> scheduleList = new ArrayList<TeamSchedule>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // read and store jsonString into a jsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // parse json node to the "body" field to get the "team" attribute
            JsonNode bodyNode = jsonNode.get("body");
            String team = bodyNode.get("team").asText();

            // parse body to schedule field
            JsonNode scheduleNode = bodyNode.get("schedule");
            // quick success check
            if (scheduleNode.isArray()) 
            {
                    String gameID = "";
                    String seasonType = "";
                    String away = "";
                    String teamIDHome = "";
                    String gameDate = "";
                    String gameStatus = "";
                    String gameWeek = "";
                    String teamIDAway = "";
                    String home = "";
                    String awayResult = "";
                    String homePts = "";
                    String gameTime = "";
                    String homeResult = "";
                    String awayPts = "";
                // iterate through each game in the schedule and store the attributes as Strings
                for (JsonNode gameNode : scheduleNode) {
                    if(gameNode.has("gameID"))
                    {
                        gameID = gameNode.get("gameID").asText();
                    }
                    if(gameNode.has("seasonType"))
                    {
                        seasonType = gameNode.get("seasonType").asText();
                    }
                    if(gameNode.has("away"))
                    {
                        away = gameNode.get("away").asText();
                    }
                    if(gameNode.has("teamIDHome"))
                    {
                        teamIDHome = gameNode.get("teamIDHome").asText();
                    }
                    if(gameNode.has("gameDate"))
                    {
                        gameDate = gameNode.get("gameDate").asText();
                    }
                    if(gameNode.has("gameStatus"))
                    {
                        gameStatus = gameNode.get("gameStatus").asText();
                    }
                    if(gameNode.has("gameWeek"))
                    {
                        gameWeek = gameNode.get("gameWeek").asText();
                    }
                    if(gameNode.has("teamIDAway"))
                    {
                        teamIDAway = gameNode.get("teamIDAway").asText();
                    }
                    if(gameNode.has("home"))
                    {
                        home = gameNode.get("home").asText();
                    }
                    if(gameNode.has("awayResult"))
                    {
                        awayResult = gameNode.get("awayResult").asText();
                    }
                    if(gameNode.has("homePts"))
                    {
                        homePts = gameNode.get("homePts").asText();
                    }
                    if(gameNode.has("gameTime"))
                    {
                        gameTime = gameNode.get("gameTime").asText();
                    }
                    if(gameNode.has("homeResult"))
                    {
                        homeResult = gameNode.get("homeResult").asText();
                    }
                    if(gameNode.has("awayPts"))
                    {
                        awayPts = gameNode.get("awayPts").asText();
                    }

                    // Create a TeamSchedule object using stored Strings
                    TeamSchedule schedule = new TeamSchedule(gameID, seasonType, away, teamIDHome, gameDate, gameStatus,
                            gameWeek, teamIDAway, home, awayResult, homePts, gameTime, homeResult, awayPts, team);

                    // Add the TeamSchedule object to the list to be returned
                    scheduleList.add(schedule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleList;
    }
}
