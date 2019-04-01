package com.gwm.module.wechat.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.wx.engine.JSONProcess;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class MenuTempService{
	
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(MenuTempService.class);
	
	public Map<String, String> updateWxMenu(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("father_menu_id", "root");
		String mainStr = (String)com.gwm.common.service.Service.getMenuByParentId(map);
		List<Map<String, Object>> mainList = JSONProcess.jsonToList(mainStr);
		//String menuJson = "{\"button\":[";
		log.info("一级菜单mainList:"+mainList);
		if(mainList.isEmpty()){
			log.error("菜单为空！！！");
			return Util.returnMap("9999", "菜单为空");
		}
		String appid = redis.getString("appid");
		log.info("appid:"+appid);
		List<Map<String, Object>> mainJsonList = new ArrayList<Map<String, Object>>();	
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		for(int i = 0; i < mainList.size(); i++){
			
			Map<String, Object> mainJsonMap = new HashMap<String, Object>();
			Map<String, Object> m = mainList.get(i);
			
			if("0".equals(m.get("menu_type")+"")){
				//有子菜单的主菜单
				map.put("father_menu_id", m.get("id")+"");
				String subStr = (String)com.gwm.common.service.Service.getMenuByParentId(map);
				List<Map<String, Object>> subList = JSONProcess.jsonToList(subStr);
				log.info("二级菜单subList:"+subList);
				
				List<Map<String, Object>> subJsonList = new ArrayList<Map<String,Object>>();
				for(int j = 0; j < subList.size(); j++){
					Map<String, Object> subMap = subList.get(j);
					Map<String, Object> subJsonMap = new HashMap<String, Object>();
					
					switch(subMap.get("menu_type")+""){
					
					case "1":
						//普通链接子菜单
						log.info("普通链接："+subMap.get("menu_url"));
						subJsonMap.put("type", "view");
						subJsonMap.put("name", subMap.get("menu_content"));
						subJsonMap.put("url", subMap.get("menu_url"));
						subJsonList.add(subJsonMap);
						break;
					case "2":
						//基础授权链接子菜单(只获取openid)
						log.info("基础授权:"+subMap.get("menu_url"));
						subJsonMap.put("type", "view");
						subJsonMap.put("name", subMap.get("menu_content"));
						String url = subMap.get("menu_url")+"";
						String urlEncode = null;
						try{
							urlEncode = URLEncoder.encode(url, "UTF-8");
						}catch(Exception ex){
							log.info("编码错误，不加载此菜单");
							break;
						}
						String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
								+ "appid="+appid+"&"
								+ "redirect_uri="+urlEncode+"&"
								+ "response_type=code&"
								+ "scope=snsapi_base&"
								+ "state=true#wechat_redirect";
						log.info("基础授权链接："+authUrl);
						subJsonMap.put("url", authUrl);
						subJsonList.add(subJsonMap);
						break;
					case "3":
						//授权页面链接子菜单(获取用户所有信息[包括关注用户和未关注用户])
						log.info("页面授权:"+subMap.get("menu_url"));
						subJsonMap.put("type", "view");
						subJsonMap.put("name", subMap.get("menu_content"));
						String url_3 = subMap.get("menu_url")+"";
						String urlEncode_3 = null;
						try{
							urlEncode_3 = URLEncoder.encode(url_3, "UTF-8");
						}catch(Exception ex){
							log.info("编码错误，不加载此菜单");
							break;
						}
						String authUrl_3 = "https://open.weixin.qq.com/connect/oauth2/authorize?"
								+ "appid="+appid+"&"
								+ "redirect_uri="+urlEncode_3+"&"
								+ "response_type=code&"
								+ "scope=snsapi_userinfo&"
								+ "state=true#wechat_redirect";
						log.info("页面授权链接："+authUrl_3);
						subJsonMap.put("url", authUrl_3);
						subJsonList.add(subJsonMap);
						break;
					case "4":
						//点击菜单
						log.info("点击菜单:"+subMap.get("menu_key"));
						subJsonMap.put("type", "click");
						subJsonMap.put("key", subMap.get("menu_key"));
						subJsonMap.put("name", subMap.get("menu_content"));
						subJsonList.add(subJsonMap);
						break;
					default:
						log.info("未知的menu_type类型！");
						break;
					}
				}
				
				mainJsonMap.put("name", m.get("menu_content"));
				mainJsonMap.put("sub_button", subJsonList);
			}else if("1".equals(m.get("menu_type")+"")){
				//普通链接主菜单
				mainJsonMap.put("type", "view");
				mainJsonMap.put("name", m.get("menu_content"));
				mainJsonMap.put("url", m.get("menu_url"));
			}else if("2".equals(m.get("menu_type"))){
				//授权链接主菜单
			}
			mainJsonList.add(mainJsonMap);
		}
		jsonMap.put("button", mainJsonList);
		log.info("mapJson:"+JSON.toJSONString(jsonMap));
		Map<String, String> retMap = WxProcess.createMenu(JSON.toJSONString(jsonMap));
		log.info("创建菜单："+retMap.toString());
		return retMap;
	}
}