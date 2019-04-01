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
<script type="text/javascript" src="./js/hotcar_list.js"></script>
</head>
<body class="easyui-layout">  
<div data-options="region:'north',title:'导入/导出'" style="height: 80px">
			<table class="queryTable" style="width: 100%">
				<tr>
					<td class="queryTitle" width="80px">导入</td>
					<td class="queryContent">
						<form method="POST" enctype="multipart/form-data" target="nm_iframe"  action="/gwm-web-manager/importExcel">  
   						<input type="file" name="file">
    					<input type="submit" value="导入"> 
						</form> 
					</td>
				</tr>
			</table>
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
							toolbar:'#tb'">
			<thead>
				<tr>
					<th data-options="field:'id',width:60,align:'center'">编号</th>
					<th data-options="field:'brand',width:60,align:'center'">品牌</th>
					<th data-options="field:'models',width:100,align:'center'">车型</th>
					<th data-options="field:'configure',width:200,align:'center'">配置</th>
					<th data-options="field:'price',width:70,align:'right'">车价</th>
					<th data-options="field:'amount_product',width:80,align:'center'">金融产品</th>
					<th data-options="field:'purchase',width:60,align:'right'">销量</th>
					<th data-options="field:'pic',width:120,align:'center'">图片</th>
					<th data-options="field:'rate',width:60,align:'right'">比例</th>
					<th data-options="field:'loanterm',width:60,align:'center'">贷款期限</th>
					<th data-options="field:'minloanprice',width:80,align:'right'">最小贷款额</th>
					<th data-options="field:'maxloanprice',width:60,align:'right'">最大贷款额</th>
					
				</tr>
			</thead>
		</table>
		<div id="tb" style="height: auto">
			<a href="#"class="easyui-linkbutton" id="btn_select" onclick="selectHotCarInfo()"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
			<a href="#"class="easyui-linkbutton" id="btn_exporterExcel"
				data-options="iconCls:'icon-print',plain:true">导出excel</a>
		</div>
	</div>
	<iframe id="id_iframe" name="nm_iframe" style="display:none;">
	<script>
	function selectHotCarInfo(){
		var url=basePath+"/selectHotCarInfo";
		var grid = $('#list');  
		var options = grid.datagrid('getPager').data("pagination").options;  
		var curr = options.pageNumber;  
		var pagesize = options.pageSize;  
		$.JsonSRpc(url,{
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
	</script>
	</iframe>
</body> 
</html>