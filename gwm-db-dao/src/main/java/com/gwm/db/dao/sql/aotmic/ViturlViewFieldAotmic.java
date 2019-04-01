package com.gwm.db.dao.sql.aotmic;

import java.util.List;

import com.gwm.db.dao.condition.Col;
import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.entity.View;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.util.Loop;
import com.gwm.db.dao.util.callback.LoopCallBack;

public class ViturlViewFieldAotmic implements Aotmic {
	private View view = null;
	private Col[] columns= null;
	public ViturlViewFieldAotmic(View view,Col[] columns){
		this.view = view;
		this.columns = columns;
	}
	@Override
	public <T> void assembleSql(final StringBuffer sql,Sql<T> sqlInfo) {
		if(columns!=null){
			for(int i = 0;i<columns.length;i++){
				sql.append(columns[i].getTab_name()+"."+columns[i].getCol_name());
				if(i!=columns.length-1){
					sql.append(",");
				}
			}
		}else{
			final List<?> fields = view.getTable1Entity().getFields();
			Loop.loop(fields, new LoopCallBack<EntityField<T>>() {
				@Override
				public void invoke(int index, EntityField<T> obj) {
					sql.append(obj.getName().toUpperCase()+",");
				}
			});
			final List<?> fields2 = view.getTable2Entity().getFields();
			Loop.loop(fields2, new LoopCallBack<EntityField<T>>() {
				@Override
				public void invoke(int index, EntityField<T> obj) {
					sql.append(obj.getName().toUpperCase());
					if(index!=fields.size()-1){
						sql.append(",");
					}
				}
			});
		}
	}

}
