//长城滨银汽车我的贷款
//页面初始化
var openid="";
//是否经过空白页进行过绑定与否的判断
var white="";
//判断是否绑定的标志 ok：绑定 err：未绑定
var judg="";
//var openid="o5iChju4y1zdq0NKheVA5ilu3r1A";
//已绑定的合同状态  L正常 N已结清
var contract_status="";
//该账号下拥有的合同数量
var contract_amount="";
//合同号
var contract_nbr="";
//申请人身份证
var business_partner_nbr="";
//申请人手机号
var phone_nbr="";
$(function (){
	$("#bindingError").show();
	$("#bindingOk").hide();
	$(".my-ico").hide();
	//openid="oImjOwLUFBN6V9h-txVjZIG5MqMw";
	openid=sessionStorage.getItem("openid");
	//sessionStorage.setItem("openid",openid);
	//getBindingContract();
	if(openid == null || openid=="" || undefined == openid ){
		var code = getParam("code");
		if(undefined == code || "" == code || code== null){
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}else{
			$.showLoadMsg("加载中，请稍后...");
			getOpenId(code);
		}
	}
	else{
		sessionStorage.setItem("openid",openid);
		if(sessionStorage.getItem("headimgurl")!=null && sessionStorage.getItem("headimgurl")!=""){
			$(".my-photo img").attr('src',sessionStorage.getItem("headimgurl"));
		}
		else{
			//获取用户头像地址
			getHeadImg(openid);
		}
		//获取绑定合同信息,根据绑定合同确定展示的还款信息类型
		getBindingContract();
	}
	
	
	//下拉信息
	$(".my-info a").click(function(){
		$(".contract-detail").slideToggle();
	});
	//个人信息页面
	$(".my-info b").click(function(){
		window.location.href="personalInfomation.html";//个人信息页
	});
	
	//-------------功能菜单的跳转---------------
	$("#earlyRepay").click(function(){
		earlypay("earlypay.html");//提前还款
	});
	$("#repayRecord").click(function(){
		repayRecord("repayRecordQuery.html");//还款记录
	});
	$("#selectcontract").click(function(){
		selectcontract("selectcontract.html");//切换合同
	});
	$("#changecontact").click(function(){
		changecontact("changecontact.html");//联系方式变更
	});
	$("#uploadcertificate").click(function(){
		uploadcertificate("uploadcertificate.html");//上传还款凭证
	});
	$("#accountSettings").click(function(){
		//无论是否处于绑定状态账号设置都能正常跳转
		window.location.href="accountSettings.html";//账号设置
	});
	
});
function getHeadImg(openid){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getWxUserInfo",{openid:openid},function(data) {
		$.hideLoadMsg();
		//console.log(data);
		if("0"==data.errcode){
			$(".my-photo img").attr('src',data.headimgurl);
			sessionStorage.setItem("headimgurl", data.headimgurl);
		}
	});
}
//获取绑定的合同信息
function getBindingContract(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//console.log(data);
		$.hideLoadMsg();
		if(data.bindingJudg=="false"){
			$("#bindingError").show();
			$("#bindingOk").hide();
			$(".my-ico").show();
			showMessage(data.promptinfo,"err");//显示对应状态的提示信息，L或N的结清情况后台已做判断
			$.hideLoadMsg();
			judg="err";
			return;
		}
		var contract=data.contract;
		if("0" == data.errcode){
			$("#bindingOk").removeClass("second-banner");
			$("#bindingOk").addClass("loan-home");
			$("#bindingError").hide();
			$("#bindingOk").show();
			$(".my-ico").hide();
			showMessage(contract.promptinfo,"ok");//显示对应状态的提示信息，L或N的结清情况后台已做判断
			judg="ok";
			//获取合同状态
			contract_status=contract.contract_status;
			contract_amount=contract.contract_amount;
			contract_nbr=contract.contract_nbr;
			overdue=contract.overdue;
			overdue_amt=contract.overdue_amt;
			business_partner_nbr=contract.business_partner_nbr;
			phone_nbr=contract.phone_nbr;
			//我的信息拼装
			$(".my-info b").append(contract.business_partner_name);//姓名
			$(".my-info a").append("合同号："+contract.contract_nbr+"");//合同号
			//合同信息拼装
			var temp="<li>车型："+contract.asset_brand+"</li>"
					+"<li>贷款金额："+fmoney(contract.financed_amt)+"元</li>"
					+"<li>贷款期限："+contract.contract_term+"期</li>"
					+"<li>合同开始时间："+dateFormat(contract.start_date)+"</li>"
					+"<li>合同截止日期："+dateFormat(contract.end_date)+"</li>"
					+"<li>贷款方式："+contract.financial_product+"</li>"
					+"<li>还款账户："+bankName(contract.bankName)+"&nbsp;"+bankHide(contract.account_nbr)+"</li>"
					+"<li>预留手机："+phone_nbr+"</li>";
			$(".flex ul").append(temp);
			//如果已经结清，则去获取最新活动作为滚动通知
			if(contract_status=="N"){
				var overRepay_temp="<p><i>您的贷款已经结清</i></p>";
				$(".contract-overview").append(overRepay_temp);
			}
			else if(overdue>0){
				//展示逾期金额和天数
				overRepay(overdue,overdue_amt);
			}else{
				//展示还款计划
				getRepayPlan(contract.contract_nbr);
			}
		}
		else{
			message(data.errmsg, function(flag){
				history.go(-1);
			});
		}
		
	});
}
//逾期展示 
function overRepay(overdue,overdue_amt){
	var overRepay_temp="<p><span>已逾期</span></p>"
					  +"<p>¥<i>"+fmoney(overdue_amt)+"</i></p>"
					  +"<p>逾期天数："+overdue+"天</p>"	;
	$(".contract-overview").append(overRepay_temp);
}

function bankName(para){
	if(para==null || para==undefined || para=="null"){
		return "";
	}
	return para;
}

function showMessage(data,type){
	var padding=parseFloat($("#okRemove").css("padding-left"))+parseFloat($("#okRemove").css("padding-right"));
	var containSize;
	if(type=="ok"){
		containSize=parseFloat($("#infoListOk").css("width"))-padding;
	}else{
		containSize=parseFloat($("#infoListErr").css("width"))-padding;
	}
	//一段提示文字能每行展示多少个（现在样式的高度固定两行文字）
	var wordCount=containSize/parseFloat($("#okRemove").css("font-size"));
	//居中显示时使用
	var lineHeight=$("#okRemove").css("height");
	var temp="";
	//html写空一个li标签用于获取相关属性，获取后删除
	$("#okRemove").remove();
	//每段轮播是2行文字，最多轮播3条，后台查询出以个人信息先开始的排序
	var length=3;
	var total="true";
	if(data.length < 3){
		length=data.length;
		total="false";
	}
	for (var i = 0; i < length; i++) {
		//多于2行提示信息，分成两段展示。目前不支持超过2段轮播的长文字。
		//若有新变化可以新添data[i].msg_content.length>wordCount*4
		if(data[i].msg_content.length>wordCount*2){
			//分成两段轮播了两条 ，轮播条数-1
			if(total=="true"){
				length--;
			}
			if(data[i].link_address!=null && data[i].link_address!=""){
				temp+="<li><a href='"+data[i].link_address+"' >"//style='color:white'
					  +data[i].msg_content.substring(0,wordCount*2)+"<a/></li>";
				temp+="<li><a href='"+data[i].link_address+"' >"
					  +data[i].msg_content.substring(wordCount*2)+"<a/></li>";
			}
			else{
				temp+="<li>"+data[i].msg_content.substring(0,wordCount*2)+"</li>";
				temp+="<li>"+data[i].msg_content.substring(wordCount*2)+"</li>";
			}
		}
		//只有一行提示信息
		else if(data[i].msg_content.length<wordCount){
			if(data[i].link_address!=null && data[i].link_address!=""){
				//居中显示
				temp+="<li><a href='"+data[i].link_address+"' style='line-height:"+lineHeight+"'>"
					  +data[i].msg_content+"<a/></li>";
			}
			else{
				temp+="<li>"+data[i].msg_content+"</li>";
			}
			
		}
		//2行提示信息 即大于wordCount 小于wordCount*2
		else{
			if(data[i].link_address!=null && data[i].link_address!=""){
				temp+="<li><a href='"+data[i].link_address+"'>"
					  +data[i].msg_content+"<a/></li>";
			}
			else{
				temp+="<li>"+data[i].msg_content+"</li>";
			}
		}
	}
	if(type=="ok"){
		if(length>1){
			$("#infoListOk").append(temp);
			//动画轮播特效
			$("#okNotice").slide({mainCell:".bd ul",autoPage:true,effect:"topLoop",autoPlay:true,vis:1,interTime:6000,mouseOverStop:"false"});
		}else{
			$("#infoListErr").append(temp);
			$("#errNotice").slide({mainCell:".bd ul",autoPage:false,effect:"topLoop",autoPlay:true,vis:1,interTime:6000,mouseOverStop:"false"});
		}
		
	}else{
		temp+="<li>尊敬的用户，绑定账号后可进行贷款查询及业务办理，请点击上方进行绑定。</li>";
		$("#infoListErr").append(temp);
		//动画轮播特效
		$("#errNotice").slide({mainCell:".bd ul",autoPage:false,effect:"topLoop",autoPlay:true,vis:1,interTime:6000,mouseOverStop:"false"});
	}
}
//function msgType(para){
//	if(para==1 || para=="1"){
//		return "【个人消息】";
//	}else if(para==2 || para=="2"){
//		return "【通知消息】";
//	}
//	return para;
//}
//获取还款计划
function getNowFormatDate() {
    var date = new Date();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear()  + month  + strDate;
    return currentdate;
} 

function getRepayPlan(contract_nbr){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/queryEarlyRepayApply",{openId: openid},function(data) {
		$.hideLoadMsg();
		if(data.errcode=="0"){
			var list=data.returnList;
			for (var i = 0; i < list.length; i++) {
				var date1=0;
				date1=list[i].apply_repay_date;//申请日期
				var date2=getNowFormatDate();//当前日期
				if(list[i].status != "4" && list[i].status != "5" && list[i].contract_code==contract_nbr&&date1>=date2 ){
					var repayPlan_temp="<p><span>待还款</span></p>"
									  +"<p>¥<i>"+fmoney(list[i].total_amt)+"</i></p>"
									  +"<p>扣款日期："+dateFormat(list[i].apply_repay_date+"")+"</p>"	;
					$(".contract-overview").append(repayPlan_temp);
					return;
				}
			}
			$.showLoadMsg("加载中，请稍后...");
			$.JsonSRpc(baseUrl+ "/queryRepayRecord",{openId:openid},function(data) {
				$.hideLoadMsg();
				if("0"==data.errcode){
					//var repayment=JSON.parse(data.repayment);
					//获得待还款计划

					var norepayment=JSON.parse(data.norepayment);
					var norepaymentterm=data.norepayment.no_repayment_term;
					if(norepaymentterm==1){
						//var repayment_plan=JSON.parse(norepayment.repayment_plan);
						//下次还款日期
						var nextdate=norepayment.repayment_plan.record.date;
						//下次还款金额
						var nextmoney=norepayment.repayment_plan.record.money;
						var repayPlan_temp="<p><span>待还款</span></p>"
										  +"<p>¥<i>"+fmoney(nextmoney)+"</i></p>"
										  +"<p>扣款日期："+dateFormat(nextdate)+"</p>"	;
						$(".contract-overview").append(repayPlan_temp);
					}
					else{
						var norepayment=JSON.parse(data.norepayment).repayment_plan;
						//下次还款日期
						var nextdate=norepayment[0].date;
						//下次还款金额
						var nextmoney=norepayment[0].money;
						var repayPlan_temp="<p><span>待还款</span></p>"
										  +"<p>¥<i>"+fmoney(nextmoney)+"</i></p>"
										  +"<p>扣款日期："+dateFormat(nextdate)+"</p>"	;
						$(".contract-overview").append(repayPlan_temp);
					}
					
				}
				else{
					message(data.errmsg);
				}
			});
		}else{
			$.showLoadMsg("加载中，请稍后...");
			$.JsonSRpc(baseUrl+ "/queryRepayRecord",{openId:openid},function(data) {
				$.hideLoadMsg();
				if("0"==data.errcode){
					//var repayment=JSON.parse(data.repayment);
					//获得待还款计划
					//var norepayment=JSON.parse(data.norepayment).repayment_plan;
					//下次还款日期
					//var nextdate=norepayment[0].date;
					//下次还款金额
					//var nextmoney=norepayment[0].money;
					//var repayPlan_temp="<p><span>待还款</span></p>"
									//  +"<p>¥<i>"+fmoney(nextmoney)+"</i></p>"
									//  +"<p>扣款日期："+dateFormat(nextdate)+"</p>"	;
					//$(".contract-overview").append(repayPlan_temp);
					
					
					var norepayment=JSON.parse(data.norepayment);
					
					var norepaymentterm=norepayment.no_repayment_term;
					
					
					
					if(norepaymentterm==1){
						//var repayment_plan=JSON.parse(norepayment.repayment_plan);
						//下次还款日期
						var nextdate=norepayment.repayment_plan.record.date;
						//下次还款金额
						var nextmoney=norepayment.repayment_plan.record.money;
						var repayPlan_temp="<p><span>待还款</span></p>"
										  +"<p>¥<i>"+fmoney(nextmoney)+"</i></p>"
										  +"<p>扣款日期："+dateFormat(nextdate)+"</p>"	;
						$(".contract-overview").append(repayPlan_temp);
					}
					else{
						var norepayment=JSON.parse(data.norepayment).repayment_plan;
						//下次还款日期
						var nextdate=norepayment[0].date;
						//下次还款金额
						var nextmoney=norepayment[0].money;
						var repayPlan_temp="<p><span>待还款</span></p>"
										  +"<p>¥<i>"+fmoney(nextmoney)+"</i></p>"
										  +"<p>扣款日期："+dateFormat(nextdate)+"</p>"	;
						$(".contract-overview").append(repayPlan_temp);
					}
				}
				else{
					message(data.errmsg);
				}
			});
		}
	});
	
}

//提前还款的跳转
function earlypay(jumpUrl){
	if(judg!="ok"){
		message("请绑定账号后再进行操作", function(flag){
			if(flag){
				window.location.href="./bindingProtocol.html";
			}
		}, true);
	}
	else{
		if(contract_status=="L"){
			window.location.href=jumpUrl;
		}
		else {
			message("您的贷款（合同号"+contract_nbr+"）已结清，更多贷款产品详见“最新活动”");
		}
	}
}
//还款记录的跳转
function repayRecord(jumpUrl){
	if(judg=="ok"){
		window.location.href=jumpUrl;
	}else{
		message("请绑定账号后再进行操作", function(flag){
			if(flag){
				window.location.href="./bindingProtocol.html";
			}
		}, true);
	}
}
//切换合同的跳转
function selectcontract(jumpUrl){
	if(judg!="ok"){
		message("请绑定账号后再进行操作", function(flag){
			if(flag){
				window.location.href="./bindingProtocol.html";
			}
		}, true);
	}
	else{
		if(parseInt(contract_amount)>1){
			window.location.href=jumpUrl;
			return;
		}else{
			message("您只有一个车辆贷款合同，无需切换。");
			return;
		}
//		if(contract_status=="L"){
//			if(parseInt(contract_amount)>1){
//				window.location.href=jumpUrl;
//				return;
//			}else{
//				message("您只有一个车辆贷款合同，无需切换。");
//				return;
//			}
//		}
//		message("您的贷款（合同号"+contract_nbr+"）已结清，更多贷款产品详见“最新活动”");
	}
}
//联系方式变更的跳转
function changecontact(jumpUrl){
	if(judg!="ok"){
		message("请绑定账号后再进行操作", function(flag){
			if(flag){
				window.location.href="./bindingProtocol.html";
			}
		}, true);
	}
	else{
		if(contract_status=="L"){
			window.location.href=jumpUrl;
		}
		else {
			message("您的贷款（合同号"+contract_nbr+"）已结清，更多贷款产品详见“最新活动”");
		}
	}
}
//上传还款凭证的跳转
function uploadcertificate(jumpUrl){
	/*if(judg!="ok"){
		message("请绑定账号后再进行操作", function(flag){
			if(flag){
				window.location.href="./bindingProtocol.html";
			}
		}, true);
	}
	else{
		if(contract_status=="L"){
			$.showLoadMsg("数据加载中，请稍后...");
			$.JsonSRpc(baseUrl+"/getcurrentcontract", {openid:openid}, function(data){
				$.hideLoadMsg();
				if(data.errcode&&"0"!=data.errcode){
					flag = false;
					message(data.errmsg);
				}else{
					window.location.href=jumpUrl;
				}
			});
		}
		else {
			message("您的贷款（合同号"+contract_nbr+"）已结清，更多贷款产品详见“最新活动”");
		}
	}*/
	
	message("敬请期待！");
}

//日期格式
function dateFormat(para){
	para = para.replace(/-/g, "");
	var year=para.toString().substring(0,4);
	var moon=para.toString().substring(4,6);
	var day=para.toString().substring(6,8);
	return year+"年"+moon+"月"+day+"日";
}
//隐藏银行卡中间数字
function bankHide(para){
	var frontShow=para.toString().substring(0,4);
	var afterShow=para.toString().substring(para.length-4);
	for (var i = 4; i < para.length-4; i++) {
		frontShow+="*";
	}
	return frontShow+afterShow;
}

//获取openid
function getOpenId(code){
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: code},function(data) {
		$.hideLoadMsg();
		console.log(data);
		if(data.errcode!=null && data.errcode!=null &&"0"==data.errcode){
			openid = data.openid;
			if(openid==undefined||""==openid){
				message("信息获取失败，请重新进入页面", function(flag){
					wx.closeWindow();
				});
				return;
			}else{
				sessionStorage.setItem("openid", openid);
				//获取用户头像地址
				getHeadImg(openid);
				//获取绑定合同信息,根据绑定合同确定展示的还款信息类型
				getBindingContract();
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
}


