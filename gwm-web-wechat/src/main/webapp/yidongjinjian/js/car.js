$(function(){
var car= $CodeSource['00001I09ZSOEA0000A0P'];
var zw="";
var id=localStorage.JXSID;
$.showLoadMsg("加载中，请稍后...");

	var date=convertDate(new Date());
	param.timestamp=date;
	var data="{'shopid':'"+id+"'}";
	param.data=data;
	
	var s="";
	data=key+data+date+key;
	for(var i=0;i<data.length;i++)
	{
		s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
	}
	var signcode=$.md5(s);
	param.code='InternalChannel';
	param.signcode=signcode;
	$.JsonSRpc(interfaceUrl, param, function(result){
		if(result)
		{
			if(result.resultCode=='0000')
			{
				$.each(result.resultData, function(i,elem) {
					//model+="<option value='"+elem.code +"'>"+elem.name+"</option>";	
					if(elem.name.trim()=="哈弗")
					{
						$.each(car, function(i,ic) {
							if(ic.NAME=="长城" || ic.NAME =="哈弗"){
								if(ic.CODE==localStorage.getItem("carName")){
									zw+="<option selected value='"+ic.CODE +"'>"+ic.NAME+"</option>";	
								}else{
									zw+="<option  value='"+ic.CODE +"'>"+ic.NAME+"</option>";	
								}
							}
						});
					}
					else if(elem.name.trim()=="WEY")
					{
						$.each(car, function(i,ic) {
							if(ic.NAME=="Wey" ){
								if(ic.CODE==localStorage.getItem("carName")){
									zw+="<option selected value='"+ic.CODE +"'>"+ic.NAME+"</option>";	
								}else{
									zw+="<option value='"+ic.CODE +"'>"+ic.NAME+"</option>";	
								}
							}
						});
					}
				});
				
				$(".carName").append(zw);
				if(localStorage.getItem("carName")!=null ||localStorage.getItem("carName")!=undefined)
				{
					getModel(localStorage.getItem("carName"));
				}
				
				$.hideLoadMsg();
				//$("#select_model").append(model);
				
				//alert(result.resultData.result);
				//countDown(objs);
			}else{
				alert("经销商网络渠道信息异常");
				$.hideLoadMsg();
			}
		}else{
			alert("经销商网络渠道信息异常！");
			$.hideLoadMsg();
		}
	});


//品牌关联车型	
});
var model="";
var style="";
function getModel(se){
	
	var makeid=$("#select_brand").val()||se;
	model="<option value='-1'>请选择</option>";	
	if(makeid=='-1')
	{
		$("#select_model").html(model);
		return;
	}
	$.showLoadMsg("加载中，请稍后...");
	var date=convertDate(new Date());
	param.timestamp=date;
	var data="{'qd':'"+makeid+"'}";
	param.data=data;
	
		var s="";
		data=key+data+date+key;
		for(var i=0;i<data.length;i++)
		{
			s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
		}
		var signcode=$.md5(s);
		param.code='GetModel';
		param.signcode=signcode;
		$.JsonSRpc(interfaceUrl, param, function(result){
			if(result)
			{
				if(result.resultCode=='0000')
				{
					$.each(result.resultData, function(i,elem) {
						if(elem.code==localStorage.getItem("carType")){

							model+="<option selected value='"+elem.code +"'>"+elem.name+"</option>";	
						}else
						{
							model+="<option value='"+elem.code +"'>"+elem.name+"</option>";	
						}
					});
					
					$("#select_model").html(model);
					
					if(localStorage.getItem("carType")!=null || localStorage.getItem("carType")!=undefined )
					{
						getStyle(localStorage.getItem("carType"));
					}
					$.hideLoadMsg();
					//alert(result.resultData.result);
					//countDown(objs);
				}else{
					alert("获取车型信息异常");
					$.hideLoadMsg();
				}
			}else{
				alert("获取车型信息异常！");
				$.hideLoadMsg();
			}
		});
}
//车型关联款式
function getStyle(se){
	
	var modelid=$("#select_model").val()||se;
	style="<option value='-1'>请选择</option>";	
	if(modelid=='-1')
	{
		$("#select_model").html(style);
		return;
	}
	$.showLoadMsg("加载中，请稍后...");
	var date=convertDate(new Date());
	param.timestamp=date;
	var data="{'model':'"+modelid+"'}";
	param.data=data;
	
	var s="";
	data=key+data+date+key;
	for(var i=0;i<data.length;i++)
	{
		s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
	}
	var signcode=$.md5(s);
	param.code='GetType';
	param.signcode=signcode;
	
	$.JsonSRpc(interfaceUrl, param, function(result){
			if(result)
			{
				if(result.resultCode=='0000')
				{
					if(result.resultData==null)
					{
						$.hideLoadMsg();
						return ;
					}
					$.each(result.resultData, function(i,elem) {
						if(elem.code==localStorage.getItem("carStyle")){

						style+="<option selected value='"+elem.code +"'>"+elem.name+"</option>";	
						}else {
							style+="<option value='"+elem.code +"'>"+elem.name+"</option>";	
						}
					});
					$("#select_style").html(style);
					$.hideLoadMsg();
					//alert(result.resultData.result);
					//countDown(objs);
				}else{
					alert("获取车款信息异常");
					$.hideLoadMsg();
				}
			}else{
				alert("获取车款信息异常！");
				$.hideLoadMsg();
			}
		});
}


//验证部分
function checks(){
	var carN=$(".carName option:selected").val();
	var carSty=$(".carSty option:selected").val();
	var carKs=$(".carKs option:selected").val();
	var str="请选择:";
	if(carN=="-1"){
		$(".carName").parent().find("span").css("color","red");
		str+="品牌,";		
	}
	else{
		$(".carName").parent().find("span").css("color","#000");
	}
	if(carSty=="-1"){
		$(".carSty").parent().find("span").css("color","red");
		str+="车型,";		
	}else{
		$(".carSty").parent().find("span").css("color","#000");
	}
	if(carKs=="-1"){
			$(".carKs").parent().find("span").css("color","red");
			str+="款式,";	
		}else{
			$(".carKs").parent().find("span").css("color","#000");
		}
	if(str != '请选择:') {
		message(str);
	  return false;
	}else{
		jsonString();
		$(".commen-bottom .next").attr("href","personalInfomation.html");	
	}	
}
$(".next").click(function(){
	
	checks();
	});

function jsonString(){
			var data=$("form").serializeArray(); //自动将form表单封装成json
						
                for (var i=0;i<data.length;i++) {
                	localStorage.setItem(data[i].name,data[i].value)
//              					 JSON.stringify(data[i].name)JSON.stringify(data[i].value)
                }
			var carNameValue=$("#select_brand").find("option:selected").text();
			var carTypeValue=$("#select_model").find("option:selected").text();
			var carStyleValue=$("#select_style").find("option:selected").text();
			localStorage.setItem("carNameValue",carNameValue);
			localStorage.setItem("carTypeValue",carTypeValue);
			localStorage.setItem("carStyleValue",carStyleValue);
                
                
               
}   