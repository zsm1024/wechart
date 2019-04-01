var localIds;
var openid;
var flag = false;
$(function(){
	pageConfig();
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

function htmlInit(){
	$.JsonSRpc(baseUrl+"/getcurrentcontract", {openid:openid}, function(data){
		if(data.errcode&&"0"!=data.errcode){
			flag = false;
			message(data.errmsg, function(flag){
				//wx.closeWindow();
				window.location.href = "./myLoan.html";
			});
		}else{
			flag = true;
			var contract = JSON.parse(data.contracts);
			loadPageMsg(contract);
		}
	});
}

function loadPageMsg(data){
	var obj = data.contract;
	$("#contract_nbr").html(obj.contract_nbr);
	$("#total_amt").html(fmoney(obj.total_amt,2)+"元");
	$("#asset_brand").html(obj.asset_brand);
	$("#financed_amt").html(fmoney(obj.financed_amt,2)+"元");
	$("#contract_term").html(obj.contract_term+"个月");
}

//js-sdk注册接口
function pageConfig(){
	$.JsonSRpc(baseUrl+"/jssdksign", {
		url : location.href.split('#')[0]
	}, function(data) {
		wx.config({
			debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : data.appId, // 必填，公众号的唯一标识
			timestamp : data.timestamp, // 必填，生成签名的时间戳
			nonceStr : data.nonceStr, // 必填，生成签名的随机串
			signature : data.signature, // 必填，签名，见附录1
			jsApiList : [ 'checkJsApi',
			              'uploadImage',
			              'chooseImage' ]
		// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function() {
			wx.checkJsApi({
			    jsApiList: [ 'checkJsApi',
				              'uploadImage',
				              'chooseImage' ], // 需要检测的JS接口列表，所有JS接口列表见附录2,
			    success: function(res) {
			        // 以键值对的形式返回，可用的api值true，不可用为false
			        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
			    }
			});
		});
		wx.error(function(res) {
			message("wx.error===" + JSON.stringify(res));
		});
	});
}

//选择图片
function imgClick(){
	wx.chooseImage({
	    count: 1, // 默认9
	    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	        localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	        $("#certimg").attr("src", localIds);
	    },
	    fail:function(res){
	    	message("选择照片失败");
	    }
	});
}

//上传图片
function uploadImg(){
	if(!flag){
		message("获取合同信息失败，无法上传图片");
		return;
	}
	if(localIds == undefined || localIds.length <= 0){
		message("请选择凭证");
		return;
	}
	var contract_nbr = $("#contract_nbr").html();
	wx.uploadImage({
	    localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
	    isShowProgressTips: 1, // 默认为1，显示进度提示
	    success: function (res) {
	        var serverId = res.serverId; // 返回图片的服务器端ID
	        $.JsonSRpc(baseUrl+"/uploadcert", {serverid:serverId,openid:openid,contract_nbr:contract_nbr}, function(data){
	        	if(data.errcode&&"0"==data.errcode){
	        		message("上传成功！", function(flag){
	        			wx.closeWindow();
	        		});
	        	}else{
	        		message(data.errmsg);
	        	}
	        });
	    },
	    fail:function(res){
	    	message("上传照片失败!");
	    }
	});
}