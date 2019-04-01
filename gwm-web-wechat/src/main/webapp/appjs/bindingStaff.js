//长城滨银汽车员工绑定
//页面初始化
//验证码倒计时60秒
//var wait=60;
//产生6位随机验证码数字
//var randomNum="";
//判断验证码是否失效
//var flag="false";
//手机有效性正则，支持13、14、15、17、18开头的手机号(2016年)。
var regPhone=/^1[3|4|5|7|8][0-9]\d{8}$/;
//数字校验
var regNumber=/^[0-9]*$/;
//身份证正则校验
var regResidentID = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
//判断客户是第一次点击获取验证码？ 弹出不同提示信息
//var isFirst="true";
var openid="";
//var openid="o5iChju4y1zdq0NKheVA5ilu3r1g";

$(function (){
	openid=sessionStorage.getItem("openid");
	console.log(openid);
	if(openid == null || openid=="" || undefined == openid ){
		var code = getParam("code");
		if(undefined == code || "" == code || code== null){
			message("信息获取失败，请重新进入页面",function(flag){
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

});
function checkIdNumber(){
	idNumberCheck();
}
function checkPhone(){
	staffPhoneCheck();
}

//校验非空、手机位数等简单前台校验
function switchCase(){
	//console.log(baseUrl);
	if(staffTypeCheck() && idNumberCheck() && staffPhoneCheck()){
		return true;
	}
	return false;
}
//员工类型检验
function staffTypeCheck(){
	var staffType=$("#staffType").val();
	if(staffType=="" || staffType==undefined){
		//var beforetime=getNowDate();
		message("请选择员工类型");
		//timeDifference(beforetime);
		return false;
	}
	return true;
}
//证件号码校验
function idNumberCheck(){
	var staffType=$("#staffType").val();
	var idNumber=$("#idNumber").val();
	if(idNumber=="" || idNumber==undefined){
		message("请输入正确的身份证号码", function(flag){
		});
		return false;
	}
	idNumber = idNumber.replace("x", "X");
	//身份证号校验
	if(!regResidentID.test(idNumber)){

		message("请输入正确的身份证号码", function(flag){
			//$("#idNumber").focus();
		});
		//timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}

	//其他证件规则校验
	
	return true;
}
//员工手机号校验
function staffPhoneCheck(){
	var staffPhone=$("#staffPhone").val();
	if(staffPhone=="" || staffPhone==undefined){
		//var beforetime=getNowDate();
		message("请输入绑定手机号", function(flag){
			//$("#staffPhone").focus();
		});
		return false;
	}
	if(!regPhone.test(staffPhone)){
		//var beforetime=getNowDate();
		message("请输入正确的手机号", function(flag){
			//$("#staffPhone").focus();
		});

		return false;
	}
	return true;
}


$("#btnBinding").on('click',function(){
	if(switchCase()){
		bindStaff();
	}
	
});
//绑定
function bindStaff(){
	$.showLoadMsg("提交中，请稍后...");
	var inputData={};
	inputData.openId=openid;
	console.log(openid);
	inputData.staffType=$("#staffType").val();//员工类型
    inputData.staffName=$("#staffName").val();//员工姓名
	inputData.idNumber=$("#idNumber").val();//证件号码
    inputData.jobNumber=$("#jobNumber").val();//员工工号
	inputData.idNumber = inputData.idNumber.replace("x", "X");
	inputData.staffPhone=$("#staffPhone").val();//员工手机号

	var staffType = $("#staffType").val();
	if(staffType == "1"|| staffType == "2"){
		message("还未开通，请稍后再绑定");
	}else if(staffType == "3"){

        $.JsonSRpc(baseUrl+"/bindStaff",inputData,function(data){
        	$.hideLoadMsg();
            if(data.errmsg!=null&&data.errcode!=""){
                message(data.errmsg);
            }
        });
	}else{
		return "员工类型不正确";
	}
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


