package com.gwm.db.dao.dbexec;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gwm.db.dao.DaoExecutor;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.sql.SqlType;
import com.gwm.db.dao.util.PageUtil;

public class SpringExector extends DaoExecutor{
	private JdbcTemplate jt = null;
	private Logger log = LoggerFactory.getLogger(SpringExector.class);
	@SuppressWarnings("rawtypes")
	@Override
	public <T> boolean run(final Sql<T> sql){
		StringBuffer str = sql.createSql();
		if(sql.getBeforeCallback()!=null&&sql.getSqlType()!=SqlType.BATCH){
			sql.getBeforeCallback().invoke(sql);
		}
		if(sql.getPager()!=null){
			str = new StringBuffer(PageUtil.trainSql(str.toString(),sql.getPager()));
		}
		log.debug("执行sql语句："+str);
		switch(sql.getSqlType()){
			case INSERT:
			case DELETE:
			case UPDATE:{
				jt.update(str.toString(), initParameter(sql));
				break;
			}
			case BATCH:{
				 if(sql.getPojo() instanceof List){
					 final List list = (List)sql.getPojo();
					 jt.batchUpdate(str.toString(), new BatchPreparedStatementSetter(){
							public void setValues(PreparedStatement ps,int i)throws SQLException
				               {
				            		Object entity = list.get(i);
				            		if(entity instanceof IEntity){
				            			final IEntity pojo = (IEntity)entity;
				            			for ( int idx = 0;idx < sql.getEn().getFields().size();idx++){
				            				EntityField col =sql.getEn().getFields().get(idx);
				            				String value = String.valueOf(pojo.get(col.getName()));

				            				switch(col.getType()){
					            				case STRING : case FILE:{
					            					ps.setString(idx+1, value);
					            					break;
					            				}
					            				case INT:{
					            					ps.setInt(idx+1, Integer.parseInt(value));
					            					break;
					            				}
					            				case FLOAT:{
					            					ps.setFloat(idx+1, Float.parseFloat(value));
					            					break;
					            				}
					            				case DOUBLE:{
					            					ps.setDouble(idx+1,Double.parseDouble(value.replace(",", "")));
					            					break;
					            				}
					            				case LONG:{
					            					ps.setLong(idx+1,Long.parseLong(value));
					            					break;
					            				}
				            				}

				            			}
				            		}
				               }

							public int getBatchSize() {
								return list.size();
							}
				        });
				 }
				break;
			}
			case SELECT_ENTITY:{
				try{
					jt.queryForObject(str.toString(),initParameter(sql),new RowMapper<T>() {
						@Override
						public T mapRow(ResultSet rs, int arg1)
								throws SQLException {
							sql.getAfterCallback().invoke(rs, sql);
							return null;
						}
					});
				}catch(Exception e){
					if(e instanceof EmptyResultDataAccessException){
						System.out.println("查询记录为空");
					}else{
						throw e;
					}
				}
				break;
			}

			case SELECT_LIST:{
				List<T> list = (List<T>) jt.query(str.toString(),initParameter(sql),new RowMapper<T>() {
					@Override
					public T mapRow(ResultSet rs, int arg1)
							throws SQLException {
						sql.getAfterCallback().invoke(rs, sql);
						return sql.getResult();
					}
				});
				sql.setResultList(list);
				break;
			}
			case SELECT_MANY:{
				
				List<Map<String,Object>> list = (List<Map<String,Object>>) jt.query(str.toString(),initParameter(sql),new RowMapper<Map<String,Object>>() {
					@Override
					public Map<String,Object> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						sql.getAfterCallback().invoke(rs, sql);
						return sql.getResultMap();
					}
				});
				sql.setResultMapList(list);
				break;
			}
			case COUNT:{
				int count = jt.queryForObject(str.toString(),initParameter(sql),Integer.class);
				sql.setCount(count);
				break;
			}
			default:
				break;

		}
		if(sql.getHistoryCallBack()!=null&&sql.getSqlType()!=SqlType.BATCH){
			sql.getHistoryCallBack().invoke(sql);
		}
		return true;
	}
	private <T> Object[] initParameter(Sql<T> sql) {
		//
		Object[][] colTypes = sql.getParamType();
		Object[] params = new Object[colTypes.length];
		for(int i = 0;i<colTypes.length;i++){
			log.debug("sql参数"+i+":"+colTypes[i][1]);
			params[i] = colTypes[i][1];
		}
		return params;
	}
	public JdbcTemplate getJt() {
		return jt;
	}
	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
}
