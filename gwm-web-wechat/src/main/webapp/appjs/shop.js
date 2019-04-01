var citylocation,map;
var markersArray = [];
var listenerArray = [];
var infoArray = [];
var markerData = [];
var nomarkerData = [];
var listData = [];
var curr_city;
var mylat;
var mylng;
var my_marker;
var marker_blue;
var marker_red;
var redmarker;
var redinfo;

$(function(){
	var openid = sessionStorage.openid;
	if(undefined == openid || "" == openid){
		var code = getParam("code");
		if(code){
			$.showLoadMsg("加载中，请稍后...");
			$.JsonSRpc(baseUrl+"/snsapibase", {code:code}, function(data){
				$.hideLoadMsg();
				if(data.errcode&&"0"==data.errcode){
					openid = data.openid;
					if(openid==undefined||""==openid){
						message("信息获取失败!", function(flag){
							wx.closeWindow();
						});
					}else{
						sessionStorage.setItem("openid", openid);
					}
				}else{
					message("获取用户信息失败！", function(flag){
						wx.closeWindow();
					});
				}
			});
		}else{
			message("获取信息失败，请重新进入页面", function(flag){
				wx.closeWindow();
			})
			//htmlInit();
		}
	}
	$.showLoadMsg("页面加载中，请稍后...");
	mapInit();
	jssdkconfig();
	setTimeout(function(){
		$.hideLoadMsg();
		var city_name = $("#city").html();
		loadShopInfo(city_name);
	}, 10000);
//	loadShopInfo(curr_city);
});

function mapInit(){
	var anchor = new qq.maps.Point(10, 34);
    var size = new qq.maps.Size(20, 34);
    var origin = new qq.maps.Point(0, 0);
    marker_blue = new qq.maps.MarkerImage(
        "img/marker_blue.png",
        size,
        origin,
        anchor
    );
	marker_red = new qq.maps.MarkerImage(
	        "img/marker_red.png",
	        size,
	        origin,
	        anchor
	    );
	var center = new qq.maps.LatLng(39.916527, 116.397128);
	map = new qq.maps.Map(document.getElementById('container'), {
		center : center,
		zoom : 10,
		zoomControl : true,
		zoomControlOptions: {
            //设置缩放控件的位置为相对左方中间位置对齐.
            position: qq.maps.ControlPosition.TOP_RIGHT,
            //设置缩放控件样式为仅包含放大缩小两个按钮
            style: qq.maps.ZoomControlStyle.DEFAULT
        }
	});
	//获取  城市位置信息查询 接口  
	citylocation = new qq.maps.CityService({
		//设置地图
		map : map,
		complete : function(results) {
			var city_name;
			detail = results.detail.detail;
			console.info(JSON.stringify(results));
			list = detail.split(",");
			if(list.length > 3){
				city_name = list[list.length-3];
			}else{
				var pro_name = list[list.length-2];
				if(pro_name.indexOf("市")>0){
					city_name = pro_name;
				}else{
					city_name = list[0];
				}
			}
			curr_city = city_name;
			sessionStorage.setItem("current_city", city_name);
			loadShopInfo(city_name);
		},
		error : function() {
			curr_city = "北京";
//			sessionStorage.setItem("current_city", city_name);
			loadShopInfo(curr_city);
		}
	});
}

//添加我的位置
function addMyPosition(lat, lng){
	var pos = new qq.maps.LatLng(lat, lng);
	//设置Marker自定义图标的属性，size是图标尺寸，该尺寸为显示图标的实际尺寸，
	//origin是切图坐标，该坐标是相对于图片左上角默认为（0,0）的相对像素坐标，
	//anchor是锚点坐标，描述经纬度点对应图标中的位置
    var anchor = new qq.maps.Point(18, 42),
        size = new qq.maps.Size(36, 46),
        origin = new qq.maps.Point(0, 0),
        icon = new qq.maps.MarkerImage(
            "img/ico_my_pos.png",
            size,
            origin,
            anchor
        );
    my_marker = new qq.maps.Marker({
        map: map,
        position: pos
    });
    my_marker.setIcon(icon);
    map.setCenter(pos);
	map.setZoom(10);
}
function delMyPosition(){
	if(my_marker){
		my_marker.setMap(null);
	}
}
function showMyPosition(){
	if(my_marker){
		my_marker.setMap(map);
	}
}

function jssdkconfig(){
	$.JsonSRpc(baseUrl+"/jssdksign", {
		url : location.href.split('#')[0]
	}, function(data) {
		wx.config({
			debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : data.appId, // 必填，公众号的唯一标识
			timestamp : data.timestamp, // 必填，生成签名的时间戳
			nonceStr : data.nonceStr, // 必填，生成签名的随机串
			signature : data.signature, // 必填，签名，见附录1
			jsApiList : [ 'checkJsApi', 'openLocation', 'getLocation',
					'onMenuShareQZone', 'chooseImage', 'closeWindow' ]
		// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function() {
			wx.getLocation({
				type : 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
				success : function(res) {
					$.hideLoadMsg();
					var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
					var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
					mylat = latitude;
					mylng = longitude;
//					addMyPosition(latitude, longitude);
					var pos = new qq.maps.LatLng(latitude, longitude);
					citylocation.searchCityByLatLng(pos);
				},
				fail : function(res) {
					$.hideLoadMsg();
					curr_city = "北京";
					//sessionStorage.setItem("current_city", city_name);
					loadShopInfo(curr_city);
				}
			});
		});
		wx.error(function(res) {
			$.hideLoadMsg();
			curr_city = "北京";
			sessionStorage.setItem("current_city", curr_city);
			$("#city").html(curr_city);
			loadShopInfo(curr_city);
		});
	});
}

//加载经销商信息
function loadShopInfo(city){
	var select_city = sessionStorage.getItem("select_city");
	if(select_city!=undefined&&""!=select_city){
		if(city == select_city){
			showMyPosition();
		}else{
			delMyPosition();
		}
		city = select_city;
		$("#msg").html("");
	}else{
		$("#msg").html("我们猜您在这儿");
		if(mylat&&mylng){
			addMyPosition(mylat, mylng);
		}
	}
	$("#city").html(city);
	$.showLoadMsg("正在查询，请稍后...");
	$.JsonSRpc(baseUrl+"/wxqueryshop", {city:city,lat:mylat,lng:mylng}, function(data){
		$.hideLoadMsg();
		if(data.errcode&&"0"==data.errcode){
			deleteOverlays();
			var shops = JSON.parse(data.shops);
			var items = '';
			var n = 0;
			markerData.length = 0;
			nomarkerData.length = 0;
			listData.length = 0;
			listData = shops;
			map.setZoom(10);
			for (var i = 0; i < shops.length; i++) {
				var num = i + 1;
				if (shops[i].sh_salelatitude != undefined && shops[i].sh_salelatitude.length>0 && shops[i].sh_salelongitude != undefined && shops[i].sh_salelongitude.length>0) {
					markerData.push(shops[i]);
					items += '<dt onclick="clickItem(\''+markerData.length+'\')">' +
					'<h3>'+shops[i].sh_name+'</h3>'+
					'地址：'+shops[i].sh_address +
					'</dt>' +
					'<dd>';
				}else{
					nomarkerData.push(shops[i]);
					items += '<dt>' +
					'<h3>'+shops[i].sh_name+'</h3>'+
					'地址：'+shops[i].sh_address +
					'</dt>' +
					'<dd>';
				}
				var hotline = shops[i].sh_salehotline.split("\/");
				for (var j = 0; j < hotline.length; j++) {
					items += '<p>销售热线：' + hotline[j] + '</p>';
				}
				items += '<p>营业时间：'+shops[i].sh_saletime+'</p>' ;
				if(hotline.length>0&&hotline[0].length>0){
					items += '<a href="tel:'+hotline[0]+'"></a>';
				}
				items += '</dd>';
			}
			addMaker();
			if (markerData.length > 0) {
				var loc = new qq.maps.LatLng(
						markerData[0].sh_salelatitude,
						markerData[0].sh_salelongitude);
				map.setCenter(loc);
			}
			$("#jxs_shop").html(items);
			$(".jxscx dt").click(function(){
				$(this).toggleClass("on");
				$(this).next().toggle();
			});
//			window.location.href = "#5";
		}
	});
}

//地图上添加标记
function addMaker() {
    for (var i = 0; i < markerData.length; i++)
    {
        (function(index, data) {
            var myLatLng = new qq.maps.LatLng(data.sh_salelatitude, data.sh_salelongitude);
            var marker = new qq.maps.Marker({
				position : myLatLng,
				map : map
			});
            marker.setIcon(marker_blue);
            var infoWindow = new qq.maps.InfoWindow({position: myLatLng, map: map});
            infoArray.push(infoWindow);
            qq.maps.event.addListener(infoWindow, "closeclick", function() {
            	infoArray[index].close();
            });
            var lis = qq.maps.event.addListener(marker, 'click', function() {
            	skipItem(index);
                for (var x = 0; x < infoArray.length; x++) {
                	infoArray[x].close();
                }
                var name = data.sh_name;
                infoArray[index].open();
                var sh_name = data.sh_name;
            	if(sh_name.length > 14){
            		infoArray[index].setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:left;white-space:nowrap;">'+sh_name.substring(0, 14)+"<br>"+sh_name.substring(14)+'</div>');
            	}else{
            		infoArray[index].setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:left;white-space:nowrap;">'+sh_name+'</div>');
            	}
//                infoArray[index].setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:center;white-space:nowrap;">'+data.sh_name+'</div>');
                redinfo = infoArray[index];
            });
            markersArray.push(marker);
            listenerArray.push(lis);
        })(i, markerData[i]);
    }
}
//点击marker
function skipItem(n){
//	$("#jxs_shop").html("");
	var index = n+1;
	var data = markerData[n];
	var items = '';
	items += '<dt class="on" onclick="clickItem(\''+index+'\')">' +
	'<h3>'+data.sh_name+'</h3>'+
	'地址：'+data.sh_address +
	'</dt>' +
	'<dd style="display:block">';
	var hotline = data.sh_salehotline.split("\/");
	for (var j = 0; j < hotline.length; j++) {
		items += '<p>销售热线：' + hotline[j] + '</p>';
	}
	items += '<p>营业时间：'+data.sh_saletime+'</p>' ;
	if(hotline.length>0&&hotline[0].length>0){
		items += '<a href="tel:'+hotline[0]+'"></a>';
	}
	items += '</dd>';
	for(var i = 0; i < markerData.length; i++){
		if(i == n){
			continue;
		}
		data = markerData[i];
		index = i+1;
		items += '<dt onclick="clickItem(\''+index+'\')">' +
		'<h3>'+data.sh_name+'</h3>'+
		'地址：'+data.sh_address +
		'</dt>' +
		'<dd>';
		hotline = data.sh_salehotline.split("\/");
		for (var j = 0; j < hotline.length; j++) {
			items += '<p>销售热线：' + hotline[j] + '</p>';
		}
		items += '<p>营业时间：'+data.sh_saletime+'</p>' ;
		if(hotline.length>0&&hotline[0].length>0){
			items += '<a href="tel:'+hotline[0]+'"></a>';
		}
		items += '</dd>';
	}
	for(var i = 0; i < nomarkerData.length; i++){
		data = nomarkerData[i];
		index = i+1;
		items += '<dt>' +
		'<h3>'+data.sh_name+'</h3>'+
		'地址：'+data.sh_address +
		'</dt>' +
		'<dd>';
		hotline = data.sh_salehotline.split("\/");
		for (var j = 0; j < hotline.length; j++) {
			items += '<p>销售热线：' + hotline[j] + '</p>';
		}
		items += '<p>营业时间：'+data.sh_saletime+'</p>' ;
		if(hotline.length>0&&hotline[0].length>0){
			items += '<a href="tel:'+hotline[0]+'"></a>';
		}
		items += '</dd>';
	}
	$("#jxs_shop").html(items);
	$(".jxscx dt").click(function(){
		$(this).toggleClass("on");
		$(this).next().toggle();
	});
	window.scrollTo(0,0);
	redShow(n);
	var loc = new qq.maps.LatLng(
			data.sh_salelatitude,
			data.sh_salelongitude);
	map.setCenter(loc);
}
function redShow(n){
	var mk = markersArray[n];
	var info = infoArray[n];
	var data = markerData[n];
	var index = n;
	if(redmarker){
		redmarker.setIcon(marker_blue);
	}
	if(redinfo){
		redinfo.close();
	}
	mk.setIcon(marker_red);
	redmarker = mk;
}
//点击info
function clickInfo(n){
	infoArray[n].close();
}
//点击列表
function clickItem(n){
	var mk = markersArray[n-1];
	var info = infoArray[n-1];
	var data = markerData[n-1];
	var index = n-1;
	if(redmarker){
		redmarker.setIcon(marker_blue);
	}
	if(redinfo){
		redinfo.close();
	}
	mk.setIcon(marker_red);
	redmarker = mk;
	info.open();
	var sh_name = data.sh_name;
	if(sh_name.length > 14){
		info.setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:left;white-space:nowrap;">'+sh_name.substring(0, 14)+"<br>"+sh_name.substring(14)+'</div>');
	}else{
		info.setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:left;white-space:nowrap;">'+sh_name+'</div>');
	}
//	info.setContent('<div onclick="clickInfo(\''+index+'\')" style="text-align:center;white-space:nowrap;">'+data.sh_name+'</div>');
	redinfo = info;
	var loc = new qq.maps.LatLng(
			data.sh_salelatitude,
			data.sh_salelongitude);
	map.setCenter(loc);
}

//删除覆盖物
function deleteOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}
}
