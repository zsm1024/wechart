package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_MSG_LOG")
public class Gw_wx_msg_log extends IEntity implements RowMapper<Gw_wx_msg_log>  {
		@Column(type=ColType.STRING,max_length="40")
		private String senter = "";
		@Column(type=ColType.STRING,max_length="30")
		private String wx_unit = "";
		@Column(type=ColType.STRING,max_length="20")
		private String senttime = "";
		@Column(type=ColType.STRING,max_length="40")
		private String receiver = "";
		@Column(type=ColType.STRING,max_length="3000")
		private String content = "";
		@Column(type=ColType.STRING,max_length="1")
		private String flag = "";
		@Column(type=ColType.STRING,max_length="3000")
		private String extend = "";
		private String table_name="Gw_wx_msg_log";
		
		public String getTable_name(){
			return table_name;
		}

		public void setSenter(String senter){
			if(senter==null||"".equals(senter)){
				senter = "";
			}
			this.senter = senter;
		}
		public String getSenter(){
			return this.senter ;
		}
		public void setWx_unit(String wx_unit){
			if(wx_unit==null||"".equals(wx_unit)){
				wx_unit = "";
			}
			this.wx_unit = wx_unit;
		}
		public String getWx_unit(){
			return this.wx_unit ;
		}
		public void setSenttime(String senttime){
			if(senttime==null||"".equals(senttime)){
				senttime = "";
			}
			this.senttime = senttime;
		}
		public String getSenttime(){
			return this.senttime ;
		}
		public void setReceiver(String receiver){
			if(receiver==null||"".equals(receiver)){
				receiver = "";
			}
			this.receiver = receiver;
		}
		public String getReceiver(){
			return this.receiver ;
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
		public void setFlag(String flag){
			if(flag==null||"".equals(flag)){
				flag = "";
			}
			this.flag = flag;
		}
		public String getFlag(){
			return this.flag ;
		}
		public void setExtend(String extend){
			if(extend==null||"".equals(extend)){
				extend = "";
			}
			this.extend = extend;
		}
		public String getExtend(){
			return this.extend ;
		}
	
		public Gw_wx_msg_log mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_msg_log entity = new  Gw_wx_msg_log();
			entity.setSenter(rs.getString("senter"));
			entity.setWx_unit(rs.getString("wx_unit"));
			entity.setSenttime(rs.getString("senttime"));
			entity.setReceiver(rs.getString("receiver"));
			entity.setContent(rs.getString("content"));
			entity.setFlag(rs.getString("flag"));
			entity.setExtend(rs.getString("extend"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("senter:["+getSenter()+"];\n");
			info.append("wx_unit:["+getWx_unit()+"];\n");
			info.append("senttime:["+getSenttime()+"];\n");
			info.append("receiver:["+getReceiver()+"];\n");
			info.append("content:["+getContent()+"];\n");
			info.append("flag:["+getFlag()+"];\n");
			info.append("extend:["+getExtend()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("senter".equals(col_name)){
				this.setSenter(String.valueOf(value));
			}
			if("wx_unit".equals(col_name)){
				this.setWx_unit(String.valueOf(value));
			}
			if("senttime".equals(col_name)){
				this.setSenttime(String.valueOf(value));
			}
			if("receiver".equals(col_name)){
				this.setReceiver(String.valueOf(value));
			}
			if("content".equals(col_name)){
				this.setContent(String.valueOf(value));
			}
			if("flag".equals(col_name)){
				this.setFlag(String.valueOf(value));
			}
			if("extend".equals(col_name)){
				this.setExtend(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("senter".equals(col_name)){
				return this.getSenter();
			}
			if("wx_unit".equals(col_name)){
				return this.getWx_unit();
			}
			if("senttime".equals(col_name)){
				return this.getSenttime();
			}
			if("receiver".equals(col_name)){
				return this.getReceiver();
			}
			if("content".equals(col_name)){
				return this.getContent();
			}
			if("flag".equals(col_name)){
				return this.getFlag();
			}
			if("extend".equals(col_name)){
				return this.getExtend();
			}
			return null;
		}
}