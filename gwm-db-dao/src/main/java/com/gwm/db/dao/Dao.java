package com.gwm.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwm.db.dao.callback.AfterCallBack;
import com.gwm.db.dao.callback.BeforeCallBack;
import com.gwm.db.dao.callback.HistoryCallBack;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.dao.condition.Col;
import com.gwm.db.dao.dbexec.SpringExector;
import com.gwm.db.dao.sql.Pager;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.sql.SqlType;
import com.gwm.db.dao.sql.aotmic.ForUpdateAotmic;

/**
 * 功能描述：数据库操作类
 * @author tianming
 *
 */
public class Dao {
	private static Logger log = LoggerFactory.getLogger(Dao.class);
	private SqlMaker sqlMaker = null;
	private BeforeCallBack beforeCallBack = null;
	private AfterCallBack oneEntityCallBack = null;
	private HistoryCallBack historyInsertCallBack =null;

	public void setHistoryInsertCallBack(HistoryCallBack historyInsertCallBack) {
		this.historyInsertCallBack = historyInsertCallBack;
	}

	public void setOneEntityCallBack(AfterCallBack oneEntityCallBack) {
		this.oneEntityCallBack = oneEntityCallBack;
	}

	public void setBeforeCallBack(BeforeCallBack beforeCallBack) {
		this.beforeCallBack = beforeCallBack;
	}

	private DaoExecutor exec = new SpringExector();
	public Dao(){
		sqlMaker = new SqlMaker();//sqlMaker的成员变量的值不会发生变化，多线程是不会出现线程不安全的问题
	}
	
	public SqlMaker getSqlMaker() {
		return sqlMaker;
	}
	
	public <T> T fetchBySql(String sql,final T sqlEntity){
		return sqlEntity;
	}
	
	public void setSqlMaker(SqlMaker sqlMaker) {
		this.sqlMaker = sqlMaker;
	}

	/**
	 * 插入记录
	 * @param table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> void insert(final T table)  {
		if(table instanceof Map){
			
		}else{//默认作实体处理
			Class<T> clazz = (Class<T>) table.getClass();
			Sql<T> sql = sqlMaker.buildSql(clazz);
			sql.setSqlType(SqlType.INSERT);
			sql.setBeforeCallback(beforeCallBack);
			sql.setHistoryCallBack(historyInsertCallBack);
			sqlMaker.makerInsert(sql,table);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		}
	}

	/**
	 * 插入记录
	 * @param table
	 * @param flag true-按规则生成key,并放入工作流 false-不处理key,但放入工作流
	 * @param <T>
     */
	public <T> void insert(final T table,boolean flag)  {
		insert(table);
	}

	/**
	 * 删除记录
	 * @param clazz
	 * @param cnd
	 * @param <T>
     */
	public <T> void delete(Class<T> clazz,Cnd cnd)  {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.DELETE);
		sql.setBeforeCallback(beforeCallBack);
		sqlMaker.makerDelete(sql, cnd);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
	}
	/**
	 * 插入记录
	 * @param table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> void batchInsert(final List<T> table)  {
		if(table==null){
			throw new RuntimeException(" list is null");
		}
		if(table.size()==0) return;
		Class<T> clazz = (Class<T>) table.get(0).getClass();
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.BATCH);
		sql.setBeforeCallback(beforeCallBack);
		sqlMaker.makerInsert(sql,table);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
	}
	/**
	 * 按照ID字段更新
	 * @param table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T updateById(final T table)  {
		Class<T> clazz = (Class<T>) table.getClass();
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.UPDATE);
		sql.setBeforeCallback(beforeCallBack);
		sql.setHistoryCallBack(historyInsertCallBack);
		sqlMaker.makerUpdateById(sql,table);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
		return sql.getResult();
	}
	/**
	 * 按照NO字段更新
	 * @param table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T updateByNo(final T table) {
		Class<T> clazz = (Class<T>) table.getClass();
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.UPDATE);
		sql.setBeforeCallback(beforeCallBack);
		sql.setHistoryCallBack(historyInsertCallBack);
		try {
			sqlMaker.makerUpdateByNo(sql,table);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		} catch (Exception e) {
			log.error("执行sql报错",e);
			throw new RuntimeException(e.getMessage());
		}
		return sql.getResult();
	}
	/**
	 * 按指定条件更新
	 * @param table
	 * @param cnd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(final T table,Cnd cnd) {
		if(table==null){
			throw new RuntimeException("更新的记录为空");
		}
		Class<T> clazz = (Class<T>) table.getClass();
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.UPDATE);
		try {
			sqlMaker.makerUpdateByCondition(sql,cnd,table);

			sql.setBeforeCallback(beforeCallBack);
			sql.setHistoryCallBack(historyInsertCallBack);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		} catch (Exception e) {
			log.error("执行sql报错",e);
			throw new RuntimeException(e.getMessage());
		}
		return sql.getResult();
	}
	@SuppressWarnings("unchecked")
	/**
	 * 功能描述：更新指定的列
	 *@param table
	 *@param cnd
	 *@param columns
	 *@return
	 *@author tianming
	 *@date 2015年12月21日
	 */
	public <T> T update(final T table,Cnd cnd,Col... columns)  {
		if(table==null){
			throw new RuntimeException("更新的记录为空");
		}
		Class<T> clazz = (Class<T>) table.getClass();
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.UPDATE);
		sqlMaker.makerUpdateByCondition(sql,cnd,table);
		sql.setBeforeCallback(beforeCallBack);
		sql.setHistoryCallBack(historyInsertCallBack);
		List<Col> cols = new ArrayList<Col>();
		for(int i = 0;i<columns.length;i++){
			cols.add(columns[i]);
		}
		if(cols.size()>0){
			sql.setCols(cols);
		}
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
		return sql.getResult();
	}
	/**
	 * 列表查询
	 * @param clazz
	 * @param cnd
	 * @return
	 */
	public <T> List<T> list(Class<T> clazz,Cnd cnd) {
		return this.list(clazz, cnd, null);
	}
	public <T> List<T> list(Class<T> clazz,Cnd cnd,boolean forupdate)   {
		return this.list(clazz, cnd, null,forupdate);
	}
	/**
	 * 列表查询
	 * @param clazz
	 * @param cnd
	 * @return
	 */
	public <T> List<T> list(Class<T> clazz,Cnd cnd,Pager pager)  {
		return this.list(clazz, cnd, pager, false);
	}
	/**
	 * 列表查询
	 * @param clazz
	 * @param cnd
	 * @return
	 */
	public <T> List<T> list(Class<T> clazz,Cnd cnd,Pager pager,boolean forupdate) {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.SELECT_LIST);
		sql.setPager(pager);
		sqlMaker.makerQueryByCondition(sql,cnd);
		if(forupdate){
			sql.append(new ForUpdateAotmic());
		}
		sql.setAfterCallback(oneEntityCallBack);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
		return sql.getResultList();
	}
	/**
	 * 按条件统计条数
	 * @param clazz
	 * @param cnd
	 * @return
	 */
	public <T> int count(Class<T> clazz,Cnd cnd)    {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.COUNT);
		sqlMaker.makerCountByCondition(sql,cnd);
		sql.setAfterCallback(oneEntityCallBack);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
		return sql.getCount();
	}
	/**
	 * 按照id字段查询单条记录(默认不锁表查询)
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T fetchById(Class<T> clazz,final String id)  {
		return fetchById(clazz,id,false);
	}
	
	/**
	 * 按照id字段查询单条记录
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T fetchById(Class<T> clazz,final String id,boolean forupdate) {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.SELECT_ENTITY);
		try {
			sqlMaker.makerQueryById(sql,id);
			if(forupdate){
				sql.append(new ForUpdateAotmic());
			}
			sql.setAfterCallback(oneEntityCallBack);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		} catch (Exception e) {
			log.error("执行sql报错",e);
			throw new RuntimeException(e.getMessage());
		}
		return sql.getResult();
	}

	/**
	 * 按照no字段查询单条记录
	 * @param clazz
	 * @param no
	 * @param <T>
     * @return
     */
	public <T> T fetchByNo(Class<T> clazz,final String no) {
		return fetchByNo(clazz,no,false);
	}

	/**
	 * 按照no字段查询单条记录
	 * @param clazz
	 * @param no
	 * @param forupdate
	 * @param <T>
	 * @return
     */
	public <T> T fetchByNo(Class<T> clazz,final String no,boolean forupdate) {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.SELECT_ENTITY);
		try {
			sqlMaker.makerQueryByNo(sql,no);

			if(forupdate){
				sql.append(new ForUpdateAotmic());
			}
			sql.setAfterCallback(oneEntityCallBack);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		} catch (Exception e) {
			log.error("执行sql报错",e);
			throw new RuntimeException(e.getMessage());
		}
		return sql.getResult();
	}

	/**
	 * 按照no字段查询单条记录
	 * @param clazz
	 * @param cnd
	 * @param <T>
	 * @return
     */
	public <T> T fetch(Class<T> clazz,Cnd cnd) {
		return fetch(clazz,cnd,false);
	}

	/**
	 * 按照no字段查询单条记录
	 * @param clazz
	 * @param cnd
	 * @param forupdate
	 * @param <T>
	 * @return
     */
	public <T> T fetch(Class<T> clazz,Cnd cnd,boolean forupdate) {
		Sql<T> sql = sqlMaker.buildSql(clazz);
		sql.setSqlType(SqlType.SELECT_ENTITY);
		sql.setForUpdate(forupdate);
		try{
			sqlMaker.makerQueryByCondition(sql,cnd);
			if(forupdate){
				sql.append(new ForUpdateAotmic());
			}
			sql.setAfterCallback(oneEntityCallBack);
			exec.run(sql);
			if(sql.getEx()!=null){
				throw sql.getEx();
			}
		} catch (Exception e) {
			log.error("执行sql报错",e);
			throw new RuntimeException(e.getMessage());
		}
		return sql.getResult();
	}
	/**
	 * 多表查询（目前只支持内连接）
	 * @param t1
	 * @param t2
	 * @return
	 */
	public List<Map<String,Object>> listMany(Class<?> t1,Class<?> t2,Cnd cnd,Col[] columns)  {
		Sql<Map<String,Object>> sql = sqlMaker.buildSql(t1,t2);
		sql.setSqlType(SqlType.SELECT_MANY);
		sqlMaker.makerVirtulCondition(sql,cnd,columns);
		sql.setAfterCallback(oneEntityCallBack);
		exec.run(sql);
		if(sql.getEx()!=null){
			throw sql.getEx();
		}
		return  sql.getResultMapList();
	}
	/**
	 * 多表查询（目前只支持内连接）
	 * @param t1
	 * @param t2
	 * @return
	 */
	public List<HashMap<String,Object>> listMany(Class<?> t1,Class<?> t2,Cnd cnd) {
		listMany(t1,t2,null);
		return  null;
	}
	public DaoExecutor getExec() {
		return exec;
	}
	public void setExec(DaoExecutor exec) {
		this.exec = exec;
	}
	
	
}
