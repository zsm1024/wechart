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
<script type="text/javascript" src="./js/contactchange.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',title:'查询条件'" style="height: 143px">
		<form>
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">状态</td>
					<td class="queryContent"><input type="hidden" id="status" /> <select
						id="search_status" class="easyui-combobox" style="width: 150px"
						data-options="panelHeight:'auto'">
							<option value="0">全部</option>
							<option value="1">未受理</option>
							<option value="2">已正常受理</option>
							<option value="3">已失效</option>
							<option value="4">完成</option>
					</select></td>
					<td class="queryTitle" width="80px">申请人</td>
					<td class="queryContent"><input class="inputText" type="text"
						id="applicationer" /></td>
					<td class="queryTitle" width="80px">合同号</td>
					<td class="queryContent"><input class="inputText" type="text"
						id="contract_code" /></td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">申请日期</td>
					<td class="queryContent" colspan="5"><input
						class="easyui-datetimebox" style="width: 125px"  data-options="editable:false"
						id="search_startdate" value="">&nbsp;到&nbsp; <input
						class="easyui-datetimebox" style="width: 125px"  data-options="editable:false"
						id="search_enddate" value=""></td>
				</tr>
				<tr>
		            <td class="queryBtnTd" colspan="6">
		                <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="selectContactChange()">查询</a>
		                &nbsp;&nbsp;
		                <a href="javascript:void(0);" onclick="resetQuery()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
		            </td>
		        </tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center' ">
		<table id="list"></table>
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