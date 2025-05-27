package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    UserService userService;

    /**
     * Method to show registration
     * 
     * @param model
     * @return
     */
    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration.html";
    }

    /**
     * Method to process registration form and add user to db
     * 
     * @param model
     * @param user
     * @return
     */
    @PostMapping("/process_register")
    public String processRegister(
            @Valid User user,
            BindingResult result,
            Model model, HttpSession session) {

        if (userService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username is already taken");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "registration.html";
        }

        userService.addUser(user);
        session.setAttribute("authenticatedUser", user);
        return "redirect:/"; // redirect to home
    }
}
