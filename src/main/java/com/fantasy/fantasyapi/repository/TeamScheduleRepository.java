package com.fantasy.fantasyapi.repository;


import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.model.TeamSchedule;

public interface TeamScheduleRepository extends MongoRepository<TeamSchedule, ObjectId>
{
    Optional<TeamSchedule>[] findGameByGameID(String gameID);
}
