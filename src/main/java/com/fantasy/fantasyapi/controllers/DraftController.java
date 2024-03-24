package com.fantasy.fantasyapi.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fantasy.fantasyapi.apiCalls.GetDraftAdp;
import com.fantasy.fantasyapi.objectModels.AdpPlayer;

import java.util.List;

@Controller
public class DraftController 
{
    /**
     * Method to show draft board of adp players that users can select from.
     * @param model
     * @return
     */
    @PostMapping("/draft")
    public String showDraftBoard(Model model) 
    {
        GetDraftAdp getDraftAdp = new GetDraftAdp();
        List<AdpPlayer> draftBoard = getDraftAdp.getFilteredAdpList(100);
        model.addAttribute("draftBoard", draftBoard);
        return "draftBoard.html";
    }

    /**
     * Method to show setup page where user selects number of teams, team name, and format.
     * @return
     */
    @GetMapping("/setup")
    public String showSetupPage() 
    {
        return "setup.html";
    }

    @PostMapping("/draftPlayer")
    public String draftPlayer(@RequestParam String playerId, @RequestParam List<AdpPlayer> playerList) 
    {
        return "redirect:/";
    }
}

