package com.gwm.db.dao;

import java.sql.Connection;

import com.gwm.db.dao.sql.Sql;
/**
 * 
 * @author kaifa1
 *
 */
public abstract class DaoExecutor {
	public <T> boolean run(Sql<T> sql,Connection con) {
		return true;
	}
	public <T> boolean run(Sql<T> sql) {
		return true;
	}
}
