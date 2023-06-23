package com.fantasy.fantasyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamSchedule 
{
    private String gameID;
    private String seasonType;
    private String away;
    private String teamIDHome;
    private String gameDate;
    private String gameStatus;
    private String gameWeek;
    private String teamIDAway;
    private String home;
    private String awayResult;
    private String homePts;
    private String gameTime;
    private String homeResult;
    private String awayPts;
    private String team;

    public TeamSchedule() {
    }
    public TeamSchedule(String gameID, String seasonType, String away, String teamIDHome, String gameDate,
            String gameStatus, String gameWeek, String teamIDAway, String home, String awayResult, String homePts,
            String gameTime, String homeResult, String awayPts, String team) {
        this.gameID = gameID;
        this.seasonType = seasonType;
        this.away = away;
        this.teamIDHome = teamIDHome;
        this.gameDate = gameDate;
        this.gameStatus = gameStatus;
        this.gameWeek = gameWeek;
        this.teamIDAway = teamIDAway;
        this.home = home;
        this.awayResult = awayResult;
        this.homePts = homePts;
        this.gameTime = gameTime;
        this.homeResult = homeResult;
        this.awayPts = awayPts;
        this.team = team;
    }
    
    public String getGameID() {
        return gameID;
    }
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
    public String getSeasonType() {
        return seasonType;
    }
    public void setSeasonType(String seasonType) {
        this.seasonType = seasonType;
    }
    public String getAway() {
        return away;
    }
    public void setAway(String away) {
        this.away = away;
    }
    public String getTeamIDHome() {
        return teamIDHome;
    }
    public void setTeamIDHome(String teamIDHome) {
        this.teamIDHome = teamIDHome;
    }
    public String getGameDate() {
        return gameDate;
    }
    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
    public String getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
    public String getGameWeek() {
        return gameWeek;
    }
    public void setGameWeek(String gameWeek) {
        this.gameWeek = gameWeek;
    }
    public String getTeamIDAway() {
        return teamIDAway;
    }
    public void setTeamIDAway(String teamIDAway) {
        this.teamIDAway = teamIDAway;
    }
    public String getHome() {
        return home;
    }
    public void setHome(String home) {
        this.home = home;
    }
    public String getAwayResult() {
        return awayResult;
    }
    public void setAwayResult(String awayResult) {
        this.awayResult = awayResult;
    }
    public String getHomePts() {
        return homePts;
    }
    public void setHomePts(String homePts) {
        this.homePts = homePts;
    }
    public String getGameTime() {
        return gameTime;
    }
    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }
    public String getHomeResult() {
        return homeResult;
    }
    public void setHomeResult(String homeResult) {
        this.homeResult = homeResult;
    }
    public String getAwayPts() {
        return awayPts;
    }
    public void setAwayPts(String awayPts) {
        this.awayPts = awayPts;
    }
    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

    
}
