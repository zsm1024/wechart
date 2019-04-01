$(function(){
	init();	
	getBindingContract();
	pageConfig();
})
function getBindingContract(){
	$.showLoadMsg("加载中，请稍后...");
	var openid=localStorage.getItem("openid");
	//alert(openid);
	$.JsonSRpc(baseUrl+ "/getBindingContract",{openId:openid},function(data) {
		//alert(data);
		$.hideLoadMsg();
		var contract=data.contract;
		if("0" == data.errcode){
			var contract_status=contract.contract_status;
			//var contract_amount=contract.contract_amount;
			var contract_nbr=contract.contract_nbr;
			var overdue=contract.overdue;
			var asset=contract.asset_brand;
			var borrower=contract.business_partner_name;
			if(overdue>0){
				message("逾期合同无法进行保险理赔业务！", function(flag){
					history.go(-1);
				});
				return;
			}
			$("#borrower").append("<p>"+borrower+"</p>");
			$("#vehicle").append("<p>"+asset+"</p>");
			$("#contractno").append("<p>"+contract_nbr+"</p>");
			$("#borrower").val(borrower);
			$("#vehicle").val(asset);
			$("#contractno").val(contract_nbr);
			
		}
		else{
			message(data.errmsg, function(flag){
				history.go(-1);
			});
		}
		
	});
}
function init(){

	if(localStorage.classN=="show"){
		$(".postLists form").css("display","block");
		localStorage.clear();
	}	
}
	$(".postLists label").click(function(){
		var $this=$(this);
		if($this.find("input").attr("checked")){
			$this.parent().find("form").show();
		}else{
			$this.parent().find("form").hide();
//			$(".postLists label").css("color","#000");			
		}		
	});
	// 验证中文名称
	function isChinaName(name) {
	 var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
	 return pattern.test(name);
	}
// 验证手机号
	function isPhoneNo(phone) { 
	 var pattern = /^1[34578]\d{9}$/; 
	 return pattern.test(phone); 
	}
	function formValidate() {
		var str="";
		var reportno= $("#reportno").val();
		 //alert(reportno);
		if(reportno !=""){
				$("#reportno").parent().find("label").css("color","#000");
		}else{
			str += "商业车险报案号未填;\n";
			$("#reportno").parent().find("label").css("color","red");
		}	
		
		 var reportdate= $("#reportdate").val();
		if(reportdate !=""){
				$("#reportdate").parent().find("label").css("color","#000");
		}else{
			str += "请选择出险日期;\n";
			$("#reportdate").parent().find("label").css("color","red");
		}	
		 
		//判断资料是否上传
		if($(".addImg label").length==1){
			str +="证明文件未添加;\n"
		}
			// 判断姓名
		 if($.trim($('#recperson').val()).length == 0) {
		  str += '姓名未填写;\n';
			$("#recperson").parent().find("label").css("color","red");
		 } else if(isChinaName($.trim($('#recperson').val())) == false){	   
		   str += '请输入正确的姓名;\n';
			$("#recperson").parent().find("label").css("color","red");	  
		 }else{
			$("#recperson").parent().find("label").css("color","#000");	
		 }
		// 地址验证
		var addHo= $("#recaddress").val();
		if(addHo !=""){
				$("#recaddress").parent().find("label").css("color","#000");
			}else{
				str += "地址未填写;\n";
				$("#recaddress").parent().find("label").css("color","red");
			}			
		// 判断手机号码
		 if ($.trim($('#recmobile').val()).length == 0){ 
			str += '手机号没有输入;\n';
		  //$('#phone').focus();
		  $("#recmobile").parent().find("label").css("color","red");
		 }else if(isPhoneNo($.trim($('#recmobile').val())) == false){   
				str += '请输入正确的手机号;\n';
				$("#recmobile").parent().find("label").css("color","red");	  
		 }else{
			$("#recmobile").parent().find("label").css("color","#000");	  
		 }
			
		 
		 
		 if(str != '') {	 	
		  message(str);
		  return false;
		 } else {		 
		  //$(".secureFT a").attr("href","secureSuccess.html");	
		  return true;
		 }
	}
	$('.secureFT a').on('click', function() {
		if($(".postLists form").is(":visible")){			
			if(!formValidate())
				return ;
			localStorage.setItem("classN","show");
			
		}else{
			var str="";
			if($(".addImg label").length==1){
				str +="证明文件未添加;\n"
			}
			var reportno= $("#reportno").val();
			// alert(reportno);
			if(reportno !=""){
					$("#reportno").parent().find("label").css("color","#000");
			}else{
				str += "商业车险报案号未填;\n";
				$("#reportno").parent().find("label").css("color","red");
			}	
			
			 var reportdate= $("#reportdate").val();
			if(reportdate !=""){
					$("#reportdate").parent().find("label").css("color","#000");
			}else{
				str += "请选择出险日期;\n";
				$("#reportdate").parent().find("label").css("color","red");
			}	
			 if(str != '') {	 	
			  message(str);
			  return ;
			 }
		}
		//alert("begin");
		var model= jsonString();

		// var date=convertDate(new Date());
		// param.timestamp=date;
		// var data=model.replace(/\"/g,"'").replace(/\\/,'');
		// param.data=data;
		// //console.log(data);
		// var s="";
		// data=key+data+date+key;
		// for(var i=0;i<data.length;i++)
		// {
			// s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
		// }
		// var signcode=$.md5(s);
		// param.code='saveSecureOrder';
		// param.signcode=signcode;
		//alert(interfaceUrl);
		
		var data=model.replace(/\"/g,"'").replace(/\\/,'');
		createParam(data,'saveSecureOrder');
		$.JsonSRpc(interfaceUrl, param, function(result){
				if(result)
				{
					if(result.resultCode=='0000')
					{
						if(result.resultData.result=="0")
						{
							message("提交成功！");
							//$(".commen-bottom .perfect").attr("href","successOrder.html");	
							window.location.href="secureSuccess.html";
							$.hideLoadMsg();
						}
						else if(result.resultData.result=="1")
						{
							message("提交异常！");
							$.hideLoadMsg();
						}
						else
						{
							message("数据异常！");
							$.hideLoadMsg();
						}
						
					}else{
						message("提交异常！");
						$.hideLoadMsg();
					}
				}else{
					message("提交异常！");
					$.hideLoadMsg();
				}
			});
		
		//$(".secureFT a").attr("href","secureSuccess.html");
		
});

	$(".input-group input").on("focus",function(){
		this.scrollIntoView();
	});
//图片批量上传

var localIds;
$('.addFiles').on('click', function () { 	

	wx.chooseImage({ 
		count:9, // 默认9
	    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
		success: function (res) {
		localIds = res.localIds;
		leng=localIds.length;
		syncUpload(localIds);
		} 
	}); 			
}); 

var len=0;
var leng=0;
var syncUpload = function(localIds){ 
	var localId = localIds.pop(); 
	wx.uploadImage({ 
		localId: localId,
		isShowProgressTips: 0,
		success: function (res) { 
			len++;
			$.showLoadMsg("上传中第"+len+"张照片，请稍后...");
			var serverId = res.serverId; // 返回图片的服务器端ID
			// var date=convertDate(new Date());
			// param.timestamp=date;
			// var data="{'mediaid':'"+serverId+"'}";	
			// param.data=data;	
			// var s="";
			// data=key+data+date+key;
			// for(var i=0;i<data.length;i++)
			// {
				// s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
			// }
			// var signcode=$.md5(s);
			// param.code='UploadSecureImg';
			// param.signcode=signcode;
			
			var data="{'mediaid':'"+serverId+"','imageType':'0'}";	
			createParam(data,'UploadSecureImg');
			$.JsonSRpc(interfaceUrl, param, function(result){
				if(result.resultCode=='0000')
				{
					var str1="";
					str1+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+localId+"' data-preview-src=''  data-preview-group='4'/></span>";
					$(".addFiles").before(str1);
					var uploadimgs=$("#files").val()+","+result.resultData.result;
					$("#files").val(uploadimgs);
					if(leng==len)
					{
						$.hideLoadMsg();
					}
					else
					{
						syncUpload(localIds);
					}
						
					
				}
					//alert(1);
				else if(result.resultCode=='0001'){
					//$.hideLoadMsg();
					message("上传失败！");
					if(leng==len)
					{
						$.hideLoadMsg();
					}
				}
				else{
					//$.hideLoadMsg();
					if(leng==len)
					{
						$.hideLoadMsg();
					}
				}
			});
			
			
		} 
	}); 
};

function jsonString(){
	var model=new Object();
	model.openid=localStorage.getItem("openid");
	model.borrower=$("#borrower").val();
	model.contractno=$("#contractno").val();
	model.vehicle=$("#vehicle").val();
	model.inimg=$("#files").val();
	//alert($("#isoriginal").attr("checked"));
	if($("#isoriginal").attr("checked")){
		model.isoriginal='1';
	}else
	{
		model.isoriginal='0';
	}
	//model.isoriginal=$("#isoriginal").val()||'0';
	model.recperson=$("#recperson").val();
	model.recaddress=$("#recaddress").val();
	model.recmobile=$("#recmobile").val();
	model.appfrom=2;
	model.reportno=$("#reportno").val();
	model.reportdate=$("#result").val();
	model.type='1';
	model.status='2';
	model.statusex='2';
	//alert(JSON.stringify(model));
	
	return JSON.stringify(model);
	
	
} 