package com.cookieclicker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
public class BuyItemResponseDto {
	private String resultCode;

	private String resultMessage;

	private String itemId;

	private String countBuyItem;

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public String getItemId() {
		return itemId;
	}

	public String getCountBuyItem() {
		return countBuyItem;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setCountBuyItem(String countBuyItem) {
		this.countBuyItem = countBuyItem;
	}

}
