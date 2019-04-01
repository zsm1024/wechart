package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_MSG_WECHAT_REG")
public class Gw_msg_wechat_reg extends IEntity implements RowMapper<Gw_msg_wechat_reg>  {
		@Column(type=ColType.STRING,max_length="14",nullable=false)
		private String msg_date = "";
		@Column(type=ColType.STRING,max_length="11",nullable=false)
		private String msg_phone = "";
		@Column(type=ColType.STRING,max_length="2",nullable=false)
		private String msg_type = "";
		@Column(type=ColType.STRING,max_length="80",nullable=false)
		private String openid = "";
		@Column(type=ColType.STRING,max_length="14")
		private String send_time = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_msg_wechat_reg";
		
		public String getTable_name(){
			return table_name;
		}

		public void setMsg_date(String msg_date){
			if(msg_date==null||"".equals(msg_date)){
				msg_date = "";
			}
			this.msg_date = msg_date;
		}
		public String getMsg_date(){
			return this.msg_date ;
		}
		public void setMsg_phone(String msg_phone){
			if(msg_phone==null||"".equals(msg_phone)){
				msg_phone = "";
			}
			this.msg_phone = msg_phone;
		}
		public String getMsg_phone(){
			return this.msg_phone ;
		}
		public void setMsg_type(String msg_type){
			if(msg_type==null||"".equals(msg_type)){
				msg_type = "";
			}
			this.msg_type = msg_type;
		}
		public String getMsg_type(){
			return this.msg_type ;
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
		public void setSend_time(String send_time){
			if(send_time==null||"".equals(send_time)){
				send_time = "";
			}
			this.send_time = send_time;
		}
		public String getSend_time(){
			return this.send_time ;
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
	
		public Gw_msg_wechat_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_msg_wechat_reg entity = new  Gw_msg_wechat_reg();
			entity.setMsg_date(rs.getString("msg_date"));
			entity.setMsg_phone(rs.getString("msg_phone"));
			entity.setMsg_type(rs.getString("msg_type"));
			entity.setOpenid(rs.getString("openid"));
			entity.setSend_time(rs.getString("send_time"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("msg_date:["+getMsg_date()+"];\n");
			info.append("msg_phone:["+getMsg_phone()+"];\n");
			info.append("msg_type:["+getMsg_type()+"];\n");
			info.append("openid:["+getOpenid()+"];\n");
			info.append("send_time:["+getSend_time()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("msg_date".equals(col_name)){
				this.setMsg_date(String.valueOf(value));
			}
			if("msg_phone".equals(col_name)){
				this.setMsg_phone(String.valueOf(value));
			}
			if("msg_type".equals(col_name)){
				this.setMsg_type(String.valueOf(value));
			}
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("send_time".equals(col_name)){
				this.setSend_time(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("msg_date".equals(col_name)){
				return this.getMsg_date();
			}
			if("msg_phone".equals(col_name)){
				return this.getMsg_phone();
			}
			if("msg_type".equals(col_name)){
				return this.getMsg_type();
			}
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("send_time".equals(col_name)){
				return this.getSend_time();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}