package com.fantasy.fantasyapi.mongoServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.apiCalls.GetTeamSchedules;
import com.fantasy.fantasyapi.objectModels.TeamSchedule;
import com.fantasy.fantasyapi.repository.TeamScheduleRepository;

@Service
@Component
public class TeamScheduleService {
    @Autowired
    TeamScheduleRepository teamScheduleRepository;

    public List<String> getGameIDsForSeason(String team, String seasonYear) {
        Optional<List<TeamSchedule>> scheduleOpt = teamScheduleRepository.findScheduleByTeam(team);

        if (scheduleOpt.isPresent()) {
            return scheduleOpt.get().stream()
                    .filter(schedule -> {
                        String gameID = schedule.getGameID();
                        String gameWeek = schedule.getGameWeek();

                        // Ensure game is not a preseason game
                        boolean isRegularOrPostseason = schedule.getSeasonType() != null &&
                                !schedule.getSeasonType().equalsIgnoreCase("Preseason");

                        // Check if gameID starts with the season year
                        boolean isSameYear = gameID.startsWith(seasonYear);

                        // Include Week 17 and Week 18, even if the game is in the next year
                        boolean isLateRegularSeason = (gameWeek != null) &&
                                (gameWeek.contains("Week 17") || gameWeek.contains("Week 18"));

                        // Include postseason games in the following year
                        boolean isPostseason = (gameWeek != null) &&
                                (gameWeek.contains("Wild Card") || gameWeek.contains("Divisional") ||
                                        gameWeek.contains("Conference Championship")
                                        || gameWeek.contains("Super Bowl"));

                        // Handle cases where a game is from the next year but is a late regular season
                        // or postseason game
                        boolean isValidNextYearGame = gameID
                                .startsWith(String.valueOf(Integer.parseInt(seasonYear) + 1)) &&
                                (isLateRegularSeason || isPostseason);

                        return isRegularOrPostseason && (isSameYear || isValidNextYearGame);
                    })
                    .map(TeamSchedule::getGameID) // Extract gameID
                    .toList();
        }

        return new ArrayList<>();
    }

    public Optional<List<TeamSchedule>> findGameByGameID(String gameID) {
        return teamScheduleRepository.findGameByGameID(gameID);
    }

    public Optional<List<TeamSchedule>> findScheduleByTeam(String team) {
        return teamScheduleRepository.findScheduleByTeam(team);
    }

    public Optional<List<TeamSchedule>> findScheduleByTeamAndGameWeek(String team, String gameWeek) {
        return teamScheduleRepository.findScheduleByTeamAndGameWeek(team, gameWeek);
    }

    public Optional<List<TeamSchedule>> findScheduleByGameWeek(String gameWeek) {
        return teamScheduleRepository.findScheduleByGameWeek(gameWeek);
    }

    public List<TeamSchedule> addTeamSchedule(String teamAbv, String season) {
        GetTeamSchedules getTeamSchedules = new GetTeamSchedules();
        List<TeamSchedule> schedulesToAdd = getTeamSchedules.getTeamSchedule(teamAbv, season);
        return teamScheduleRepository.saveAll(schedulesToAdd);
    }
}
