package com.fantasy.fantasyapi.leagueModels;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Model for an app user
@Document(collection = "user")
public class User 
{
    private String username;
    private String password;
    @Id
    private String userID;
    private List<FantasyTeam> completedMocks;
    
    public User() {
    }

    public User(String username, String password, String userID, List<FantasyTeam> completedMocks) {
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

    public List<FantasyTeam> getCompletedMocks() {
        return completedMocks;
    }

    public void setCompletedMocks(List<FantasyTeam> completedMocks) {
        this.completedMocks = completedMocks;
    }
    
}
