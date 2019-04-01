package com.gwm.db.dao.sql;


public interface Aotmic {
	public <T> void assembleSql(final StringBuffer sql,Sql<T> sqlInfo);
}
