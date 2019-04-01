package com.gwm.module.wechat.service;

import java.lang.reflect.Method;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.db.dao.Dao;
import com.gwm.module.wechat.WxTimerRunUtil;
@Service
public class TimerTaskService {
	private Logger log=LoggerFactory.getLogger(TimerTaskService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	
	@Transactional
	public void getTimerTaskList()
	{
		try{
			log.info("******扫描定时任务表开始******");
			List<Map<String,Object>> lists=null;
			Format format = new SimpleDateFormat("yyyyMMddHHmmss");
			String sysTime=format.format(new Date());
			String sql="select * from gw_wx_timer_task where flag='1' and execute_time<='"+sysTime+"' and execute_flag='0'";
			log.info("sql==="+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			lists=jdbc.queryForList(sql);
			log.info("需要执行的定时任务【"+lists.size()+"】个");
			for(final Map<String,Object> map:lists){
	        	new Thread(new Runnable() { 
	        		public void run() {
	                	WxTimerRunUtil.run(map);
	                }
	            }).start();
	        }
			log.info("******扫描定时任务表结束******");
		}catch(Exception e){
			log.info("扫描定时任务出现异常",e);
		}
	}
}
