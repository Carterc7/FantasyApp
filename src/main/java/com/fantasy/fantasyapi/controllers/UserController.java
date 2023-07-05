package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /** 
     * @param user
     * @return User
     */
    @PostMapping("/add")
    public User addUser(@RequestBody User user) 
    {
        return userService.addUser(user);
    }

    /** 
     * @param userID
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable("userID") String userID) 
    {
        try 
        {
            userRepository.deleteByUserID(userID);
            return ResponseEntity.ok("User with userID: " + userID + " deleted successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user with userID: " + userID);
        }
    }

    /** 
     * @param user
     * @return User
     */
    @PutMapping("/update")
    public User updateUser(@RequestBody User user)
    {
        return userService.updateUser(user);
    }
}

