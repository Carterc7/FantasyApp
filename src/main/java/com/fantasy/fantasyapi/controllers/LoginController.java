package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fantasy.fantasyapi.mongoServices.UserService;

@Controller
public class LoginController 
{
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login.html"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        boolean isAuthenticated = userService.authenticateUser(username, password);
        if (isAuthenticated) 
        {
            return "loginSuccess.html";
        } else 
        {
            model.addAttribute("error", "Invalid username or password");
            return "login.html"; // Return login page with error message
        }
    }
}
