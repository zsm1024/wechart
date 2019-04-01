package com.gwm.db.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class Entity<T> {
	private String tab_name = "";
	private String history_tab_name="";
	private Class<T> clazz = null;
	private List<EntityField<T>> fields = new ArrayList<EntityField<T>>();

	public List<EntityField<T>> getFields() {
		return fields;
	}

	public void setFields(List<EntityField<T>> fields) {
		this.fields = fields;
	}

	public String getTab_name() {
		return tab_name;
	}

	public String getHistory_tab_name() {
		return history_tab_name;
	}

	public void setHistory_tab_name(String history_tab_name) {
		this.history_tab_name = history_tab_name;
	}

	public void setTab_name(String tab_name) {
		this.tab_name = tab_name;
	}
	public void addField(EntityField<T> f){
		this.fields.add(f);
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	
}
