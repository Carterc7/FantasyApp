package com.fantasy.fantasyapi;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;

import java.io.IOException;
import java.util.List;

class ApiTest {

    private GetAllPlayers getAllPlayers = new GetAllPlayers();

    @Test
    void testMapJsonToPlayerObject() throws IOException 
    {
        // Sample JSON string representing player data
        String jsonString = "{\"body\": [{\"espnName\": \"Player1\", \"pos\": \"QB\"}, {\"espnName\": \"Player2\", \"pos\": \"WR\"}]}";

        // Testing mapJsonToPlayerObject method
        List<EspnPlayer> players = getAllPlayers.mapJsonToPlayerObject(jsonString);

        // Validating the result
        Assert.notNull(players, "Players list should not be null");
        Assert.notEmpty(players, "Players list should not be empty");
        Assert.isTrue(players.size() == 2, "Expected 2 players in the list");
        Assert.isTrue(players.get(0).getEspnName().equals("Player1"), "Player1 should be present in the list");
        Assert.isTrue(players.get(1).getPos().equals("WR"), "Player2's position should be WR");
    }

    @Test
    void testSendRequestGetAllPlayers() 
    {
        GetAllPlayers getAllPlayers = new GetAllPlayers();

        // Testing sendRequestGetAllPlayers method
        String response = getAllPlayers.sendRequestGetAllPlayers();

        // Validating the result
        Assert.notNull(response, "API response should not be null");
        Assert.isTrue(!response.isEmpty(), "API response should not be empty");
    }
}

