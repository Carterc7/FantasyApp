package com.fantasy.fantasyapi.mongoServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import com.fantasy.fantasyapi.repository.AdpPlayerRepository;

@Service
@Component
public class AdpPlayerService 
{
    // Autowire repositories to gain access to mongo collection
    @Autowired
    private AdpPlayerRepository adpRepository;

    public Optional<AdpPlayer> findPlayerByPlayerID(String playerID)
    {
        return adpRepository.findPlayerByPlayerID(playerID);
    }

    public Optional<AdpPlayer> findPlayerByAverageDraftPositionPPR(int averageDraftPositionPPR)
    {
        return adpRepository.findPlayerByAverageDraftPositionPPR(averageDraftPositionPPR);
    }

    public AdpPlayer updateAdpPlayer(AdpPlayer adpPlayer)
    {
        AdpPlayer exisitingPlayer = adpRepository.findById(adpPlayer.getPlayerID()).get();
        exisitingPlayer.setAverageDraftPosition(adpPlayer.getAverageDraftPosition());
        exisitingPlayer.setAverageDraftPosition2QB(adpPlayer.getAverageDraftPosition2QB());
        exisitingPlayer.setAverageDraftPositionPPR(adpPlayer.getAverageDraftPositionPPR());
        exisitingPlayer.setName(adpPlayer.getName());
        exisitingPlayer.setByeWeek(adpPlayer.getByeWeek());
        exisitingPlayer.setTeam(adpPlayer.getTeam());
        exisitingPlayer.setAverageDraftPositionDynasty(adpPlayer.getAverageDraftPositionDynasty());
        exisitingPlayer.setAverageDraftPosition(adpPlayer.getAverageDraftPosition());
        exisitingPlayer.setPosition(adpPlayer.getPosition());
        return adpRepository.save(exisitingPlayer);
    }
}
