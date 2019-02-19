package com.cookieclicker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cookieclicker.dto.GetDisplatDataResponseDto;
import com.cookieclicker.entity.ItemBuyStatusEntity;
import com.cookieclicker.entity.ItemDataEntity;
import com.cookieclicker.entity.PictureDataEntity;
import com.cookieclicker.entity.UserEntity;
import com.cookieclicker.enumeration.ResultCode;
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



	/*
	 * DBアクセス処理部分
	 */
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



	/*
	 * ゲーム画面表示情報取得APIロジック
	 */
	//レスポンス作成用
	public GetDisplatDataResponseDto getDisplatDataLogic(String auth) {
	GetDisplatDataResponseDto getDisplatDataResponse = new GetDisplatDataResponseDto();

	try {
		//アイテムデータ取得(表示順に取得)
		List<ItemDataEntity> itemData = findItem();
		//画像データ取得
		List<PictureDataEntity> pictureData = findPicture();

		//ユーザー情報テーブルからauthを検索
		List<UserEntity> userData = find(auth);
		if (userData.isEmpty() == true) {

			//authをSELECTした結果がNULLだった場合、新規auth_idををDBへ登録
			UserEntity user = new UserEntity();
			user.setAuthId(auth);
			user.setCountTotalCookie(0);
			user.setTotalProduction(0);
			save(user);

			//アイテム購入状況初期データ登録
			for (int i = 0; i < itemData.size(); i++) {
				ItemBuyStatusEntity ItemBuyStatus = new ItemBuyStatusEntity();
				ItemBuyStatus.setAuthId(auth);
				ItemBuyStatus.setItemId(itemData.get(i).getItemId());
				ItemBuyStatus.setCountBuyItem(0);
				ItemBuyStatus.setEnabledFlg("0");
				saveItemBuyStatus(ItemBuyStatus);
			}

			userData = find(auth);
		}

		//購入状況データ取得
		List<ItemBuyStatusEntity> itemBuyStatus = findItemBuyStatus(auth);

		//レスポンス作成
		getDisplatDataResponse.setResultCode(ResultCode.OK.getResultCode());
		getDisplatDataResponse.setResultMessage(ResultCode.OK.getResultMessage());
		getDisplatDataResponse.setAuth(userData.get(0).getAuthId());
		getDisplatDataResponse.setCountTotalCookie(String.valueOf(userData.get(0).getCountTotalCookie()));
		getDisplatDataResponse.setTotalProduction(String.valueOf(userData.get(0).getTotalProduction()));

		//item項目部分作成用
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		Map<String, String> item;
		for (int i = 0; i < itemData.size(); i++) {
			//item項目部分作成
			item = new HashMap<String, String>();
			item.put("pictureId", itemData.get(i).getPictureId());
			item.put("picturePassItem", pictureData.get(i).getPicturePass());
			item.put("itemId", itemData.get(i).getItemId());
			item.put("itemName", itemData.get(i).getItemName());
			item.put("itemCost", String.valueOf(itemData.get(i).getItemCost()));
			item.put("increaseCookie", String.valueOf(itemData.get(i).getIncreaseCookie()));
			item.put("enabledFlg", String.valueOf(itemBuyStatus.get(i).getEnabledFlg()));
			item.put("countBuyItem", String.valueOf(itemBuyStatus.get(i).getCountBuyItem()));
			items.add(item);
		}
		//レスポンスオブジェクトのITEM項目へ作成した値を格納
		getDisplatDataResponse.setItems(items);

	} catch (Exception e) {
		//例外発生時はエラーメッセージをレスポンスとして返却
		e.printStackTrace();
		getDisplatDataResponse = new GetDisplatDataResponseDto();
		getDisplatDataResponse.setResultCode(ResultCode.NG.getResultCode());
		getDisplatDataResponse.setResultMessage(ResultCode.NG.getResultMessage());
	} finally {
		return getDisplatDataResponse;
	}


	}
}
