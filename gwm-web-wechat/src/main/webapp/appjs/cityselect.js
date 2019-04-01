$(function(){
	cityInit();
});

function cityInit(name){
	var cityList = "";
	sessionStorage.removeItem("select_city");
	var current_city = sessionStorage.getItem("current_city");
	if(current_city!=undefined&&""!=current_city){
		cityList += '<dt>' +
				'<a href="javascript:history.back();" class="back"></a>' +
				'<span>定位城市</span>' +
				'</dt>' +
				'<dd onclick="clickItem(\''+current_city+'\')">'+current_city+'</dd>';
	}
	var h='';
	for(var i = 0; i < cityHead.length; i++){
		var head = cityHead[i];
		h = '<dt>'+head+'</dt>';
		var city = cityJson[head];
		var c = '';
		for(var j = 0; j < city.length; j++){
			if(name&&name.length>0){
				var city_name = city[j].CityName;
				if(name.indexOf(city_name)>=0||city_name.indexOf(name)>=0)
				{
					c += '<dd onclick="clickItem(\''+city_name+'\')">'+city_name+'</dd>';
				}
			}else{
				var city_name = city[j].CityName;
				c += '<dd onclick="clickItem(\''+city_name+'\')">'+city_name+'</dd>';
			}
		}
		if(c.length>0){
			cityList += h;
			cityList += c;
		}
	}
	$("#city_list").html(cityList);
}

function clickItem(city){
	sessionStorage.setItem("select_city", city);
	history.go(-1);
}

function searchCity(){
	var city=$("#city").val().trim();
	console.info(city);
	if(city&&city.length>0){
		cityInit(city);
	}else{
		cityInit();
	}
}