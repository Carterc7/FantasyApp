package com.fantasy.fantasyapi;

import java.util.ArrayList;
import java.util.List;

import com.fantasy.fantasyapi.apiCalls.GetAllPlayers;
import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.model.AdpPlayer;
import com.fantasy.fantasyapi.model.EspnPlayer;

public class Test 
{
    public static void main(String[] args)
    {
       GetAllPlayers getAllPlayers = new GetAllPlayers();
       List<EspnPlayer> filteredList = new ArrayList<EspnPlayer>();
       filteredList = getAllPlayers.getFilteredPlayerList(150);
       System.out.println("-----------------------------------------");
       System.out.println("ESPN Player List");
       System.out.println("-----------------------------------------");
       for(EspnPlayer player : filteredList)
       {
         System.out.println(player.getEspnName() + " " + player.getPos());
       }
       System.out.println("-----------------------------------------");
       System.out.println("END ESPN Player List");
       System.out.println("-----------------------------------------");
       GetDraftAdp getDraftAdp = new GetDraftAdp();
       List<AdpPlayer> adpList = new ArrayList<AdpPlayer>();
       adpList = getDraftAdp.getFilteredAdpList(100);
       System.out.println("-----------------------------------------");
       System.out.println("ADP Player List");
       System.out.println("-----------------------------------------");
       for(AdpPlayer player : adpList)
       {
        System.out.println(player.getName() + " " + player.getPosition() + " " + player.getAverageDraftPositionPPR());
       }
       System.out.println("-----------------------------------------");
       System.out.println("END ADP Player List");
       System.out.println("-----------------------------------------");
    }
}
