package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_SUGGEST_REG")
public class Gw_suggest_reg extends IEntity implements RowMapper<Gw_suggest_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String id = "";
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="1")
		private String suggest_type = "";
		@Column(type=ColType.STRING,max_length="400")
		private String suggest_cont = "";
		@Column(type=ColType.STRING,max_length="20")
		private String contact_type = "";
		@Column(type=ColType.STRING,max_length="100")
		private String submit_person = "";
		@Column(type=ColType.STRING,max_length="14")
		private String submit_time = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_suggest_reg";
		
		public String getTable_name(){
			return table_name;
		}

		public void setId(String id){
			if(id==null||"".equals(id)){
				id = "";
			}
			this.id = id;
		}
		public String getId(){
			return this.id ;
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
		public void setSuggest_type(String suggest_type){
			if(suggest_type==null||"".equals(suggest_type)){
				suggest_type = "";
			}
			this.suggest_type = suggest_type;
		}
		public String getSuggest_type(){
			return this.suggest_type ;
		}
		public void setSuggest_cont(String suggest_cont){
			if(suggest_cont==null||"".equals(suggest_cont)){
				suggest_cont = "";
			}
			this.suggest_cont = suggest_cont;
		}
		public String getSuggest_cont(){
			return this.suggest_cont ;
		}
		public void setContact_type(String contact_type){
			if(contact_type==null||"".equals(contact_type)){
				contact_type = "";
			}
			this.contact_type = contact_type;
		}
		public String getContact_type(){
			return this.contact_type ;
		}
		public void setSubmit_person(String submit_person){
			if(submit_person==null||"".equals(submit_person)){
				submit_person = "";
			}
			this.submit_person = submit_person;
		}
		public String getSubmit_person(){
			return this.submit_person ;
		}
		public void setSubmit_time(String submit_time){
			if(submit_time==null||"".equals(submit_time)){
				submit_time = "";
			}
			this.submit_time = submit_time;
		}
		public String getSubmit_time(){
			return this.submit_time ;
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
	
		public Gw_suggest_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_suggest_reg entity = new  Gw_suggest_reg();
			entity.setId(rs.getString("id"));
			entity.setOpenid(rs.getString("openid"));
			entity.setSuggest_type(rs.getString("suggest_type"));
			entity.setSuggest_cont(rs.getString("suggest_cont"));
			entity.setContact_type(rs.getString("contact_type"));
			entity.setSubmit_person(rs.getString("submit_person"));
			entity.setSubmit_time(rs.getString("submit_time"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("openid:["+getOpenid()+"];\n");
			info.append("suggest_type:["+getSuggest_type()+"];\n");
			info.append("suggest_cont:["+getSuggest_cont()+"];\n");
			info.append("contact_type:["+getContact_type()+"];\n");
			info.append("submit_person:["+getSubmit_person()+"];\n");
			info.append("submit_time:["+getSubmit_time()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("suggest_type".equals(col_name)){
				this.setSuggest_type(String.valueOf(value));
			}
			if("suggest_cont".equals(col_name)){
				this.setSuggest_cont(String.valueOf(value));
			}
			if("contact_type".equals(col_name)){
				this.setContact_type(String.valueOf(value));
			}
			if("submit_person".equals(col_name)){
				this.setSubmit_person(String.valueOf(value));
			}
			if("submit_time".equals(col_name)){
				this.setSubmit_time(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("suggest_type".equals(col_name)){
				return this.getSuggest_type();
			}
			if("suggest_cont".equals(col_name)){
				return this.getSuggest_cont();
			}
			if("contact_type".equals(col_name)){
				return this.getContact_type();
			}
			if("submit_person".equals(col_name)){
				return this.getSubmit_person();
			}
			if("submit_time".equals(col_name)){
				return this.getSubmit_time();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}