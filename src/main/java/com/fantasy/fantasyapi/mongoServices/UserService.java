package com.fantasy.fantasyapi.mongoServices;

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
        exisitingUser.setTeamName(user.getTeamName());
        exisitingUser.setFirstName(user.getFirstName());
        exisitingUser.setLastName(user.getLastName());
        exisitingUser.setEmail(user.getEmail());
        exisitingUser.setDateOfBirth(user.getDateOfBirth());
        return userRepository.save(exisitingUser);
    }
}
