package com.gwm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.service.Service;
import com.gwm.module.wechat.service.AccessTokenErrService;
import com.wx.engine.WxProcess;

@Configuration
@EnableScheduling
public class ScheduleTask {
	static Logger log = LoggerFactory.getLogger(ScheduleTask.class);
//	@Autowired
//	private static RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
	public ScheduleTask(){
		log.debug("初始化定时任务");
	}
	
	public static void init(){
		//初始化线程
		try{
			new Thread(new Runnable() {
		        public void run() {
		        	checkAccessToken();//生产解开这个注释
		        	timerTask();//生产时注释掉
		            WxTempInterFace.run();
		            getOnlineOpenid();
		        }
		    }).start();
		}catch(Exception ex){
			log.error("任务初始化失败ScheduleTask init fail!", ex);
		}
	}
	
	
	//@Scheduled(fixedRate=1000)
	@Scheduled(cron="0 0/5 * * * ?")//每5分钟校验token有效性
	public static void checkAccessToken() {
		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		log.info("进入5分钟校验token有效性检查");
		try{
			String access_token=redisDao.getString("access_token")==null?"":redisDao.getString("access_token");
			String jsapi_ticket = redisDao.getString("jsapi_ticket")==null?"":redisDao.getString("jsapi_ticket");
			long tnow = System.currentTimeMillis();
			if("".equals(access_token) || "".equals(jsapi_ticket)){
				getAccessToken();
			}
			else{
				/*String access_token_time=redisDao.getString("access_token_time")==null?"":redisDao.getString("access_token_time");
				if("".equals(access_token_time))
				{
					getAccessToken();
				}
				else{
					long s = tnow - Long.valueOf(access_token_time);
					  if (s >= 3600000) {
						  getAccessToken();
					  }
				}*/
				AccessTokenErrService.getAccessToken();
			}
			log.info("退出5分钟校验token有效性检查");
		}catch(Exception e)
		{
			log.error("5分钟校验token有效性出现异常", e);
		}
		
	}
	
	@Scheduled(cron="0 0/5 * * * ?")//每5分钟校验token有效性
	public static void getOnlineOpenid() {
		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		log.info("进入5分钟校验openid在线");
		try{
			Object online=com.gwm.common.service.Service.online();
			List<Map> customers=  JSONObject.parseArray(online.toString(), Map.class);
			if(customers==null || customers.size()==0){
				return ;
			}
			List<String> keys=new ArrayList<String>();
			for(Map m : customers){
				for(Object key :m.keySet()){
					if(key.toString().equals("openid"))
					keys.add(m.get(key).toString());
				}
					
			}
			redisDao.delete("customer_online");
			redisDao.save("customer_online", keys);
			log.info("退出5分钟校验token有效性检查");
		}catch(Exception e)
		{
			log.error("5分钟校验token有效性出现异常", e);
		}
		
	}
	
	@Scheduled(cron="0 0 3 * * ?")//每天3点更新经销商信息
	public static void updateShopInfo(){
		try{
			log.info("更新经销商信息");
			Service.updateShopInfo();
		}catch(Exception ex){
			log.error("更新经销商信息失败", ex);
		}
	}
	
	/**
	 * 获取微信token
	 * */
	public static String getAccessToken()throws Exception {
		log.info("*********获取token开始*********");
		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		String appid=redisDao.getString("appid");
		String appsecret=redisDao.getString("appsecret");
		String s=WxProcess.getAccessToken(appid, appsecret);
		String ticket = WxProcess.getJsapiTicket(s);
		long tnow = System.currentTimeMillis();
        log.info("刷新access_token="+s);
        log.info("刷新jsapi_ticket="+ticket);
        redisDao.save("access_token", s);
        redisDao.save("jsapi_ticket", ticket);
        redisDao.save("access_token_time", tnow+"");
        Object result=Service.replaceAccessInfo();
        if(!"4".equals(result.toString())){
        	log.error("更新数据库token及其时间操作记录条数不为4,请检查："+result.toString());
        }
        log.info("*********获取token结束*********");
        return s;
	}
	
	/**
	 *  系统定时任务扫描
	 * */
	@Scheduled(cron="0 0/3 * * * ?")//每3分钟扫描判读行是否存在要执行的任务
	public static void timerTask() {
//		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		log.info("【timer_task】定时任务扫描开始>>>");
		try{
			Service.getTimerTaskList();
			log.info("【timer_task】定时任务扫描结束>>>");
		}catch(Exception e)
		{
			log.error("【timer_task】定时任务启动出现异常:", e);
		}
		
	}
}
