	$(function(){
		<!-- 画面表示情報取得処理 -->
		function diplay_info(status_code, error_message, result) {
			//テスト用変数
			var status_code = 0;
			var error_message = "えらー！";
			var result = {
					"auth": "user_id",
					"count_total_cookie": "1500",
					"picture_pass_cookie": "/image/perfectCookie.png",
					"items": [{
							"picture_pass_item": "/image/storeTile.jpg",
							"item_name": "storeTile",
							"item_cost": "",
							"count_buy_item": ""
						},
						{
							"picture_pass_item": "/image/item1.png",
							"item_name": "カーソル",
							"item_cost": "2000",
							"count_buy_item": "20"
						},
						{
							"picture_pass_item": "/image/item2.png",
							"item_name": "BBA",
							"item_cost": "3000",
							"count_buy_item": "30"
						},
						{
							"picture_pass_item": "/image/item3.png",
							"item_name": "農場",
							"item_cost": "4000",
							"count_buy_item": "40"
						},
						{
							"picture_pass_item": "/image/item4.png",
							"item_name": "鉱山",
							"item_cost": "5000",
							"count_buy_item": "50"
						},
						{
							"picture_pass_item": "/image/item5.png",
							"item_name": "工場",
							"item_cost": "6000",
							"count_buy_item": "60"
						},
						{
							"picture_pass_item": "/image/item6.png",
							"item_name": "銀行",
							"item_cost": "7000",
							"count_buy_item": "70"
						},
						{
							"picture_pass_item": "/image/item7.png",
							"item_name": "魔女の塔",
							"item_cost": "8000",
							"count_buy_item": "80"
						},
						{
							"picture_pass_item": "/image/item8.png",
							"item_name": "宇宙人",
							"item_cost": "9000",
							"count_buy_item": "90"
						},
						{
							"picture_pass_item": "/image/cookieShower2.png",
							"item_name": "cookieShower",
							"item_cost": "",
							"count_buy_item": ""
						},
						{
							"picture_pass_item": "/image/panelVertical1.png",
							"item_name": "panel",
							"item_cost": "",
							"count_buy_item": ""
						}
					]
				};

			//アイテム部分表示
			$.each(result.items,function(i){
				var item = result.items[i].item_name

				switch(item){
				case "カーソル" :
					$("#buy_item_count_1").text(result.items[i].count_buy_item);
					$("#item_name_1").text(result.items[i].item_name);
					$('#item_icon_1').append('<img src="'+ result.items[i].picture_pass_item +'">');
					break;

				case "BBA" :
					$("#buy_item_count_2").text(result.items[i].count_buy_item);
					$("#item_name_2").text(result.items[i].item_name);
					$('#item_icon_2').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "農場" :
					$("#buy_item_count_3").text(result.items[i].count_buy_item);
					$("#item_name_3").text(result.items[i].item_name);
					$('#item_icon_3').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "鉱山" :
					$("#buy_item_count_4").text(result.items[i].count_buy_item);
					$("#item_name_4").text(result.items[i].item_name);
					$('#item_icon_4').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "工場" :
					$("#buy_item_count_5").text(result.items[i].count_buy_item);
					$("#item_name_5").text(result.items[i].item_name);
					$('#item_icon_5').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "銀行" :
					$("#buy_item_count_6").text(result.items[i].count_buy_item);
					$("#item_name_6").text(result.items[i].item_name);
					$('#item_icon_6').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "魔女の塔" :
					$("#buy_item_count_7").text(result.items[i].count_buy_item);
					$("#item_name_7").text(result.items[i].item_name);
					$('#item_icon_7').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "宇宙人" :
					$("#buy_item_count_8").text(result.items[i].count_buy_item);
					$("#item_name_8").text(result.items[i].item_name);
					$('#item_icon_8').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "storeTile" :
					$('#item_list').append('<img src="'+ result.items[i].picture_pass_item +'">');
					$('#item_list_2').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "cookieShower" :
					$('#slide_1').append('<img src="'+ result.items[i].picture_pass_item +'">');
					$('#slide_2').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;

				case "panel" :
					$('#panel').append('<img src="'+ result.items[i].picture_pass_item +'">');
					$('#panel_2').append('<img src="'+ result.items[i].picture_pass_item +'">');
					$('#panel_3').append('<img src="'+ result.items[i].picture_pass_item +'">');
					$('#panel_4').append('<img src="'+ result.items[i].picture_pass_item +'">');
				    break;
				}
			});

			//クッキー枚数、クッキー画像表示
			$("#total_cookie_count").append(result.count_total_cookie);
			$('#big_cookie').append('<img src="'+ result.picture_pass_cookie +'">');

			if (status_code == 1) {
				alert(error_message);
			}

		};
		diplay_info();


	<!-- auth関連処理 -->
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

	<!-- カウントアップ処理 -->
		var count = $("#total_cookie_count").text();
		console.log(count);

		//クリック時処理
		$("#big_cookie").click(function() {
			$("#cookie_count").text(++count);
		});

		//テスト用
		//function count_up() {
		//	$("#cookie_count").html(++count);
		//}
		//setInterval('count_up()', 1000);

		//クッキー枚数登録
		function update_cookie_count() {
			$.ajax({
				url : '/cookieclicker/countup',
				type : 'POST',
				auth : String($.cookie("UI")),
				data : {
					'cookie_count' : count
				}
			})
		}
		setInterval('update_cookie_count()', 10000);

	<!-- アイテム購入処理 -->
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

	<!-- アイテム有効化 -->
	$(".item_flg").click(function() {
	var flg = $(this).text();
	console.log(flg);
	if(flg == 'ON'){
		$(this).text('OFF');
	}else{
		$(this).text('ON');
	}
	});




	<!-- クッキー背景ループ -->
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


	<!-- スクロールロック -->
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