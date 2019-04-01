package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_HISTORY_NEWS_REG")
public class Gw_history_news_reg extends IEntity implements RowMapper<Gw_history_news_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String id = "";
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="1")
		private String msg_type = "";
		@Column(type=ColType.STRING,max_length="100")
		private String msg_title = "";
		@Column(type=ColType.STRING,max_length="3000")
		private String msg_cont = "";
		@Column(type=ColType.STRING,max_length="8")
		private String msg_date = "";
		@Column(type=ColType.STRING,max_length="8")
		private String msg_time = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_history_news_reg";
		
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
		public void setMsg_type(String msg_type){
			if(msg_type==null||"".equals(msg_type)){
				msg_type = "";
			}
			this.msg_type = msg_type;
		}
		public String getMsg_type(){
			return this.msg_type ;
		}
		public void setMsg_title(String msg_title){
			if(msg_title==null||"".equals(msg_title)){
				msg_title = "";
			}
			this.msg_title = msg_title;
		}
		public String getMsg_title(){
			return this.msg_title ;
		}
		public void setMsg_cont(String msg_cont){
			if(msg_cont==null||"".equals(msg_cont)){
				msg_cont = "";
			}
			this.msg_cont = msg_cont;
		}
		public String getMsg_cont(){
			return this.msg_cont ;
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
		public void setMsg_time(String msg_time){
			if(msg_time==null||"".equals(msg_time)){
				msg_time = "";
			}
			this.msg_time = msg_time;
		}
		public String getMsg_time(){
			return this.msg_time ;
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
	
		public Gw_history_news_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_history_news_reg entity = new  Gw_history_news_reg();
			entity.setId(rs.getString("id"));
			entity.setOpenid(rs.getString("openid"));
			entity.setMsg_type(rs.getString("msg_type"));
			entity.setMsg_title(rs.getString("msg_title"));
			entity.setMsg_cont(rs.getString("msg_cont"));
			entity.setMsg_date(rs.getString("msg_date"));
			entity.setMsg_time(rs.getString("msg_time"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("openid:["+getOpenid()+"];\n");
			info.append("msg_type:["+getMsg_type()+"];\n");
			info.append("msg_title:["+getMsg_title()+"];\n");
			info.append("msg_cont:["+getMsg_cont()+"];\n");
			info.append("msg_date:["+getMsg_date()+"];\n");
			info.append("msg_time:["+getMsg_time()+"];\n");
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
			if("msg_type".equals(col_name)){
				this.setMsg_type(String.valueOf(value));
			}
			if("msg_title".equals(col_name)){
				this.setMsg_title(String.valueOf(value));
			}
			if("msg_cont".equals(col_name)){
				this.setMsg_cont(String.valueOf(value));
			}
			if("msg_date".equals(col_name)){
				this.setMsg_date(String.valueOf(value));
			}
			if("msg_time".equals(col_name)){
				this.setMsg_time(String.valueOf(value));
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
			if("msg_type".equals(col_name)){
				return this.getMsg_type();
			}
			if("msg_title".equals(col_name)){
				return this.getMsg_title();
			}
			if("msg_cont".equals(col_name)){
				return this.getMsg_cont();
			}
			if("msg_date".equals(col_name)){
				return this.getMsg_date();
			}
			if("msg_time".equals(col_name)){
				return this.getMsg_time();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}