package edu.example.dev_2_cc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainViewController {

    @GetMapping("/app")
    public String homePage(){
        return "index";
    }

    @GetMapping("/app/mypage")
    public String myPage(){
        return "my-page";
    }

    @GetMapping("/app/admin")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/app/admin/product")
    public String adminProductPage() {
        return "admin_product";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
