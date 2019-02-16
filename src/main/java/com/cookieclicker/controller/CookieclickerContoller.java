package com.cookieclicker.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cookieclicker.entity.ItemBuyStatusEntity;
import com.cookieclicker.entity.ItemDataEntity;
import com.cookieclicker.entity.PictureDataEntity;
import com.cookieclicker.entity.UserEntity;
import com.cookieclicker.service.CookieclickerService;


@Controller
public class CookieclickerContoller {

	@Autowired
	private CookieclickerService cookieclickerService;


	@GetMapping("/cookieclicker")
	public String screenDisplay() {
		return "index.html";
	}

	@RequestMapping("/cookieclicker/display")
	@ResponseBody
	public String getDisplatData(@RequestHeader("auth") String auth)  {


		//レスポンス作成用
		String json = null;
		String responseJson = null;



		//アイテムデータ取得(表示順に取得)
		List<ItemDataEntity> itemData = cookieclickerService.findItem();
		//画像データ取得
		List<PictureDataEntity> pictureData = cookieclickerService.findPicture();


		//ユーザー情報テーブルからauthを検索
		List<UserEntity> userData = cookieclickerService.find(auth);
		if(userData.isEmpty() == true) {

			//新規auth_idををDBへ登録
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
				ItemBuyStatus.setEnabledFlg(0);
				cookieclickerService.saveItemBuyStatus(ItemBuyStatus);
			}

			userData = cookieclickerService.find(auth);
		}

		//購入状況データ取得
		List<ItemBuyStatusEntity> itemBuyStatus = cookieclickerService.findItemBuyStatus(auth);

		//レスポンス作成
		json = "{\"auth\":\""+userData.get(0).getAuthId()+"\",\"count_total_cookie\":\""+userData.get(0).getCountTotalCookie()+"\",";
		json = "{\"auth\":\""+userData.get(0).getAuthId()+"\",\"count_total_cookie\":\""+userData.get(0).getCountTotalCookie()+"\",\"total_production\":\""+userData.get(0).getTotalProduction()+"\",";

		//item項目部分作成用
		String itemList ="[";
		for(int i=0; i<itemData.size(); i++) {
			//item項目部分作成
			itemList = itemList+"{\"picture_id\":\""+itemData.get(i).getPictureId()+"\",";
			itemList = itemList+"\"picture_pass_item\":\""+pictureData.get(i).getPicturePass()+"\",";
			itemList = itemList+"\"item_id\":\""+itemData.get(i).getItemId()+"\",";
			itemList = itemList+"\"item_name\":\""+itemData.get(i).getItemName()+"\",";
			itemList = itemList+"\"item_cost\":\""+itemData.get(i).getItemCost()+"\",";
			itemList = itemList+"\"increase_cookie\":\""+itemData.get(i).getIncreaseCookie()+"\",";
			itemList = itemList+"\"enabled_flg\":\""+itemBuyStatus.get(i).getEnabledFlg()+"\",";
			itemList = itemList+"\"count_buy_item\":\""+itemBuyStatus.get(i).getCountBuyItem()+"\"},";

		};
		//事前に作成したauth項目と連結し、不正なJSON形式にならないよう置換
		json=json+"\"items\":"+itemList+"]}";
		responseJson = json.replace("},]", "}]");


		return responseJson;
	}

	//@RequestMapping("/cookieclicker/countup")
	@ResponseBody
	public Integer updateCookieCount(@RequestHeader("auth") String auth, @RequestBody String request) {

		Pattern cookieReg = Pattern.compile("cookie_count=(.*?)&");
        Matcher cookieRegMatch = cookieReg.matcher(request);

		Pattern totalProductionReg = Pattern.compile("total_production=(.*?)$");
        Matcher totalProductionRegMatch = totalProductionReg.matcher(request);

		//クッキー合計枚数登録
		UserEntity user = new UserEntity();
		user.setAuthId(auth);

		cookieRegMatch.find();
		totalProductionRegMatch.find();
		user.setCountTotalCookie(Integer.parseInt(cookieRegMatch.group(1)));
		user.setTotalProduction(Integer.parseInt(totalProductionRegMatch.group(1)));
		UserEntity result = cookieclickerService.save(user);


		return result.getCountTotalCookie();
	}

	//@RequestMapping("/cookieclicker/totaldisplay")
	@ResponseBody
	public int getTotalProduction(@RequestHeader("auth") String auth) {
		List<UserEntity> userData = cookieclickerService.find(auth);
		Integer totalProduction = userData.get(0).getTotalProduction();

		return totalProduction;
	}


	@RequestMapping("/cookieclicker/buy")
	@ResponseBody
	public String buyItem(@RequestHeader("auth") String auth, @RequestBody String itemId) {

		String itemIdVal = itemId.replace("item_id=picture_", "");

		Integer itemCount = cookieclickerService.findItemCount(auth, itemIdVal);

		//アイテム購入状況更新
		ItemBuyStatusEntity ItemBuyStatus = new ItemBuyStatusEntity();
		ItemBuyStatus.setAuthId(auth);
		ItemBuyStatus.setItemId(itemIdVal);
		ItemBuyStatus.setEnabledFlg(0);
		ItemBuyStatus.setCountBuyItem(itemCount+1);

		ItemBuyStatusEntity result = cookieclickerService.saveItemBuyStatus(ItemBuyStatus);

		String responseJson = "{\"item_id\":\""+result.getItemId()+"\","+"\"count_buy_item\":\""+result.getCountBuyItem()+"\"}";

		return responseJson;
	}


	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public String itemFlgUpdate(@RequestHeader("auth") String auth, @RequestBody String request) {

		Pattern itemIdReg = Pattern.compile("item_id=(.*?)&");
        Matcher itemIdRegMatch = itemIdReg.matcher(request);

		Pattern enabledFlgReg = Pattern.compile("enabled_flg=(.*?)$");
        Matcher enabledFlgRegMatch = enabledFlgReg.matcher(request);

        itemIdRegMatch.find();
        enabledFlgRegMatch.find();
        String itemId = itemIdRegMatch.group(1);
        String enabledFlg = enabledFlgRegMatch.group(1);


        //有効化フラグ更新
        Integer updateCount = cookieclickerService.saveItemBuyStatusForEnabledFlg(auth, itemId, enabledFlg);

        //アイテム増加数取得
        List<ItemDataEntity> increaseCookie = cookieclickerService.findIncreaseCookie(itemId);

        //レスポンス作成
        String responseJson = "{\"increase_cookie\":\""+increaseCookie.get(0).getIncreaseCookie()+"\"}";

		return responseJson;
	}
}
