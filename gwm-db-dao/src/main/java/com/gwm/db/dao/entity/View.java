package com.gwm.db.dao.entity;

public class View {
	private Class<?> table1;
	private Entity<?> table1Entity;
	private Class<?> table2;
	private Entity<?> table2Entity;
	public Class<?> getTable1() {
		return table1;
	}
	public void setTable1(Class<?> table1) {
		this.table1 = table1;
	}
	public Entity<?> getTable1Entity() {
		return table1Entity;
	}
	public void setTable1Entity(Entity<?> table1Entity) {
		this.table1Entity = table1Entity;
	}
	public Class<?> getTable2() {
		return table2;
	}
	public void setTable2(Class<?> table2) {
		this.table2 = table2;
	}
	public Entity<?> getTable2Entity() {
		return table2Entity;
	}
	public void setTable2Entity(Entity<?> table2Entity) {
		this.table2Entity = table2Entity;
	}

}
