package com.fantasy.fantasyapi.objectModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Model class for data received by stats API
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsPlayer 
{
    public String playerID;
    public String gameID;
    public String team;
    public String longName;
    public String receptions;
    public String recTD;
    public String recYds;
    public String targets;
    public String rushYds;
    public String carries;
    public String rushTd;
    public String passAttempts;
    public String passCompletions;
    public String passYds;
    public String passTD;
    public String totalTackles;
    public String defTD;
    public String soloTackles;
    public String tfls;
    public String qbHits;
    public String defInterceptions;
    public String sacks;
    public String passDeflections;
    @JsonProperty("int")
    public String interceptions;
    public double fantasyPoints;

    public StatsPlayer() {
    }
    public StatsPlayer(String playerID, String gameID, String team, String longName, String receptions, String recTD,
            String recYds, String targets, String rushYds, String carries, String rushTd, String passAttempts,
            String passCompletions, String passYds, String passTD, String totalTackles, String defTD,
            String soloTackles, String tfls, String qbHits, String defInterceptions, String sacks,
            String passDeflections, String interceptions, double fantasyPoints) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.team = team;
        this.longName = longName;
        this.receptions = receptions;
        this.recTD = recTD;
        this.recYds = recYds;
        this.targets = targets;
        this.rushYds = rushYds;
        this.carries = carries;
        this.rushTd = rushTd;
        this.passAttempts = passAttempts;
        this.passCompletions = passCompletions;
        this.passYds = passYds;
        this.passTD = passTD;
        this.totalTackles = totalTackles;
        this.defTD = defTD;
        this.soloTackles = soloTackles;
        this.tfls = tfls;
        this.qbHits = qbHits;
        this.defInterceptions = defInterceptions;
        this.sacks = sacks;
        this.passDeflections = passDeflections;
        this.interceptions = interceptions;
        this.fantasyPoints = fantasyPoints;
    }
    public String getPlayerID() {
        return playerID;
    }


    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }


    public String getGameID() {
        return gameID;
    }


    public void setGameID(String gameID) {
        this.gameID = gameID;
    }


    public String getTeam() {
        return team;
    }


    public void setTeam(String team) {
        this.team = team;
    }


    public String getLongName() {
        return longName;
    }


    public void setLongName(String longName) {
        this.longName = longName;
    }


    public String getReceptions() {
        return receptions;
    }


    public void setReceptions(String receptions) {
        this.receptions = receptions;
    }


    public String getRecTD() {
        return recTD;
    }


    public void setRecTD(String recTD) {
        this.recTD = recTD;
    }


    public String getRecYds() {
        return recYds;
    }


    public void setRecYds(String recYds) {
        this.recYds = recYds;
    }


    public String getTargets() {
        return targets;
    }


    public void setTargets(String targets) {
        this.targets = targets;
    }


    public String getRushYds() {
        return rushYds;
    }


    public void setRushYds(String rushYds) {
        this.rushYds = rushYds;
    }


    public String getCarries() {
        return carries;
    }


    public void setCarries(String carries) {
        this.carries = carries;
    }


    public String getRushTd() {
        return rushTd;
    }


    public void setRushTd(String rushTd) {
        this.rushTd = rushTd;
    }


    public String getPassAttempts() {
        return passAttempts;
    }


    public void setPassAttempts(String passAttempts) {
        this.passAttempts = passAttempts;
    }


    public String getPassCompletions() {
        return passCompletions;
    }


    public void setPassCompletions(String passCompletions) {
        this.passCompletions = passCompletions;
    }


    public String getPassYds() {
        return passYds;
    }


    public void setPassYds(String passYds) {
        this.passYds = passYds;
    }


    public String getPassTD() {
        return passTD;
    }


    public void setPassTD(String passTD) {
        this.passTD = passTD;
    }


    public String getTotalTackles() {
        return totalTackles;
    }


    public void setTotalTackles(String totalTackles) {
        this.totalTackles = totalTackles;
    }


    public String getDefTD() {
        return defTD;
    }


    public void setDefTD(String defTD) {
        this.defTD = defTD;
    }


    public String getSoloTackles() {
        return soloTackles;
    }


    public void setSoloTackles(String soloTackles) {
        this.soloTackles = soloTackles;
    }


    public String getTfls() {
        return tfls;
    }


    public void setTfls(String tfls) {
        this.tfls = tfls;
    }


    public String getQbHits() {
        return qbHits;
    }


    public void setQbHits(String qbHits) {
        this.qbHits = qbHits;
    }


    public String getDefInterceptions() {
        return defInterceptions;
    }


    public void setDefInterceptions(String defInterceptions) {
        this.defInterceptions = defInterceptions;
    }


    public String getSacks() {
        return sacks;
    }


    public void setSacks(String sacks) {
        this.sacks = sacks;
    }


    public String getPassDeflections() {
        return passDeflections;
    }


    public void setPassDeflections(String passDeflections) {
        this.passDeflections = passDeflections;
    }


    public String getInterceptions() {
        return interceptions;
    }


    public void setInterceptions(String interceptions) {
        this.interceptions = interceptions;
    }
    public double getFantasyPoints() {
        return fantasyPoints;
    }
    public void setFantasyPoints(double fantasyPoints) {
        this.fantasyPoints = fantasyPoints;
    }
    
}
