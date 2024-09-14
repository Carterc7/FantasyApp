package com.fantasy.fantasyapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.mongoServices.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController 
{
    @Autowired
    UserService userService;

    /**
     * Method to show registration
     * @param model
     * @return
     */
    @GetMapping("/")
    public String showRegistrationForm(Model model) 
    {
        model.addAttribute("user", new User());
        return "registration.html";
    }

    /**
     * Method to process registration form and add user to db
     * @param model
     * @param user
     * @return
     */
    @PostMapping("/process_register")
    public String processRegister(Model model, User user) 
    {  
        model.addAttribute("user", user);
        userService.addUser(user);
        return "registrationSuccess.html";
    }
}
