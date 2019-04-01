$(function() {
	if (undefined == getParam("code")) {
		window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3c58b725b9220b9e&redirect_uri=http%3A%2F%2Fweixintest.gwmfc.net%2Fgwm-web-wechat%2Fhtml%2Fauthtest.html&response_type=code&scope=snsapi_base&state=true#wechat_redirect";
		return;
	} else {
		$.JsonSRpc(baseUrl + "/snsapibase", {
			code : getParam("code")
		}, function(data) {
			alert(JSON.stringify(data));
		});
	}
});