package com.gwm.db.dao.sql.aotmic;

import com.gwm.db.dao.DaoConstant;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;

public class ForUpdateAotmic implements Aotmic {

	@Override
	public <T> void assembleSql(final StringBuffer sql, Sql<T> sqlInfo) {
		switch (DaoConstant.DB_TYPE){
			case ORACLE:{
				sql.append(" FOR UPDATE WAIT 5 ");
				break;
			}
			case INFORMIX:{
				sql.append(" FOR UPDATE ");
				break;
			}
			default:
				break;
		}
	}

}
