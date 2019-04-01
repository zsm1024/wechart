package com.gwm.common;

public class StringUtils {
	public static String fromTabNameToEntity(String tableName){
		return "com.gwm.db.entity."+tableName;
	}
}
