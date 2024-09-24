package com.fantasy.fantasyapi.leagueModels;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Model for an app user
@Document(collection = "user")
public class User 
{
    @NotEmpty(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+]).+$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;
    @Id
    private String userID;
    // User's list of completed teams, includes mock teams
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
