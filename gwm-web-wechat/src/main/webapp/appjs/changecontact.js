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
						message("信息获取失败!", function(flag){
							wx.closeWindow();
						});
						return;
					}else{
						sessionStorage.setItem("openid", openid);
						htmlInit();
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
	}else{
		htmlInit();
	}
});

//页面初始化
function htmlInit(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//console.log(data);
		$.hideLoadMsg();
		if(data.bindingJudg=="false"){
			message("您还没有绑定任何合同", function(flag){
				//wx.closeWindow();
				window.location.href = "./myLoan.html";
			});
		}
	});
	$(".get-code").click(function(){
		getCode();
	});
	$(".submit-btn").click(function(){
		submitChange();
	});
}

/*获取验证码*/
function getCode(){
	var ori_phone = $("#ori_phone").val().trim();
	var new_phone = $("#new_phone").val().trim();
	if(!checkMobile(ori_phone)){
		message("请输入正确的原手机号！");
		return; 
	}
	$.JsonSRpc(baseUrl+"/getcheckcode", {ori_phone:ori_phone,openid:openid,new_phone:new_phone}, function(data){
		if(data.errcode&&"0"==data.errcode){
			message("验证码已发送");
			var obj = $(".get-code");
			countDown(obj);
		}else{
			message(data.errmsg);
		}
	});
}

/*更改联系方式*/
function submitChange(){
	var ori_phone = $("#ori_phone").val().trim();
	var new_phone = $("#new_phone").val().trim();
	var check_code = $("#check_code").val().trim();
	if(!checkMobile(new_phone)){
		message("请输入正确的新手机号码！");
		return;
	}
	$.showLoadMsg("申请处理中，请稍后...");
	$.JsonSRpc(baseUrl+"/changecontactmode", {ori_phone:ori_phone,openid:openid,new_phone:new_phone,check_code:check_code}, function(data){
		$.hideLoadMsg();
		if(data.errcode&&"0"==data.errcode){
			window.location.href = "./changecontractcfm.html";
		}else{
			message(data.errmsg);
			return;
		}
	});
}
//获取验证码
var wait = 60;
function countDown(o){
	if (wait == 0) { 
		flag="false";
		o.css("background","#c8161d");
		o.removeAttr("disabled");    
		o.html("获取验证码") ;
        wait = 60;  
    } else {  
    	flag="true";
    	o.css("background","gray");
    	o.attr("disabled", true);  
    	o.html("重新发送("+wait+")");  
        wait--;  
        setTimeout(function() {  
        	countDown(o);
        },  1000);  
    }  
}