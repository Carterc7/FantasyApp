package com.fantasy.fantasyapi;

public class Player 
{
    public String playerName;
    public String playerID;
    public String pos;
    public String jerseyNum;
    public String height;
    public String team;

    public Player() {
    }
    public Player(String playerName, String playerID, String pos, String jerseyNum, String height, String team) {
        this.playerName = playerName;
        this.playerID = playerID;
        this.pos = pos;
        this.jerseyNum = jerseyNum;
        this.height = height;
        this.team = team;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerID() {
        return playerID;
    }
    public void setPlayerID(String playerID) {
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
    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

    
}
