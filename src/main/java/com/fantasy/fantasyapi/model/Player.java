package com.fantasy.fantasyapi.model;

public class Player 
{
    public String playerName;
    public String playerID;
    public String pos;
    public String jerseyNum;
    public String height;
    
 
    public Player(String playerName, String playerID, String pos, String jerseyNum, String height) {
        this.playerName = playerName;
        this.playerID = playerID;
        this.pos = pos;
        this.jerseyNum = jerseyNum;
        this.height = height;
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
    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }
    public String getJerseyNum() {
        return jerseyNum;
    }
    public void setJerseyNum(String jerseyNum) {
        this.jerseyNum = jerseyNum;
    }
    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }
    
}


