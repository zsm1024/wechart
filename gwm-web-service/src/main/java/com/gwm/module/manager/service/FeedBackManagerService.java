package com.gwm.module.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_suggest_reg;
import com.gwm.db.entity.meta.Gw_suggest_regMeta;
import com.gwm.engine.Util;

@Service
public class FeedBackManagerService {
	private Logger log = LoggerFactory.getLogger(FeedBackManagerService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	
	public Object selectFBManagerForSub(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		try{
			String openid=map.get("openid");
			inputMap.put("openid", openid);
			String sql="select * from gw_suggest_reg where openid='$openid$' order by submit_time";
			sql=SqlManager.getSql(sql, inputMap);
			log.info("sql:"+sql);
			retList=jdbc.queryForList(sql);
			retMap.put("rows", retList);
			retMap.put("errcode", "0000");	
			retMap.put("errmsg", "当前用户查询意见反馈记录成功");
			JSONObject jsonObject = JSONObject.fromObject(retMap);
			log.info("当前用户查询意见反馈条数:"+"rows=", retList.size());
			return jsonObject;
		}catch(Exception e){
			log.error("当前用户查询意见反馈出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
		
	}
	
	/**
	 * 后台管理-查询意见反馈记录
	 * @param map
	 * @return
	 */
	public Object selectFBManager(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		Map<String,Object> inputMap2=new HashMap<String,Object>();
		try{
			String search_status=map.get("search_status");
			String search_startdate=map.get("search_startdate");
			String search_enddate=map.get("search_enddate");
			String index=map.get("pagenumber");//第几页
			String size=map.get("pagesize");//每页条数
			int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);
			
			String sql=" select r.*  from (select a.openid,max(a.submit_time) as submit_time "
					+ " from ( select * from gw_suggest_reg where 1=1 ";
			String allSql=sql;
			
			if(search_status!=null && !"".equals(search_status.toString()) && !"0".equals(search_status.toString())){
				sql+=" and status='$search_status$' ";
				allSql+=" and status='$search_status$' ";
			}
			if(search_startdate!=null && !"".equals(search_startdate.toString())){
				search_startdate=formatDateForKongjian(search_startdate);
				sql+=" and submit_time>'$search_startdate$' ";
				allSql+=" and submit_time>'$search_startdate$' ";
			}
			if(search_enddate!=null && !"".equals(search_enddate.toString())){
				search_enddate=formatDateForKongjian(search_enddate);
				sql+=" and submit_time<'$search_enddate$' ";
				allSql+=" and submit_time<'$search_enddate$' ";
			}
			sql+=" order by submit_time desc )a "
			+ " group by a.openid limit $startnum$,$size$)  b left "
			+ "join gw_suggest_reg r on r.openid=b.openid and r.submit_time=b.submit_time";
			
			allSql+=" order by submit_time desc )a "
					+ " group by a.openid )  b left "
					+ "join gw_suggest_reg r on r.openid=b.openid and r.submit_time=b.submit_time";
			//查询该条件下所有数据
			
			inputMap.put("search_status", search_status);
			inputMap.put("search_startdate", search_startdate);
			inputMap.put("search_enddate", search_enddate);
			inputMap.put("startnum", startnum);
			inputMap.put("size", size);
			inputMap2.put("search_status", search_status);
			inputMap2.put("search_startdate", search_startdate);
			inputMap2.put("search_enddate", search_enddate);
			
			sql=SqlManager.getSql(sql, inputMap);
			allSql=SqlManager.getSql(allSql, inputMap2);
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			log.info("执行sql: "+allSql);
			allList=jdbc.queryForList(allSql);
			if(retList==null || retList.size()<=0){
				log.info("未找到符合当前查询的记录");
				retMap.put("rows", retList);
				retMap.put("total", allList.size());
				retMap.put("errcode", "0001");	
				retMap.put("errmsg", "未找到符合当前查询的记录");
				JSONObject jsonObject = JSONObject.fromObject(retMap);
				log.info("查询意见反馈条数:"+allList.size());
				return jsonObject;
			}
			retMap.put("rows", retList);
			retMap.put("total", allList.size());
			retMap.put("errcode", "0000");	
			retMap.put("errmsg", "查询意见反馈记录成功");
			JSONObject jsonObject = JSONObject.fromObject(retMap);
			log.info("查询意见反馈条数:"+allList.size());
			return jsonObject;
		}catch(Exception e){
			log.error("查询意见反馈出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
		
	}
	
	/**
	 * 后台管理-更新意见反馈状态
	 * @param map
	 * @return
	 */
	public Map<String, String> updateFBManager(Map<String,String> map){
		try{
			String id=map.get("id");
			String status=map.get("status");
			Gw_suggest_reg updateEntity=null;
			updateEntity=dao.fetch(Gw_suggest_reg.class, Cnd.where(Gw_suggest_regMeta.id.eq(id)));
			if(updateEntity!=null){
				updateEntity.setStatus(status);
				dao.update(updateEntity, Cnd.where(Gw_suggest_regMeta.id.eq(id)));
				log.info("更新状态成功");
				return Util.returnMap("0000", "更新状态成功");
			}
			else{
				log.error("更新状态失败");
				return Util.returnMap("0001", "更新状态失败");
			}
			
		}catch(Exception e){
			log.error("更新意见反馈状态出现异常",e);
			return Util.returnMap("0001", "更新意见反馈状态异常");
		}
		
	}
	
	/**
	 * 后台管理-导出意见反馈excel
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> exportFBManager(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		try{
			String search_status=map.get("search_status");
			String search_startdate=map.get("search_startdate");
			String search_enddate=map.get("search_enddate");
			String sql="select id,status,status as dostatus,submit_time,submit_person,"
					+ "contact_type,suggest_cont,suggest_type from gw_suggest_reg where 1=1 ";
			if(search_status!=null && !"".equals(search_status.toString()) && !"0".equals(search_status.toString())){
				sql+=" and status='$search_status$' ";
			}
			if(search_startdate!=null && !"".equals(search_startdate.toString())){
				search_startdate=formatDateForKongjian(search_startdate);
				sql+=" and submit_time>'$search_startdate$' ";
			}
			if(search_enddate!=null && !"".equals(search_enddate.toString())){
				search_enddate=formatDateForKongjian(search_enddate);
				sql+=" and submit_time<'$search_enddate$' ";
			}
			sql+=" order by submit_time desc";
			inputMap.put("search_status", search_status);
			inputMap.put("search_startdate", search_startdate);
			inputMap.put("search_enddate", search_enddate);
			
			sql=SqlManager.getSql(sql, inputMap);
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			
			if(retList==null || retList.size()<=0){
				log.info("查询意见反馈记录为空");
				retMap.put("errcode", "0001");
				retMap.put("errmsg", "未找到符合当前查询的记录。");
				retList.add(retMap);
				return retList;
			}
			log.info("查询意见反馈条数:"+retList.size());
			return retList;
		}catch(Exception e){
			log.error("查询意见反馈记录异常");
			retMap.put("errcode", "0001");
			retMap.put("errmsg", "查询意见反馈记录异常，请稍后再试");
			retList.add(retMap);
			return retList;
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
	
}
