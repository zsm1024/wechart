package com.gwm.db.dao.sql.aotmic;

import java.util.List;

import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.util.Loop;
import com.gwm.db.dao.util.callback.LoopCallBack;

public class InsertValuesAotmic implements Aotmic {
	@Override
	public <T> void assembleSql(final StringBuffer sql, Sql<T> sqlInfo) {
		sql.append(" (");
		final List<EntityField<T>> fields = sqlInfo.getEn().getFields();
		Loop.loop(fields, new LoopCallBack<EntityField<T>>() {
			@Override
			public void invoke(int index, EntityField<T> obj) {
				sql.append("?");
				if(index!=fields.size()-1){
					sql.append(",");
				}
			}
		});
		sql.append(")");
	}
	
}
