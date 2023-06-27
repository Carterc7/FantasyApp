package com.fantasy.fantasyapi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.model.AdpPlayer;

/**
 * Repository to open connection to AdpPlayer object collection in MongoDB
 */
public interface AdpPlayerRepository extends MongoRepository<AdpPlayer, ObjectId>
{
     Optional<AdpPlayer> findPlayerByPlayerID(String playerID);

     Optional<AdpPlayer> findPlayerByAverageDraftPositionPPR(int averageDraftPositionPPR);
}
