<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    	String path = request.getContextPath();
    %>
<!DOCTYPE html>
<html>
<head>
<title>提前还款管理</title>
<jsp:include page="head.jsp"></jsp:include>
<style type="text/css">
	.tree-folder-open {
    	background:url();
	}
	.tree-folder {
    	background:url();
	}
	.tree-file {
    	background:url();
	}
</style>
<script type="text/javascript" src="./js/url.js"></script>
<script type="text/javascript" src="./js/check.js"></script>
<script type="text/javascript" src="js/earlyrepay.js"></script>
<script type="text/javascript" src="./js/global.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',title:'查询条件'" style="height: 140px">
		<form id="queryForm">
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">状态</td>
					<td class="queryContent">
						<select id="status" class="easyui-combobox"
							style="width: 150px" data-options="panelHeight:'auto',editable:false">
							<option value="-1">请选择</option>
							<option value="1">已提交</option>
							<option value="2">正常受理</option>
							<option value="3">附条件受理</option>
							<option value="4">关闭</option>
							<option value="5">已结清</option>
					</select></td>
					<td class="queryTitle" width="80px">申请人</td>
					<td class="queryContent">
						<input class="inputText" type="text" id="applicationer" />
					</td>
					<td class="queryTitle" width="80px">合同号</td>
					<td class="queryContent"><input class="inputText" type="text" id="contract_code" /></td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">还款方式</td>
					<td class="queryContent">
						<select id="repay_type" class="easyui-combobox"
							style="width: 150px" data-options="panelHeight:'auto',editable:false">
							<option value="-1">请选择</option>
							<option value="1">预留银行卡还款</option>
							<option value="2">对公账户还款</option>
						</select>
					</td>
					<td class="queryTitle" width="80px">提前还款日</td>
					<td class="queryContent"><input
						class="easyui-datebox" style="width: 125px" editable="false"
						id="apply_repay_date" value=""></td>
					<td class="queryTitle" width="80px">申请时间</td>
					<td class="queryContent" colspan="5"><input
						class="easyui-datetimebox" style="width: 125px" editable="false"
						id="start_apply_date" value="">&nbsp;到&nbsp; <input
						class="easyui-datetimebox" style="width: 125px" editable="false"
						id="end_apply_date" value=""></td>
				</tr>
				<tr>
					<td class="queryBtnTd" colspan="6"><a
						href="javascript:void(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="selectApplyInfo()">查询</a>
						&nbsp;&nbsp; 
						<a href="javascript:void(0);" id="searchMember"
						onclick="resetQuery()" class="easyui-linkbutton"
						data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center' ">
		<table id="list"></table>
		<div id="tb" style="height: auto">
			<a href="#" class="easyui-linkbutton" id="btn_submit"
				data-options="iconCls:'icon-save',plain:true">提交</a> <a href="#"
				class="easyui-linkbutton" id="btn_channel"
				data-options="iconCls:'icon-cancel',plain:true">取消</a> <a href="#"
				class="easyui-linkbutton" id="btn_exporterExcel"
				data-options="iconCls:'icon-print',plain:true">导出excel</a>
		</div>
	</div>
</body>
</html>