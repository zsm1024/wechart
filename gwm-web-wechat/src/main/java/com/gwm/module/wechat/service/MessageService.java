package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class MessageService{
	
	Logger log = LoggerFactory.getLogger(MessageService.class);
	
	/**
	 * 查询我的消息
	 * @param param
	 * @return
	 */
	public Map<String, String> getMyMsg(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String msg_type = param.get("msg_type");
			log.info("openid:"+openid);
			log.info("msg_type:"+msg_type);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			if(StringUtils.isEmpty(msg_type)||"undefined".equals(msg_type)){
				log.info("消息类型为空");
				return Util.returnMap("9999", "未知的消息类型");
			}
			Object obj = com.gwm.common.service.Service.getMyMsg();
			String retStr = (String)obj;
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			return Util.mapObjectToString(retMap);
		}catch(Exception ex){
			log.info("查询我的消息异常");
			return Util.returnMap("9999", "未查到消息");
		}
	}
	
	/**
	 * 查询我的消息类型
	 * @param param
	 * @return
	 */
	public Map<String, String> getMyMsgType(Map<String, String> param){
		try{
			String openid = param.get("openid");
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			Object obj = com.gwm.common.service.Service.getMyMsgType();
			String retStr = (String)obj;
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			return Util.mapObjectToString(retMap);
		}catch(Exception ex){
			log.info("查询我的消息异常");
			return Util.returnMap("9999", "未查到消息");
		}
	}
}