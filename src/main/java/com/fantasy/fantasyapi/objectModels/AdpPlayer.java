package com.fantasy.fantasyapi.objectModels;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

    // Model class for data received by adp API
    @Document(collection = "fantasyPlayersAdp")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AdpPlayer
    {
        @JsonProperty("PlayerID")
        private String playerID;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Team")
        private String team;
        @JsonProperty("Position")
        private String position;
        @JsonProperty("AverageDraftPosition")
        private int averageDraftPosition;
        @JsonProperty("AverageDraftPositionPPR")
        private int averageDraftPositionPPR;
        @JsonProperty("ByeWeek")
        private String byeWeek;
        @JsonProperty("AuctionValue")
        private String auctionValue;
        @JsonProperty("AuctionValuePPR")
        private String auctionValuePPR;
        @JsonProperty("AverageDraftPositionIDP")
        private String averageDraftPositionIDP;
        @JsonProperty("AverageDraftPositionRookie")
        private String averageDraftPositionRookie;
        @JsonProperty("AverageDraftPositionDynasty")
        private int averageDraftPositionDynasty;
        @JsonProperty("AverageDraftPosition2QB")
        private int averageDraftPosition2QB;
        
        public String getPlayerID() {
            return playerID;
        }
        public void setPlayerID(String playerID) {
            this.playerID = playerID;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getTeam() {
            return team;
        }
        public void setTeam(String team) {
            this.team = team;
        }
        public String getPosition() {
            return position;
        }
        public void setPosition(String position) {
            this.position = position;
        }
        public int getAverageDraftPosition() {
            return averageDraftPosition;
        }
        public void setAverageDraftPosition(int averageDraftPosition) {
            this.averageDraftPosition = averageDraftPosition;
        }
        public int getAverageDraftPositionPPR() {
            return averageDraftPositionPPR;
        }
        public void setAverageDraftPositionPPR(int averageDraftPositionPPR) {
            this.averageDraftPositionPPR = averageDraftPositionPPR;
        }
        public String getByeWeek() {
            return byeWeek;
        }
        public void setByeWeek(String byeWeek) {
            this.byeWeek = byeWeek;
        }
        public String getAuctionValue() {
            return auctionValue;
        }
        public void setAuctionValue(String auctionValue) {
            this.auctionValue = auctionValue;
        }
        public String getAuctionValuePPR() {
            return auctionValuePPR;
        }
        public void setAuctionValuePPR(String auctionValuePPR) {
            this.auctionValuePPR = auctionValuePPR;
        }
        public String getAverageDraftPositionIDP() {
            return averageDraftPositionIDP;
        }
        public void setAverageDraftPositionIDP(String averageDraftPositionIDP) {
            this.averageDraftPositionIDP = averageDraftPositionIDP;
        }
        public String getAverageDraftPositionRookie() {
            return averageDraftPositionRookie;
        }
        public void setAverageDraftPositionRookie(String averageDraftPositionRookie) {
            this.averageDraftPositionRookie = averageDraftPositionRookie;
        }
        public int getAverageDraftPositionDynasty() {
            return averageDraftPositionDynasty;
        }
        public void setAverageDraftPositionDynasty(int averageDraftPositionDynasty) {
            this.averageDraftPositionDynasty = averageDraftPositionDynasty;
        }
        public int getAverageDraftPosition2QB() {
            return averageDraftPosition2QB;
        }
        public void setAverageDraftPosition2QB(int averageDraftPosition2QB) {
            this.averageDraftPosition2QB = averageDraftPosition2QB;
        }
        
    }
