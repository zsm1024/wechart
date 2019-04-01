package com.gwm.db.dao;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwm.common.StringUtils;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.dao.condition.Col;
import com.gwm.db.dao.entity.Entity;
import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.entity.View;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.sql.aotmic.InsertFieldAotmic;
import com.gwm.db.dao.sql.aotmic.InsertValuesAotmic;
import com.gwm.db.dao.sql.aotmic.StringAotmic;
import com.gwm.db.dao.sql.aotmic.TableNameAotmic;
import com.gwm.db.dao.sql.aotmic.UpdateFieldAotmic;
import com.gwm.db.dao.sql.aotmic.ViturlViewFieldAotmic;
import com.gwm.db.dao.sql.aotmic.ViturlViewTableNameAotmic;
import com.gwm.db.dao.sql.aotmic.WhereByIdAotmic;
import com.gwm.db.dao.sql.aotmic.WhereByNoAotmic;

public class SqlMaker {
	private Logger logger = LoggerFactory.getLogger(SqlMaker.class);
	private Map<Class<?>,Object> entityInfo = null;
	public SqlMaker(){
		entityInfo = new ConcurrentHashMap<Class<?>,Object>();
	}
	/**
	 * 功能描述：取得实体信息
	 * @param clazz
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	@SuppressWarnings("unchecked")
 	public <T> Entity<T> getEntity(String tablename){
		Class<T> clazz = null;
		try {
			logger.debug(StringUtils.fromTabNameToEntity(tablename));
			clazz = (Class<T>) Class.forName(StringUtils.fromTabNameToEntity(tablename));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("未能找到对应实体类"+tablename);
		}
		Entity<T> en = getEntity(clazz);
		return en;
	}
	/**
	 * @Description: 取得表字段信息
	 * @param table_name
	 * @param col_name
	 * @return    
	 * @return: EntityField<T>    
	 * @throws 
	 * @author：tianming
	 * @2016年8月10日下午12:36:27
	 */
	public <T> EntityField<T> getEntityField(String table_name,String col_name){
		Entity<T> en = this.getEntity(table_name);
		for(int i = 0;i<en.getFields().size();i++){
			EntityField<T> field = en.getFields().get(i);
			if(field.getName().equalsIgnoreCase(col_name)){
				return field;
			}
		}
		return null;
	}
	/**
	 * 功能描述：取得实体信息
	 * @param clazz
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	@SuppressWarnings("unchecked")
 	public <T> Entity<T> getEntity(Class<T> clazz){
		Entity<T> en = (Entity<T>)entityInfo.get(clazz);
 		if(en==null){
 			synchronized (entityInfo) {
 				en = this.loadEntity(clazz);
 				entityInfo.put(clazz, en);
			}
 		}
		return en;
	}
 	public <T> Sql<T> buildSql(Class<T> clazz){
 		Entity<T> en = this.getEntity(clazz);
		Sql<T> sql = new Sql<T>(en);
		return sql;
 	}
 	public Sql<Map<String,Object>> buildSql(Class<?> clazz,Class<?> clazz2){
 		View view = new View();
 		Entity<?> en = this.getEntity(clazz);
 		Entity<?> en2 = this.getEntity(clazz2);
 		view.setTable1(clazz);
 		view.setTable1Entity(en);
 		view.setTable2(clazz2);
 		view.setTable2Entity(en2);
		Sql<Map<String,Object>> sql = new Sql<Map<String,Object>>(view);
		return sql;
 	}
	/**
	 * 功能描述：组装插入语句
	 * @param sql
	 * @param obj
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerInsert(Sql<T> sql,Object obj) {
		sql.setPojo(obj);
		sql .append(new StringAotmic(" INSERT INTO "))
			.append(new TableNameAotmic())
			.append(new StringAotmic(" ( "))
			.append(new InsertFieldAotmic())
			.append(new StringAotmic(" ) "))
			.append(new StringAotmic(" VALUES "))
			.append(new InsertValuesAotmic())
			.addParam(sql.getPojo());
		return sql;
	}
	/**
	 * 功能描述：组装单表按照NO字段查询的SQL
	 * @param sql
	 * @param no
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerQueryByNo(Sql<T> sql,Object no) {
		sql .append(new StringAotmic(" SELECT "))
			.append(new InsertFieldAotmic())
			.append(new StringAotmic(" FROM "))
			.append(new TableNameAotmic())
			.append(new WhereByNoAotmic())
			.addParam(no);
		return sql;
	}
	/**
	 * 功能描述：组装单表按照ID字段查询的SQL
	 * @param sql
	 * @param id
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerQueryById(Sql<T> sql,Object id) {
		sql .append(new StringAotmic(" SELECT "))
			.append(new InsertFieldAotmic())
			.append(new StringAotmic(" FROM "))
			.append(new TableNameAotmic())
			.append(new WhereByIdAotmic())
			.addParam(id);
		return sql;
	}
	/**
	 * 功能描述：组装单表按照ID更新的SQL
	 * @param sql
	 * @param obj
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerUpdateById(Sql<T> sql,Object obj) {
		sql.setPojo(obj);
		sql .append(new StringAotmic(" UPDATE "))
			.append(new TableNameAotmic())
			.append(new StringAotmic(" SET "))
			.append(new UpdateFieldAotmic())
			.append(new WhereByIdAotmic())
			.addParam(sql.getPojo());
		if(obj instanceof IEntity){
			IEntity pojo = (IEntity)obj;
			sql.addParam(pojo.get("id"));
		}
		return sql;
	}
	/**
	 * DELETE FROM TAB_NAME WHERE NO=?
	 * @return
	 */
	public <T> Sql<T> makerDelete(Sql<T> sql,Cnd cnd) {
		sql .append(new StringAotmic(" DELETE FROM "))
			.append(new TableNameAotmic())
			.append(cnd.getItems())
			.addParam(cnd.getParams());
		return sql;
	}
	/**
	 * 功能描述：组装单表按照NO更新的SQL
	 * @param sql
	 * @param obj
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerUpdateByNo(Sql<T> sql,Object obj) {
		sql.setPojo(obj);
		sql .append(new StringAotmic(" UPDATE "))
			.append(new TableNameAotmic())
			.append(new StringAotmic(" SET "))
			.append(new UpdateFieldAotmic())
			.append(new WhereByNoAotmic())
			.addParam(sql.getPojo());
		if(obj instanceof IEntity){
			IEntity pojo = (IEntity)obj;
			sql.addParam(pojo.get("no"));
		}
		return sql;
	}
	/**
	 * 功能描述：组装单表按照指定条件查询的SQL
	 * @param sql
	 * @param cnd
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerQueryByCondition(Sql<T> sql,Cnd cnd) {
		sql .append(new StringAotmic(" SELECT "))
			.append(new InsertFieldAotmic())
			.append(new StringAotmic(" FROM "))
			.append(new TableNameAotmic());
		if(cnd!=null){
			sql.append(cnd.getItems())
			.addParam(cnd.getParams());
		}
		return sql;
	}
	/**
	 * 功能描述：多表查询
	 * @param sql
	 * @param cols
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerVirtulCondition(Sql<T> sql,Cnd cnd,Col[] cols) {
		sql .append(new StringAotmic(" SELECT "))
			.append(new ViturlViewFieldAotmic(sql.getView(),cols))
			.append(new StringAotmic(" FROM "))
			.append(new ViturlViewTableNameAotmic(sql.getView()))
			.append(cnd.getItems())
			.addParam(cnd.getParams());
		return sql;
	}
	/**
	 * 功能描述：根据条件对象组装SQL :  SELECT COUNT(*) FROM TABLENAME WHERE .......
	 * @param sql
	 * @param cnd
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	public <T> Sql<T> makerCountByCondition(Sql<T> sql,Cnd cnd) {
		sql .append(new StringAotmic(" SELECT COUNT(*) FROM "))
			.append(new TableNameAotmic())
			.append(cnd.getItems())
			.addParam(cnd.getParams());
		return sql;
	}
	/**
	 * 功能描述：组装更新sql = UPDATE TABLENAME SET ......... WHERE .......
	 * @param sql
	 * @param cnd
	 * @param obj
	 * @return
	 * @author tianming
	 * @date 2015年12月21日
	 */
	public <T> Sql<T> makerUpdateByCondition(Sql<T> sql,Cnd cnd,Object obj) {
		sql.setPojo(obj);
		sql .append(new StringAotmic(" UPDATE "))
			.append(new TableNameAotmic())
			.append(new StringAotmic(" SET "))
			.append(new UpdateFieldAotmic())
			.append(cnd.getItems())
			.addParam(sql.getPojo())
			.addParam(cnd.getParams());
		return sql;
	}
	/**
	 * 功能描述：加载实体的注解信息
	 * @param clazz
	 * @return
	 * @author tianming
	 * @date 2016年1月8日
	 */
	private <T> Entity<T> loadEntity(Class<T> clazz){
		Entity<T> en = new Entity<T>();
		Field[] fields = clazz.getDeclaredFields();
		Table table = clazz.getAnnotation(Table.class);
		if(table!=null){
			en.setTab_name(table.name());
			en.setHistory_tab_name(table.historyName());
		}
		en.setClazz(clazz);
		for(int i = 0;i<fields.length;i++){
			Field f = fields[i];
			Column column = f.getAnnotation(Column.class);
			if(column!=null){
				EntityField<T> entityField = new EntityField<T>();
				entityField.setEntity(en);
				entityField.setName(f.getName());
				entityField.setType(column.type());
				entityField.setComment(column.comment());
				entityField.setMax_length(column.max_length());
				entityField.setNullable(column.nullable());
				en.addField(entityField);
			}
		}
		return en;
	}
}
