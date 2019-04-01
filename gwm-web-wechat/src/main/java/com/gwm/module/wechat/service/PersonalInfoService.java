package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class PersonalInfoService {
	Logger log = LoggerFactory.getLogger(PersonalInfoService.class);
	
	public Map<String, String> personalInfo(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取个人信息失败");
		}
		Object obj = com.gwm.common.service.Service.personalInfo(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode").toString())){
			log.info("获取个人信息成功");
			return Util.returnMap("0", "获取个人信息成功",Util.mapObjectToString(msgMap));
		}
		else{
			log.info("获取个人信息失败");
			if(msgMap.get("errmsg")!=null){
				return Util.returnMap("9999", msgMap.get("errmsg").toString());
			}
			return Util.returnMap("9999", "获取个人信息失败");
		}
	}
	
	public Map<String, String> incomeChange(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		String income = map.get("income");//收入范围
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取个人信息失败");
		}
		if(StringUtils.isEmpty(income)){
			log.error("income收入范围为空");
			return Util.returnMap("9999", "收入范围更新失败");
		}
		Object obj = com.gwm.common.service.Service.incomeChange(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("收入范围更新成功");
			return Util.returnMap("0", "收入范围更新成功");
		}
		else{
			return Util.returnMap("9999", msgMap.get("errmsg").toString());
		}
	}
	
	public Map<String, String> zoneChange(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		String select_city = map.get("select_city");//地区
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取个人信息失败");
		}
		if(StringUtils.isEmpty(select_city)){
			log.error("select_city地区更新为空");
			return Util.returnMap("9999", "地区更新失败");
		}
		Object obj = com.gwm.common.service.Service.zoneChange(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("地区更新成功");
			return Util.returnMap("0", "地区更新成功");
		}
		else{
			return Util.returnMap("9999", msgMap.get("errmsg").toString());
		}
	}
	
	
}
