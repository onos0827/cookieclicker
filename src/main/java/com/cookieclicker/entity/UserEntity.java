package com.cookieclicker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="TBL_USER")
public class UserEntity {
	@Id
	@Column(name="AUTH_ID")
	private String authId;

	@Column(name="COUNT_TOTAL_COOKIE")
	@ColumnDefault("0")
	private Integer countTotalCookie;

	@Column(name="TOTAL_PRODUCTION")
	@ColumnDefault("0")
	private Integer totalProduction;

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public Integer getCountTotalCookie() {
		return countTotalCookie;
	}

	public void setCountTotalCookie(Integer countTotalCookie) {
		this.countTotalCookie = countTotalCookie;
	}

	public Integer getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(Integer totalProduction) {
		this.totalProduction = totalProduction;
	}

}
