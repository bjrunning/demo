package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/privacy_policy")
    public String privacyPolicy() {
        return "other/privacy_policy";
    }

    @GetMapping("/rules")
    public String rules() {
        return "other/rules";
    }
}
