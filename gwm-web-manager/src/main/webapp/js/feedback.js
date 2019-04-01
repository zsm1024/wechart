var editIndex = undefined;
var total=0;
$(function(){
	$('#list').datagrid({
		singleSelect : "true",
		height:'600',
		striped: true,
		fit: true,
		border:false,
		toolbar:"#tb",
		collapsible:true,
		onClickRow:onClickRow,
		fitColumns:true,
		remoteSort:false,
		columns : [ [ {
						field : 'dostatus',title : '处理',width : 130,align : 'left',
						formatter:function(value,row,index){
							if(row.status==1){
								return'未处理';
							}else if(row.status==2){
								return '已处理';
							}else if(row.status==3){
								return '已关闭'
							}
						},
						editor:{
                            type:'combobox',
	                            options:{
	                            data:[{'id':'1','text':'请选择'},{'id':'2','text':'处理'} ,{'id':'3','text':'关闭'}] ,
                                valueField:'id',
                                textField:'text',
                                required:true,
                                panelHeight:'auto',
                                editable:false
                            }
						}
					},
						{field : 'status',title : '状态',width : 150,align : 'center',
						formatter:function(value,row,index){		          
			        	    if(value==1){
			        	        return '已提交';
			        	    }else if(value==2){
			        	        return '处理中';
			        	    }else{
			        	    	return '已关闭'
			        	    }}},
						{field : 'submit_time',title : '提报时间',width : 80,align : 'center',
			        	formatter:function(value,row,index){
							if(value!=undefined&&value!=''&&value!=null){
								var year=value.substring(0,4);
								var month=value.substring(4,6);
								var day=value.substring(6,8);
								var date=year+'-'+month+'-'+day
								var hour=value.substring(8,10);
								var min=value.substring(10,12);
								var sec=value.substring(12,14);
								var time=hour+':'+min+':'+sec;
								return date+' '+time;
							}
						}}, 
						{field : 'submit_person',title : '提报人',width : 80,align : 'center'}, 
						{field : 'suggest_type',title : '意见类别',width : 90,align : 'center',
						formatter:function(value,row,index){		          
			        	    if(value==1){
			        	        return '产品建议';
			        	    }else if(value==2){
			        	        return '程序错误';
			        	    }else{
			        	        return '其他';
			        	    }}}, 
						{field : 'suggest_cont',title : '详细内容',width : 125,align : 'center'},
						{field : 'contact_type',title : '联系方式',width : 80,align : 'center'}
		] ],
		autoRowHeight:false,
		pagination:true,
		rownumbers:true,
		view : detailview,
		detailFormatter : function(index, row) {
			return '<div style="padding:2px"><table class="ddv"></table></div>';
		},
		onExpandRow : function(index, row) {
			var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
			ddv.datagrid( {
				fitColumns : '',
				singleSelect : true,
				rownumbers : true,
				loadMsg : '',
				height : 'auto',
				columns : [ [ 
					{field : 'status',title : '状态',width : 150,align : 'center',
						formatter:function(value,row,index){		          
						    if(value==1){
						        return '已提交';
						    }else if(value==2){
						        return '处理中';
						    }else{
						    	return '已关闭';
						    }}},
						{field : 'submit_time',title : '提报时间',width : 120,align : 'center',
						formatter:function(value,row,index){
							if(value!=undefined&&value!=''&&value!=null){
								var year=value.substring(0,4);
								var month=value.substring(4,6);
								var day=value.substring(6,8);
								var date=year+'-'+month+'-'+day
								var hour=value.substring(8,10);
								var min=value.substring(10,12);
								var sec=value.substring(12,14);
								var time=hour+':'+min+':'+sec;
								return date+' '+time;
							}
						}}, 
						{field : 'submit_person',title : '提报人',width : 80,align : 'center'}, 
						{field : 'suggest_type',title : '意见类别',width : 90,align : 'center',
						formatter:function(value,row,index){		          
						    if(value==1){
						        return '产品建议';
						    }else if(value==2){
						        return '程序错误';
						    }else{
						        return '其他';
						    }}}, 
						{field : 'suggest_cont',title : '详细内容',width : 125,align : 'center'},
						{field : 'contact_type',title : '联系方式',width : 80,align : 'center'}
						] ],
				onResize : function() {
					$('#list').datagrid('fixDetailRowHeight', index);
				},
				onLoadSuccess : function() {
					setTimeout(function() {
						$('#list').datagrid('fixDetailRowHeight', index);
					}, 0);
				}
				
			});
			selectFBManagerForSub(row.openid,ddv);
			$('#list').datagrid('fixDetailRowHeight', index);
		},
		onBeforeLoad:function(param){
		 	if(param.orderTimeBg){
		 		if(!isDate(param.orderTimeBg)){
		 			$.messager.alert('提示信息','提交日期格式为 yyyy-MM-dd','error');
		 			return false;
		 		}
		 	}
		 	if(param.orderTimeEd){
		 		if(!isDate(param.orderTimeEd)){
		 			$.messager.alert('提示信息','提交日期格式为 yyyy-MM-dd','error');
		 			return false;
		 		}
		 	}
		 	if(param.orderTimeBg && param.orderTimeEd && (param.orderTimeBg > param.orderTimeEd)) {  
				$.messager.alert('提示信息','起始日期要小于或等于截至日期','info'); 
				return false;  
			}
		 } 
    });
	InitPagenation();//分页
	selectFBManager();//初始化数据
	$('#btn_submit').click(function(){
//		alert("提交");
		endEditing();
	});
	$('#btn_channel').click(function(){
		selectFBManager();
		editIndex=undefined;
	});
	$('#btn_exporterExcel').click(function(){
		//执行导入excel
		exproterExcel();
	});
});
//选择行后的编辑处理
function onClickRow(index){
	var rows = $('#list').datagrid('getSelections');
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if(row){
//			if(row.dostatus=="2"){
//				alert("此记录已处理不可修改");
//				return;
//			}
			if(row.dostatus=="3"){
				alert("此记录已关闭不可修改");
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
//更改状态的处理
function endEditing(){
	if (editIndex == undefined){
		return true;
	}
	if ($('#list').datagrid('validateRow', editIndex)){
		var ed = $('#list').datagrid('getEditor', {index:editIndex,field:'dostatus'});
		if(ed!=null){
			var name = $(ed.target).combobox('getText');
			$('#list').datagrid('getRows')[editIndex]['dostatus'] = name;
			$('#list').datagrid('endEdit', editIndex);
			var rows=$('#list').datagrid('getRows');
			if(rows[editIndex].dostatus=='2' || rows[editIndex].dostatus=='3'){
				updateFBManager(rows[editIndex].id,rows[editIndex].dostatus);
			}
			if(rows[editIndex].dostatus=='1'){
				if(rows[editIndex].status=='2'){
					alert("该记录已处理，不能修改为未处理！");
					selectFBManager();
					editIndex = undefined;
					return false;
				}else{
					$('#list').datagrid('refreshRow', editIndex);
				}
//				rows[editIndex].dostatus='2';
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

//function channelEditing(){
//	if(editIndex==undefined)
//		return;
//	selectFBManager();
//	editIndex = undefined;
//}

function exproterExcel(){
	var search_status=$('#search_status').combobox('getValue');//状态
	var search_startdate=$('#search_startdate').datetimebox('getValue');//开始时间
	var search_enddate=$('#search_enddate').datetimebox('getValue');//结束时间
	var url=basePath+"/exportFBManager";
	openPostWindow(url,search_status,search_startdate,search_enddate);
}
function openPostWindow(url,search_status,search_startdate,search_enddate){
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面
    var hideInput1 = document.createElement("input");
    hideInput1.type = "hidden";
    hideInput1.name="search_status"; //后台要接受这个参数来取值
    hideInput1.value = search_status; //后台实际取到的值
    
    var hideInput2 = document.createElement("input");
    hideInput2.type = "hidden";
    hideInput2.name="search_startdate";
    hideInput2.value = search_startdate;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    var hideInput3 = document.createElement("input");
    hideInput3.type = "hidden";
    hideInput3.name="search_enddate";
    hideInput3.value = search_enddate;  //这里就是如果需要第二个参数的时候可以自己再设置
    
    tempForm.appendChild(hideInput1);
    tempForm.appendChild(hideInput2);
    tempForm.appendChild(hideInput3);
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

//重置清空搜索条件
function resetQuery(){
	$('#search_status').combobox('setValue','');
	$('#search_startdate').datetimebox('setValue','');
	$('#search_enddate').datetimebox('setValue','');
}
function selectFBManagerForSub(openid,ddv){
	var url=basePath+"/selectFBManagerForSub";
	$.JsonSRpc(url,{
		openid:openid
	},
	function(data){
		if(data.errcode!="0000"){
			alert(data.errmsg);
			ddv.datagrid('loadData', { total: 0, rows: [] }); 
			return;
		}else{
			ddv.datagrid.cache = null;  
			ddv.datagrid('loadData', data);
		}
	});
}
//查询意见反馈并展示
function selectFBManager(){
	editIndex=undefined;
	var search_status=$('#search_status').combobox('getValue');//状态
	var search_startdate=$('#search_startdate').datetimebox('getValue');//开始时间
	var search_enddate=$('#search_enddate').datetimebox('getValue');//结束时间
	var url=basePath+"/selectFBManager";
	var grid = $('#list');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
	var pagesize = options.pageSize;  
	$.JsonSRpc(url,{
			search_status:search_status,
			search_startdate:search_startdate,
			search_enddate:search_enddate,
			pagenumber:curr,
			pagesize:pagesize
		},
		function(data){
			if(data.errcode!="0000"){
				alert(data.errmsg);
				$('#list').datagrid('loadData', { total: 0, rows: [] }); 
				return;
			}else{
				$("#list").data().datagrid.cache = null;  
				$('#list').datagrid('loadData', data);
			}
		});
}

//更新意见反馈状态
function updateFBManager(idnum,dostatus){
	var id=idnum;//id
	var status=dostatus;//处理
	var url=basePath+"/updateFBManager";
	$.JsonSRpc(url,{
		id:id,
		status:status
		},
		function(data){
			if(data.errcode!="0000"){
				alert(data.errmsg);
				selectFBManager();
				return false;
			}else{
				alert(data.errmsg);
				selectFBManager();
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
        	selectFBManager();
        }
    });
}