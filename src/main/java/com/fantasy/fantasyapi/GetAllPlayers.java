package com.fantasy.fantasyapi;

import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonFilter;

@RestController
@RequestMapping("/")
public class GetAllPlayers 
{
    @Autowired
    WebClient.Builder builder;

    @RequestMapping("Chiefs")
    public void getChiefsRoster()
    {
        String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLTeamRoster?teamID=16&teamAbv=KC";

		String players = builder.build()
			.get()
			.uri(url)
			.header("X-RapidAPI-Key", "e65f398570mshf333bbc306e2bd0p160558jsn1dfe348a5886")
			.header("X-RapidAPI-Host", "tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com")
			.retrieve()
			.bodyToMono(String.class)
			.block();
	
    }
    
}
