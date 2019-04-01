package com.gwm.db.dao.callback;

import com.gwm.db.dao.sql.Sql;

public interface BeforeCallBack {
	public Object invoke(Sql<?> sql);
}
