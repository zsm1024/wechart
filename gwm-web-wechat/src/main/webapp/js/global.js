var pathname=window.location.pathname;
var basePath = window.location.protocol + "//" + window.location.host + pathname.substring(0,pathname.substring(1).indexOf("/")+1);
var baseUrl = basePath;
//var reqUrl = baseUrl + "/dhc/sys/main.do";
//var verifyCodeUrl = baseUrl + "/dhc/sys/checkcode.do";
//var pwboardUrl = baseUrl + "/dhc/sys/pwboard.do";
var cmsUrl_active="http://gwmfc.com/wx_newactice.html";
var cmsUrl_active1="http://gwmfc.com/wx_activeAugust.html";
//var cmsUrl_active2="http://gwmfc.com/wx_activeVV5.html";
var cmsUrl_active2="http://gwmfc.com/wx_spring.html";
var cmsUrl_active4="http://gwmfc.com/wx_active_spring.html";
var cmsUrl_active5="http://gwmfc.com/wx_activeblueH6.html";
var cmsUrl_active6="http://gwmfc.com/wx_Midsummer.html";
var cmsUrl_active8="http://gwmfc.com/wx_activeH8H9.html";
var cmsUrl_active9="http://gwmfc.com/wx_activeM6.html";
//var cmsUrl_active9="http://gwmfc.com/wx_spring.html";

var cmsUrl_question="http://gwmfc.com/wx_question.html";
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
//隐藏导航栏
function hideBottom(){
	$(".tab-bar").hide();
	$("body").css("padding","0");
}
//显示导航栏
function showBottom(){
	$(".tab-bar").show();
	$("body").css("padding-bottom",".55rem");
}
//判断是否为空
function isNull(v){
	if(v==null||v==undefined||v==""||v=="null"||v=="undefined"){
		return true;
	} 
	return false;
}

//返回点击
function backClick(){
	if(history.length > 1){
		history.go(-1);
	}else{
		wx.closeWindow();
	}
}

$.JsonSRpc = function(url, data, succFunc, error, async) {
    data = data || {};
    succFunc = succFunc || function() {
    };
    error = error || function() {
    };
    if(data.only9999==true)
    	{}
    else
    	//$.showLoadMsg("");
    $.ajax({
        url: url,
        type: "POST",
       // datatype: "JSONP",
        timeout: 180000,
        data: data,
        async: async != false,
        success: function(obj) {
//            $.hideLoadMsg();
            if (typeof (obj) == "object") {
                if (obj.successful !== false) {
                    succFunc(obj);
                } else {
                    message(obj.resultHint);
                }
            } else {
                var response = $.parseJSON(obj);
                succFunc(response);
            }
        },
        complete: error
    });
};

/**
 * 根据key值获取url参数value
 * @param id
 * @returns
 */
function getParam(id){
    var href=window.location.href+"";
    var index=href.indexOf("?");
    if(index<0)
        return null;
    href=href.substring(index+1);
    var params=href.split("&");
    var obj={};
    for(var i=0;i<params.length;i++){
        var param=params[i];
        index=param.indexOf("=");
        if(index<0)
            continue;
        obj[param.substring(0,index)]=param.substring(index+1);
    }
    return obj[id];
 }

/**
 * 格式化金额
 * @param s 数据
 * @param n 小数位数（会四舍五入）
 * @returns {String}
 */
function fmoney(s, n)   
{   
   n = n > 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse(),   
   r = s.split(".")[1];   
   t = "";   
   for(var i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + "." + r;   
}

/**
 * 校验手机号(1开头11位数字)
 * @param str
 * @returns {Boolean}
 */
function checkMobile(str) {
   var re = /^1\d{10}$/;
   if (re.test(str)) {
       return true;
   } else {
       return false;
   }
}

$.hideLoadMsg = function() {
    $("#loadMask").remove();
    $("#loadMsg").remove();
};
$.showLoadMsg = function(showText) {
    $.hideLoadMsg();
    $(document.body).append("<div id='loadMask' style='position:fixed;left:0;right:0;top:0;bottom:0;background-color:#000;z-index:1111;filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;'></div>");
    $(document.body).append("<div id='loadMsg' style='position:absolute;font-size:12px;border:0px solid #0d68c5;background-color:#000;background:rgba(0,0,0,0.5);border-radius:5px;padding:5px;z-index: 1112;text-align:center'><div>" + showText + "</div></div>");
    //$("#loadMsg").append("<img src='image/loading.gif'/ style='width:26px;height:26px;margin:10px;'>");
    $("#loadMsg").css("left", ($("#loadMask").width() - $("#loadMsg").width()) / 2);
//    $("#loadMsg").css("top", ((document.body.scrollHeight-$(window).height())+($(window).height() - $("#msgContent").height()) / 2));
    $("#loadMsg").css("top", "45%");
    $("#loadMsg").css("position", "fixed");
};

function getDFT(v) {
    var d = new Date(parseInt(v));
    return d.getFullYear() + "-" + fmn(d.getMonth() + 1) + "-" + fmn(d.getDate()) + " " + fmn(d.getHours()) + ":" + fmn(d.getMinutes()) + ":" + fmn(d.getSeconds());
}
function getTFD(v, bool) {
    var d = new Date();
    d.setYear(v.substring(0, 4));
    d.setMonth(parseInt(v.substring(5, 7)) - 1);
    d.setDate(v.substring(8, 10));
    if (bool)
        d.setHours(24);
    else
        d.setHours(0);
    d.setMinutes(0);
    d.setSeconds(0);
    return d.getTime();
}
function fmn(num) {
    if ((num + "").length === 1) {
        return "0" + num;
    }
    return num;
}

var messageList = [];
var show = false;
setInterval(function() {
    if (show) {
        return;
    }
    if (messageList.length > 0) {
        var obj = messageList.splice(0, 1)[0];
        show = true;
        createMessage(obj);
    }
}, 10);

function message(html, func, flag) {
	if(html && func && flag){
		messageList.push({html: html, func: func, flag:flag});
	}else if (html && func && !flag) {
        messageList.push({html: html, func: func});
    } else if (html && !func && !flag) {
        messageList.push({html: html});
    }
}

function createMessage(v) {
    $(document.body).append("<div class='mask-wrap'></div>");
    $(document.body).append();
    var msgBox = "<div class='pop-tips'>"
    		+"<div class='txt'>"+v.html+"</div>"
    		+"<div class='flex'>"
    		+"<button id='_msg_btn_true'>确定</button>";
    if(v.flag){
    	msgBox += "<button id='_msg_btn_false'>取消</button>";
    }
    msgBox += "</div>"
    		+"</div>";
    $(document.body).append(msgBox);
	if (v.func) {
		$("#_msg_btn_true").click(function(){
			$(".mask-wrap").hide();
			$(".pop-tips").hide();
			$("body").removeAttr("style");
			$(".mask-wrap").remove();
			$(".pop-tips").remove();
			v.func(true);
			show=false;
		});
		$("#_msg_btn_false").click(function(){
			$(".mask-wrap").hide();
			$(".pop-tips").hide();
			$("body").removeAttr("style");
			$(".mask-wrap").remove();
			$(".pop-tips").remove();
			v.func(false);
			show=false;
		});
	} else {
		$("#_msg_btn_true").click(function(){
			$(".mask-wrap").hide();
			$(".pop-tips").hide();
			$("body").removeAttr("style");
			$(".mask-wrap").remove();
			$(".pop-tips").remove();
			show=false;
		});
		$("#_msg_btn_false").click(function(){
			$(".mask-wrap").hide();
			$(".pop-tips").hide();
			$("body").removeAttr("style");
			$(".mask-wrap").remove();
			$(".pop-tips").remove();
			show=false;
		});
	}
	$(".mask-wrap").show();
	$(".pop-tips").show();
	$("body").css({"height":"100%","overflow":"hidden"});
}

//检验按钮是否可用
function checkIsUseed(obj){
	var tmp=obj.attr('class');
	if((tmp+"").indexOf("butdisable")==-1){
		return true;
	}
	return false;
}
function back(){
	if(WeixinJSBridge)WeixinJSBridge.call('closeWindow');
}

//$(function(){
//	if(!is_weixn()){
//		//window.location=basePath+"/mobilePage/fx_error.html";
//		return;
//	}
//});

function is_weixn(){  
    var ua = navigator.userAgent.toLowerCase();  
    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
        return true;  
    } else {  
        return false;  
    }  
} 

function errorMessage(){
	message("系统升级中，如有问题烦请致电400 6527 606。");
}