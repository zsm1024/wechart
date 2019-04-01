package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class AccountSettingService {
	Logger log = LoggerFactory.getLogger(AccountSettingService.class);
	
	public Map<String, String> removeBindAccount(Map<String, String> map){
		log.info("请求参数："+map);
		String idType = map.get("idType");//证件类型
		String idNumber = map.get("idNumber");//证件号码
		String reservePhone = map.get("reservePhone");//预留手机号
		String validateCode = map.get("validateCode");//验证码
		
		if(StringUtils.isEmpty(idType)){
			log.error("证件类型为空");
			return Util.returnMap("9999", "证件类型为空");
		}else if(StringUtils.isEmpty(idNumber)){
			log.error("证件号码为空");
			return Util.returnMap("9999", "证件号码为空");
		}else if(StringUtils.isEmpty(reservePhone)){
			log.error("预留手机号为空");
			return Util.returnMap("9999", "预留手机号为空");
		}else if(StringUtils.isEmpty(validateCode)){
			log.error("验证码为空");
			return Util.returnMap("9999", "验证码为空");
		}
		Object obj = com.gwm.common.service.Service.removeBindAccount(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if(msgMap!=null && msgMap.get("errcode")!=null && "0".equals(msgMap.get("errcode"))){
			log.info("解绑成功");
			return Util.returnMap("0", "解绑成功",Util.mapObjectToString(msgMap));
		}
		else{
			log.info("解绑失败");
			if(msgMap.get("errmsg")!=null){
				return Util.returnMap("9999", msgMap.get("errmsg").toString());
			}
			return Util.returnMap("9999", "解绑失败");
		}
	}
	
	public Map<String, String> feedBack(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//openId
		String suggest_type = map.get("suggest_type");//反馈类型
		String suggest_cont = map.get("suggest_cont");//反馈信息
		String contact_type = map.get("contact_type");//联系方式
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取个人信息失败");
		}else if(StringUtils.isEmpty(suggest_type)){
			log.error("证件类型为空");
			return Util.returnMap("9999", "反馈类型为空");
		}else if(StringUtils.isEmpty(suggest_cont)){
			log.error("反馈信息为空");
			return Util.returnMap("9999", "反馈信息为空");
		}else if(StringUtils.isEmpty(contact_type)){
			log.error("联系方式为空");
			return Util.returnMap("9999", "联系方式为空");
		}
		Object obj = com.gwm.common.service.Service.feedBack(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("反馈信息提交成功");
			return Util.returnMap("0", "反馈信息提交成功",Util.mapObjectToString(msgMap));
		}
		else{
			log.info("反馈信息提交失败");
			if(msgMap.get("errmsg")!=null){
				return Util.returnMap("9999", msgMap.get("errmsg").toString());
			}
			return Util.returnMap("9999", "反馈信息提交失败");
		}
	}
	
}
