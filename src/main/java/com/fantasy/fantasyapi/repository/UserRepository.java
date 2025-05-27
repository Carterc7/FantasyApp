package com.fantasy.fantasyapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.leagueModels.User;


/**
 * Repository to open connection to User object collection in MongoDB
 */
public interface UserRepository extends MongoRepository<User, String>
{
    void deleteByUserID(String userID);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserID(String userID);
}
