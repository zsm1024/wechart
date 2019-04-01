//var openid='on4t5juAuErQ0-6IH_whIsTEqCL8';
var openid="";
var dataMap=new Map();
$(function(){
	$.showLoadMsg("加载中，请稍后...");
	$.JsonSRpc(baseUrl+ "/snsapibase",{code: getParam("code")},function(data) {
		//
		if(data.errcode!=null && data.errcode!=null &&"0"==data.errcode){
			openid = data.openid;
			if(openid==undefined||""==openid){
				message("信息获取失败，请重新进入页面", function(flag){
					$.hideLoadMsg();
					wx.closeWindow();
					
				});
				return;
			}else{
				localStorage.setItem("openid", openid);
				 
				var date=convertDate(new Date());
				param.timestamp=date;
				var data="{'openid':'"+openid+"'}";
				param.data=data;
				
				var s="";
				data=key+data+date+key;
				for(var i=0;i<data.length;i++)
				{
					s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
				}
				var signcode=$.md5(s);

				param.code='getOrderByOpenid';
				param.signcode=signcode;
				
				$.JsonSRpc(interfaceUrl, param, function(result){
					if(result)
					{
						if(result.resultCode=='0000')
						{
							var html="";
							$.each(result.resultData,function(i,item){
								 html+="<div class='orderList'><label class='orderTime'><span>申请时间 ：</span><span>"+item.applicationDate+"</span><span><a id="+item.id+" onClick='updateItem("+item.id+")'><img src='../img/editor.png'/></a></span></label><p class='orderName'><span>申请人：</span><span>"+item.UserName+"</span></p><p class='orderCar'><span>贷款车辆：</span><span>"+item.carNameValue+" "+item.carTypeValue+item.carStyleValue+"</span></p><p class='orderJXS'><span>经销商：</span><span>"+item.JXSName+"</span></p></div>";
								 
								 dataMap.set("acs"+item.id,item);
							})
							$("#orderLists").html(html);
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
			$.hideLoadMsg();
			message("信息获取失败，请重新进入页面", function(flag){
				wx.closeWindow();
			});
			return;
		}
	});
	
	
	
});

function updateItem(id)
{
	localStorage.clear();
	var order=dataMap.get('acs'+id);
	for(var key in order)
	{
		localStorage.setItem(key,order[key]);
	}
	window.location.href="scan.html";
}



