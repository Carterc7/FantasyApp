package com.fantasy.fantasyapi.objectModels;

import java.util.List;

import com.fantasy.fantasyapi.leagueModels.FantasyTeam;

public class PlayerSelectionRequest {
    private String selectedPlayerName;
    private int numOfTeams;
    private String userId;
    private int currentPick;
    private List<AdpPlayerCSV> adpList;
    private List<FantasyTeam> mockTeams;
    private int currentTeamId;
    private int roundNumber;
    private boolean isReversed;
    
    public String getSelectedPlayerName() {
        return selectedPlayerName;
    }
    public void setSelectedPlayerName(String selectedPlayerName) {
        this.selectedPlayerName = selectedPlayerName;
    }
    public int getNumOfTeams() {
        return numOfTeams;
    }
    public void setNumOfTeams(int numOfTeams) {
        this.numOfTeams = numOfTeams;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getCurrentPick() {
        return currentPick;
    }
    public void setCurrentPick(int currentPick) {
        this.currentPick = currentPick;
    }
    public List<AdpPlayerCSV> getAdpList() {
        return adpList;
    }
    public void setAdpList(List<AdpPlayerCSV> adpList) {
        this.adpList = adpList;
    }
    public List<FantasyTeam> getMockTeams() {
        return mockTeams;
    }
    public void setMockTeams(List<FantasyTeam> mockTeams) {
        this.mockTeams = mockTeams;
    }
    public int getCurrentTeamId() {
        return currentTeamId;
    }
    public void setCurrentTeamId(int currentTeamId) {
        this.currentTeamId = currentTeamId;
    }
    public int getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
    public boolean isReversed() {
        return isReversed;
    }
    public void setReversed(boolean isReversed) {
        this.isReversed = isReversed;
    }

    // Getters and setters
    
}

