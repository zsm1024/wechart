package com.gwm.db.dao.condition;

import java.util.ArrayList;
import java.util.List;

import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.aotmic.OperatorAotmic;

public class Cnd {
	private List<Aotmic> items = new ArrayList<Aotmic>();
	private List<Object> params = new ArrayList<Object>();
	public static Cnd where(Object opt){
		Cnd cnd = new Cnd();
		wraperOpt(cnd,"WHERE",opt);
		return cnd;
	}

	public Cnd and(Object opt){
		wraperOpt(this,"AND",opt);
		return this;
	}

	public Cnd or(Object opt){
		wraperOpt(this,"OR",opt);
		return this;
	}

	public Cnd orderby(Order... opts){
		String sql = "ORDER BY";
		List<Order> list = new ArrayList<Order>();
		for(int i =0;i<opts.length;i++){
			list.add(opts[i]);
		}
		Operator opt = new Operator();
		opt.setListOrder(list);
		wraperOpt(this,sql,opt);
		return this;
	}
	@SuppressWarnings("unchecked")
	private static void wraperOpt (Cnd cnd,String keyWord,Object obj){
		if(obj instanceof Operator){
			Operator opt = (Operator)obj;
			opt.setKeyWord(keyWord);
			cnd.items.add(new OperatorAotmic(opt));
			if(opt.getOther()!=null&&!(opt.getOther() instanceof Col)){
				if(opt.getOther() instanceof List){
					cnd.params.addAll((List<Object>)opt.getOther());
				}else{
					cnd.params.add(opt.getOther());
				}
			}
		}else if(obj instanceof Group){
			Group group = (Group)obj;
			group.setKey(keyWord);
			group.getGroup().setGroup(group);
			cnd.items.add(group.getGroup());
			if(group.getParams()!=null&&group.getParams().size()>0){
				cnd.params.addAll(group.getParams());
			}
		}
	}
	public List<Aotmic> getItems() {
		return items;
	}
	public void setItems(List<Aotmic> items) {
		this.items = items;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	
}
