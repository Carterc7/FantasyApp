package com.fantasy.fantasyapi.leagueModels;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import com.fantasy.fantasyapi.objectModels.EspnPlayer;

public class FantasyTeam 
{
    @Id
    private String teamID;
    private String teamName;
    private List<AdpPlayer> roster;

    
    public FantasyTeam() {
    }
    public FantasyTeam(String teamID, String teamName, List<AdpPlayer> roster) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.roster = roster;
    }
    
    public String getTeamID() {
        return teamID;
    }
    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public List<AdpPlayer> getRoster() {
        return roster;
    }
    public void setTeamOwner(List<AdpPlayer> roster) {
        this.roster = roster;
    }  
}
