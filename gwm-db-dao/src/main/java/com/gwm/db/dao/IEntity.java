package com.gwm.db.dao;

public abstract class IEntity {
	// private String sql = null;//多表查询sql
	public <T> void set(String col_name,T value){}
	public  Object get(String col_name){return null;}
}
