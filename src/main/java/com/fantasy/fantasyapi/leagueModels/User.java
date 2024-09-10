package com.fantasy.fantasyapi.leagueModels;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

// Model for an app user
@Document(collection = "user")
public class User 
{
    private String username;
    private String password;
    @Id
    private String userID;
     // Change completedMocks to a list of lists
    private List<List<FantasyTeam>> completedMocks = new ArrayList<>();
    
    public User() {
    }

    public User(String username, String password, String userID, List<List<FantasyTeam>> completedMocks) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.completedMocks = completedMocks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<List<FantasyTeam>> getCompletedMocks() {
        return completedMocks;
    }

    public void setCompletedMocks(List<List<FantasyTeam>> completedMocks) {
        this.completedMocks = completedMocks;
    }
    
}
