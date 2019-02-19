package com.cookieclicker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
public class ItemFlgUpdateResponseDto{

	private String resultCode;

	private String resultMessage;

	private String increaseCookie;

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

	public String getIncreaseCookie() {
		return increaseCookie;
	}

	public void setIncreaseCookie(String increaseCookie) {
		this.increaseCookie = increaseCookie;
	}





}
