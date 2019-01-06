package com.cookieclicker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookieclickerContoller {

	@GetMapping("/cookieclicker")
    public String screenDisplay() {
        return "index";
    }
}
