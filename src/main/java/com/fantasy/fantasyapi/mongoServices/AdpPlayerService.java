package com.fantasy.fantasyapi.mongoServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.model.AdpPlayer;
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
}
