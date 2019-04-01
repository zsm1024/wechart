package com.gwm.module.wechat.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_history_news_reg;
import com.gwm.db.entity.meta.Gw_history_news_regMeta;
import com.gwm.engine.Util;

@Service
public class HomeService{
	
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	Dao dao = null;
	Logger log = LoggerFactory.getLogger(HomeService.class);
	
	@Transactional
	public Map<String, String> messageNotify(Map<String, String> param){
		try{
			String openid = param.get("openid");
			
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.error("用户编号openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			
			List<Gw_history_news_reg> list=dao.list(Gw_history_news_reg.class, 
					Cnd.where(Gw_history_news_regMeta.openid.eq(openid)).and(Gw_history_news_regMeta.status.eq("0")));
			if(list!=null && list.size()>0){
				log.info("有未阅读的消息通知");
				return Util.returnMap("0", "有未阅读的消息通知");
			}
			else{
				log.info("没有未阅读的消息通知");
				return Util.returnMap("9999", "没有未阅读的消息通知");
			}
		}catch(Exception ex){
			log.error("获取新消息通知异常");
			return Util.returnMap("9999", "获取新消息通知异常");
		}
	}
	
	
}