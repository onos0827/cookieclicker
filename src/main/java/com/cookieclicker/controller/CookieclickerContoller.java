package com.cookieclicker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cookieclicker.dto.BuyItemResponseDto;
import com.cookieclicker.dto.ItemFlgUpdateRequestDto;
import com.cookieclicker.dto.ItemFlgUpdateResponseDto;
import com.cookieclicker.dto.ItemIdRequestDto;
import com.cookieclicker.dto.TotalProductionResponseDto;
import com.cookieclicker.dto.getDisplatDataResponseDto;
import com.cookieclicker.dto.updateCookieCountRequestDto;
import com.cookieclicker.dto.updateCookieCountResponseDto;
import com.cookieclicker.entity.ItemBuyStatusEntity;
import com.cookieclicker.entity.ItemDataEntity;
import com.cookieclicker.entity.PictureDataEntity;
import com.cookieclicker.entity.UserEntity;
import com.cookieclicker.enumeration.ResultCode;
import com.cookieclicker.service.CookieclickerService;
import com.cookieclicker.util.JsonUtil;


@Controller
public class CookieclickerContoller {

	@Autowired
	private CookieclickerService cookieclickerService;

	/*
	 * 初期表示
	 */

	@GetMapping("/cookieclicker")
	public String screenDisplay() {
		return "index.html";
	}

	/*
	 * ゲーム画面表示情報取得API
	 */
	@RequestMapping("/cookieclicker/display")
	@ResponseBody
	public String getDisplatData(@RequestHeader("auth") String auth) {


		//レスポンス作成用
		getDisplatDataResponseDto getDisplatDataResponse = new getDisplatDataResponseDto();



		try {
			//アイテムデータ取得(表示順に取得)
			List<ItemDataEntity> itemData = cookieclickerService.findItem();
			//画像データ取得
			List<PictureDataEntity> pictureData = cookieclickerService.findPicture();


			//ユーザー情報テーブルからauthを検索
			List<UserEntity> userData = cookieclickerService.find(auth);
			if(userData.isEmpty() == true) {

				//authをSELECTした結果がNULLだった場合、新規auth_idををDBへ登録
				UserEntity user = new UserEntity();
				user.setAuthId(auth);
				user.setCountTotalCookie(0);
				user.setTotalProduction(0);
				cookieclickerService.save(user);

				//アイテム購入状況初期データ登録
				for(int i=0; i<itemData.size(); i++) {
					ItemBuyStatusEntity ItemBuyStatus = new ItemBuyStatusEntity();
					ItemBuyStatus.setAuthId(auth);
					ItemBuyStatus.setItemId(itemData.get(i).getItemId());
					ItemBuyStatus.setCountBuyItem(0);
					ItemBuyStatus.setEnabledFlg("0");
					cookieclickerService.saveItemBuyStatus(ItemBuyStatus);
				}

				userData = cookieclickerService.find(auth);
			}

			//購入状況データ取得
			List<ItemBuyStatusEntity> itemBuyStatus = cookieclickerService.findItemBuyStatus(auth);

			//レスポンス作成
			getDisplatDataResponse.setResultCode(ResultCode.OK.getResultCode());
			getDisplatDataResponse.setResultMessage(ResultCode.OK.getResultMessage());
			getDisplatDataResponse.setAuth(userData.get(0).getAuthId());
			getDisplatDataResponse.setCountTotalCookie(String.valueOf(userData.get(0).getCountTotalCookie()));
			getDisplatDataResponse.setTotalProduction(String.valueOf(userData.get(0).getTotalProduction()));

			//item項目部分作成用
			for(int i=0; i<itemData.size(); i++) {
				//item項目部分作成
				List<Map<String,String>> items =  new ArrayList<Map<String,String>>();
				Map<String,String> item;
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

				//レスポンスオブジェクトのITEM項目へ作成した値を格納
				getDisplatDataResponse.setItems(items);
			}

		}catch(Exception e){
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			getDisplatDataResponse = new getDisplatDataResponseDto();
			getDisplatDataResponse.setResultCode(ResultCode.NG.getResultCode());
			getDisplatDataResponse.setResultMessage(ResultCode.NG.getResultMessage());
		}finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			String responseJson = JsonUtil.toJsonString(getDisplatDataResponse);
			return responseJson;
		}
	}



	/*
	 * クッキーカウントアップAPI
	 */
	//@RequestMapping("/cookieclicker/countup")
	@ResponseBody
	public String updateCookieCount(@RequestHeader("auth") String auth, @RequestBody updateCookieCountRequestDto request) {

		//クッキーカウントアップAPIレスポンスDTO初期化
		updateCookieCountResponseDto updateCookieCountResponse = new updateCookieCountResponseDto();

		try {

			//クッキー合計枚数登録
			String countTotalCookie = request.getCookieCount();
			String totalProduction = request.getTotalProduction();

			UserEntity user = new UserEntity();
			user.setAuthId(auth);
			user.setCountTotalCookie(Integer.parseInt(countTotalCookie));
			user.setTotalProduction(Integer.parseInt(totalProduction));

			UserEntity result = cookieclickerService.save(user);

			//レスポンス作成
			updateCookieCountResponse.setResultCode(ResultCode.OK.getResultCode());
			updateCookieCountResponse.setResultMessage(ResultCode.OK.getResultMessage());
			updateCookieCountResponse.setCountTotalCookie(String.valueOf(result.getCountTotalCookie()));

		}catch(Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			updateCookieCountResponse = new updateCookieCountResponseDto();
			updateCookieCountResponse.setResultCode(ResultCode.NG.getResultCode());
			updateCookieCountResponse.setResultMessage(ResultCode.NG.getResultMessage());
		}finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			String responseJson = JsonUtil.toJsonString(updateCookieCountResponse);
			return responseJson;
		}
	}

	/*
	 * クッキー総生産量表示API
	 */
	@RequestMapping("/cookieclicker/totaldisplay")
	@ResponseBody
	public String getTotalProduction(@RequestHeader("auth") String auth){

		//クッキー総生産量表示APIDTO初期化
		TotalProductionResponseDto totalProductionResponse = new TotalProductionResponseDto();

		try {
			//ユーザー情報テーブルからクッキー総生産量をSELECT
			List<UserEntity> userData = cookieclickerService.find(auth);

			//レスポンス作成
			totalProductionResponse.setResultCode(ResultCode.OK.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.OK.getResultMessage());
			totalProductionResponse.setTotalProduction(userData.get(0).getAuthId());

		}catch(Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			totalProductionResponse = new TotalProductionResponseDto();
			totalProductionResponse.setResultCode(ResultCode.NG.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.NG.getResultMessage());
		}finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			String responseJson = JsonUtil.toJsonString(totalProductionResponse);
			return responseJson;
		}


	}


	/*
	 * アイテム購入API
	 */
	@RequestMapping("/cookieclicker/buy")
	@ResponseBody
	public String buyItem(@RequestHeader("auth") String auth, @RequestBody ItemIdRequestDto request) {


		//アイテム購入APIレスポンスDTO初期化
		BuyItemResponseDto buyItemResponse = new BuyItemResponseDto();

		try {

			//リクエストからアイテムIDを取得
			String itemId = request.getItemId();

			Integer itemCount = cookieclickerService.findItemCount(auth, itemId);

			//アイテム購入状況更新
			ItemBuyStatusEntity ItemBuyStatus = new ItemBuyStatusEntity();
			ItemBuyStatus.setAuthId(auth);
			ItemBuyStatus.setItemId(itemId);
			ItemBuyStatus.setEnabledFlg("0");
			ItemBuyStatus.setCountBuyItem(itemCount+1);

			ItemBuyStatusEntity result = cookieclickerService.saveItemBuyStatus(ItemBuyStatus);

			//レスポンス作成
			buyItemResponse.setResultCode(ResultCode.OK.getResultCode());
			buyItemResponse.setResultMessage(ResultCode.OK.getResultMessage());
			buyItemResponse.setItemId(result.getItemId());
			buyItemResponse.setCountBuyItem(String.valueOf(result.getCountBuyItem()));

		}catch(Exception e){
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			buyItemResponse = new BuyItemResponseDto();
			buyItemResponse.setResultCode(ResultCode.NG.getResultCode());
			buyItemResponse.setResultMessage(ResultCode.NG.getResultMessage());

		}finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			String responseJson = JsonUtil.toJsonString(buyItemResponse);
			return responseJson;
		}
	}


	/*
	 *アイテム効果有効化API
	 */
	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public String itemFlgUpdate(@RequestHeader("auth") String auth, @RequestBody ItemFlgUpdateRequestDto request) {

		//アイテム効果有効化APIレスポンスDTO初期化
		ItemFlgUpdateResponseDto itemFlgUpdateResponse = new ItemFlgUpdateResponseDto();

		try {
			//リクエストからアイテムIDと有効化フラグを取得
			String itemId = request.getItemId();
			String enabledFlg= request.getEnabledFlg();

			//有効化フラグ更新
			Integer updateCount = cookieclickerService.saveItemBuyStatusForEnabledFlg(auth, itemId, enabledFlg);

			//アイテム増加数取得
			List<ItemDataEntity> increaseCookie = cookieclickerService.findIncreaseCookie(itemId);

			//レスポンス作成
			itemFlgUpdateResponse.setResultCode(ResultCode.OK.getResultCode());
			itemFlgUpdateResponse.setResultMessage(ResultCode.OK.getResultMessage());
			itemFlgUpdateResponse.setIncreaseCookie(String.valueOf(increaseCookie.get(0).getIncreaseCookie()));

		}catch(Exception e){
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			itemFlgUpdateResponse = new ItemFlgUpdateResponseDto();
			itemFlgUpdateResponse.setResultCode(ResultCode.NG.getResultCode());
			itemFlgUpdateResponse.setResultMessage(ResultCode.NG.getResultMessage());
		}finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			String responseJson = JsonUtil.toJsonString(itemFlgUpdateResponse);
			return responseJson;
		}
	}
}

