package com.gwm.module.wechat.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.gwm.db.entity.Gw_wx_user;
import com.gwm.db.entity.meta.Gw_wx_userMeta;
import com.gwm.engine.Util;

@Service
public class WxUserInfoService{
	
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	Dao dao = null;
	Logger log = LoggerFactory.getLogger(WxUserInfoService.class);
	
	/**
	 * 获取微信用户信息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> getWxUserInfo(Map<String, String> param){
		try{
			String openid = param.get("openid");
			
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "获取微信用户信息失败");
			}
			Gw_wx_user queryEntity=null;
			queryEntity=dao.fetch(Gw_wx_user.class, 
					Cnd.where(Gw_wx_userMeta.openid.eq(openid)).and(Gw_wx_userMeta.subsribe.eq("1")));
			if(queryEntity!=null){
				Date date=new Date();
				DateFormat format=new SimpleDateFormat("yyyyMMdd");
				String checkDate=format.format(date);
				if(queryEntity.getLatest_date()!=null && queryEntity.getLatest_date().equals(checkDate)){
					log.info("获取微信用户信息成功");
					Map<String, String> map=new HashMap<String, String>();
					map.put("openid", queryEntity.getOpenid());
					map.put("subsribe", queryEntity.getSubsribe());
					map.put("pwid", queryEntity.getPwid());
					map.put("nickname", queryEntity.getNickname());
					map.put("sex", queryEntity.getSex());
					map.put("city", queryEntity.getCity());
					map.put("country", queryEntity.getCountry());
					map.put("province", queryEntity.getProvince());
					map.put("language", queryEntity.getLanguage());
					map.put("headimgurl", queryEntity.getHeadimgurl());
					map.put("first_subscribe_time", queryEntity.getFirst_subscribe_time());
					map.put("subscribe_time", queryEntity.getSubscribe_time());
					map.put("groupid", queryEntity.getGroupid());
					map.put("latest_date", queryEntity.getLatest_date());

					return Util.returnMap("0", "获取微信用户信息成功",map);
				}
				else{
					log.info("数据库中的用户信息已过时效");
					return Util.returnMap("9999", "数据库中的用户信息已过时效");
				}
			}
			else{
				return Util.returnMap("9999", "尚未关注");
			}
		}catch(Exception ex){
			log.error("获取微信用户信息异常");
			return Util.returnMap("9999", "获取微信用户信息异常");
		}
	}
	
	/**
	 * 更新微信用户信息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> updateWxUserInfo(Map<String, String> param){
		try{
			Gw_wx_user updateEntity=null;
			updateEntity=dao.fetch(Gw_wx_user.class, 
					Cnd.where(Gw_wx_userMeta.openid.eq(param.get("openid"))));
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyyMMdd");
			String checkDate=format.format(date);
			log.info(checkDate);
			if(updateEntity!=null){
				updateEntity.setNickname(param.get("nickname"));
				updateEntity.setSex(param.get("sex"));
				updateEntity.setCity(param.get("city")!=null?param.get("city"):"");
				updateEntity.setCountry(param.get("country")!=null?param.get("country"):"");
				updateEntity.setProvince(param.get("province")!=null?param.get("province"):"");
				updateEntity.setLanguage(param.get("language")!=null?param.get("language"):"");
				updateEntity.setHeadimgurl(param.get("headimgurl")!=null?param.get("headimgurl"):"");
				updateEntity.setSubscribe_time(param.get("subscribe_time"));
				updateEntity.setGroupid(param.get("groupid"));
				updateEntity.setLatest_date(checkDate);
				dao.update(updateEntity, Cnd.where(Gw_wx_userMeta.openid.eq(param.get("openid"))));
				log.info("更新微信用户信息成功");
				return Util.returnMap("0", "更新微信用户信息成功");
			}
			else{
				Gw_wx_user insertEntity=new Gw_wx_user();
				insertEntity.setNickname(param.get("nickname"));
				insertEntity.setSex(param.get("sex"));
				insertEntity.setCity(param.get("city")!=null?param.get("city"):"");
				insertEntity.setCountry(param.get("country")!=null?param.get("country"):"");
				insertEntity.setProvince(param.get("province")!=null?param.get("province"):"");
				insertEntity.setLanguage(param.get("language")!=null?param.get("language"):"");
				insertEntity.setHeadimgurl(param.get("headimgurl")!=null?param.get("headimgurl"):"");
				insertEntity.setSubscribe_time(param.get("subscribe_time"));
				insertEntity.setGroupid(param.get("groupid"));
				insertEntity.setLatest_date(checkDate);
				insertEntity.setOpenid(param.get("openid"));
				dao.insert(insertEntity);
				log.info("更新微信用户信息成功");
				return Util.returnMap("0", "更新微信用户信息成功");
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
			log.error("更新微信用户信息失败");
			return Util.returnMap("9999", "更新微信用户信息失败");
		}
	}
	
	
}