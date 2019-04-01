//验证码倒计时60秒
var wait=60;
//产生6位随机验证码数字
//var randomNum="";
//判断验证码是否失效
var flag="false";
//手机有效性正则，支持13、14、15、17、18开头的手机号(2016年)。
var regPhone=/^1[3|4|5|7|8][0-9]\d{8}$/;
//数字校验
var regNumber=/^[0-9]*$/;
//身份证正则校验
var regResidentID = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
//判断客户是第一次点击获取验证码？ 弹出不同提示信息
var isFirst="true";
var openid="";
//var openid="o5iChju4y1zdq0NKheVA5ilu3r1A";

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
	}
	else{
		sessionStorage.setItem("openid",openid);
		htmlInit();
	}
	$("#btnOk").click(function(){
		if(switchCase()){
			var beforetime=getNowDate();
			if(confirm('解除绑定后，您将无法收到我们为您推送的还款提醒信息，真的要解除绑定吗？')){
				timeDifference(beforetime);
				bindAccount();
			}
			else{
				timeDifference(beforetime);
			}
		}
	});
	//获取验证码
	$("#getCode").click(function(){
		//点击获取验证码后县校验预留手机号
		if(reservePhoneCheck()){
			isFirst="false";
			//获取对象
			var o=$("#getCode");
			message("验证码已发送");
			//倒计时
			getCode(o);
			//向手机发送验证码
			sendVerificationCode();
		}
	});

});
function checkIdNumber(){
	idNumberCheck();
}
function checkPhone(){
	reservePhoneCheck();
}
function htmlInit(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//console.log(data);
		$.hideLoadMsg();
		if(data.bindingJudg=="false"){
			message("您还没有绑定任何合同", function(flag){
//				wx.closeWindow();
				backClick();
			});
		}
	});
}

//计算弹出提示框消耗的时间，反馈给验证码倒计时
function timeDifference(beforetime){
	if(flag=="true"){
		var aftertime=getNowDate();
		if(wait-(aftertime-beforetime)<0){
			wait=0;
		}
		else{
			wait=wait-(aftertime-beforetime);
		}
	}
}
//获取当前时间
function getNowDate(){
	var myDate = new Date();
	var mytime=myDate.getTime();        //获取当前时间(从1970.1.1开始的毫秒数)
	return Math.round(mytime*0.001);
}
//获取验证码
function getCode(o){
	if (wait == 0) { 
		flag="false";
		o.css("background","#c8161d");
		o.removeAttr("disabled");    
		o.val("获取验证码") ;
        wait = 60;  
    } else {  
    	flag="true";
    	o.css("background","#ccc");
    	o.attr("disabled", true);  
    	o.val("重新发送("+wait+")");  
        wait--;  
        setTimeout(function() {  
        	getCode(o);
        },  1000);  
    }  
}
//向手机发送验证码
function sendVerificationCode(){
	$.JsonSRpc(baseUrl+ "/sendVerificationCodeUn",{openid:openid,ori_phone:$("#reservePhone").val()},function(data) {
		if(data.errcode=="9999"){
			message(data.errmsg);
			//发送手机验证码失败，倒计时清0
	        wait = 0; 
		}
	});
};
	
	
//校验非空、手机位数等简单前台校验
function switchCase(){
	//console.log(baseUrl);
	if(idTypeCheck() && idNumberCheck() && reservePhoneCheck()
			&& validateCodeCheck() ){
		return true;
	}
	return false;
}
//证件类型检验
function idTypeCheck(){
	var idType=$("#idType").val();
	if(idType=="" || idType==undefined){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码");
		timeDifference(beforetime);
		return false;
	}
	return true;
}
//证件号码校验
function idNumberCheck(){
	var idType=$("#idType").val();
	var idNumber=$("#idNumber").val();
	if(idNumber=="" || idNumber==undefined){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码");
		timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}
	idNumber = idNumber.replace("x", "X");
	//身份证号校验
	if(idType=="1" && !regResidentID.test(idNumber)){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码");
		timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}
	//临时身份证校验
	if(idType=="2" && !regResidentID.test(idNumber)){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码");
		timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}
	//其他证件规则校验
	
	return true;
}
//预留手机号校验
function reservePhoneCheck(){
	var reservePhone=$("#reservePhone").val();
	if(reservePhone=="" || reservePhone==undefined){
		var beforetime=getNowDate();
		message("请输入预留手机号");
		timeDifference(beforetime);
		//$("#reservePhone").focus();
		return false;
	}
	if(!regPhone.test(reservePhone)){
		var beforetime=getNowDate();
		message("请输入正确的手机号");
		timeDifference(beforetime);
		//$("#reservePhone").focus();
		return false;
	}
	return true;
}

//验证码校验
function validateCodeCheck(){
	var validateCode=$("#validateCode").val();
	if(validateCode=="" || validateCode==undefined){
		var beforetime=getNowDate();
		message("请输入验证码");
		timeDifference(beforetime);
		$("#reservePhone").focus();
		return false;
	}
	return true;
}

//解绑账号
function bindAccount(){
	$.showLoadMsg("提交中，请稍后...");
	var inputData={};
	inputData.openId=openid;
	inputData.idType=$("#idType").val();//证件类型
	inputData.idNumber=$("#idNumber").val();//证件号码
	inputData.idNumber = inputData.idNumber.replace("x", "X");
	inputData.reservePhone=$("#reservePhone").val();//预留手机号
	inputData.validateCode=$("#validateCode").val();//客户输入的验证码
	$.JsonSRpc(baseUrl+ "/removeBindAccount",inputData,function(data) {
		$.hideLoadMsg();
		//console.log(data);
		if(data.errcode=="0"){
			//返回贷后主页前的空白页重新进行绑定状态判断
			message("解绑成功", function(flag){
				window.location.href="myLoan.html";
			});
		}	
		else{
			var beforetime=getNowDate();
			message(data.errmsg);
			timeDifference(beforetime);
			return;
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
				htmlInit();
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}



