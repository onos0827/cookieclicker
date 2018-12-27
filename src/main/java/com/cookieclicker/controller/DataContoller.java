package com.cookieclicker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DataContoller {

	@GetMapping("/cookieclicker")
    public String screenDisplay() {
        return "index";
    }
}
