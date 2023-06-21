package com.fantasy.fantasyapi.model;

import java.util.Date;
import java.util.List;

public class FantasyTeam 
{
    private String teamID;
    private String teamName;
    private User teamOwner;
    private FantasyLeague league;
    private List<EspnPlayer> roster;
    private int points;
    private int rank;
    private int draftOrder;
    private Date createdDate;

    
    public FantasyTeam() {
    }
    public FantasyTeam(String teamID, String teamName, User teamOwner, FantasyLeague league, List<EspnPlayer> roster,
            int points, int rank, int draftOrder, Date createdDate) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.teamOwner = teamOwner;
        this.league = league;
        this.roster = roster;
        this.points = points;
        this.rank = rank;
        this.draftOrder = draftOrder;
        this.createdDate = createdDate;
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
    public User getTeamOwner() {
        return teamOwner;
    }
    public void setTeamOwner(User teamOwner) {
        this.teamOwner = teamOwner;
    }
    public FantasyLeague getLeague() {
        return league;
    }
    public void setLeague(FantasyLeague league) {
        this.league = league;
    }
    public List<EspnPlayer> getRoster() {
        return roster;
    }
    public void setRoster(List<EspnPlayer> roster) {
        this.roster = roster;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public int getDraftOrder() {
        return draftOrder;
    }
    public void setDraftOrder(int draftOrder) {
        this.draftOrder = draftOrder;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }   
    
    
}
