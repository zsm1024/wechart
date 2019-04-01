var editIndex = undefined;
var total=0;
$(function(){
	InitPagenation();
	$('#btn_submit').click(function(){
		//alert("提交");
		endEditing();
	});
	$('#btn_channel').click(function(){
		selectApplyInfo();
		editIndex=undefined;
	});
	//申请单状态
	$('#com_status').combobox({data:[{"id":"1","text":"未受理"},{"id":"2","text":"已受理"}] ,
		    valueField:"id", textField:"text", panelHeight:"auto", editable:false,
		    onChange:function(newValue, oldValue){  
				$("#com_status").val(newValue) ;
		    }
		}).combobox('clear') ;
	//申请单来源
	$('#com_source').combobox({data:[{"id":"1","text":"金融官网"},{"id":"2","text":"金融微信服务号"}
	,{"id":"3","text":"哈弗商城"},{"id":"4","text":"品牌官网"}] ,
	    valueField:"id", textField:"text", panelHeight:"auto", editable:false,
	    onChange:function(newValue, oldValue){  
			$("#com_status").val(newValue) ;
	    }
	}).combobox('clear') ;
	//selectApplyInfo();
	$('#btn_exporterExcel').click(function(){
		//执行导入excel
		exproterExcel();
	});
	$('#com_status').combobox('setValue','1');//='1';
	$('#com_source').combobox('setValue','1');
});


function onClickRow(index){
	var rows = $('#list').datagrid('getSelections');
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if(row){
			if(row.dostatus=="2"){
				alert("此记录已处理不可修改");
				return;
			}
		}
	}
	if (editIndex != index){
		if (endEditing()){
			$('#list').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#list').datagrid('selectRow', editIndex);
		}
	}
}

function endEditing(){
	if (editIndex == undefined){return true;}
	if ($('#list').datagrid('validateRow', editIndex)){
		var ed = $('#list').datagrid('getEditor', {index:editIndex,field:'dostatus'});
		if(ed!=null){
			var name = $(ed.target).combobox('getText');
			$('#list').datagrid('getRows')[editIndex]['dostatus'] = name;
			$('#list').datagrid('endEdit', editIndex);
			var rows=$('#list').datagrid('getRows');
			if(rows[editIndex].dostatus=='2'){
				updateApplyInfo(rows[editIndex].application_num,rows[editIndex].dostatus);
			}
			editIndex = undefined;
			return true;
		}
		else{
			return false;
		}
		
	} else {
		return false;
	}
}

function channelEditing(){
	if(editIndex==undefined)
		return;
	selectApplyInfo();
	editIndex = undefined;
}

function openPostWindow(url,source,status,name,phone,starttime,endtime){
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面
    var hideInput1 = document.createElement("input");
    hideInput1.type = "hidden";
    hideInput1.name="source"; //后台要接受这个参数来取值
    hideInput1.value = source; //后台实际取到的值
    
    var hideInput2 = document.createElement("input");
    hideInput2.type = "hidden";
    hideInput2.name="status";
    hideInput2.value = status;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    var hideInput3 = document.createElement("input");
    hideInput3.type = "hidden";
    hideInput3.name="name";
    hideInput3.value = name;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    var hideInput4 = document.createElement("input");
    hideInput4.type = "hidden";
    hideInput4.name="phone";
    hideInput4.value = phone;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    var hideInput5 = document.createElement("input");
    hideInput5.type = "hidden";
    hideInput5.name="starttime";
    hideInput5.value = starttime;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    var hideInput6 = document.createElement("input");
    hideInput6.type = "hidden";
    hideInput6.name="endtime";
    hideInput6.value = endtime;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    tempForm.appendChild(hideInput1);
    tempForm.appendChild(hideInput2);
    tempForm.appendChild(hideInput3);
    tempForm.appendChild(hideInput4);
    tempForm.appendChild(hideInput5);
    tempForm.appendChild(hideInput6);
    if(document.all){
        tempForm.attachEvent("onsubmit",function(){});        //IE
    }else{
        var subObj = tempForm.addEventListener("submit",function(){},false);    //firefox
    }
    document.body.appendChild(tempForm);
    if(document.all){
        tempForm.fireEvent("onsubmit");
    }else{
        tempForm.dispatchEvent(new Event("submit"));
    }
    tempForm.submit();
    document.body.removeChild(tempForm);
}

function exproterExcel(){
	var source=$('#com_source').combobox('getValue');//来源
	var status=$('#com_status').combobox('getValue');//状态
	var name=document.getElementById("txt_name").value;//姓名
	var phone=document.getElementById("txt_phone").value;//手机
	var starttime=$('#start_date').datetimebox('getValue');//开始时间
	var endtime=$('#end_date').datetimebox('getValue');//结束时间
	var url=basePath+"/exportApplyInfo";
	openPostWindow(url,source,status,name,phone,starttime,endtime);
}

function resetQuery(){
	$('#com_status').combobox('setValue','');
	$('#com_source').combobox('setValue','');
	$('#txt_name').val('');
	$('#txt_phone').val('');
	$('#start_date').datetimebox('setValue','');
	$('#end_date').datetimebox('setValue','');
}

function selectApplyInfo(){
	editIndex=undefined;
	var source=$('#com_source').combobox('getValue');//来源
	var status=$('#com_status').combobox('getValue');//状态
	var name=document.getElementById("txt_name").value;//姓名
	var phone=document.getElementById("txt_phone").value;//手机
	var starttime=$('#start_date').datetimebox('getValue');//开始时间
	var endtime=$('#end_date').datetimebox('getValue');//结束时间
	var url=basePath+"/selectApplyInfo";
	var grid = $('#list');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
	var pagesize = options.pageSize;  
	$.JsonSRpc(url,{
			source:source,
			status:status,
			name:encodeURI(name),
			phone:phone,
			starttime:starttime,
			endtime:endtime,
			pagenumber:curr,
			pagesize:pagesize
		},
		function(data){
			if(data.error!="0000"){
				alert(data.error_msg);
				$('#list').datagrid('loadData', { total: 0, rows: [] }); 
				return;
			}else{
				$("#list").data().datagrid.cache = null;  
				$('#list').datagrid('loadData', data);
			}
		});
}

//更新在线申请状态
function updateApplyInfo(id,dostatus){
	var apply_id=id;//申请单号
	var status=dostatus;//处理
	var url=basePath+"/updateApplyInfo";
	$.JsonSRpc(url,{
		apply_id:apply_id,
		status:status,
		operator:userid
		},
		function(data){
			if(data[0].error!="0000"){
				alert(data[0].error_msg);
				return false;
			}else{
				alert(data[0].error_msg);
				selectApplyInfo();
				return true;
			}
	});
}

function InitPagenation() {
    //设置分页控件 
    var p = $('#list').datagrid('getPager');
    $(p).pagination({
        pageSize: 10, //每页显示的记录条数，默认为10 
//        pageList: [10, 20, 30], //可以设置每页记录条数的列表 
        onSelectPage: function (pageNumber, pageSize) {
//           alert(pageNumber+''+pageSize);
           selectApplyInfo();
        }
    });
}