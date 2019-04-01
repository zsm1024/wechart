$(function () {
	init();
})

function init() {
	if (localStorage.FirrelationName != null || localStorage.FirrelationName != undefined) {
		$("#FirrelationName").val(localStorage.FirrelationName);
	}
	if (localStorage.FirrelationPhone != null || localStorage.FirrelationPhone != undefined) {
		$("#FirrelationPhone").val(localStorage.FirrelationPhone);
	}
	if (localStorage.FirrelationAddress != null || localStorage.FirrelationAddress != undefined) {
		$("#FirrelationAddress").val(localStorage.FirrelationAddress);
	}
	if (localStorage.FirrelationID != null || localStorage.FirrelationID != undefined) {
		$("#FirrelationID").val(localStorage.FirrelationID);
	}
	if (localStorage.FirrelationSalare != null || localStorage.FirrelationSalare != undefined) {
		$("#FirrelationSalare").val(localStorage.FirrelationSalare);
	}
	if (localStorage.SecrelationName != null || localStorage.SecrelationName != undefined) {
		$("#SecrelationName").val(localStorage.SecrelationName);
	}
	if (localStorage.SecrelationPhone != null || localStorage.SecrelationPhone != undefined) {
		$("#SecrelationPhone").val(localStorage.SecrelationPhone);
	}
	if (localStorage.SecrelationAddress != null || localStorage.SecrelationAddress != undefined) {
		$("#SecrelationAddress").val(localStorage.SecrelationAddress);
	}
	if (localStorage.SecrelationID != null || localStorage.SecrelationID != undefined) {
		$("#SecrelationID").val(localStorage.SecrelationID);
	}
	if (localStorage.SecrelationSalare != null || localStorage.SecrelationSalare != undefined) {
		$("#SecrelationSalare").val(localStorage.SecrelationSalare);
	}
	if (localStorage.cardType != null || localStorage.cardType != undefined) {
		$("#cardType").val(localStorage.cardType);
	}
	if (localStorage.cardTypeTow != null || localStorage.cardTypeTow != undefined) {
		$("#cardTypeTow").val(localStorage.cardTypeTow);
	}

}
//..................................证件类型获取..................................
 var cardID =$CodeSource['00001H8RUUJY00000C0C'];
 var CardType='', CardType1='';
 $.each(cardID, function (i, elem) {
	console.log(elem)
	if (localStorage.cardType == elem.CODE) {
		CardType += "<option selected value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	} else {
		CardType += "<option  value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	}
	if (localStorage.cardTypeTow == elem.CODE) {
		CardType1 += "<option selected value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	} else {
		CardType1 += "<option  value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	}
});
$(".emerFir").find(".cardType").append(CardType);
$(".emerSec").find(".cardTypeTow").append(CardType1);

//.................................表单验证部分...................................
var rela = $CodeSource['00001H8RUUJY00000C06'];
var zw = "",
	zw1 = "";
$.each(rela, function (i, elem) {
	if (localStorage.Firrelation == elem.CODE) {
		zw += "<option selected value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	} else {
		zw += "<option  value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	}
	if (localStorage.Secrelation == elem.CODE) {
		zw1 += "<option selected value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	} else {
		zw1 += "<option  value='" + elem.CODE + "'>" + elem.NAME + "</option>";
	}
});
$(".emerFir").find(".relation").append(zw);
$(".emerSec").find(".relation").append(zw1);
// 验证中文名称
function isChinaName(name) {
	var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
	return pattern.test(name);
}
// 验证手机号
function isPhoneNo(phone) {
	var pattern = /^1[34578]\d{9}$/;
	return pattern.test(phone);
}
// 验证身份证 
function isCardNo(card) {
	var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	return pattern.test(card);
}
// var strc="";
// var strb="";

//...............................................紧急联系一...................................
// $.each($(".relation"),function(i,elem){
// 	$(this).change(function(){
// 		debugger;
//	   	var relation= $(this).find("option:selected").val();
//	   	if(relation=="1155AD69569644E78B83"){
//			$(this).find(".mus").show();				
//		}else{
//			$(this).find(".mus").hide();
//		}
//	   });
// });

$(".emerFir .relation").change(function () {
	var re1 = $(".emerFir .relation").find("option:selected").val();
	// var card1 =$(".emerFir .cardType").find("option:selected").val();
	var re2 = $(".emerSec .relation").find("option:selected").val();
	// var card2 =$(".emerFir .cardTypeTow").find("option:selected").val();
	var relation = $(this).find("option:selected").val();
	if (re1 == "00006") {
		if (re2 == "00006") {
			message("与申请人关系重复");

		} else {
			if (relation == "00006") {
				$(".emerFir .mus").show();

			} else {
				$(".emerFir .mus").hide();
			}
		}
	} else {
		if (relation == "00006") {
			$(".emerFir .mus").show();
		} else {
			$(".emerFir .mus").hide();
		}
	}
});
$(".emerSec .relation").change(function () {
	var re1 = $(".emerFir .relation").find("option:selected").val();
	var re2 = $(".emerSec .relation").find("option:selected").val();
	// var card1 =$(".emerFir .cardType").find("option:selected").val();
	// var card2 =$(".emerFir .cardTypeTow").find("option:selected").val();
	if (re2 == "00006") {
		if (re1 == "00006") {
			message("与申请人关系重复");
		} else {
			var relation = $(this).find("option:selected").val();
			if (relation == "00006") {
				$(".emerSec .mus").show();
			} else {
				$(".emerSec .mus").hide();
			}
		}
	}
});

function formValidate(obj) {
	//与申请人关系验证
	var str = "";
	var str1 = "";
	var str2 = "";
	str1 += "<div><p class='hom1'>紧急联系人一</p><div class='p1'>";
	var relation = $(".emerFir").find(".relation option:selected").val();
	 var card1 =$(".emerFir .cardType").find("option:selected").val();
	 console.log(card1)
	if (relation == "请选择") {
		$(".emerFir .relation").parent().find("label").css("color", "red");
		// $(".emerFir .cardType").parent().find("label").css("color", "red");
		str1 += "与申请人关系未填写;\n";
	} else {
		$(".emerFir").find(".relation").parent().find("label").css("color", "#000");
		$(".emerFir").find(".cardType").parent().find("label").css("color", "#000");
		if (relation == "00006") {
			if ($.trim($(".emerFir").find('.selfInd').val()).length == 0||$.trim($(".emerFir").find('.selfInd').val())=='请选择') {
				str1 += '证件号或证件类型未填写;\n';
				$(".emerFir .selfInd").parent().find("label").css("color", "red");
				$(".emerFir .cardType").parent().find("label").css("color", "red");

			} else {
				if(card1=='00001'&&relation == "00006"){
					if (isCardNo($.trim($(".emerFir").find('.selfInd').val())) == false) {
							str1 += '请输入正确的证件号;\n';
							$(".emerFir .selfInd").parent().find("label").css("color", "red");
							$(".emerFir .cardType").parent().find("label").css("color", "red");
						}
						$(".emerFir .selfInd").parent().find("label").css("color", "#000");
						$(".emerFir .cardType").parent().find("label").css("color", "#000");
				}
				// if (isCardNo($.trim($(".emerFir").find('.selfInd').val())) == false) {
				// 	str1 += '请输入正确的身份证号;\n';
				// 	$(".emerFir .selfInd").parent().find("label").css("color", "red");
				// }
				// $(".emerFir .selfInd").parent().find("label").css("color", "#000");

			}
			var selfMoney = $(".emerFir").find(".selfMoney").val();
			if (selfMoney == "0") {
				str1 += "月收入不能为0\n;";
				$(".emerFir .selfMoney").parent().siblings("label").css("color", "red");
			} else {
				$(".emerFir .selfMoney").parent().siblings("label").css("color", "#000");
			}
		}
	}
	//姓名验证
	// 判断姓名
	if ($.trim($(".emerFir").find('.selfName').val()).length == 0) {
		str1 += '姓名未填写;\n';
		$(".emerFir .selfName").parent().find("label").css("color", "red");
	} else {
		if (isChinaName($.trim($(".emerFir").find('.selfName').val())) == false) {
			str1 += '姓名不合法;\n';
			$(".emerFir .selfName").parent().find("label").css("color", "red");
		}
		$(".emerFir .selfName").parent().find("label").css("color", "#000");
	}
	// 判断手机号码
	if ($.trim($(".emerFir").find('.phone').val()).length == 0) {
		str1 += '手机号未输入;\n';
		//$('#phone').focus();
		$(".emerFir .phone").parent().find("label").css("color", "red");
	} else {
		if (isPhoneNo($.trim($(".emerFir").find('.phone').val())) == false) {
			str1 += '请输入正确的手机号;\n';
			$(".emerFir .phone").parent().find("label").css("color", "red");
		}
		$(".emerFir .phone").parent().find("label").css("color", "#000");
	}

	str1 += "</div></div>";

	//.........................................紧急联系二.....................................
	str2 += "<div><p class='work1'>紧急联系人二</p><div class='p2'>"
	var relation1 = $(".emerSec .relation option:selected").val();
	var TypeTow = $(".emerSec .cardTypeTow option:selected").val();
	console.log( TypeTow)
	console.log($.trim($('.emerSec .cardTypeTow').val()))
	if (relation1 == "请选择") {

		$(".emerSec .relation").parent().find("label").css("color", "red");
		// $(".emerSec .cardTypeTow").parent().find("label").css("color", "red");
		str2 += "与申请人关系未填写;\n";
	} else {
		$(" .emerSec .relation").parent().find("label").css("color", "#000");
		$(" .emerSec .cardTypeTow").parent().find("label").css("color", "#000");
		if (relation1 == "00006") {
			if ($.trim($('.emerSec .selfInd').val()).length == 0||$.trim($('.emerSec .cardTypeTow').val())=='请选择') {
				str2 += '证件号或证件类型未填写;\n';
				$(".emerSec .selfInd").parent().find("label").css("color", "red");
				$(".emerSec .cardTypeTow").parent().find("label").css("color", "red");
			} else {
				if(relation1 == "00006"&&TypeTow=='00001'){
				if (isCardNo($.trim($('.emerSec .selfInd').val())) == false) {
					str2 += '请输入正确的证件号;\n';
					$(".emerSec .selfInd").parent().find("label").css("color", "red");
				}
				$(".emerSec .selfInd").parent().find("label").css("color", "#000");
				$(".emerSec .cardTypeTow").parent().find("label").css("color", "#000");

							// console.log(relation1)
				}
				// if (isCardNo($.trim($('.emerSec .selfInd').val())) == false) {
				// 	str2 += '请输入正确的身份证号;\n';
				// 	$(".emerSec .selfInd").parent().find("label").css("color", "red");
				// }
				// $(".emerSec .selfInd").parent().find("label").css("color", "#000");

			}
			var selfMoney1 = $(".emerSec .selfMoney").val();
			if (selfMoney1 == "0") {
				str2 += "月收入不能为0\n;";
				 $(".emerSec .selfMoney").parent().siblings("label").css("color", "red");
			} else {
				$(".emerSec .selfMoney").parents().siblings("label").css("color", "#000");
			}
		}
	}
	//姓名验证
	// 判断姓名
	if ($.trim($('.emerSec .selfName').val()).length == 0) {
		str2 += '姓名未填写;\n';
		$(".emerSec .selfName").parent().find("label").css("color", "red");
	} else {
		if (isChinaName($.trim($('.emerSec .selfName').val())) == false) {
			str2 += '姓名不合法;\n';
			$(".emerSec .selfName").parent().find("label").css("color", "red");
		}
		$(".emerSec .selfName").parent().find("label").css("color", "#000");
	}
	// 判断手机号码
	if ($.trim($('.emerSec .phone').val()).length == 0) {
		str2 += '手机号未输入;\n';
		//$('#phone').focus();
		$(".emerSec .phone").parent().find("label").css("color", "red");
	} else {
		if (isPhoneNo($.trim($('.emerSec .phone').val())) == false) {
			str2 += '请输入正确的手机号;\n';
			$(".emerSec .phone").parent().find("label").css("color", "red");
		}
		$(".emerSec .phone").parent().find("label").css("color", "#000");
	}
	str2 += "</div></div>";
	str += str1 + str2;
	var strm = "<div><p class='hom1'>紧急联系人一</p><div class='p1'></div></div><div><p class='work1'>紧急联系人二</p><div class='p2'></div></div>";
	var strm1 = "<div><p class='hom1'>紧急联系人一</p></div>";
	var strm2 = "<div><p class='work1'>紧急联系人二</p></div>";
	if (str != strm) {
		message(str);
		return false;
	} else {
		jsonString();
		$.showLoadMsg("加载中，请稍后...");

		var date = convertDate(new Date());
		param.timestamp = date;
		var data = JSON.stringify(localStorage).replace(/\"/g, "'").replace(/\\/, '');
		param.data = data;
		//console.log(data);
		var s = "";
		data = key + data + date + key;
		for (var i = 0; i < data.length; i++) {
			s += "\\u" + ('0000' + data.charCodeAt(i).toString(16)).slice(-4);
		}
		var signcode = $.md5(s);
		param.code = 'saveOrder';
		param.signcode = signcode;

		$.JsonSRpc(interfaceUrl, param, function (result) {
			if (result) {
				if (result.resultCode == '0000') {
					if (result.resultData.result == "0") {
						message("提交成功！");
						//$(".commen-bottom .perfect").attr("href","successOrder.html");	
						window.location.href = "successOrder.html";
						$.hideLoadMsg();
					} else if (result.resultData.result == "1") {
						message("提交异常！");
						$.hideLoadMsg();
					} else {
						message("数据异常！");
						$.hideLoadMsg();
					}

				} else {
					message("提交异常！");
					$.hideLoadMsg();
				}
			} else {
				message("提交异常！");
				$.hideLoadMsg();
			}
		});

	}
}
//var firstS=$(".emerFir");
//var secondS=$(".emerSec");
$('.perfect').on('click', function () {
	var re1 = $(".emerFir .relation").find("option:selected").val();
	var re2 = $(".emerSec .relation").find("option:selected").val();
	if (re1 == "1155AD69569644E78B83" && re2 == "1155AD69569644E78B83") {
		message("与申请人关系重复");
		return false;
	} else {
		formValidate();




	}
	//	var msg="";
	//	$.each($(".mui-input-group"),function(){
	//		msg+=(formValidate($(this))||"");
	//		jsonString();
	//	});		

	//	if(msg != ''){
	//	 	message(msg);
	//	}else{		
	//		$(".commen-bottom .perfect").attr("href","successOrder.html");
	//	}
});

function jsonString() {
	var data = $("form").serializeArray(); //自动将form表单封装成json						
	for (var i = 0; i < data.length; i++) {
		localStorage.setItem(data[i].name, data[i].value)

	}
}