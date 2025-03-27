package com.fantasy.fantasyapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fantasy.fantasyapi.objectModels.EspnPlayer;
 
/**
 * Repository to open connection to EspnPlayer object collection in MongoDB
 */
public interface EspnPlayerRepository extends MongoRepository<EspnPlayer, String>
{
    Optional<EspnPlayer> findPlayerByPlayerID(String playerID);

    Optional<EspnPlayer> findPlayerByEspnName(String espnName);

    @Query("{'espnName' : {$regex : ?0, $options: 'i'}}") // case-insensitive match
    List<EspnPlayer> findPlayersByEspnName(String espnName);
}
