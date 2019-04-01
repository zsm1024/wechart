var citylocation;
var personalData;
var openid="";
//选择的地区
var select_city="";
//定位的城市
var curr_city="";
$(function (){
	select_city=sessionStorage.getItem("select_city");
	select_city = checkNullToEmpty(select_city);
	sessionStorage.removeItem("select_city");
	openid=sessionStorage.getItem("openid");
	if(openid == null || openid=="" || undefined == openid ){
		var code = getParam("code");
		if(undefined == code || "" == code || code== null){
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}else{
			$.showLoadMsg("加载中，请稍后...");
			//根据code重新获取openid
			getOpenId(code);
		}
	}
	else{
		sessionStorage.setItem("openid",openid);
		//获取个人信息数据
		getList();
	}
	
	//获取  城市位置信息查询 接口  
	citylocation = new qq.maps.CityService({
		complete : function(results) {
			var city_name;
			detail = results.detail.detail;
			//console.info(JSON.stringify(results));
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
			//sessionStorage.setItem("current_city", city_name);
			//根据定位获取的位置进行拼装数据
			assembling(curr_city);
		},
		error : function() {
			curr_city = "未填写";
			//sessionStorage.setItem("current_city", curr_city);
			//根据定位获取的位置进行拼装数据
			assembling(curr_city);
		}
	});
	
	//收入的变化传到后台
	$("#info_income").change(function(){
		incomeChange();
	});
});
//根据定位获取的位置进行拼装数据
function assembling(city){
	if(city.indexOf("未填写")>=0){
		$("#info_zone").html("未填写");
	}else{
		select_city = city;
		$("#info_zone").html(city);
		zoneChange();
	}
//	var tempOne="<li>" 
//				+"<label>姓名：</label>"+checkNullToEmpty(personalData.user_name)
//				+"</li>" 
//				+"<li>" 
//				+"<label>性别：</label>"+checkNullToEmpty(sex(personalData.user_sex))
//				+"</li>"
//				+"<li>" 
//				+"<label>地区：</label>"+checkNullToEmpty(zone(city))
//				+"</li>"
//				+"<li>" 
//				+"<label>年龄：</label>"+checkNullToEmpty(personalData.user_age)
//				+"</li>"
//				+"<li>" +
//				"<label>手机号：</label>"+checkNullToEmpty(personalData.user_phone)
//				+"</li>";
//
//	var tempTwo="<li margin-left: 150px'>" 
//				+"<label>身份证号：</label>"+checkNullToEmpty(personalData.user_id)
//				+"</li>" ;
//	income(personalData.income);
//	$("#lineOne").append(tempOne);
//	$("#lineTwo").prepend(tempTwo);
}
//获取个人信息数据,如果地区不为空则数据在此拼装
function getList(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/personalInfo",{openId: openid},function(data) {
		$.hideLoadMsg();
		if(data.errcode=="0"){
			$("#info_name").html(checkNullToEmpty(data.user_name)==""?"未填写":data.user_name);
			$("#info_sex").html(checkNullToEmpty(data.user_sex)==""?"未填写":data.user_sex);
			$("#info_age").html(checkNullToEmpty(data.user_age)==""?"未填写":data.user_age);
			$("#info_phone").html(checkNullToEmpty(data.user_phone)==""?"未填写":data.user_phone);
			
			$("#info_idno").html(checkNullToEmpty(data.user_id)==""?"未填写":data.user_id);
			var income = checkNullToEmpty(data.income)==""?"0":data.income;
			$("#info_income").val(income);
			if(""!=select_city){
				$("#info_zone").html(select_city);
				zoneChange();
			}else{
				if(checkNullToEmpty(data.user_zone)==""){
					$("#info_zone").html("未填写");
					jssdkconfig();
				}else{
					$("#info_zone").html(checkNullToEmpty(data.user_zone));
				}
			}
			
//			if(data.user_zone==null || data.user_zone==undefined || data.user_zone==""){
//				//启用定位获取位置
//				jssdkconfig();
//				personalData=data;
//			}
//			else{
//				var tempOne="<li>" 
//							+"<label>姓名：</label>"+checkNullToEmpty(data.user_name)
//							+"</li>" 
//							+"<li>" 
//							+"<label>性别：</label>"+checkNullToEmpty(sex(data.user_sex))
//							+"</li>"
//							+"<li>" 
//							+"<label>地区：</label>"+checkNullToEmpty(zone(data.user_zone))
//							+"</li>"
//							+"<li>" 
//							+"<label>年龄：</label>"+checkNullToEmpty(data.user_age)
//							+"</li>"
//							+"<li>" +
//							"<label>手机号：</label>"+checkNullToEmpty(data.user_phone)
//							+"</li>";
//				
//				var tempTwo="<li margin-left: 150px'>" 
//					+"<label>身份证号：</label>"+checkNullToEmpty(data.user_id)
//					+"</li>" ;
//				income(data.income);
//				$("#lineOne").append(tempOne);
//				$("#lineTwo").prepend(tempTwo);
//			}
		}else{
			message(data.errmsg, function(flag){
				history.go(-1);
			});
		}
		
	});
}
//null转空
function checkNullToEmpty(para){
	if(para==null || para=="null" || para==undefined){
		return "";
	}
	return para;
}
//性别编码转换
function sex(para){
	if(para==1){
		return "男";
	}else if(para==2){
		return "女";
	}
	return para;
}
//地区转换
function zone(para){
	if(select_city=="" || select_city==null || select_city==undefined){
		if(para==null || para=="null" || para==undefined){
			return "<a href='cityselect.html'>未填写</a>";
		}
		else{
			return "<a  href='cityselect.html'>"+para+"</a>";
		}
	}else{
		zoneChange(select_city);
		return "<a  href='cityselect.html'>"+select_city+"</a>";
	}
}
//收入值初始化
function income(para){
	if(para==null || para=="null" || para==undefined){
		$("#income").val(0);
	}
	else{
		$("#income").val(para);
	}
}
//选择收入范围
function incomeChange(){
	$.JsonSRpc(baseUrl+ "/incomeChange",{income: $("#info_income").val(),openId: openid},function(data) {
		if(data.errcode!="0"){
			message(data.errmsg);
		}
	});
}
//选择地区
function zoneChange(){
	$.JsonSRpc(baseUrl+ "/zoneChange",{select_city: select_city,openId: openid},function(data) {
		if(data.errcode!="0"){
			message(data.errmsg);
		}
	});
}

//wx定位获取经纬度
function jssdkconfig(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+"/jssdksign", {
		url : location.href.split('#')[0]
	}, function(data) {
		$.hideLoadMsg();
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
					var pos = new qq.maps.LatLng(latitude, longitude);
					citylocation.searchCityByLatLng(pos);
				},
				fail : function(res) {
					$.hideLoadMsg();
					curr_city = "未填写";
//					sessionStorage.setItem("current_city", curr_city);
					//根据定位获取的位置进行拼装数据
					assembling(curr_city);
				}
			});
		});
		wx.error(function(res) {
			$.hideLoadMsg();
			curr_city = "未填写";
//			sessionStorage.setItem("current_city", curr_city);
			//根据定位获取的位置进行拼装数据
			assembling(curr_city);
		});
	});
}

//获取openid
function getOpenId(code){
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: code},function(data) {
		$.hideLoadMsg();
		if(data.errcode!=null && data.errcode!=null &&"0"==data.errcode){
			openid = data.openid;
			if(openid==undefined||""==openid){
				message("信息获取失败，请重新进入页面", function(flag){
					wx.closeWindow();
				});
				return;
			}else{
				sessionStorage.setItem("openid", openid);
				//获取个人信息数据
				getList();
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}





