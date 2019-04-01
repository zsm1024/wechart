package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

@Service
public class HomeService {
	Logger log = LoggerFactory.getLogger(HomeService.class);
	
	public Map<String, Object> messageNotify(Map<String, String> map){
		log.info("请求参数："+map);
		String openid = map.get("openid");//微信openid
		Map<String, Object> returnMap=new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(openid)){
			log.error("openid为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "获取微信账号失败");
			return returnMap;
		}
		Object obj = com.gwm.common.service.Service.messageNotify(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("获取新消息通知成功");
			return msgMap;
		}
		else{
			log.info("获取新消息通知失败");
			return msgMap;
		}
	}
	
	
}
