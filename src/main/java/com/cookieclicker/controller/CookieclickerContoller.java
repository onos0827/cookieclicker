package com.cookieclicker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.cookieclicker.entity.UserEntity;
import com.cookieclicker.enumeration.ResultCode;
import com.cookieclicker.service.CookieclickerService;

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
	public ResponseEntity<GetDisplatDataResponseDto> getDisplatData(@RequestHeader("auth") String auth) {

		//画面表示情報取得
		GetDisplatDataResponseDto displayData = cookieclickerService.getDisplatDataLogic(auth);

		//レスポンスオブジェクトをJSON文字列に変換し、返却
		return ResponseEntity.ok(displayData);
	}

	/*
	 * クッキーカウントアップAPI
	 */
	@RequestMapping("/cookieclicker/countup")
	@ResponseBody
	public ResponseEntity<UpdateCookieCountResponseDto> updateCookieCount(@RequestHeader("auth") String auth,
			@RequestBody UpdateCookieCountRequestDto request) {

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

			UserEntity result = cookieclickerService.save(user);

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
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			return ResponseEntity.ok(updateCookieCountResponse);
		}
	}

	/*
	 * クッキー総生産量表示API
	 */
	@RequestMapping("/cookieclicker/totaldisplay")
	@ResponseBody
	public ResponseEntity<TotalProductionResponseDto> getTotalProduction(@RequestHeader("auth") String auth) {

		//クッキー総生産量表示APIDTO初期化
		TotalProductionResponseDto totalProductionResponse = new TotalProductionResponseDto();

		try {
			//ユーザー情報テーブルからクッキー総生産量をSELECT
			List<UserEntity> userData = cookieclickerService.find(auth);

			//レスポンス作成
			totalProductionResponse.setResultCode(ResultCode.OK.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.OK.getResultMessage());
			totalProductionResponse.setTotalProduction(userData.get(0).getAuthId());

		} catch (Exception e) {
			//例外発生時はエラーメッセージをレスポンスとして返却
			e.printStackTrace();
			totalProductionResponse = new TotalProductionResponseDto();
			totalProductionResponse.setResultCode(ResultCode.NG.getResultCode());
			totalProductionResponse.setResultMessage(ResultCode.NG.getResultMessage());
		} finally {
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			return ResponseEntity.ok(totalProductionResponse);
		}

	}

	/*
	 * アイテム購入API
	 */
	@RequestMapping("/cookieclicker/buy")
	@ResponseBody
	public ResponseEntity<BuyItemResponseDto> buyItem(@RequestHeader("auth") String auth,
			@RequestBody ItemIdRequestDto request) {

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
			ItemBuyStatus.setCountBuyItem(itemCount + 1);

			ItemBuyStatusEntity result = cookieclickerService.saveItemBuyStatus(ItemBuyStatus);

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
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			return ResponseEntity.ok(buyItemResponse);
		}
	}

	/*
	 *アイテム効果有効化API
	 */
	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public ResponseEntity<ItemFlgUpdateResponseDto> itemFlgUpdate(@RequestHeader("auth") String auth,
			@RequestBody ItemFlgUpdateRequestDto request) {

		//アイテム効果有効化APIレスポンスDTO初期化
		ItemFlgUpdateResponseDto itemFlgUpdateResponse = new ItemFlgUpdateResponseDto();

		try {
			//リクエストからアイテムIDと有効化フラグを取得
			String itemId = request.getItemId();
			String enabledFlg = request.getEnabledFlg();

			//有効化フラグ更新
			Integer updateCount = cookieclickerService.saveItemBuyStatusForEnabledFlg(auth, itemId, enabledFlg);

			//アイテム増加数取得
			List<ItemDataEntity> increaseCookie = cookieclickerService.findIncreaseCookie(itemId);

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
			//レスポンスオブジェクトをJSON文字列に変換し、返却
			return ResponseEntity.ok(itemFlgUpdateResponse);
		}
	}
}
