package com.fantasy.fantasyapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fantasy.fantasyapi.leagueModels.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String showHomePage(HttpSession session, Model model) {
        // Retrieve the authenticated user from the session
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            System.out.println("User not found in session");
            return "home-default.html";
        }
        // Add the user to the model
        model.addAttribute("authenticatedUser", authenticatedUser);
        return "home.html";
    }
}
