 
function getProvinces(){
	var provinces = [
	          	    {"ProvinceName":"北京市","ProvinceID":"11"},
	          	    {"ProvinceName":"天津市","ProvinceID":"12"},
	          	    {"ProvinceName":"上海市","ProvinceID":"31"},
	          	    {"ProvinceName":"重庆市","ProvinceID":"50"},
	          	    {"ProvinceName":"河北省","ProvinceID":"13"},
	          	    {"ProvinceName":"山西省","ProvinceID":"14"},
	          	    {"ProvinceName":"内蒙古","ProvinceID":"15"},
	          	    {"ProvinceName":"辽宁省","ProvinceID":"21"},
		          	{"ProvinceName":"吉林省","ProvinceID":"22"},
		          	{"ProvinceName":"黑龙江省","ProvinceID":"23"},
		          	{"ProvinceName":"江苏省","ProvinceID":"32"},
		          	{"ProvinceName":"浙江省","ProvinceID":"33"},
		          	{"ProvinceName":"安徽省","ProvinceID":"34"},
		          	{"ProvinceName":"福建省","ProvinceID":"35"},
		          	{"ProvinceName":"江西省","ProvinceID":"36"},
		          	{"ProvinceName":"山东省","ProvinceID":"37"},
		          	{"ProvinceName":"河南省","ProvinceID":"41"},
		          	{"ProvinceName":"湖北省","ProvinceID":"42"},
		          	{"ProvinceName":"湖南省","ProvinceID":"43"},
		          	{"ProvinceName":"广东省","ProvinceID":"44"},
		          	{"ProvinceName":"广西省","ProvinceID":"45"},
		          	{"ProvinceName":"海南省","ProvinceID":"46"},
		          	{"ProvinceName":"四川省","ProvinceID":"51"},
		          	{"ProvinceName":"贵州省","ProvinceID":"52"},
		          	{"ProvinceName":"云南省","ProvinceID":"53"},
		          	{"ProvinceName":"西藏","ProvinceID":"54"},
		          	{"ProvinceName":"陕西省","ProvinceID":"61"},
		          	{"ProvinceName":"甘肃省","ProvinceID":"62"},
		          	{"ProvinceName":"青海省","ProvinceID":"63"},
		          	{"ProvinceName":"宁夏省","ProvinceID":"64"},
		          	{"ProvinceName":"新疆","ProvinceID":"65"}
	        	];

	return provinces;
}


function getCities(province){
	var cities = {"北京市":[{"CityName":"北京市","CityID":"11"}],
		"天津市":[{"CityName":"天津市","CityID":"12"}],
		"上海市":[{"CityName":"上海市","CityID":"31"}],
		"重庆市":[{"CityName":"重庆市","CityID":"50"}],
		"河北省":[{"CityName":"石家庄市","CityID":"1301"},{"CityName":"唐山市","CityID":"1302"},{"CityName":"秦皇岛市","CityID":"1303"},{"CityName":"邯郸市","CityID":"1304"},{"CityName":"邢台市","CityID":"1305"},{"CityName":"保定市","CityID":"1306"},{"CityName":"张家口市","CityID":"1307"},{"CityName":"承德市","CityID":"1308"},{"CityName":"沧州市","CityID":"1309"},{"CityName":"廊坊市","CityID":"1310"},{"CityName":"衡水市","CityID":"1311"}],
		"山西省":[{"CityName":"太原市","CityID":"1401"},{"CityName":"大同市","CityID":"1402"},{"CityName":"阳泉市","CityID":"1403"},{"CityName":"长治市","CityID":"1404"},{"CityName":"晋城市","CityID":"1405"},{"CityName":"朔州市","CityID":"1406"},{"CityName":"晋中市","CityID":"1407"},{"CityName":"运城市","CityID":"1408"},{"CityName":"忻州市","CityID":"1409"},{"CityName":"临汾市","CityID":"1410"},{"CityName":"吕梁市","CityID":"1411"},{"CityName":"雁北市","CityID":"1412"}],
		"内蒙古":[{"CityName":"呼和浩特市","CityID":"1501"},{"CityName":"包头市","CityID":"1502"},{"CityName":"乌海市","CityID":"1503"},{"CityName":"赤峰市","CityID":"1504"},{"CityName":"通辽市","CityID":"1505"},{"CityName":"鄂尔多斯","CityID":"1506"},{"CityName":"呼伦贝尔盟","CityID":"1507"},{"CityName":"巴彦淖尔盟","CityID":"1508"},{"CityName":"乌兰察布盟","CityID":"1509"},{"CityName":"兴安盟","CityID":"1522"},{"CityName":"锡林郭勒盟","CityID":"1525"},{"CityName":"阿拉善盟","CityID":"1529"}],
		"辽宁省":[{"CityName":"沈阳市","CityID":"2101"},{"CityName":"大连市","CityID":"2102"},{"CityName":"鞍山市","CityID":"2103"},{"CityName":"抚顺市","CityID":"2104"},{"CityName":"本溪市","CityID":"2105"},{"CityName":"丹东市","CityID":"2106"},{"CityName":"锦州市","CityID":"2107"},{"CityName":"营口市","CityID":"2108"},{"CityName":"阜新市","CityID":"2109"},{"CityName":"辽阳市","CityID":"2110"},{"CityName":"盘锦市","CityID":"2111"},{"CityName":"铁岭市","CityID":"2113"},{"CityName":"葫芦岛市","CityID":"2114"}],
		"吉林省":[{"CityName":"长春市","CityID":"2201"},{"CityName":"吉林市","CityID":"2202"},{"CityName":"四平市","CityID":"2203"},{"CityName":"辽源市","CityID":"2204"},{"CityName":"通化市","CityID":"2205"},{"CityName":"白山市","CityID":"2206"},{"CityName":"松原市","CityID":"2207"},{"CityName":"白城市","CityID":"2208"},{"CityName":"延边朝鲜族自治州","CityID":"2224"}],
		"黑龙江省":[{"CityName":"哈尔滨市","CityID":"2301"},{"CityName":"齐齐哈尔市","CityID":"2302"},{"CityName":"鸡西市","CityID":"2303"},{"CityName":"鹤岗市","CityID":"2304"},{"CityName":"双鸭山市","CityID":"2305"},{"CityName":"大庆市","CityID":"2306"},{"CityName":"伊春市","CityID":"2307"},{"CityName":"佳木斯市","CityID":"2308"},{"CityName":"七台河市","CityID":"2309"},{"CityName":"牡丹江市","CityID":"2310"},{"CityName":"黑河市","CityID":"2311"},{"CityName":"绥化市","CityID":"2312"},{"CityName":"大兴安岭","CityID":"2327"},{"CityName":"松花江地区","CityID":"2328"},{"CityName":"农垦地区","CityID":"2329"}],
		"江苏省":[{"CityName":"南京市","CityID":"3201"},{"CityName":"无锡市","CityID":"3202"},{"CityName":"徐州市","CityID":"3203"},{"CityName":"常州市","CityID":"3204"},{"CityName":"苏州市","CityID":"3205"},{"CityName":"南通市","CityID":"3206"},{"CityName":"连云港市","CityID":"3207"},{"CityName":"淮安市","CityID":"3208"},{"CityName":"盐城市","CityID":"3209"},{"CityName":"扬州市","CityID":"3210"},{"CityName":"镇江市","CityID":"3211"},{"CityName":"泰州市","CityID":"3212"},{"CityName":"宿迁市","CityID":"3213"}],
		"浙江省":[{"CityName":"杭州市","CityID":"3301"},{"CityName":"宁波市","CityID":"3302"},{"CityName":"温州市","CityID":"3303"},{"CityName":"嘉兴市","CityID":"3304"},{"CityName":"湖州市","CityID":"3305"},{"CityName":"绍兴市","CityID":"3306"},{"CityName":"金华市","CityID":"3307"},{"CityName":"衢州市","CityID":"3308"},{"CityName":"舟山市","CityID":"3309"},{"CityName":"台州市","CityID":"3310"},{"CityName":"丽水市","CityID":"3311"}],
		"安徽省":[{"CityName":"合肥市","CityID":"3401"},{"CityName":"芜湖市","CityID":"3402"},{"CityName":"蚌埠市","CityID":"3403"},{"CityName":"淮南市","CityID":"3404"},{"CityName":"马鞍山市","CityID":"3405"},{"CityName":"淮北市","CityID":"3406"},{"CityName":"铜陵市","CityID":"3407"},{"CityName":"安庆市","CityID":"3408"},{"CityName":"黄山市","CityID":"3410"},{"CityName":"滁州市","CityID":"3411"},{"CityName":"阜阳市","CityID":"3412"},{"CityName":"宿州市","CityID":"3413"},{"CityName":"六安市","CityID":"3415"},{"CityName":"亳州市","CityID":"3416"},{"CityName":"池州市","CityID":"3417"},{"CityName":"宣城市","CityID":"3418"},{"CityName":"巢湖市","CityID":"340181"}],
		"福建省":[{"CityName":"福州市","CityID":"3501"},{"CityName":"厦门市","CityID":"3502"},{"CityName":"莆田市","CityID":"3503"},{"CityName":"三明市","CityID":"3504"},{"CityName":"泉州市","CityID":"3505"},{"CityName":"漳州市","CityID":"3506"},{"CityName":"南平市","CityID":"3507"},{"CityName":"龙岩市","CityID":"3208"},{"CityName":"龙岩市","CityID":"3508"},{"CityName":"宁德市","CityID":"3509"},{"CityName":"省直系统","CityID":"3510"}],
		"江西省":[{"CityName":"南昌市","CityID":"3601"},{"CityName":"景德镇","CityID":"3602"},{"CityName":"萍乡市","CityID":"3603"},{"CityName":"九江市","CityID":"3604"},{"CityName":"新余市","CityID":"3605"},{"CityName":"鹰潭市","CityID":"3606"},{"CityName":"赣州市","CityID":"3607"},{"CityName":"吉安市","CityID":"3608"},{"CityName":"宜春市","CityID":"3609"},{"CityName":"抚州市","CityID":"3610"},{"CityName":"上饶市","CityID":"3611"},{"CityName":"南昌市省直系统","CityID":"3612"}],
		"山东省":[{"CityName":"济南市","CityID":"3701"},{"CityName":"青岛市","CityID":"3702"},{"CityName":"淄博市","CityID":"3703"},{"CityName":"枣庄市","CityID":"3704"},{"CityName":"东营市","CityID":"3705"},{"CityName":"烟台市","CityID":"3706"},{"CityName":"潍坊市","CityID":"3707"},{"CityName":"济宁市","CityID":"3708"},{"CityName":"泰安市","CityID":"3709"},{"CityName":"威海市","CityID":"3710"},{"CityName":"日照市","CityID":"3711"},{"CityName":"莱芜市","CityID":"3712"},{"CityName":"临沂市","CityID":"3713"},{"CityName":"德州市","CityID":"3714"},{"CityName":"聊城市","CityID":"3715"},{"CityName":"滨州市","CityID":"3716"},{"CityName":"菏泽市","CityID":"3717"},{"CityName":"青岛市增补","CityID":"3718"},{"CityName":"潍坊市增补","CityID":"3719"},{"CityName":"烟台市增补","CityID":"3720"}],
		"河南省":[{"CityName":"郑州市","CityID":"4101"},{"CityName":"开封市","CityID":"4102"},{"CityName":"洛阳市","CityID":"4103"},{"CityName":"平顶山市","CityID":"4104"},{"CityName":"安阳市","CityID":"4105"},{"CityName":"鹤壁市","CityID":"4106"},{"CityName":"新乡市","CityID":"4107"},{"CityName":"焦作市","CityID":"4108"},{"CityName":"濮阳市","CityID":"4109"},{"CityName":"许昌市","CityID":"4110"},{"CityName":"漯河市","CityID":"4111"},{"CityName":"三门峡","CityID":"4112"},{"CityName":"南阳市","CityID":"4113"},{"CityName":"商丘市","CityID":"4114"},{"CityName":"信阳市","CityID":"4115"},{"CityName":"周口市","CityID":"4116"},{"CityName":"驻马店市","CityID":"4117"},{"CityName":"济源市","CityID":"419001"}],
		"湖北省":[{"CityName":"武汉市","CityID":"4201"},{"CityName":"黄石市","CityID":"4202"},{"CityName":"十堰市","CityID":"4203"},{"CityName":"宜昌市","CityID":"4205"},{"CityName":"襄阳市","CityID":"4206"},{"CityName":"鄂州市","CityID":"4207"},{"CityName":"荆门市","CityID":"4208"},{"CityName":"孝感市","CityID":"4209"},{"CityName":"荆州市","CityID":"4210"},{"CityName":"黄冈市","CityID":"4211"},{"CityName":"咸宁市","CityID":"4212"},{"CityName":"随州市","CityID":"4213"},{"CityName":"仙桃市","CityID":"429004"},{"CityName":"潜江市","CityID":"429005"},{"CityName":"天门市","CityID":"429006"},{"CityName":"神农架林区","CityID":"429021"},{"CityName":"恩施土家族苗族自治州","CityID":"4228"}],
		"湖南省":[{"CityName":"长沙市","CityID":"4301"},{"CityName":"株洲市","CityID":"4302"},{"CityName":"湘潭市","CityID":"4303"},{"CityName":"衡阳市","CityID":"4304"},{"CityName":"邵阳市","CityID":"4305"},{"CityName":"岳阳市","CityID":"4306"},{"CityName":"常德市","CityID":"4307"},{"CityName":"张家界","CityID":"4308"},{"CityName":"益阳市","CityID":"4309"},{"CityName":"郴州市","CityID":"4310"},{"CityName":"永州市","CityID":"4311"},{"CityName":"怀化市","CityID":"4312"},{"CityName":"娄底市","CityID":"4313"},{"CityName":"湘西土家族苗族自治州","CityID":"4331"}],
		"广东省":[{"CityName":"广州市","CityID":"4401"},{"CityName":"韶关市","CityID":"4402"},{"CityName":"深圳市","CityID":"4403"},{"CityName":"珠海市","CityID":"4404"},{"CityName":"汕头市","CityID":"4405"},{"CityName":"佛山市","CityID":"4406"},{"CityName":"江门市","CityID":"4407"},{"CityName":"湛江市","CityID":"4408"},{"CityName":"茂名市","CityID":"4409"},{"CityName":"肇庆市","CityID":"4412"},{"CityName":"惠州市","CityID":"4413"},{"CityName":"梅州市","CityID":"4414"},{"CityName":"汕尾市","CityID":"4415"},{"CityName":"河源市","CityID":"4416"},{"CityName":"阳江市","CityID":"4417"},{"CityName":"清远市","CityID":"4418"},{"CityName":"东莞市","CityID":"4419"},{"CityName":"中山市","CityID":"4420"},{"CityName":"潮州市","CityID":"4451"},{"CityName":"揭阳市","CityID":"4452"},{"CityName":"云浮市","CityID":"4453"},{"CityName":"顺德区","CityID":"4454"},{"CityName":"南海区","CityID":"4455"},{"CityName":"港澳","CityID":"4480"}],
		"广西省":[{"CityName":"南宁市","CityID":"4501"},{"CityName":"柳州市","CityID":"4502"},{"CityName":"桂林市","CityID":"4503"},{"CityName":"梧州市","CityID":"4504"},{"CityName":"北海市","CityID":"4505"},{"CityName":"防城港市","CityID":"4506"},{"CityName":"钦州市","CityID":"4507"},{"CityName":"贵港市","CityID":"4508"},{"CityName":"玉林市","CityID":"4509"},{"CityName":"百色市","CityID":"4510"},{"CityName":"贺州市","CityID":"4511"},{"CityName":"河池市","CityID":"4512"},{"CityName":"来宾市","CityID":"4513"},{"CityName":"崇左市","CityID":"4514"},{"CityName":"桂林地区","CityID":"4525"}],
		"海南省":[{"CityName":"海口市","CityID":"4601"},{"CityName":"三亚市","CityID":"4602"},{"CityName":"洋浦开发区市","CityID":"4606"},{"CityName":"五指山市","CityID":"469001"},{"CityName":"琼海市","CityID":"469002"}],
		"甘肃省":[{"CityName":"兰州市","CityID":"6201"},{"CityName":"嘉峪关市","CityID":"6202"},{"CityName":"金昌市","CityID":"6203"},{"CityName":"白银市","CityID":"6204"},{"CityName":"天水市","CityID":"6205"},{"CityName":"武威市","CityID":"6206"},{"CityName":"张掖市","CityID":"6207"},{"CityName":"平凉市","CityID":"6208"},{"CityName":"酒泉市","CityID":"6209"},{"CityName":"庆阳市","CityID":"6210"},{"CityName":"定西市","CityID":"6211"},{"CityName":"陇南市","CityID":"6212"},{"CityName":"临夏回族自治州","CityID":"6229"},{"CityName":"甘南藏族自治州","CityID":"6230"}],
		"青海省":[{"CityName":"西宁市","CityID":"6301"},{"CityName":"海东地区","CityID":"6321"},{"CityName":"海北藏族自治州","CityID":"6322"},{"CityName":"黄南藏族自治州","CityID":"6323"},{"CityName":"海南藏族自治州","CityID":"6325"},{"CityName":"果洛藏族自治州","CityID":"6326"},{"CityName":"玉树藏族自治州","CityID":"6327"},{"CityName":"海西蒙古族藏族自治州","CityID":"6328"}],
		"宁夏省":[{"CityName":"银川市","CityID":"6401"},{"CityName":"石嘴山市","CityID":"6402"},{"CityName":"吴忠市","CityID":"6403"},{"CityName":"固原市","CityID":"6404"},{"CityName":"中卫市","CityID":"6405"}],
		"陕西省":[{"CityName":"西安市","CityID":"6101"},{"CityName":"铜川市","CityID":"6102"},{"CityName":"宝鸡市","CityID":"6103"},{"CityName":"咸阳市","CityID":"6104"},{"CityName":"渭南市","CityID":"6105"},{"CityName":"延安市","CityID":"6106"},{"CityName":"汉中市","CityID":"6107"},{"CityName":"榆林市","CityID":"6108"},{"CityName":"安康市","CityID":"6109"},{"CityName":"商洛市","CityID":"6110"},{"CityName":"杨凌高新农业示范区","CityID":"6125"}],
		"四川省":[{"CityName":"成都市","CityID":"5101"},{"CityName":"自贡市","CityID":"5103"},{"CityName":"攀枝花市","CityID":"5104"},{"CityName":"泸州","CityID":"5105"},{"CityName":"德阳市","CityID":"5106"},{"CityName":"绵阳市","CityID":"5107"},{"CityName":"广元市","CityID":"5108"},{"CityName":"遂宁市","CityID":"5109"},{"CityName":"内江市","CityID":"5110"},{"CityName":"乐山市","CityID":"5111"},{"CityName":"南充市","CityID":"5113"},{"CityName":"眉山市","CityID":"5114"},{"CityName":"宜宾市","CityID":"5115"},{"CityName":"广安市","CityID":"5116"},{"CityName":"达州市","CityID":"5117"},{"CityName":"雅安市","CityID":"5118"},{"CityName":"巴中市","CityID":"5119"},{"CityName":"资阳市","CityID":"5120"},{"CityName":"阿坝市","CityID":"5132"},{"CityName":"甘孜市","CityID":"5133"},{"CityName":"凉山彝族自治州","CityID":"5134"},{"CityName":"西昌市","CityID":"513401"}],
		"贵州省":[{"CityName":"贵阳市","CityID":"5201"},{"CityName":"六盘水市","CityID":"5202"},{"CityName":"遵义市","CityID":"5203"},{"CityName":"安顺市","CityID":"5204"},{"CityName":"毕节市","CityID":"5205"},{"CityName":"铜仁市","CityID":"5206"},{"CityName":"黔西南布依族苗族自治","CityID":"5223"},{"CityName":"黔东南苗族侗族自治州","CityID":"5226"},{"CityName":"黔南布依族苗族自治州","CityID":"5227"}],
		"云南省":[{"CityName":"昆明市","CityID":"5301"},{"CityName":"曲靖市","CityID":"5303"},{"CityName":"玉溪市","CityID":"5304"},{"CityName":"保山市","CityID":"5305"},{"CityName":"昭通市","CityID":"5306"},{"CityName":"丽江市","CityID":"5307"},{"CityName":"普洱市","CityID":"5308"},{"CityName":"临沧市","CityID":"5309"},{"CityName":"楚雄彝族自治州","CityID":"5323"},{"CityName":"红河哈尼族彝族自治州","CityID":"5325"},{"CityName":"文山壮族苗族自治州","CityID":"5326"},{"CityName":"西双版纳傣族自治州","CityID":"5328"},{"CityName":"大理白族自治州","CityID":"5329"},{"CityName":"德宏傣族景颇族自治州","CityID":"5331"},{"CityName":"怒江傈僳族自治州","CityID":"5333"},{"CityName":"迪庆藏族自治州","CityID":"5334"}],
		"新疆":[{"CityName":"乌鲁木齐市","CityID":"6501"},{"CityName":"克拉玛依","CityID":"6502"},{"CityName":"克拉玛依","CityID":"6502"},{"CityName":"吐鲁番地区","CityID":"6521"},{"CityName":"哈密地区","CityID":"6522"},{"CityName":"昌吉回族自治州","CityID":"6523"},{"CityName":"博尔塔拉蒙古自治州","CityID":"6527"},{"CityName":"巴音郭楞蒙古自治州","CityID":"6528"},{"CityName":"阿克苏地区","CityID":"6529"},{"CityName":"克孜勒苏柯尔克孜自治","CityID":"6530"},{"CityName":"喀什地区","CityID":"6531"},{"CityName":"和田地区","CityID":"6532"},{"CityName":"伊犁哈萨克自治州","CityID":"6540"},{"CityName":"塔城地区","CityID":"6542"},{"CityName":"阿勒泰地区","CityID":"6543"}],
		"西藏":[{"CityName":"拉萨市","CityID":"5401"},{"CityName":"昌都地区","CityID":"5421"},{"CityName":"山南地区","CityID":"5422"},{"CityName":"日喀则地区","CityID":"5423"},{"CityName":"那曲地区","CityID":"5424"},{"CityName":"阿里地区","CityID":"5425"},{"CityName":"林芝地区","CityID":"5426"}]};
//		"请选择":[{"CityName":"请选择","CityID":"0"}]
	if(province == null || province == ""){
		return false;
	}
	return eval("cities." + province);
	
}