package com.gwm.module.manager.service;

import java.net.URLDecoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import net.sf.json.JSONObject;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import com.alibaba.fastjson.JSON;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_hot_car;
import com.gwm.db.entity.Gw_loan_reg;
import com.gwm.db.entity.meta.Gw_hot_carMeta;
import com.gwm.db.entity.meta.Gw_loan_regMeta;

@Service
public class ManagerService {
	private Logger logger = LoggerFactory.getLogger(ManagerService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	/**
	 * 导入热销车型excel
	 * */
	@Transactional
	public List<Map<String,Object>> importExcel(Map<String,String> map){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		try{
			dao.delete(Gw_hot_car.class,Cnd.where(Gw_hot_carMeta.id.isNotNull()));
			String ss=map.get("excelInfo");
			JSONObject json=JSONObject.fromObject(ss);
			String list=json.getString("List").toString();
			List list1 = new ArrayList();
	    	list1=JSON.parseArray(list);
	    	for(int i=0;i<list1.size();i++)
	    	{
	    		JSONObject obj = JSONObject.fromObject(list1.get(i).toString());
	    		Gw_hot_car hot_carEntity=new Gw_hot_car();
	    		double id=Double.parseDouble(obj.get("id").toString());
	    		hot_carEntity.setId((int)id);
	    		hot_carEntity.setBrand(obj.get("brand").toString());
	    		hot_carEntity.setModels(obj.get("models").toString());
	    		hot_carEntity.setConfigure(obj.get("configure").toString());
	    		hot_carEntity.setPrice((obj.get("price")==null||obj.get("price").equals(""))?"0":obj.get("price").toString());
	    		hot_carEntity.setAmount_product(obj.get("amount_product").toString());
	    		double purchase=Double.parseDouble(obj.get("purchase").toString());
	    		hot_carEntity.setPurchase((int)purchase);
	    		hot_carEntity.setPic(obj.get("pic").toString());
	    		hot_carEntity.setRate(obj.get("rate").toString());
	    		hot_carEntity.setLoanterm(obj.get("loanterm").toString());
	    		hot_carEntity.setMinloanprice(obj.get("minloanprice").toString());
	    		hot_carEntity.setMaxloanprice(obj.get("maxloanprice").toString());
	    		dao.insert(hot_carEntity);
	    	}
		}catch(Exception e){
			logger.error("导入excel异常",e);
			
			throw e;
		}
		logger.info("导入excel成功");
    	Map<String,Object> result_map=new HashMap<String,Object>();
    	result_map.put("error", "0000");
		result_map.put("error_msg", "执行成功");
		result.add(result_map);
		return result;
	}
	/**
	 * 更新在线申请状态
	 * */
	@Transactional
	public List<Map<String,Object>>	updateApplyInfo(Map<String,String> map) throws Exception{
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		String application_num=(map.get("apply_id")==null||map.get("apply_id")=="")?"":map.get("apply_id").toString().trim();
		String status=(map.get("status")==null||map.get("status")=="")?"":map.get("status").toString().trim();
		Gw_loan_reg regEntity=dao.fetch(Gw_loan_reg.class,Cnd.where(Gw_loan_regMeta.application_num.eq(application_num)));
		if(regEntity==null){
			logger.info("申请记录不存在");
			result_map.put("error", "0001");
			result_map.put("error_msg", "申请记录不存在，请重试");
			result.add(result_map);
			return result;
		}
		String openid=regEntity.getOpenid();
		String sql="";
		Map<String,String> sql_map=new HashMap<String,String>();
		sql_map.put("status", status);
		sql_map.put("application_num", application_num);
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		try{
			if(application_num.equals("")||status.equals("")){
				logger.info("更新在线申请出现异常");
				result_map.put("error", "0001");
				result_map.put("error_msg", "参数传递有误，请重试");
				result.add(result_map);
				return result;
			}else{
				sql="update gw_loan_reg set status='$status$' where application_num='$application_num$'";
				sql=SqlManager.getSql(sql, sql_map);
				jdbc.update(sql);
				logger.info("更新在线申请表状态成功");
			}
		}catch(Exception e){
			logger.error("更新在线申请状态出现异常",e);
			throw e;
		}
		
//		String openid=(map.get("openid")==null||map.get("openid")=="")?"":map.get("openid").toString();
		String operator=(map.get("operator")==null||map.get("operator")=="")?"":map.get("operator").toString();
		
		try{
			Format format = new SimpleDateFormat("yyyyMMddHHmmss");
			String sysTime=format.format(new Date());
			String rev_date=sysTime.substring(0,8);
			sql_map.put("rev_date", rev_date);
			String rev_time=sysTime.substring(8,14); 
			sql_map.put("rev_time", rev_time);
			sql_map.put("openid", openid);
			sql_map.put("operator", operator);
			sql="insert into gw_loan_reg_hst (openid,application_num,status,operator,rev_date,rev_time) "
					+ "values ('$openid$','$application_num$','$status$','$operator$','$rev_date$','$rev_time$')";
			sql=SqlManager.getSql(sql, sql_map);
			jdbc.update(sql);
		}catch(Exception e){
			logger.error("在线申请登记失败",e);
			throw e;
		}
		logger.info("在线申请登记簿录入成功");
		result_map.put("error", "0000");
		result_map.put("error_msg", "执行成功");
		result.add(result_map);
		return result;
	}
	/**
	 * 导出热销车型信息
	 * */
	public List<Map<String,Object>> exportHotCarInfo(Map<String,String> map){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		try{
			Map<String,String> sql_Map=new HashMap<String,String>();
			String sql="select * from gw_hot_car where 1=1";
			sql=SqlManager.getSql(sql, sql_Map);
			logger.info("sql-----"+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			result=jdbc.queryForList(sql);
			if(result.size()<=0){
				logger.info("查询热销车型记录为空");
				result_map.put("error", "0001");
				result_map.put("error_msg", "未找到符合当前查询的记录。");
				result.add(result_map);
				return result;
			}
			logger.info("查询热销车型记录条数:"+result.size());
		}catch(Exception e){
			logger.error("查询热销车型出现异常",e);
			result_map.put("error", "0001");
			result_map.put("error_msg", "查询车型车型出现异常，请稍后重试");
			result.add(result_map);
			return result;
		}
		return result;
	}
	/**
	 * 导出在线申请信息
	 * */
	public List<Map<String,Object>> exportApplyInfo(Map<String,String> map){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		try{
			String source=(map.get("source")==null||map.get("source")=="")?null:map.get("source").toString().trim();
			String status=(map.get("status")==null||map.get("status")=="")?null:map.get("status").toString().trim();
			String name=(map.get("name")==null||map.get("name")=="")?null:URLDecoder.decode(map.get("name").toString().trim(), "UTF-8");
			String phone=(map.get("phone")==null||map.get("phone")=="")?null:map.get("phone").toString().trim();
			String starttime=(map.get("starttime")==null||map.get("starttime")=="")?null:map.get("starttime").toString().trim();
			String endtime=(map.get("endtime")==null||map.get("endtime")=="")?null:map.get("endtime").toString().trim();
			Map<String,String> sql_Map=new HashMap<String,String>();
			sql_Map.put("source", source);
			sql_Map.put("status", status);
			sql_Map.put("name", name);
			sql_Map.put("phone", phone);
			Map<String,String> time_map=new HashMap<String,String>();
			sql_Map.put("starttime", formatDateForKongjian(starttime));
			sql_Map.put("endtime", formatDateForKongjian(endtime));
			String sql="select a.application_num,a.brand,a.model,a.style,a.first_amt,a.province,"
					+ "a.city,a.franchiser,a.name,a.card_id,a.sex,a.phone,a.status,a.status as dostatus "
					+ ",a.source,a.time,a.apply_date,a.apply_time "
					+ "from ( select *, (case when  length(apply_time)<6 "
					+ "then concat(apply_date,'0',apply_time) "
					+ "when length(apply_time)>=6 then concat(apply_date,apply_time) end )as time "
					+ "from gw_loan_reg ) a where 1=1 "
					+ "{ and a.source='$source$'} { and a.status='$status$' }"
					+ " { and a.name='$name$'} { and a.phone='$phone$'} "
					+ "{ and a.time>=$starttime$ } { and a.time<=$endtime$ }";
			sql=SqlManager.getSql(sql, sql_Map);
			logger.info("sql-----"+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			result=jdbc.queryForList(sql);
			if(result.size()<=0){
				logger.info("查询在线申请记录为空");
				result_map.put("error", "0001");
				result_map.put("error_msg", "未找到符合当前查询的记录。");
				result.add(result_map);
				return result;
			}
			logger.info("查询在线申请记录条数:"+result.size());
		}catch(Exception e){
			logger.error("查询在线申请出现异常",e);
			result_map.put("error", "0001");
			result_map.put("error_msg", "查询在线申请出现异常，请稍后重试");
			result.add(result_map);
			return result;
		}
		return result;
	
	}
	/**
	 * 查询热销车型信息
	 * */
	public Object selectHotCarInfo(Map<String,String> map){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		try{
			String index=map.get("pagenumber");
			String size=map.get("pagesize");
			int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);
			Map<String,String> sql_Map=new HashMap<String,String>();
			sql_Map.put("startnum", startnum+"");
			sql_Map.put("len", size);
			String sql="select * from gw_hot_car where 1=1 limit $startnum$,$len$";
			String count_sql="select * from gw_hot_car where 1=1";
			sql=SqlManager.getSql(sql, sql_Map);
			count_sql=SqlManager.getSql(count_sql, sql_Map);
			logger.info("sql-----"+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			Map<String,Object> listMap=new HashMap<String,Object>();
			List<Map<String,Object>> ll=jdbc.queryForList(sql);
			if(ll.size()<=0){
				logger.info("查询热销车型记录为空");
				listMap.put("rows", ll);
				ll=jdbc.queryForList(count_sql);
				listMap.put("total", ll.size());
				listMap.put("error", "0001");	
				listMap.put("error_msg", "未找到符合当前查询的记录。");
				JSONObject jsonObject = JSONObject.fromObject(listMap);
				logger.info("查询热销车型记录条数:"+ll.size());
				return jsonObject;
			}
			listMap.put("rows", ll);
			ll=jdbc.queryForList(count_sql);
			listMap.put("total", ll.size());
			listMap.put("error", "0000");	
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			logger.info("查询热销车型记录条数:"+ll.size());
			return jsonObject;
		}catch(Exception e){
			logger.error("查询热销车型记录出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("error", "0001");	
			listMap.put("error_msg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
	}
	/**
	 * 查询在线申请信息
	 * */
	public Object selectApplyInfo(Map<String,String> map){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		try{
			String source=(map.get("source")==null||map.get("source")=="")?null:map.get("source").toString().trim();
			String status=(map.get("status")==null||map.get("status")=="")?null:map.get("status").toString().trim();
			String name=(map.get("name")==null||map.get("name")=="")?null:URLDecoder.decode(map.get("name").toString().trim(), "UTF-8");
			String phone=(map.get("phone")==null||map.get("phone")=="")?null:map.get("phone").toString().trim();
			String starttime=(map.get("starttime")==null||map.get("starttime")=="")?null:map.get("starttime").toString().trim();
			String endtime=(map.get("endtime")==null||map.get("endtime")=="")?null:map.get("endtime").toString().trim();
			String index=map.get("pagenumber");
			String size=map.get("pagesize");
			int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);
			Map<String,String> sql_Map=new HashMap<String,String>();
			sql_Map.put("source", source);
			sql_Map.put("status", status);
			sql_Map.put("name", name);
			sql_Map.put("phone", phone);
			sql_Map.put("starttime", formatDateForKongjian(starttime));
			sql_Map.put("endtime", formatDateForKongjian(endtime));
			sql_Map.put("startnum", startnum+"");
			sql_Map.put("len", size);
			String sql="select a.application_num,a.brand,a.model,a.style,a.first_amt,a.province,"
					+ "a.city,a.franchiser,a.name,a.card_id,a.sex,a.phone,a.status,a.status as dostatus "
					+ ",a.source,a.time,a.apply_date,a.apply_time "
					+ "from ( select *, "
					+ "(case when  length(apply_time)=5 then concat(apply_date,'0',apply_time) "
					+ "when length(apply_time)=4 then concat(apply_date,'00',apply_time) "
					+ "when length(apply_time)=3 then concat(apply_date,'000',apply_time) "
					+ "when length(apply_time)=2 then concat(apply_date,'0000',apply_time) "
					+ "when length(apply_time)=1 then concat(apply_date,'00000',apply_time) "
					+ "when length(apply_time)>=6 then concat(apply_date,apply_time) end )as time "
					+ "from gw_loan_reg ) a where 1=1 "
					+ "{ and a.source='$source$'} { and a.status='$status$' }"
					+ " { and a.name='$name$'} { and a.phone='$phone$'} "
					+ "{ and a.time>=$starttime$ } { and a.time<=$endtime$ } order by a.time desc limit $startnum$,$len$";
			String count_sql="select a.application_num,a.brand,a.model,a.style,a.first_amt,a.province,"
					+ "a.city,a.franchiser,a.name,a.card_id,a.sex,a.phone,a.status,a.status as dostatus "
					+ ",a.source,a.time,a.apply_date,a.apply_time "
					+ "from ( select *, (case when  length(apply_time)<6 "
					+ "then concat(apply_date,'0',apply_time) "
					+ "when length(apply_time)>=6 then concat(apply_date,apply_time) end )as time "
					+ "from gw_loan_reg ) a where 1=1 "
					+ "{ and a.source='$source$'} { and a.status='$status$' }"
					+ " { and a.name='$name$'} { and a.phone='$phone$'} "
					+ "{ and a.time>=$starttime$ } { and a.time<=$endtime$ } ";
			sql=SqlManager.getSql(sql, sql_Map);
			count_sql=SqlManager.getSql(count_sql, sql_Map);
			logger.info("sql-----"+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			Map<String,Object> listMap=new HashMap<String,Object>();
			List<Map<String,Object>> ll=jdbc.queryForList(sql);
			if(ll.size()<=0){
				logger.info("查询在线申请记录为空");
				listMap.put("rows", ll);
				ll=jdbc.queryForList(count_sql);
				listMap.put("total", ll.size());
				listMap.put("error", "0001");	
				listMap.put("error_msg", "未找到符合当前查询的记录。");
				JSONObject jsonObject = JSONObject.fromObject(listMap);
				logger.info("查询在线申请记录条数:"+ll.size());
				return jsonObject;
			}
			listMap.put("rows", ll);
			ll=jdbc.queryForList(count_sql);
			listMap.put("total", ll.size());
			listMap.put("error", "0000");	
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			logger.info("查询在线申请记录条数:"+ll.size());
			return jsonObject;
		}catch(Exception e){
			logger.error("查询在线申请出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("error", "0001");	
			listMap.put("error_msg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
		
	}
	
	public String formatDateForKongjian(String datetime){
		if(datetime==null||datetime==""){
			return null;
		}
		datetime=datetime.replace(" ", "");
		datetime=datetime.replaceAll("-", "");
		datetime=datetime.replaceAll(":", "");
		return datetime;
	}
	
	public static void main(String args[]){
		String a="1.00";
		double b=Double.parseDouble(a);
		
		System.out.println(b);
//		String time="2016-12-16 02:03:56";
//		String [] arg=time.split(" ");
//		String date=arg[0];
//		String times=arg[1];
//		System.out.println(date+"====="+times);
	}
}
