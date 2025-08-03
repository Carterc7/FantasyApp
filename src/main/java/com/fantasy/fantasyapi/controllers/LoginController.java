package com.fantasy.fantasyapi.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Method to show login
     * 
     * @return
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login.html";
    }

    @GetMapping("/test-password")
    @ResponseBody
    public String testMatch() {
        String raw = "Password!";
        String stored = "$2a$10$mlczSinzIWkfiout8pu0veGhAoHOfoKnc29jkb8gwWLOWn3oyG1Zy";
        boolean matches = passwordEncoder.matches(raw, stored);
        return "Password match? " + matches;
    }

    /**
     * Method to perform user login and add User to the session
     * 
     * @param username
     * @param password
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String authenticateUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        // validate user
        if (userService.authenticateUser(username, password)) {
            // Fetch the user and add it to the session
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = new User();
                user = userOptional.get();
                System.out.print("User logged in: UserID: " + user.getUserID());
                System.out.print(" Username: " + user.getUsername());
                session.setAttribute("authenticatedUser", user);
                return "redirect:/"; // redirect to home
            } else {
                model.addAttribute("errorMessage", "Login failed: Non-unique result found for username.");
                return "error"; // return the error page
            }
        }
        return "error"; // or any page indicating authentication failure
    }

    /**
     * Method to logout the user and remove from the session
     * 
     * @param sessionStatus
     * @return
     */
    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus, HttpSession httpSession) {
        System.out.println("User logged out.");
        // Invalidate the current session to clear all session attributes
        httpSession.invalidate();
        sessionStatus.setComplete(); // clear the session
        return "redirect:/";
    }
}
