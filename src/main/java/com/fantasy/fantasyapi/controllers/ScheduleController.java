package com.fantasy.fantasyapi.controllers;


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

    @GetMapping("/{gameID}")
    public Optional<TeamSchedule>[] findGameByID(@PathVariable String gameID)
    {
        return teamScheduleRepository.findGameByGameID(gameID);
    }
}