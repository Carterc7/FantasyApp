package com.fantasy.fantasyapi.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.fantasyapi.mongoServices.TeamScheduleService;
import com.fantasy.fantasyapi.objectModels.TeamSchedule;
import com.fantasy.fantasyapi.repository.TeamScheduleRepository;

@RestController
@RequestMapping("/schedule")
@CrossOrigin(origins = "http://localhost:3000")
public class ScheduleController 
{
    @Autowired
    TeamScheduleRepository teamScheduleRepository;

    @Autowired
    TeamScheduleService teamScheduleService;

    /** 
     * @param gameID
     * @return Optional<List<TeamSchedule>>
     */
    @GetMapping("/gameID/{gameID}")
    public Optional<List<TeamSchedule>> findGameByID(@PathVariable String gameID)
    {
        return teamScheduleRepository.findGameByGameID(gameID);
    }

    /** 
     * @param team
     * @return Optional<List<TeamSchedule>>
     */
    @GetMapping("/team/{team}")
    public Optional<List<TeamSchedule>> findScheduleByTeam(@PathVariable String team)
    {
        return teamScheduleRepository.findScheduleByTeam(team);
    }

    /** 
     * @param gameWeek
     * @return Optional<List<TeamSchedule>>
     */
    @GetMapping("/week/{gameWeek}")
    public Optional<List<TeamSchedule>> findScheduleByGameWeek(@PathVariable String gameWeek)
    {
        return teamScheduleRepository.findScheduleByGameWeek(gameWeek);
    }

    /** 
     * @param team
     * @param gameWeek
     * @return Optional<List<TeamSchedule>>
     */
    @GetMapping("/team/{team}/{gameWeek}")
    public Optional<List<TeamSchedule>> findScheduleByTeamAndGameWeek(@PathVariable String team, @PathVariable String gameWeek)
    {
        return teamScheduleRepository.findScheduleByTeamAndGameWeek(team, gameWeek);
    }

    /** 
     * @param teamAbv
     * @param season
     * @return List<TeamSchedule>
     */
    @PostMapping("/add/{teamAbv}/{season}")
    public List<TeamSchedule> addTeamSchedule(@PathVariable String teamAbv, @PathVariable String season)
    {
        return teamScheduleService.addTeamSchedule(teamAbv, season);
    }
}
