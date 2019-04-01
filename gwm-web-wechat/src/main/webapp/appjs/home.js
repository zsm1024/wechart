//长城滨银汽车金融首页
//页面初始化
var select_city;
var userid;
var openid;
$(function (){
	openid=sessionStorage.getItem("openid");
//	openid="o5iChju4y1zdq0NKheVA5ilu3r1A";
	if(openid == null || openid=="" || undefined == openid ){
		var code = getParam("code");
		if(undefined == code || "" == code || code== null){
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}else{
			$.showLoadMsg("加载中，请稍后...");
			getOpenId();
		}
	}
	else{
		//查看是否有未阅读的消息通知
		sessionStorage.setItem("openid", openid);
		messageNotify();
		htmlInit();
	}
	
	select_city=sessionStorage.select_city;
	if(select_city==""||select_city=="null"||select_city =="undefined"||select_city==undefined)
	{
		// 定位功能
		wxdingwei();
	}
	else
	{
		changecity(select_city);	
	}
	
});
var judg = false;
function htmlInit(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//console.log(data);
		$.hideLoadMsg();
		if(data.bindingJudg=="false"){
			judge = false;
		}else{
			judge = true;
		}
		$("#repayRecordQuery").click(function(){
			if(judge){
				window.location.href = "./repayRecordQuery.html";
			}else{
				message("您还没有绑定任何合同", function(flag){
					if(flag){
						window.location.href="./bindingProtocol.html";
					}
				}, true);
			}
		});
		$("#changecontact").click(function(){
			if(judge){
				window.location.href = "./changecontact.html";
			}else{
				message("您还没有绑定任何合同", function(flag){
					if(flag){
						window.location.href="./bindingProtocol.html";
					}
				}, true);
			}
		});
		$("#quotacesuan").click(function(){
			message("敬请期待！");
//			window.location.href="./quotacalcindex.html";
		})
		$("#uploadcertificate").click(function(){
			message("敬请期待！");
//			if(judge){
//				$.showLoadMsg("数据加载中，请稍后...");
//				$.JsonSRpc(baseUrl+"/getcurrentcontract", {openid:openid}, function(data){
//					$.hideLoadMsg();
//					if(data.errcode&&"0"!=data.errcode){
//						flag = false;
//						message(data.errmsg);
//					}else{
//						window.location.href = "./uploadcertificate.html";
//					}
//				});
//			}else{
//				message("您还没有绑定任何合同", function(flag){
//					if(flag){
//						window.location.href="./bindingProtocol.html";
//					}
//				}, true);
//			}
		});
		$("#earlypay").click(function(){
			if(judge){
				window.location.href = "./earlypay.html";
			}else{
				message("您还没有绑定任何合同", function(flag){
					if(flag){
						window.location.href="./bindingProtocol.html";
					}
				}, true);
			}
		});
	});
}

function wxdingwei(){
	$.showLoadMsg("加载中，请稍后...");
	var url = basePath+"/jssdksign?test=1";
	$.JsonSRpc(url, {url:location.href.split('#')[0]},
		function(data) {
		$.hideLoadMsg();
		if (data.error) {
			message(data.error);
			//$.hideLoadMsg();
			return;
		}
		else 
		{
			wx.config({
	            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	            appId: data.appId, // 必填，公众号的唯一标识
	            timestamp: data.timestamp, // 必填，生成签名的时间戳
	            nonceStr: data.nonceStr, // 必填，生成签名的随机串
	            signature: data.signature, // 必填，签名，见附录1
	            jsApiList: [
	                'checkJsApi',
	                'openLocation',
	                'getLocation',
	                'onMenuShareQZone',
	                'chooseImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	        });
			 wx.ready(function() {

			 wx.getLocation({
	                type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	                success: function(res) {
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    lat = res.latitude;
	                    lng = res.longitude;
	                    var latLng = new qq.maps.LatLng(latitude,longitude);
						geocoder.getAddress(latLng);
	                },
	                fail: function(res) {
	                    message("获取地理位置失败！");
//	                    wx.closeWindow();
	                }
	            });
			 });
		}
	});
}

function changecity(cname) {
	$('#address').hide();
	$('#dingwei').html(cname);
//	selectbr();
}
geocoder = new qq.maps.Geocoder({
	complete : function(result) {
		var cityname = result.detail.addressComponents.city;
		cityname = cityname.replace("市", "");
		cityname = cityname.trim();
		sessionStorage.select_city=cityname;
		changecity(cityname);
		return;
	}
});
var showNOGPShtml = function(){
	var content = "<div style='width: 100%; position: absolute;' id='tip'><p style='background: url(image/iocn_37.png) no-repeat center 6.6rem; padding-top: 18.0rem; font-size: 1.3rem; color: #888; text-align: center;'>亲~,您的GPS未启用或定位获取中。</p></div>"
		+ "<nav class='list_body'></nav>";
	$(".list").html(content);
};


function messageNotify(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/messageNotify",{openid:openid},function(data) {
		$.hideLoadMsg();
		//console.log(data);
		if(data.errcode=="0"){
			$("#messageErr").removeClass("news");
			$("#messageOk").addClass("news");
		}
		else{
			$("#messageOk").removeClass("news");
			$("#messageErr").addClass("news");
		}
	});
}


//获取openid
function getOpenId(){
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: getParam("code"),flag:'true'},function(data) {
		$.hideLoadMsg();
		if(data.errcode!=null && data.errcode!=null &&"0"==data.errcode){
			openid = data.openid;
//			alert("1233");
			if(openid==undefined||""==openid){
				window.location.href="./guanzhu.html";
//				message("信息获取失败，请重新进入页面", function(flag){
//					wx.closeWindow();
//				});
				return;
			}else{
				sessionStorage.setItem("openid", openid);
				messageNotify();
				htmlInit();
			}
		}else{
			window.location.href="./guanzhu.html";
			return;
//			message("信息获取失败，请重新进入页面", function(flag){
//				wx.closeWindow();
//			});
//			return;
		}
	});
}

//金融产品点击
function productClick(){
	window.location.href = "./product.html";
}