package com.fantasy.fantasyapi.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.model.AdpPlayer;
import com.fantasy.fantasyapi.model.EspnPlayer;
import com.fantasy.fantasyapi.utility.MongoDbHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoOperations 
{
    // Method to search for a player by any STRING {key, value} pair
    // returns searched players document
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
    
    // Executes an update on the entire fantasyPlayersEspn collection
    // "insert" is an up-to-date list pulled directly from API
    public void performFantasyEspnUpdate()
    {
        MongoOperations mongo = new MongoOperations();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        List<EspnPlayer> insert = new ArrayList<EspnPlayer>();
        insert = getAllPlayers.getFilteredPlayerList(100);
        // for(EspnPlayer player : insert)
        // {
        //     player.setEspnName("test");
        // }
        mongo.updateEspnPlayers(insert, "fantasyPlayersEspn");
    }

    // Executes an update on the entire players collection
    // "insert" is an up-to-date list pulled directly from API
    public void performFantasyEspnUpdateAllPlayers()
    {
        MongoOperations mongo = new MongoOperations();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        List<EspnPlayer> insert = new ArrayList<EspnPlayer>();
        String jsonString = getAllPlayers.sendRequestGetAllPlayers();
        try 
        {
            insert = getAllPlayers.mapJsonToPlayerObject(jsonString);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to generate allPlayers");
        }
        // for(EspnPlayer player : insert)
        // {
        //     player.setEspnName("test");
        // }
        mongo.updateEspnPlayers(insert, "players");
    }

    // Executes an update on the entire fantasyPlayersAdp collection
    // "insert" is an up-to-date list pulled directly from API
    public void performFantasyAdpUpdate()
    {
        MongoOperations mongo = new MongoOperations();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        List<AdpPlayer> insert = new ArrayList<AdpPlayer>();
        insert = getDraftAdp.getFilteredAdpList(100);
        // for(AdpPlayer player : insert)
        // {
        //     player.setName("test");
        // }
        mongo.updateAdpPlayers(insert, "fantasyPlayersAdp");
    }

    // Method to UPDATE espnPlayer objects in database
    public void updateEspnPlayers(List<EspnPlayer> updateList, String collectionName) 
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
            MongoCollection<Document> players = database.getCollection(dbHelperMain.getCollectionName());
            // print success message
            System.out.println("Connection to " + collectionName + ": SUCCESS");
            List<Document> updateDocuments = new ArrayList<Document>();
            // iterate through updateList and append each attribute to the document
            for (EspnPlayer player : updateList) {
                Document document = new Document("espnID", player.getEspnID())
                        .append("espnName", player.getEspnName())
                        .append("espnIDFull", player.getEspnIDFull())
                        .append("weight", player.getWeight())
                        .append("jerseyNum", player.getJerseyNum())
                        .append("cbsShortName", player.getCbsShortName())
                        .append("team", player.getTeam())
                        .append("yahooPlayerID", player.getYahooPlayerID())
                        .append("age", player.getAge())
                        .append("espnLink", player.getEspnLink())
                        .append("yahooLink", player.getYahooLink())
                        .append("bDay", player.getBDay())
                        .append("espnHeadshot", player.getEspnHeadshot())
                        .append("cbsLongName", player.getCbsLongName())
                        .append("teamID", player.getTeamID())
                        .append("pos", player.getPos())
                        .append("school", player.getSchool())
                        .append("cbsPlayerID", player.getCbsPlayerID())
                        .append("longName", player.getLongName())
                        .append("height", player.getHeight())
                        .append("cbsPlayerIDFull", player.getCbsPlayerIDFull())
                        .append("lastGamePlayed", player.getLastGamePlayed())
                        .append("playerID", player.getPlayerID())
                        .append("exp", player.getExp());
                    updateDocuments.add(document);
            }
            int counter = 1;
            for(Document updateDoc : updateDocuments)
            {
                Document filter = new Document("espnID", updateDoc.get("espnID"));
                players.replaceOne(filter, updateDoc);
                System.out.println(counter + "): " + updateDoc.get("espnName") + " was just updated!");
                counter++;
            }
            System.out.println(collectionName + " updated successfully!");
            client.close();
        }
    }
    
    // method to UPDATE adpPlayer objects in database
    public void updateAdpPlayers(List<AdpPlayer> updateList, String collectionName) 
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
            MongoCollection<Document> players = database.getCollection(dbHelperMain.getCollectionName());
            // print success message
            System.out.println("Connection to " + collectionName + ": SUCCESS");
            List<Document> updateDocuments = new ArrayList<Document>();
            // iterate through updateList and append each attribute to the document
            for (AdpPlayer player : updateList) 
            {
                Document document = new Document("playerID", player.getPlayerID())
                        .append("name", player.getName())
                        .append("team", player.getTeam())
                        .append("position", player.getPosition())
                        .append("averageDraftPosition", player.getAverageDraftPosition())
                        .append("averageDraftPositionPPR", player.getAverageDraftPositionPPR())
                        .append("byeWeek", player.getByeWeek())
                        .append("auctionValue", player.getAuctionValue())
                        .append("auctionValuePPR", player.getAuctionValuePPR())
                        .append("averageDraftPositionIDP", player.getAverageDraftPositionIDP())
                        .append("averageDraftPositionRookie", player.getAverageDraftPositionRookie())
                        .append("averageDraftPositionDynasty", player.getAverageDraftPositionDynasty())
                        .append("averageDraftPosition2QB", player.getAverageDraftPosition2QB());
                // insert the player document document list
                updateDocuments.add(document);
            }
            int counter = 1;
            for(Document updateDoc : updateDocuments)
            {
                Document filter = new Document("playerID", updateDoc.get("playerID"));
                players.replaceOne(filter, updateDoc);
                System.out.println(counter + "): " + updateDoc.get("name") + " was just updated!");
            }
            System.out.println(collectionName + " updated successfully!");
            client.close();
        }
    }
    
}
