package com.fantasy.fantasyapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public class GetAllPlayers {
    @Autowired
    WebClient.Builder builder;

    String url = "https://tank01-nfl-live-in-game-real-time-statistics-nfl.p.rapidapi.com/getNFLTeamRoster?teamID=16&teamAbv=KC";

    String players[] = null;
}
