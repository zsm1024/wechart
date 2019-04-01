//照片部分
// function imgs(){
	// $("#head_img_change").change(function() {
	// 　　　　　　var $file = $(this);
	// 　　　　　　var fileObj = $file[0];
	// 　　　　　　var windowURL = window.URL || window.webkitURL;
	// 　　　　　　var dataURL;
	// 　　　　　　var $img = $("#headimg");
	// 　　　　　　if(fileObj && fileObj.files && fileObj.files[0]){
	// 　　　　　　　　dataURL = windowURL.createObjectURL(fileObj.files[0]);
	// 　　　　　　　　$img.attr('src',dataURL);				
			// $(".mug_61").hide();
			// $("#headimg").addClass('headimg_1');
			// $(".mug_4").css({
				   // position: "absolute",
					// top: "1.4rem",
   					// left: "1.05rem",
   				 // transform:"scale(.6)",
				
			// }) 
	// 　　　　　　}else{
	// 　　　　　　　　dataURL = $file.val();
	// 　　　　　　　　var imgObj = document.getElementById("headimg");
	// 　　　　　　　　// 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性再加入，无效；
	// 　　　　　　　　// 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
	// 　　　　　　　　imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
	// 　　　　　　　　imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;								
	// 　　　　　　}
	// //return false;
// 　　　　});
	// $("#head_img_change_1").change(function() {
	// 　　　　　　var $file = $(this);
	// 　　　　　　var fileObj = $file[0];
	// 　　　　　　var windowURL = window.URL || window.webkitURL;
	// 　　　　　　var dataURL;
	// 　　　　　　var $img = $("#headimg_f");
	// 　　　　　　if(fileObj && fileObj.files && fileObj.files[0]){
	// 　　　　　　　　dataURL = windowURL.createObjectURL(fileObj.files[0]);
	// 　　　　　　　　$img.attr('src',dataURL);
			// localStorage.setItem("imgRight",dataURL);						
			// $(".mug_62").hide();
			// $("#headimg_f").addClass('headimg_1');
			// $(".mug_5").css({
				   // position: "absolute",
					// top: "1.4rem",
   					// left: "2.9rem",
   				 // transform:"scale(.6)",
				
			// })
	// 　　　　　　}else{
	// 　　　　　　　　dataURL = $file.val();
	// 　　　　　　　　var imgObj = document.getElementById("headimg_f");
	// 　　　　　　　　// 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性再加入，无效；
	// 　　　　　　　　// 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
	// 　　　　　　　　imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
	// 　　　　　　　　imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
					
					
	// 　　　　　　}
	// 　　　　});		
	
// }
// imgs();


$(function(){
	
	init();
})

function init(){
	if(localStorage.identityNum!=null ||localStorage.identityNum!=undefined )
	{
		$("#identity").val(localStorage.identityNum);
	}
	if(localStorage.UserName!=null ||localStorage.UserName!=undefined )
	{
		$("#name").val(localStorage.UserName);
	}
	if(localStorage.UserBirthday!=null ||localStorage.UserBirthday!=undefined )
	{
		$("#result").val(localStorage.UserBirthday);
	}
	if(localStorage.CardTime!=null ||localStorage.CardTime!=undefined )
	{
		$("#idTime").val(localStorage.CardTime);
	}
	if(localStorage.UserMember!=null ||localStorage.UserMember!=undefined )
	{
		$("#homeNum").val(localStorage.UserMember);
	}
	if(localStorage.workYear!=null ||localStorage.workYear!=undefined )
	{
		$("#workNum").val(localStorage.workYear);
	}
}

var localIds;
//选择图片
function imgClick(im){
	wx.chooseImage({
	    count: 1, // 默认9
	    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	        localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
			//alert(browser.versions.ios);
			
			if(browser.versions.ios)
			{
				wx.getLocalImgData({  
                  localId:localIds[0].toString(), // 图片的localID
                   success: function (res) {
					   var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
						localData = localData.replace('jgp', 'jpeg');//iOS 系统里面得到的数据，类型为 image/jgp,因此需要替换一下
						//alert(localData);
						localIds=localData;//把base64格式的图片添加到ioslocId数组里 这样该数组里的元素都是base64格式的
				   },
				   fail:function(res){
						//$.hideLoadMsg();
						message(JSON.stringify(res));
					}
				})
			}
			
			if(im=="2"){
				$("#headimg").attr("src", localIds[0].toString());			
				$(".mug_61").hide();
				$("#headimg").addClass('headimg_1');
				$(".mug_4").css({
				   position: "absolute",
					top: "1.4rem",
   					left: "1.05rem",
   				   transform:"scale(.6)",
				
				}) 
			}else{
				$("#headimg_f").attr("src", localIds[0].toString());
				$(".mug_62").hide();
				$("#headimg_f").addClass('headimg_2');
				$(".mug_5").css({
				   position: "absolute",
					top: "1.4rem",
   					left: "2.9rem",
   				    transform:"scale(.6)",
				
			})
			}
			
			
			setTimeout(function(){
			
				wx.uploadImage({
					localId: res.localIds[0].toString(), // 需要上传的图片的本地ID，由chooseImage接口获得
					isShowProgressTips: 1, // 默认为1，显示进度提示
					success: function (res) {
						
						$.showLoadMsg("OCR解析中，请稍后...");
						var serverId = res.serverId; // 返回图片的服务器端ID
						var date=convertDate(new Date());
						param.timestamp=date;
						var data="{'mediaid':'"+serverId+"','imageType':'"+im+"'}";
						
						
						param.data=data;
						
						var s="";
						data=key+data+date+key;
						for(var i=0;i<data.length;i++)
						{
							s+="\\u" +('0000'+data.charCodeAt(i).toString(16)).slice(-4);
						}
						var signcode=$.md5(s);
						param.code='UploadImg';
						param.signcode=signcode;
						$.JsonSRpc(interfaceUrl, param, function(result){
							if(result.resultCode=='0000')
							{
								if(im=="2"){
									$("#identity").val(result.resultData.identityNum);
									$("#name").val(result.resultData.UserName);
									$("#UserSex").val();
									var zw="";
									$.each(sex, function(i,elem) {
										if(result.resultData.UserSex==elem.NAME){
										zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
									}else{
										zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";		
										}
				
									});
									$(".sex").append(zw);
									
									
									var nation=$CodeSource['00001HBSRAVDD0000A1B']  
									var na="";
									$.each(nation, function(i,elem) {
										if(result.resultData.userNation==elem.NAME){
											na+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
										}else{
											na+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";		
										}
										
									});
									$(".nation").append(na);
									
									
									$("#result").val(result.resultData.UserBirthday);
									localStorage.setItem("imagePath1",result.resultData.imagePath1);
									$.hideLoadMsg();
								}
								else
								{
									$("#idTime").val(result.resultData.CardTime);
									localStorage.setItem("imagePath2",result.resultData.imagePath2);
									$.hideLoadMsg();

								}
							}
								//alert(1);
							else{
								$.hideLoadMsg();
								if(im=="2"){
									localStorage.setItem("imagePath1",result.resultData.imagePath1);
								}else{
									localStorage.setItem("imagePath2",result.resultData.imagePath2);
								}
								message("解析失败，请手动填写相关信息！");
							}
						});
					},
					fail:function(res){
						$.hideLoadMsg();
						message(JSON.stringify(res));
					}
				});
				},100);
	    },
	    fail:function(res){
	    	message("选择照片失败");
	    }
	});
}
//上传图片
function uploadImg(){
	
	if(localIds == undefined || localIds.length <= 0){
		message("请选择照片");
		return;
	}
	
}

//.............................数据绑定............................
//学历
	var educ=$CodeSource['00001H8RUUJY00000C0B'];
	var zw="";
	$.each(educ, function(i,elem) {
		if(localStorage.UserEduc==elem.CODE){
			zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
		}else{
			zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
		}
		
	});
	$(".educ").append(zw);
//民族
	var nation=$CodeSource['00001HBSRAVDD0000A1B']  
	var zw="";
	$.each(nation, function(i,elem) {
		if(localStorage.UserNation==elem.CODE){
		zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
		}else{
			zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";		
		}
		
	});
	$(".nation").append(zw);
	
//户口所在地
	var home=$CodeSource['00001H8RUUJY00000C0G'];
	var zw="";
	$.each(home, function(i,elem) {
		if(localStorage.UserHome==elem.CODE){
			zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";		
		}else{
			zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
		}
		
	});
	$(".home").append(zw);
//婚姻状况
	var marry= $CodeSource['00001H8RUUJY00000C05'];
	var zw="";
	$.each(marry, function(i,elem) {
		if(localStorage.UserMarry==elem.CODE){
			zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
		}else{
			zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";	
		}
		
	});
	$(".marry").append(zw);
//性别
	var sex= $CodeSource['000005JIA84C00000A00'];
	var zw="";
	$.each(sex, function(i,elem) {
		if(localStorage.UserSex==elem.CODE){
			zw+="<option selected value='"+elem.CODE +"'>"+elem.NAME+"</option>";
		}else{
			zw+="<option  value='"+elem.CODE +"'>"+elem.NAME+"</option>";		
		}
		
	});
	$(".sex").append(zw);
//.............................表单验证部分............................	
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
 //验证日期是否正确
 function isTime(time){
 	var pattern=/^((?:19|20)\d\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/;
 	return pattern.test(time);
 }
// 验证函数
function formValidate() {
var str = '';	
//判断是否上传图片
 var mug1= $(".mug .mug_1").find("img").hasClass("headimg_1");
	 if(mug1!=true){
		 str += "身份证正面照未上传;\n";	
	 }
	 var mug2= $(".mug .mug_2").find("img").hasClass("headimg_2");
	 if(mug2!=true){
		 str += "身份证反面照未上传;\n";	
	 }
// 验证身份证
	 if($.trim($('#identity').val()).length == 0){ 
	  str += '身份证号码未输入;\n';
	  $("#identity").parent().find("label").css("color","red");
	 } else {
	  if(isCardNo($.trim($('#identity').val())) == false) {
	   str += '请输入正确的身份证号;\n';	  
	   $("#identity").parent().find("label").css("color","red");
	  }
	  $("#identity").parent().find("label").css("color","#000");
	 }
// 判断姓名
	 if($.trim($('#name').val()).length == 0) {
	  str += '姓名未填写;\n';
	$("#name").parent().find("label").css("color","red");
	 } else {
	  if(isChinaName($.trim($('#name').val())) == false) {
	   str += '请输入正确的姓名;\n';
		$("#name").parent().find("label").css("color","red");
	  }
	   $("#name").parent().find("label").css("color","#000");
	 }
//判断 出生日期是否填写
	 var results= $("#result").val();
		if($.trim(results).length == 0){
			str += "出生日期未填写;\n";
			$("#result").parents('.calender').find("label").css("color","red");
		}else {
			if(isTime($("#result").val())==false){
				str+="出生日期格式不正确;"
				$("#result").parents('.calender').find("label").css("color","red");
			}else{
				$("#result").parents('.calender').find("label").css("color","#000");
			}	
		}
		//判断 出生日期是否填写
	 var results1= $("#idTime").val();
	 if($.trim(results1).length == 0){
			str += "身份证到期日未填写;\n";
			$("#idTime").parents('.calender').find("label").css("color","red");
		}else {
			if(isTime(results1)==false){
				str+="身份证到期日格式不正确;"
				$("#idTime").parents('.calender').find("label").css("color","red");
			}else{
				$("#idTime").parents('.calender').find("label").css("color","#000");
			}
	
		}
//学历选择判断
	var educ= $(".educ option:selected").val();
	if(educ=="请选择"){
			$(".educ").parent().find("label").css("color","red");
			str += "学历未填写;\n";		
		}else{
			$(".educ").parent().find("label").css("color","#000");
		}
//民族判断
	var nation= $(".nation option:selected").val();
	if(nation=="请选择"){
			$(".nation").parent().find("label").css("color","red");
			str += "民族未填写;\n";		
		}else{
			$(".nation").parent().find("label").css("color","#000");
		}

//户口所在地判断
	var home=$(".home option:selected").val();
	if(home=="请选择"){
			$(".home").parent().find("label").css("color","red");
			str += "户口类型未填写;\n";		
		}else{
			$(".home").parent().find("label").css("color","#000");
		}
//婚姻状况验证
	var marry=$(".marry option:selected").val();
	if(marry=="请选择"){
			$(".marry").parent().find("label").css("color","red");
			str += "婚姻状况未填写;\n";		
		}else{
			$(".marry").parent().find("label").css("color","#000");
		}
//性别验证
	var sex=$(".sex option:selected").val();
	if(sex=="请选择"){
			$(".sex").parent().find("label").css("color","red");
			str += "性别未填写;\n";		
		}else{
			$(".sex").parent().find("label").css("color","#000");
		}
//家庭人数
var homeNum=$("#homeNum ").val();
	if(homeNum=="0"){
			$("#homeNum").parent().parent().find("label").css("color","red");
			str += "家庭人数不能为0;\n";		
		}else{
			$("#homeNum").parent().parent().find("label").css("color","#000");
		}
//工作年限
var workNum=$("#workNum").val();
	if(workNum=="0"){
			$("#workNum").parent().parent().find("label").css("color","red");
			str += "工作年限不能为0;\n";		
		}else{
			$("#workNum").parent().parent().find("label").css("color","#000");
		}
// 如果没有错误则提交
	 if(str != '') {	 	
	  message(str);
	  return false;
	 } else {
		 
	  $(".commen-bottom .perfect").attr("href","addressInfo.html");	
	 }
}
	 
$('.perfect').on('click', function() {
	 formValidate(); 
	 jsonString();
});
function jsonString(){
	var data=$("form").serializeArray(); //自动将form表单封装成json						
       for (var i=0;i<data.length;i++) {
        localStorage.setItem(data[i].name,data[i].value)
        } 
        //var birthDay=$("#result").html();
        //localStorage.setItem("UserBirthday",birthDay);   
} 
var browser={
    versions:function(){
        var u = navigator.userAgent, 
            app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            weixin: u.indexOf('MicroMessenger') > -1, //是否微信 （2015-01-22新增）
            qq: u.match(/\sQQ/i) == " qq" //是否QQ
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase()
}
