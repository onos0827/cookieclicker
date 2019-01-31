$(function(){
	/**
	 * 画面表示情報取得処理
	 */
	function diplay_info() {
			return $.ajax({
				url : '/cookieclicker/display',
				type : 'POST',
				   headers: {
				        "auth" : $.cookie("UI")
				    }
			})
	}

	diplay_info().done(function(result){
		console.log(result);

		//テスト用変数
		var status_code = 0;
		var error_message = "えらー！";
		var result = {
				"auth": "user_id",
				"count_total_cookie": "1500",
				"increase_cookie_onflg":"3500",
				"items": [
				{
					"picture_pass_item": "/image/item1.png",
					"picture_id": "item_icon_1",
					"item_id": "item1",
					"item_name": "SSS",
					"item_cost": "2000",
					"item_flg": "OFF",
					"count_buy_item": "20"
				},
				{
					"picture_pass_item": "/image/item2.png",
					"picture_id": "item_icon_2",
					"item_id": "item2",
					"item_name": "BBA",
					"item_cost": "3000",
					"item_flg": "OFF",
					"count_buy_item": "30"
				},
				{
					"picture_pass_item": "/image/item3.png",
					"picture_id": "item_icon_3",
					"item_id": "item3",
					"item_name": "農場",
					"item_cost": "4000",
					"item_flg": "OFF",
					"count_buy_item": "40"
				},
				{
					"picture_pass_item": "/image/item4.png",
					"picture_id": "item_icon_4",
					"item_id": "item4",
					"item_name": "鉱山",
					"item_cost": "5000",
					"item_flg": "OFF",
					"count_buy_item": "50"
				},
				{
					"picture_pass_item": "/image/item5.png",
					"picture_id": "item_icon_5",
					"item_id": "item5",
					"item_name": "工場",
					"item_cost": "6000",
					"item_flg": "OFF",
					"count_buy_item": "60"
				},
				{
					"picture_pass_item": "/image/item6.png",
					"picture_id": "item_icon_6",
					"item_id": "item6",
					"item_name": "銀行",
					"item_cost": "7000",
					"item_flg": "OFF",
					"count_buy_item": "70"
				},
				{
					"picture_pass_item": "/image/item7.png",
					"picture_id": "item_icon_7",
					"item_id": "item7",
					"item_name": "魔女の塔",
					"item_cost": "8000",
					"item_flg": "OFF",
					"count_buy_item": "80"
				},
				{
					"picture_pass_item": "/image/item8.png",
					"picture_id": "item_icon_8",
					"item_id": "item8",
					"item_name": "宇宙人",
					"item_cost": "9000",
					"item_flg": "OFF",
					"count_buy_item": "90"
				}
				]
		};

/*	  	left:700px;
	top:128px; */


		//アイテム部分表示
		var top_val = 75
		var item_icon_top = 64
		$.each(result.items,function(i){
			top_val = top_val+65;
			item_icon_top = item_icon_top+64;
			var icon = $('<div class="item_icon" id="'+result.items[i].picture_id+'">');
			icon.append('<img src="'+ result.items[i].picture_pass_item +'">');
			icon.css({'left': '700px','top':item_icon_top});

			var buy_count = $('<div class="buy_count" id="buy_item_count_'+result.items[i].item_id+'">');
			buy_count.append(result.items[i].count_buy_item);
			buy_count.css({'left': '950px','top':top_val});


			var item_name = $('<div class="item_name" id="item_name_'+result.items[i].item_id+'">');
			item_name.append(result.items[i].item_name);
			item_name.css({'left': '800px','top':top_val});


			var item_flg = $('<div class="item_flg" id="item_flg_'+result.items[i].item_id+'">');
			item_flg.append(result.items[i].item_flg);
			item_flg.css({'left': '900px','top':top_val});

			$('body').append(icon);
			$('body').append(buy_count);
			$('body').append(item_name);
			$('body').append(item_flg);

		});

		//クッキー枚数表示
		$("#total_cookie_count").append(result.count_total_cookie);


		//アイテム機能有効
		interval_item(Number(result.increase_cookie_onflg));

	}).fail(function(status_code, error_message){
		alert(error_message);
	});


	/**
	 * auth関連処理
	 */
	function create_auth() {
		//クッキー取得
		var cookie_info = String($.cookie("UI"));
		var login_id = cookie_info.replace(undefined, "");

		//文字列ランダム生成
		var len = 32;
		var chr = "abcdefghijklmnopqrstuvwxyz0123456789";

		var chr_len = chr.length;
		for (var i = 0; i < len; i++) {
			login_id += chr[Math.floor(Math.random() * chr_len)];
		}

		//クッキー設定
		$.cookie('UI', login_id.slice(0, 32), {
			expires : 30,
			path : '/'
		});
	};
	create_auth();

	/**
	 * カウントアップ処理
	 */
	var count = Number($("#total_cookie_count").text());


	//クリック時処理
	$("#big_cookie").click(function() {
		$("#cookie_count").text(++count);
	});


	//クッキー枚数登録

	setInterval(function() {
		console.log(count);
		$.ajax({
			url : '/cookieclicker/countup',
			type : 'POST',
			headers: {"auth" : $.cookie("UI")},
			data:count,
	        contentType: 'application/json',
	        dataType: "json"
		})
	},10000
);
	/**
	 *アイテム購入処理
	 */

	$(document).on("click",".item_icon",(function() {
		$.ajax({
			url : '/cookieclicker/buy',
			type : 'POST',
			auth : String($.cookie("UI")),
			data : {
				'item_id' : $(this).attr('id')
			}
		})
	}));


	/**
	 * アイテム有効化
	 */

	//アイテム効果有効化処理
	var click_flg = false;
	var flg;
	var id;
	$(document).on("click",".item_flg",(function() {
		 flg = $(this).text();
		 id = $(this).attr('id');
		//ON OFFボタン連打防止
		if(click_flg){
			return false;
		}
		click_flg = true;


		//サーバー側にアイテムIDと有効化フラグを送る
		$.ajax({
			url : '/cookieclicker/enable',
			type : 'POST',
	        contentType: 'application/json',
	        dataType: "json",
			headers: {"auth" : $.cookie("UI")},
			data : {
				'item_id' : id,
				'enabled_flg' : flg
			}
		}).done(function(response){

		var a = "#"+id
		var increment_num = response;
		var decrease_num =-response;


		//ボタン表示を書き換え、オンの場合はレスポンスの値、
		//オフの場合はマイナスの値をinterval_item関数に渡す
		if(flg == 'ON'){
			$(a).text('OFF');
			interval_item(decrease_num);
		}else{
			$(a).text('ON');
			interval_item(increment_num);
		}

		click_flg = false;

	})
	}));

	var total_cnt = 0;
	var set_time;

	//total_cntに格納されている値分、クッキー合計を定期的に加算していく
	function interval_item (interval_cnt){
		clearInterval(set_time);
		total_cnt = total_cnt+interval_cnt;

		//小数点以下の場合はクッキー合計が減らないように0固定
		if(total_cnt<0){
			total_cnt =0;
		}
			set_time =setInterval (function () {
			$("#cookie_count").text(count+=total_cnt);
		},1000);
	}




	/**
	 * 画像スライド
	 */
	$('.slider').slick({
		arrows : false,
		autoplay : true,
		infinite : true,
		autoplaySpeed : 0,
		speed : 20000,
		swipe : false,
		vertical : true,
		cssEase : 'linear',
		pauseOnFocus : false,
		pauseOnHover : false,
		pauseOnDotsHover : false,

	});


	/**
	 * スクロールロック
	 */
	$(function() {
		if ($(".no_scroll").length) {
			$(window).on('wheel', function(e) {
				e.preventDefault();
			});
		} else {
			$(window).off('wheel');
		}
	});
});