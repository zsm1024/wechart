//var  interfaceUrl= "http://219.150.94.73:9092/api/MobAppInterface";
//var  interfaceUrl= "http://10.50.129.82:9092/api/MobAppInterface";
var  interfaceUrl= "http://219.148.138.170:8989/api/MobAppInterface";
var queryDealerByCity="http://bdorange.gwmfc.com:1833/S170010/dealer/querybycity?city=";
var key="AR2JVIK63U5";
var param={
	"timestamp":"",
	"data":"",
	"signcode":"",
	"mode":"md5",
	"uid":"0001",
	"code":""
	};
function convertDate(date){
	var year = date.getFullYear()+'';
	var month = date.getMonth()+1+'';//js从0开始取 
	var day = date.getDate()+''; 
	var hour = date.getHours()+''; 
	var minutes = date.getMinutes()+''; 
	var second = date.getSeconds()+'';
	return year+month+day+hour+minutes+second+'';
}
function createParam(data,method){
	var date=convertDate(new Date());
	param.timestamp=date;
	var data=data;
	param.data=data;
	
	var s="";
	data=key+data+date+key;
	for(var i=0;i<data.length;i++)
	{
		s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
	}
	var signcode=$.md5(s);

	param.code=method;
	param.signcode=signcode;
	
}