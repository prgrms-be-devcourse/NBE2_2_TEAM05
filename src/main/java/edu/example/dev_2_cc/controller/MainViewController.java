package edu.example.dev_2_cc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class MainViewController {

    @GetMapping
    public String homePage(){
        return "index";
    }
}
