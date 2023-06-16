package com.fantasy.fantasyapi.model;

public class Player 
{
    public String playerName;
    public String playerID;

    
    public Player(String playerName, String playerID) {
        this.playerName = playerName;
        this.playerID = playerID;
    }
    public Player() {
    }
    public String getplayerName() {
        return playerName;
    }
    public void setplayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getplayerID() {
        return playerID;
    }
    public void setplayerID(String playerID) {
        this.playerID = playerID;
    }
}


