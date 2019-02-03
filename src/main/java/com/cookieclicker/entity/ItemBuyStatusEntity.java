package com.cookieclicker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_ITEM_BUY_STATUS")
public class ItemBuyStatusEntity {



	@Id
	@Column(name="AUTH_ID")
	private String authId;

	@Column(name="ITEM_ID")
	private String itemId;

	@Column(name="COUNT_BUY_ITEM")
	private Integer countBuyItem;

	@Column(name="ENABLED_FLG")
	private Integer enabledFlg;


	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getCountBuyItem() {
		return countBuyItem;
	}

	public void setCountBuyItem(Integer countBuyItem) {
		this.countBuyItem = countBuyItem;
	}

	public Integer getEnabledFlg() {
		return enabledFlg;
	}

	public void setEnabledFlg(Integer enabledFlg) {
		this.enabledFlg = enabledFlg;
	}

}