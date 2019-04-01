var openid="";
//var contract_id="2245";//合同id
//var contract_nbr="GW100002";//合同号
//var openid="o5iChju4y1zdq0NKheVA5ilu3r1A";
$(function (){
	openid=sessionStorage.getItem("openid");
	if(openid == null || openid=="" || undefined == openid ){
		var code = getParam("code");
		if(undefined == code || "" == code || code== null){
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}else{
			$.showLoadMsg("加载中，请稍后...");
			getOpenId();
		}
	}else{
		sessionStorage.setItem("openid",openid);
		//获取还款记录
		getList();
	}
	$(".repayment-select a").click(function(){
		var x =$(this).index();
		$(this).removeClass().addClass("curr").siblings().removeClass();
		$(".repayment-list dl").eq(x).show().siblings().hide();
	});
	//采用.on()方法来绑定，append拼装的新标签元素时间
	$(".repayment-list").on("click","h3",function(){
		$(this).toggleClass("open");
		$(this).next().toggle();
	});

});

//提前还款申请记录查询
function getList(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/queryRepayRecord",{openId:openid},function(data) {
		$.hideLoadMsg();
		if(data.errcode=="0"){
			var repayment=JSON.parse(data.repayment);
			var norepayment=JSON.parse(data.norepayment);
			$("#repayment_term").text(repayment_term_check(repayment.repayment_term));
			$("#no_repayment_term").text(no_repayment_term_check(norepayment.no_repayment_term));
			
			//——————————————————————————拼装已还款记录——————————————————————————————————————————————
			//已还款的数据
			var repaymentPlan=repayment.repayment_plan;
			var repayment_term = repayment.repayment_term;
			//已还款的最后一个年份
			var repay_year="";
			//拼装的标签
			var repay_temp="";
			for (var i = repaymentPlan.length-1; i >= 0; i--) {
				var repay_oldyear=((repayment.repayment_plan)[i].date).toString().substring(0,4);
				if(i==repaymentPlan.length-1){
					repay_year=(repaymentPlan[repaymentPlan.length-1].date).toString().substring(0,4);
					repay_temp="<h3 class='open'>"+repay_year+"年</h3>";
					repay_temp+="<ul>";
				}
				//同一年的下拉单
				if(repay_year==repay_oldyear){
					repay_temp+="<li>"+dateFormat(repaymentPlan[i].date)+"&nbsp;&nbsp;" +
							    "<span>"+fmoney(repaymentPlan[i].money,2)+"元</span></li>";
				}
				//不同年另起的下拉单
				else{
					repay_year=repay_oldyear;
					repay_temp+="</ul><h3>"+repay_oldyear+"年</h3><ul style='display:none;'>"+
							    "<li>"+dateFormat(repaymentPlan[i].date)+"&nbsp;&nbsp;" +
							    "<span>"+fmoney(repaymentPlan[i].money,2)+"元</span></li>";
				}
				if(i==0){
					repay_temp+="</ul>";
				}
			}
			if(repayment_term > 0){
				repay_temp += '<div style="text-align:right;padding:.1rem;color:red;">合计&nbsp;&nbsp;<span>'+fmoney(repayment.repayment_amt,2)+'</span>元</div>';
			}
			$("#repayment_dl").show();
			$("#repayment_dl").append(repay_temp);
			
			//——————————————————————————拼装待还款记录——————————————————————————————————————————————
			//已还款的数据
			var norepaymentPlan=norepayment.repayment_plan;
			var no_repayment_term = norepayment.no_repayment_term;
			//未还款的第一个年份
			var norepay_year = "";
			//拼装的标签
			var norepay_temp = "";
			repayamt = 0;
			
			if(no_repayment_term!=1){
				for (var i = 0; i < norepaymentPlan.length; i++) {
					repayamt += parseFloat(norepaymentPlan[i].money);
					var norepay_newyear=(norepaymentPlan[i].date).toString().substring(0,4);
					if(i==0){
						norepay_year=(norepaymentPlan[0].date).toString().substring(0,4);
						norepay_temp="<h3 class='open'>"+norepay_year+"年</h3>";
						norepay_temp+="<ul>";
					}
					//同一年的下拉单
					if(norepay_year==norepay_newyear){
						norepay_temp+="<li>"+dateFormat(norepaymentPlan[i].date)+"&nbsp;&nbsp;" +
								      "<span>"+fmoney(norepaymentPlan[i].money,2)+"元</span></li>";
					}
					//不同年另起的下拉单
					else{
						norepay_year=norepay_newyear;
						norepay_temp+="</ul><h3>"+norepay_newyear+"年</h3><ul style='display:none;'>"+
								      "<li>"+dateFormat(norepaymentPlan[i].date)+"&nbsp;&nbsp;" +
								      "<span>"+fmoney(norepaymentPlan[i].money,2)+"元</span></li>";
					}
					if(i==norepaymentPlan.length-1){
						norepay_temp+="</ul>";
					}
				}
			}
			else{
				repayamt=parseFloat(norepaymentPlan.record.money);
				norepay_year=(norepaymentPlan.record.date).toString().substring(0,4);
				norepay_temp="<h3 class='open'>"+norepay_year+"年</h3>";
				norepay_temp+="<ul>";
				norepay_temp+="<li>"+dateFormat(norepaymentPlan.record.date)+"&nbsp;&nbsp;" +
			      "<span>"+fmoney(norepaymentPlan.record.money,2)+"元</span></li>";
				norepay_temp+="</ul>";
			}
				
			
			
			
			if(no_repayment_term > 0){
				norepay_temp += '<div style="text-align:right;padding:.1rem;color:red;">合计&nbsp;&nbsp;<span>'+fmoney(repayamt, 2)+'</span>元</div>';
			}
			$("#norepayment_dl").hide();
			$("#norepayment_dl").append(norepay_temp);
			
		}
		else{
			if(data.errmsg==null || data.errmsg=="" || data.errmsg=="null"){
				message("数据通讯异常，请联系客服", function(flag){
					wx.closeWindow();
				});
			}
			else{
				message(data.errmsg, function(flag){
					//wx.closeWindow();
					window.location.href = "./myLoan.html";
				});
			}
		}
		
	});
}
//已还款期数
function repayment_term_check(para){
	return "已还款："+para+"期";
}
//未还款期数
function no_repayment_term_check(para){
	return "待还款："+para+"期";
}
//日期格式
function dateFormat(para){
	var year=para.toString().substring(0,4);
	var moon=para.toString().substring(5,7);
	var day=para.toString().substring(8,10);
	return year+"年"+moon+"月"+day+"日";
}

//获取openid
function getOpenId(){
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: getParam("code")},function(data) {
		$.hideLoadMsg();
		if(data.errcode!=null && data.errcode!=null &&"0"==data.errcode){
			openid = data.openid;
			if(openid==undefined||""==openid){
				message("信息获取失败，请重新进入页面", function(flag){
					wx.closeWindow();
				});
				return;
			}else{
				sessionStorage.setItem("openid", openid);
				//获取还款记录
				getList();
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}

