package com.gwm.db.dao.util;

import com.gwm.db.dao.DaoConstant;
import com.gwm.db.dao.sql.Pager;

public class PageUtil {
	public static String trainSql(String sql,Pager pager){
		switch(DaoConstant.DB_TYPE){
			case ORACLE:
				sql = trainSql_oracle(sql,pager);
				break;
			case MYSQL:
				sql = trainSql_mysql(sql,pager);
				break;
			case DB2:
				break;
			case INFORMIX:
				break;
			default:
				break;
		}
		return sql;
	}
	
	/**
	 * 
	 * 功能描述：把sql语句组合成带分页的查询sql，页数是通过对象传进来的
	 * @param 查询sql语句
	 * @param 页数脚本的对象集合　pageParameter
	 * @return　一个带分页的查询语句
	 * @author lvdanqi
	 * @date 2008-9-8
	 * @修改日志：
	 */
	private static String trainSql_oracle(String sql,Pager pageParameter){		
		int pageNo= pageParameter.getPageNo();    //当前页
		int pageSize= pageParameter.getPageSize(); //每页显示页数
		int maxRow=pageNo*pageSize;                              //最后一条记录
		int minRow=(pageNo-1)*pageSize+1;                        //最先一条记录
		StringBuffer sb=new StringBuffer("select * from (select mytable.*,rownum as  myrownum from (");
		sb.append(sql);
		sb.append(") mytable where rownum<="+maxRow);
		sb.append(") where myrownum>="+minRow);	
		return sb.toString();
	}
	
	/**
	 * 
	 * 功能描述：把sql语句组合成带分页的查询sql，页数是通过对象传进来的
	 * @param 查询sql语句
	 * @param 页数脚本的对象集合　pageParameter
	 * @return　一个带分页的查询语句
	 * @author lvdanqi
	 * @date 2008-9-8
	 * @修改日志：
	 */
	private static String trainSql_mysql(String sql,Pager pageParameter){		
		int pageNo= pageParameter.getPageNo();    //当前页
		int pageSize= pageParameter.getPageSize(); //每页显示页数
		int minRow=(pageNo-1)*pageSize;                        //最先一条记录
		StringBuffer sb=new StringBuffer(sql+" limit "+minRow+"," +pageSize);	
		return sb.toString();
	}
}
