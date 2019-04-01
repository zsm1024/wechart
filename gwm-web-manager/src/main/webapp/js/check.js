//所有后管页面必须引入次js，进行单页面进入验证
var userid="zxl";
var ssouser="";
var access_token="";
$(function(){
	var arrStr = document.cookie.split("; ");  
	for(var i = 0;i < arrStr.length;i ++){  
	    var temp = arrStr[i].split("=");  
	    if(temp[0] == "SSOUSER") {  
	      ssouser=unescape(temp[1]);
	    }  
	    if(temp[0] == "access_token"){
	    	access_token=unescape(temp[1]);
	    }
	    if(temp[0]=="userid"){
	    	userid=unescape(temp[1]);
	    }
	}
	if(ssouser==undefined||access_token==undefined||userid==undefined||ssouser==""||access_token==""||userid==""){
		alert("用户登录过期，请重新登录");
		window.location.href=hfUrl+"ec-order/login.jsp";
		return;
	}
	var url=basePath+"/checkUser";
	$.JsonSRpc(url,{
		ssouser:ssouser,
		access_token:access_token
	},
	function(data){
		if(data[0].error!="0000"){
			alert(data[0].error_msg);
			window.location.href=hfUrl+"ec-order/login.jsp";
			return;
		}		
	});
});