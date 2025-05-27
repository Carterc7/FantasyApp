package com.fantasy.fantasyapi.objectModels;

public class DraftPlayer {
    private AdpPlayerCSV adpPlayer;
    private String espnHeadshot;
    private String team;

    public DraftPlayer(AdpPlayerCSV adpPlayer, String espnHeadshot, String team) {
        this.adpPlayer = adpPlayer;
        this.espnHeadshot = espnHeadshot;
        this.team = team;
    }

    public AdpPlayerCSV getAdpPlayer() {
        return adpPlayer;
    }

    public String getEspnHeadshot() {
        return espnHeadshot;
    }

    public String getTeam() {
        return team;
    }
}
