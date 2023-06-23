package com.fantasy.fantasyapi.model;

import java.util.Date;
import java.util.List;

public class FantasyLeague 
{
    private String leagueID;
    private String leagueName;
    private User leagueCommish;
    private List<FantasyTeam> teams;
    private Date startDate;
    private Date endDate;
    private Date draftDay;
    private int maxTeams;
    private int maxRosterSize;

    
    public FantasyLeague() {
    }
    public FantasyLeague(String leagueID, String leagueName, User leagueCommish, List<FantasyTeam> teams,
            Date startDate, Date endDate, Date draftDay, int maxTeams, int maxRosterSize) {
        this.leagueID = leagueID;
        this.leagueName = leagueName;
        this.leagueCommish = leagueCommish;
        this.teams = teams;
        this.startDate = startDate;
        this.endDate = endDate;
        this.draftDay = draftDay;
        this.maxTeams = maxTeams;
        this.maxRosterSize = maxRosterSize;
    }
    
    public String getLeagueID() {
        return leagueID;
    }
    public void setLeagueID(String leagueID) {
        this.leagueID = leagueID;
    }
    public String getLeagueName() {
        return leagueName;
    }
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
    public User getLeagueCommish() {
        return leagueCommish;
    }
    public void setLeagueCommish(User leagueCommish) {
        this.leagueCommish = leagueCommish;
    }
    public List<FantasyTeam> getTeams() {
        return teams;
    }
    public void setTeams(List<FantasyTeam> teams) {
        this.teams = teams;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getDraftDay() {
        return draftDay;
    }
    public void setDraftDay(Date draftDay) {
        this.draftDay = draftDay;
    }
    public int getMaxTeams() {
        return maxTeams;
    }
    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }
    public int getMaxRosterSize() {
        return maxRosterSize;
    }
    public void setMaxRosterSize(int maxRosterSize) {
        this.maxRosterSize = maxRosterSize;
    }

    
}
