var dlModel = new Array('127','280','281','290');
var dlCar = new Array('117','119','120','121','123','136','147');
var _CIACarModelData;
var openid;
var imgUrl="https://wechat.gwmfc.com/gwm-web-wechat/img/share2.jpg";	
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
						window.location.href="./guanzhu.html";
//						message("信息获取失败!", function(flag){
//							wx.closeWindow();
//						});
					}else{
						sessionStorage.setItem("openid", openid);
						htmlInit();
					}
				}else{
					window.location.href="./guanzhu.html";
//					message("获取用户信息失败！", function(flag){
//						wx.closeWindow();
//					});
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
	$.JsonSRpc(baseUrl+"/jssdksign", {
		url : location.href.split('#')[0]
	}, function(data) {
		wx.config({
			debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : data.appId, // 必填，公众号的唯一标识
			timestamp : data.timestamp, // 必填，生成签名的时间戳
			nonceStr : data.nonceStr, // 必填，生成签名的随机串
			signature : data.signature, // 必填，签名，见附录1
			jsApiList : [ 'checkJsApi', 'openLocation', 'getLocation',
					'onMenuShareTimeline','onMenuShareAppMessage', 'chooseImage' ]
		// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function() {
			wx.onMenuShareTimeline({
				title:"长城金融贷款计算器",
				link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeba2859c6bbe5dc4&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Floancalc.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
				imgUrl:imgUrl,
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
			wx.onMenuShareAppMessage({
				 title: '长城金融贷款计算器', // 分享标题
				 desc: '根据您的个人资金情况及贷款期限需求，选择多种贷款方式，计算月供金额。', // 分享描述
				 link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeba2859c6bbe5dc4&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Floancalc.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
				 imgUrl: imgUrl, // 分享图标
				 type: '', // 分享类型,music、video或link，不填默认为link
				 dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				 success: function () { 
				     // 用户确认分享后执行的回调函数
				 },
				 cancel: function () { 
				     // 用户取消分享后执行的回调函数
				 }
			});
		});
		wx.error(function(res) {
			alert("wx.error===" + JSON.stringify(res));
		});
	});
});

function htmlInit(){
	var m;
	//品牌改变
	$("#brand").change(function(){
		var n = $("#brand").val();
		selectbrand(n);
		hideButton();
	});
	m = $("#brand").val();
	selectbrand(m);
	hideButton();
	//车型改变
	$("#models").change(function(){
		var n = $("#models").val();
		selectmodel(n);
		hideButton();
		checkModels();
	});
	m = $("#models").val();
	selectmodel(m);
	hideButton();
	//车款改变
	$("#car_style").change(function(){
		var n = $("#car_style").val();
		set_model(n);
		showButton();
	});
	//金融产品改变
	$("#loan_type").change(function(){
		var n = $("#loan_type").val();
		check_loan(n);
		checkModels();
	});
	m = $("#loan_type").val();
	check_loan(m);
	
	$("#priceInput").focus(function(){//车价获得焦点
        var price=document.getElementById("priceInput");
        var myprice=removeThousand(price.value);//去掉金额中的千分符
        $("#priceInput").val(myprice);
    });
	
	//点击开始计算
	$("#startCalcute").click(function(){
//		window.location.href = "./loancalc.html#startCalcute";
//		window.scrollTo(0,1000);
		cacluteLoan();
		$('html, body').animate({  
            scrollTop: $("#apply").offset().top  
        }, 500);
	});
	
	//在线申请
	$("#apply").click(function(){
		apply();
	});
	
	hideButton();
	checkModels();
}

//检查车型设置还款期数
function checkModels(){
	var model_name = $("#models").find("option:selected").text();
	var loan_type = $("#loan_type").val();
	if("1"==loan_type){
		//随薪贷
		var slider = $("#range_3").data("ionRangeSlider");
		slider.options.max=36;
		slider.options.from=24;
		slider.reset();
	}else{
		//常规产品(等额本金、等额本息)
		if(model_name.indexOf("风骏") >= 0){
			var slider = $("#range_3").data("ionRangeSlider");
			slider.options.max=36;
			slider.options.from=24;
			slider.reset();
		}else{
			var slider = $("#range_3").data("ionRangeSlider");
			slider.options.max=60;
			slider.options.from=24;
			slider.reset();
		}
	}
}

function hideButton(){
	$("#startCalcute").css("background","gray");
//	$("#startCalcute").attr("disabled", true);  
	$("#apply").css("background","gray");
//	$("#apply").attr("disabled", true);  
}
function showButton(){
	$("#startCalcute").css("background","#c8161d");
//	$("#startCalcute").removeAttr("disabled"); 
	$("#apply").css("background","#c8161d");
//	$("#apply").removeAttr("disabled"); 
}

//加载车型数据源
function selectbrand(brand){
	debugger;
    if(brand=="1"){
    	_CIACarModelData = _ccModelData;
    }else if(brand=="2"){
    	_CIACarModelData = _hfModelData;
    }
    else if(brand=="3"){
    	/*message("WEY品牌暂无车型上市，敬请期待！", function(flag){
    		$("#brand").val("-1");
    	});
        _CIACarModelData=[];*/
    	_CIACarModelData = _weyModelData;
    	var product='<option value="3">等额本息</option><option value="1">弹性尾款</option>';
    	$("#loan_type").html(product);
    }else if(brand=="4"){
    	_CIACarModelData = _oraModelData;
    	$("#loan_type").html(product);
    }
    
    else{
    	_CIACarModelData=[];
    }
    var html = '<option value="-1">请选择车型</option>';
    for (var i = 0; i < _CIACarModelData.length; i++) {
        if (_CIACarModelData[i].PID == 0 && jQuery.inArray(_CIACarModelData[i].CategoryID,dlModel) < 0){
            html += '<option value="'+_CIACarModelData[i].CategoryID+'">'+_CIACarModelData[i].Name+'</option>';
        }
    }
    $("#models").html(html);
    selectmodel("-1");
    $("#priceInput").val("");
}
//加载车款
function selectmodel(id){
    var html='<option value="-1">请选择车款</option>';
    if (id) {
        for (var i = 0; i < _CIACarModelData.length; i++) {
            if (_CIACarModelData[i].PID == id) {
//                html += '<li onclick=set_model("'+_CIACarModelData[i].Name+'","'+i+'")>' + _CIACarModelData[i].Name+'</li>';
            	html += '<option value="'+_CIACarModelData[i].CategoryID+'">'+_CIACarModelData[i].Name+'</option>';
            }
        }
    }
    $("#car_style").html(html);
    if("-1" == id){
		return;
	}
    $("#priceInput").val("");
//    $("#priceInput").val(0);
//    myPrice();
}

//设置车价
function set_model(id){
	if(id == -1){
		return ;
	}
	var data;
	for(var i = 0; i < _CIACarModelData.length; i++){
		if(id == _CIACarModelData[i].CategoryID){
			data = _CIACarModelData[i];
		}
	}
	if(!data){
		return;
	}
    var price =  carCostParseFloat(Math.round(data.field.Price * 10000));
    if (price == undefined || price == null){
        return;
    }
    $("#priceInput").val(price);
    myPrice();
    ///////////////////////////////////////////////////
    //下面进行经销商验证与查询
    //var carModel=$("#Model").html();
    //getShopInfo(carModel);
}

//车价离开焦点
function myPrice(price){
    var i=document.getElementById("priceInput").value;
    if(i.length==0){
    	message("请输入正确金额!");
    	$("#price_warn").html("请输入正确的金额");
    	$("#priceInput").val("");
        $("#priceInput").addClass("warning");
    	return false;
    }
    if ( isNaN(i) ) {
    	message("请输入正确金额!");
    	$("#price_warn").html("请输入正确的金额");
        $("#priceInput").val("0");
        $("#priceInput").addClass("warning");
        return false;
    }
    if(i.length>14){
    	message("请输入正确金额!");
    	$("#price_warn").html("请输入正确的金额");
        $("#priceInput").addClass("warning");
        return false;
    }
    $("#price_warn").html("");
    $("#priceInput").val(format(parseInt(i),0));
    $("#priceInput").removeClass("warning");
}

//去除千分符
function removeThousand(num){
    if(num!=null&&num!=""&&num.length>0){
        return num.replace(new RegExp(/(,)/g),"");
    }
}

//格式化金额
var carCostParseFloat = function (val) {
    if (val == undefined) {
        return 0;
    }
    if (isNaN(val)) {
        return 0;
    }

    if (val == "") {
        return 0;
    }

    var result = parseFloat(val);
    if (result < 0) {
        result = 0;
    }

    return result;
};

//格式化金额
function format (num,fixed) {
    if(fixed==undefined){
        fixed = 0;
    }
    return (num.toFixed(fixed) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}

//选择金融产品
function check_loan(loan_type){
	if(loan_type=="1"){
		$("#weikunli").show();
	}
	else{
		$("#weikunli").hide();
	}
}

/**
 * 开始计算
 */
function cacluteLoan(){
	var color=$("#startCalcute").css("background-color");
//	if(color=="rgb(128, 128, 128)"){
//		alert("不可用");
//	}
//	alert(color);
	if(color!="rgb(128, 128, 128)") {
		var param = {};
		var type=$("#loan_type").val();
		if(type=="1")
		{
			param.type = 1;//$("#loanType").find("option:selected").val();
		}else if(type=="2"){
			param.type = 2;
		}else if(type=="3"){
			param.type = 3;
		}
		else{
			message("贷款方案不能为空，请选择");
			return;
		}
		var shouyue="";
		if(type=="2"){
			shouyue="<span id='shouyue' style='font-size:0.13rem'>（首月）</span>";
		}
		param.firstSize = $("#range_1").data("ionRangeSlider").result.from;//首付比例
		param.price = $("#priceInput").val().split(",").join("");//车价
		param.count = $("#range_3").data("ionRangeSlider").result.from;//还款期数
		param.lastsize = $("#last_per").attr("checked") ? 20 : 25;//尾款比例
		var obj = debj(param);

		$("#sf_amt").html(format(obj.shoufu));

		$("#loan_amt").html(format(obj.daikuan));

		$("#month_amt").html(format(obj.yuegong)+shouyue);
		if(obj.weikuan == undefined){
			$("#last_amt").html("--");
		}else{
			$("#last_amt").html(format(obj.weikuan));
		}
	}
}

//在线申请
function apply(){
	var color=$("#startCalcute").css("background-color");
//	if(color=="rgb(128, 128, 128)"){
//		alert("不可用");
//	}
//	alert(color);
	if(color!="rgb(128, 128, 128)")  {
		sessionStorage.brand = $("#brand").val();
		sessionStorage.models = $("#models").val();
		sessionStorage.car_style = $("#car_style").find("option:selected").text();
		sessionStorage.priceInput = $("#sf_amt").html().split(",").join("");
		window.location.href = "./apply.html";
	}
}