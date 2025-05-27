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
            @RequestParam(name = "position", required = false, defaultValue = "all") String position,
            @RequestParam(name = "format", required = false, defaultValue = "ppr") String format,
            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText) {

        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            System.out.println("User not found in session");
        }
        model.addAttribute("authenticatedUser", authenticatedUser);

        String csvToParse;
        switch (format.toLowerCase()) {
            case "standard":
                csvToParse = "no-ppr.csv";
                break;
            case "half-ppr":
                csvToParse = "half-ppr.csv";
                break;
            case "dynasty":
                csvToParse = "dynasty.csv";
                break;
            case "2qb-dynasty":
                csvToParse = "2qb-dynasty.csv";
                break;
            case "ppr":
                csvToParse = "ppr.csv";
                break;
            default:
                csvToParse = "ppr.csv";
                break;
        }
        System.out.println("csv to parse is: " + csvToParse);
        List<AdpPlayerCSV> players = csvParser.parseCsv(csvToParse);

        if (!"all".equalsIgnoreCase(position)) {
            players = players.stream()
                    .filter(player -> position.equalsIgnoreCase(player.getPosition()))
                    .collect(Collectors.toList());
        }

        if (!searchText.isBlank()) {
            String loweredSearch = searchText.toLowerCase();
            players = players.stream()
                    .filter(player -> player.getName().toLowerCase().contains(loweredSearch))
                    .collect(Collectors.toList());
        }

        model.addAttribute("players", players);
        model.addAttribute("selectedPosition", position);
        model.addAttribute("selectedFormat", format);
        model.addAttribute("searchText", searchText);

        return "players.html";
    }

}
