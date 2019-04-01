<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    	String path = request.getContextPath();
    %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="head.jsp"></jsp:include>
<script type="text/javascript" src="./js/global.js"></script>
<script type="text/javascript" src="./js/url.js"></script>
<script type="text/javascript" src="./js/check.js"></script>
<script type="text/javascript" src="./js/loan_applylist.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',title:'查询条件'" style="height: 145px">
		<form>
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">状态</td>
					<td class="queryContent"><input type="hidden" id="status" /> <select
						id="com_status" class="easyui-combobox" style="width: 150px"
						data-options="panelHeight:'auto'">
							<option value="">请选择</option>
					</select></td>
					<td class="queryTitle" width="80px">来源</td>
					<td class="queryContent"><input type="hidden" id="source" /> <select
						id="com_source" class="easyui-combobox" style="width: 150px"
						data-options="panelHeight:'auto'">
							<option value="">请选择</option>
					</select></td>
					<td class="queryTitle" width="80px">姓名</td>
					<td class="queryContent"><input class="inputText" type="text"
						id="txt_name" /></td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">手机号</td>
					<td class="queryContent"><input class="inputText" type="text"
						id="txt_phone" /></td>
					<td class="queryTitle" width="120px">申请时间</td>
					<td class="queryContent" colspan="5"><input
						class="easyui-datetimebox" style="width: 125px" editable="false"
						id="start_date" value="">&nbsp;到&nbsp; <input
						class="easyui-datetimebox" style="width: 125px" editable="false"
						id="end_date" value=""></td>
				</tr>
				<tr>
            <td class="queryBtnTd" colspan="6">
                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="selectApplyInfo()">查询</a>
                &nbsp;&nbsp;
                <a href="javascript:void(0);" id="searchMember" onclick="resetQuery()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
            </td>
        </tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center' ">
		<table id="list" class="easyui-datagrid"
			data-options="  nowrap: true,
					        autoRowHeight: false,
					        striped: true,
					        singleSelect: true,
					        border: false,
					        collapsible: true,
					        fitColumns: false,
					        rownumbers: true,
					        pageSize: 10,
					        fit: true,
					        pagination: true,
							onClickRow: onClickRow,
							toolbar:'#tb'">
								
			<thead>
				<tr>
					<th data-options="field:'dostatus',width:60,align:'center',formatter:function(value,row,index){
							if(value==1){
								return'未处理';
							}else if(value==2){
								return '已处理';
							}
						},
						editor:{
                            type:'combobox',
                            
	                            options:{
	                            data:[{'id':'1','text':'请选择'},{'id':'2','text':'正常受理'}] ,
                                valueField:'id',
                                textField:'text',
                                required:true,
                                panelHeight:'auto'
                            }
					}">处理</th>
					<th data-options="field:'status',width:60,align:'center',formatter:function(value,row,index){		          
		        	    if(value==1){
		        	        return '已提交';
		        	    }else if(value==2){
		        	        return '已受理';
		        	    }}">状态</th>
					<th data-options="field:'application_num',width:180">申请单号</th>
					<th data-options="field:'time',width:125,formatter:function(value,row,index){
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
					}">申请时间</th>
					<th data-options="field:'name',width:60,align:'center'">姓名</th>
					<th data-options="field:'sex',width:30,align:'center',formatter:function(value,row,index){		          
		        	    if(value==1){
		        	        return '男';
		        	    }else{
		        	        return '女';
		        	    }}">称呼</th>
					<th data-options="field:'phone',width:80,align:'center'">手机号</th>
					<th data-options="field:'card_id',width:120">身份证号</th>
					<th data-options="field:'brand',width:30,align:'center'">品牌</th>
					<th data-options="field:'model',width:80,align:'center'">车型</th>
					<th data-options="field:'first_amt',width:60,align:'center'">首付</th>
					<th data-options="field:'province',width:60,align:'center'">省份</th>
					<th data-options="field:'city',width:60,align:'center'">城市</th>
					<th data-options="field:'franchiser',width:180,align:'center'">经销商</th>
					<th data-options="field:'source',width:40,align:'center',formatter:function(value,row,index){		          
		        	    if(value==1){
		        	        return '官网';
		        	    }else if(value==2){
		        	        return '微信';
		        	    }else if(value==3){
		        	    	return '哈弗商城';
		        	    }else if(value==4){
		        	    	return '品牌商城';
		        	    }}">来源</th>
					
				</tr>
			</thead>
		</table>
		<div id="tb" style="height: auto">
			<a href="#" class="easyui-linkbutton" id="btn_submit"
				data-options="iconCls:'icon-save',plain:true">提交</a>
			<a href="#" class="easyui-linkbutton" id="btn_channel"
				data-options="iconCls:'icon-cancel',plain:true">取消</a>
			<a href="#"class="easyui-linkbutton" id="btn_exporterExcel"
				data-options="iconCls:'icon-print',plain:true">导出excel</a>
		</div>
	</div>
</body>
</html>