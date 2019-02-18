package com.cookieclicker.enumeration;

public enum ResultCode {

	OK("0","success"),

	NG("1","error");

	private String resultCode;

	private String resultMessage;

	private ResultCode(String resultCode, String resultMessage) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}
}
