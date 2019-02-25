$(function(){
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

	diplay_info().done(function(responseJson){


		var result = responseJson;

		//アイテム部分表示
		var top_val = 75
		var item_icon_top = 64
		var increase_cookie_onflg = 0
		$.each(result.items,function(i){
			top_val = top_val+65;
			item_icon_top = item_icon_top+64;
			var icon = $('<div class="item_icon" data-toggle="tooltip" data-original-title="購入価格：'+result.items[i].itemCost+' 枚" id="picture_'+result.items[i].itemId+'">');
			icon.append('<img src="'+ result.items[i].picturePassItem +'">');
			icon.css({'left': '700px','top':item_icon_top});

			var buy_count = $('<div class="buy_count" id="buy_item_count_'+result.items[i].itemId+'">');
			buy_count.append(result.items[i].countBuyItem);
			buy_count.css({'left': '950px','top':top_val});


			var item_name = $('<div class="item_name" id="item_name_'+result.items[i].itemId+'">');
			item_name.append(result.items[i].itemName);
			item_name.css({'left': '800px','top':top_val});


			var item_cost = $('<div class="item_cost" id="item_cost_'+result.items[i].itemId+'">');
			item_cost.append(result.items[i].itemCost);


			var item_flg = $('<div class="item_flg" id="item_flg_'+result.items[i].itemId+'">');
			item_flg.append(result.items[i].enabledFlg);
			item_flg.css({'left': '900px','top':top_val});
			if(result.items[i].enabledFlg == 1){
				item_flg.text("ON");
				increase_cookie_onflg = increase_cookie_onflg+Number(result.items[i].increaseCookie)*Number(result.items[i].countBuyItem);
				console.log(increase_cookie_onflg);
			}else{
				item_flg.text("OFF");
				increase_cookie_onflg = increase_cookie_onflg+0;
			}

			$('body').append(icon);
			$('body').append(buy_count);
			$('body').append(item_name);
			$('body').append(item_flg);
			$('body').append(item_cost);
			$('.item_cost').hide();

		});

		//アイテム所持数が0のものは有効化ボタンを非活性化
		$.each(result.items,function(i){
			if(result.items[i].countBuyItem == 0){
				$("#item_flg_"+result.items[i].itemId).prop( 'disabled', true )
			}});

		//クッキー枚数表示
		$("#cookie_count").append(result.countTotalCookie);
		$("#total_cookie_count").append(result.totalProduction);
		$('#total_cookie_count').hide();


		//アイテム機能有効
		interval_item(Number(increase_cookie_onflg));

	}).fail(function(status_code, error_message){
		alert("画面の表示に失敗しました");
	});



	/**
	 * カウントアップ処理
	 */

	//クリック時処理
	$("#big_cookie").click(function() {
		var count = Number($("#cookie_count").text());
		var total_production = Number($("#total_cookie_count").text());
		$("#cookie_count").text(++count);
		$("#total_cookie_count").text(++total_production);
		console.log($("#total_cookie_count").text())
	});


	//クッキー枚数登録

	setInterval(function() {
		var jsondata = {"cookieCount":$("#cookie_count").text(),"totalProduction":$('#total_cookie_count').text()};
		console.log($("#cookie_count").text());
		$.ajax({
			url : '/cookieclicker/countup',
			type : 'POST',
			contentType: 'application/json',
			dataType: "json",
			headers: {"auth" : $.cookie("UI")},
			data : JSON.stringify(jsondata)
		})
	},10000
	);


	/**
	 *アイテム購入処理
	 */

	$(document).on("click",".item_icon",(function() {
		var item_id = $(this).attr('id')
		var item_cost_id = item_id.replace("picture","item_cost")
		var item_cost = $("#"+item_cost_id).text();
		var jsondata = {"itemId":item_id.replace("picture_", "")};

		var exchange = Number($("#cookie_count").text()) - Number(item_cost)
		if(exchange >=0){
			count = exchange;
			$("#cookie_count").text(exchange);

			$.ajax({
				url : '/cookieclicker/buy',
				type : 'POST',
				contentType: 'application/json',
				dataType: "json",
				headers: {"auth" : $.cookie("UI")},
				data : JSON.stringify(jsondata)
			}).done(function(response){
				if(response.resultCode ==0){
					var count_buy_item = response.countBuyItem;
					var count_buy_id = "buy_item_count_"+response.itemId;
					var item_flg_id = "item_flg_"+response.itemId;

					//初回購入時は非活性になっている有効化ボタンを活性化させる
					if(count_buy_item == 1){
						$("#"+item_flg_id).prop( 'disabled', false )
					}
					$("#"+count_buy_id).text(count_buy_item);
				}else{
					alert("アイテム購入処理に失敗しました");
				}
			})

		}else{
			alert("クッキーが不足しているため、購入できません");
		}

	}));



	/**
	 * アイテム有効化
	 */

	var click_flg = false;
	var flg;
	var id;
	$(document).on("click",".item_flg",(function() {
		flg = $(this).text();
		id = $(this).attr('id');
		var buy_item_count_id = id.replace("item_flg","buy_item_count");
		var a = "#"+id
		var count_buy_id = id.replace("item_flg","buy_item_count")
		var count_buy_item = Number($("#"+count_buy_id).text());



		//ON OFFボタン連打防止
		if(click_flg){
			return false;
		}
		click_flg = true;

		if(flg == "OFF"){
			flg = 1
		} else{
			flg = 0
		}


		var jsondata = {"itemId":id.replace("item_flg_",""),"enabledFlg":flg};

		//サーバー側にアイテムIDと有効化フラグを送る
		$.ajax({
			url : '/cookieclicker/enable',
			type : 'POST',
			contentType: 'application/json',
			dataType: "json",
			headers: {"auth" : $.cookie("UI")},
			data : JSON.stringify(jsondata)
		}).done(function(response){
			if(response.resultCode ==0){
				var increment_num = response.increaseCookie*count_buy_item;
				var decrease_num =-response.increaseCookie*count_buy_item;
				console.log(response.increase_cookie)
				console.log(response.increase_cookie)


				//ボタン表示を書き換え、オンの場合はレスポンスの値、
				//オフの場合はマイナスの値をinterval_item関数に渡す
				if(flg == 0){
					$(a).text('OFF');
					interval_item(decrease_num);
				}else{
					$(a).text('ON');
					interval_item(increment_num);
				}

				click_flg = false;

			}else{
				alert("アイテム有効化処理に失敗しました。");
			}
		})
	}));

	var total_cnt = 0;
	var set_time;

	//total_cntに格納されている値分、クッキー合計を定期的に加算していく
	function interval_item (interval_cnt){
		clearInterval(set_time);
		total_cnt = total_cnt+interval_cnt;

		set_time =setInterval (function () {
			var count = Number($("#cookie_count").text());
			var total_production = Number($("#total_cookie_count").text());
			$("#cookie_count").text(count+=total_cnt);
			$("#total_cookie_count").text(total_production+=total_cnt);
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
	 * モーダル表示
	 */

	$(document).on("click","#modal_button",(function() {
		$.ajax({
			url : '/cookieclicker/totaldisplay',
			type : 'POST',
			headers: {"auth" : $.cookie("UI")},
		}).done(function(response){
			$("#modal_body_text").text("これまで生産したクッキーの数は  "+response.totalProduction+" 枚です。")
		})

	}
	));

	/**
	 * ツールチップ有効化
	 */

	$(function () {
		$('[data-toggle="tooltip"]').tooltip();
	})

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