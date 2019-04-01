package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class BindAccountService {
	Logger log = LoggerFactory.getLogger(BindAccountService.class);
	
	public Map<String, String> bindAccount(Map<String, String> map){
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
		Object obj = com.gwm.common.service.Service.bindAccount(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if(msgMap!=null && msgMap.get("errcode")!=null && "0".equals(msgMap.get("errcode"))){
			log.info("绑定成功");
			return Util.returnMap("0", "绑定成功",Util.mapObjectToString(msgMap));
		}
		else{
			log.info("绑定失败");
			if(msgMap.get("errmsg")!=null){

				return Util.returnMap("9999", msgMap.get("errmsg").toString());
			}
			return Util.returnMap("9999", "绑定失败，请稍后再试");
		}
	}

	public Map<String, String> bindStaff(Map<String, String> map){
		Object obj = com.gwm.common.service.Service.bindStaff(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if(msgMap!=null && msgMap.get("errcode")!=null && "0".equals(msgMap.get("errcode"))){
			log.info("绑定成功");
			return Util.returnMap("0", "绑定成功",Util.mapObjectToString(msgMap));
		}
		else{
			log.info("绑定失败");
			if(msgMap.get("errmsg")!=null){

				return Util.returnMap("9999", msgMap.get("errmsg").toString());
			}
			return Util.returnMap("9999", "绑定失败，请稍后再试");
		}
	}
	
	public Map<String, String> sendVerificationCode(Map<String, String> map){
		String openid = map.get("openid");
		String ori_phone = map.get("ori_phone");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("openid为空！");
			return Util.returnMap("9999", "用户微信识别号获取失败，请重新打开此页面");
		}
		if(StringUtils.isEmpty(ori_phone)||"undefined".equals(ori_phone)){
			log.info("手机号为空！");
			return Util.returnMap("9999", "请输入手机号码");
		}
		map.put("flag", "bind");
		Object retObj = com.gwm.common.service.Service.sendVerificationCode(map);
		String retJson = retObj+"";
		Map<String, String> retMap = Util.mapObjectToString(JSON.parseObject(retJson));
		log.info("获取验证码返回结果："+retMap);
		
		return retMap;
	}
	
	public Map<String, String> sendVerificationCodeUn(Map<String, String> map){
		String openid = map.get("openid");
		String ori_phone = map.get("ori_phone");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("openid为空！");
			return Util.returnMap("9999", "用户微信识别号获取失败，请重新打开此页面");
		}
		if(StringUtils.isEmpty(ori_phone)||"undefined".equals(ori_phone)){
			log.info("手机号为空！");
			return Util.returnMap("9999", "请输入手机号码");
		}
		map.put("flag", "unbind");
		Object retObj = com.gwm.common.service.Service.sendVerificationCode(map);
		String retJson = retObj+"";
		Map<String, String> retMap = Util.mapObjectToString(JSON.parseObject(retJson));
		log.info("获取验证码返回结果："+retMap);
		
		return retMap;
	}
}
