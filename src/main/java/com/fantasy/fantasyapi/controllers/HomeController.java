package com.fantasy.fantasyapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController 
{
    @GetMapping("/")
    public String showHomePage()
    {
        return "home.html";
    }
    
}
