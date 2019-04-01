package com.gwm.module.wechat.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class WxMsgDownsideService{
	
	Logger log = LoggerFactory.getLogger(WxMsgDownsideService.class);
	
	public Map<String, String> templateMessageIn(String content, Map<String, String> map){
		return sendTemplateMsg(content, map, "system");
	}
	
	public Map<String, String> textMessageIn(String text, String openid){
		return sendTextMsg(text, openid, "system");
	}
	
	private Map<String, String> sendTextMsg(String text, String openid, String sender){
		Map<String, String> retMap = null;
    	String json = WxProcess.getTextMsg(openid, text);
        try{
            retMap = WxProcess.sendMessage(URLDecoder.decode(json, "utf-8"));
            log.info("发送客服消息："+retMap.toString());
            int index = 3;
            while (!"0".equals(retMap.get("errcode")) && index > 0) {
            	retMap = WxProcess.sendMessage(URLDecoder.decode(json, "utf-8"));
            	log.info("发送客服消息：[重试"+index+"]"+retMap);
                index--;
            }
        }catch(Exception e){
            log.error("发送客服消息异常：", e);
            if(retMap == null){
            	retMap = Util.returnMap("9999", "发送客服消息异常");
            }
        }
        String flag = "0".equals(retMap.get("errcode"))?"1":"0";
        messageSave(json, openid, sender, flag, retMap.get("errmsg"));
        return retMap;
	}
	
	private Map<String, String> sendTemplateMsg(String content, Map<String, String> map, String sender){
		if(map == null || map.isEmpty()){
			return Util.returnMap("9999", "数据错误，请检查！！");
		}
		if(StringUtils.isEmpty(map.get("template_id"))){
			log.info("模板消息id为空");
			return Util.returnMap("9999", "模板消息template_id为空");
		}
		if(StringUtils.isEmpty(map.get("openid"))){
			log.info("微信识别码openid为空");
			return Util.returnMap("9999", "用户id为空！！");
		}
		if(StringUtils.isEmpty(map.get("msg_type"))){
			log.info("消息类型为空");
			return Util.returnMap("9999", "消息类型为空");
		}
		if(StringUtils.isEmpty(content)){
			log.info("模板消息内容为空");
			return Util.returnMap("9999", "模板内容template为空！！！");
		}
		String json = WxProcess.getTemplateMsg(content, map);
		Map<String, String> retMap = null;
		try{
			retMap = WxProcess.sendTemplateMsg(json);
			log.info("发送模板消息："+retMap);
            int index = 3;
            while (!"0".equals(retMap.get("errcode")) && index > 0) {
            	retMap = WxProcess.sendTemplateMsg(json);
        		log.info("发送模板消息：[重试"+index+"]"+retMap);
                index--;
            }
        }catch(Exception e){
            log.error("发送模板异常：", e);
            if(retMap == null){
            	retMap = Util.returnMap("9999", "发送模板消息异常");
            }
        }
		//记录模板消息
		templateSave(map);
		String flag = "0".equals(retMap.get("errcode"))?"1":"0";
		messageSave(json, map.get("openid"), sender, flag, retMap.get("errmsg"));
		return retMap;
	}
	
	private void messageSave(String json, String openid, String sender, String flag, String errmsg){
		String content = json.replace("\'","''");
        if(content.length() > 3000){
        	log.info("消息长度超过字段限制（3000）:"+content);
        	content = "消息长度超过字段限制（3000）,请查看日志";
        }
		Map<String, String> msgMap = new HashMap<String, String>();
        String senttime = Util.formatDate(System.currentTimeMillis());
        msgMap.put("senter",sender);
        msgMap.put("receiver",openid);
        msgMap.put("senttime", ""+senttime);
        msgMap.put("flag", flag);
        msgMap.put("extend",errmsg);
        msgMap.put("content", content);
        log.info("发送消息["+senttime+"]:"+json);
        Object retMsg = com.gwm.common.service.Service.downsideMsgLog(msgMap);
        log.info("记录日志"+retMsg.toString());
	}
	
	private void templateSave(Map<String, String> map){
		Object retMsg = com.gwm.common.service.Service.templateMsgLog(map);
        log.info("记录日志"+retMsg.toString());
	}
}