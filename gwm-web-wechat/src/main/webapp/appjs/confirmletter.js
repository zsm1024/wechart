$(function(){
	var name=sessionStorage.getItem("business_partner_name");
	var id=sessionStorage.getItem("business_partner_nbr");
	var applydate=sessionStorage.getItem("repaydate");
	var ht=sessionStorage.getItem("contract_nbr");
	var mondy=sessionStorage.getItem("total");
	var currentdate=getNowFormatDate();
	var applyyear=applydate.substring(0,4);
	var applymon=applydate.substring(5, 7);
	var applyday=applydate.substring(8, 10);
	applydate=applyyear+"年"+applymon+"月"+applyday+"日";
//	applydate=
//	var content=document.getElementById("content");
//	content.innerHtml='<p>借款人***（身份证件号码：*************** ）已于******向本公司提出提前清偿合同编号为 **********的《汽车抵押贷款合同》（以下简称“抵押贷款合同”）项下的全部贷款本息及其他应付费用及款项的申请。天津长城滨银汽车金融有限公司（以下简称“我公司”）特此同意。</p><p>借款人同意按本确认函要求的金额和期限支付抵押贷款合同项下所有未归还的本息及其他应付费用及款项。</p><p>1.	截至********（以下简称“约定日”），在抵押贷款合同项下借款人的提前还款应付总额为*********元。（以下简称“提前还款应付总额”）。</p><p>2.	借款人应确保在约定日期，委托扣款授权书中规定的贷款还款账户中可用余额不少于提前还款应付总额，并同意我公司于上述日期或之前从贷款还款账户中扣收提前还款应付总额。</p><p>3.	为避免歧义，上文第1条规定的提前还款应付总额仅供参考，借款人提前还款义务的最终完成以我公司出具的《贷款结清函》为准。</p><p>4.	本通知函自签发日期起生效，有效期至约定日期上午10:00止。本通知函及借款人的以下书面确认均可以传真方式签署，传真签署与当面签署具有同等的法律效力。如在约定日期上午10:00前，借款人未将相当于提前还款应付总额的款项存入贷款还款账户，或届时贷款还款账户内的可用资金少于提前还款应付总额的，本通知函自动失效，借款人仍应按抵押贷款合同规定的期限和方式归还贷款本息。如借款人仍希望提前归还贷款的，应重新向我公司提出提前还款申请。</p><p>签发日期：*******</p><p><b>借款人确认：</b></p><p>本人已收悉上述提前还款确认函，并同意我公司从贷款还款账户中扣收该确认函中所载明的提前还款应付总额。</p>';
	$("#content").html('<p>借款人'+name+'已于'+formateDate(currentdate)+'向本公司提出提前清偿合同编号为 '+ht+'的《汽车抵押贷款合同》（以下简称“抵押贷款合同”）项下的全部贷款本息及其他应付费用及款项的申请。天津长城滨银汽车金融有限公司（以下简称“我公司”）特此同意。</p><p>借款人同意按本确认函要求的金额和期限支付抵押贷款合同项下所有未归还的本息及其他应付费用及款项。</p><p>1.	截至'+applydate+'（以下简称“约定日”），在抵押贷款合同项下借款人的提前还款应付总额为'+mondy+'元。（以下简称“提前还款应付总额”）。</p><p>2.	借款人应确保在约定日期，委托扣款授权书中规定的贷款还款账户中可用余额不少于提前还款应付总额，并同意我公司于上述日期或之前从贷款还款账户中扣收提前还款应付总额。</p><p>3.	为避免歧义，上文第1条规定的提前还款应付总额仅供参考，借款人提前还款义务的最终完成以我公司出具的《贷款结清函》为准。</p><p>4.	本通知函自签发日期起生效，有效期至约定日期上午10:00止。本通知函及借款人的以下书面确认均可以传真方式签署，传真签署与当面签署具有同等的法律效力。如在约定日期上午10:00前，借款人未将相当于提前还款应付总额的款项存入贷款还款账户，或届时贷款还款账户内的可用资金少于提前还款应付总额的，本通知函自动失效，借款人仍应按抵押贷款合同规定的期限和方式归还贷款本息。如借款人仍希望提前归还贷款的，应重新向我公司提出提前还款申请。</p><p>签发日期：'+formateDate(currentdate)+'</p><p><b>借款人确认：</b>本人已收悉上述提前还款确认函，并同意我公司从贷款还款账户中扣收该确认函中所载明的提前还款应付总额。</p>');
});

function formateDate(date){
	if(date!=''&&date!=undefined&&date.length==8){
		var year=date.substring(0,4);
		var mon=date.substring(4,6);
		var day=date.substring(6,8);
		return year+'年'+mon+'月'+day+'日';
	}
}

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