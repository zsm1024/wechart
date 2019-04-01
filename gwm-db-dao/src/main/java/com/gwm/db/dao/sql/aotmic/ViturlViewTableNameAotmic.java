package com.gwm.db.dao.sql.aotmic;

import com.gwm.db.dao.entity.Entity;
import com.gwm.db.dao.entity.View;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;

public class ViturlViewTableNameAotmic implements Aotmic {
	private View view = null;
	public ViturlViewTableNameAotmic(View view){
		this.view = view;
	}
	@Override
	public <T> void assembleSql(final StringBuffer sql, Sql<T> sqlInfo) {
		Entity<?> tab1 = view.getTable1Entity();
		Entity<?> tab2 = view.getTable2Entity();
		sql.append(tab1.getTab_name()+","+tab2.getTab_name());
	}
}
