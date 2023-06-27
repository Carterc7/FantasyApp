package com.fantasy.fantasyapi.objectModels;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    // Model class for data received by NFLPlayers API
    @Document(collection = "fantasyPlayersEspn")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class EspnPlayer 
    {
        private String espnID;
        private String espnName;
        private String espnIDFull;
        private String weight;
        private String jerseyNum;
        private String cbsShortName;
        private String team;
        private String yahooPlayerID;
        private String age;
        private String espnLink;
        private String yahooLink;
        private String bDay;
        private String espnHeadshot;
        private String cbsLongName;
        private String teamID;
        private String pos;
        private String school;
        private String cbsPlayerID;
        private String longName;
        private String height;
        private String cbsPlayerIDFull;
        private String lastGamePlayed;
        private String playerID;
        private String exp;
        
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
        public String getCbsLongName() {
            return cbsLongName;
        }
        public void setCbsLongName(String cbsLongName) {
            this.cbsLongName = cbsLongName;
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
        public String getBDay() {
            return bDay;
        }
}