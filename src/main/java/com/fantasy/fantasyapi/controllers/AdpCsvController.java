package com.fantasy.fantasyapi.controllers;

import com.fantasy.fantasyapi.draft.CsvParser;
import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;
import com.fantasy.fantasyapi.objectModels.AdpPlayerCSV;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdpCsvController {

    private final CsvParser csvParser;

    @Autowired
    UserService userService;

    public AdpCsvController() {
        this.csvParser = new CsvParser(); // Create an instance of CsvParser
    }

    @GetMapping("/adp/list")
    public String showAdpList(
            HttpSession session,
            Model model,
            @RequestParam(name = "position", required = false, defaultValue = "all") String position) {

        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            System.out.println("User not found in session");
        }
        model.addAttribute("authenticatedUser", authenticatedUser);

        // Fetch the full list from CSV
        List<AdpPlayerCSV> players = csvParser.parseCsv();

        // Filter players based on position
        if (!"all".equalsIgnoreCase(position)) {
            players = players.stream()
                    .filter(player -> position.equalsIgnoreCase(player.getPosition()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("players", players);
        model.addAttribute("selectedPosition", position); // Keep track of selected position
        return "players.html";
    }
}


