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
		return "index.html";
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
	public String updateCookieCount(@RequestHeader("auth") String auth, @RequestBody String cokkiecount) {

		//テスト用
		String ck = cokkiecount;

		return ck;
	}


	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public int itemFlgUpdate(@RequestHeader("auth") String auth, @RequestBody String request) {


		//テスト用
		int num = 100;



		return num;
	}
}
