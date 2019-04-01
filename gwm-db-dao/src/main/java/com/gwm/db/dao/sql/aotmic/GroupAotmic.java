package com.gwm.db.dao.sql.aotmic;

import java.util.ArrayList;
import java.util.List;

import com.gwm.db.dao.condition.Group;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;

public class GroupAotmic implements Aotmic {
	private Group group = null;
	private List<Aotmic> items = new ArrayList<Aotmic>();
	@Override
	public <T> void assembleSql(final StringBuffer sql, Sql<T> sqlInfo) {
		 sql.append(group.getKey()+" (");
		 for(Aotmic aotmic:items){
			 aotmic.assembleSql(sql, sqlInfo);
		 }
		 sql.append(") ");
	}
	public void addAotmic(Aotmic aotmic){
		items.add(aotmic);
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public List<Aotmic> getItems() {
		return items;
	}
	public void setItems(List<Aotmic> items) {
		this.items = items;
	}
	
}
