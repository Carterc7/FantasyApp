package com.fantasy.fantasyapi.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.model.AdpPlayer;
import com.fantasy.fantasyapi.model.EspnPlayer;
import com.fantasy.fantasyapi.model.TeamSchedule;
import com.fantasy.fantasyapi.utility.MongoDbHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoOperations 
{   
    /**
     * Executes an update on the entire fantasyPlayersEspn collection
     * "update" is an up-to-date list pulled directly from API
     */
    public void performFantasyEspnUpdate()
    {
        MongoOperations mongo = new MongoOperations();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        List<EspnPlayer> update = new ArrayList<EspnPlayer>();
        // get up-to-date list
        update = getAllPlayers.getFilteredPlayerList(300);
        // for(EspnPlayer player : update)
        // {
        //     player.setEspnName("test");
        // }
        // send list to update method and correct collection
        mongo.updateEspnPlayers(update, "fantasyPlayersEspn");
    }

    /**
     * Executes an update on the entire players collection
     * "update" is an up-to-date list pulled directly from API
     */
    public void performFantasyEspnUpdateAllPlayers()
    {
        MongoOperations mongo = new MongoOperations();
        GetAllPlayers getAllPlayers = new GetAllPlayers();
        List<EspnPlayer> update = new ArrayList<EspnPlayer>();
        String jsonString = getAllPlayers.sendRequestGetAllPlayers();
        try 
        {
            // get up-to-date-list
            update = getAllPlayers.mapJsonToPlayerObject(jsonString);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to generate allPlayers");
        }
        // for(EspnPlayer player : update)
        // {
        //     player.setEspnName("test");
        // }
        // send list to update method and correct collection
        mongo.updateEspnPlayers(update, "players");
    }

    /**
     * Executes an update on the entire fantasyPlayersAdp collection
     * "update" is an up-to-date list pulled directly from API
     */
    public void performFantasyAdpUpdate()
    {
        MongoOperations mongo = new MongoOperations();
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        List<AdpPlayer> update = new ArrayList<AdpPlayer>();
        // get up-to-date list
        update = getDraftAdp.getFilteredAdpList(300);
        // for(AdpPlayer player : update)
        // {
        //     player.setName("test");
        // }
        // send list to update method and correct collection
        mongo.updateAdpPlayers(update, "fantasyPlayersAdp");
    }

    /** 
     * Method to UPDATE collection by creating new documents and replacing by filtering playerID
     * @param updateList
     * @param collectionName
     */
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
            // iterate through newly created documents
            for(Document updateDoc : updateDocuments)
            {
                // create a filter to match old documents with new ones
                Document filter = new Document("espnID", updateDoc.get("espnID"));
                // replace each document by ID filter and new document
                players.replaceOne(filter, updateDoc);
                // print status to console
                System.out.println(counter + "): " + updateDoc.get("espnName") + " was just updated!");
                counter++;
            }
            client.close();
        }
    }

    /**
     * Method to CREATE and INSERT team schedules to collection 
     * @param listToInsert
     * @param collectionName
     */
    public void addTeamSchedule(List<TeamSchedule> listToInsert, String collectionName)
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
            MongoCollection<Document> schedules = database.getCollection(dbHelperMain.getCollectionName());
            // print success message
            System.out.println("Connection to " + collectionName + ": SUCCESS");
            List<Document> insertDocuments = new ArrayList<Document>();
             // iterate through updateList and append each attribute to the document
            for (TeamSchedule schedule : listToInsert) 
            {
                // create a new document and append attributes for each schedule
                Document document = new Document("gameID", schedule.getGameID())
                        .append("seasonType", schedule.getSeasonType())
                        .append("away", schedule.getAway())
                        .append("teamIDHome", schedule.getTeamIDHome())
                        .append("gameDate", schedule.getGameDate())
                        .append("gameStatus", schedule.getGameStatus())
                        .append("gameWeek", schedule.getGameWeek())
                        .append("teamIDAway", schedule.getTeamIDAway())
                        .append("home", schedule.getHome())
                        .append("awayResult", schedule.getAwayResult())
                        .append("homePts", schedule.getHomePts())
                        .append("gameTime", schedule.getGameTime())
                        .append("homeResult", schedule.getHomeResult())
                        .append("awayPts", schedule.getAwayPts())
                        .append("team", schedule.getTeam());
                // add the schedule to the list
                insertDocuments.add(document);
            }
            int counter = 1;
            // iterate through newly formed documents
            for(Document insertDoc : insertDocuments)
            {
                // insert each document into the collection
                schedules.insertOne(insertDoc);
                // print status to console
                System.out.println(counter + "): " + insertDoc.get("gameWeek") + " " + insertDoc.get("gameID") + " was just added!");
                counter++;
            }
            client.close();
        }
    }
    
    /**
     * Method to UPDATE collection by creating new documents and replacing by filtering playerID
     * @param updateList
     * @param collectionName
     */
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
                // update the player document document list
                updateDocuments.add(document);
            }
            int counter = 1;
            // iterate through newly created documents
            for(Document updateDoc : updateDocuments)
            {
                    // define filter to replace documents with correct playerID
                    Document filter = new Document("playerID", updateDoc.get("playerID").toString());
                    // replace document using filter and newly created document
                    players.replaceOne(filter, updateDoc);
                    // print status to console
                    System.out.println(counter + "): " + updateDoc.get("name") + " was just updated!");
                    counter++;
                    // players.insertOne(updateDoc);
                    // System.out.println(counter + "): " + updateDoc.get("name") + " was just inserted!");
                    // counter++;
            }
            System.out.println(collectionName + " updated successfully!");
            client.close();
        }
    }
    
}
