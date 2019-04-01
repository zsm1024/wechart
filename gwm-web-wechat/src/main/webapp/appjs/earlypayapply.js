$(function(){
	var openid = sessionStorage.getItem("openid");
	var total = sessionStorage.getItem("total");
	var contract_nbr = sessionStorage.getItem("contract_nbr");
	var asset_brand_dsc = sessionStorage.getItem("asset_brand_dsc");
	var bankNum=sessionStorage.getItem("bankNum");
	var ids=sessionStorage.getItem("business_partner_nbr");
	if(ids==""||ids==undefined){
		message("获取预留信息失败", function(flag){
			wx.closeWindow();
		});
		return;
	}
	if(undefined == openid || "" == openid){
		message("获取用户信息失败", function(flag){
			wx.closeWindow();
		});
		return;
	}
	if(undefined == total){
		message("请先在提前还款页面中进行提前还款试算", function(flag){
			wx.closeWindow();
		});
		return;
	}
	if(undefined==bankNum||""==bankNum){
		message("获取预留卡号信息失败", function(flag){
			wx.closeWindow();
		});
		return;
	}
	$("#total").html(total);
	$("#total_card").html(bankHide(bankNum));
	$("#contract_nbr").html("<span>合同号：</span>"+contract_nbr);
	$("#asset_brand").html("<span>车&nbsp;&nbsp;&nbsp;&nbsp;型：</span>"+asset_brand_dsc);
	$(".submit-btn").click(function(){
		if(myID()&&agreeCheck()){
			repayApply();
		}
	});
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
	$("#agree").click(function(){
		if($("#agree").get(0).checked){
			$("#btnOk").removeClass("disabled");
		}
		else{
			$("#btnOk").removeClass("disabled");
			$("#btnOk").addClass("disabled");
		}
	});
	$("#confirmletter").click(function(event){
		window.location.href="confirmletter.html";
	});
});
function bankHide(para){
	var frontShow=para.toString().substring(0,4);
	var afterShow=para.toString().substring(para.length-4);
	for (var i = 4; i < para.length-4; i++) {
		frontShow+="*";
	}
	return frontShow+afterShow;
}
function repayApply(){
	var repay_type = $('input:radio:checked').val();
	var openid = sessionStorage.getItem("openid");
	var total = sessionStorage.getItem("total");
	var repaydate = sessionStorage.getItem("repaydate");
	var contract_nbr = sessionStorage.getItem("contract_nbr");
	var phone_nbr=sessionStorage.getItem("phone_nbr");
	if(undefined == openid || "" == openid){
		message("获取用户信息失败，请重新进行还款试算", function(flag){
			wx.closeWindow();
		});
		return;
	}
	if(undefined == total){
		message("请先在提前还款页面中进行提前还款试算", function(flag){
			wx.closeWindow();
		});
		return;
	}
	if(0 == total){
		message("还款金额为0，无需申请提前还款");
		return;
	}
	if(undefined == repaydate){
		message("获取还款日期失败，请重新进行试算");
		return;
	}
	repay_type='1';
	$.showLoadMsg("申请处理中，请稍后...");
	$.JsonSRpc(baseUrl+"/wxearlyrepayapply", 
			{openid:openid,total:total,contract_nbr:contract_nbr,application_date:repaydate,repay_type:repay_type,phone_nbr:phone_nbr}, function(data){
				$.hideLoadMsg();
				if(data.errcode&&"0"==data.errcode){
					//message("申请成功", function(flag){
						if(data.predt!=undefined||data.predt.length>0)
						if("1"==repay_type){
							if(data.predt!=undefined||data.predt.length>0){
								sessionStorage.content = "您已成功申请提前还款业务，请确保在扣款日前一天"+data.predt+" 向还款账户中存入提前结清金额"+total+"元。";
							}else{
								sessionStorage.content = "您已成功申请提前还款业务，请确保在扣款日"+repaydate+" 10：00前向还款账户中存入提前结清金额"+total+"元。";
							}
							window.location.href = "./earlyrepaycfm1.html";
						}else if("2" == repay_type){
//							sessionStorage.content = "您已成功申请提前还款业务，请确保在扣款日"+repaydate+" 10：00前向对公账户中存入提前结清金额"+total+"元，并上传还款凭证，之后我们将对合同进行结清。";
							if(data.predt!=undefined||data.predt.length>0){
								sessionStorage.content = "您已成功申请提前还款业务，请确保在扣款日前一天"+data.predt+"向对公账户中存入提前结清金额"+total+"元，同时上传还款凭证。";
							}else{
								sessionStorage.content = "您已成功申请提前还款业务，请确保在扣款日"+repaydate+" 10：00前向还款账户中存入提前结清金额"+total+"元，同时上传还款凭证，之后我们将对合同进行结清。";
							}
							window.location.href = "./earlyrepaycfm2.html";
						}else{
							window.location.href = "./myLoan.html";
						}
					//});
					//跳转到为的贷款页面
				}else{
					message(data.errmsg);
				}
			});
}

function myID(){
	var id=$("#idno").val();
	if(id.length<6){
		message("请输入身份证后6位");
		$("#idno").addClass("warning");
		return false;
	}
	var ids=sessionStorage.getItem("business_partner_nbr").substring(12,18);
	if(id.toLowerCase()!=ids.toLowerCase()){
		message("验证失败，请检查您输入是否有误");
		$("#idno").addClass("warning");
		return false;
	}
	return true;
}

function agreeCheck(){
	if(!$("#agree").get(0).checked) {
		message("请阅读提前还款确认函并选择");
//		timeDifference(beforetime);
		return false;
	}
	return true;
}