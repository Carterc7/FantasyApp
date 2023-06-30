package com.fantasy.fantasyapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.objectModels.EspnPlayer;
 
/**
 * Repository to open connection to EspnPlayer object collection in MongoDB
 */
public interface EspnPlayerRepository extends MongoRepository<EspnPlayer, String>
{
    Optional<EspnPlayer> findPlayerByPlayerID(String playerID);
}
