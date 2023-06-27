package com.fantasy.fantasyapi.repository;

import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fantasy.fantasyapi.objectModels.TeamSchedule;

/**
 * Repository to open connection to TeamSchedule object collection in MongoDB
 */
public interface TeamScheduleRepository extends MongoRepository<TeamSchedule, ObjectId>
{
    Optional<List<TeamSchedule>> findGameByGameID(String gameID);

    Optional<List<TeamSchedule>> findScheduleByTeam(String team);

    Optional<List<TeamSchedule>> findScheduleByGameWeek(String gameWeek);

    Optional<List<TeamSchedule>> findScheduleByTeamAndGameWeek(String team, String gameWeek);
}
