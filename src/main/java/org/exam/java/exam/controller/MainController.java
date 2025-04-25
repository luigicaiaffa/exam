package org.exam.java.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class MainController {
    
    @GetMapping("home")
    public String homePage() {
        
        return "/main/homepage";
    }

}
