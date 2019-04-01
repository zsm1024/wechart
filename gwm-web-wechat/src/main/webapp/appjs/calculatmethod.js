//取得贷款利率
var oneyear = 11.60;
var onetotwoyear = 11.85;
var twotothreeyear = 12.10;
var threetofiveyear = 12.85;
//贷款计算器
function debj(param){
	  var obj = {};
	  var firstSize=param.firstSize;//首付比例
	  var total=param.price==""?0:param.price;//车价
	  var count=param.count;//还款期数
	  var lastsize=param.lastsize;//尾款比例
	  var last;
	  var result;
	  if(firstSize==null || firstSize==""){
		  firstSize=20.00;
	  }
	  if(count==null || count==""){
		  count=12;
	  }
	  if(lastsize==null ||lastsize==""){
		  lastsize=20;
	  }
	  last=total*lastsize/100.00;//尾款金额
	  var firstMon=total*firstSize/100.00;//首付金额
	  var borrow=total-firstMon;//贷款金额
	  var rate;
	  if(count==12){
		rate=oneyear;
	  }else if(count>12 && count <=24){
		rate=onetotwoyear;
	  }else if(count >24 && count <=36){
		rate=twotothreeyear;
	  }else if(count >36 && count <=60){
		rate=threetofiveyear;
	  }
	  var r=(rate/100)/12;//月利率
	  var tem=0;
	  var type = param.type;
	  if(type=="2"){//等额本金
		  result= (borrow/count)+(borrow*(1-(1-1)/count)*r);// 月供 = （贷款金额/还款期数） + 贷款金额*月利率 = 每期还款本金 + 利息
		  result=result.toFixed(2);
		  if(borrow.toString().split(".").length!=1){
			  borrow=borrow.toFixed(2);
		  }
	  }else if(type=="3"){//等额本息 
		  tem=r*Math.pow((1+r),count);
		  tem/=Math.pow((1+r),count)-1;
		  result=borrow*tem;
		  result=result.toFixed(2);
		  if(borrow.toString().split(".").length!=1){
			  borrow=borrow.toFixed(2);
		  }
	  }else{//随薪贷
		  for(var i=1;i<count;i++){
			  tem=tem+1/(Math.pow((1+r),i))
		  }
		  result=(borrow-last*(1/(Math.pow((1+r), count))))/tem;
		  result=result.toFixed(2);
		  if(borrow.toString().split(".").length!=1){
			  borrow=borrow.toFixed(2);
		  }
		  obj.weikuan = Math.ceil(parseFloat(last));//format(last,2);
	  }
	  //var firstMons=firstMon.toFixed(2);
	  obj.shoufu = Math.ceil(parseFloat(firstMon));//format(firstMon,2);
	  obj.daikuan = Math.ceil(parseFloat(borrow));//format(borrow,2);
	  if(param.type=='2'){
		  obj.yuegong = Math.ceil(parseFloat(result));//format(parseFloat(result),0);
	  }else{
		  obj.yuegong = Math.ceil(parseFloat(result));//format(parseFloat(result),0);
	  }
	  return obj;
}