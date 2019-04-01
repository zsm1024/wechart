package com.gwm.module.loanmanager.service;

import ch.ethz.ssh2.crypto.Base64;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.common.DecodeXmlImpl;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.Ipage;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.dao.sql.Pager;
import com.gwm.db.entity.Gw_loan_reg;
import com.gwm.db.entity.Gw_loan_reg_hst;
import com.gwm.db.entity.Gw_param;
import com.gwm.db.entity.Gw_hot_car;
import com.gwm.db.entity.Gw_shop;
import com.gwm.db.entity.meta.Gw_hot_carMeta;
import com.gwm.db.entity.meta.Gw_shopMeta;
import com.gwm.engine.Util;
import com.gwm.outsideinterface.service.WsdlInterfaceService;

@Service
public class LoanManagerService {
	private static Logger logger = LoggerFactory.getLogger(LoanManagerService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	
	@Transactional
	public List<Map<String,Object>> insertParam(String source,String brand,String Model,String ckuan,String price,String province,String city, 
			String shop, String name,String sex,String id, String phone,String openid) throws Exception{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map= new HashMap<String,Object>();
		Map<String,Object> reg_map= new HashMap<String,Object>();
		String application_num=UUID.randomUUID().toString();
		reg_map.put("application_num", application_num);
		reg_map.put("brand", brand);
		reg_map.put("model", Model);
		reg_map.put("style", ckuan);
		reg_map.put("first_amt", price);	
		reg_map.put("province", province);
		reg_map.put("city", city);
		reg_map.put("franchiser", shop);
		reg_map.put("name", name);
		reg_map.put("card_id", id);
		reg_map.put("sex", sex);
		reg_map.put("phone", phone);
		reg_map.put("status", "1");
		reg_map.put("openid", source.equals("site")?"":openid);
		reg_map.put("source", source.equals("site")?"1":"2");
		Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		String sysTime=format.format(new Date());
		String applyDate=sysTime.substring(0,8);
		reg_map.put("apply_date", applyDate);
		String applyTime=sysTime.substring(8,14); 
		reg_map.put("apply_time", applyTime);
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		try{
			String sql="select count(*) as count from gw_loan_reg where phone='$phone$' and apply_date='$apply_date$'";
			sql=SqlManager.getSql(sql, reg_map);
			logger.info("sql:"+sql);
			List<Map<String,Object>> lists=jdbc.queryForList(sql);
			if(lists.size()>=3){
				logger.info("当前手机号当日在线申请次数超过3次");
				map.put("error", "0001");
				map.put("error_msg", "当前手机号贷款申请超过每日规定次数，请明日再申请！");
				list.add(map);
				return list;
			}
			String reg_insert="insert into  gw_loan_reg  ( openid,application_num,brand,model,style,first_amt,province,city,franchiser,name,card_id,sex,phone,status,apply_date,apply_time,source ) "
					+ "values  ('$openid$','$application_num$','$brand$','$model$','$style$',$first_amt$,'$province$','$city$','$franchiser$','$name$','$card_id$','$sex$','$phone$','$status$',$apply_date$,$apply_time$,'$source$')";
			reg_insert=SqlManager.getSql(reg_insert, reg_map);
			logger.info("reg_insert:"+reg_insert);
			int i=jdbc.update(reg_insert);
			if(i==0){
				logger.error("受影响的条数记录为"+i);
				map.put("error", "0001");
				map.put("error_msg", "贷款申请登记失败！");
				list.add(map);
				return list;
			}
		}catch(Exception e){
			logger.error("贷款申请表登记失败",e);
			throw e;
//			map.put("error", "0001");
//			map.put("error_msg", "贷款申请登记失败！");
//			list.add(map);
//			return list;
		}
		
		Map<String,Object> hst_map=new HashMap<String,Object>();
		hst_map.put("openid", source.equals("site")?"":openid);
		hst_map.put("application_num", application_num);
		hst_map.put("status", "1");
		hst_map.put("operator", name);
		hst_map.put("rev_date", applyDate);
		hst_map.put("rev_time", applyTime);
		try{
			String hst_insert="insert into gw_loan_reg_hst ( openid,application_num,status,operator,rev_date,rev_time ) "
					+ " values  ('$openid$','$application_num$','$status$','$operator$',$rev_date$,$rev_time$)";
			hst_insert=SqlManager.getSql(hst_insert, hst_map);
			int i=jdbc.update(hst_insert);
			if(i==0){
				map.put("error", "0001");
				map.put("error_msg", "贷款申请登记失败！");
				list.add(map);
				return list;
			}
		}catch(Exception e){
			logger.error("贷款申请记录变更记录表登记失败",e);
			throw e;
//			map.put("error", "0001");
//			map.put("error_msg", "贷款申请登记失败！");
//			list.add(map);
//			return list;
		}
		map.put("error", "0000");
		map.put("error_msg", "尊敬的客户，我们已经受理您的在线申请，经销商会尽快与您联系，您可以登录长城金融官网http://www.gwmfc.com，关注“长城汽车金融”服务号查看最新活动信息，感谢您的支持。");
		list.add(map);
		return list;
	}

	/**
	 * 我的专属方案查询
	 *
	 * @param price
	 *            期望车价
	 * @param downpayment
	 *            预计首付
	 * @param repayPeriod
	 *            还款期数
	 * @return
	 */
	public List<Map<String, String>> caculteRecommand(String price, String downpayment, String repayPeriod) {
		logger.info("******进入方案查询******");
		NumberFormat formatter = new DecimalFormat("###,###.00");
		String[] tmp_price = price.split("@");
		double min_price = Double.parseDouble(tmp_price[0].toString()) * 10000;
		double max_price = Double.parseDouble(tmp_price[1].toString()) * 10000;
		double downpaymentd=Double.parseDouble(downpayment)*10000;
		downpayment=formatter.format(downpaymentd);
		Map<String, String> sql_map = new HashMap<String, String>();
		sql_map.put("min_price", min_price+"");
		sql_map.put("max_price", max_price+"");
		sql_map.put("price", downpaymentd+"");
		// List<Gw_hot_car> list=dao.list(Gw_hot_car.class,
		// Cnd.where(Gw_hot_carMeta.price.gtAndEq(min_price)).and(Gw_hot_carMeta.price.lessAndEq(max_price)));
		String sql ="select * from gw_hot_car "
				+ "	where purchase>0 and price>= $min_price$"//		购车区间<=车价<=购车区间
				+ " and price<= $max_price$ "
				+ " and $price$<=price "//				车价>=首付金额
				+ "and $price$/price>=rate "//			首付金额/车价 >=首付比例
				+ "and price-$price$>=minloanprice "//	车价-首付金额>=分期金额最小值
				+ "and price-$price$<=maxloanprice"//	车价-首付金额<=分期金额最大值
//				+ "and ((amount_product='快易贷' and $price$/price>=rate)  "
//				+ "or (amount_product='等额本息' and $price$/price>=rate) "
//				+ "or (amount_product='简易贷' and $price$/price>=rate)) "
				+ " order by purchase desc";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			sql = SqlManager.getSql(sql, sql_map);
			logger.info("sql:"+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			List<Map<String, Object>> lists = jdbc.queryForList(sql);
			if (lists != null && lists.size() > 0) {// 查到有数据
				logger.info("专属方案查询记录"+lists.size()+"条");
				//筛选list中，相同车型、配置的不同金融产品
				List<Map<String, Object>> res_lists=new ArrayList<Map<String,Object>>();
				List<Map<String, Object>> tmp_lists=new ArrayList<Map<String,Object>>();
				System.out.println("处理前"+lists);
				tmp_lists=checkList(lists);
				System.out.println("处理后"+tmp_lists);
				int count=0;
				if(tmp_lists.size()>5){
					count=5;
				}else{
					count=tmp_lists.size();
				}
				for(int i=0;i<count;i++){
					Map<String,Object> tmp_map=tmp_lists.get(i);
					res_lists.add(tmp_map);
				}
				System.out.println("前5条"+res_lists);
				for (int i = 0; i < res_lists.size(); i++) {
					Map<String, String> value = new HashMap<String, String>();
					value.put("brand", res_lists.get(i).get("brand").toString().trim());// 品牌
					value.put("models", res_lists.get(i).get("models").toString().trim());// 车型
					value.put("configure", res_lists.get(i).get("configure").toString().trim());// 款式
					double tmp_prices=Double.parseDouble(res_lists.get(i).get("price").toString().trim());
					value.put("amt", tmp_prices+"");//金额用于计算
					value.put("price",(int)tmp_prices+"");// 金额
					value.put("amount_product",res_lists.get(i).get("amount_product").toString().trim());// 金融产品
					value.put("purchase", res_lists.get(i).get("purchase").toString().trim());// 销量
					String pic=res_lists.get(i).get("pic")==null?"":res_lists.get(i).get("pic").toString().trim();
					value.put("pic", pic);// 图片
					value.put("downpayment", downpayment);//预计首付
					value.put("preoid", repayPeriod);//还款期数
					double fist=downpaymentd/tmp_prices*100;//首付比例=预计首付金额/车价*100%
					System.out.println("首付比例："+fist);
					String firstsize=String.valueOf(Math.floor(fist));
					//四舍五入取整:Math.rint(3.5)=4
					//进位取整:Math.ceil(3.1)=4 
					value.put("firstsize",firstsize);//首付金额
					if("简易贷".equals(res_lists.get(i).get("amount_product").toString().trim())){
						value.put("link","simple-loan.html");//金融产品链接
					}
					else if("快易贷".equals(res_lists.get(i).get("amount_product").toString().trim())){
						value.put("link","fast-easy-loan.html");//金融产品链接
					}
					else if("等额本息".equals(res_lists.get(i).get("amount_product").toString().trim())){
						value.put("link","matching-service.html");//金融产品链接
					}
					else if("等额本金".equals(res_lists.get(i).get("amount_product").toString().trim())){
						value.put("link","equal-principal.html");//金融产品链接
					}
					else if("随薪贷".equals(res_lists.get(i).get("amount_product").toString().trim())){
						value.put("link","pay-loan.html");//金融产品链接
					}
					list.add(value);
				}
			} else {// 没查到数据
				logger.info("******没有查询到相应方案******");
				Map<String, String> value = new HashMap<String, String>();
				value.put("error", "0001");
				list.add(value);
			}
		} catch (Exception e) {
			logger.error("专属方案查询出现异常：",e);
			e.printStackTrace();
		}
		logger.info("******退出方案查询******");
		return list;
	}

	public List<Map<String,Object>>	checkList(List<Map<String,Object>> lists){
		String remove="";
		for(int i=0;i<lists.size();i++){
			String model=lists.get(i).get("models").toString();//车型
			String configure=lists.get(i).get("configure").toString();//配置
			String amount_product=lists.get(i).get("amount_product").toString();//金融产品
			String id=lists.get(i).get("id").toString();//id;
			for(int j=i+1;j<lists.size();j++){
				String model2=lists.get(j).get("models").toString();//车型
				String configure2=lists.get(j).get("configure").toString();//配置
				String amount_product2=lists.get(j).get("amount_product").toString();//金融产品
				String id2=lists.get(j).get("id").toString();//id;
				if(model.equals(model2)&&configure.equals(configure2)){
					if(amount_product.trim().equals("简易贷"))//若用简易贷产品进入比较，发现相同车型、配置的记录，直接删除后者
					{
						remove+=id2+",";
					}
					if(amount_product.trim().equals("快易贷"))//若用快易贷。后者是简易贷，则删除前者，后者是等额本息，则删除后者。
					{
						if(amount_product2.trim().equals("简易贷")){
							remove+=id+",";
						}else if(amount_product2.trim().equals("等额本息")){
							remove+=id2+",";
						}
					}
					if(amount_product.trim().equals("等额本息"))//若用等额本息产品进入比较，发现相同车型、配置的记录，直接删除前者
					{
						remove+=id+",";
					}
				}
			}
		}
		if(remove.equals("")){
			System.out.println("remove为空");
			return lists;
		}else{
			remove=remove.substring(0,remove.length()-1);
			System.out.println("remove"+remove);
			String [] removelist=remove.split(",");
			List<Map<String,Object>> qq=new ArrayList<Map<String,Object>>();
			for(int k=0;k<lists.size();k++){
				String id=lists.get(k).get("id").toString();
				boolean flag=true;
				for(int m=0;m<removelist.length;m++){
					if(removelist[m].equals(id)){
						flag=false;
						break;
					}
				}
				if(flag){
					qq.add(lists.get(k));
				}
			}
			return qq;
		}
	}
	/**
	 * 火车汽车图片
	 * */
	public List<Map<String,Object>> getgetCarPic_gw(String ctype) throws Exception{
		logger.info("*******进入车辆图片查询*******");
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		Map<String,String> pic_map=new HashMap<String,String>();
		Map<String,Object> result=new HashMap<String,Object>();
		pic_map.put("models", ctype);
		String pic_sql="select pic from gw_hot_car where models='$models$'";
		pic_sql=SqlManager.getSql(pic_sql, pic_map);
		System.out.println(pic_sql);
		List<Map<String,Object>> res_list=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> pic_list=jdbc.queryForList(pic_sql);
		if(pic_list.size()<=0){
			result.put("error", "0001");
			result.put("error_", "未找到当前车型对应车辆图片");
			logger.info("未找到当前车型对应车辆图片:"+ctype);
		}else{
			result.put("error", "0000");
			result.put("car_pic", pic_list.get(0).get("pic").toString().trim());
			logger.info(ctype+":当前车型对应车辆图片"+pic_list.get(0).get("pic").toString().trim());
		}
		res_list.add(result);
		return res_list;
	}
	
	/**
	 * 官网获得经销商信息
	 * */
	public List<Map<String,Object>> getShopInfo_gw(String carModel){
		logger.info("*******进入经销商查询********");
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		Map<String,String> model_map=new HashMap<String,String>();
		Map<String,Object> map=new HashMap<String,Object>();//返回数据的map
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		model_map.put("ifc_car_model", carModel);
		String model_sql="";
		String province_sql="";
		String city_sql="";
		String shop_sql="";
		try {
			model_sql="select * from gw_turn_car_model where ifc_car_model='$ifc_car_model$'";
			model_sql=SqlManager.getSql(model_sql, model_map);
			List<Map<String,Object>> list_model=new ArrayList<Map<String,Object>>();
			list_model=jdbc.queryForList(model_sql);
			if(list_model.size()<=0){
				map.clear();
				map.put("error", "0001");
				map.put("error_msg", "当前车型没有经销商代理，请选择其他车型");
				logger.info("车型转换表中未找到匹配的车型名："+model_sql);
				list.add(map);
				return list;
			}
			//注意-----数据库中表gw_turn_car_model列shop_car_model不允许为空！！！！！
			carModel=list_model.get(0).get("shop_car_model").toString().trim();
			String sh_state="正常";//销售状态
			String sh_financecoorp="1";//是否合作
			Map<String,String> sql_map=new HashMap<String,String>();//sql的map
			
			sql_map.put("carModel", carModel);
			sql_map.put("sh_state", sh_state);
			sql_map.put("sh_financecoorp", sh_financecoorp);
			
			province_sql="select sh_province from gw_shop "
					+ "where sh_financecoorp='$sh_financecoorp$' and sh_state='$sh_state$' "
					+ "and sh_salecarbrand like '%$carModel$%' group by sh_province";
			city_sql="select sh_province,sh_city from gw_shop "
					+ "where sh_financecoorp='$sh_financecoorp$' and sh_state='$sh_state$' "
					+ "and sh_salecarbrand like '%$carModel$%' "
					+ "group by sh_province,sh_city order by sh_province";
			shop_sql="select sh_city, sh_name from gw_shop where sh_financecoorp='$sh_financecoorp$'"
					+ " and sh_state='$sh_state$' and sh_salecarbrand like '%$carModel$%' order by sh_city";
			province_sql=SqlManager.getSql(province_sql, sql_map);
			city_sql=SqlManager.getSql(city_sql, sql_map);
			shop_sql=SqlManager.getSql(shop_sql, sql_map);
		} catch (Exception e) {
			logger.error("拼装sql出现异常",e);
			map.clear();
			map.put("error", "0001");
			map.put("error_msg", "拼装sql出现异常");
			list.add(map);
			return list;
		}
		//查询省份
		List<Map<String,Object>> list_province=jdbc.queryForList(province_sql);
		if(list_province.size()<=0){
			map.clear();
			map.put("error", "0001");
			map.put("error_msg", "当前车型没有经销商代理，请选择其他车型");
			logger.info("拼装sql出现异常");
			list.add(map);
			return list;
		}
		String provice="[";
		for(int i=0;i<list_province.size();i++){
			provice+="{" +
    		  		"\"ProvinceName\":\""+list_province.get(i).get("sh_province").toString().trim()+"\"" +
    		  	"}," ;
		}
		provice=provice.substring(0,provice.length()-1);
		provice+="]";
//		System.out.println("provice=="+provice);
		map.put("province", provice);//向map中放置省份信息
		List<Map<String,Object>> list_city=jdbc.queryForList(city_sql);
		String city="";
		for(int j=0;j<list_city.size();j++){
			String pro=list_city.get(j).get("sh_province").toString().trim();
			city+="],\""+pro+"\":[{"+
					"\"CityName\":\""+list_city.get(j).get("sh_city").toString().trim()+"\""
					+ "}";
			for(int m=j+1;m<list_city.size();m++){
				if(pro.equals(list_city.get(m).get("sh_province").toString().trim())){
//					city=city.substring(0,city.length()-2);
					city+=",{" +
		    		  		"\"CityName\":\""+list_city.get(m).get("sh_city").toString().trim()+"\"" +
		        		  	"}" ;
				}else{
					break;
				}
				j=m;
			}
		}
		city=city.substring(2,city.length());
		city="{"+city+"]}";
//		System.out.println("city=="+city);
		map.put("city", city);
		if(list_city.size()<=0){
			map.clear();
			map.put("error", "0001");
			map.put("error_msg", "当前车型没有经销商代理，请选择其他车型");
			list.add(map);
			logger.info("当前车型没有经销商代理，请选择其他车型");
			return list;
		}
		List<Map<String,Object>> shop_list=jdbc.queryForList(shop_sql);	
		if(shop_list.size()<=0){
			map.clear();
			map.put("error", "0001");
			map.put("error_msg", "当前车型没有经销商代理，请选择其他车型");
			logger.info("当前车型没有经销商代理，请选择其他车型");
			list.add(map);
			return list;
		}
		String shop="";
		for(int j=0;j<shop_list.size();j++){
			String ci=shop_list.get(j).get("sh_city").toString().trim();
			shop+="],\""+ci+"\":[{"+
					"\"ShopName\":\""+shop_list.get(j).get("sh_name").toString().trim()+"\""
					+ "}";
			for(int m=j+1;m<shop_list.size();m++){
				if(ci.equals(shop_list.get(m).get("sh_city").toString().trim())){
//					city=city.substring(0,city.length()-2);
					shop+=",{" +
		    		  		"\"ShopName\":\""+shop_list.get(m).get("sh_name").toString().trim()+"\"" +
		        		  	"}" ;
				}else{
					break;
				}
				j=m;
			}
		}
		shop=shop.substring(2,shop.length());
		shop="{"+shop+"]}";
		map.put("shop",shop);
		System.out.println("shop=="+shop);
		logger.info("*******退出经销商查询******");
		list.add(map);
		return list;
	}
	/**
	 * 官网发送短信接口
	 * @param phone 手机号
	 * @param num 验证码
	 * */
	@Transactional
	public List<Map<String,Object>> sendCodeToPhone(String phone,String num){
		logger.info("******发送手机验证码开始******");
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		if(num==null||num.length()!=6){
			logger.error("验证码获取有误");
			map.put("error", "0001");
			map.put("error_msg","验证码获取有误");
		}
		else
		{
			try{
				//判断当前手机号获取验证码次数是否大于3次
				String sql="select count(*) as count from gw_msg_reg where type='$type$' and date='$date$' and phone='$phone$'";
				Map<String,Object> count_map=new HashMap<String,Object>();
				count_map.put("type", "3");//短信类型:3.验证码
				Format format = new SimpleDateFormat("yyyyMMdd");
				String sysdate=format.format(new Date());
				count_map.put("date", sysdate);
				count_map.put("phone", phone);
				sql=SqlManager.getSql(sql, count_map);
				List<Map<String,Object>> count_lists=jdbc.queryForList(sql);
				int count=Integer.parseInt(count_lists.get(0).get("count").toString().trim());
				int cc=Integer.parseInt(redisDao.getString("check_code_limit"));
				logger.info("每日短信接收条数参数：check_code_limit:"+cc);
				if(count>=cc){
					logger.info("当前手机号此类短信超过三条，请改日再发。");
					map.put("error", "0001");
					map.put("error_msg", "此手机此类短信接收超过每日上限，请改日再试");
				}else{
					String flag=redisDao.getString("NewMsgFlag");//新旧短信平台标志
					//调用短信转发接口
					//String duxin_xml="【长城金融】您的验证码为："+num+"，10分钟内有效。为保证账户安全，请勿向任何人透露。如非本人操作，请致电客服热线400 6527 606。";
					String duxin_xml="您的验证码为："+num+"，10分钟内有效。为保证账户安全，请勿向任何人透露。如非本人操作，请致电客服热线400 6527 606。";
					String user=redisDao.getString("MSG_USER");
					String pass=redisDao.getString("MSG_PASS");
					if(flag.equals("0")){//旧短信平台
						String url=redisDao.getString("OLD_SENDMSG_URL");//旧短信平台地址
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
						stringBuffer.append("<root>");
						stringBuffer.append("<msgs>");
						stringBuffer.append("<msg>");
						stringBuffer.append("<account>"+user+"</account>");
						stringBuffer.append("<pwd>"+pass+"</pwd>");
						stringBuffer.append("<phone>"+phone+"</phone>");
						stringBuffer.append("<content>"+duxin_xml+"</content>");
						stringBuffer.append("</msg>");
						stringBuffer.append("</msgs>");
						stringBuffer.append("</root>");
						// 定义客户端对象
						try {		 
							String requestXML=stringBuffer.toString();
							logger.info("requestXML="+requestXML);
							WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "send");
//							logger.info("返回报文"+repxml); 
//							//解析返回报文等到map
//							returnMap = DecodeXmlImpl.decode(repxml);
//							Map<String,String> temp=Util.returnMap("0", "查询成功", Util.mapObjectToString(returnMap));
							logger.info("验证码发送成功,手机号："+phone+"，验证码:"+num);
						} catch (Exception ex) {
							logger.error("短信发送接口异常:", ex);
							map.put("error", "0001");
							map.put("error_msg","验证码发送失败");
							list.add(map);
							return list;
//							return Util.returnMap("9999", "还款记录查询失败");
						}
					}else{
						logger.info("========通过新短信平台转发=======");
						String url=redisDao.getString("NEW_SENDMSG_URL");//新短信平台地址
						String source=redisDao.getString("MSG_GW_FLAG");
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
						stringBuffer.append("<root>");
						stringBuffer.append("<msgs>");
						stringBuffer.append("<msg>");
						stringBuffer.append("<account>"+user+"</account>");
						stringBuffer.append("<pwd>"+pass+"</pwd>");
						stringBuffer.append("<origin>"+source+"</origin>");
						stringBuffer.append("<phone>"+phone+"</phone>");
						stringBuffer.append("<content>"+duxin_xml+"</content>");
						stringBuffer.append("</msg>");
						stringBuffer.append("</msgs>");
						stringBuffer.append("</root>");
						String requestXML=stringBuffer.toString();
						logger.info("requestXML="+requestXML);
						boolean f=NewSendmsg(url,requestXML);
						if(f){
							logger.info("验证码发送成功,手机号："+phone+"，验证码:"+num);
						}else{
							logger.error("调用短信发送接口异常");
							map.put("error", "0001");
							map.put("error_msg","验证码发送失败");
							list.add(map);
							return list;
						}
					}
					sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
							+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
					count_map.put("content", duxin_xml);
					count_map.put("wx_msg_id", "");
					count_map.put("status", "1");
					sql=SqlManager.getSql(sql, count_map);
					jdbc.update(sql);
					//Map<String,Object> returnMap = new HashMap<String, Object>();
					logger.info("验证码发送成功,手机号："+phone+"，验证码:"+num);
					map.put("error", "0000");
					map.put("error_msg", "验证码发送成功");
					Format format2 = new SimpleDateFormat("yyyyMMddHHmmss");
					String sysdate2=format2.format(new Date());
					map.put("sendtime", sysdate2);
				}
				
			}catch(Exception e){
				logger.error("******发送手机验证码出现异常******",e);
				map.put("error", "0001");
				map.put("error_msg","验证码获取有误");
				list.add(map);
				return list;
			}
		}
		logger.info("******发送手机验证码开始******");
		list.add(map);
		return list;
	}
	
	
    public static boolean NewSendmsg(String url,String sendXML){
    	boolean flag=true;
    	try{
    		WsdlInterfaceService.sendXML(url, sendXML, "http://utils.gwm.com", "send");
    	}catch(Exception e){
    		logger.error("发送短信验证码失败",e);
    		flag=false;
    		return flag;
    	}
    	return flag;
    }
    
    
	public static void main(String args[]) {
		 List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		 Map<String, String> map=new HashMap<String,String>();
		 map.put("name", "java");
		 map.put("name", "php");
		 map.put("name", "net");
		 map.put("name", "java");
		 map.put("name", "php");
		 map.put("name", "net");
		 list.add(map);
	     
		for(int i=0;i<list.size();i++){
			String tmp=list.get(i).get("name").toString();
			System.out.println(tmp);
			for(int j=i+1;j<list.size();j++){
				String tmp2=list.get(j).get("name").toString();
				System.out.println(tmp);
				if(tmp.equals(tmp2)){
					list.remove(j);
				}
			}
		}
		System.out.println("处理后集合："+list);
		
//		decodeBase64("NmFPTzZhcVBOUT09",2);
		Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		String sysTime=format.format(new Date());
		String applyDate=sysTime.substring(0,8);
		String applyTime=sysTime.substring(8,14);
		System.out.println(applyTime);
//		NumberFormat formatter = new DecimalFormat("###,###.00");
//		System.out.println(formatter.format(101800.00));
//
//		NumberFormat numberFormat1 = NumberFormat.getNumberInstance();
//		System.out.println(numberFormat1.format(10000000)); // 结果是11,122.33
	}
}
