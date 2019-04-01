var listData = [];
$(function(){
	htmlInit();
});
//页面初始化
function htmlInit(){
	var obj = {};
	obj.province = sessionStorage.province;
	obj.city = sessionStorage.city;
	obj.min_bal = sessionStorage.min_bal;
	obj.max_bal = sessionStorage.max_bal;
	obj.first_amt = sessionStorage.first_amt;
	obj.state = sessionStorage.state;
	obj.openid = sessionStorage.openid;
	var sf = obj.first_amt;
	$.JsonSRpc(baseUrl+"/wxgetschemes", obj, function(data){
		$(".car-list").html("");
		if(data.errcode&&"0"==data.errcode){
			var schemesStr = data.schemes;
			var schemes = JSON.parse(schemesStr);
			listData = schemes;
			var item = '';
			for(var i = 0; i < schemes.length; i++){
				var jisuan_parm = {};
				jisuan_parm.type = '3';//目前产品全是等额本息
				jisuan_parm.firstSize = schemes[i].firstsize;//首付比例
				jisuan_parm.price= schemes[i].amt;//车价
				jisuan_parm.count = schemes[i].preoid;//还款期数
				jisuan_parm.lastsize = "";//金融产品类型都是等额本息，所有尾款期数可以不填。
				var obj = debj(jisuan_parm);
				obj.shoufu = sf+"0000";
				listData[i].shoufu = obj.shoufu;
				
				item += '<li class="flex">' +
					'<img src="car_img/'+schemes[i].pic+'" alt="">' +
					'<div class="car-info">' +
					'<h3>'+schemes[i].models+'</h3>' +
					'<h3 style="font-size:0.15rem">'+schemes[i].configure+'</h3>' +
					'<p>官方指导价：<em>¥'+format(parseInt(schemes[i].price), 0)+'</em></p>' +
					'<div class="loan-info flex">' +
					'<dl>' +
					'<dt onclick="productHtml()"><span style="text-decoration:underline">'+schemes[i].amount_product+'</span>&nbsp;&nbsp;&nbsp;&nbsp;'+schemes[i].preoid+'期</dt>' +
					'<dd>首付 ¥'+format(parseInt(obj.shoufu))+'</dd>' +
					'<dd>月供 ¥'+format(parseInt(obj.yuegong))+'</dd>' +
					'</dl>' +
					'<a href="#" onclick="applyNow(\''+i+'\')">立即申请</a>' +
					'</div>' +
					'</div>' +
					'</li>';
			}
			$(".car-list").html(item);
		}else{
			message(data.errmsg);
		}
	});
}

function productHtml(){
	window.location.href = "./product.html";
}

//格式化金额
function format (num,fixed) {
    if(fixed==undefined){
        fixed = 0;
    }
    return (num.toFixed(fixed) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}

//立即申请
function applyNow(n){
	sessionStorage.brand = listData[n].brand;
	sessionStorage.models = listData[n].models;
	sessionStorage.car_style = listData[n].configure;
	sessionStorage.priceInput = listData[n].shoufu;
	window.location.href = "./apply.html";
}