package com.fantasy.fantasyapi;

import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.controllers.DraftController;
import com.fantasy.fantasyapi.objectModels.AdpPlayer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class DraftTest {

    @Test
    void testShowDraftBoard() {
        // Setup variables
        DraftController draftController = new DraftController();
        Model model = Mockito.mock(Model.class);

        // Mocking the GetDraftAdp class and its method
        GetDraftAdp getDraftAdp = Mockito.mock(GetDraftAdp.class);
        List<AdpPlayer> adpList = Arrays.asList(
                new AdpPlayer("1", "Player1", "Team1", "QB", 100, 120, "Bye1", "Auction1", "AuctionPPR1", "IDP1",
                        "Rookie1", 110, 130),
                new AdpPlayer("2", "Player2", "Team2", "WR", 150, 160, "Bye2", "Auction2", "AuctionPPR2", "IDP2",
                        "Rookie2", 140, 150));
        when(getDraftAdp.getFilteredAdpList(anyInt())).thenReturn(adpList);

        // Test parameters
        int numOfTeams = 10;
        String mockTeamName = "MyTeam";
        int draftPosition = 1;
        int numOfRounds = 8;

        // Call the method
        String viewName = draftController.showDraftBoard(numOfTeams, mockTeamName, draftPosition, numOfRounds, model);

        // Verify the returned view name
        assertEquals("draftBoard", viewName);
    }
}
