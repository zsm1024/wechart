package com.gwm.db.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.callback.AfterCallBack;
import com.gwm.db.dao.callback.BeforeCallBack;
import com.gwm.db.dao.callback.HistoryCallBack;
import com.gwm.db.dao.condition.Col;
import com.gwm.db.dao.entity.Entity;
import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.entity.View;
import com.gwm.db.dao.util.Loop;
import com.gwm.db.dao.util.callback.LoopCallBack;

public class Sql<T> {
	private List<Aotmic> aotmic = new ArrayList<Aotmic>();//sql原子单位
	private List<Object> params = new ArrayList<Object>();//sql参数
	private Entity<T> en = null;//实体元信息
	private Object pojo = null;//sql操作的实体对象
	
	private SqlType sqlType = null;//sql类型
	private boolean isForUpdate = false;//是否锁表
	private Pager pager = null;//分页参数
	
	private BeforeCallBack beforeCallback = null;//ResultSet回调
	private AfterCallBack afterCallback = null;//ResultSet回调
	private HistoryCallBack historyCallBack =null;//用于处理历史记录的回调，只用于insert和update
	private T result = null;//sql执行结果
	private Map<String,Object> resultMap = null;//sql执行结果
	private List<T> resultList = null;//sql执行结果
	private List<Map<String,Object>> resultMapList = null;//sql执行结果
	private int count = 0;//用户Dao.cout
	private RuntimeException ex = null;//记录执行过程中的异常
	
	private View view = null;//用于多表查询，构建虚拟视图
	
	private List<Col> cols = null;//更新或插入操作的列信息
	public Sql(Entity<T> en){
		this.en = en;
	}
	public Sql(View view){
		this.view = view;
	}
	public Sql<T> append(Aotmic aotmic){
		this.aotmic.add(aotmic);
		return this;
	}
	public Sql<T> append(List<Aotmic> aotmic){
		this.aotmic.addAll(aotmic);
		return this;
	}
	public Sql<T> addParam(Object param) {
		if(param==null){
			throw new RuntimeException("参数对象为空");
		}
		params.add(param);
		return this;
	}
	public StringBuffer createSql(){
		final StringBuffer sql = new StringBuffer();
		final Sql<T> itself = this;
		Loop.loop(aotmic, new LoopCallBack<Aotmic>() {
			@Override
			public void invoke(int index, Aotmic obj) {
				obj.assembleSql(sql, itself);
			}
		});
		return sql;
	}
	public Object getPojo() {
		return pojo;
	}
	public void setPojo(Object pojo) {
		this.pojo = pojo;
	}
	
	public Object[][] getParamType() {
		final List<Object[]> temp = new ArrayList<Object[]>();
		for(int i =0;i<params.size();i++){
			Object param = params.get(i);
			if(param instanceof IEntity){
				final IEntity pojo = (IEntity)param;
				List<EntityField<T>> cols = this.getOptEntityField();
				Loop.loop(cols, new LoopCallBack<EntityField<T>>() {
					@Override
					public void invoke(int index, EntityField<T> obj) {
						Object[] array = new Object[2];
						Object value = pojo.get(obj.getName());//列值
						array[0] = obj.getType();//列类型
						/*if(value instanceof Number){
							//Number number = (Number)value;
							//array[1] = number.getValue();
						}else{
							array[1] = value;
						}*/
						array[1] = value;
						temp.add(array);
					}
				});
			}else if(param instanceof String||param instanceof Integer||param instanceof Double){
				Object[] array = new Object[2];
				//array[0] = ColType.STRING;
				array[1] = param;
				temp.add(array);
			}else if(param instanceof List){
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>)param;
				for(int j = 0;j<list.size();j++){
					Object[] array = new Object[2];
					//array[0] = ColType.STRING;
					array[1] = list.get(j);
					temp.add(array);
				}
			}
		}
		return temp.toArray(new Object[temp.size()][]);
	}
	/**
	 * 功能描述：取得当前sql操作的列信息
	 * @return
	 * @author tianming
	 * @date 2015年12月21日
	 */
	public List<EntityField<T>> getOptEntityField(){
		if(this.cols==null){
			return this.en.getFields();
		}else{
			List<EntityField<T>> optCol = new ArrayList<EntityField<T>>();
			for(int i = 0;i<this.cols.size();i++){
				Col col = cols.get(i);
				for(int j = 0 ; j<this.en.getFields().size();j++){
					EntityField<T> field = this.en.getFields().get(j);
					if(col.getCol_name().equalsIgnoreCase(field.getName())){
						optCol.add(field);
						break;
					}
				}
			}
			return optCol;
		}
	}
	public SqlType getSqlType() {
		return sqlType;
	}
	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}
	public boolean isForUpdate() {
		return isForUpdate;
	}
	public void setForUpdate(boolean isForUpdate) {
		this.isForUpdate = isForUpdate;
	}
	
	public AfterCallBack getAfterCallback() {
		return afterCallback;
	}
	public void setAfterCallback(AfterCallBack afterCallback) {
		this.afterCallback = afterCallback;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result =  result;
	}
	public List<Aotmic> getAotmic() {
		return aotmic;
	}
	
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public void setAotmic(List<Aotmic> aotmic) {
		this.aotmic = aotmic;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	public Entity<T> getEn() {
		return en;
	}
	public void setEn(Entity<T> en) {
		this.en = en;
	}
	public BeforeCallBack getBeforeCallback() {
		return beforeCallback;
	}
	public void setBeforeCallback(BeforeCallBack beforeCallback) {
		this.beforeCallback = beforeCallback;
	}
	public RuntimeException getEx() {
		return ex;
	}
	public void setEx(RuntimeException ex) {
		this.ex = ex;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	public List<Map<String, Object>> getResultMapList() {
		return resultMapList;
	}
	public void setResultMapList(List<Map<String, Object>> resultMapList) {
		this.resultMapList = resultMapList;
	}
	public List<Col> getCols() {
		return cols;
	}
	public void setCols(List<Col> cols) {
		this.cols = cols;
	}

	public HistoryCallBack getHistoryCallBack() {
		return historyCallBack;
	}

	public void setHistoryCallBack(HistoryCallBack historyCallBack) {
		this.historyCallBack = historyCallBack;
	}
}
