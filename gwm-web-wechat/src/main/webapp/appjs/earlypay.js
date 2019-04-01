var openid;
$(function(){
	var repaydate = $("#repaydate").val();
	if(repaydate.length>0){
		$("#datep").html("");
	}
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
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//console.log(data);
		$.hideLoadMsg();
		if(data.bindingJudg=="false"){
			message("您还没有绑定任何合同", function(flag){
				//wx.closeWindow();
				window.location.href = "./myLoan.html";
			});
		}
	});
	$(".select-date input").change(function(){
		$(this).next("p").hide();
		undefined==total;
	});
	$("#jisuan").click(function(){
		var repaydate = $("#repaydate").val();
		if(repaydate==undefined||repaydate.length<10){
			message("请选择提前还款日期");
			return;
		}
		sessionStorage.setItem("repaydate", repaydate);
		$.showLoadMsg("正在查询，请稍后...");
		$.JsonSRpc(baseUrl+"/getearlyrepayinfo", {openid:openid,application_date:repaydate}, function(data){
			$.hideLoadMsg();
			if(data.errcode&&"0"==data.errcode){
				$("#total").html(data.total);
				$("#interest").html(data.interest);
				$("#penalty").html(data.penalty);
				$("#capital").html(data.capital);
				sessionStorage.setItem("total", data.total);
				sessionStorage.setItem("contract_nbr", data.contract_nbr);
				sessionStorage.setItem("asset_brand_dsc", data.asset_brand_dsc);
				$("#repayapply").removeClass("disabled");
			}else{
				message(data.errmsg);
				$("#repaydate").val("");
				$("#datep").show();
				$("#total").html("0.00");
				$("#interest").html("0.00");
				$("#penalty").html("0.00");
				$("#capital").html("0.00");
				sessionStorage.removeItem("total");
				sessionStorage.removeItem("contract_nbr");
				sessionStorage.removeItem("asset_brand_desc");
				$("#repayapply").addClass("disabled");
				total=undefined;
			}
		});
		$.JsonSRpc(baseUrl+"/wxearlyrepaygetbank", 
				{openid:openid}, function(data2){
					$.hideLoadMsg();
					if(data2.errcode&&"0"==data2.errcode){
						sessionStorage.setItem("bankNum", data2.bandNum);
						sessionStorage.setItem("business_partner_name", data2.business_partner_name);
						sessionStorage.setItem("business_partner_nbr", data2.business_partner_nbr);
						sessionStorage.setItem("phone_nbr", data2.phone_nbr);
//						window.location.href = "./earlypayapply.html";
					}else{
						message(data2.errmsg);
					}
				});
	});
	$("#repayapply").click(function(){
		//判断是否选择日期
		var repaydate = $("#repaydate").val();
		if(repaydate==undefined||repaydate.length<10){
			message("请选择提前还款日期");
			return;
		}
		var total = sessionStorage.getItem("total");
		var contract_nbr = sessionStorage.getItem("contract_nbr");
		if(undefined==total){
			message("请先点击计算，进行试算");
			return;
		}
//		if(0 == total){
//			message("应还总金额为0元，无需申请提前还款");
//			return;
//		}
		$.showLoadMsg("申请处理中，请稍后...");
		var repay_type = "1";
		var openid = sessionStorage.getItem("openid");
		var total = sessionStorage.getItem("total");
		var repaydate = sessionStorage.getItem("repaydate");
		var contract_nbr = sessionStorage.getItem("contract_nbr");
		$.JsonSRpc(baseUrl+"/wxearlyrepaycheck", 
				{openid:openid,total:total,contract_nbr:contract_nbr,application_date:repaydate,repay_type:repay_type}, function(data){
					$.hideLoadMsg();
					if(data.errcode&&"0"==data.errcode){
						window.location.href = "./earlypayapply.html";
					}else{
						message(data.errmsg);
					}
				});
	});
	$("#repayrecord").click(function(){
		window.location.href = "./earlyRepayApplyQuery.html";
	});
}