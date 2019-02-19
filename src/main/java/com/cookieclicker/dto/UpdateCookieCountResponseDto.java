package com.cookieclicker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
public class UpdateCookieCountResponseDto {
	private String resultCode;

	private String resultMessage;

	private String countTotalCookie;

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

	public String getCountTotalCookie() {
		return countTotalCookie;
	}

	public void setCountTotalCookie(String countTotalCookie) {
		this.countTotalCookie = countTotalCookie;
	}

}
