package org.exam.java.exam.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
public class MainController {

    @GetMapping("/")
    public String homePage(Authentication auth, Model model) {
        model.addAttribute("user", auth.getName());
        return "/main/homepage";
    }

    @GetMapping("/login")
    public String login() {

        return "/main/login";
    }

}
