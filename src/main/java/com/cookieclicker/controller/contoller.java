package com.cookieclicker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contoller {

	@GetMapping("/cookieclicker")
    public String GameScreenDisplay() {
        return "index";
    }
}
