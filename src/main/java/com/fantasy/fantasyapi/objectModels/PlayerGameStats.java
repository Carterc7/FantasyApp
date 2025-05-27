package com.fantasy.fantasyapi.objectModels;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerGameStats {
    @JsonProperty("games")
    private Map<String, GameData> games;

    public Map<String, GameData> getGames() {
        return games;
    }

    public void setGames(Map<String, GameData> games) {
        this.games = games;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameData {
        @JsonProperty("gameID")
        private String gameID;
        @JsonProperty("snapCounts")
        private SnapCounts snapCounts;
        @JsonProperty("Receiving")
        private Receiving receiving;
        @JsonProperty("Rushing")
        private Rushing rushing;
        @JsonProperty("Passing")
        private Passing passing;  // Added Passing
        @JsonProperty("fantasyPointsDefault")
        private FantasyPointsDefault fantasyPointsDefault;

        // Getters and Setters
        public String getGameID() {
            return gameID;
        }

        public void setGameID(String gameID) {
            this.gameID = gameID;
        }

        public SnapCounts getSnapCounts() {
            return snapCounts;
        }

        public void setSnapCounts(SnapCounts snapCounts) {
            this.snapCounts = snapCounts;
        }

        public Receiving getReceiving() {
            return receiving;
        }

        public void setReceiving(Receiving receiving) {
            this.receiving = receiving;
        }

        public Rushing getRushing() {
            return rushing;
        }

        public void setRushing(Rushing rushing) {
            this.rushing = rushing;
        }

        public Passing getPassing() {
            return passing;
        }

        public void setPassing(Passing passing) {
            this.passing = passing;
        }

        public FantasyPointsDefault getFantasyPointsDefault() {
            return fantasyPointsDefault;
        }

        public void setFantasyPointsDefault(FantasyPointsDefault fantasyPointsDefault) {
            this.fantasyPointsDefault = fantasyPointsDefault;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SnapCounts {
            @JsonProperty("offSnapPct")
            private String offSnapPct;
            @JsonProperty("defSnap")
            private String defSnap;
            @JsonProperty("stSnap")
            private String stSnap;
            @JsonProperty("stSnapPct")
            private String stSnapPct;
            @JsonProperty("offSnap")
            private String offSnap;
            @JsonProperty("defSnapPct")
            private String defSnapPct;

            // Getters and Setters
            public String getOffSnapPct() {
                return offSnapPct;
            }

            public void setOffSnapPct(String offSnapPct) {
                this.offSnapPct = offSnapPct;
            }

            public String getDefSnap() {
                return defSnap;
            }

            public void setDefSnap(String defSnap) {
                this.defSnap = defSnap;
            }

            public String getStSnap() {
                return stSnap;
            }

            public void setStSnap(String stSnap) {
                this.stSnap = stSnap;
            }

            public String getStSnapPct() {
                return stSnapPct;
            }

            public void setStSnapPct(String stSnapPct) {
                this.stSnapPct = stSnapPct;
            }

            public String getOffSnap() {
                return offSnap;
            }

            public void setOffSnap(String offSnap) {
                this.offSnap = offSnap;
            }

            public String getDefSnapPct() {
                return defSnapPct;
            }

            public void setDefSnapPct(String defSnapPct) {
                this.defSnapPct = defSnapPct;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Receiving {
            @JsonProperty("receptions")
            private String receptions;
            @JsonProperty("recTD")
            private String recTD;
            @JsonProperty("longRec")
            private String longRec;
            @JsonProperty("targets")
            private String targets;
            @JsonProperty("recYds")
            private String recYds;
            @JsonProperty("recAvg")
            private String recAvg;

            // Getters and Setters
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

            public String getLongRec() {
                return longRec;
            }

            public void setLongRec(String longRec) {
                this.longRec = longRec;
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

            public String getRecAvg() {
                return recAvg;
            }

            public void setRecAvg(String recAvg) {
                this.recAvg = recAvg;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rushing {
            @JsonProperty("rushYds")
            private String rushYds;
            @JsonProperty("carries")
            private String carries;
            @JsonProperty("rushTD")
            private String rushTD;
            @JsonProperty("rushAvg")
            private String rushAvg;
            @JsonProperty("longRush")
            private String longRush;

            // Getters and Setters
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

            public String getRushAvg() {
                return rushAvg;
            }

            public void setRushAvg(String rushAvg) {
                this.rushAvg = rushAvg;
            }

            public String getLongRush() {
                return longRush;
            }

            public void setLongRush(String longRush) {
                this.longRush = longRush;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Passing {  // Added Passing
            @JsonProperty("qbr")
            private String qbr;
            @JsonProperty("rtg")
            private String rtg;
            @JsonProperty("sacked")
            private String sacked;
            @JsonProperty("passAttempts")
            private String passAttempts;
            @JsonProperty("passAvg")
            private String passAvg;
            @JsonProperty("passTD")
            private String passTD;
            @JsonProperty("passYds")
            private String passYds;
            @JsonProperty("int")
            private String interceptions;
            @JsonProperty("passingTwoPointConversion")
            private String passingTwoPointConversion;
            @JsonProperty("passCompletions")
            private String passCompletions;

            // Getters and Setters
            public String getQbr() {
                return qbr;
            }

            public void setQbr(String qbr) {
                this.qbr = qbr;
            }

            public String getRtg() {
                return rtg;
            }

            public void setRtg(String rtg) {
                this.rtg = rtg;
            }

            public String getSacked() {
                return sacked;
            }

            public void setSacked(String sacked) {
                this.sacked = sacked;
            }

            public String getPassAttempts() {
                return passAttempts;
            }

            public void setPassAttempts(String passAttempts) {
                this.passAttempts = passAttempts;
            }

            public String getPassAvg() {
                return passAvg;
            }

            public void setPassAvg(String passAvg) {
                this.passAvg = passAvg;
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

            public String getInterceptions() {
                return interceptions;
            }

            public void setInterceptions(String interceptions) {
                this.interceptions = interceptions;
            }

            public String getPassingTwoPointConversion() {
                return passingTwoPointConversion;
            }

            public void setPassingTwoPointConversion(String passingTwoPointConversion) {
                this.passingTwoPointConversion = passingTwoPointConversion;
            }

            public String getPassCompletions() {
                return passCompletions;
            }

            public void setPassCompletions(String passCompletions) {
                this.passCompletions = passCompletions;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FantasyPointsDefault {
            @JsonProperty("standard")
            private String standard;
            @JsonProperty("PPR")
            private String PPR;
            @JsonProperty("halfPPR")
            private String halfPPR;

            // Getters and Setters
            public String getStandard() {
                return standard;
            }

            public void setStandard(String standard) {
                this.standard = standard;
            }

            public String getPPR() {
                return PPR;
            }

            public void setPPR(String PPR) {
                this.PPR = PPR;
            }

            public String getHalfPPR() {
                return halfPPR;
            }

            public void setHalfPPR(String halfPPR) {
                this.halfPPR = halfPPR;
            }
        }
    }
}





