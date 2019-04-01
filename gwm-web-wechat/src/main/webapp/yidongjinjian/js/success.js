$(document).ready(function(){
	var storages=localStorage;
	storages=JSON.stringify(localStorage);		
})
$(".OKbtn").click(function(){
	localStorage.clear();
	wx.closeWindow();
})
