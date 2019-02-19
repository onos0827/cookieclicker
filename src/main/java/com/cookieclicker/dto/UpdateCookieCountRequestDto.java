package com.cookieclicker.dto;

public class UpdateCookieCountRequestDto {


	private String cookieCount;

	public String getCookieCount() {
		return cookieCount;
	}

	public void setCookieCount(String cookieCount) {
		this.cookieCount = cookieCount;
	}

	public String getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(String totalProduction) {
		this.totalProduction = totalProduction;
	}

	private String totalProduction;

}
