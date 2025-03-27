package com.fantasy.fantasyapi.leagueModels;

import java.util.List;

import org.springframework.data.annotation.Id;
import com.fantasy.fantasyapi.objectModels.AdpPlayerCSV;

// Model for a fantasy team, each having a name, id, and roster of players
public class FantasyTeam 
{
    @Id
    private String teamID;
    private String teamName;
    private List<AdpPlayerCSV> roster;

    
    public FantasyTeam() {
    }

    public FantasyTeam(String teamID, String teamName, List<AdpPlayerCSV> roster) {
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
    public List<AdpPlayerCSV> getRoster() {
        return roster;
    }
    public void setTeamOwner(List<AdpPlayerCSV> roster) {
        this.roster = roster;
    }  
}
