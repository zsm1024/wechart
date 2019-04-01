package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class WxAuthorizeService{
	
	@Autowired
	RedisDao redis = null;
	
	Logger log = LoggerFactory.getLogger(WxAuthorizeService.class);
	
	/**
	 * 根据code获取用户信息
	 * map.flag=true时获取详细信息，否则只返回openid
	 * @param map
	 * @return
	 */
	public Map<String, String> snsapiBase(Map<String, String> map){
		
		if(StringUtils.isEmpty(map.get("code"))){
			log.error("基础授权code为空");
			return Util.returnMap("9999", "参数错误（code值为空）");
		}
		Map<String, String> retMap = WxProcess.getOauthAccessToken(map.get("code"));
		if(!"0".equals(retMap.get("errcode"))){
			log.error("获取openid错误:"+retMap);
			return retMap;
		}
		log.info("获取标志（flag为true时获取详细信息，否则只返回openid）："+map.get("flag"));
		if(map.get("flag") == null || !"true".equals(map.get("flag"))){
			log.info("只获取openid："+retMap);
			return retMap;
		}
		log.info("获取用户详细信息");
		Map<String, String> userMap = WxProcess.getUserInfo(retMap.get("openid"));
		if("0".equals(userMap.get("subscribe"))){
			log.info("用户未关注openid:"+userMap.get("openid")+"(已取消关注或从未关注)");
			userMap = Util.returnMap("9999", "未关注该公众号", userMap);
		}
		log.info("用户信息："+userMap);
		
		return userMap;
	}
	
	/**
	 * 根据openid获取用户信息
	 * @param map
	 * @return
	 */
	public Map<String, String> getWxUserInfo(Map<String, String> map){
		
		if(StringUtils.isEmpty(map.get("openid"))){
			log.error("openid为空");
			return Util.returnMap("9999", "参数错误（openid值为空）");
		}
		log.info("获取用户详细信息");
		Object obj = com.gwm.common.service.Service.getWxUserInfo(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if(msgMap.get("errcode").equals("0")){
			log.info("用户信息"+msgMap);
			return Util.mapObjectToString(msgMap);
		}
		else{
			Map<String, String> userMap = WxProcess.getUserInfo(map.get("openid"));
			if("0".equals(userMap.get("subscribe"))){
				log.info("用户未关注openid:"+userMap.get("openid")+"(已取消关注或从未关注)");
				userMap = Util.returnMap("9999", "未关注该公众号", userMap);
			}
			Object objt = com.gwm.common.service.Service.updateWxUserInfo(userMap);
			String returnJson=(String)objt;//返回的map信息
			Map<String, Object> messageMap = JSONObject.parseObject(returnJson);
			if(messageMap.get("errcode").equals("0")){
				log.info("用户信息："+userMap);
				return userMap;
			}
			else{
				log.info("更新微信用户信息失败");
				return Util.returnMap("9999", "更新微信用户信息失败");
			}
		}
		
	}
}