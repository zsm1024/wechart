package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.Gw_wx_user;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.db.entity.meta.Gw_wx_userMeta;
import com.gwm.engine.Util;

@Service
public class WxUpsideService{
	
	Logger log = LoggerFactory.getLogger(WxUpsideService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	
	/**
	 * 微信用户关注
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> userSubscribe(Map<String, String> map){
		
		//判断用户是否关注
		if(map.get("subscribe")==null||"0".equals(map.get("subscribe"))){
			log.info("用户未关注map:"+map);
		}
		Gw_wx_user userQuery = null;
		userQuery = dao.fetch(Gw_wx_user.class, Cnd.where(Gw_wx_userMeta.openid.eq(map.get("openid"))));

		if(userQuery == null){
			log.info("用户初次关注，开始登记");
			
			userQuery = new Gw_wx_user();
			userQuery.setCity(map.get("city"));
			userQuery.setCountry(map.get("country"));
			userQuery.setGroupid(map.get("groupid"));
			userQuery.setHeadimgurl(map.get("headimgurl"));
			userQuery.setLanguage(map.get("language"));
			userQuery.setNickname(map.get("nickname"));
			userQuery.setOpenid(map.get("openid"));
			userQuery.setProvince(map.get("province"));
			userQuery.setSex(map.get("sex"));
			userQuery.setSubscribe_time(map.get("subscribe_time"));
			userQuery.setSubsribe(map.get("subscribe"));
		
			userQuery.setFirst_subscribe_time(map.get("subscribe_time"));
			dao.insert(userQuery);
		}else{
			log.info("用户重新关注，更新用户信息");
			userQuery.setCity(map.get("city"));
			userQuery.setCountry(map.get("country"));
			userQuery.setGroupid(map.get("groupid"));
			userQuery.setHeadimgurl(map.get("headimgurl"));
			userQuery.setLanguage(map.get("language"));
			userQuery.setNickname(map.get("nickname"));
			userQuery.setOpenid(map.get("openid"));
			userQuery.setProvince(map.get("province"));
			userQuery.setSex(map.get("sex"));
			userQuery.setSubscribe_time(map.get("subscribe_time"));
			userQuery.setSubsribe(map.get("subscribe"));
			
			dao.update(userQuery, Cnd.where(Gw_wx_userMeta.openid.eq(map.get("openid"))));
		}
		log.info("关注时校验绑定信息");
		String openid = map.get("openid");
		Gw_user_loan_rel bindEntity = null;
		bindEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
				.where(Gw_user_loan_relMeta.openid.eq(openid))
				.and(Gw_user_loan_relMeta.status.eq("2")));
		if(bindEntity == null){
			log.info("无绑定信息");
		}else{
			log.info("查到绑定信息，开始解绑");
			bindEntity.setStatus("3");
			dao.update(bindEntity, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			log.info("解绑完成");
		}
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("errcode", "0");
		retMap.put("errmsg", "交易成功");
		return retMap;
	}
	/**
	 * 用户取消关注
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> userUnSubscribe(Map<String, String> map){
		try{
			String openid = map.get("FromUserName");
			//更改用户关注状态
			String oriSql = "update gw_wx_user set subsribe='0' where openid='$FromUserName$'";
			String sql = SqlManager.getSql(oriSql, map);
			jdbc.update(sql);
			log.info("取消关注，开始查询是否有绑定信息openid:"+openid);
			Gw_user_loan_rel bindEntity = null;
			bindEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(bindEntity == null){
				log.info("无绑定信息");
			}else{
				log.info("查到绑定信息，开始解绑");
				bindEntity.setStatus("3");
				dao.update(bindEntity, Cnd
						.where(Gw_user_loan_relMeta.openid.eq(openid))
						.and(Gw_user_loan_relMeta.status.eq("2")));
				log.info("解绑完成");
			}
			return Util.returnMap("0", "交易成功");
		}catch(Exception ex){
			log.error("更新用户状态异常", ex);
			return Util.returnMap("9999", "更新用户状态异常");
		}
	}
}