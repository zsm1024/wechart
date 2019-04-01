
$(function(){
	
	localStorage.clear();
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
				localStorage.setItem("openid", openid);
				//alert(openid);
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
})

// 验证手机号
	function isPhoneNo(phone) { 
	 var pattern = /^1[3465789]\d{9}$/; 
	 return pattern.test(phone); 
	}
function phone(){
 		if ($.trim($('#myPhone').val()).length == 0) {
			$('#myPhone').css("border"," 1px solid red");
			message('手机号没有输入;\n');		
			return false;
		}else {
		  if(isPhoneNo($.trim($('#myPhone').val())) == false) {	   
		   	$('#myPhone').css("border","1px solid red");	   	
		   	message('请输入正确的手机号;\n');	   
		   	return false;
		  }
		  $('#myPhone').css("border"," 1px solid rgba(0, 0, 0, .2)");	
		}				
}
//$('#myPhone').blur(function(){
//		phone();
//	});
//$('#check_code').blur(function(){
//	 var check_code = $("#check_code").val();
//		if(check_code==undefined||check_code==""){
//	        message("验证码不能为空;\n");	       
//	       	$('#check_code').css("border"," 1px solid red");	       	
//	       
//	    }else if(check_code.length<6){
//	    	message("您的验证码小于6位请重新输入;\n");
//	    	$('#check_code').css("border"," 1px solid red");	    	
//	    	return false;
//	    }else{
//	    	$('#check_code').css("border"," 1px solid rgba(0, 0, 0, .2)");
//	    }   
//});
function formValidate(chosetype) {
	//console.log(i)
	//alert(chosetype);
	var str="";
	// 判断手机号码
	 if ($.trim($('#myPhone').val()).length == 0) { 
	  	str += '手机号没有输入;\n';
	  //$('#phone').focus();
		$('#myPhone').css("border"," 1px solid red");
	 }else {
	  if(isPhoneNo($.trim($('#myPhone').val())) == false) {
	   		str += '请输入正确的手机号;\n';
	   	$('#myPhone').css("border"," 1px solid red");
	  }
//	  	$("#phone").parent().find("label").css("color","#000");
	 }
	var check_code = $("#check_code").val();
		if(check_code==undefined||check_code==""){
	        str+="验证码不能为空;\n";	       
//	        $("#check_code").addClass("warning");	      
	    }else if(check_code.length<6){
	    	str+="您的验证码小于6位请重新输入;\n";
	    }	   		
	if(str != ''){		 
	  message(str);
	  
	 } else {
		 $.showLoadMsg("加载中，请稍后...");
		var date=convertDate(new Date());
		//message(date);
		param.timestamp=date;
		var data="{'phone':"+$.trim($('#myPhone').val())+",'checkCode':'"+check_code+"'}";
		param.data=data;
		
		var s="";
		data=key+data+date+key;
		for(var i=0;i<data.length;i++)
		{
			s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
		}
		//message(s);
		var signcode=$.md5(s);

		param.code='checkVerification';
		param.signcode=signcode;
		//message(interfaceUrl);
		
		$.JsonSRpc(interfaceUrl, param, function(res){
			//message(1);
			//message(JSON.stringify());
			if(res)
			{
				if(res.resultCode=='0000')
				{
					if(res.resultData.result=='0')
					{
						var myPhone=$("#myPhone").val();
						localStorage.setItem("UserPhone",myPhone);   
						if(chosetype==1){
							window.location.href="scan.html";
						}else{
							window.location.href="chooseCity.html";
						}
						$.hideLoadMsg();
					}
					else
					{
						message("验证码错误！");
						$.hideLoadMsg();
						
					}
					
				}else{
					message("验证码错误！");
					$.hideLoadMsg();
					
				}
			}else{
				message("验证码错误！");
				$.hideLoadMsg();
				
			}
		});
		 
		
	}

}




function getCheckCodes(){
	debugger;
	var objs =$(".get-code");
	
	var phone = $("#myPhone").val();
		if ($.trim($('#myPhone').val()).length == 0) {
			$('#myPhone').css("border"," 1px solid red");
			message('手机号没有输入\n');
			return false;
		}
		if(isPhoneNo($.trim($('#myPhone').val())) == false) {
		   	$('#myPhone').css("border","1px solid red");	   	
		   	message('手机号码不合法\n');
		   	return false;
		 	}
		$('#myPhone').css("border"," 1px solid rgba(0, 0, 0, .2)");
		
		
		countDown(objs);
		message("发送成功！");
		var date=convertDate(new Date());
		param.timestamp=date;
		var data="{'phone':"+phone+"}";
		
		param.data=data;
		
		var s="";
		data=key+data+date+key;
		for(var i=0;i<data.length;i++)
		{
			s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
		}
		var signcode=$.md5(s);

		param.code='SendVerification';
		param.signcode=signcode;
		$.JsonSRpc(interfaceUrl, param, function(res){
			//alert()
			if(res)
			{
				if(res.resultCode=='0000')
				{
					if(res.resultData.result=='0'){
						
					}
					else if(res.resultData.result=='1')
					{
						message("每小时只能发送6次！");
					}
					
				}else{
					message(res.resultData);
				}
			}else{
				message("发送异常！");
			}
		});
		//showButton();
}
//获取验证码
var wait = 60;
function countDown(o){
	if (wait == 0) { 
		flag="false";
		o.css("background","#fff");
		o.on("click",function(){
			getCheckCodes();
		});
		o.removeAttr("disabled");    
		o.html("获取验证码") ;
        wait = 60;  
    } else {  
    	flag="true";
    	o.css("background","gray");
    	o.off("click");
    	o.attr("disabled", true);  
    	o.html("重新发送("+wait+")");  
        wait--; 
        setTimeout(function() {  
        	countDown(o);
        },  1000);  
    }  
}

//if($(".get-code").attr("disabled")==true){
//	$(this).unbind("click");
//}