package com.fantasy.fantasyapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.leagueModels.User;

/**
 * Repository to open connection to User object collection in MongoDB
 */
public interface UserRepository extends MongoRepository<User, String>
{
    void deleteByUserID(String userID);
}
