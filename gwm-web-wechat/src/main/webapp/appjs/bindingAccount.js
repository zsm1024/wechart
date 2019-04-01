//长城滨银汽车我的贷款
//页面初始化
//验证码倒计时60秒
var wait=60;
//产生6位随机验证码数字
//var randomNum="";
//判断验证码是否失效
var flag="false";
//手机有效性正则，支持13、14、15、17、18开头的手机号(2016年)。
var regPhone=/^1[3|4|5|7|8|6|9][0-9]\d{8}$/;
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
	
	$("#btnOk").click(function(){
		if(switchCase()){
			bindAccount();
		}
	});
	//同意协议书
	$("#agree").click(function(){
		if($("#agree").get(0).checked){
			$("#btnOk").removeClass("disabled");
		}
		else{
			$("#btnOk").removeClass("disabled");
			$("#btnOk").addClass("disabled");
		}
	});
	//获取验证码
	$("#getCode").click(function(){
		//点击获取验证码后县校验预留手机号
		if(reservePhoneCheck()){
			message("验证码已发送");
			isFirst="false";
			//获取对象
			var o=$("#getCode");
			//倒计时
			getCode(o);
			//向手机发送验证码
			sendVerificationCode();
		}
	});
	$("#protocol").click(function(event){
		window.location.href="bindingProtocol.html";
	});
});
function checkIdNumber(){
	idNumberCheck();
}
function checkPhone(){
	reservePhoneCheck();
}
//计算弹出提示框消耗的时间，反馈给验证码倒计时
function timeDifference(beforetime){
	var aftertime=getNowDate();
	if(flag=="true"){
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
	$.JsonSRpc(baseUrl+ "/sendVerificationCode",{openid:openid,ori_phone:$("#reservePhone").val()},function(data) {
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
			&& validateCodeCheck() && agreeCheck() ){
		return true;
	}
	return false;
}
//证件类型检验
function idTypeCheck(){
	var idType=$("#idType").val();
	if(idType=="" || idType==undefined){
		var beforetime=getNowDate();
		message("请选择证件类型");
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
		message("请输入正确的身份证号码", function(flag){
			//$("#idNumber").focus();
		});
		timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}
	idNumber = idNumber.replace("x", "X");
	//身份证号校验
	if(idType=="1" && !regResidentID.test(idNumber)){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码", function(flag){
			//$("#idNumber").focus();
		});
		timeDifference(beforetime);
		//$("#idNumber").focus();
		return false;
	}
	//临时身份证校验
	if(idType=="2" && !regResidentID.test(idNumber)){
		var beforetime=getNowDate();
		message("请输入正确的身份证号码", function(flag){
			//$("#idNumber").focus();
		});
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
		message("请输入预留手机号", function(flag){
			//$("#reservePhone").focus();
		});
		timeDifference(beforetime);
		//$("#reservePhone").focus();
		return false;
	}
	if(!regPhone.test(reservePhone)){
		var beforetime=getNowDate();
		message("请输入正确的手机号", function(flag){
			//$("#reservePhone").focus();
		});
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
//绑定协议书
function agreeCheck(){
	if(!$("#agree").get(0).checked) {
		var beforetime=getNowDate();
		message("请阅读协议书选择同意");
		timeDifference(beforetime);
		return false;
	}
	return true;
}

//绑定
function bindAccount(){
	$.showLoadMsg("提交中，请稍后...");
	var inputData={};
	inputData.openId=openid;
	inputData.idType=$("#idType").val();//证件类型
	inputData.idNumber=$("#idNumber").val();//证件号码
	inputData.idNumber = inputData.idNumber.replace("x", "X");
	inputData.reservePhone=$("#reservePhone").val();//预留手机号
	inputData.validateCode=$("#validateCode").val();//客户输入的验证码
	$.JsonSRpc(baseUrl+ "/bindAccount",inputData,function(data) {
		$.hideLoadMsg();
		//console.log(data);
		if(data.errcode=="0"){
			//下一步查询合同数量
			$.showLoadMsg("正在查询合同信息，请稍后...")
			$.JsonSRpc(baseUrl+"/getcontractinfo", 
					{openid:openid}, function(data){
						$.hideLoadMsg();
						if(data.errcode&&data.errcode!="0"){
							message(data.errmsg);
							return;
						}else{
							var contracts = JSON.parse(data.contracts);
							if(contracts.length > 1){
								window.location.href="selectcontract.html";
							}else if(contracts.length == 0){
								message("未查到合同信息，请检查您的信息！");
								return;
							}else{
								$.showLoadMsg("正在完成绑定，请稍后...");
								var contract_id = contracts[0].contract_id;
								var contract_nbr = contracts[0].contract_nbr;
								var business_partner_name = contracts[0].business_partner_name;
								$.JsonSRpc(baseUrl+"/wxchangecontract", {openid:openid,contract_id:contract_id, contract_nbr:contract_nbr,business_partner_name:business_partner_name}, function(data){
									$.hideLoadMsg();
									if(data.errcode&&data.errcode!="0"){
										message(data.errmsg);
										return;
									}else{
										message("您已成功绑定合同", function(flag){
											window.location.href = "./myLoan.html";
										});
									}
								});
							}
						}
					}
			);
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
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}


