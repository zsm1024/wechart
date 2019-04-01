var editIndex = undefined;
var commonObj = {};
$(function(){
	$('#list').datagrid({
		nowrap:true,
		lines:true,
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
		columns : [ [  
		              {
						field : 'dostatus',title : '处理',width : 130,align : 'left',
						formatter:function(value,row,index){
							var v = row.status;
							if(v==1){
								return'请选择';
							}else if(v==2){
								return '正常受理';
							}else if(v==3){
								return '附条件受理';
							}else if(v==4){
								return '关闭';
							}else if(v==5){
								return '已结清';
							}
						},
						editor:{
							type:'combobox',
                            options:{
                            	 data:[{'id':'1', 'text':'请选择'},
   	                            	{'id':'2', 'text':'正常受理'},
   	                            	{'id':'3', 'text':'附条件受理'},
   	                            	{'id':'4', 'text':'关闭'},
   	                            	{'id':'5', 'text':'已结清'}] ,
                                   valueField:'id',
                                   textField:'text',
                                   required:true,
                                   panelHeight:'auto'
                            }
						}
					},
						{field : 'status',title : '状态',width : 150,align : 'center',
						formatter:function(value,row,index){		          
							if(value==1){
			        	        return '已提交';
			        	    }else if(value==2){
			        	        return '正常受理';
			        	    }else if(value==3){
			        	        return '附条件受理';
			        	    }else if(value==4){
			        	        return '关闭';
			        	    }else if(value==5){
			        	        return '已结清';
			        	    }}},
						{field : 'application_num',title : '提前还款申请单编号',width : 240,align : 'center'}, 
						{field : 'apply_date_time',title : '申请时间',width : 160,align : 'center',
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
						{field : 'contract_code',title : '合同号',width : 150,align : 'center'}, 
						{field : 'applicationer',title : '申请人',width : 150,align : 'center'}, 
						{field : 'apply_repay_date',title : '提前还款日',width : 120,align : 'center',
							formatter:function(value){
								value = value+'';
								if(value&&value.length==8){
									var y = value.substring(0, 4);
									var m = value.substring(4, 6);
									var d = value.substring(6, 8);
									return '<b>'+y+'-'+m+'-'+d+'</b>';
								}
								return value;
							}},
						{field : 'total_amt',title : '提前还款金额',width : 120,align : 'center'},
						{field : 'repay_type',title : '还款方式',width : 120,align : 'center',
							formatter:function(value){
							if(value=='1'){
								return '预留银行卡还款';
							}else if(value=='2'){
								return '对公账户还款';
							}else{
								return value;
							}
						}}
//						{field : 'repay_voucher',title : '还款凭证',width : 140,align : 'center',
//							formatter:function(value){
//							if(value&&value.length>0){
//								return '<a target=\'_blank\' href=\''+value+'\'>查看</a>';
//							}
//							return value;
//						}}
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
	        return '正常受理';
	    }else if(value==3){
	        return '附条件受理';
	    }else if(value==4){
	        return '关闭';
	    }else if(value==5){
	        return '已结清';
	    }}},
	{field : 'application_num',title : '提前还款申请单编号',width : 240,align : 'center'}, 
	{field : 'apply_date_time',title : '申请时间',width : 160,align : 'center',
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
	{field : 'contract_code',title : '合同号',width : 150,align : 'center'}, 
	{field : 'applicationer',title : '申请人',width : 150,align : 'center'}, 
	{field : 'apply_repay_date',title : '提前还款日',width : 120,align : 'center',
		formatter:function(value){
			value = value+'';
			if(value&&value.length==8){
				var y = value.substring(0, 4);
				var m = value.substring(4, 6);
				var d = value.substring(6, 8);
				return '<b>'+y+'-'+m+'-'+d+'</b>';
			}
			return value;
		}},
	{field : 'total_amt',title : '提前还款金额',width : 120,align : 'center'},
	{field : 'repay_type',title : '还款方式',width : 120,align : 'center',
		formatter:function(value){
		if(value=='1'){
			return '预留银行卡还款';
		}else if(value=='2'){
			return '对公账户还款';
		}else{
			return value;
		}
	}}
//	{field : 'repay_voucher',title : '还款凭证',width : 140,align : 'center',
//		formatter:function(value){
//		if(value&&value.length>0){
//			return '<a target=\'_blank\' href=\''+value+'\'>查看</a>';
//		}
//		return value;
//	}}
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
			queryInfoForSub(row.contract_code,ddv);
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
	//设置分页控件 
    var p = $('#list').datagrid('getPager');
    $(p).pagination({
        pageSize: 10, //每页显示的记录条数，默认为10 
        onSelectPage: function (pageNumber, pageSize) {
        	queryInfo();
        }
    });
    queryInfo();
    $("#btn_channel").click(function(){
    	cancelClick();
    });
    
    $("#btn_submit").click(function(){
    	submitClick();
    });
    $('#btn_exporterExcel').click(function(){
		//执行导入excel
		exproterExcel();
	});
});

//导出excel
function exproterExcel(){
	var obj = commonObj;
	delete commonObj.pagesize;
	delete commonObj.pagenumber;
	for(var i in obj){
		console.info(i+":"+obj[i]);
	}
	var url=basePath+"/exportearlyrepayinfo";
	openPostWindow(url,obj);
}
//下载excel
function openPostWindow(url,obj){
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面
    for(var i in obj){
    	var hideInput = document.createElement("input");
    	hideInput.type = "hidden";
    	hideInput.name = i;
    	hideInput.value = obj[i];
    	tempForm.appendChild(hideInput);
    }
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

//取消编辑
function cancelClick(){
	if (editIndex != undefined){
		$('#list').datagrid('cancelEdit', editIndex);
		 queryInfo();
		editIndex = undefined;
	}
}
//提交编辑
function submitClick(){
	if (editIndex != undefined){
		var t = $('#list');
		t.datagrid('endEdit', editIndex);
		editIndex = undefined;
		var rows = t.datagrid('getSelected');
		var dostatus = rows.dostatus;
		console.info(dostatus);
		if(dostatus && dostatus!="-1"){
			var obj = {};
			obj.dostatus = dostatus;
			obj.application_num = rows.application_num;
			$.JsonSRpc(baseUrl+"/mdealrepayinfo", obj, function(data){
				if(data.errcode=="9999"){
					alert(data.errmsg);
				}
				console.info(JSON.stringify(data));
				queryInfo();
		    });
		}else{
			queryInfo();
		}
	}
}
//点击数据
function onClickRow(index){
	if (editIndex != index){
		if (endEditing()){
			var row = $('#list').datagrid('getSelected');
			if(row.status!="4"&&row.status!="5"){
				editIndex = index;
				$("#list").datagrid('beginEdit', editIndex);
			}
		}
	}
}
//判断是否在编辑中
function endEditing(){
	if (editIndex == undefined){return true;}
	$('#list').datagrid('select', editIndex);
	return false;
}
//重置查询条件
function resetQuery(){
	commonObj = {};
	queryInfo();
	$("#status").combobox("setValue", "-1");
	$("#repay_type").combobox("setValue", "-1");
	$("#applicationer").val("");
	$("#contract_code").val("");
	$("#apply_repay_date").datebox("setValue", "");
    $("#start_apply_date").datebox("setValue", "");
    $("#end_apply_date").datebox("setValue", "");
}
function queryInfoForSub(contract_code,ddv){
	var obj={};
	obj.contract_code=contract_code;
	$.JsonSRpc(baseUrl+"/earlyrepayinfoForSub", obj, function(data){
		if(data.errcode&&"0"==data.errcode){
			ddv.datagrid.cache = null;  
			ddv.datagrid('loadData', data);
			editIndex = undefined;
		}else{
			ddv.datagrid('loadData', { total: 0, rows: [] }); 
			alert(data.errmsg);
			return;
		}
    });
}
function queryInfo(){
	var options = $("#list").datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
	var pagesize = options.pageSize;
	var obj = commonObj;
	obj.pagenumber = curr;
	obj.pagesize = pagesize;
	$.JsonSRpc(baseUrl+"/mgetrepayinfo", obj, function(data){
		if(data.errcode&&"0"==data.errcode){
			$("#list").datagrid("loadData", data);
			editIndex = undefined;
		}else{
			alert(data.errmsg);
		}
    });
}
//查询
function selectApplyInfo(){
	var obj = {};
	var status = $("#status").combobox("getValue");
	var applicationer = $("#applicationer").val();
	var contract_code = $("#contract_code").val();
	var repay_type = $("#repay_type").combobox("getValue");
	var apply_repay_date = $("#apply_repay_date").datebox("getValue");
    var start_apply_date = $("#start_apply_date").datetimebox("getValue");
    var end_apply_date = $("#end_apply_date").datetimebox("getValue");
    if(status&&status!="-1"){
    	obj.status = status;
    }
    if(applicationer&&applicationer!=""){
    	obj.applicationer = applicationer;
    }
    if(contract_code&&contract_code!=""){
    	obj.contract_code = contract_code;
    }
    if(repay_type&&repay_type != "-1"){
    	obj.repay_type = repay_type;
    }
    if(apply_repay_date&&apply_repay_date!=""){
    	obj.apply_repay_date = apply_repay_date;
    }
    if(start_apply_date&&start_apply_date != ""){
    	obj.start_apply_date = start_apply_date;
    }
    if(end_apply_date&&end_apply_date != ""){
    	obj.end_apply_date = end_apply_date;
    }
    commonObj = obj;
    queryInfo();
}