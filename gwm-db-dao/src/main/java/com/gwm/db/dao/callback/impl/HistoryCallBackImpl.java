package com.gwm.db.dao.callback.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.gwm.db.dao.callback.HistoryCallBack;
import com.gwm.db.dao.sql.Sql;

/**
 * 新增修改前初始化基本信息，修改时备份数据
 * @author Administrator
 *
 */
public class HistoryCallBackImpl implements HistoryCallBack {

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		//this.jdbcTemplate = jdbcTemplate;
	}

	//private JdbcTemplate jdbcTemplate;

	@Override
	public Object invoke(Sql<?> sql) {
		switch(sql.getSqlType()){
			/**
			 * insert后insert历史表
			 */
			case INSERT:{
			}
			/**
			 * update后insert历史表
			 */
			case UPDATE:{
				/*IEntity pojo = (IEntity)sql.getPojo();
				if(pojo.isHasHistory()){
					jdbcTemplate.update("insert into "+sql.getEn().getHistory_tab_name()+" select * from "+sql.getEn().getTab_name()+" where id=?",pojo.get("id"));
				}*/
				break;
			}
			default:
				break;

		}
		return null;
	}
}
