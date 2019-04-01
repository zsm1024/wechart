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
	var curr = 0;
	$(".question li input").click(function(){
		var q_index = $(this).closest("div").index();
		var c_index = $(this).parent("li").index();
		if(q_index == 8){
			$(".edcs").hide();
			$(".edcs-result").show();
			quotacalc();
		}
		$(this).closest("div").delay(100).fadeOut().next().delay(400).fadeIn();
		$(".steps li").eq(q_index).removeClass().addClass("sico"+q_index+c_index+" on");
		if(q_index>=curr){
			curr=q_index;
			var h_bar = 0.51 * (curr+1);
			$(".steps .bar .scale").css("height",h_bar+"rem");
			$(".steps li").eq(q_index).next().addClass("on");
		}
	});
	$(".steps li").click(function(){
		var s_index = $(this).index()-1;
		if(s_index<=curr){
		$(".question>div").eq($(this).index()).show().siblings().hide();
		}
	});
	$("#sub_btn").removeClass("disabled");
	$("#sub_btn").click(function(){
		applyNow();
	}); 
}
//在线申请链接
function applyNow(){
	window.location.href = "./apply.html";
}
//额度测算
function quotacalc(){
	//	$("input[name='sex']:checked").val();
	var score = 0;
	var family = 0;
	var min = 0;
	var period = 0;
	var flag = true;
	for(var i = 1; i <= 9; i++){
		var str = "input[name='q"+i+"']:checked";
		var val = $(str).val();
		score += getScore(i, parseInt(val));
		if(i == 2){
			if(val == "1"){
				flag = true; //已婚
			}
			else{
				flag = false; //未婚
			}
		}
		if(i == 4){
			switch(val){
			case "1":
				min = flag?3500*2:3500;
				break;
			case "2":
				min = flag?2500*2:2500;
				break;
			case "3":
				min = flag?2000*2:2000;
				break;
			}
		}
		if(i == 6){
			switch(val){
			case "1":
				period = 12;
				break;
			case "2":
				period = 24;
				break;
			case "3":
				period = 36;
				break;
			case "4":
				period = 48;
				break;
			}
		}
		if(i == 9){
			switch(val){
			case "1":
				family = 5000;
				break;
			case "2":
				family = 8000;
				break;
			case "3":
				family = 12000;
				break;
			case "4":
				family = 15000;
				break;
			case "5":
				family = 20000;
				break;
			}
		}
	}
	var weight = score/2069;
	var amt = parseInt(((family - min)*period)*weight);
	if(amt < 25000){
		amt = 25000;
	}
	if(amt > 250000){
		amt = 250000;
	}
	amt = amt.toString();
	initAmt();
	for(var i = 0; i < amt.length; i++){
		var item = "n"+i;
		$("#"+item).html(amt[amt.length-1-i]);
	}
}
//初始化金额为0
function initAmt(){
	for(var i = 0; i < 6; i++){
		var item = "n"+i;
		$("#"+item).html("0");
	}
}

//根据题和选项返回分数
function getScore(q_index,c_index){
	switch(q_index){
	case 1:
		switch(c_index){
		case 1:
			return 344;
		case 2:
			return 354;
		case 3:
			return 392;
		case 4:
			return 374;
		}
		break;
	case 2:
		switch(c_index){
		case 1:
			return 246;
		case 2:
			return 188;
		}
		break;
	case 3:
		switch(c_index){
		case 1:
			return 178;
		case 2:
			return 155;
		case 3:
			return 139;
		case 4:
			return 120;
		}
		break;
	case 4:
		switch(c_index){
		case 1:
			return 200;
		case 2:
			return 180;
		case 3:
			return 160;
		}
		break;
	case 5:
		switch(c_index){
		case 1:
			return 146;
		case 2:
			return 204;
		case 3:
			return 209;
		case 4:
			return 262;
		}
		break;
	case 6:
		switch(c_index){
		case 1:
			return 415;
		case 2:
			return 402;
		case 3:
			return 195;
		case 4:
			return 182;
		}
		break;
	case 7:
		switch(c_index){
		case 1:
			return 465;
		case 2:
			return 296;
		case 3:
			return 126;
		case 4:
			return 17;
		}
		break;
	case 8:
		switch(c_index){
		case 1:
			return 240;
		case 2:
			return 160;
		}
		break;
	case 9:
		switch(c_index){
		case 1:
			return 154;
		case 2:
			return 167;
		case 3:
			return 177;
		case 4:
			return 188;
		case 5:
			return 269;
		}
		break;
	default:
		break;
	}
}