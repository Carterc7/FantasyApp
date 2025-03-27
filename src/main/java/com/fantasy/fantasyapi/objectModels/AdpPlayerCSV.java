package com.fantasy.fantasyapi.objectModels;

public class AdpPlayerCSV {
    private int rank;
    private String playerName;
    private String position;
    private String positionRank;
    private double adpFeb4;
    private double adpMar11;
    private double adpChange;

    public AdpPlayerCSV(int rank, String playerName, String position, String positionRank, 
                        double adpFeb4, double adpMar11, double adpChange) {
        this.rank = rank;
        this.playerName = playerName;
        this.position = position;
        this.positionRank = positionRank;
        this.adpFeb4 = adpFeb4;
        this.adpMar11 = adpMar11;
        this.adpChange = adpChange;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionRank() {
        return positionRank;
    }

    public void setPositionRank(String positionRank) {
        this.positionRank = positionRank;
    }

    public double getAdpFeb4() {
        return adpFeb4;
    }

    public void setAdpFeb4(double adpFeb4) {
        this.adpFeb4 = adpFeb4;
    }

    public double getAdpMar11() {
        return adpMar11;
    }

    public void setAdpMar11(double adpMar11) {
        this.adpMar11 = adpMar11;
    }

    public double getAdpChange() {
        return adpChange;
    }

    public void setAdpChange(double adpChange) {
        this.adpChange = adpChange;
    }

    @Override
    public String toString() {
        return "AdpPlayerCSV{" +
                "rank=" + rank +
                ", playerName='" + playerName + '\'' +
                ", position='" + position + '\'' +
                ", positionRank='" + positionRank + '\'' +
                ", adpFeb4=" + adpFeb4 +
                ", adpMar11=" + adpMar11 +
                ", adpChange=" + adpChange +
                '}';
    }
}


