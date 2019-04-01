var openid="";
//var openid="o5iChju4y1zdq0NKheVA5ilu3r1A";
//联系方式数字正则，不能以0开头的数字
var regNumber=/^[1-9]{1}[0-9]*$/;
$(function (){
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
			getOpenId();
		}
	}else{
		sessionStorage.setItem("openid",openid);
	}
	
	$("#btnOk").click(function(){
		if(switchCase()){
			feedBack();
		}
	});
});

//提交前校验
function switchCase(){
	if(infomation() && contactType()){
		return true;
	}
	return false;
}

//反馈信息
function infomation(){
	if($("#suggest_cont").val()!=null && $("#suggest_cont").val()!=""){
		return true;
	}
	message("请填写反馈信息");
	return false;
}

//联系方式
function contactType(){
	if($("#contact_type").val()!=null && $("#contact_type").val()!=""){
		if(!regNumber.test($("#contact_type").val())){
			message("请填写正确的联系方式");
			return false;
		}
		return true;
	}
	message("请填写联系方式");
	return false;
}
//提交意见反馈
function feedBack(){
	$.showLoadMsg("提交中，请稍后...");
	var suggest_type=$('input:radio[name="yjfk"]:checked').val();
	var suggest_cont=$("#suggest_cont").val();
	var contact_type=$("#contact_type").val();
	$.JsonSRpc(baseUrl+ "/feedBack",{openId: openid,suggest_type: suggest_type,
		suggest_cont: suggest_cont,contact_type :contact_type},function(data) {
		$.hideLoadMsg();
		if(data.errcode=="0"){
			message("反馈信息提交成功，感谢您的支持！", function(flag){
				window.location.href="./accountSettings.html";
			});
			$("#suggest_cont").val("");
			$("#contact_type").val("");
		}
		else{
			message(data.errmsg);
		}
	});
}


//获取openid
function getOpenId(){
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: getParam("code")},function(data) {
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
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}





