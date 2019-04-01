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
<script type="text/javascript" src="./js/msgcontent.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'center' ">
		<table id="list" ></table>
		<div id="tb" style="height: auto">
			<a  class="easyui-linkbutton" id="btn_newmsg"
				data-options="iconCls:'icon-add',plain:true" onclick="addWindow()">新建消息</a>
		</div>
	</div>
	<div id="addWindow" class="easyui-window" title="新增消息" style="width:300px;height: 560"
			data-options="closed:true,minimizable:false,maximizable:false,collapsible:false,shadow: false,modal:true,resizable:false">
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">消息类型</td>
					<td class="queryContent">
						<select id="add_msg_type" class="easyui-combobox" style="width:200px;">   
						    <option value="1">个人类消息</option>   
						    <option value="2">通知类消息</option>   
						</select>  
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">绑定状态</td>
					<td class="queryContent">
						<select id="add_binding_state" class="easyui-combobox" style="width:200px;">   
						    <option value="1">未绑定</option>   
						    <option value="2">绑定未结清</option>   
						    <option value="3">绑定已结清</option>   
						</select>  
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">消息内容</td>
					<td class="queryContent">
						<input id="add_content" class="easyui-textbox" data-options="multiline:true" style="width:200px;height:70px">
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">链接地址</td>
					<td class="queryContent">
						<input id="add_address" class="easyui-textbox" data-options="multiline:false" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="queryTitle">消息状态</td>
					<td class="queryContent">
						<input id="add_qiyong" value="1" name="add_msg_state" type="radio">启用
						<input id="add_tingyong" value="0" name="add_msg_state" type="radio">停用
						
					</td>
				</tr>
				<tr>
					<td  colspan="2" style="text-align: center">
						<a class="easyui-linkbutton" id="add_btn_ok">确定</a> 
						<a class="easyui-linkbutton" id="add_btn_cancel">取消</a>
					</td>
				</tr>
			</table>
	</div>
	<div id="editWindow" class="easyui-window" title="编辑消息" style="width:300px;height: 560"
			data-options="closed:true,minimizable:false,maximizable:false,collapsible:false,shadow: false,modal:true,resizable:false">
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">消息类型</td>
					<td class="queryContent">
						<select id="edit_msg_type" class="easyui-combobox" style="width:200px;">   
						    <option value="1">个人类消息</option>   
						    <option value="2">通知类消息</option>   
						</select>  
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">绑定状态</td>
					<td class="queryContent">
						<select id="edit_binding_state" class="easyui-combobox" style="width:200px;">   
						    <option value="1">未绑定</option>   
						    <option value="2">绑定未结清</option>   
						    <option value="3">绑定已结清</option>   
						</select>  
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">消息内容</td>
					<td class="queryContent">
						<input id="edit_content" class="easyui-textbox" data-options="multiline:true" style="width:200px;height:70px">
					</td>
				</tr>
				<tr>
					<td class="queryTitle" width="80px">链接地址</td>
					<td class="queryContent">
						<input id="edit_address" class="easyui-textbox" data-options="multiline:false" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="queryTitle">消息状态</td>
					<td class="queryContent">
						<input name="edit_msg_state" value="1" id="edit_qiyong" type="radio">启用
						<input name="edit_msg_state" value="0" id="edit_tingyong" type="radio">停用
					</td>
				</tr>
				<tr>
					<td  colspan="2" style="text-align: center">
						<a class="easyui-linkbutton" id="edit_btn_ok">确定</a> 
						<a class="easyui-linkbutton" id="edit_btn_cancel">取消</a>
					</td>
				</tr>
			</table>
	</div>
</body>
</html>