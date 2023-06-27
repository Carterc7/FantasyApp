package com.fantasy.fantasyapi.mongoServices;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.objectModels.TeamSchedule;
import com.fantasy.fantasyapi.repository.TeamScheduleRepository;

@Service
@Component
public class TeamScheduleService 
{
    @Autowired
    TeamScheduleRepository teamScheduleRepository;

    public Optional<List<TeamSchedule>> findGameByGameID(String gameID)
    {
        return teamScheduleRepository.findGameByGameID(gameID);
    }

    public Optional<List<TeamSchedule>> findScheduleByTeam(String team)
    {
        return teamScheduleRepository.findScheduleByTeam(team);
    }

    public Optional<List<TeamSchedule>> findScheduleByTeamAndGameWeek(String team, String gameWeek)
    {
        return teamScheduleRepository.findScheduleByTeamAndGameWeek(team, gameWeek);
    }

    public Optional<List<TeamSchedule>> findScheduleByGameWeek(String gameWeek)
    {
        return teamScheduleRepository.findScheduleByGameWeek(gameWeek);
    }
}
