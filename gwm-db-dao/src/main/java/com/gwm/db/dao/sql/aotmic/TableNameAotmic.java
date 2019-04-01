package com.gwm.db.dao.sql.aotmic;

import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;

public class TableNameAotmic implements Aotmic {

	@Override
	public <T> void assembleSql(final StringBuffer sql, Sql<T> sqlInfo) {
		sql.append(" "+sqlInfo.getEn().getTab_name()+" ");
	}

}
