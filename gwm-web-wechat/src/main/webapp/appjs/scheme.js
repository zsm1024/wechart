var imgUrl="https://wechat.gwmfc.com/gwm-web-wechat/img/share1.jpg";	            	  // 分享图标
var openid;
$(function(){
	openid = sessionStorage.openid;
	if(undefined == openid || "" == openid){
		var code = getParam("code");
		if(code){
			$.showLoadMsg("加载中，请稍后...");
			$.JsonSRpc(baseUrl+"/snsapibase", {code:code}, function(data){
				$.hideLoadMsg();
				if(data.errcode&&"0"==data.errcode){
					openid = data.openid;
					if(openid==undefined||""==openid){
						window.location.href="./guanzhu.html";
//						message("信息获取失败!", function(flag){
//							wx.closeWindow();
//						});
					}else{
						sessionStorage.setItem("openid", openid);
						htmlInit();
					}
				}else{
					window.location.href="./guanzhu.html";
//					message("获取用户信息失败！", function(flag){
//						wx.closeWindow();
//					});
				}
			});
		}else{
			message("获取信息失败，请重新进入页面", function(flag){
				wx.closeWindow();
			})
			//htmlInit();
		}
	}else{
		htmlInit();
	}
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
					'onMenuShareTimeline','onMenuShareAppMessage', 'chooseImage' ]
		// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function() {
			wx.onMenuShareTimeline({
				title:"快来定制您的专属方案",
				link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeba2859c6bbe5dc4&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Fscheme.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
				imgUrl:imgUrl,
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
			wx.onMenuShareAppMessage({
				 title: '快来定制您的专属方案', // 分享标题
				 desc: '一键输入预期购买的车辆价格及能提供的首付金额，即可获得专属金融方案', // 分享描述
				 link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeba2859c6bbe5dc4&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Fscheme.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
				 imgUrl: imgUrl, // 分享图标
				 type: '', // 分享类型,music、video或link，不填默认为link
				 dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				 success: function () { 
				     // 用户确认分享后执行的回调函数
				 },
				 cancel: function () { 
				     // 用户取消分享后执行的回调函数
				 }
			});
		});
		wx.error(function(res) {
			alert("wx.error===" + JSON.stringify(res));
		});
	});
	
});

//页面初始化
function htmlInit(){
	//立即推荐点击
	$("#promote").click(function(){
		promote();
	});
	var cityOption = "<option value='-1'>请选择城市</option>";
	$("#jxs_city").html(cityOption);
	//初始化省市选择菜单
	initProvince();
}

//立即推荐
function promote(){
	var min_bal = $("#range_1").data("ionRangeSlider").result.from;
	var max_bal = $("#range_1").data("ionRangeSlider").result.to;
	var first_amt = $("#range_2").data("ionRangeSlider").result.from;
	var state = $("#range_3").data("ionRangeSlider").result.from;
	var province = $("#jxs_province").val();
	var city = $("#jxs_city").val();
	if(province == "-1"){
		message("请选择省份");
		return;
	}
	if(city == "-1"){
		message("请选择城市");
		return;
	}
	province = $("#jxs_province").find("option:selected").text();
	city = $("#jxs_city").find("option:selected").text();
	sessionStorage.province = province;
	sessionStorage.city = city;
	sessionStorage.min_bal = min_bal;
	sessionStorage.max_bal = max_bal;
	sessionStorage.first_amt = first_amt;
	sessionStorage.state = state;
	
	var obj = {};
	obj.province = sessionStorage.province;
	obj.city = sessionStorage.city;
	obj.min_bal = sessionStorage.min_bal;
	obj.max_bal = sessionStorage.max_bal;
	obj.first_amt = sessionStorage.first_amt;
	obj.state = sessionStorage.state;
	obj.openid = sessionStorage.openid;
	$.showLoadMsg("请稍后...");
	$.JsonSRpc(baseUrl+"/wxgetschemes", obj, function(data){
		$.hideLoadMsg();
		if(data.errcode&&"0"==data.errcode){
			var schemesStr = data.schemes;
			var schemes = JSON.parse(schemesStr);
			if(schemes.length <= 0){
				message("没有查询到合适的车型方案，请修改期望车价、首付重新推荐。");
			}else{
				window.location.href = "./schemepromote.html";
			}
		}else{
			message("没有查询到合适的车型方案，请修改期望车价、首付重新推荐。");
		}
	});
}

/**
 *初始化省级可选项
 */
function initProvince() {
	var provinces = getProvinces();
	var option = "<option value='-1'>请选择省份</value>";
	for (var i = 0; i < provinces.length; i++) {
		option += '<option value="' + provinces[i].ProvinceID + '">'
				+ provinces[i].ProvinceName + '</option>';
	}
	$("#jxs_province").html(option);
	$("#jxs_province").change(function() {
		var text = $("#jxs_province").find("option:selected").text();
		initcity(text);
	});
	$("#jxs_city").change(function() {
		var text = $("#jxs_city").find("option:selected").text();
	});
}

//省市二级联动
function initcity(cit) {
	$("#jxs_city").html("");
	var cityArray = getCities(cit);
	var cityOption = "<option value='-1'>请选择城市</option>";
	if (cityArray != undefined) {
		for (var i = 0; i < cityArray.length; i++) {
			cityOption += '<option value="' + cityArray[i].CityID + '">'
					+ cityArray[i].CityName + '</option>';
		}
	}
	$("#jxs_city").html(cityOption);
}