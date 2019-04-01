package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gwm.common.SqlManager;

@Service
public class WxMenuService{
	
	@Autowired
	JdbcTemplate jdbc = null;
	
	Logger log = LoggerFactory.getLogger(WxMenuService.class);
	
	public List<Map<String, Object>> getMenuByParentId(String id){
		try{
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("id", id);
			String oriSql = "select * from gw_wx_menu where father_menu_id='$id$' order by menu_order";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			return list;
		}catch(Exception ex){
			log.error("查询菜单异常", ex);
			return null;
		}
	}
	
}