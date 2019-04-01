$(function(){
	var ssouser="";
	var arrStr = document.cookie.split("; ");  
//	for(var i = 0;i < arrStr.length;i ++){  
//	    var temp = arrStr[i].split("=");  
//	    if(temp[0] == "SSOUSER") {  
//	      ssouser=unescape(temp[1]);
//	    }  
//	}
	var Request=new urlSearch(); //URL实例化
	var page="";
	if(Request.target!=undefined){//URL带参数做如下处理
		page=decodeURI(decodeURI(Request.target));
	}else{
		alert("跳转参数为空，无法打开相关界面");
		return ;
	}
	if(ssouser==""||ssouser==undefined){
		//alert("无法获取校验参数，请重新登录后，再次尝试！");
		ssouser=decodeURI(decodeURI(Request.userToken));
		b(ssouser);
		//return;
	}

//	if(page=="applyInfo"){
//		window.location.href=localUrl+"gwm-web-manager/loan_applylist.jsp";
//	}
//	if(page=="earlyrepay"){
//		window.location.href=localUrl+"gwm-web-manager/earlyrepay.jsp";
//	}
//	if(page=="feedback"){
//		window.location.href=localUrl+"gwm-web-manager/feedback.jsp";
//	}
//	if(page=="hotcar_list"){
//		window.location.href=localUrl+"gwm-web-manager/hotcar_list.jsp";
//	}
//	if(page=="msgcontent"){
//		window.location.href=localUrl+"gwm-web-manager/msgcontent.jsp";
//	}
//	if(page=="contactchange"){
//		window.location.href=localUrl+"gwm-web-manager/contactchange.jsp";
//	}
	var url=basePath+"/verification";
	$.JsonSRpc(url,{
		ssouser:ssouser
	},
	function(data){
		if(data[0].error!="0000"){
			alert(data[0].error_msg);
			return;
		}else{
			//alert(page);
			if(page=="applyInfo"){
				a(data[0].acc_token,data[0].userid);
				//alert(localUrl+"gwm-web-manager/loan_applylist.jsp");
				window.location.href=localUrl+"gwm-web-manager/loan_applylist.jsp";
			}
			if(page=="earlyrepay"){
				a(data[0].acc_token,data[0].userid);
				window.location.href=localUrl+"gwm-web-manager/earlyrepay.jsp";
			}
			if(page=="feedback"){
				a(data[0].acc_token,data[0].userid);
				window.location.href=localUrl+"gwm-web-manager/feedback.jsp";
			}
			if(page=="hotcar_list"){
				a(data[0].acc_token,data[0].userid);
				window.location.href=localUrl+"gwm-web-manager/hotcar_list.jsp";
			}
			if(page=="msgcontent"){
				a(data[0].acc_token,data[0].userid);
				window.location.href=localUrl+"gwm-web-manager/msgcontent.jsp";
			}
			if(page=="contactchange"){
				a(data[0].acc_token,data[0].userid);
				window.location.href=localUrl+"gwm-web-manager/contactchange.jsp";
			}
		}
	});
});

function a(token,userid){
	var str = "access_token"+ "=" + escape(token);  
	str += "; path=/; domain=.wechat.gwmfc.com";  
    document.cookie = str;
    str="";
    str += "userid=" + userid+"; path=/; domain=.wechat.gwmfc.com"; 
    document.cookie = str;
}

function b(user){
	var str = "SSOUSER"+ "=" + escape(user);  
	str += "; path=/; domain=.wechat.gwmfc.com";  
    document.cookie = str;
}

function urlSearch(){
    var name,value;
    var str=location.href; //取得整个地址栏
    var num=str.indexOf("?");
    str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]

    var arr=str.split("&"); //各个参数放到数组里
    for(var i=0;i < arr.length;i++){
        num=arr[i].indexOf("=");
        if(num>0){
            name=arr[i].substring(0,num);
            value=arr[i].substr(num+1);
            this[name]=value;
        }
    }
}