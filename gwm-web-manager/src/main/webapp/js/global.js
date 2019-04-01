var pathname = window.location.pathname;
var basePath = window.location.protocol + "//" + window.location.host + pathname.substring(0,pathname.substring(1).indexOf("/")+1);
var baseUrl = basePath;

$.JsonSRpc=function(url,data,succFunc,error,async){
	data=data||{};
	succFunc=succFunc||function(){};
	error=error||function(){};
	$.ajax({
        url: url,
        type:"POST",
        datatype: "JSON",
        timeout : 180000,
        data: data,
        async: async != false,
        success: function(obj){
    		if(typeof(obj) == "object"){
    			if(obj.successful!==false){
    				succFunc(obj);
    			}else{
    				message(obj.resultHint);
    			}
    		}else{
    			var response = $.parseJSON(obj);
    			succFunc(response);
    		}
        },
        complete:error
    });
}

function getParam(id){
    var href=window.location.href+"";
    var index=href.indexOf("?")
    if(index<0)
        return null;
    href=href.substring(index+1);
    var params=href.split("&");
    var obj={};
    for(var i=0;i<params.length;i++){
        var param=params[i];
        index=param.indexOf("=");
        if(index<0)
            continue;
        obj[param.substring(0,index)]=param.substring(index+1);
    }
    return obj[id];
 }