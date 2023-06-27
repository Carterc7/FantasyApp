package com.fantasy.fantasyapi.mongoServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.objectModels.EspnPlayer;
import com.fantasy.fantasyapi.repository.EspnPlayerRepository;

@Service
@Component
public class EspnPlayerService 
{
    @Autowired
    private EspnPlayerRepository espnRepository;

    public Optional<EspnPlayer> findPlayerByPlayerID(String playerID)
    {
        return espnRepository.findPlayerByPlayerID(playerID);
    }
}
