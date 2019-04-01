
//var proS= $CodeSource['00001HLABTHDE0000A1H'];
//var zw="";
//$.each(proS, function(i,elem) {
//	if(elem.PCODE==0){
//		zw+="<option>"+elem.NAME+"</option>";
//	}		
//});
//$(".proS").append(zw);
////省市数据绑定

$(function(){
	init();
})

function init(){
	if(localStorage.homeAddress!=null ||localStorage.homeAddress!=undefined )
	{
		$("#homeAddress").val(localStorage.homeAddress);
	}
	if(localStorage.homePostCode!=null ||localStorage.homePostCode!=undefined )
	{
		$("#homePostCode").val(localStorage.homePostCode);
	}
	if(localStorage.homeTime!=null ||localStorage.homeTime!=undefined )
	{
		$("#result").val(localStorage.homeTime);
	}
	if(localStorage.workAddress!=null ||localStorage.workAddress!=undefined )
	{
		$("#workAddress").val(localStorage.workAddress);
	}
	if(localStorage.workPostCode!=null ||localStorage.workPostCode!=undefined )
	{
		$("#workPostCode").val(localStorage.workPostCode);
	}
	if(localStorage.workTime!=null ||localStorage.workTime!=undefined )
	{
		$("#results").val(localStorage.workTime);
	}
	if(localStorage.homeCityValue!=null ||localStorage.homeCityValue!=undefined )
	{
		$("#selJob").val(localStorage.homeCityValue);
		$("#selJob").attr('HomecodeNum',localStorage.homeCity)
	}
	if(localStorage.workCityValue!=null ||localStorage.workCityValue!=undefined )
	{
		$("#selJob1").val(localStorage.workCityValue);
		$("#selJob1").attr('WorkcodeNum',localStorage.workCity)

	}
	if(localStorage.postAddress!=null ||localStorage.postAddress!=undefined )
	{
		if(localStorage.postAddress=="1")
		{
			$("#postAddress1").attr('checked',true);
		}
		else{
			$("#postAddress2").attr('checked',true);
		}
	}
}

var nameEl =document.getElementById('selJob');
var nameEl1 =document.getElementById('selJob1');
var first = []; /* 主类型 */
var first1 = []; 
var second = []; /* 子类型 */
var second1 = [];
var selectedIndex = [0, 0, 0]; /* 默认选中的类型 */
var selectedIndex1 = [0, 0, 0];
var checked = [0, 0, 0]; /* 已选选项 */
var checked1 = [0, 0, 0];

function creatList(obj, list,seq){
  obj.forEach(function(item, index, arr){
	  var temp = new Object();
	  temp.text = item.NAME;
	  temp.value = item.CODE;
	  if(seq=="1")
	  {
		  
	  }
	  else
	  {
		  
	  }
	  //if(item.CODE==localStorage.)
	  list.push(temp);
  })
}
var arr1=new Array();
var arr2=new Array();
var code=$CodeSource['00001HLABTHDE0000A1H'];
var tmpObj={};
for(i in code){

	if(code[i].PCODE==0){

		tmpObj[code[i].CODE]={NAME: code[i].NAME,sub:[],CODE:code[i].CODE};
		 tmpObj[code[i].CODE].sub	

		;
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
	arr2.push(tmpObj[i]);
}
creatList(arr1, first,1);
creatList(arr2, first1,2);
if (arr1[selectedIndex[0]].hasOwnProperty('sub')) {
  creatList(arr1[selectedIndex[0]].sub, second);
} else {
  second = [{text: '', value: 0}];
}
if (arr2[selectedIndex[0]].hasOwnProperty('sub')) {
  creatList(arr2[selectedIndex[0]].sub, second1);
} else {
  second1 = [{text: '', value: 0}];
}
arr1[selectedIndex[0]].sub;
arr2[selectedIndex[0]].sub;
//arr1[selectedIndex1[0]].sub;
//家庭地址部分
var picker = new Picker({
    data: [first, second],
  selectedIndex: selectedIndex,  
});
//办公地址部分
var picker1 = new Picker({
    data: [first1, second1],
  selectedIndex1: selectedIndex1,  
});
picker.on('picker.select', function (selectedVal, selectedIndex) {
var text3 = first[selectedIndex[0]].text;
var text4 = second[selectedIndex[1]].text;
var text5 = second[selectedIndex[1]].value;
var text6 = first[selectedIndex[0]].value;
	if (text5.indexOf(text6) != 0) {
		message("省市信息不匹配;");
		return;
	}
   nameEl.value=text3 + '/' + text4;
   nameEl.setAttribute('HomecodeNum',text5 )
});
picker1.on('picker.select', function (selectedVal, selectedIndex) {
var text1 = first1[selectedIndex[0]].text;
var text2 = second1[selectedIndex[1]].text;
var text6 =second1[selectedIndex[1]].value;
var text7 = first1[selectedIndex[0]].value;
if (text6.indexOf(text7) != 0) {
		message("省市信息不匹配;");
		return;
}
   nameEl1.value = text1 + '/' + text2;
  nameEl1.setAttribute('WorkcodeNum',text6)
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
picker1.on('picker.change', function (index, selectedIndex) {
	
  if (index === 0){
    firstChange1();
  } 
  function firstChange1() {
    second1 = [];
    checked[0] = selectedIndex;
    var firstCity1 = arr2[selectedIndex];
    if (firstCity1.hasOwnProperty('sub')) {
      creatList(firstCity1.sub, second1);
    } else {
      second1 = [{text: '', value: 0}];
      checked[1] = 0;
    }
    picker1.refillColumn(1, second1);
    picker1.scrollColumn(1, 0)

  }
});
nameEl.addEventListener('click', function (){
    picker.show();
});
nameEl1.addEventListener('click', function (){
    picker1.show();
});

//房产类型
var homeS= $CodeSource['00001H8RUUJY00000C0K'];
var zw="";
$.each(homeS, function(i,elem) {
	if(localStorage.homeSelect==elem.CODE ){
		zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}else{
		zw+="<option value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}			
});
$("#select_k1").append(zw);
var zw1="";
$.each(homeS, function(i,elem) {
	if(localStorage.workSelect==elem.CODE ){
		zw1+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}else{
		zw1+="<option value='"+elem.CODE +"'>"+elem.NAME+"</option>";
	}			
});
$("#select_k2").append(zw1);
//表单验证部分

//邮政编码

function is_postcode(postcode) {
	 var pattern = /^[0-9][0-9]{5}$/;
	 return pattern.test(postcode);
}
//验证函数
function formValidates() {
 var str="";
 //家庭地址部分
 // 省市验证
  var str1="<div><p class='hom1'>家庭地址部分</p><div class='p1'>";
 
 	var proS= $(".homeAd .proS ").val();
 	var Hcitys=$("#selJob").attr('HomecodeNum');
	if(proS==""&&Hcitys==undefined){
			$(" .homeAd .proS").parent().find("label").css("color","red");
			str1 += "请确认您的省市信息是否正确;\n";		
		}else{
			$(".homeAd .proS").parent().find("label").css("color","#000");
		} 
 // 地址验证
  	var addHo= $(".homeAd .addHo").val();
	if(addHo !=""){
			$(".homeAd .addHo").parent().find("label").css("color","#000");
		}else{
			str1 += "地址未填写;\n";
			$(".homeAd .addHo").parent().find("label").css("color","red");
		}
//邮箱验证
 if($.trim($('.homeAd .numHo').val()).length == 0) { 
	  str1 += '邮政编码未输入;\n';
	  $(".homeAd .numHo").parent().find("label").css("color","red");
	 } else {
	  if(is_postcode($.trim($('.homeAd .numHo').val())) == false) {
	   str1 += '请输入正确的邮政编码;\n';	  
	   $(".homeAd .numHo").parent().find("label").css("color","red");
	  }
	  $(".homeAd .numHo").parent().find("label").css("color","#000");
	 }
//房产类型
	var homeS= $(".homeAd .homeS option:selected").val();
	if(homeS=="请选择"){
			$(" .homeAd .homeS").parent().find("label").css("color","red");
			str1 += "房产类型未填写;\n";		
		}else{
			$(".homeAd .homeS").parent().find("label").css("color","#000");
		} 
	// 时间类型
	var results= $("#result").val();
		if(results !=""){
			$("#result").parent().find("label").css("color","#000");
		}else{
			str1 += "起始时间未填写;\n";
			$("#result").parent().find("label").css("color","red");
		}
//办公地址部分
 // 省市验证
  var str2="<div><p class='work1'>办公地址部分</p><div class='p2'>"; 
 	var proS= $(".workAd .proS ").val();
 	var Wcitys=$("#selJob1").attr('WorkcodeNum');
	if(proS==""&&Wcitys==undefined){
			$(" .workAd .proS").parent().find("label").css("color","red");
			str2 += "请确认您的省市信息是否正确;\n";		
		}else{
			$(".workAd .proS").parent().find("label").css("color","#000");
		} 
 // 地址验证
  	var addHo= $(".workAd .addHo").val();
	if(addHo !=""){
			$(".workAd .addHo").parent().find("label").css("color","#000");
		}else{
			str2 += "地址未填写;\n";
			$(".workAd .addHo").parent().find("label").css("color","red");
		}
//邮箱验证
 if($.trim($('.workAd .numHo').val()).length == 0) { 
	  str2 += '邮政编码未输入;\n';
	  $(".workAd .numHo").parent().find("label").css("color","red");
	 } else {
	  if(is_postcode($.trim($('.workAd .numHo').val())) == false) {
	   str2 += '请输入正确的邮政编码;\n';	  
	   $(".workAd.numHo").parent().find("label").css("color","red");
	  }
	  $(".workAd .numHo").parent().find("label").css("color","#000");
	 }
//房产类型
	var homeS= $(".workAd .homeS option:selected").val();
	if(homeS=="请选择"){
			$(".workAd .homeS").parent().find("label").css("color","red");
			str2 += "房产类型未填写;\n";		
		}else{
			$(".workAd .homeS").parent().find("label").css("color","#000");
		} 
	// 时间类型
	var results= $("#results").val();
		if(results !=""){
			$("#results").parent().find("label").css("color","#000");
		}else{
			str2 += "起始时间未填写;\n";
			$("#results").parent().find("label").css("color","red");
		}
		str1+="</div></div>";
		str2+="</div></div>";
		// 如果没有错误则提交
		str+=str1+str2;
		var strm="<div><p class='hom1'>家庭地址部分</p><div class='p1'></div></div><div><p class='work1'>办公地址部分</p><div class='p2'></div></div>";
		var strm1="<div><p class='hom1'>家庭地址部分</p></div>";
		var strm2="<div><p class='work1'>办公地址部分</p></div>";				
	 if(str != strm){ 	
	  message(str);	  
	  return false;
	 } else {
	  $(".commen-bottom .next").attr("href","workInfo.html");	
	 }
}
$('.next').on('click', function() {
	jsonString();
	 formValidates(); 
});

	function  workAD(){			
		(function($) {
				$.init();
				var results = $('#results')[0];
				var btns = $('.btn1');
				btns.each(function(i, btn) {
					btn.addEventListener('tap', function() {
						var _self = this;
						if(_self.picker) {
							_self.picker.show(function (rs) {
								results.value =rs.text;

								_self.picker = null;
							});
						} else {
							var optionsJson = this.getAttribute('data-options') || '{}';
							var options = JSON.parse(optionsJson);
							var id = this.getAttribute('id');
							//_self.picker = new $.DtPicker(options);
							_self.picker = new $.DtPicker({
								type:"date",//设置日历模式
								beginDate:new Date(1940,01,01),
								endDate:new Date()
							});	
							_self.picker.endDate=new data();
							_self.picker.show(function(rs) {

								results.value =rs.text;

								_self.picker.dispose();
								_self.picker = null;
							});
						}
						
					}, false);
				});
			})(mui);
}
workAD();

workAD1();
	function  workAD1(){			
		(function($) {
				$.init();
				var results = $('#result')[0];
				var btns = $('.btn');
				btns.each(function(i, btn) {
					btn.addEventListener('tap', function() {
						var _self = this;
						if(_self.picker) {
							_self.picker.show(function (rs) {
								results.value =rs.text;

								_self.picker = null;
							});
						} else {
							var optionsJson = this.getAttribute('data-options') || '{}';
							var options = JSON.parse(optionsJson);
							var id = this.getAttribute('id');
//							_self.picker = new $.DtPicker(options);
							_self.picker = new $.DtPicker({
								type:"date",//设置日历模式
								beginDate:new Date(1940,01,01),
								endDate:new Date()
							});	
							
							
//							_self.picker.setSelectedValue("1980-01-01");
							_self.picker.endDate=new data();
							_self.picker.show(function(rs) {

								results.value =rs.text;

								_self.picker.dispose();
								_self.picker = null;
							});
						}
						
					}, false);
				});
			})(mui);
}
//将页面数据转成json格式
function jsonString(){		
       var data=$("form").serializeArray(); //自动将form表单封装成json						
       for (var i=0;i<data.length;i++) {
       localStorage.setItem(data[i].name,data[i].value); 
        }
        var Hcity=$("#selJob").attr('HomecodeNum');
      	var Wcity=$("#selJob1").attr('WorkcodeNum');
        localStorage.setItem("homeCity",Hcity);
        localStorage.setItem("workCity",Wcity);
		localStorage.setItem("homeCityValue",$("#selJob").val());
        localStorage.setItem("workCityValue",$("#selJob1").val());
		localStorage.setItem("postAddress",$("input:radio:checked").val());
} 

$("#result").focus(function(){	
	$(this).blur();
});
$("#results").focus(function(){	
	$(this).blur();
});

