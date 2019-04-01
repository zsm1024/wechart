var editIndex = undefined;
var total=0;
$(function(){
	InitPagenation();

	selectHotCarInfo();
	$('#btn_exporterExcel').click(function(){
		//执行导入excel
		exproterExcel();
	});
});



function openPostWindow(url){
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面
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
	var url=basePath+"/exportHotCarInfo";
	openPostWindow(url);
}


function selectHotCarInfo(){
	var url=basePath+"/selectHotCarInfo";
	var grid = $('#list');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var curr = options.pageNumber<1?1:options.pageNumber;  
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

function InitPagenation() {
    //设置分页控件 
    var p = $('#list').datagrid('getPager');
    $(p).pagination({
        pageSize: 10, //每页显示的记录条数，默认为10 
//        pageList: [10, 20, 30], //可以设置每页记录条数的列表 
        onSelectPage: function (pageNumber, pageSize) {
//           alert(pageNumber+''+pageSize);
        	selectHotCarInfo();
        }
    });
}