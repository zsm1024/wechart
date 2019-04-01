package com.gwm.db.dao.condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gwm.common.SpringUtil;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.SqlMaker;
import com.gwm.db.dao.condition.Operator;
import com.gwm.db.dao.condition.Order;
import com.gwm.db.dao.entity.EntityField;
import com.gwm.db.dao.sql.ColType;

public class Col {
	private String col_name = null;
	private String tab_name = null;
	private ColType type = null;
	public Col(String col_name,String tab_name){
		this.col_name = col_name;
		this.tab_name = tab_name;
		SqlMaker sqlMaker = SpringUtil.getBean(Dao.class).getSqlMaker();
		EntityField<?> field = sqlMaker.getEntityField(tab_name, col_name);
		type = field.getType();
	}
	public Operator getOperator(){
		Operator opt = new Operator();
		opt.setTab_name(tab_name);
		opt.setCol_name(col_name);
		return opt;
	}
	/**
	 * @Description: 
	 * @param other
	 * @return    
	 * @return: Object    
	 * @throws 
	 * @author：tianming
	 * @2016年8月10日下午12:53:08
	 */
	private Object wraperOtherValue(Object other){
		if(other!=null){
			if(other instanceof String){
				String value = (String)other;
				switch(type){
					case INT:return Integer.parseInt(value);
					case FLOAT:return Float.parseFloat(value);
					case DOUBLE:{
						return new BigDecimal(value).doubleValue();
					}
					case LONG:return Long.parseLong(value);
					default:return other;
				}
			}
		}
		return other;
	}
	public Operator like(Object col){
		Operator opt = this.getOperator();
		opt.setOpt(" LIKE ");
		opt.setOther(col);
		return opt;
	}
	public Operator eq(Object obj){
		Operator opt = this.getOperator();
		opt.setOpt(" = ");
		opt.setOther(wraperOtherValue(obj));
		return opt;
	}
	public Operator in(Object... params){
		Operator opt = this.getOperator();
		opt.setOpt(" IN ");
		List<Object> paramsList = new ArrayList<Object>();
		for(int i = 0;i<params.length;i++){
			paramsList.add(params[i]);
		}
		opt.setOther(paramsList);
		return opt;
	}
	public Operator notIn(Object... params){
		Operator opt = this.getOperator();
		opt.setOpt(" NOT IN ");
		List<Object> paramsList = new ArrayList<Object>();
		for(int i = 0;i<params.length;i++){
			paramsList.add(params[i]);
		}
		opt.setOther(paramsList);
		return opt;
	}
	public Operator gt(Object obj){
		Operator opt = this.getOperator();
		opt.setOpt(" > ");
		opt.setOther(wraperOtherValue(obj));
		return opt;
	}
	public Operator less(Object col){
		Operator opt = this.getOperator();
		opt.setOpt(" < ");
		opt.setOther(wraperOtherValue(col));
		return opt;
	}
	public Operator notLike(Object col){
		Operator opt = this.getOperator();
		opt.setOpt(" NOT LIKE ");
		opt.setOther(wraperOtherValue(col));
		return opt;
	}
	public Operator notEq(Object obj){
		Operator opt = this.getOperator();
		opt.setOpt(" <> ");
		opt.setOther(wraperOtherValue(obj));
		return opt;
	}
	public Operator gtAndEq(Object obj){
		Operator opt = this.getOperator();
		opt.setOpt(" >= ");
		opt.setOther(wraperOtherValue(obj));
		return opt;
	}
	public Operator lessAndEq(Object obj){
		Operator opt = this.getOperator();
		opt.setOpt(" <= ");
		opt.setOther(wraperOtherValue(obj));
		return opt;
	}
	public Operator isNull(){
		Operator opt = this.getOperator();
		opt.setOpt(" IS NULL ");
		return opt;
	}
	public Operator isNotNull(){
		Operator opt = this.getOperator();
		opt.setOpt(" IS NOT NULL ");
		return opt;
	}
	public Order asc(){
		Order opt = new Order();
		opt.setTab_name(tab_name);
		opt.setName(col_name);
		opt.setType(" ASC ");
		return opt;
	}
	public Order desc(){
		Order opt = new Order();
		opt.setTab_name(tab_name);
		opt.setName(col_name);
		opt.setType(" DESC ");
		return opt;
	}
	public Col in(Col col){
		return null;
	}
	public String getCol_name() {
		return col_name;
	}
	public void setCol_name(String col_name) {
		this.col_name = col_name;
	}
	public String getTab_name() {
		return tab_name;
	}
	public void setTab_name(String tab_name) {
		this.tab_name = tab_name;
	}
	
}
