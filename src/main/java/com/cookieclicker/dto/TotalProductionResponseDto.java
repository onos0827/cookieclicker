package com.cookieclicker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
public class TotalProductionResponseDto {

	private String resultCode;

	private String resultMessage;

	private String totalProduction;

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

	public String getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(String totalProduction) {
		this.totalProduction = totalProduction;
	}

}
