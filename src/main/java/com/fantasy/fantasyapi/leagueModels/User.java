package com.fantasy.fantasyapi.leagueModels;

import org.springframework.data.mongodb.core.mapping.Document;

// Model for an app user
@Document(collection = "user")
public class User 
{
    private String username;
    private String password;
    private String userID;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String teamName;
    
    public User() {
    }
    public User(String username, String password, String userID, String email, String firstName, String lastName,
            String dateOfBirth, String teamName) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.teamName = teamName;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }  
}
