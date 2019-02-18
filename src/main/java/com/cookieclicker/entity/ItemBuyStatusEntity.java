package com.cookieclicker.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

@Entity
@Table(name="TBL_ITEM_BUY_STATUS")
@IdClass(value=ItemBuyStatusPrimaryKey.class)
public class ItemBuyStatusEntity {



	@EmbeddedId
	@Id
	@Column(name="AUTH_ID")
	private String authId;


	@EmbeddedId
	@Id
	@Column(name="ITEM_ID")
	private String itemId;

	@Column(name="COUNT_BUY_ITEM")
	private Integer countBuyItem;

	@Column(name="ENABLED_FLG")
	private String enabledFlg;


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

	public String getEnabledFlg() {
		return enabledFlg;
	}

	public void setEnabledFlg(String enabledFlg) {
		this.enabledFlg = enabledFlg;
	}


}
