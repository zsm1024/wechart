package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.gwm.common.SqlManager;
import com.gwm.engine.Util;

@Service
public class SMessageService{
	
	@Autowired
	JdbcTemplate jdbc = null;
	
	Logger log = LoggerFactory.getLogger(SMessageService.class);
	
	@Transactional
	public Map<String, String> getMsg(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String msg_type = param.get("msg_type");
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("用户编号openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("消息类型为空");
				return Util.returnMap("9999", "未查到消息");
			}
			String oriSql = "select * from gw_history_news_reg where openid='$openid$' and msg_type='$msg_type$' order by concat(msg_date,msg_time) desc";
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("openid", openid);
			sqlMap.put("msg_type", msg_type);
			String sql = SqlManager.getSql(oriSql, sqlMap);
			List<Map<String, Object>> retList = jdbc.queryForList(sql);
			if(!retList.isEmpty()){
				Map<String, Object> map = retList.get(0);
				String dt = map.get("msg_date")+"";
				String tm = map.get("msg_time")+"";
				String dtTm = dt+tm;
				oriSql = "update gw_history_news_reg set status='1' where openid='$openid$' and msg_type='$msg_type$' and concat(msg_date,msg_time)<='$dttm$'";
				sqlMap.put("dttm", dtTm);
				sql = SqlManager.getSql(oriSql, sqlMap);
				jdbc.update(sql);
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("msgs", JSON.toJSONString(retList));
			log.info("我的消息:"+retMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		}catch(Exception ex){
			log.error("查询我的消息异常");
			return Util.returnMap("9999", "未查到消息");
		}
	}
	
	public Map<String, String> getMsgType(Map<String, String> param){
		try {
			String openid = param.get("openid");
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			String oriSql = "select msg_date,status from gw_history_news_reg where openid='$openid$' and msg_type='$msg_type$' order by concat(msg_date,msg_time) desc";
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("openid", openid);
			Map<String, Object> hisMap = null;
			List<Map<String, Object>> hisList = null;
			String sql = "";
			sqlMap.put("msg_type", "1");
			sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("查询通知公告:"+sql);
			hisList = jdbc.queryForList(sql);
			if(hisList.isEmpty()){
				hisMap = null;
			}else{
				hisMap = hisList.get(0);
			}
			if(hisMap==null){
				hisMap = new HashMap<String, Object>();
				hisMap.put("num", "0");
			}else{
				hisMap.put("num", "1");
			}
			retMap.put("notification", JSON.toJSONString(hisMap));
			
			sqlMap.put("msg_type", "2");
			sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("还款通知公告:"+sql);
			hisList = jdbc.queryForList(sql);
			if(hisList.isEmpty()){
				hisMap = null;
			}else{
				hisMap = hisList.get(0);
			}
			if(hisMap==null){
				hisMap = new HashMap<String, Object>();
				hisMap.put("num", "0");
			}else{
				hisMap.put("num", "1");
			}
			retMap.put("repayment", JSON.toJSONString(hisMap));
			
			sqlMap.put("msg_type", "3");
			sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("逾期提醒公告:"+sql);
			hisList = jdbc.queryForList(sql);
			if(hisList.isEmpty()){
				hisMap = null;
			}else{
				hisMap = hisList.get(0);
			}
			if(hisMap==null){
				hisMap = new HashMap<String, Object>();
				hisMap.put("num", "0");
			}else{
				hisMap.put("num", "1");
			}
			retMap.put("overdue", JSON.toJSONString(hisMap));
			
			sqlMap.put("msg_type", "4");
			sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("查询系统消息:"+sql);
			hisList = jdbc.queryForList(sql);
			if(hisList.isEmpty()){
				hisMap = null;
			}else{
				hisMap = hisList.get(0);
			}
			if(hisMap==null){
				hisMap = new HashMap<String, Object>();
				hisMap.put("num", "0");
			}else{
				hisMap.put("num", "1");
			}
			retMap.put("system", JSON.toJSONString(hisMap));
			log.info("我的消息类型:"+retMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		} catch (Exception ex) {
			log.error("查询消息类型异常", ex);
			return Util.returnMap("9999", "查询失败");
		}
	}
}