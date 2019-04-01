package com.gwm.module.wechat.service;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gwm.common.RedisDao;

@Service
public class UploadService {
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	MyLoanService myLoanService = null;
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	
	Logger log = LoggerFactory.getLogger(UploadService.class);
	
	public void process(Map<String, String> map){
		try{
			log.info("map:"+map);
			map.put("openId", map.get("FromUserName"));
			if(map.get("FromUserName")!=null||map.get("FromUserName")!=""){
				String content = "敬请期待！";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
				log.info("发送成功");
				return ;
			}else{
				log.info("openid为空");
			}
		}catch(Exception e){
			log.error("发送客服消息异常",e);
		}
	}
}
