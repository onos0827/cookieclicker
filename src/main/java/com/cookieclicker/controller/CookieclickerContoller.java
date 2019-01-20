package com.cookieclicker.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CookieclickerContoller {
	@GetMapping("/cookieclicker")
	public String screenDisplay() {
		return "index";
	}

	@RequestMapping("/cookieclicker/display")
	@ResponseBody
	public List<String> getDisplatData(@RequestHeader("auth") String auth) {

		//テスト用
		List<String> list = new ArrayList<String>();
		list.add("TEST");
		list.add(auth);

		return list;
	}

	@RequestMapping("/cookieclicker/countup")
	@ResponseBody
	public Long updateCookieCount(@RequestHeader("auth") String auth, @RequestBody Long cokkiecount) {

		//テスト用
		Long ck = cokkiecount;


		return ck;
	}
}
