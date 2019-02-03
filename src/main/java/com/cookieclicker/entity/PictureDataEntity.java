package com.cookieclicker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="TBL_PICTURE_DATA")
public class PictureDataEntity {
	@Id
	@Column(name="PICTURE_ID")
	private String pictureId;

	@Column(name="PICTURE_NAME")
	private String pictureName;

	@Column(name="PICTURE_PASS")
	private String picturePass;

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getPicturePass() {
		return picturePass;
	}

	public void setPicturePass(String picturePass) {
		this.picturePass = picturePass;
	}

}
