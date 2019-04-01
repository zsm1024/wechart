package com.gwm.module.wechat.service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class ChangeContactService{
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	MyLoanService myLoanService = null;
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	
	Logger log = LoggerFactory.getLogger(ChangeContactService.class);
	
	public void process(Map<String, String> map){
		try{
			map.put("openid", map.get("FromUserName"));
			map.put("openId", map.get("FromUserName"));
			Map<String, String> retMap = getBindInfo(map);
			if("0".equals(retMap.get("errcode"))){
				log.info("已绑定");
				String EventKey = map.get("EventKey");
				Map<Object, Object> menuMap = redis.getMap("clickmenumap");
				String url = menuMap.get(EventKey+"_url")+"";
				String authUrl = WxProcess.getBaseAuthUrl(url);
				String content = null;
				Map<String, Object> bindMap = myLoanService.getBindingContract(map);
				if(!"0".equals(bindMap.get("errcode"))){
					log.error("errMsg:"+bindMap);
					content = "未查到合同信息！请联系客服";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return ;
				}
				Map<String, Object> contractMap = (Map<String, Object>)bindMap.get("contract");
				String contract_status = (String)contractMap.get("contract_status");
				String contract_nbr = (String)contractMap.get("contract_nbr");
				if("N".equals(contract_status)){
					log.info("已结清");
					url = redis.getString("repayrecordurl");
					authUrl = WxProcess.getBaseAuthUrl(url);
					content = "您好，您的合同单号"+contract_nbr+"下的债务已经结清，感谢您的支持！\n"
							+ "如需了解还款详情，请";
					content += "<a href='"+authUrl+"'>点击这里</a>";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return ;
				}
				
				String phone = retMap.get("phone");
				content = "您好，您当前预留联系方式为"+phone+"，如需变更，请";
				content += "<a href='"+authUrl+"'>点击这里进行操作</a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9998".equals(retMap.get("errcode"))){
				log.info("未绑定");
				String url = redis.getString("wxbindurl");
				String authUrl = WxProcess.getBaseAuthUrl(url);
				String content = "您好，您暂未绑定账户，无法进行相应操作。";
				content += "<a href='"+authUrl+"'>点击这里，绑定账户</a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else{
				log.info("未知错误");
				String content = "查询失败，请联系客服。";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}
		}catch(Exception ex){
			log.error("查询信息异常", ex);
			String content = "查询失败，请联系客服。";
			wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
		}
	}
	
	public Map<String, String> getBindInfo(Map<String, String> map){
		String openid = map.get("openid");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("openid为空！");
			return Util.returnMap("9999", "用户微信识别号获取失败，请重新打开此页面");
		}
		Object retObj = com.gwm.common.service.Service.getUserLoanInfo(map);
		String retJson = retObj+"";
		Map<String, String> retMap = Util.mapObjectToString(JSON.parseObject(retJson));
		log.info("查询绑定信息："+retMap);
		
		return retMap;
	}
	
	/**
	 * 联系方式变更-获取验证码
	 * @param map
	 * @return
	 */
	public Map<String, String> getCheckCode(Map<String, String> map){
		
		String openid = map.get("openid");
		String ori_phone = map.get("ori_phone");
		String new_phone = map.get("new_phone");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("openid为空！");
			return Util.returnMap("9999", "用户微信识别号获取失败，请重新打开此页面");
		}
		if(StringUtils.isEmpty(new_phone)||"undefined".equals(new_phone)){
			log.info("原手机号为空！");
			return Util.returnMap("9999", "请输入新手机号码");
		}
		Object retObj = com.gwm.common.service.Service.getCheckCode();
		String retJson = retObj+"";
		Map<String, String> retMap = Util.mapObjectToString(JSON.parseObject(retJson));
		log.info("获取验证码返回结果："+retMap);
		
		return retMap;
	}
	
	public Map<String, String> changeContactMode(Map<String, String> map){
		try{
			
			String ori_phone = map.get("ori_phone");
			String openid = map.get("openid");
			String new_phone = map.get("new_phone");
			String check_code = map.get("check_code");
			log.info("开始校验数据");
			if(StringUtils.isEmpty(ori_phone)){
				log.info("原手机号为空");
				return Util.returnMap("9999", "请输入原手机号码");
			}
			if(StringUtils.isEmpty(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "用户微信识别号获取失败，请重新打开此页面");
			}
			if(StringUtils.isEmpty(new_phone)){
				log.info("新手机号为空");
				return Util.returnMap("9999", "请输入变更后的手机号码");
			}
			if(StringUtils.isEmpty(check_code)){
				log.info("验证码为空");
				return Util.returnMap("9999", "请输入验证码");
			}
			if(ori_phone.equals(new_phone)){
				log.info("变更手机号与原手机号相同");
				return Util.returnMap("9999", "变更手机号与原手机号相同");
			}
			String objStr = ""+com.gwm.common.service.Service.changeContactMode();
			Map<String, String> retMap = Util.mapObjectToString(JSONObject.parseObject(objStr));
			log.info("更改用户信息返回结果："+retMap);
			if("0".equals(retMap.get("errcode"))){
				log.info("变更申请提交成功,开始给客户发送微信提醒！");
				String name = "";
				if(retMap.get("user_name")==null||"".equals(retMap.get("user_name"))){
					name = "先生/女士";
				}else{
					name = retMap.get("user_name")+"先生/女士";
				}
				if(retMap.get("template_id") == null || "".equals(retMap.get("template_id"))){
					log.info("模板消息id为空，发文本消息");
					String text = "尊敬的"+name+"：我们已经受理您的联系方式变更申请，"
							+ "为了您更好的使用微信查询办理业务，请于1个工作日后使用变更后手机号绑定微信。"
							+ "客服热线400 6527 606，祝您万事如意！";
					wxMsgDownsideService.textMessageIn(text, openid);
				}else{
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("template", retMap.get("template"));
					tempMap.put("title", retMap.get("title"));
					tempMap.put("msg_type", "1");
					tempMap.put("openid", openid);
					tempMap.put("template_id", retMap.get("template_id"));
					tempMap.put("color", retMap.get("color"));
					tempMap.put("url", getAuthUrl(retMap.get("url")));
					String first = "尊敬的"+name+"：\n您的手机号码变更业务申请成功\n";
					tempMap.put("first", first);
					String keyword1 = "手机号码变更申请";
					tempMap.put("keyword1", keyword1);
					String keyword2 = Util.formatDate(System.currentTimeMillis());
					tempMap.put("keyword2", keyword2.substring(0, 4)+"-"+keyword2.substring(4, 6)+"-"+keyword2.substring(6, 8));
					String remark = "\n☆温馨提示☆ 请您于1个工作日后使用变更后手机号重新绑定微信。";
					tempMap.put("remark", remark);
					Object obj = wxMsgDownsideService.templateMessageIn(tempMap.get("template"), tempMap);
					log.info("发送结果:"+obj);
				}
			}
			
//			return retMap;
			return Util.returnMap(retMap.get("errcode"), retMap.get("errmsg"));
		}catch(Exception ex){
			log.info("更改联系方式异常：", ex);
			return Util.returnMap("9999", "更改信息失败");
		}
	}
	
	private String getAuthUrl(String url){
		String appid = redis.getString("appid");
		String urlEncode = null;
		try{
			urlEncode = URLEncoder.encode(url, "UTF-8");
		}catch(Exception ex){
			urlEncode = url;
		}
		String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid="+appid+"&"
				+ "redirect_uri="+urlEncode+"&"
				+ "response_type=code&"
				+ "scope=snsapi_base&"
				+ "state=true#wechat_redirect";
		return authUrl;
	}
}