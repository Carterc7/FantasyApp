package com.fantasy.fantasyapi.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.model.TeamSchedule;
import com.fantasy.fantasyapi.repository.TeamScheduleRepository;

@RestController
@RequestMapping("/schedule")
public class ScheduleController 
{
    @Autowired
    TeamScheduleRepository teamScheduleRepository;

    @GetMapping("/gameID/{gameID}")
    public Optional<List<TeamSchedule>> findGameByID(@PathVariable String gameID)
    {
        return teamScheduleRepository.findGameByGameID(gameID);
    }

    @GetMapping("/team/{team}")
    public Optional<List<TeamSchedule>> findScheduleByTeam(@PathVariable String team)
    {
        return teamScheduleRepository.findScheduleByTeam(team);
    }

    @GetMapping("/week/{gameWeek}")
    public Optional<List<TeamSchedule>> findScheduleByGameWeek(@PathVariable String gameWeek)
    {
        return teamScheduleRepository.findScheduleByGameWeek(gameWeek);
    }

    @GetMapping("/team/{team}/{gameWeek}")
    public Optional<List<TeamSchedule>> findScheduleByTeamAndGameWeek(@PathVariable String team, @PathVariable String gameWeek)
    {
        return teamScheduleRepository.findScheduleByTeamAndGameWeek(team, gameWeek);
    }
}
