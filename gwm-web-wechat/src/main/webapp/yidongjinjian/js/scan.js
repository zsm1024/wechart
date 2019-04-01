	function qr(){			
		$.showLoadMsg("加载中，请稍后...");
		var url = basePath+"/jssdksign?test=1";
		$.JsonSRpc(url, {url:location.href.split('#')[0]},
			function(data) {
			$.hideLoadMsg();
			if (data.error) {
				message(data.error);
				//$.hideLoadMsg();
				return;
			}
			else 
			{
			
				wx.config({
					debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
					appId: data.appId, // 必填，公众号的唯一标识
					timestamp: data.timestamp, // 必填，生成签名的时间戳
					nonceStr: data.nonceStr, // 必填，生成签名的随机串
					signature: data.signature, // 必填，签名，见附录1
					jsApiList: [
						'checkJsApi',
						'openLocation',
						'getLocation',
						'onMenuShareQZone',
						'chooseImage',
						'scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
				 wx.ready(function() {

					wx.scanQRCode({
									needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
									scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
									success: function (res) {
									var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
									var a=res.resultStr;
									result=result.split("/");
  									result.pop();
  									result=result.join("/");
  									a=a.split("/");
  									a.shift();
  									a=a.join("/");
									$(".QRcode").hide();
//									var str="";
//								str+="<div class='JXSinfor'><label class='jxsMes'><span>经销商ID：</span></label><span class='JXSID'>"+result+"</span></div>"
//								str+="<div class='JXSinfor'><label class='jxsMes'><span>经销商ID：</span></label><span class='class='JXSName'>"+a+"</span></div>"

//									str+="<div class='mui-input-row'><label class='jxsMes'><span>经销商ID：</span></label><span class='JXSID choose'>"+result+"</span></div>";
//									str+="<div class='mui-input-row'><label class='jxsMes'><span>经销商名称：</span></label><span class='JXSName choose'>"+a+"</span></div>";
//									$(".jxscontent").empty();
//									$(".jxscontent").append(str);
//									$(".okNext").removeAttr("disabled")
  									localStorage.setItem("JXSID",result);
  									localStorage.setItem("JXSName",a);
									$.hideLoadMsg();
									window.location.href="car.html";
							}
								});
									});
			}
		});
	}
	$(".okNext").on("click",function(){
		window.location.href="car.html";
})