package com.cookieclicker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_ITEM_DATA")
public class ItemDataEntity {
	@Id
	@Column(name="ITEM_ID")
	private String itemId;

	@Column(name="ITEM_NAME")
	private String itemName;

	@Column(name="ITEM_COST")
	private Integer itemCost;

	@Column(name="INCREASE_COOKIE")
	private Integer increaseCookie;

	@Column(name="PICTURE_ID")
	private String pictureId;

	@Column(name="DISPLAY_ORDER")
	private Integer displayOrder;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCost() {
		return itemCost;
	}

	public void setItemCost(Integer itemCost) {
		this.itemCost = itemCost;
	}

	public Integer getIncreaseCookie() {
		return increaseCookie;
	}

	public void setIncreaseCookie(Integer increaseCookie) {
		this.increaseCookie = increaseCookie;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
