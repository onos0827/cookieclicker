package com.cookieclicker.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cookieclicker.entity.ItemBuyStatusEntity;
import com.cookieclicker.entity.ItemDataEntity;
import com.cookieclicker.entity.PictureDataEntity;
import com.cookieclicker.entity.UserEntity;
import com.cookieclicker.repository.ItemBuyStatusRepository;
import com.cookieclicker.repository.ItemDataRepository;
import com.cookieclicker.repository.PictureDataRepository;
import com.cookieclicker.repository.UserRepository;

@Service
@Transactional
public class CookieclickerService {


	@Autowired
	UserRepository user;

	@Autowired
	ItemDataRepository itemData;

	@Autowired
	PictureDataRepository pictureData;

	@Autowired
	ItemBuyStatusRepository itemBuyStatus;

	public List<UserEntity> find(String auth) {
		return user.findByAuthId(auth);
	}

	public UserEntity save(UserEntity userEntity) {
		return user.save(userEntity);
	}

	public List<PictureDataEntity> findPicture() {
		return pictureData.findAll();
	}

	public List<ItemDataEntity> findItem() {
		return itemData.findAllByOrderByDisplayOrder();
	}

	public List<ItemDataEntity> findIncreaseCookie(String itemId) {
		return itemData.findByItemId(itemId);
	}

	public List<ItemBuyStatusEntity> findItemBuyStatus(String auth) {
		return itemBuyStatus.findByAuthId(auth);
	}

	public Integer findItemCount(String auth, String itemId) {
		return itemBuyStatus.findCountBuyItem(auth,itemId);
	}

	public ItemBuyStatusEntity saveItemBuyStatus(ItemBuyStatusEntity itemBuyStatusEntity) {
		return itemBuyStatus.save(itemBuyStatusEntity);
	}

	public Integer saveItemBuyStatusForEnabledFlg(String auth, String itemId, String enabledFlg) {
		return itemBuyStatus.updateByEnabledFlg(auth, itemId, enabledFlg);
	}


}
