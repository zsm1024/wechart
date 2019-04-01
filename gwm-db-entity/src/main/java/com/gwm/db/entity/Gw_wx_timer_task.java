package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_TIMER_TASK")
public class Gw_wx_timer_task extends IEntity implements RowMapper<Gw_wx_timer_task>  {
		@Column(type=ColType.STRING,max_length="50")
		private String task_name = "";
		@Column(type=ColType.STRING,max_length="300")
		private String class_name = "";
		@Column(type=ColType.STRING,max_length="50")
		private String method_name = "";
		@Column(type=ColType.STRING,max_length="20")
		private String execute_time = "";
		@Column(type=ColType.STRING,max_length="1")
		private String execute_cycle = "";
		@Column(type=ColType.STRING,max_length="1")
		private String execute_flag = "";
		@Column(type=ColType.STRING,max_length="20")
		private String last_execute_time = "";
		@Column(type=ColType.STRING,max_length="1")
		private String last_execute_flag = "";
		@Column(type=ColType.STRING,max_length="2000")
		private String iplist = "";
		@Column(type=ColType.STRING,max_length="500")
		private String params = "";
		@Column(type=ColType.STRING,max_length="1")
		private String flag = "";
		private String table_name="Gw_wx_timer_task";
		
		public String getTable_name(){
			return table_name;
		}

		public void setTask_name(String task_name){
			if(task_name==null||"".equals(task_name)){
				task_name = "";
			}
			this.task_name = task_name;
		}
		public String getTask_name(){
			return this.task_name ;
		}
		public void setClass_name(String class_name){
			if(class_name==null||"".equals(class_name)){
				class_name = "";
			}
			this.class_name = class_name;
		}
		public String getClass_name(){
			return this.class_name ;
		}
		public void setMethod_name(String method_name){
			if(method_name==null||"".equals(method_name)){
				method_name = "";
			}
			this.method_name = method_name;
		}
		public String getMethod_name(){
			return this.method_name ;
		}
		public void setExecute_time(String execute_time){
			if(execute_time==null||"".equals(execute_time)){
				execute_time = "";
			}
			this.execute_time = execute_time;
		}
		public String getExecute_time(){
			return this.execute_time ;
		}
		public void setExecute_cycle(String execute_cycle){
			if(execute_cycle==null||"".equals(execute_cycle)){
				execute_cycle = "";
			}
			this.execute_cycle = execute_cycle;
		}
		public String getExecute_cycle(){
			return this.execute_cycle ;
		}
		public void setExecute_flag(String execute_flag){
			if(execute_flag==null||"".equals(execute_flag)){
				execute_flag = "";
			}
			this.execute_flag = execute_flag;
		}
		public String getExecute_flag(){
			return this.execute_flag ;
		}
		public void setLast_execute_time(String last_execute_time){
			if(last_execute_time==null||"".equals(last_execute_time)){
				last_execute_time = "";
			}
			this.last_execute_time = last_execute_time;
		}
		public String getLast_execute_time(){
			return this.last_execute_time ;
		}
		public void setLast_execute_flag(String last_execute_flag){
			if(last_execute_flag==null||"".equals(last_execute_flag)){
				last_execute_flag = "";
			}
			this.last_execute_flag = last_execute_flag;
		}
		public String getLast_execute_flag(){
			return this.last_execute_flag ;
		}
		public void setIplist(String iplist){
			if(iplist==null||"".equals(iplist)){
				iplist = "";
			}
			this.iplist = iplist;
		}
		public String getIplist(){
			return this.iplist ;
		}
		public void setParams(String params){
			if(params==null||"".equals(params)){
				params = "";
			}
			this.params = params;
		}
		public String getParams(){
			return this.params ;
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
	
		public Gw_wx_timer_task mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_timer_task entity = new  Gw_wx_timer_task();
			entity.setTask_name(rs.getString("task_name"));
			entity.setClass_name(rs.getString("class_name"));
			entity.setMethod_name(rs.getString("method_name"));
			entity.setExecute_time(rs.getString("execute_time"));
			entity.setExecute_cycle(rs.getString("execute_cycle"));
			entity.setExecute_flag(rs.getString("execute_flag"));
			entity.setLast_execute_time(rs.getString("last_execute_time"));
			entity.setLast_execute_flag(rs.getString("last_execute_flag"));
			entity.setIplist(rs.getString("iplist"));
			entity.setParams(rs.getString("params"));
			entity.setFlag(rs.getString("flag"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("task_name:["+getTask_name()+"];\n");
			info.append("class_name:["+getClass_name()+"];\n");
			info.append("method_name:["+getMethod_name()+"];\n");
			info.append("execute_time:["+getExecute_time()+"];\n");
			info.append("execute_cycle:["+getExecute_cycle()+"];\n");
			info.append("execute_flag:["+getExecute_flag()+"];\n");
			info.append("last_execute_time:["+getLast_execute_time()+"];\n");
			info.append("last_execute_flag:["+getLast_execute_flag()+"];\n");
			info.append("iplist:["+getIplist()+"];\n");
			info.append("params:["+getParams()+"];\n");
			info.append("flag:["+getFlag()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("task_name".equals(col_name)){
				this.setTask_name(String.valueOf(value));
			}
			if("class_name".equals(col_name)){
				this.setClass_name(String.valueOf(value));
			}
			if("method_name".equals(col_name)){
				this.setMethod_name(String.valueOf(value));
			}
			if("execute_time".equals(col_name)){
				this.setExecute_time(String.valueOf(value));
			}
			if("execute_cycle".equals(col_name)){
				this.setExecute_cycle(String.valueOf(value));
			}
			if("execute_flag".equals(col_name)){
				this.setExecute_flag(String.valueOf(value));
			}
			if("last_execute_time".equals(col_name)){
				this.setLast_execute_time(String.valueOf(value));
			}
			if("last_execute_flag".equals(col_name)){
				this.setLast_execute_flag(String.valueOf(value));
			}
			if("iplist".equals(col_name)){
				this.setIplist(String.valueOf(value));
			}
			if("params".equals(col_name)){
				this.setParams(String.valueOf(value));
			}
			if("flag".equals(col_name)){
				this.setFlag(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("task_name".equals(col_name)){
				return this.getTask_name();
			}
			if("class_name".equals(col_name)){
				return this.getClass_name();
			}
			if("method_name".equals(col_name)){
				return this.getMethod_name();
			}
			if("execute_time".equals(col_name)){
				return this.getExecute_time();
			}
			if("execute_cycle".equals(col_name)){
				return this.getExecute_cycle();
			}
			if("execute_flag".equals(col_name)){
				return this.getExecute_flag();
			}
			if("last_execute_time".equals(col_name)){
				return this.getLast_execute_time();
			}
			if("last_execute_flag".equals(col_name)){
				return this.getLast_execute_flag();
			}
			if("iplist".equals(col_name)){
				return this.getIplist();
			}
			if("params".equals(col_name)){
				return this.getParams();
			}
			if("flag".equals(col_name)){
				return this.getFlag();
			}
			return null;
		}
}