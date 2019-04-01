package com.gwm.db.dao.callback;

import com.gwm.db.dao.sql.Sql;

public interface HistoryCallBack {
	public Object invoke(Sql<?> sql);
}
