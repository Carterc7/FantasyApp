package com.fantasy.fantasyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

    // Model class for data received by adp API
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AdpPlayer
    {
        @JsonProperty("PlayerID")
        private int playerID;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Team")
        private String team;
        @JsonProperty("Position")
        private String position;
        @JsonProperty("AverageDraftPosition")
        private double averageDraftPosition;
        @JsonProperty("AverageDraftPositionPPR")
        private double averageDraftPositionPPR;
        @JsonProperty("ByeWeek")
        private int byeWeek;
        @JsonProperty("AuctionValue")
        private int auctionValue;
        @JsonProperty("AuctionValuePPR")
        private int auctionValuePPR;
        @JsonProperty("AverageDraftPositionIDP")
        private Double averageDraftPositionIDP;
        @JsonProperty("AverageDraftPositionRookie")
        private Double averageDraftPositionRookie;
        @JsonProperty("AverageDraftPositionDynasty")
        private Double averageDraftPositionDynasty;
        @JsonProperty("AverageDraftPosition2QB")
        private Double averageDraftPosition2QB;
        public int getPlayerID() {
            return playerID;
        }
        public void setPlayerID(int playerID) {
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
        public double getAverageDraftPosition() {
            return averageDraftPosition;
        }
        public void setAverageDraftPosition(double averageDraftPosition) {
            this.averageDraftPosition = averageDraftPosition;
        }
        public double getAverageDraftPositionPPR() {
            return averageDraftPositionPPR;
        }
        public void setAverageDraftPositionPPR(double averageDraftPositionPPR) {
            this.averageDraftPositionPPR = averageDraftPositionPPR;
        }
        public int getByeWeek() {
            return byeWeek;
        }
        public void setByeWeek(int byeWeek) {
            this.byeWeek = byeWeek;
        }
        public int getAuctionValue() {
            return auctionValue;
        }
        public void setAuctionValue(int auctionValue) {
            this.auctionValue = auctionValue;
        }
        public int getAuctionValuePPR() {
            return auctionValuePPR;
        }
        public void setAuctionValuePPR(int auctionValuePPR) {
            this.auctionValuePPR = auctionValuePPR;
        }
        public Double getAverageDraftPositionIDP() {
            return averageDraftPositionIDP;
        }
        public void setAverageDraftPositionIDP(Double averageDraftPositionIDP) {
            this.averageDraftPositionIDP = averageDraftPositionIDP;
        }
        public Double getAverageDraftPositionRookie() {
            return averageDraftPositionRookie;
        }
        public void setAverageDraftPositionRookie(Double averageDraftPositionRookie) {
            this.averageDraftPositionRookie = averageDraftPositionRookie;
        }
        public Double getAverageDraftPositionDynasty() {
            return averageDraftPositionDynasty;
        }
        public void setAverageDraftPositionDynasty(Double averageDraftPositionDynasty) {
            this.averageDraftPositionDynasty = averageDraftPositionDynasty;
        }
        public Double getAverageDraftPosition2QB() {
            return averageDraftPosition2QB;
        }
        public void setAverageDraftPosition2QB(Double averageDraftPosition2QB) {
            this.averageDraftPosition2QB = averageDraftPosition2QB;
        } 
    }
