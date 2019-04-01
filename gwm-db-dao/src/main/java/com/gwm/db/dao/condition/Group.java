package com.gwm.db.dao.condition;

import java.util.ArrayList;
import java.util.List;

import com.gwm.db.dao.sql.aotmic.GroupAotmic;
import com.gwm.db.dao.sql.aotmic.OperatorAotmic;

public class Group {
	private String key = null;
	private GroupAotmic group = new GroupAotmic();
	private List<Object> params = new ArrayList<Object>();
	public Group start(Operator opt){
		wraperOpt(this,"",opt);
		return this;
	}
	public Group and(Operator opt){
		wraperOpt(this,"AND",opt);
		return this;
	}
	public Group or(Operator opt){
		wraperOpt(this,"OR",opt);
		return this;
	}
	@SuppressWarnings("unchecked")
	private static void wraperOpt (Group cnd,String keyWord,Operator opt){
		opt.setKeyWord(keyWord);
		cnd.getGroup().addAotmic(new OperatorAotmic(opt));
		if(opt.getOther()!=null&&!(opt.getOther() instanceof Col)){
			if(opt.getOther() instanceof List){
				cnd.params.addAll((List<Object>)opt.getOther());
			}else{
				cnd.params.add(opt.getOther());
			}
		}
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public GroupAotmic getGroup() {
		return group;
	}
	public void setGroup(GroupAotmic group) {
		this.group = group;
	}
	
}
