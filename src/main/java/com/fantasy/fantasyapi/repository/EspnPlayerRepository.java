package com.fantasy.fantasyapi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.model.EspnPlayer;
 
public interface EspnPlayerRepository extends MongoRepository<EspnPlayer, ObjectId>
{
    Optional<EspnPlayer> findPlayerByPlayerID(String playerID);
}
