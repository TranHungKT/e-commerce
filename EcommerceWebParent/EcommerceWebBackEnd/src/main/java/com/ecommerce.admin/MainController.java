package com.ecommerce.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

}
