
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
	if(localStorage.city != null || localStorage.homeAddress!=undefined){
		var nameEl =document.getElementById('selJob');
   		nameEl.value=localStorage.cityName;
		list(localStorage.city)
	}
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
//选则城市和地区
var picker = new Picker({
    data: [first, second],
  selectedIndex: selectedIndex,  
});
//					输出已选城市和地区（text3,text4）

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
	 console.log(text5)
	 localStorage.city = text5
	 localStorage.cityName= text3 + '/' + text4
	 list(text5)
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

nameEl.addEventListener('click', function (){
    picker.show();
});


$("#result").focus(function(){	
	$(this).blur();
});
$("#results").focus(function(){	
	$(this).blur();
});

//渲染数据
function list(city){
	$.showLoadMsg("加载中，请稍后...");
	var data;
	$.ajax({
			type: 'GET',
			url:queryDealerByCity+city,
			dataType: 'json',
			success: function( rest ) {
				console.log(rest)
				data=[]
				data=rest.data
				if(rest.data.length==0){
					$('.zwxx').show()
				}else{
					$('.zwxx').hide()
				}
			},complete:function(){
				var html = '';
				for (var i in data) { /*href=?name='+data[i].name+'&id+'+data[i].id+'*/
					html += '<li class="list">' + '<a >' + '<span class="name">' + data[i].name + '</span>' + '&nbsp;' +
						'<small style="display:none">' + data[i].id + '</small>' + '</a>' + '</li>';
				}
				var oContent = $('.mui-table-view');
				oContent.html(html);
				$.hideLoadMsg();				
			}
	});


}
$('.mui-table-view').on('click','.list',function () {
	$('.foterBoten').show()
		var $this = $(this)
	$this.parent().children().removeClass('active');
	$this.addClass('active')
	var result=$this.children().children("small").text(),
		a=$this.children().children("span").text();
	localStorage.setItem("JXSID",result);
	localStorage.setItem("JXSName",a);
	console.log(localStorage.setItem)
})

