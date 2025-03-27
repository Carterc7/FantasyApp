package com.fantasy.fantasyapi.mongoServices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.repository.EspnPlayerRepository;

@Service
@Component
public class EspnPlayerService {
    @Autowired
    private EspnPlayerRepository espnRepository;

    public Optional<EspnPlayer> findPlayerByPlayerID(String playerID) {
        System.out.println("In findPlayerByPlayerID");
        return espnRepository.findPlayerByPlayerID(playerID);
    }

    public Optional<EspnPlayer> findPlayerByEspnName(String espnName) {
        List<EspnPlayer> players = espnRepository.findPlayersByEspnName(espnName);
        if (players.size() == 1) {
            return Optional.of(players.get(0));
        } else if (players.size() > 1) {
            // Handle case where there are multiple results, choose the best match (e.g.,
            // based on more data)
            return Optional.of(players.get(0)); // Or apply more logic here to decide
        } else {
            return Optional.empty();
        }
    }

    public EspnPlayer updateEspnPlayer(EspnPlayer EspnPlayer) {
        EspnPlayer exisitingPlayer = espnRepository.findById(EspnPlayer.getPlayerID()).get();
        exisitingPlayer.setAge(EspnPlayer.getAge());
        exisitingPlayer.setEspnHeadshot(EspnPlayer.getEspnHeadshot());
        exisitingPlayer.setEspnLink(EspnPlayer.getEspnLink());
        exisitingPlayer.setEspnName(EspnPlayer.getEspnName());
        exisitingPlayer.setExp(EspnPlayer.getExp());
        exisitingPlayer.setHeight(EspnPlayer.getHeight());
        exisitingPlayer.setJerseyNum(EspnPlayer.getJerseyNum());
        exisitingPlayer.setLastGamePlayed(EspnPlayer.getLastGamePlayed());
        exisitingPlayer.setPos(EspnPlayer.getPos());
        exisitingPlayer.setTeam(EspnPlayer.getTeam());
        exisitingPlayer.setTeamID(EspnPlayer.getTeamID());
        exisitingPlayer.setWeight(EspnPlayer.getWeight());
        exisitingPlayer.setbDay(EspnPlayer.getbDay());
        return espnRepository.save(exisitingPlayer);
    }
}
