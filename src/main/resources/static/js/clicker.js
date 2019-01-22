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
				"items": [
				{
					"picture_pass_item": "/image/item1.png",
					"picture_id": "item_icon1",
					"item_id": "item1",
					"item_name": "カーソル",
					"item_cost": "2000",
					"item_flg": "OFF",
					"count_buy_item": "20"
				},
				{
					"picture_pass_item": "/image/item2.png",
					"picture_id": "item_icon2",
					"item_id": "item2",
					"item_name": "BBA",
					"item_cost": "3000",
					"item_flg": "OFF",
					"count_buy_item": "30"
				},
				{
					"picture_pass_item": "/image/item3.png",
					"picture_id": "item_icon3",
					"item_id": "item3",
					"item_name": "農場",
					"item_cost": "4000",
					"item_flg": "OFF",
					"count_buy_item": "40"
				},
				{
					"picture_pass_item": "/image/item4.png",
					"picture_id": "item_icon4",
					"item_id": "item4",
					"item_name": "鉱山",
					"item_cost": "5000",
					"item_flg": "OFF",
					"count_buy_item": "50"
				},
				{
					"picture_pass_item": "/image/item5.png",
					"picture_id": "item_icon5",
					"item_id": "item5",
					"item_name": "工場",
					"item_cost": "6000",
					"item_flg": "OFF",
					"count_buy_item": "60"
				},
				{
					"picture_pass_item": "/image/item6.png",
					"picture_id": "item_icon6",
					"item_id": "item6",
					"item_name": "銀行",
					"item_cost": "7000",
					"item_flg": "OFF",
					"count_buy_item": "70"
				},
				{
					"picture_pass_item": "/image/item7.png",
					"picture_id": "item_icon7",
					"item_id": "item7",
					"item_name": "魔女の塔",
					"item_cost": "8000",
					"item_flg": "OFF",
					"count_buy_item": "80"
				},
				{
					"picture_pass_item": "/image/item8.png",
					"picture_id": "item_icon8",
					"item_id": "item8",
					"item_name": "宇宙人",
					"item_cost": "9000",
					"item_flg": "OFF",
					"count_buy_item": "90"
				}
				]
		};

		//アイテム部分表示
		$.each(result.items,function(i){
			var icon = $('<div class="item_icon" id="'+result.items[i].picture_id+'">');
			icon.append('<img src="'+ result.items[i].picture_pass_item +'">');

			var buy_count = $('<div id="buy_item_count_'+result.items[i].item_id+'">');
			buy_count.append(result.items[i].count_buy_item);

			var item_name = $('<div id="item_name_'+result.items[i].item_id+'">');

			var item_flg = $('<div class="item_flg" id="item_flg_'+result.items[i].item_id+'">');
			item_flg.append(result.items[i].item_flg);

			$('body').append(icon);
			$('body').append(buy_count);
			$('body').append(item_name);
			$('body').append(item_flg);

		});

		//クッキー枚数表示
		$("#total_cookie_count").append(result.count_total_cookie);

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
	console.log(count);

	//クリック時処理
	$("#big_cookie").click(function() {
		$("#cookie_count").text(++count);
	});


	//クッキー枚数登録
	var json =JSON.stringify(count);
	console.log(json);
	setInterval(function() {
		$.ajax({
			url : '/cookieclicker/countup',
			type : 'POST',
			headers: {"auth" : $.cookie("UI")},
			data:JSON.stringify(count),
	        contentType: 'application/json',
	        dataType: "json"
		})
	},10000
);
	/**
	 *アイテム購入処理
	 */
	$(".item_icon").click(function() {
		$.ajax({
			url : '/cookieclicker/buy',
			type : 'POST',
			auth : String($.cookie("UI")),
			data : {
				'item_id' : $(this).attr('id')
			}
		})
	});

	/**
	 * アイテム有効化
	 */
	var func_obj =[];
	var Interval_obj =[];

	//アイテムごとの処理
	func_obj.item1 = function(){
		Interval_obj.item_flg_1 =setInterval (function () {
			var  Interval_count1 = 1000;
			$("#cookie_count").text(count+=Interval_count1);
		},1000);
	}


	//アイテム効果有効化処理
	var click_flg = false;
	$(".item_flg").click(function() {

		//ON OFFボタン連打防止
		if(click_flg){
			return false;
		}
		click_flg = true;


		var flg = $(this).text();
		var id = $(this).attr('id');
		var func_name = $(this).attr('name');

		//フラグがオンの場合は関数実行 オフの場合はclearIntervalを実行
		if(flg == 'ON'){
			$(this).text('OFF');
			clearInterval(Interval_obj[id]);

		}else{
			$(this).text('ON');
			func_obj[func_name]();
		}

		//サーバー側にアイテムIDと有効化フラグを送る
		$.ajax({
			url : '/cookieclicker/enable',
			type : 'POST',
			auth : String($.cookie("UI")),
			data : {
				'item_id' : $(this).attr('id'),
				'enabled_flg' : flg
			}
		})
		click_flg = false;
	});




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