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
						message("信息获取失败!",function(flag){
							wx.closeWindow();
						});
					}else{
						sessionStorage.setItem("openid", openid);
						htmlInit();
					}
				}else{
					message("获取用户信息失败！",function(flag){
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

function htmlInit(){
	$.showLoadMsg("页面加载中，请稍候...");
	$.JsonSRpc(baseUrl+"/wxgetmymsgtype", {openid:openid}, function(data){
		$.hideLoadMsg();
		if(data.errcode&&"0"==data.errcode){
			//通知公告
			var notification = data.notification;
			var ntf = JSON.parse(notification);
			if(ntf.num=="1"){
				var msg_date = ntf.msg_date;
				var status = ntf.status;
				msg_date = msg_date.substring(0, 4)+"-"+msg_date.substring(4, 6)+"-"+msg_date.substring(6, 8);
				$("#notification_date").html(msg_date);
				if("0"==status){
					$("#notification_icon").addClass("new");
				}
			}
			//还款消息
			var repayment = data.repayment;
			var rpm = JSON.parse(repayment);
			if(rpm.num=="1"){
				var msg_date = rpm.msg_date;
				var status = rpm.status;
				msg_date = msg_date.substring(0, 4)+"-"+msg_date.substring(4, 6)+"-"+msg_date.substring(6, 8);
				$("#repayment_date").html(msg_date);
				if("0"==status){
					$("#repayment_icon").addClass("new");
				}
			}
			//逾期提醒
			var overdue = data.overdue;
			var ovd = JSON.parse(overdue);
			if(ovd.num=="1"){
				var msg_date = ovd.msg_date;
				var status = ovd.status;
				msg_date = msg_date.substring(0, 4)+"-"+msg_date.substring(4, 6)+"-"+msg_date.substring(6, 8);
				$("#overdue_date").html(msg_date);
				if("0"==status){
					$("#overdue_icon").addClass("new");
				}
			}
			//系统消息
			var system = data.system;
			var sys = JSON.parse(system);
			if(sys.num=="1"){
				var msg_date = sys.msg_date;
				var status = sys.status;
				msg_date = msg_date.substring(0, 4)+"-"+msg_date.substring(4, 6)+"-"+msg_date.substring(6, 8);
				$("#system_date").html(msg_date);
				if("0"==status){
					$("#system_icon").addClass("new");
				}
			}
		}
	});
}

function clickItem(type){
	if(undefined==type){
		return;
	}
	sessionStorage.msg_type=type;
	window.location.href=baseUrl+"/message.html";
}