package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

@Service
public class MyLoanService {
	Logger log = LoggerFactory.getLogger(MyLoanService.class);
	
	public Map<String, Object> getBindingContract(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		Map<String, Object> returnMap=new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "获取微信账号失败");
			return returnMap;
		}
		Object obj = com.gwm.common.service.Service.getBindingContract(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("获取绑定合同成功");
			return msgMap;
		}
		else{
			log.info("获取绑定合同失败");
			return msgMap;
		}
	}
	
	
}
