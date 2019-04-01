var editIndex = undefined;
var total=0;
var editId="";
$(function(){
	$('#list').datagrid({
		nowrap: true,
        autoRowHeight: false,
        striped: true,
        singleSelect: true,
        border: false,
        collapsible: true,
        fitColumns: false,
        rownumbers: true,
        pageSize: 10, //每页显示的记录条数，默认为10
        onSelectPage: function (pageNumber, pageSize) {
        	selectMsgContent();
        },
        fit: true,
        pagination: true,
		toolbar:'#tb',
		url:basePath+"/selectMsgContent",
		columns:[[    
		          {field:'dostatus',title:'处理',width:120,align:'center',formatter:function(value,rowData,rowIndex){
		    	  		var html = [];
		    	  		html.push("&nbsp;<img class='op-enable' src='","img/edit.jpg' onClick='editWindow("+rowIndex+")' title='编辑'/>");
		    	  		html.push("&nbsp;<img class='op-enable' src='","img/delete.jpg' onClick='confirm1("+rowIndex+")' title='删除'/>");
		    	  		return html.join('');
		          }},    
		          {field:'msg_type',title:'消息类型',width:160,align:'center',formatter:function(value,row,index){		          
			      	    if(value==1){
			      	    	return '个人类消息';
			      	    }else if(value==2){
			      	    	return '通知类消息';
			      	    }
			      }},    
		          {field:'binding_state',title:'绑定状态',width:140,align:'center',formatter:function(value,row,index){		          
			      	    if(value==1){
			    	        return '未绑定';
			    	    }else if(value==2){
			    	        return '绑定未结清';
			    	    }else if(value==3){
			    	    	return '绑定已结清';
			    	    }
		      	  }},
		      	  {field:'msg_content',title:'消息内容',width:320,align:'center'},
		      	  {field:'link_address',title:'链接地址',width:210,align:'center'},
			      {field:'msg_state',title:'消息状态',width:140,align:'center',formatter:function(value,row,index){		          
			      	    if(value==0){
			    	        return '未启用';
			    	    }else if(value==1){
			    	        return '启用';
			    	    }
		      	  }}
		      ]],
	});
	
//	$('#btn_submit').click(function(){
//		alert("提交");
//		endEditing();
//	});
//	$('#btn_channel').click(function(){
//		selectFBManager();
//		editIndex=undefined;
//	});
//	$('#btn_exporterExcel').click(function(){
//		//执行导入excel
//		exproterExcel();
//	});
	
	//新增弹窗界面确定按钮
	$("#add_btn_ok").click(function(){
		addSwitchCase();
	});
	//新增弹窗界面取消按钮
	$("#add_btn_cancel").click(function(){
		$('#addWindow').window('close');
	});
	//编辑弹窗界面确定按钮
	$("#edit_btn_ok").click(function(){
		editSwitchCase();
	});
	//编辑弹窗界面取消按钮
	$("#edit_btn_cancel").click(function(){
		$('#editWindow').window('close');
	});
});
function addWindow(){
	//打开前默认为首选项
	$("#add_msg_type").combobox("setValue","1");
	$("#add_binding_state").combobox("setValue","1");
	$("#add_content").textbox("setValue","");
	$("#add_address").textbox("setValue","");
	$("#add_qiyong").click();
	$('#addWindow').window('open');
}
function removeProduct(rowIndex){
	var rows=$("#list").datagrid("getRows");
	var row=rows[rowIndex];
	var id=row.id;
	if(id==undefined || id==null|| id==""){
		alert("参数获取失败,请刷新重试");
		return false;
	}
	var url=basePath+"/delMsgContent";
//	alert(msg_type+","+binding_state+","+msg_content+","+link_address+","+msg_state);
	$.JsonSRpc(url,{
		id:id
	},
	function(data){
		if(data.errcode!="0000"){
			alert(data.errmsg);
			return;
		}else{
			alert(data.errmsg);
			selectMsgContent();
		}
	});
}

function editWindow(rowIndex){
	var rows=$("#list").datagrid("getRows");
	var row=rows[rowIndex];
	var msg_type=row.msg_type;
	var binding_state=row.binding_state;
	var content=row.msg_content;
	var address=row.link_address;
	var qiyong=row.msg_state;
	editId=row.id;
	$("#edit_msg_type").combobox("setValue",msg_type);
	$("#edit_binding_state").combobox("setValue",binding_state);
	$("#edit_content").textbox("setValue",content);
	$("#edit_address").textbox("setValue",address);
	if(qiyong=="1"){
		$("#edit_qiyong").click();
	}else{
		$("#edit_tingyong").click();
	}
	$('#editWindow').window('open');
}
//维护提交
function editSwitchCase(){
	var msg_type=$('#edit_msg_type').combobox('getValue');//状态
	var binding_state=$('#edit_binding_state').combobox('getValue');//状态
	var msg_content=$("#edit_content").val();
	var link_address=$("#edit_address").val();//链接地址可以为空
	var msg_state=getstatus("edit");//单选框用name获取
	if(msg_type==null || msg_type==""){
		alert("请选择消息类型");
		return false;
	}
	if(binding_state==null || binding_state==""){
		alert("请选择绑定状态");
		return false;
	}
	if(msg_content==null || msg_content==""){
		alert("请输入消息内容");
		return false;
	}
//	if(link_address==null || link_address==""){
//		alert("请选择消息类型");
//		return false;
//	}
	if(getstatus==undefined ||msg_state==null || msg_state==""){
		alert("请选择消息状态");
		return false;
	}
	if(editId==undefined||editId==null||editId==""){
		alert("参数错误，请重新选择");
		return false;
	}
	var url=basePath+"/updateMsgContent";
//	alert(msg_type+","+binding_state+","+msg_content+","+link_address+","+msg_state);
	$.JsonSRpc(url,{
		msg_type:msg_type,
		binding_state:binding_state,
		msg_content:encodeURI(msg_content),
		link_address:link_address,
		msg_state:msg_state,
		id:editId
	},
	function(data){
		if(data.errcode!="0000"){
			alert(data.errmsg);
			return;
		}else{
			$('#editWindow').window('close');
			alert(data.errmsg);
			selectMsgContent();
		}
	});
}
//新增提交
function addSwitchCase(){
	var msg_type=$('#add_msg_type').combobox('getValue');//状态
	var binding_state=$('#add_binding_state').combobox('getValue');//状态
	var msg_content=$("#add_content").val();
	var link_address=$("#add_address").val();//链接地址可以为空
	var msg_state=getstatus("add");//单选框用name获取
	if(msg_type==null || msg_type==""){
		alert("请选择消息类型");
		return false;
	}
	if(binding_state==null || binding_state==""){
		alert("请选择绑定状态");
		return false;
	}
	if(msg_content==null || msg_content==""){
		alert("请输入消息内容");
		return false;
	}
//	if(link_address==null || link_address==""){
//		alert("请选择消息类型");
//		return false;
//	}
	if(getstatus==undefined ||msg_state==null || msg_state==""){
		alert("请选择消息状态");
		return false;
	}
	var url=basePath+"/addMsgContent";
//	alert(msg_type+","+binding_state+","+msg_content+","+link_address+","+msg_state);
	$.JsonSRpc(url,{
		msg_type:msg_type,
		binding_state:binding_state,
		msg_content:encodeURI(msg_content),
		link_address:link_address,
		msg_state:msg_state
	},
	function(data){
		if(data.errcode!="0000"){
			alert(data.errmsg);
			$('#list').datagrid('loadData', { total: 0, rows: [] }); 
			return;
		}else{
			alert(data.errmsg);
			$('#addWindow').window('close');
			selectMsgContent();
		}
	});
}

function getstatus(type)
{
	if(type=="add"){
		var temp = document.getElementsByName("add_msg_state");
		  for(var i=0;i<temp.length;i++)
		  {
		     if(temp[i].checked){
		    	 var msg_state = temp[i].value;
		         return msg_state; 
		     }
		  }
	}
	if(type=="edit"){
		var temp = document.getElementsByName("edit_msg_state");
		  for(var i=0;i<temp.length;i++)
		  {
		     if(temp[i].checked){
		    	 var msg_state = temp[i].value;
		         return msg_state; 
		     }
		  }
	}
} 
//查询意见反馈并展示
function selectMsgContent(){
	editIndex=undefined;
	var url=basePath+"/selectMsgContent";
	var grid = $('#list');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
	var pagesize = options.pageSize;  
	$.JsonSRpc(url,{
			page:curr,
			rows:pagesize
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
				alert(data[0].errmsg);
				return false;
			}else{
				alert(data.errmsg);
				selectFBManager();
				return true;
			}
	});
}

function confirm1(index){
	$.messager.confirm('删除确认', '您确定要删除吗?', function(r){
		if (r){
			alert('confirmed: '+r);
			removeProduct(index);
		}
	});
}