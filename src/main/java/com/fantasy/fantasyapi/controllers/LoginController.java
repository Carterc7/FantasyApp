package com.fantasy.fantasyapi.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login.html";
    }

    @PostMapping("/login")
    public String authenticateUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        if (userService.authenticateUser(username, password)) {
            // Fetch the user and add it to the session
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = new User();
                user = userOptional.get();
                System.out.print("User logged in: UserID: " + user.getUserID());
                System.out.print(" Username: " + user.getUsername());
                session.setAttribute("authenticatedUser", user);
                return "redirect:/setup"; // redirect to draft setup
            } else {
                model.addAttribute("errorMessage", "Login failed: Non-unique result found for username.");
                return "error"; // return the error page
            }
        }
        return "login"; // or any page indicating authentication failure
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        System.out.println("User logged out.");
        sessionStatus.setComplete(); // clear the session
        return "redirect:/login";
    }
}
