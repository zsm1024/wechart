package com.gwm.db.dao;

import java.util.List;

public class Ipage {
	private int pageNo;  //当前页
	private int pageSize = 10; //每页条数
	private List<Object> rows;  //查询结果
	private int total = 0;//总记录数
	private String success = "0000";
	private String sortname = null;//排序字段
	private String sortorder = null;//排序字段
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public List<Object> getRows() {
		return rows;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setRows(List rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSortname() {
		return sortname;
	}
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}
	public String getSortorder() {
		return sortorder;
	}
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
	
	public int getStartNum(){
		return (pageNo-1)*pageSize+1;
	}
}
