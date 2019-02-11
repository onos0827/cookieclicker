package com.cookieclicker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemBuyStatusPrimaryKey implements Serializable {



	@Column(name="AUTH_ID")
	private String authId;


	@Column(name="ITEM_ID")
	private String itemId;

}
