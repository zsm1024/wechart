package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_LOAN_REG_HST")
public class Gw_loan_reg_hst extends IEntity implements RowMapper<Gw_loan_reg_hst>  {
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="100")
		private String application_num = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		@Column(type=ColType.STRING,max_length="100")
		private String operator = "";
		@Column(type=ColType.INT,max_length="10")
		private int rev_date = 0;
		@Column(type=ColType.INT,max_length="10")
		private int rev_time = 0;
		private String table_name="Gw_loan_reg_hst";
		
		public String getTable_name(){
			return table_name;
		}

		public void setOpenid(String openid){
			if(openid==null||"".equals(openid)){
				openid = "";
			}
			this.openid = openid;
		}
		public String getOpenid(){
			return this.openid ;
		}
		public void setApplication_num(String application_num){
			if(application_num==null||"".equals(application_num)){
				application_num = "";
			}
			this.application_num = application_num;
		}
		public String getApplication_num(){
			return this.application_num ;
		}
		public void setStatus(String status){
			if(status==null||"".equals(status)){
				status = "";
			}
			this.status = status;
		}
		public String getStatus(){
			return this.status ;
		}
		public void setOperator(String operator){
			if(operator==null||"".equals(operator)){
				operator = "";
			}
			this.operator = operator;
		}
		public String getOperator(){
			return this.operator ;
		}
		public void setRev_date(int rev_date){
			this.rev_date = rev_date;
		}
		public void setRev_date(String rev_date){
			if(rev_date==null||rev_date.length()==0){
				rev_date = "0";
			}
			this.rev_date = Integer.parseInt(rev_date);
		}
		public int getRev_date(){
			return this.rev_date ;
		}
		public void setRev_time(int rev_time){
			this.rev_time = rev_time;
		}
		public void setRev_time(String rev_time){
			if(rev_time==null||rev_time.length()==0){
				rev_time = "0";
			}
			this.rev_time = Integer.parseInt(rev_time);
		}
		public int getRev_time(){
			return this.rev_time ;
		}
	
		public Gw_loan_reg_hst mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_loan_reg_hst entity = new  Gw_loan_reg_hst();
			entity.setOpenid(rs.getString("openid"));
			entity.setApplication_num(rs.getString("application_num"));
			entity.setStatus(rs.getString("status"));
			entity.setOperator(rs.getString("operator"));
			entity.setRev_date(rs.getString("rev_date"));
			entity.setRev_time(rs.getString("rev_time"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("application_num:["+getApplication_num()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			info.append("operator:["+getOperator()+"];\n");
			info.append("rev_date:["+getRev_date()+"];\n");
			info.append("rev_time:["+getRev_time()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("application_num".equals(col_name)){
				this.setApplication_num(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
			if("operator".equals(col_name)){
				this.setOperator(String.valueOf(value));
			}
			if("rev_date".equals(col_name)){
				this.setRev_date(String.valueOf(value));
			}
			if("rev_time".equals(col_name)){
				this.setRev_time(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("application_num".equals(col_name)){
				return this.getApplication_num();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			if("operator".equals(col_name)){
				return this.getOperator();
			}
			if("rev_date".equals(col_name)){
				return this.getRev_date();
			}
			if("rev_time".equals(col_name)){
				return this.getRev_time();
			}
			return null;
		}
}