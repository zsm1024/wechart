package com.gwm.db.dao.callback;

import java.sql.ResultSet;

import com.gwm.db.dao.sql.Sql;

public interface AfterCallBack {
	public <T> Object invoke(ResultSet rs,Sql<T> sql);
}
