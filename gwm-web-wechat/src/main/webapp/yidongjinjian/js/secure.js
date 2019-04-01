$(".HeaderTitle").find("a").click(function(){
	var $this=$(this);
	$this.addClass("as").siblings().removeClass("as");	
});
//var openid="oImjOwLUFBN6V9h-txVjZIG5MqMw";
var openid="";
var dataMap=new Map();
var order=new Object();
$(document).ready(function(){
	$.showLoadMsg("加载中，请稍后...");
	var wh=$(window).height();
	var mh=$(".commen-header").height();
	var ch=$(".commenHeader").height();
	var th=$(".tab-bar").height();
	var h=wh-ch-th-mh;
	$(".OrderRecord").css("height",h);
	$(".carLists").css("height",h);
	var str="";
	
	//localStorage.clear();
	//alert(getParam("code"));
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: getParam("code")},function(data) {
		
		//alert(data.errcode);
		if(data.errcode!=null && data.errcode!=null ){
			//alert(localStorage.getItem("openid"));
			openid = data.openid||localStorage.getItem("openid");
			//alert(openid);
			if(openid==undefined||""==openid){
				$.hideLoadMsg();
				message("信息获取失败，请重新进入页面", function(flag){
					wx.closeWindow();
				});
				return;
			}else{
				localStorage.setItem("openid", openid);
				 
				var date=convertDate(new Date());
				param.timestamp=date;
			
				param.data=data;
				
				var s="";
				data=key+data+date+key;
				//alert(data);
				for(var i=0;i<data.length;i++)
				{
					s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
				}
				var signcode=$.md5(s);
				
				param.code='getSecureList';
				param.signcode=signcode;
				var data="{'openid':'"+openid+"'}";
				createParam(data,'getSecureList');
				
				//alert(interfaceUrl);
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							var html="";
							var shistory="";
							if(result.resultData==null){
								$.hideLoadMsg();
								return;
							}
							$.each(result.resultData,function(i,item){
								//alert(item.status);
								if(item.status!="3" ){
									//alert(1)
									html+="<ul><li><a href='#' onclick=showHistory('"+item.id+"')>"+item.vehicle+"</a><a class='statues' href='#' onclick=showHistory('"+item.id+"')>"+item.statusdsc+"</a></li><li><a href='#' onclick='showHistory('"+item.id+"')'>"+item.contractno+"</a><a href='#'  onclick='showHistory('"+item.id+"')'>"+item.appdate+"</a></li></ul>";
								}else{
									shistory+="<ul><li><a href='#' onclick=showHistory('"+item.id+"')>"+item.vehicle+"</a><a class='statues' href='#' onclick='showHistory('"+item.id+"')'>"+item.statusdsc+"</a></li><li><a href='#' onclick='showHistory('"+item.id+"')'>"+item.contractno+"</a><a href='#'  onclick='showHistory('"+item.id+"')'>"+item.appdate+"</a></li></ul>";
								}
								 dataMap.set("acs"+item.id,item);
							})
							$("#carLists").append(html);	
							$("#PayLists").append(shistory);	
							$.hideLoadMsg();

						}else{
							$.hideLoadMsg();
							message("获取信息失败！");
						
							
						}
					}else{
						$.hideLoadMsg();
						message("获取信息失败！");
						
						
					}
				});
				
				
				
			}
		}else{
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
	
	
	//str+="<ul><li><a href='#AcceptInfo'>蓝标哈弗H6</a><a class='statues' href='#AcceptInfo'>受理中</a></li><li><a href='#AcceptInfo'>GW-A123456789</a><a href='#AcceptInfo'>2017-6-18 08:55</a></li></ul><ul><li><a href='#AcceptInfo'>蓝标哈弗H6</a><a class='statues' href='#AcceptInfo'>退回</a></li><li><a href='#AcceptInfo'>GW-A123456789</a><a href='#AcceptInfo'>2017-6-18 08:55</a></li></ul>"						
		
		
	// $("#carLists").find("ul").each(function(){

		// var $this=$(this);
		// $this.click(function(){	
		// var hm=	$this.find(".statues").html();
			// if(hm=="受理中"){
				// $(".IMfiles").find(".reLoadImg").unbind("click");
				// var str="返回";
				// $(".Re-load .preBack").html(str);
				// $(".IMfiles .reLoadImg").hide();
				// $(".IMfiles .carContainer").css({"float":"left","margin-left":".05rem"})
				// return;
			// }else{
				// $(".IMfiles").find(".reLoadImg").each(function(){
					// var $that=$(this);
					// $that.click(function(){
						// var a=$that.siblings(".reLoadImg1").find("img");
						// ChoImg(a);		
						
					// });	
				// })
				// var str="提交";
				// $(".Re-load .preBack").html(str);
				// $(".IMfiles .reLoadImg").show();
				// $(".IMfiles .carContainer").css({"float":"none","margin-left":"0rem"})
				
			// }
			
			
		// });
	// });		
});

function downloadImage(id)
{
	//window.location.href="http://10.50.129.82/Handler/ImageView.ashx?dataid=00001J1ZODBOI3919A0Q";
	$.showLoadMsg("下载中，请稍后...");
	var data="{'mediaid':'"+id+"'}";
				createParam(data,'uploadWxImg');
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							//alert(result.resultData.medis_id);
							wx.downloadImage({
								serverId: result.resultData.media_id,
								isShowProgressTips: 0,
								success: function (res) { 
									message("下载成功！");
									$.hideLoadMsg();
								}
							});


						}else{
							$.hideLoadMsg();
							message("下载失败！");
						
							
						}
					}else{
						$.hideLoadMsg();
						message("下载失败！");
						
						
					}
				});
	
}



function showHistory(id){
	order=dataMap.get('acs'+id);
	//alert(order["reportno"]);
	//alert(order["status"]);
	if(order["status"]=="2" ){
		$("#inborrower").html("<p>"+order["borrower"]+"</p>");
		$("#incontractno").html("<p>"+order["contractno"]+"</p>");
		$("#invehicle").html("<p>"+order["vehicle"]+"</p>");
	
		
		$("#reportno").html("<p>"+order["reportno"]+"</p>");
		$("#reportdate").html("<p>"+order["reportdate"]+"</p>");
		if(order["inimg"]!=null)
		{
			var data="{'mediaid':'"+order["inimg"]+"'}";
				createParam(data,'downloadSecureImg');
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							var str="";
							$.each(result.resultData,function(i,item){
								if(order["status"]=="21" ||order["status"]=="31"){
									var a=item.result.indexOf("dataid=");
									var sstr=item.result.substring(a+7,item.result.length);
									str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="1" /></span><span class="reLoadImg" onclick=addimg("2","'+sstr+'",this)>重新上传</span></label>';
									$("#UPNewImg").show();
									var str1="提交";
									$(".Re-load .preBack").html(str1);
									$(".IMfiles .reLoadImg").show();
									$(".IMfiles .carContainer").css({"float":"none","margin-left":"0rem"})
								}
								else{
									str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="2" /></span></label>';
									$("#UPNewImg").hide();
									$(".IMfiles").find(".reLoadImg").unbind("click");
									var str1="返回";
									$(".Re-load .preBack").html(str1);
									$(".IMfiles .reLoadImg").hide();
									$(".IMfiles .carContainer").css({"float":"left","margin-left":".05rem"})
								}
								
							})
							$("#inimgs").html(str);	
							$.hideLoadMsg();

						}else{
							$.hideLoadMsg();
							message("获取信息失败！");
						
							
						}
					}else{
						$.hideLoadMsg();
						message("获取信息失败！");
						
						
					}
				});	
		}
		
		
		
		
		// if(hm=="受理中"){
				// $(".IMfiles").find(".reLoadImg").unbind("click");
				// var str="返回";
				// $(".Re-load .preBack").html(str);
				// $(".IMfiles .reLoadImg").hide();
				// $(".IMfiles .carContainer").css({"float":"left","margin-left":".05rem"})
				// return;
			// }else{
				// $(".IMfiles").find(".reLoadImg").each(function(){
					// var $that=$(this);
					// $that.click(function(){
						// var a=$that.siblings(".reLoadImg1").find("img");
						// ChoImg(a);		
						
					// });	
				// })
				// var str="提交";
				// $(".Re-load .preBack").html(str);
				// $(".IMfiles .reLoadImg").show();
				// $(".IMfiles .carContainer").css({"float":"none","margin-left":"0rem"})
				
			// }
		window.location.href="#AcceptInfo"
	}else
	{
		var memo=order["msg"]||'';
		//alert(order["msg"]);
		$("#emo").html("<p>"+memo+"</p>");
		$("#historyBorrower").html("<p>"+order["borrower"]+"</p>");
		$("#historyContractNo").html("<p>"+order["contractno"]+"</p>");
		$("#historyVehicle").html("<p>"+order["vehicle"]+"</p>");
		var company=order["express"]||'';
		$("#historyPostCompany").html("<p>"+company+"</p>");
		var postno=order["postno"]||'';
		$("#historyPostNo").html("<p>"+postno+"</p>");
		
		$("#reportnoh").html("<p>"+order["reportno"]+"</p>");
		$("#reportdateh").html("<p>"+order["reportdate"]+"</p>");
		
		if(order["inimg"]!=null)
		{
			var data="{'mediaid':'"+order["inimg"]+"'}";
				createParam(data,'downloadSecureImg');
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							var str="";
							$.each(result.resultData,function(i,item){
								//str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="1" /></span><span class="reLoadImg">重新上传</span></label>';
								str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="3" /></span></label>'
							})
							$("#himgs").html(str);	
							$.hideLoadMsg();

						}else{
							$.hideLoadMsg();
							message("获取信息失败！");
						
							
						}
					}else{
						$.hideLoadMsg();
						message("获取信息失败！");
						
						
					}
				});	
		}
		
		if(order["backinimg"]!=null)
		{
			var data="{'mediaid':'"+order["backinimg"]+"'}";
				createParam(data,'downloadSecureImg');
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							var str="";
							$.each(result.resultData,function(i,item){
								var a=item.result.indexOf("dataid=");
								var sstr=item.result.substring(a+7,item.result.length);
								//str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="1" /></span><span class="reLoadImg">重新上传</span></label>';
								// str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="2" /></span><span class="reLoadImg" onclick=downloadImage("'+sstr+'")>下载</span></label>'
								//str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="2" /></span><label class="LoadSecImg"><a href="#" onclick=downloadImage("'+sstr+'")>下载</a></label></label>';
								str+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+item.result+'" data-preview-src="" data-preview-group="2" /></span></label>';
							})
							$("#hbimgs").html(str);	
							$.hideLoadMsg();

						}else{
							$.hideLoadMsg();
							message("获取信息失败！");
						
							
						}
					}else{
						$.hideLoadMsg();
						message("获取信息失败！");
						
						
					}
				});	
		}
		
		$(".IMfiles").find(".reLoadImg").unbind("click");
		var str="返回";
		$(".Re-load .preBack").html(str);
		$(".IMfiles .reLoadImg").hide();
		$(".IMfiles .carContainer").css({"float":"left","margin-left":".05rem"})
		
		window.location.href="#settleClaim"
		
	}
}

var localIds;
//选择图片
// function ChoImg(a){
	// wx.chooseImage({
	    // count:9, // 默认9
	    // sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    // sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
	    // success: function (res) {
	    // localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
		// a.attr("src",localIds);		
		// }	   
	// });	
		
 
// }


function recommit(btn){
	
	
	if($(btn).html()=="提交")
	{
		$.showLoadMsg("重新提交中，请稍后...");
		var status=order["status"];
		if(order["status"]=="21"){
			status="22";
		}else if(order["status"]=="31"){
			status="32";
		}else {
			
		}
			
		var data="{'status':'"+status+"','shopid':'"+order["id"]+"'}";
			createParam(data,'recommit');
			$.JsonSRpc(interfaceUrl, param, function(result){
				if(result)
				{
					if(result.resultCode=='0000')
					{
						$.hideLoadMsg();
					    window.location.href="secure.html";
						message("重新提交成功！");

					}else{
						$.hideLoadMsg();
						message("提交失败！");
					
						
					}
				}else{
					$.hideLoadMsg();
					message("提交失败！");
					
					
				}
			});	
			
	}else
	{
		window.location.href="secure.html";
	}
}

function addimg(type,oldimgid,btn) { 	

	wx.chooseImage({ 
		count:9, // 默认9
	    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
		success: function (res) {
		localIds = res.localIds;
//		alert(localIds);
		for(var i=0;i<localIds.length;i++){
			//var str1="";
			//str1+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+localIds[i]+"' data-preview-src=''  data-preview-group='4'/></span>";
			//$(".addFiles").before(str1);
			//alert(localIds[i]);
			syncUpload(localIds[i],type,oldimgid,btn);
		}
		
		} 
	}); 			
}

var syncUpload = function(localId,type,oldimgid,btn){ 
	//var localId = localIds.pop(); 
	wx.uploadImage({ 
		localId: localId,
		isShowProgressTips: 1,
		success: function (res) { 
			$.showLoadMsg("上传中，请稍后...");
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
			
			var data="{'mediaid':'"+serverId+"','imageType':'"+type+"','shopid':'"+order["id"]+"','oldimgid':'"+oldimgid+"'}";	
			createParam(data,'UploadSecureImg');
			$.JsonSRpc(interfaceUrl, param, function(result){
				if(result.resultCode=='0000')
				{
					var str1="";
					
					if(type=="2")
					{
						var data="{'mediaid':'"+result.resultData.result+"'}";
						createParam(data,'downloadSecureImg');
						$.JsonSRpc(interfaceUrl, param, function(resul){
							if(resul)
							{
								if(resul.resultCode=='0000')
								{
									//alert($(btn).prev().children("img").attr("src"));
									//alert(result.resultData[0].result);
									$(btn).prev().children("img").attr("src",resul.resultData[0].result);
									order["inimg"]=order["inimg"].replace(oldimgid,result.resultData.result)
									$.hideLoadMsg();

								}else{
									$.hideLoadMsg();
									message("获取信息失败！");
								
									
								}
							}else{
								$.hideLoadMsg();
								message("获取信息失败！");
								
								
							}
						});	
					}
					else
					{
						//str1+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+localId+"' data-preview-src=''  data-preview-group='4'/></span>";
						if(order["status"]=="21" ||order["status"]=="31"){
							str1+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+localId+'" data-preview-src="" data-preview-group="1" /></span><span class="reLoadImg" onclick=addimg("2","'+result.resultData.result+'")>重新上传</span></label>';
						}
						else{
							str1+='<label class="carContainer"><span  class="reLoadImg1"><img src="'+localId+'" data-preview-src="" data-preview-group="2" /></span></label>';
						}
						$("#inimgs").prepend(str1);	
						//$(".addFiles").before(str1);
						//var uploadimgs=$("#files").val()+","+result.resultData.result;
						//$("#files").val(uploadimgs);
						$.hideLoadMsg();
					}
				}
					//alert(1);
				else if(result.resultCode=='0001'){
					$.hideLoadMsg();
					message("上传失败！");
				}
				else{
					$.hideLoadMsg();
				}
			});
			
			
		} 
	}); 
};


//$(".IMfiles").find(".reLoadImg").each(function(){
//	var $this=$(this);
//	$this.click(function(){
//		var a=$this.siblings(".reLoadImg1").find("img");
//		ChoImg(a);		
//	});	
//})
var browser={
    versions:function(){
        var u = navigator.userAgent, 
            app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            weixin: u.indexOf('MicroMessenger') > -1, //是否微信 （2015-01-22新增）
            qq: u.match(/\sQQ/i) == " qq" //是否QQ
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase()
}

function test(btn)
{
	$(btn).prev().children("img");
}
//备用
//照片部分
//str+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+URL+"'data-preview-src=''data-preview-group='1' /></span>	<span class='reLoadImg'>重新上传</span></label>"
//理赔授权书照片部分
//str+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+URL+"'data-preview-src=''data-preview-group='3' /></span></label>"

//证明文件照片部分
//str+='<form ><div class="OrderTips"><label class="OrderTipsTi">受理提示信息:</label><label class="OrderTipsCon">'+ 提示信息+'</label></div><div class="input-row"> <label>姓名：</label> <span class="Pname">'+ 姓名+' </span></div><div class="input-row"><label class="navigate-right">合同号：</label><span>';
//str+=合同号+'</span></div><div class="input-row"><label>车辆：</label><span class="Pcar">'+车辆+'</span></div></form>';		   				   								
//str+="<label class='carContainer'><span  class='reLoadImg1'><img src='"+URL+"'data-preview-src=''data-preview-group='2' /></span></label>"