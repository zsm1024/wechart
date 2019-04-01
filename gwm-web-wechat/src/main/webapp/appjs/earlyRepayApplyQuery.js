var openid="";
$(function (){
	//openid="o5iChju4y1zdq0NKheVA5ilu3r1A";
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
		//获取提前还款申请记录
		getList();
	}
	$("#btnOk").click(function(){
		//指向上传还款凭证页面
		window.location.href="uploadcertificate.html";
	});
});

//提前还款申请记录查询
function getList(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/queryEarlyRepayApply",{openId: openid},function(data) {
		$.hideLoadMsg();
		if(data.errcode=="0"){
			var list=data.returnList;
			for (var i = 0; i < list.length; i++) {
				var temp="<li>"
						+"<span>"+checkNullToEmpty(dateFormat(list[i].apply_date))
						+"&nbsp;&nbsp;"+checkNullToEmpty(timeFormat(list[i].apply_time))+"</span>"
						+"<span>"+checkNullToEmpty(dateFormat(list[i].apply_repay_date))+"</span>"
						+"<span>"+checkNullToEmpty(fmoney(list[i].total_amt,2))+"元</span>"
						+"</li>";
				$("#ulData").append(temp);
			}
		}
		else{
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
//日期格式
function dateFormat(para){
	var year=para.toString().substring(0,4);
	var moon=para.toString().substring(4,6);
	var day=para.toString().substring(6,8);
	var newdate=year+"-"+moon+"-"+day;
	return newdate;
}
//时间格式
function timeFormat(para){
	var hour=null;
	var minute=null;
	var time= para.toString();
//	var second=null;
	while(time.length < 6){
		time="0"+time;
	}
	hour=time.substring(0,2);
	minute=time.substring(2,4);
//	second=para.substring(4,6);
	var newtime=hour+":"+minute;
	return newtime;
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
				//获取提前还款申请记录
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




