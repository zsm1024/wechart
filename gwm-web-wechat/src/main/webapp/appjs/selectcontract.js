var contractData;
var openid;
$(function(){
	openid = sessionStorage.openid;
	if(openid == undefined || openid.length <= 0){
		history.go(-1);
		return;
	}
	$.JsonSRpc(baseUrl+"/getcontractinfo", 
			{openid:openid}, function(data){
		if(data.errcode&&data.errcode!="0"){
			message(data.errmsg, function(flag){
				location.href=history.go(-1);
			});
			return;
		}
		var contracts = JSON.parse(data.contracts);
		var d = '';
		contractData = contracts;
		for(var i = 0; i < contracts.length; i++){
			var contract_status;
			if(contracts[i].contract_status=="L"){
				contract_status = "正常";
			}else if(contracts[i].contract_status=="N"){
				contract_status = "结清";
			}else{
				contract_status = contracts[i].contract_status;
			}
			d += '<dt><a href="javascript:void(0);" onclick="selectContract(\''+i+'\')">合同编号：'+contracts[i].contract_nbr+'</a></dt>'
				+'<dd>'
				+'<p><label style="text-align:right">还款状态：</label>'+contract_status+'</p>'
				+'<p><label style="text-align:right">车型：</label>'+contracts[i].asset_brand+'</p>'
				+'<p><label style="text-align:right">贷款金额：</label>'+fmoney(contracts[i].financed_amt, 2)+'</p>'
				+'<p><label style="text-align:right">贷款期限：</label>'+contracts[i].contract_term+'个月</p>'
				+'</dd>';
		}
		$(".xzht").html(d);
	});
});

function selectContract(n){
	var contract_id = contractData[n].contract_id;
	var contract_nbr = contractData[n].contract_nbr;
	var business_partner_name = contractData[n].business_partner_name;
	$.JsonSRpc(baseUrl+"/wxchangecontract", {openid:openid,contract_id:contract_id, contract_nbr:contract_nbr,business_partner_name:business_partner_name}, function(data){
		if(data.errcode&&data.errcode!="0"){
			message(data.errmsg);
			return;
		}else{
			message("您已成功绑定合同", function(flag){
				window.location.href = "./myLoan.html";
			});
		}
	});
}