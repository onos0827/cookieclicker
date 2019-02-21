package com.cookieclicker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cookieclicker.dto.BuyItemResponseDto;
import com.cookieclicker.dto.GetDisplatDataResponseDto;
import com.cookieclicker.dto.ItemFlgUpdateRequestDto;
import com.cookieclicker.dto.ItemFlgUpdateResponseDto;
import com.cookieclicker.dto.ItemIdRequestDto;
import com.cookieclicker.dto.TotalProductionResponseDto;
import com.cookieclicker.dto.UpdateCookieCountRequestDto;
import com.cookieclicker.dto.UpdateCookieCountResponseDto;
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
	UserRepository userData;

	@Autowired
	ItemDataRepository itemData;

	@Autowired
	PictureDataRepository pictureData;

	@Autowired
	ItemBuyStatusRepository itemBuyStatus;

	/*
	 * ゲーム画面表示情報取得ロジック
	 */
	public GetDisplatDataResponseDto getDisplatDataLogic(String auth) {
		GetDisplatDataResponseDto getDisplatDataResponse = new GetDisplatDataResponseDto();

		try {
			//アイテムデータ取得(表示順に取得)
			List<ItemDataEntity> itemDataList = itemData.findAllByOrderByDisplayOrder();
			//画像データ取得
			List<PictureDataEntity> pictureDataList = pictureData.findAll();

			//ユーザー情報テーブルからauthを検索
			List<UserEntity> userDataList = userData.findByAuthId(auth);
			if (userDataList.isEmpty() == true) {

				//authをSELECTした結果がNULLだった場合、新規auth_idををDBへ登録
				UserEntity user = new UserEntity();
				user.setAuthId(auth);
				user.setCountTotalCookie(0);
				user.setTotalProduction(0);
				userData.save(user);

				//アイテム購入状況初期データ登録
				for (int i = 0; i < itemDataList.size(); i++) {
					ItemBuyStatusEntity ItemBuyStatusData = new ItemBuyStatusEntity();
					ItemBuyStatusData.setAuthId(auth);
					ItemBuyStatusData.setItemId(itemDataList.get(i).getItemId());
					ItemBuyStatusData.setCountBuyItem(0);
					ItemBuyStatusData.setEnabledFlg("0");
					itemBuyStatus.save(ItemBuyStatusData);
				}

				userDataList = userData.findByAuthId(auth);
			}

			//購入状況データ取得
			List<ItemBuyStatusEntity> itemBuyStatusDataList = itemBuyStatus.findByAuthId(auth);

			//レスポンス作成
			getDisplatDataResponse.setResultCode(ResultCode.OK.getResultCode());
			getDisplatDataResponse.setResultMessage(ResultCode.OK.getResultMessage());
			getDisplatDataResponse.setAuth(userDataList.get(0).getAuthId());
			getDisplatDataResponse.setCountTotalCookie(String.valueOf(userDataList.get(0).getCountTotalCookie()));
			getDisplatDataResponse.setTotalProduction(String.valueOf(userDataList.get(0).getTotalProduction()));

			//item項目部分作成用
			List<Map<String, String>> items = new ArrayList<Map<String, String>>();
			Map<String, String> item;
			for (int i = 0; i < itemDataList.size(); i++) {
				//item項目部分作成
				item = new HashMap<String, String>();
				item.put("pictureId", itemDataList.get(i).getPictureId());
				item.put("picturePassItem", pictureDataList.get(i).getPicturePass());
				item.put("itemId", itemDataList.get(i).getItemId());
				item.put("itemName", itemDataList.get(i).getItemName());
				item.put("itemCost", String.valueOf(itemDataList.get(i).getItemCost()));
				item.put("increaseCookie", String.valueOf(itemDataList.get(i).getIncreaseCookie()));
				item.put("enabledFlg", String.valueOf(itemBuyStatusDataList.get(i).getEnabledFlg()));
				item.put("countBuyItem", String.valueOf(itemBuyStatusDataList.get(i).getCountBuyItem()));
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

	/*
	 * クッキーカウントアップロジック
	 */
	public UpdateCookieCountResponseDto updateCookieCountLogic(String auth,
			UpdateCookieCountRequestDto request) {

		//クッキーカウントアップAPIレスポンスDTO初期化
		UpdateCookieCountResponseDto updateCookieCountResponse = new UpdateCookieCountResponseDto();

		try {

			//クッキー合計枚数登録
			String countTotalCookie = request.getCookieCount();
			String totalProduction = request.getTotalProduction();

			UserEntity user = new UserEntity();
			user.setAuthId(auth);
			user.setCountTotalCookie(Integer.parseInt(countTotalCookie));
			user.setTotalProduction(Integer.parseInt(totalProduction));

			UserEntity result = userData.save(user);

			//レスポンス作成
			updateCookieCountResponse.setResultCode(ResultCode.OK.getResultCode());
			updateCookieCountResponse.setResultMessage(ResultCode.OK.getResultMessage());
			updateCookieCountResponse.setCountTotalCookie(String.valueOf(result.getCountTotalCookie()));

		} catch (Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			updateCookieCountResponse = new UpdateCookieCountResponseDto();
			updateCookieCountResponse.setResultCode(ResultCode.NG.getResultCode());
			updateCookieCountResponse.setResultMessage(ResultCode.NG.getResultMessage());
		} finally {
			return updateCookieCountResponse;
		}
	}

	/*
	 * クッキー総生産量表示ロジック
	 */
	public TotalProductionResponseDto getTotalProduction(@RequestHeader("auth") String auth) {

		//クッキー総生産量表示APIDTO初期化
		TotalProductionResponseDto totalProductionResponse = new TotalProductionResponseDto();

		try {
			//ユーザー情報テーブルからクッキー総生産量をSELECT
			List<UserEntity> userDataList = userData.findByAuthId(auth);

			//レスポンス作成
			totalProductionResponse.setResultCode(ResultCode.OK.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.OK.getResultMessage());
			totalProductionResponse.setTotalProduction(String.valueOf(userDataList.get(0).getTotalProduction()));

		} catch (Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			totalProductionResponse = new TotalProductionResponseDto();
			totalProductionResponse.setResultCode(ResultCode.NG.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.NG.getResultMessage());
		} finally {
			return totalProductionResponse;
		}
	}

	/*
	 * アイテム購入ロジック
	 */
	public BuyItemResponseDto buyItem(@RequestHeader("auth") String auth,
			@RequestBody ItemIdRequestDto request) {

		//アイテム購入APIレスポンスDTO初期化
		BuyItemResponseDto buyItemResponse = new BuyItemResponseDto();

		try {

			//リクエストからアイテムIDを取得
			String itemId = request.getItemId();

			Integer itemCount = itemBuyStatus.findCountBuyItem(auth, itemId);

			//アイテム購入状況更新
			ItemBuyStatusEntity ItemBuyStatus = new ItemBuyStatusEntity();
			ItemBuyStatus.setAuthId(auth);
			ItemBuyStatus.setItemId(itemId);
			ItemBuyStatus.setEnabledFlg("0");
			ItemBuyStatus.setCountBuyItem(itemCount + 1);

			ItemBuyStatusEntity result = itemBuyStatus.save(ItemBuyStatus);

			//レスポンス作成
			buyItemResponse.setResultCode(ResultCode.OK.getResultCode());
			buyItemResponse.setResultMessage(ResultCode.OK.getResultMessage());
			buyItemResponse.setItemId(result.getItemId());
			buyItemResponse.setCountBuyItem(String.valueOf(result.getCountBuyItem()));

		} catch (Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			buyItemResponse = new BuyItemResponseDto();
			buyItemResponse.setResultCode(ResultCode.NG.getResultCode());
			buyItemResponse.setResultMessage(ResultCode.NG.getResultMessage());

		} finally {
			return buyItemResponse;
		}
	}

	/*
	 * アイテム効果有効化ロジック
	 */
	public ItemFlgUpdateResponseDto itemFlgUpdate(@RequestHeader("auth") String auth,
			@RequestBody ItemFlgUpdateRequestDto request) {

		//アイテム効果有効化APIレスポンスDTO初期化
		ItemFlgUpdateResponseDto itemFlgUpdateResponse = new ItemFlgUpdateResponseDto();

		try {
			//リクエストからアイテムIDと有効化フラグを取得
			String itemId = request.getItemId();
			String enabledFlg = request.getEnabledFlg();

			//有効化フラグ更新
			Integer updateCount = itemBuyStatus.updateByEnabledFlg(auth, itemId, enabledFlg);

			//アイテム増加数取得
			List<ItemDataEntity> increaseCookie = itemData.findByItemId(itemId);

			//レスポンス作成
			itemFlgUpdateResponse.setResultCode(ResultCode.OK.getResultCode());
			itemFlgUpdateResponse.setResultMessage(ResultCode.OK.getResultMessage());
			itemFlgUpdateResponse.setIncreaseCookie(String.valueOf(increaseCookie.get(0).getIncreaseCookie()));

		} catch (Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			itemFlgUpdateResponse = new ItemFlgUpdateResponseDto();
			itemFlgUpdateResponse.setResultCode(ResultCode.NG.getResultCode());
			itemFlgUpdateResponse.setResultMessage(ResultCode.NG.getResultMessage());
		} finally {
			return itemFlgUpdateResponse;
		}
	}

}
