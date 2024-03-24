package com.fantasy.fantasyapi.mongoServices;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.repository.UserRepository;

@Service
@Component
public class UserService 
{
    @Autowired
    UserRepository userRepository;

    /**
     * Method to delete user by userID
     * @param userID
     */
    public void deleteUserByUserID(String userID) 
    {
        userRepository.deleteByUserID(userID);
    }

    /**
     * Method to authenticate user based on username and password, returns bool
     * @param username
     * @param password
     * @return
     */
    public boolean authenticateUser(String username, String password) 
    {
        // find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        // check if found
        if (userOptional.isPresent()) 
        {
            // fetch user
            User user = userOptional.get();
            // check if match
            return user.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Method to search for user by username property
     * @param username
     * @return
     */
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    /**
     * Method to add user to `users` collection
     * @param user
     * @return
     */
    public User addUser(User user)
    {
        user.setUserID(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public User updateUser(User user)
    {
        User exisitingUser = userRepository.findById(user.getUserID()).get();
        exisitingUser.setUsername(user.getUsername());
        exisitingUser.setPassword(user.getPassword());
        exisitingUser.setCompletedMocks(user.getCompletedMocks());
        return userRepository.save(exisitingUser);
    }
}
