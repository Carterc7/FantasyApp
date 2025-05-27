package com.fantasy.fantasyapi.objectModels;

import java.util.Map;

import org.springframework.data.annotation.Id;

import com.fantasy.fantasyapi.objectModels.PlayerGameStats.GameData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDetails {
    @Id
    public String espnID;
    public String espnName;
    public String sleeperBotID;
    public String fantasyProsPlayerID;
    public String espnIDFull;
    public String weight;
    public String jerseyNum;
    public String cbsShortName;
    public String team;
    public String yahooPlayerID;
    public String age;
    public String espnLink;
    public String yahooLink;
    public String bDay;
    public String espnHeadshot;
    public String isFreeAgent;
    public String rotoWirePlayerIDFull;
    public String cbsLongName;
    public Injury injury;
    public String teamID;
    public String pos;
    public String school;
    public String cbsPlayerID;
    public String longName;
    public String rotoWirePlayerID;
    public String height;
    public String cbsPlayerIDFull;
    public String lastGamePlayed;
    public String playerID;
    public String exp;
    public String fantasyProsLink;
    public Stats stats;

    // New field for game stats
    private Map<String, GameData> gameStats;
    

    // Getter and Setter for gameStats
    public Map<String, GameData> getGameStats() {
        return gameStats;
    }

    public void setGameStats(Map<String, GameData> gameStats) {
        this.gameStats = gameStats;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Injury {
        public String description;
        public String injDate;
        public String designation;
        public String injReturnDate; // This field will be ignored if it is present in the response
        
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getInjDate() {
            return injDate;
        }
        public void setInjDate(String injDate) {
            this.injDate = injDate;
        }
        public String getDesignation() {
            return designation;
        }
        public void setDesignation(String designation) {
            this.designation = designation;
        }
        public String getInjReturnDate() {
            return injReturnDate;
        }
        public void setInjReturnDate(String injReturnDate) {
            this.injReturnDate = injReturnDate;
        }

        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {
        public Rushing Rushing;
        public Passing Passing;
        public Receiving Receiving;
        public Defense Defense;
        public String teamID;
        public String team;
        public String teamAbv;

        public Rushing getRushing() {
            return Rushing;
        }
        public void setRushing(Rushing rushing) {
            Rushing = rushing;
        }
        public Passing getPassing() {
            return Passing;
        }
        public void setPassing(Passing passing) {
            Passing = passing;
        }
        public Receiving getReceiving() {
            return Receiving;
        }
        public void setReceiving(Receiving receiving) {
            Receiving = receiving;
        }
        public Defense getDefense() {
            return Defense;
        }
        public void setDefense(Defense defense) {
            Defense = defense;
        }
        public String getTeamID() {
            return teamID;
        }
        public void setTeamID(String teamID) {
            this.teamID = teamID;
        }
        public String getTeam() {
            return team;
        }
        public void setTeam(String team) {
            this.team = team;
        }
        public String getTeamAbv() {
            return teamAbv;
        }
        public void setTeamAbv(String teamAbv) {
            this.teamAbv = teamAbv;
        }

        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rushing {
        public String rushYds;
        public String carries;
        public String rushTD;

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
        public String getRushTD() {
            return rushTD;
        }
        public void setRushTD(String rushTD) {
            this.rushTD = rushTD;
        }

        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Passing {
        public String passAttempts;
        public String passTD;
        public String passYds;
        public String passCompletions;
        @JsonProperty("int")
        public String intVal; // 'int' is a Java keyword, use 'intVal'


        public String getPassAttempts() {
            return passAttempts;
        }
        public void setPassAttempts(String passAttempts) {
            this.passAttempts = passAttempts;
        }
        public String getPassTD() {
            return passTD;
        }
        public void setPassTD(String passTD) {
            this.passTD = passTD;
        }
        public String getPassYds() {
            return passYds;
        }
        public void setPassYds(String passYds) {
            this.passYds = passYds;
        }
        public String getPassCompletions() {
            return passCompletions;
        }
        public void setPassCompletions(String passCompletions) {
            this.passCompletions = passCompletions;
        }
        public String getIntVal() {
            return intVal;
        }
        public void setIntVal(String intVal) {
            this.intVal = intVal;
        }

        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Receiving {
        public String receptions;
        public String recTD;
        public String targets;
        public String recYds;
        public String gamesPlayed;


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
        public String getTargets() {
            return targets;
        }
        public void setTargets(String targets) {
            this.targets = targets;
        }
        public String getRecYds() {
            return recYds;
        }
        public void setRecYds(String recYds) {
            this.recYds = recYds;
        }
        public String getGamesPlayed() {
            return gamesPlayed;
        }
        public void setGamesPlayed(String gamesPlayed) {
            this.gamesPlayed = gamesPlayed;
        }

        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Defense {
        public String totalTackles;
        public String fumblesLost;
        public String defTD;
        public String fumbles;
        public String fumblesRecovered;
        public String soloTackles;
        public String defensiveInterceptions;
        public String qbHits;
        public String tfl;
        public String passDeflections;
        public String sacks;


        public String getTotalTackles() {
            return totalTackles;
        }
        public void setTotalTackles(String totalTackles) {
            this.totalTackles = totalTackles;
        }
        public String getFumblesLost() {
            return fumblesLost;
        }
        public void setFumblesLost(String fumblesLost) {
            this.fumblesLost = fumblesLost;
        }
        public String getDefTD() {
            return defTD;
        }
        public void setDefTD(String defTD) {
            this.defTD = defTD;
        }
        public String getFumbles() {
            return fumbles;
        }
        public void setFumbles(String fumbles) {
            this.fumbles = fumbles;
        }
        public String getFumblesRecovered() {
            return fumblesRecovered;
        }
        public void setFumblesRecovered(String fumblesRecovered) {
            this.fumblesRecovered = fumblesRecovered;
        }
        public String getSoloTackles() {
            return soloTackles;
        }
        public void setSoloTackles(String soloTackles) {
            this.soloTackles = soloTackles;
        }
        public String getDefensiveInterceptions() {
            return defensiveInterceptions;
        }
        public void setDefensiveInterceptions(String defensiveInterceptions) {
            this.defensiveInterceptions = defensiveInterceptions;
        }
        public String getQbHits() {
            return qbHits;
        }
        public void setQbHits(String qbHits) {
            this.qbHits = qbHits;
        }
        public String getTfl() {
            return tfl;
        }
        public void setTfl(String tfl) {
            this.tfl = tfl;
        }
        public String getPassDeflections() {
            return passDeflections;
        }
        public void setPassDeflections(String passDeflections) {
            this.passDeflections = passDeflections;
        }
        public String getSacks() {
            return sacks;
        }
        public void setSacks(String sacks) {
            this.sacks = sacks;
        }

        
    }

    public String getEspnID() {
        return espnID;
    }

    public void setEspnID(String espnID) {
        this.espnID = espnID;
    }

    public String getEspnName() {
        return espnName;
    }

    public void setEspnName(String espnName) {
        this.espnName = espnName;
    }

    public String getSleeperBotID() {
        return sleeperBotID;
    }

    public void setSleeperBotID(String sleeperBotID) {
        this.sleeperBotID = sleeperBotID;
    }

    public String getFantasyProsPlayerID() {
        return fantasyProsPlayerID;
    }

    public void setFantasyProsPlayerID(String fantasyProsPlayerID) {
        this.fantasyProsPlayerID = fantasyProsPlayerID;
    }

    public String getEspnIDFull() {
        return espnIDFull;
    }

    public void setEspnIDFull(String espnIDFull) {
        this.espnIDFull = espnIDFull;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getJerseyNum() {
        return jerseyNum;
    }

    public void setJerseyNum(String jerseyNum) {
        this.jerseyNum = jerseyNum;
    }

    public String getCbsShortName() {
        return cbsShortName;
    }

    public void setCbsShortName(String cbsShortName) {
        this.cbsShortName = cbsShortName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getYahooPlayerID() {
        return yahooPlayerID;
    }

    public void setYahooPlayerID(String yahooPlayerID) {
        this.yahooPlayerID = yahooPlayerID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEspnLink() {
        return espnLink;
    }

    public void setEspnLink(String espnLink) {
        this.espnLink = espnLink;
    }

    public String getYahooLink() {
        return yahooLink;
    }

    public void setYahooLink(String yahooLink) {
        this.yahooLink = yahooLink;
    }

    public String getbDay() {
        return bDay;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public String getEspnHeadshot() {
        return espnHeadshot;
    }

    public void setEspnHeadshot(String espnHeadshot) {
        this.espnHeadshot = espnHeadshot;
    }

    public String getIsFreeAgent() {
        return isFreeAgent;
    }

    public void setIsFreeAgent(String isFreeAgent) {
        this.isFreeAgent = isFreeAgent;
    }

    public String getRotoWirePlayerIDFull() {
        return rotoWirePlayerIDFull;
    }

    public void setRotoWirePlayerIDFull(String rotoWirePlayerIDFull) {
        this.rotoWirePlayerIDFull = rotoWirePlayerIDFull;
    }

    public String getCbsLongName() {
        return cbsLongName;
    }

    public void setCbsLongName(String cbsLongName) {
        this.cbsLongName = cbsLongName;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCbsPlayerID() {
        return cbsPlayerID;
    }

    public void setCbsPlayerID(String cbsPlayerID) {
        this.cbsPlayerID = cbsPlayerID;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getRotoWirePlayerID() {
        return rotoWirePlayerID;
    }

    public void setRotoWirePlayerID(String rotoWirePlayerID) {
        this.rotoWirePlayerID = rotoWirePlayerID;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCbsPlayerIDFull() {
        return cbsPlayerIDFull;
    }

    public void setCbsPlayerIDFull(String cbsPlayerIDFull) {
        this.cbsPlayerIDFull = cbsPlayerIDFull;
    }

    public String getLastGamePlayed() {
        return lastGamePlayed;
    }

    public void setLastGamePlayed(String lastGamePlayed) {
        this.lastGamePlayed = lastGamePlayed;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getFantasyProsLink() {
        return fantasyProsLink;
    }

    public void setFantasyProsLink(String fantasyProsLink) {
        this.fantasyProsLink = fantasyProsLink;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

}
