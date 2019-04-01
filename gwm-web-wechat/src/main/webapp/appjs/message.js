$(function(){
	openid = sessionStorage.openid;
	msg_type = sessionStorage.msg_type;
	if(undefined==openid||""==openid){
		message("获取用户信息失败", function(flag){
			wx.closeWindow();
		});
		return ;
	}
	if(undefined == msg_type){
		message("消息查询失败", function(flag){
			wx.closeWindow();
		});
		history.go(-1);
	}
	if("1"==msg_type){
		$(".title").html("通知公告");
	}
	if("2"==msg_type){
		$(".title").html("还款通知");
	}
	if("3"==msg_type){
		$(".title").html("逾期提醒");
	}
	if("4"==msg_type){
		$(".title").html("系统信息");
	}
	$.showLoadMsg("页面加载中，请稍候...");
	$.JsonSRpc(baseUrl+"/wxgetmymsg", {openid:openid, msg_type:msg_type}, function(data){
		$.hideLoadMsg();
		if(data.errcode&&"0"==data.errcode){
			var msgs = data.msgs;
			var ms = JSON.parse(msgs);
			var item = '';
			for(var i = 0; i < ms.length; i++){
				var cont = ms[i].msg_cont.replace(/\n/g,"<BR>");
				cont = ms[i].msg_title + ':<BR>' + '<BR>' + cont;
				item += '<li>' +
					'<i></i>' +
					'<span>'+cont+'</span>' +
					'</li>';
			}
			$("#msg_list").html(item);
			if(ms.length == 0){
				var th = document.documentElement.clientHeight/2 -14*0.12*20;
				var tip = "<div class='tips' style='margin-top:"+th+"px;text-align:center;' valign='middle'>"
					+"暂时没有相关消息"
					+"</div>";
				$(document.body).append(tip);
			}
		}
	});
});