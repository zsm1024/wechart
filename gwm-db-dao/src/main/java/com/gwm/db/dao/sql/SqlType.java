package com.gwm.db.dao.sql;

public enum SqlType {
	  /**
	   * 单表单条记录查询
	   */
	  SELECT_ENTITY,
	  /**
	   * 单表多条记录查询
	   */
	  SELECT_LIST,
	  /**
	   * 多表多条记录查询
	   */
	  SELECT_MANY, 
	  /**
	   * 删除
	   */
	  DELETE,
	  /**
	   * 更新
	   */
	  UPDATE,
	  /**
	   * 插入实体
	   */
	  INSERT,
	  /**
	   * 批量插入
	   */
	  BATCH,
	  /**
	   * 统计记录数
	   */
	  COUNT
}
