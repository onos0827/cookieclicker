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

	diplay_info().done(function(responseJson){


		var result = JSON.parse(responseJson);
		console.log(result);

		//アイテム部分表示
		var top_val = 75
		var item_icon_top = 64
		var increase_cookie_onflg = 0
		$.each(result.items,function(i){
			top_val = top_val+65;
			item_icon_top = item_icon_top+64;
			var icon = $('<div class="item_icon" id="picture_'+result.items[i].item_id+'">');
			icon.append('<img src="'+ result.items[i].picture_pass_item +'">');
			icon.css({'left': '700px','top':item_icon_top});

			var buy_count = $('<div class="buy_count" id="buy_item_count_'+result.items[i].item_id+'">');
			buy_count.append(result.items[i].count_buy_item);
			buy_count.css({'left': '950px','top':top_val});


			var item_name = $('<div class="item_name" id="item_name_'+result.items[i].item_id+'">');
			item_name.append(result.items[i].item_name);
			item_name.css({'left': '800px','top':top_val});


			var item_cost = $('<div class="item_cost" id="item_cost_'+result.items[i].item_id+'">');
			item_cost.append(result.items[i].item_cost);


			var item_flg = $('<div class="item_flg" id="item_flg_'+result.items[i].item_id+'">');
			item_flg.append(result.items[i].enabled_flg);
			item_flg.css({'left': '900px','top':top_val});
			if(result.items[i].enabled_flg == 1){
				item_flg.text("ON");
				increase_cookie_onflg = increase_cookie_onflg+Number(result.items[i].increase_cookie)*Number(result.items[i].count_buy_item);
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

		//クッキー枚数表示
		$("#cookie_count").append(result.count_total_cookie);
		$("#total_cookie_count").append(result.total_production);
		$('#total_cookie_count').hide();


		//アイテム機能有効
		interval_item(Number(increase_cookie_onflg));

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
	//var count = Number($("#cookie_count").text());



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
		console.log($("#cookie_count").text());
		$.ajax({
			url : '/cookieclicker/countup',
			type : 'POST',
	        contentType: 'application/json',
	        dataType: "json",
			headers: {"auth" : $.cookie("UI")},
			data : {
				'cookie_count' : $("#cookie_count").text(),
				'total_production' : $('#total_cookie_count').text()
			}
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

		var exchange = Number($("#cookie_count").text()) - Number(item_cost)
		console.log(exchange)
		if(exchange >0){
			count = exchange;
			$("#cookie_count").text(exchange);

			$.ajax({
				url : '/cookieclicker/buy',
				type : 'POST',
		        contentType: 'application/json',
		        dataType: "json",
				headers: {"auth" : $.cookie("UI")},
				data : {
					'item_id' : $(this).attr('id')
				}
			}).done(function(response){
				var count_buy_item = response.count_buy_item;
				var count_buy_id = "buy_item_count_"+response.item_id;
				$("#"+count_buy_id).text(count_buy_item);
			})

		}else{
			alert("クッキーが不足しているため、購入できません");
		}

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

		if(flg == "OFF"){
			flg = 1
		} else{
			flg = 0
		}

		//サーバー側にアイテムIDと有効化フラグを送る
		$.ajax({
			url : '/cookieclicker/enable',
			type : 'POST',
	        contentType: 'application/json',
	        dataType: "json",
			headers: {"auth" : $.cookie("UI")},
			data : {
				'item_id' : id.replace("item_flg_",""),
				'enabled_flg' : flg
			}
		}).done(function(response){
		//var result_enable = JSON.parse(response);

		var a = "#"+id
		var count_buy_id = id.replace("item_flg","buy_item_count")
		var count_buy_item = Number($("#"+count_buy_id).text());

		var increment_num = response.increase_cookie*count_buy_item;
		var decrease_num =-response.increase_cookie*count_buy_item;
		console.log(increment_num)
		console.log(decrease_num)


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
				$("#modal_body_text").text("これまで生産したクッキーの数は  "+response+" 枚です。")
				})

		}
	));


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