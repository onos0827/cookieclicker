package com.cookieclicker.controller;

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

		//クッキー枚数と総生産量をDBへ登録
		UpdateCookieCountResponseDto cokkieCount = cookieclickerService.updateCookieCountLogic(auth, request);

		//レスポンスオブジェクトをJSON文字列に変換し、返却
		return ResponseEntity.ok(cokkieCount);
	}

	/*
	 * クッキー総生産量表示API
	 */
	@RequestMapping("/cookieclicker/totaldisplay")
	@ResponseBody
	public ResponseEntity<TotalProductionResponseDto> getTotalProduction(@RequestHeader("auth") String auth) {

		//クッキー総生産量を取得
		TotalProductionResponseDto totalProduction = cookieclickerService.getTotalProduction(auth);

		//レスポンスオブジェクトをJSON文字列に変換し、返却
		return ResponseEntity.ok(totalProduction);

	}

	/*
	 * アイテム購入API
	 */
	@RequestMapping("/cookieclicker/buy")
	@ResponseBody
	public ResponseEntity<BuyItemResponseDto> buyItem(@RequestHeader("auth") String auth,
			@RequestBody ItemIdRequestDto request) {

		//アイテム購入
		BuyItemResponseDto buyItemData = cookieclickerService.buyItem(auth, request);

		//レスポンスオブジェクトをJSON文字列に変換し、返却
		return ResponseEntity.ok(buyItemData);

	}

	/*
	 *アイテム効果有効化API
	 */
	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public ResponseEntity<ItemFlgUpdateResponseDto> itemFlgUpdate(@RequestHeader("auth") String auth,
			@RequestBody ItemFlgUpdateRequestDto request) {

		//アイテム効果有効化
		ItemFlgUpdateResponseDto itemFlgData = cookieclickerService.itemFlgUpdate(auth, request);

		//レスポンスオブジェクトをJSON文字列に変換し、返却
		return ResponseEntity.ok(itemFlgData);
	}

}
