package com.gwm.db.dao.sql.aotmic;

import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;

public class StringAotmic implements Aotmic{
	private String message = "";
	public StringAotmic(String message){
		this.message = message;
	}
	@Override
	public <T> void assembleSql(StringBuffer sql,Sql<T> sqlInfo) {
		sql.append(message);
	}

}
