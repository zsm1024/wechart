package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_MSG_REG")
public class Gw_msg_reg extends IEntity implements RowMapper<Gw_msg_reg>  {
		@Column(type=ColType.STRING,max_length="14",nullable=false)
		private String date = "";
		@Column(type=ColType.STRING,max_length="11",nullable=false)
		private String phone = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String content = "";
		@Column(type=ColType.STRING,max_length="1",nullable=false)
		private String type = "";
		@Column(type=ColType.STRING,max_length="80")
		private String wx_msg_id = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_msg_reg";
		
		public String getTable_name(){
			return table_name;
		}

		public void setDate(String date){
			if(date==null||"".equals(date)){
				date = "";
			}
			this.date = date;
		}
		public String getDate(){
			return this.date ;
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
		public void setContent(String content){
			if(content==null||"".equals(content)){
				content = "";
			}
			this.content = content;
		}
		public String getContent(){
			return this.content ;
		}
		public void setType(String type){
			if(type==null||"".equals(type)){
				type = "";
			}
			this.type = type;
		}
		public String getType(){
			return this.type ;
		}
		public void setWx_msg_id(String wx_msg_id){
			if(wx_msg_id==null||"".equals(wx_msg_id)){
				wx_msg_id = "";
			}
			this.wx_msg_id = wx_msg_id;
		}
		public String getWx_msg_id(){
			return this.wx_msg_id ;
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
	
		public Gw_msg_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_msg_reg entity = new  Gw_msg_reg();
			entity.setDate(rs.getString("date"));
			entity.setPhone(rs.getString("phone"));
			entity.setContent(rs.getString("content"));
			entity.setType(rs.getString("type"));
			entity.setWx_msg_id(rs.getString("wx_msg_id"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("date:["+getDate()+"];\n");
			info.append("phone:["+getPhone()+"];\n");
			info.append("content:["+getContent()+"];\n");
			info.append("type:["+getType()+"];\n");
			info.append("wx_msg_id:["+getWx_msg_id()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("date".equals(col_name)){
				this.setDate(String.valueOf(value));
			}
			if("phone".equals(col_name)){
				this.setPhone(String.valueOf(value));
			}
			if("content".equals(col_name)){
				this.setContent(String.valueOf(value));
			}
			if("type".equals(col_name)){
				this.setType(String.valueOf(value));
			}
			if("wx_msg_id".equals(col_name)){
				this.setWx_msg_id(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("date".equals(col_name)){
				return this.getDate();
			}
			if("phone".equals(col_name)){
				return this.getPhone();
			}
			if("content".equals(col_name)){
				return this.getContent();
			}
			if("type".equals(col_name)){
				return this.getType();
			}
			if("wx_msg_id".equals(col_name)){
				return this.getWx_msg_id();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}