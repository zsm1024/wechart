package com.gwm.db.dao.callback.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import com.gwm.db.dao.DaoException;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.callback.AfterCallBack;
import com.gwm.db.dao.sql.Sql;

public class OnePojoCallBackImpl implements AfterCallBack {

	@Override
	public <T> Object invoke(ResultSet rs, Sql<T> sql) {
		try {
				if(sql.getEn()!=null){
					T obj = sql.getEn().getClazz().newInstance();
					if(obj instanceof IEntity){
							IEntity pojo = (IEntity)obj;
							ResultSetMetaData rsm = rs.getMetaData();
							int columnCount = rsm.getColumnCount();
							for(int i = 1;i<=columnCount;i++){
								String val = rs.getString(rsm.getColumnName(i));
								pojo.set(rsm.getColumnName(i).toLowerCase(),val==null?"":val);
							}
							sql.setResult(obj);
					}
				}else if(sql.getView()!=null){
					Map<String,Object> pojo = new HashMap<String,Object>();
					ResultSetMetaData rsm = rs.getMetaData();
					int columnCount = rsm.getColumnCount();
					for(int i = 1;i<=columnCount;i++){
						String val = rs.getString(rsm.getColumnName(i));
						pojo.put(rsm.getColumnName(i).toLowerCase(),val==null?"":val);
					}
					sql.setResultMap(pojo);
				}
			} catch (Exception e) {
				sql.setEx(new DaoException(e));
			}
			
		return null;
	}

}
