package com.gwm.module.wechat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.model.ScheduleTask;
import com.wx.engine.HttpProcess;
import com.wx.engine.WxProcess;

@Service
public class AccessTokenErrService {
	
	static long tm = 0;
	
	static String memToken = "";
	
	static Logger log = LoggerFactory.getLogger(AccessTokenErrService.class);
	
	/**
	 * 处理accesstoken 异常
	 * @return
	 */
	synchronized public static String  getAccessToken(){
		long t = System.currentTimeMillis();
		if(t-tm<5*60*1000){
			log.info("5分钟内只处理一次异常情况，直接返回内存中的值");
			return memToken;
		}
		RedisDao redis = SpringUtil.getBean(RedisDao.class);
		String accessToken = null;
		Map<String, String> retMap = null;
		try{
			for(int i = 0; i < 3; i++){
				
				accessToken = redis.getString("access_token");
				retMap = WxProcess.getCallBackIp(accessToken);
				log.info("验证accessToken第["+i+"]次");
				if("0".equals(retMap.get("errcode"))){
					log.info("验证成功，直接返回:"+accessToken);
					return accessToken;
				}
				log.info("验证失败，错误信息:"+retMap);
				Thread.sleep(2000);
			}
			tm = System.currentTimeMillis();
			redis.lock("access_token");
			memToken = ScheduleTask.getAccessToken();
			accessToken = redis.getString("access_token");
			redis.unlock();
			log.info("处理accessToken异常:"+accessToken);
			return accessToken;
		}catch(Exception ex){
			log.error("处理accessToken错误", ex);
			return "error";
		}
	}
}
