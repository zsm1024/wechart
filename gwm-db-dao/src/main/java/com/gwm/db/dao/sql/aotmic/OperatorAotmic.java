package com.gwm.db.dao.sql.aotmic;

import java.util.List;

import com.gwm.db.dao.condition.Col;
import com.gwm.db.dao.condition.Operator;
import com.gwm.db.dao.condition.Order;
import com.gwm.db.dao.sql.Aotmic;
import com.gwm.db.dao.sql.Sql;
import com.gwm.db.dao.sql.SqlType;

public class OperatorAotmic  implements Aotmic{
	private Operator opt = null;
	public OperatorAotmic(Operator opt){
		this.opt = opt;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> void assembleSql(StringBuffer sql,Sql<T> sqlInfo) {
		 String col_name = "";
		 String second_col_name = "";
		 Object obj = opt.getOther();
		 Col other = null;
		 if(obj instanceof Col){
			 other = (Col)obj;
		 }
		 if(opt.getListOrder()==null){//非排序操作
			 if(sqlInfo.getSqlType()==SqlType.SELECT_MANY){
				 col_name = opt.getTab_name()+"."+opt.getCol_name() ;
				 if(other!=null)
				 second_col_name = other.getTab_name() +"." + other.getCol_name();
			 }else{
				 col_name =  opt.getCol_name() ;
				 if(other!=null)
					 second_col_name = other.getCol_name();
			 }
			 if(other!=null){
				 sql.append(" " + opt.getKeyWord()+ " " + col_name + opt.getOpt() + second_col_name);
			 }else{
				 if("IN".equals(opt.getOpt().trim())||"NOT IN".equals(opt.getOpt().trim())){
					List<Object> params = (List<Object>)opt.getOther();
					sql.append(" " + opt.getKeyWord()+ " " + col_name + opt.getOpt()+" ( ");
					for(int i = 0;i<params.size();i++){
						sql.append("?");
						if(i!=params.size()-1){
							sql.append(",");
						}
					}
					sql.append(" ) ");
				 }else{
					 if("IS NOT NULL".equals(opt.getOpt().trim())||"IS NULL".equals(opt.getOpt().trim())){
						 sql.append(" " + opt.getKeyWord()+ " " + col_name + opt.getOpt()+" ");
					 }else{
						 sql.append(" " + opt.getKeyWord()+ " " + col_name + opt.getOpt()+" ? ");
					 }
				 }
			 }
		 }else{
			 sql.append(" " + opt.getKeyWord()+ " ");
			 for(int i = 0 ;i<opt.getListOrder().size();i++){
				 Order order = opt.getListOrder().get(i);
				 if(sqlInfo.getSqlType()==SqlType.SELECT_MANY){
					 sql.append(" "+ order.getTab_name() + "." +order.getName() +" "+order.getType()+" ");
				 }else{
					 sql.append(" "+ order.getName() +" "+order.getType()+" ");
				 }
				 if(i!=opt.getListOrder().size()-1){
					 sql.append(",");
				 }
			 }
		 }
	}

}
