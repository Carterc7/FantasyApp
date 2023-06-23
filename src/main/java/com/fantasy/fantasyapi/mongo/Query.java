package com.fantasy.fantasyapi.mongo;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.fantasy.fantasyapi.utility.MongoDbHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class Query 
{
    /** 
     * Method to search for a specific player in specified collection by any "STRING" {key, value} pair
     * Returns mongo Document for FIRST query result
     * @param collectionName
     * @param key
     * @param value
     * @return Document
     */
    public Document searchPlayer(String collectionName, String key, String value)
    {
        // load dotenv attributes (hidden)
        Dotenv dotenv = Dotenv.load();
        // use helper class to initialize an object with necessary connection attributes
        MongoDbHelper dbHelperMain = new MongoDbHelper(dotenv.get("CONNECTION_STRING"), dotenv.get("DATABASE_NAME_API"), collectionName);
        // initialize mongo client using connection string
        try (MongoClient client = new MongoClient(new MongoClientURI(dbHelperMain.getConnectionString()))) {
            // initialize database using db name
            MongoDatabase database = client.getDatabase(dbHelperMain.getDatabaseName());
            // initialize collection using collection name
            MongoCollection<Document> collection = database.getCollection(dbHelperMain.getCollectionName());
            // print success message
            System.out.println("Connection to " + collectionName + ": SUCCESS");
            // create document key value pair to search for
            Document query = new Document(key, value);
            // search for first player that matches query
            Document player = collection.find(query).first();
            client.close();
            return player;
        }
    }

    /** 
     * Method to get a gameID by the season (year), team, and gameWeek
     * Returns String gameID value
     * @param seasonYear
     * @param teamAbv
     * @param gameWeek
     * @return String
     */
    public String getGameID(String seasonYear, String teamAbv, String gameWeek)
    {
        // load dotenv attributes (hidden)
        Dotenv dotenv = Dotenv.load();
        // use helper class to initialize an object with necessary connection attributes
        MongoDbHelper dbHelperMain = new MongoDbHelper(dotenv.get("CONNECTION_STRING"), dotenv.get("DATABASE_NAME_API"), "teamSchedules");
        // initialize mongo client using connection string
        try (MongoClient client = new MongoClient(new MongoClientURI(dbHelperMain.getConnectionString())))
        {
            String gameID = "";
            // initialize database using db name
            MongoDatabase database = client.getDatabase(dbHelperMain.getDatabaseName());
            // initialize collection using collection name
            MongoCollection<Document> collection = database.getCollection(dbHelperMain.getCollectionName());
            // Map team and gameWeek values to search with
            Map<String, String> searchCriteria = new HashMap<>();
                searchCriteria.put("team", teamAbv);
                searchCriteria.put("gameWeek", gameWeek);
            Document query = new Document();
            // append each key value pair to the query to search by
            for(Map.Entry<String, String> entry : searchCriteria.entrySet())
            {
                query.append(entry.getKey(), entry.getValue());
            }
            // retrieve a list of results based of query
            FindIterable<Document> results = collection.find(query);
            // iterate through the list
            for(Document document : results)
            {
                if(document.get("gameWeek").toString().contains("17") || document.get("gameWeek").toString().contains("18") || document.get("seasonType").toString().toLowerCase().contains("postseason"))
                {
                    int season = Integer.parseInt(seasonYear);
                    season++;
                    String value = String.valueOf(season);
                    seasonYear = value;
                }
                // check if the year matches in the gameID field and check that the game was completed
                if(document.get("gameID").toString().contains(seasonYear) && document.get("gameStatus").toString().contains("Completed"))
                {
                    // store the gameID in a string and return
                    gameID = document.get("gameID").toString();
                }
            }
            client.close();
            return gameID;
        }
    }
}
