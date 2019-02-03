package com.cookieclicker.controller;

import java.util.List;

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
		UserEntity user = new UserEntity();
		user.setAuthId(auth);

		//レスポンス作成用
		String json = null;
		String responseJson = null;



		//ユーザー情報テーブルからauthを検索
		List<UserEntity> userData = cookieclickerService.find(auth);



		//新規auth_idををDBへ登録
		user.setCountTotalCookie(0);
		cookieclickerService.save(user);

		//画像データ取得
		List<PictureDataEntity> pictureData = cookieclickerService.findPicture();
		//アイテムデータ取得(表示順に取得)
		List<ItemDataEntity> itemData = cookieclickerService.findItem();
		//購入状況データ取得
		List<ItemBuyStatusEntity> itemBuyStatus = cookieclickerService.findItemBuyStatus(auth);



		//レスポンス作成
		json = "{\"auth\":\""+userData.get(0).getAuthId()+"\",\"count_total_cookie\":\""+userData.get(0).getCountTotalCookie()+"\",";

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

			//アイテム購入状況テーブルが未登録だった場合、フラグ=OFF, 購入数=0を返す
			if(itemBuyStatus.size()==0 || itemBuyStatus.get(i).getCountBuyItem().equals(null)) {
				itemList = itemList+"\"enabled_flg\":\"OFF\",";
				itemList = itemList+"\"count_buy_item\":\""+0+"\"},";
			}else {
				itemList = itemList+"\"enabled_flg\":\""+itemBuyStatus.get(i).getEnabledFlg()+"\",";
				itemList = itemList+"\"count_buy_item\":\""+itemBuyStatus.get(i).getCountBuyItem()+"\"},";
			}
		};
		//事前に作成したauth項目と連結し、不正なJSON形式にならないよう置換
		json=json+"\"items\":"+itemList+"]}";
		responseJson = json.replace("},]", "}]");


		return responseJson;
	}

	@RequestMapping("/cookieclicker/countup")
	@ResponseBody
	public Integer updateCookieCount(@RequestHeader("auth") String auth, @RequestBody String cokkiecount) {

		//クッキー合計枚数登録
		UserEntity user = new UserEntity();
		user.setAuthId(auth);
		user.setCountTotalCookie(Integer.parseInt(cokkiecount.replace("cookie_count=","")));
		UserEntity result = cookieclickerService.save(user);


		return result.getCountTotalCookie();
	}


	@RequestMapping("/cookieclicker/enable")
	@ResponseBody
	public int itemFlgUpdate(@RequestHeader("auth") String auth, @RequestBody String request) {


		//テスト用
		int num = 100;



		return num;
	}
}
