//行业类型


$(function(){
	init();
})

function init(){
	if(localStorage.workPlaceName!=null ||localStorage.workPlaceName!=undefined )
	{
		$("#workPlaceName").val(localStorage.workPlaceName);
	}
	if(localStorage.workScale!=null ||localStorage.workScale!=undefined )
	{
		$("#workScale").val(localStorage.workScale);
	}
	if(localStorage.monthSalary!=null ||localStorage.monthSalary!=undefined )
	{
		$("#monthSalary").val(localStorage.monthSalary);
	}
	if(localStorage.homeSalary!=null ||localStorage.homeSalary!=undefined )
	{
		$("#homeSalary").val(localStorage.homeSalary);
	}
	if(localStorage.OutstandingLoan!=null ||localStorage.OutstandingLoan!=undefined )
	{
		$("#OutstandingLoan").val(localStorage.OutstandingLoan);
	}
	if(localStorage.monthRepay!=null ||localStorage.monthRepay!=undefined )
	{
		$("#monthRepay").val(localStorage.monthRepay);
	}
	if(localStorage.homePost!=null ||localStorage.homePost!=undefined )
	{
		$("#homePost").val(localStorage.homePost);
	}
	if(localStorage.jobTypeValue!=null ||localStorage.jobTypeValue!=undefined )
	{
		$("#selJob").text(localStorage.jobTypeValue);
		$("#selJob").attr('JobcodeNum',localStorage.jobType);

	}
}
//function JobTp(){
//	if(localStorage.jobTypeValue!="请选择"){
//		$("#selJob").html(localStorage.jobTypeValue);
//	}else{
//		$("#selJob").html("请选择");
//	}
//}
var industry=$CodeSource['00001H8RUUJY70000C0U'];
var str="";
$.each(industry, function(i,elem) {	
	if(elem.PCODE==0){
	if(localStorage.industryName==elem.CODE){
		str+="<option selected value=" +elem.CODE+">"+elem.NAME+"</option>";	
	}else{
		str+="<option value=" +elem.CODE+">"+elem.NAME+"</option>";	
	}
		
	}	
});
$(".industry").append(str);
//雇佣类型
var hire=$CodeSource['00001H8RUUJY70000C0W'];
var str1="";
$.each(hire, function(i,elem) {
	if(localStorage.hireType==elem.CODE){
		str1+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}else{
		str1+="<option value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}		
});
$(".hire").append(str1);
//职位
var job=$CodeSource['00001HFWVAV5W0000A21'];
var zw="";
$.each(job, function(i,elem) {
	if(localStorage.jobLevel==elem.CODE){
		zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
	}else{
		zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
	}
	
});
$(".job").append(zw);
//数据验证部分

function formValidate() {
	var str="";
//单位名称验证
	 var workInfos= $(".workInfos").val();
		if(workInfos !=""){
			$(".workInfos").parent().find("label").css("color","#000");
		}else{
			str += "单位未填写;\n";
			$(".workInfos").parent().find("label").css("color","red");
		}

//单位规模
	var workNum=$(".workNum ").val();
	if(workNum=="0"){
			$(".workNum").parent().parent().find("label").css("color","red");
			str += "单位规模不能为0;\n";		
		}else{
			$(".workNum").parent().parent().find("label").css("color","#000");
		}

//行业类型判断
	var industry= $(".industry option:selected").val();
	if(industry=="请选择"){
			$(".industry").parent().find("label").css("color","red");
			str += "行业类型未填写;\n";		
		}else{
			$(".industry").parent().find("label").css("color","#000");
		}
//雇佣类型		
	var hire= $(".hire option:selected").val();
	if(hire=="请选择"){
			$(".hire").parent().find("label").css("color","red");
			str += "雇佣类型;\n";		
		}else{
			$(".hire").parent().find("label").css("color","#000");
		}
//职业类型		
	var jobs= $("#selJob").html();
	if(jobs=="请选择"){
			$("#selJob").parent().find("label").css("color","red");
			str += "职业类型未填写;\n";		
		}else{
			$("#selJob").parent().find("label").css("color","#000");
		}
//职位
var job= $(".job option:selected").val();
	if(job=="请选择"){
			$(".job").parent().find("label").css("color","red");
			str += "职卫未填写;\n";		
		}else{
			$(".job").parent().find("label").css("color","#000");
		}
//本人收入
	var mineMoney=$(".mineMoney ").val();
	if(mineMoney=="0"){
			$(".mineMoney").parent().parent().find("label").css("color","red");
			str += "本人收入不能为0;\n";		
		}else{
			$(".mineMoney").parent().parent().find("label").css("color","#000");
		}
//家庭收入
	// var homeMoney=$(".homeMoney ").val();
	// if(homeMoney=="0"){
			// $(".homeMoney").parent().parent().find("label").css("color","red");
			// str += "家庭收入不能为0;\n";		
		// }else{
			// $(".homeMoney").parent().parent().find("label").css("color","#000");
		// }
//家庭月支出
var monthMoney=$(".monthMoney ").val();
	if(monthMoney=="0"){
			$(".monthMoney").parent().parent().find("label").css("color","red");
			str += "家庭月支出不能为0;\n";		
		}else{
			$(".monthMoney").parent().parent().find("label").css("color","#000");
		}
// 如果没有错误则提交
	 if(str != '') {	 	
	  message(str);
	  return false;
	 } else {
	  $(".commen-bottom .next").attr("href","emergencyContact.html");	
	 }
	}
	 
$('.next').on('click', function() {
	jsonString();
	 formValidate(); 
});
//职业类型数据绑定
var nameEl = document.getElementById('selJob');
var first = []; /* 主类型 */
var second = []; /* 子类型 */
var selectedIndex = [0, 0, 0]; /* 默认选中的类型 */

var checked = [0, 0, 0]; /* 已选选项 */

function creatList(obj, list){
  obj.forEach(function(item, index, arr){
  var temp = new Object();
  temp.text = item.NAME;
  temp.value = item.CODE;
  list.push(temp);
  })
}
var arr1=new Array();
var code=$CodeSource['00001I19D00IV0000A1E'];
var tmpObj={};
for(i in code){
	if(code[i].PCODE==0){
		tmpObj[code[i].CODE]={NAME: code[i].NAME,sub:[],CODE:code[i].CODE};
	}
}

for(i in code){
  	if(code[i].PCODE>0){
  		var aa=code[i].NAME;
  		aa=aa.split("/");
  		aa.shift();
  		aa=aa.join("/")
		code[i].NAME=aa;		
		tmpObj[code[i].PCODE].sub.push( code[i]);
	}
  
}

for(var i in tmpObj){
	arr1.push(tmpObj[i]);
}
creatList(arr1, first);
if (arr1[selectedIndex[0]].hasOwnProperty('sub')) {
  creatList(arr1[selectedIndex[0]].sub, second);
} else {
  second = [{text: '', value: 0}];
}
arr1[selectedIndex[0]].sub
var picker = new Picker({
    data: [first, second],
  selectedIndex: selectedIndex,  
});
picker.on('picker.select', function (selectedVal, selectedIndex) {

  var text1 = first[selectedIndex[0]].text;
  var text2 = second[selectedIndex[1]].text;
  var text3=second[selectedIndex[1]].value;
   nameEl.innerHTML = text1 + '/' + text2 
   nameEl.setAttribute('JobcodeNum',text3 )
   
});
picker.on('picker.change', function (index, selectedIndex) {
  if (index === 0){
    firstChange();
  } 
  function firstChange() {
    second = [];
    checked[0] = selectedIndex;
    var firstCity = arr1[selectedIndex];
    if (firstCity.hasOwnProperty('sub')) {
      creatList(firstCity.sub, second);
    } else {
      second = [{text: '', value: 0}];
      checked[1] = 0;

    }
    picker.refillColumn(1, second);
    picker.scrollColumn(1, 0)

  }
});
nameEl.addEventListener('click', function () {
    picker.show();
});
function jsonString(){
		var data=$("form").serializeArray(); //自动将form表单封装成json						
                for (var i=0;i<data.length;i++) {
               	localStorage.setItem(data[i].name,data[i].value)
          } 
         var Wcity=$("#selJob").attr('JobcodeNum');
        localStorage.setItem("jobType",Wcity);
		localStorage.setItem("jobTypeValue",$("#selJob").text());
}    