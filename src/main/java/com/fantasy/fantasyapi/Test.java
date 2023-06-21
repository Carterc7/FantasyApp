package com.fantasy.fantasyapi;
import org.bson.Document;

import com.fantasy.fantasyapi.mongo.MongoOperations;


public class Test 
{
    public static void main(String[] args)
    {
        MongoOperations mongo = new MongoOperations();
        // mongo.performFantasyEspnUpdate();
        // Document player = mongo.searchPlayer("fantasyPlayersEspn", "exp", "R");
        // System.out.println(player.get("espnName") + " " + player.get("exp"));
    }
}
