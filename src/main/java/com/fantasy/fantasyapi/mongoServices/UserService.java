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

    public void deleteUserByUserID(String userID) 
    {
        userRepository.deleteByUserID(userID);
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Assuming you're storing passwords securely and using a secure comparison method
            return user.getPassword().equals(password);
        }
        return false;
    }

    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

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
