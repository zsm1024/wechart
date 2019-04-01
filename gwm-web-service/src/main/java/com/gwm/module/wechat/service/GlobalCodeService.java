package com.gwm.module.wechat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.db.dao.Dao;
@Service
public class GlobalCodeService {
	private Logger log=LoggerFactory.getLogger(GlobalCodeService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	
	@Transactional
	public int replaceAccessInfo()
	{
		log.info("替换数据库中的access_token、jsapi_ticket及其时间");
		int i=0;
		String access_token=redisDao.getString("access_token");
		String jsapi_ticket = redisDao.getString("jsapi_ticket");
		String sql="replace into gw_wx_sys_param (sys_key, sys_value, memo) values ('access_token', '"+access_token+"', 'token')";
		log.info("sql==="+sql);
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		i+=jdbc.update(sql);
		log.info("更新token结果i:"+i);
		sql="replace into gw_wx_sys_param (sys_key, sys_value, memo) values ('jsapi_ticket', '"+jsapi_ticket+"', '微信js接口票据')";
		i+=jdbc.update(sql);
		log.info("更新ticket结果i:"+i);
		String access_token_time=redisDao.getString("access_token_time");
		sql="replace into gw_wx_sys_param (sys_key, sys_value, memo) values ('access_token_time', '"+access_token_time+"', '时间')";
		i+=jdbc.update(sql);
		log.info("更新时间结果i:"+i);
		return i;
	}
}
