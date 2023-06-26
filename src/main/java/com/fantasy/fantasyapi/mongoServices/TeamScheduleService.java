package com.fantasy.fantasyapi.mongoServices;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.model.TeamSchedule;
import com.fantasy.fantasyapi.repository.TeamScheduleRepository;

@Service
@Component
public class TeamScheduleService 
{
    @Autowired
    TeamScheduleRepository teamScheduleRepository;

    public Optional<TeamSchedule>[] findGameByGameID(String gameID)
    {
        return teamScheduleRepository.findGameByGameID(gameID);
    }
}
