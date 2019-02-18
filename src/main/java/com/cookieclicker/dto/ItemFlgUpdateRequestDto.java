package com.cookieclicker.dto;

public class ItemFlgUpdateRequestDto{


	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getEnabledFlg() {
		return enabledFlg;
	}

	public void setEnabledFlg(String enabledFlg) {
		this.enabledFlg = enabledFlg;
	}

	private String itemId;

	private String enabledFlg;
}
