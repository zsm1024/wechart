package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_USER_INFO")
public class Gw_user_info extends IEntity implements RowMapper<Gw_user_info>  {
		@Column(type=ColType.STRING,max_length="100")
		private String user_code = "";
		@Column(type=ColType.STRING,max_length="100")
		private String user_name = "";
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="1")
		private String user_sex = "";
		@Column(type=ColType.STRING,max_length="10")
		private String user_age = "";
		@Column(type=ColType.STRING,max_length="20")
		private String user_phone = "";
		@Column(type=ColType.STRING,max_length="20")
		private String user_id = "";
		@Column(type=ColType.STRING,max_length="100")
		private String profession = "";
		@Column(type=ColType.STRING,max_length="100")
		private String user_zone = "";
		@Column(type=ColType.STRING,max_length="100")
		private String income = "";
		private String table_name="Gw_user_info";
		
		public String getTable_name(){
			return table_name;
		}

		public void setUser_code(String user_code){
			if(user_code==null||"".equals(user_code)){
				user_code = "";
			}
			this.user_code = user_code;
		}
		public String getUser_code(){
			return this.user_code ;
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
		public void setOpenid(String openid){
			if(openid==null||"".equals(openid)){
				openid = "";
			}
			this.openid = openid;
		}
		public String getOpenid(){
			return this.openid ;
		}
		public void setUser_sex(String user_sex){
			if(user_sex==null||"".equals(user_sex)){
				user_sex = "";
			}
			this.user_sex = user_sex;
		}
		public String getUser_sex(){
			return this.user_sex ;
		}
		public void setUser_age(String user_age){
			if(user_age==null||"".equals(user_age)){
				user_age = "";
			}
			this.user_age = user_age;
		}
		public String getUser_age(){
			return this.user_age ;
		}
		public void setUser_phone(String user_phone){
			if(user_phone==null||"".equals(user_phone)){
				user_phone = "";
			}
			this.user_phone = user_phone;
		}
		public String getUser_phone(){
			return this.user_phone ;
		}
		public void setUser_id(String user_id){
			if(user_id==null||"".equals(user_id)){
				user_id = "";
			}
			this.user_id = user_id;
		}
		public String getUser_id(){
			return this.user_id ;
		}
		public void setProfession(String profession){
			if(profession==null||"".equals(profession)){
				profession = "";
			}
			this.profession = profession;
		}
		public String getProfession(){
			return this.profession ;
		}
		public void setUser_zone(String user_zone){
			if(user_zone==null||"".equals(user_zone)){
				user_zone = "";
			}
			this.user_zone = user_zone;
		}
		public String getUser_zone(){
			return this.user_zone ;
		}
		public void setIncome(String income){
			if(income==null||"".equals(income)){
				income = "";
			}
			this.income = income;
		}
		public String getIncome(){
			return this.income ;
		}
	
		public Gw_user_info mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_user_info entity = new  Gw_user_info();
			entity.setUser_code(rs.getString("user_code"));
			entity.setUser_name(rs.getString("user_name"));
			entity.setOpenid(rs.getString("openid"));
			entity.setUser_sex(rs.getString("user_sex"));
			entity.setUser_age(rs.getString("user_age"));
			entity.setUser_phone(rs.getString("user_phone"));
			entity.setUser_id(rs.getString("user_id"));
			entity.setProfession(rs.getString("profession"));
			entity.setUser_zone(rs.getString("user_zone"));
			entity.setIncome(rs.getString("income"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("user_code:["+getUser_code()+"];\n");
			info.append("user_name:["+getUser_name()+"];\n");
			info.append("openid:["+getOpenid()+"];\n");
			info.append("user_sex:["+getUser_sex()+"];\n");
			info.append("user_age:["+getUser_age()+"];\n");
			info.append("user_phone:["+getUser_phone()+"];\n");
			info.append("user_id:["+getUser_id()+"];\n");
			info.append("profession:["+getProfession()+"];\n");
			info.append("user_zone:["+getUser_zone()+"];\n");
			info.append("income:["+getIncome()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("user_code".equals(col_name)){
				this.setUser_code(String.valueOf(value));
			}
			if("user_name".equals(col_name)){
				this.setUser_name(String.valueOf(value));
			}
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("user_sex".equals(col_name)){
				this.setUser_sex(String.valueOf(value));
			}
			if("user_age".equals(col_name)){
				this.setUser_age(String.valueOf(value));
			}
			if("user_phone".equals(col_name)){
				this.setUser_phone(String.valueOf(value));
			}
			if("user_id".equals(col_name)){
				this.setUser_id(String.valueOf(value));
			}
			if("profession".equals(col_name)){
				this.setProfession(String.valueOf(value));
			}
			if("user_zone".equals(col_name)){
				this.setUser_zone(String.valueOf(value));
			}
			if("income".equals(col_name)){
				this.setIncome(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("user_code".equals(col_name)){
				return this.getUser_code();
			}
			if("user_name".equals(col_name)){
				return this.getUser_name();
			}
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("user_sex".equals(col_name)){
				return this.getUser_sex();
			}
			if("user_age".equals(col_name)){
				return this.getUser_age();
			}
			if("user_phone".equals(col_name)){
				return this.getUser_phone();
			}
			if("user_id".equals(col_name)){
				return this.getUser_id();
			}
			if("profession".equals(col_name)){
				return this.getProfession();
			}
			if("user_zone".equals(col_name)){
				return this.getUser_zone();
			}
			if("income".equals(col_name)){
				return this.getIncome();
			}
			return null;
		}
}