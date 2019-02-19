package com.cookieclicker.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetDisplatDataResponseDto {
	private String resultCode;

	private String resultMessage;

	private String auth;

	private String countTotalCookie;

	private String totalProduction;

	private List<Map<String,String>> items = new ArrayList<Map<String,String>>();

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getCountTotalCookie() {
		return countTotalCookie;
	}

	public void setCountTotalCookie(String countTotalCookie) {
		this.countTotalCookie = countTotalCookie;
	}

	public String getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(String totalProduction) {
		this.totalProduction = totalProduction;
	}

	public List<Map<String, String>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, String>> items) {
		this.items = items;
	}



}
