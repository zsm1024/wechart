package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_USER_LOAN_REL")
public class Gw_user_loan_rel extends IEntity implements RowMapper<Gw_user_loan_rel>  {
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="100")
		private String id_type = "";
		@Column(type=ColType.STRING,max_length="100")
		private String id_num = "";
		@Column(type=ColType.STRING,max_length="100")
		private String user_name = "";
		@Column(type=ColType.STRING,max_length="20")
		private String phone = "";
		@Column(type=ColType.STRING,max_length="100")
		private String contract_id = "";
		@Column(type=ColType.STRING,max_length="100")
		private String contract_code = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_user_loan_rel";
		
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
		public void setId_type(String id_type){
			if(id_type==null||"".equals(id_type)){
				id_type = "";
			}
			this.id_type = id_type;
		}
		public String getId_type(){
			return this.id_type ;
		}
		public void setId_num(String id_num){
			if(id_num==null||"".equals(id_num)){
				id_num = "";
			}
			this.id_num = id_num;
		}
		public String getId_num(){
			return this.id_num ;
		}
		public void setUser_name(String user_name){
			if(user_name==null||"".equals(user_name)){
				user_name = "";
			}
			this.user_name = user_name;
		}
		public String getUser_name(){
			return this.user_name ;
		}
		public void setPhone(String phone){
			if(phone==null||"".equals(phone)){
				phone = "";
			}
			this.phone = phone;
		}
		public String getPhone(){
			return this.phone ;
		}
		public void setContract_id(String contract_id){
			if(contract_id==null||"".equals(contract_id)){
				contract_id = "";
			}
			this.contract_id = contract_id;
		}
		public String getContract_id(){
			return this.contract_id ;
		}
		public void setContract_code(String contract_code){
			if(contract_code==null||"".equals(contract_code)){
				contract_code = "";
			}
			this.contract_code = contract_code;
		}
		public String getContract_code(){
			return this.contract_code ;
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
	
		public Gw_user_loan_rel mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_user_loan_rel entity = new  Gw_user_loan_rel();
			entity.setOpenid(rs.getString("openid"));
			entity.setId_type(rs.getString("id_type"));
			entity.setId_num(rs.getString("id_num"));
			entity.setUser_name(rs.getString("user_name"));
			entity.setPhone(rs.getString("phone"));
			entity.setContract_id(rs.getString("contract_id"));
			entity.setContract_code(rs.getString("contract_code"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("id_type:["+getId_type()+"];\n");
			info.append("id_num:["+getId_num()+"];\n");
			info.append("user_name:["+getUser_name()+"];\n");
			info.append("phone:["+getPhone()+"];\n");
			info.append("contract_id:["+getContract_id()+"];\n");
			info.append("contract_code:["+getContract_code()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("id_type".equals(col_name)){
				this.setId_type(String.valueOf(value));
			}
			if("id_num".equals(col_name)){
				this.setId_num(String.valueOf(value));
			}
			if("user_name".equals(col_name)){
				this.setUser_name(String.valueOf(value));
			}
			if("phone".equals(col_name)){
				this.setPhone(String.valueOf(value));
			}
			if("contract_id".equals(col_name)){
				this.setContract_id(String.valueOf(value));
			}
			if("contract_code".equals(col_name)){
				this.setContract_code(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("id_type".equals(col_name)){
				return this.getId_type();
			}
			if("id_num".equals(col_name)){
				return this.getId_num();
			}
			if("user_name".equals(col_name)){
				return this.getUser_name();
			}
			if("phone".equals(col_name)){
				return this.getPhone();
			}
			if("contract_id".equals(col_name)){
				return this.getContract_id();
			}
			if("contract_code".equals(col_name)){
				return this.getContract_code();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}