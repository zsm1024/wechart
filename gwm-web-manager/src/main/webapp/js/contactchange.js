var editIndex = undefined;
var total=0;
$(function(){
	$('#list').datagrid({
		nowrap:true,
		singleSelect : "true",
		height:'600',
		striped: true,
		fit: true,
		border:false,
		singleSelect : true,
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
								return '已关闭';
							}else if(row.status==4){
								return '完成';
							}
						},
						editor:{
							type:'combobox',
                            options:{
                            data:[{'id':'1','text':'请选择'},{'id':'2','text':'正常受理'} ,{'id':'3','text':'关闭'},{'id':'4','text':'完成'}] ,
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
			        	        return '未受理';
			        	    }else if(value==2){
			        	        return '正常受理';
			        	    }else if(value==3){
			        	    	return '已失效';
			        	    }else{
			        	    	return '完成';
			        	    }}},
						{field : 'tel_reg_num',title : '联系方式变更单号',width : 240,align : 'center'}, 
						{field : 'dtime',title : '申请日期',width : 160,align : 'center',
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
						{field : 'applicationer',title : '申请人',width : 150,align : 'center'}, 
						{field : 'contract_code',title : '合同号',width : 180,align : 'center'},
						{field : 'old_phone',title : '原手机号',width : 140,align : 'center'},
						{field : 'new_phone',title : '变更后手机号',width : 140,align : 'center'}
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
			        	        return '未受理';
			        	    }else if(value==2){
			        	        return '正常受理';
			        	    }else if(value==3){
			        	    	return '已失效'
			        	    }else{
			        	    	return '完成';
			        	    }}},
						{field : 'tel_reg_num',title : '联系方式变更单号',width : 240,align : 'center'}, 
						{field : 'dtime',title : '申请日期',width : 160,align : 'center',
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
						{field : 'applicationer',title : '申请人',width : 150,align : 'center'}, 
						{field : 'contract_code',title : '合同号',width : 180,align : 'center'},
						{field : 'old_phone',title : '原手机号',width : 140,align : 'center'},
						{field : 'new_phone',title : '变更后手机号',width : 140,align : 'center'}
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
			selectContactChangeForSub(row.contract_code,ddv);
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
	selectContactChange();//初始化数据
	$('#btn_submit').click(function(){
//		alert("提交");
		endEditing();
	});
	$('#btn_channel').click(function(){
		selectContactChange();
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
			if(row.dostatus=="4"){
				alert("此记录已经完成不可修改");
				return;
			}
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
	if (editIndex == undefined){return true;}
	if ($('#list').datagrid('validateRow', editIndex)){
		var ed = $('#list').datagrid('getEditor', {index:editIndex,field:'dostatus'});
		if(ed!=null){
			var name = $(ed.target).combobox('getText');
			$('#list').datagrid('getRows')[editIndex]['dostatus'] = name;
			$('#list').datagrid('endEdit', editIndex);
			var rows=$('#list').datagrid('getRows');
			if(rows[editIndex].dostatus=='2' || rows[editIndex].dostatus=='3'||rows[editIndex].dostatus=='4'){
				if(rows[editIndex].dostatus=='2' && rows[editIndex].status!='1'){
					alert("当前记录状态不允许进行受理操作");
					selectContactChange();
				}else if(rows[editIndex].dostatus=='3' && rows[editIndex].status!='1'){
					alert("当前记录状态不允许进行关闭操作");
					selectContactChange();
				}else if(rows[editIndex].dostatus=='4' && rows[editIndex].status!='2'){
					alert("当前记录状态不允许进行完成操作");
					selectContactChange();
				}else{
					updateContactChange(rows[editIndex].tel_reg_num,rows[editIndex].dostatus);
				}
			}else if(rows[editIndex].dostatus=='1'){
				if(rows[editIndex].status=="2"){
					alert("当前记录状态不允许修改为未处理");
				}
				$('#list').datagrid('refreshRow', editIndex);
				selectContactChange();
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


function exproterExcel(){
	var applicationer=$('#applicationer').val();//申请人
	var contract_code=$('#contract_code').val();//合同号
	var search_status=$('#search_status').combobox('getValue');//状态
	var search_startdate=$('#search_startdate').datetimebox('getValue');//开始时间
	var search_enddate=$('#search_enddate').datetimebox('getValue');//结束时间
	var url=basePath+"/exportContactChange";
	openPostWindow(url,search_status,search_startdate,search_enddate,applicationer,contract_code);
}
function openPostWindow(url,search_status,search_startdate,search_enddate,applicationer,contract_code){
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
    hideInput2.value = search_startdate;  
    
    var hideInput3 = document.createElement("input");
    hideInput3.type = "hidden";
    hideInput3.name="search_enddate";
    hideInput3.value = search_enddate; 
    
    var hideInput4 = document.createElement("input");
    hideInput3.type = "hidden";
    hideInput3.name="applicationer";
    hideInput3.value = applicationer; 
    
    var hideInput5 = document.createElement("input");
    hideInput3.type = "hidden";
    hideInput3.name="contract_code";
    hideInput3.value = contract_code;  
    
    tempForm.appendChild(hideInput1);
    tempForm.appendChild(hideInput2);
    tempForm.appendChild(hideInput3);
    tempForm.appendChild(hideInput4);
    tempForm.appendChild(hideInput5);
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
	$('#applicationer').val("");
	$('#contract_code').val("");
	$('#search_status').combobox('setValue','1');
	$('#search_startdate').datetimebox('setValue','');
	$('#search_enddate').datetimebox('setValue','');
}

function selectContactChangeForSub(contract_code,ddv){
	var url=basePath+"/selectContactChangeForSub";
	$.JsonSRpc(url,{
		contract_code:contract_code
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
//查询联系方式变更表并展示
function selectContactChange(){
	editIndex=undefined;
	var applicationer=$('#applicationer').val();//申请人
	var contract_code=$('#contract_code').val();//合同号
	var search_status=$('#search_status').combobox('getValue');//状态
	var search_startdate=$('#search_startdate').datetimebox('getValue');//开始时间
	var search_enddate=$('#search_enddate').datetimebox('getValue');//结束时间
	var url=basePath+"/selectContactChange";
	var grid = $('#list');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
	var pagesize = options.pageSize;  
	$.JsonSRpc(url,{
			applicationer:applicationer,
			contract_code:contract_code,
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
function updateContactChange(num,dostatus){
	var tel_reg_num=num;//id
	var status=dostatus;//处理
	var url=basePath+"/updateContactChange";
	$.JsonSRpc(url,{
		tel_reg_num:tel_reg_num,
		status:status,
		userid:userid
		},
		function(data){
			if(data.errcode!="0000"){
				alert(data.errmsg);
				selectContactChange();
				return false;
			}else{
				alert(data.errmsg);
				selectContactChange();
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
        	selectContactChange();
        }
    });
}