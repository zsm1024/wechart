var imgUrl = "https://wechat.gwmfc.com/gwm-web-wechat/img/share.png";	            	  // 分享图标
$(function() {
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
				title:"长城汽车金融",
				link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64df46fa7208efb6&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Fhome.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
				imgUrl:imgUrl,
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
			wx.onMenuShareAppMessage({
				 title: '长城汽车金融', // 分享标题
				 desc: '长城汽车金融为您提供最新最好的“一站式”、“终生式”、“超出客户期望”的金融服务', // 分享描述
				 link:"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64df46fa7208efb6&redirect_uri=https%3A%2F%2Fwechat.gwmfc.com%2Fgwm-web-wechat%2Fhome.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect",
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