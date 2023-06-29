package com.fantasy.fantasyapi.mongoServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
}
