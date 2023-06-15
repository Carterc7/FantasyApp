package com.fantasy.fantasyapi.model;

public class NFLTeam 
{
    public String espnName;
    public String espnID;

    
    public NFLTeam(String espnName, String espnID) {
        this.espnName = espnName;
        this.espnID = espnID;
    }
    public NFLTeam() {
    }
    public String getEspnName() {
        return espnName;
    }
    public void setEspnName(String espnName) {
        this.espnName = espnName;
    }
    public String getEspnID() {
        return espnID;
    }
    public void setEspnID(String espnID) {
        this.espnID = espnID;
    }
}


