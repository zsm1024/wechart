var dlModel = new Array('127','280','281','290');
var dlCar = new Array('117','119','120','121','123','136','147');
var _CIACarModelData;
var initflag = false;
var _jmpFlg = false;

var jxs_pro = [];
var jxs_cit = [];
var listData = [];

var openid;
var obj = {};

$(function(){
	openid = sessionStorage.openid;
	if(undefined == openid || "" == openid){
		var code = getParam("code");
		if(code){
			$.showLoadMsg("加载中，请稍后...");
			$.JsonSRpc(baseUrl+"/snsapibase", {code:code}, function(data){
				$.hideLoadMsg();
				if(data.errcode&&"0"==data.errcode){
					openid = data.openid;
					if(openid==undefined||""==openid){
						message("信息获取失败!", function(flag){
							wx.closeWindow();
						});
					}else{
						sessionStorage.setItem("openid", openid);
						htmlInit();
					}
				}else{
					message("获取用户信息失败！", function(flag){
						wx.closeWindow();
					});
				}
			});
		}else{
			message("获取信息失败，请重新进入页面", function(flag){
				wx.closeWindow();
			})
			//htmlInit();
		}
	}else{
		htmlInit();
	}
});

function htmlInit(){
	//品牌改变
	$("#brand").change(function(){
		var n = $("#brand").val();
		selectbrand(n);
		hideButton();
		$("#priceInput").val("");
	});
	//车型改变
	$("#models").change(function(){
		var n = $("#models").val();
		selectmodel(n);
		hideButton();
		sessionStorage.removeItem("car_style");
		$("#priceInput").val("");
	});
	//金融产品改变
	$("#loan_type").change(function(){
		var n = $("#loan_type").val();
		check_loan(n);
	});
	//车价获得焦点
	$("#priceInput").focus(function(){
        var price=document.getElementById("priceInput");
        var myprice=removeThousand(price.value);//去掉金额中的千分符
        $("#priceInput").val(myprice);
    });
	//经销商选择
	$("#shops").change(function(){
		showButton();
	});
	//点击开始计算
	$("#startCalcute").click(function(){
		cacluteLoan();
	});
	
	//在线申请
	$("#apply").click(function(){
		apply();
	});
	//提交
	$("#submit").click(function(){
		submit();
	});
	//获取验证码
	$("#chk_btn").click(function(){
		getCheckCode();
	});
	//加载初始化数据
    var brand = sessionStorage.brand;
	var models = sessionStorage.models;
	var priceInput = sessionStorage.priceInput;
	var province = sessionStorage.province;
	var city = sessionStorage.city;
	sessionStorage.brand = undefined;
	sessionStorage.models = undefined;
	sessionStorage.priceInput = undefined;

	if(false == isNull(province)&&false==isNull(city)){
		initflag = true;
	}
	if(false == isNull(brand)&&false == isNull(models)){
		if(brand == "长城"){
			brand = "1";
		}else if(brand == "哈弗"){
			brand = "2";
		}else if(brand == "wey"){
			brand = "3";
		}
		$("#brand").val(brand);
		selectbrand(brand);
		for(var i = 0; i < _CIACarModelData.length; i++){
			if(_CIACarModelData[i].Name == models){
				models = _CIACarModelData[i].CategoryID;
				break;
			}
		}
		$("#models").val(models);
		selectmodel(models);
	}
	if(false == isNull(priceInput)){
		$("#priceInput").val(priceInput);
		myPrice();
	}
	var jumpflag = sessionStorage._jumpshop;
	if("1" == jumpflag){
		_jmpFlg = true;
		sessionStorage._jumpshop = "0";
		var _brand = sessionStorage._brand;
		var _models = sessionStorage._models;
		var _priceInput = sessionStorage._priceInput;
		var _jxs_province = sessionStorage._jxs_province;
		var _jxs_city = sessionStorage._jxs_city;
		var _shops = sessionStorage._shops;
		var _applyName = sessionStorage._applyName;
		var _sex = sessionStorage._sex;
		var _idno = sessionStorage._idno;
		var _myPhone = sessionStorage._myPhone;
		var _check_code = sessionStorage._check_code;
		if(_brand == "长城"){
			_brand = "1";
		}else if(_brand == "哈弗"){
			_brand = "2";
		}else if(_brand == "wey"){
			_brand = "3";
		}
		$("#brand").val(_brand);
		selectbrand(_brand);
		for(var i = 0; i < _CIACarModelData.length; i++){
			if(_CIACarModelData[i].Name == _models){
				_models = _CIACarModelData[i].CategoryID;
				break;
			}
		}
		sessionStorage.province = _jxs_province;
		sessionStorage.city = _jxs_city;
		initflag = true;
		$("#models").val(_models);
		selectmodel(_models);
		if("1" == _sex){
			$("#sex-m").attr("checked", "checked");
		}else if("2" == _sex){
			$("#sex-f").attr("checked", "checked");
		}
		$("#priceInput").val(_priceInput);
		$("#applyName").val(_applyName);
		$("#idno").val(_idno);
		$("#myPhone").val(_myPhone);
		$("#_check_code").val(check_code);
	}
}

function hideButton(){
	$("#submit").css("background","gray");
	$("#submit").attr("disabled", true);  
}
function showButton(){
	$("#submit").css("background","#c8161d");
	$("#submit").removeAttr("disabled"); 
}

function shopClick(){
	//跳转网点查询，保存现场数据
	var brand = $("#brand").val();
	var models = $("#models").val();
	var priceInput = $("#priceInput").val();
	var jxs_province = $("#jxs_province").val();
	var jxs_city = $("#jxs_city").val();
	var shops = $("#shops").val();
	var applyName = $("#applyName").val();
	var sex = $("input[name='sex']:checked").val();
	var idno = $("#idno").val();
	var myPhone = $("#myPhone").val();
	var check_code = $("#check_code").val();
	sessionStorage._jumpshop = "1";
	sessionStorage._brand = brand;
	sessionStorage._models = models;
	sessionStorage._priceInput = priceInput;
	sessionStorage._jxs_province = jxs_province;
	sessionStorage._jxs_city = jxs_city;
	sessionStorage._shops = shops;
	sessionStorage._applyName = applyName;
	sessionStorage._sex = sex;
	sessionStorage._idno = idno;
	sessionStorage._myPhone = myPhone;
	sessionStorage._check_code = check_code;
	window.location.href = "./shop.html";
}

/**
 *初始化省级可选项
 */
function initProvince() {
	var provinces = getProvinces();
	var option = "<option value='-1'>请选择省份</value>";
	for (var i = 0; i < provinces.length; i++) {
		if($.inArray(provinces[i].ProvinceName, jxs_pro)==-1){
			continue;
		}
		option += '<option value="' + provinces[i].ProvinceName + '">'
				+ provinces[i].ProvinceName + '</option>';
	}
	$("#jxs_province").html(option);
	$("#jxs_province").change(function() {
		var text = $("#jxs_province").find("option:selected").text();
		initcity(text);
	});
	$("#jxs_city").change(function() {
		var text = $("#jxs_city").find("option:selected").text();
		initJXS(text);
	});
	if(initflag){
		initflag = false;
		var province = sessionStorage.province;
		var city = sessionStorage.city;
		sessionStorage.province = undefined;
		sessionStorage.city = undefined;
		$("#jxs_province").val(province);
		initcity(province);
		$("#jxs_city").val(city);
		initJXS(city);
		$("#shops").val(sessionStorage._shops);
	}else{
		var text = $("#jxs_province").find("option:selected").text();
		initcity(text);
	}
}

//省市二级联动
function initcity(cit) {
	$("#jxs_city").html("");
	var cityArray = getCities(cit);
	var cityOption = "<option value='-1'>请选择城市</option>";
	if (cityArray != undefined) {
		for (var i = 0; i < cityArray.length; i++) {
			if($.inArray(cityArray[i].CityName, jxs_cit)==-1){
				continue;
			}
			cityOption += '<option value="' + cityArray[i].CityName + '">'
					+ cityArray[i].CityName + '</option>';
		}
	}
	$("#jxs_city").html(cityOption);
	var text = $("#jxs_city").find("option:selected").text();
	initJXS(text);
}

function initJXS(text){
	showButton();
	var html = '<option value="-1">请选择经销商</option>';
    for (var i = 0; i < listData.length; i++) {
        if (listData[i].sh_city == text){
            html += '<option value="'+listData[i].sh_name+'">'+listData[i].sh_name+'</option>';
        }
    }
    $("#shops").html(html);
}

//加载经销商信息
function getJxsList(text){
	$.showLoadMsg("数据加载中，请稍候...");
	$.JsonSRpc(baseUrl+"/wxqueryshopbycar", {model:text}, function(data){
		$.hideLoadMsg();
		jxs_pro.length = 0;
		jxs_cit.length = 0;
		listData.length = 0;
		if(data.errcode&&"0"==data.errcode){
			var shops = JSON.parse(data.shops);
//			var items = '<option value="-1">请选择经销商</value>';
//			var n = 0;
//			listData.length = 0;
//			listData = shops;
			listData = shops;
			for (var i = 0; i < shops.length; i++) {
				jxs_pro.push(shops[i].sh_province);
				jxs_cit.push(shops[i].sh_city);
			}
//			$("#shops").html(items);
			initProvince();
		}
	});
}

//加载车型数据源
function selectbrand(brand){
    if(brand=="1"){
    	_CIACarModelData = _ccModelData;
    }else if(brand=="2"){
    	_CIACarModelData = _hfModelData;
    }
    else if(brand=="3"){
    	/*message("WEY品牌暂无车型上市，敬请期待！");
        _CIACarModelData=[];*/
    	_CIACarModelData = _weyModelData;
    }else{
    	_CIACarModelData=[];
    }
    var html = '<option value="-1">请选择车型</option>';
    for (var i = 0; i < _CIACarModelData.length; i++) {
        if (_CIACarModelData[i].PID == 0 && jQuery.inArray(_CIACarModelData[i].CategoryID,dlModel) < 0){
            html += '<option value="'+_CIACarModelData[i].CategoryID+'">'+_CIACarModelData[i].Name+'</option>';
        }
    }
    $("#models").html(html);

}

//加载车款
function selectmodel(id){
	if("-1" == id){
		//return;
	}
    var html='<option value="-1">请选择车款</option>';
    if (id) {
        for (var i = 0; i < _CIACarModelData.length; i++) {
        	if(_CIACarModelData[i].CategoryID == id){
        		getJxsList(_CIACarModelData[i].Name);
        	}
            if (_CIACarModelData[i].PID == id) {
            	html += '<option value="'+_CIACarModelData[i].CategoryID+'">'+_CIACarModelData[i].Name+'</option>';
            }
        }
    }
    $("#car_style").html(html);
//    $("#priceInput").val(0);
//    myPrice();
}
//电话号
function myPhone(){
	showBottom();
    var phone=document.getElementById("myPhone");
    if(phone.value.length!=11){
        message("手机号码输入有误，请检查！");
//        $("#myPhone").val("");
        $("#myPhone").attr("placeholder", "手机号码输入有误，请检查");
        $("#myPhone").addClass("warning");
        return false;
    }
    if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone.value))){
        message("手机号码输入有误，请检查！");
//        $("#myPhone").val("");
        $("#myPhone").attr("placeholder", "手机号码输入有误，请检查");
        $("#myPhone").addClass("warning");
        return false;
    }
    $("#myPhone").removeClass("warning");
    return true;
}
//身份证离开
function myID(){
	showBottom();
    var id=document.getElementById("idno");
    var idno = id.value;
    idno = idno.replace("x", "X");
    if(idno.length!=18){
        message("请输入正确的身份证号码");
//        $("#idno").val("");
        $("#idno").attr("placeholder", "请输入正确的身份证号码");
        $("#idno").addClass("warning");
        return false;
    }
    if(!IdentityCodeValid(idno)){
//    	$("#idno").val("");
        $("#idno").attr("placeholder", "请输入正确的身份证号码");
        $("#idno").addClass("warning");
        return false;
    }
    $("#idno").removeClass("warning");
    return true;
}
//姓名离开焦点事件
function myName(name){
	showBottom();
    var name=document.getElementById("applyName");
    if(name.value.length>16||name.value.length<=0){
        message("姓名输入有误，请检查！");
//        $("#applyName").val("");
        $("#applyName").attr("placeholder", "姓名输入有误，请检查");
        $("#applyName").addClass("warning");
        return false;
    }
    var regu = "^[\u4e00-\u9fa5]+$";
    var re = new RegExp(regu);
    if (!re.test( name.value)) {
    	message("姓名输入有误，请检查！");
//    	$("#applyName").val("");
        $("#applyName").attr("placeholder", "姓名输入有误，请检查");
        $("#applyName").addClass("warning");
        return false;
    }
    $("#applyName").removeClass("warning");
    return true;
}

//车价离开焦点
function myPrice(price){
	showBottom();
    var i=document.getElementById("priceInput").value;
    if(i.length==0){
    	message("请输入正确金额!");
//    	$("#price_warn").html("请输入正确的金额");
//    	$("#priceInput").val("");
    	$("#priceInput").attr("placeholder", "请输入您的首付金额");
        $("#priceInput").addClass("warning");
    	return false;
    }
    if ( isNaN(i) ) {
    	message("请输入正确金额!");
//    	$("#price_warn").html("请输入正确的金额");
//        $("#priceInput").val("");
        $("#priceInput").attr("placeholder", "请输入正确金额");
        $("#priceInput").addClass("warning");
        return false;
    }
    if(i.length>14){
    	message("超出贷款限额，请输入正确金额!");
//    	$("#price_warn").html("请输入正确的金额");
//    	$("#priceInput").val("");
        $("#priceInput").attr("placeholder", "请输入正确金额");
        $("#priceInput").addClass("warning");
        return false;
    }
    $("#price_warn").html("");
    $("#priceInput").val(format(parseInt(i),0));
    $("#priceInput").removeClass("warning");
}

//去除千分符
function removeThousand(num){
    if(num!=null&&num!=""&&num.length>0){
        return num.replace(new RegExp(/(,)/g),"");
    }
}

//格式化金额
var carCostParseFloat = function (val) {
    if (val == undefined) {
        return 0;
    }
    if (isNaN(val)) {
        return 0;
    }

    if (val == "") {
        return 0;
    }

    var result = parseFloat(val);
    if (result < 0) {
        result = 0;
    }

    return result;
};

//格式化金额
function format (num,fixed) {
    if(fixed==undefined){
        fixed = 0;
    }
    return (num.toFixed(fixed) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}
function IdentityCodeValid(code) {
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;

    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        tip = "请输入正确的身份证号码";
        pass = false;
    }

    else if(!city[code.substr(0,2)]){
        tip = "请输入正确的身份证号码";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "请输入正确的身份证号码";
                pass =false;
            }
        }
    }
    if(!pass) message(tip);
    return pass;
}
//获取验证码
function getCheckCode(){
	if(!checkInfo()){
		hideButton();
		return;
	}
	if(!myPhone()){
		hideButton();
		return;
	}
	var phone = $("#myPhone").val();
	$.JsonSRpc(baseUrl+"/wxgetapplycode", {phone:phone, openid:openid}, function(data){
		if(data.errcode&&"0"==data.errcode){
			message("验证码已发送");
			var obj = $(".get-code");
			countDown(obj);
		}else{
			message(data.errmsg);
		}
	});
	showButton();
}
//获取验证码
var wait = 60;
function countDown(o){
	if (wait == 0) { 
		flag="false";
		o.css("background","#c8161d");
		o.removeAttr("disabled");    
		o.html("获取验证码") ;
        wait = 60;  
    } else {  
    	flag="true";
    	o.css("background","gray");
    	o.attr("disabled", true);  
    	o.html("重新发送("+wait+")");  
        wait--;  
        setTimeout(function() {  
        	countDown(o);
        },  1000);  
    }  
}
//提交
function submit(){
	if(!$("#submit").hasClass("disabled")) {
		if(!checkInfo()){
			hideButton();
			return;
		}
		if(!myPhone()){
			hideButton();
			return;
		}
		obj.openid = openid;
		obj.phone = $("#myPhone").val();
		var check_code = $("#check_code").val();
		if(check_code==undefined||check_code==""){
	        message("验证码不能为空");
	        $("#check_code").addClass("warning");
	        return;
	    }
		obj.check_code = check_code;
		if(sessionStorage.car_style != undefined && sessionStorage.car_style != ""){
			obj.style=sessionStorage.car_style;
		}
		$.showLoadMsg("申请中，请稍后...");
		$.JsonSRpc(baseUrl+"/wxloanapply", obj, function(data){
			$.hideLoadMsg();
			message(data.errmsg, function(flag){
				if(data.errcode&&"0"==data.errcode){
					wx.closeWindow();
				}
			});
		});
	}
}

function checkInfo(){
	var brand = $("#brand").val();
	var models = $("#models").val();
	var priceInput = $("#priceInput").val().split(",").join("");;
	var jxs_province = $("#jxs_province").val();
	var jxs_city = $("#jxs_city").val();
	var shops = $("#shops").val();
	var applyName = $("#applyName").val();
	var sex = $("input[name='sex']:checked").val();
	var idno = $("#idno").val();
	idno = idno.replace("x", "X");
	if(!brand||brand == "-1"){
		message("请选择品牌");
		return false;
	}
	if(!models || models == "-1"){
		message("请选择车型");
		return false;
	}
	if(!priceInput||priceInput == 0){
		message("请输入首付金额");
		return false;
	}
	if(!jxs_province||jxs_province == "-1"){
		message("请选择经销商省份");
		return false;
	}
	if(!jxs_city || jxs_city == "-1"){
		message("请选择经销商城市");
		return false;
	}
	if(!shops || shops == "-1"){
		message("请选择经销商");
		return false;
	}
    if(!myName(applyName)){
    	return false;
    }
    if(!myID()){
    	return false;
    }
    obj.brand = $("#brand").find("option:selected").text();
	obj.card_id = idno;
	obj.city = $("#jxs_city").find("option:selected").text();
	obj.first_amt = priceInput;
	obj.franchiser = shops;
	obj.model = $("#models").find("option:selected").text();
	obj.name = applyName;
	obj.province = $("#jxs_province").find("option:selected").text();
	obj.sex = sex;
    return true;
}