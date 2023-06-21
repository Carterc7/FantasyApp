package com.fantasy.fantasyapi.mongo;

import java.util.List;
import org.bson.Document;
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
    // Method to initialize database connection at a specific endpoint
    public MongoCollection<Document> dbConnect(String collectionName)
    {
        // load dotenv attributes (hidden)
        Dotenv dotenv = Dotenv.load();

        // use helper class to initialize an object with necessary connection attributes
        MongoDbHelper dbHelperMain = new MongoDbHelper(dotenv.get("CONNECTION_STRING"), dotenv.get("DATABASE_NAME"), collectionName);
        // initialize mongo client using connection string
        try (MongoClient client = new MongoClient(new MongoClientURI(dbHelperMain.getConnectionString()))) {
            // initialize database using db name
            MongoDatabase database = client.getDatabase(dbHelperMain.getDatabaseName());
            // initialize collection using collection name
            MongoCollection<Document> players = database.getCollection(dbHelperMain.getCollectionName());
            // return document collection to allow CRUD operations
            return players;
        }
    }

    // Method to INSERT espnPlayer objects into database
    public void insertObjectsEspn(List<EspnPlayer> listToInsert) 
    {
            MongoOperations mongo = new MongoOperations();
            // initialize database connection and document collection "fantasyPlayersEspn" 
            MongoCollection<Document> players = mongo.dbConnect("fantasyPlayersEspn");

            // iterate through listToInsert and append each attribute to the document
            for (EspnPlayer player : listToInsert) {
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
                // insert the player document into the db collection
                players.insertOne(document);
                System.out.println(player.getEspnName() + " added to database!");
            }
    }
    
    // method to INSERT adpPlayer objects into database
    public void insertObjectsAdp(List<AdpPlayer> listToInsert) 
    {
            MongoOperations mongo = new MongoOperations();
            // initialize database connection and document collection "fantasyPlayersAdp"
            MongoCollection<Document> players = mongo.dbConnect("fantasyPlayersAdp");

            // iterate through listToInsert and append each attribute to the document
            for (AdpPlayer player : listToInsert) {
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
                // insert the player document into the db collection
                players.insertOne(document);
                System.out.println(player.getName() + " added to database!");
            }
    }
    
}
