package com.gwm.db.dao.callback.impl;

import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.callback.BeforeCallBack;
import com.gwm.db.dao.sql.Sql;
/**
 * 新增修改前初始化基本信息，修改时备份数据
 * @author Administrator
 *
 */
public class BeforeCallBackImpl implements BeforeCallBack {

	@Override
	public Object invoke(Sql<?> sql) {
		switch(sql.getSqlType()){
			/**
			 * insert前初始化id,no,key等信息
			 * 初始化创建时间，ip,创建人等信息
			 */
			case INSERT:{//
				Object pojo = sql.getPojo();
				if(pojo instanceof IEntity){
					//IEntity obj = (IEntity)pojo;
				}
				break;
			}
			/**
			 * update前初始化id,no,key等信息
			 * 初始化创建时间，ip,创建人等信息
			 */
			case UPDATE:{
				//IEntity pojo = (IEntity)sql.getPojo();
				break;
			}
			default:
				break;
			
		}
		return null;
	}

}
