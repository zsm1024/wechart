package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_PROMPT_INFO")
public class Gw_prompt_info extends IEntity implements RowMapper<Gw_prompt_info>  {
		@Column(type=ColType.STRING,max_length="100",nullable=false)
		private String id = "";
		@Column(type=ColType.STRING,max_length="1",nullable=false)
		private String msg_type = "";
		@Column(type=ColType.STRING,max_length="1",nullable=false)
		private String binding_state = "";
		@Column(type=ColType.STRING,max_length="400")
		private String msg_content = "";
		@Column(type=ColType.STRING,max_length="400")
		private String link_address = "";
		@Column(type=ColType.STRING,max_length="1")
		private String msg_state = "";
		private String table_name="Gw_prompt_info";
		
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
		public void setMsg_type(String msg_type){
			if(msg_type==null||"".equals(msg_type)){
				msg_type = "";
			}
			this.msg_type = msg_type;
		}
		public String getMsg_type(){
			return this.msg_type ;
		}
		public void setBinding_state(String binding_state){
			if(binding_state==null||"".equals(binding_state)){
				binding_state = "";
			}
			this.binding_state = binding_state;
		}
		public String getBinding_state(){
			return this.binding_state ;
		}
		public void setMsg_content(String msg_content){
			if(msg_content==null||"".equals(msg_content)){
				msg_content = "";
			}
			this.msg_content = msg_content;
		}
		public String getMsg_content(){
			return this.msg_content ;
		}
		public void setLink_address(String link_address){
			if(link_address==null||"".equals(link_address)){
				link_address = "";
			}
			this.link_address = link_address;
		}
		public String getLink_address(){
			return this.link_address ;
		}
		public void setMsg_state(String msg_state){
			if(msg_state==null||"".equals(msg_state)){
				msg_state = "";
			}
			this.msg_state = msg_state;
		}
		public String getMsg_state(){
			return this.msg_state ;
		}
	
		public Gw_prompt_info mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_prompt_info entity = new  Gw_prompt_info();
			entity.setId(rs.getString("id"));
			entity.setMsg_type(rs.getString("msg_type"));
			entity.setBinding_state(rs.getString("binding_state"));
			entity.setMsg_content(rs.getString("msg_content"));
			entity.setLink_address(rs.getString("link_address"));
			entity.setMsg_state(rs.getString("msg_state"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("msg_type:["+getMsg_type()+"];\n");
			info.append("binding_state:["+getBinding_state()+"];\n");
			info.append("msg_content:["+getMsg_content()+"];\n");
			info.append("link_address:["+getLink_address()+"];\n");
			info.append("msg_state:["+getMsg_state()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("msg_type".equals(col_name)){
				this.setMsg_type(String.valueOf(value));
			}
			if("binding_state".equals(col_name)){
				this.setBinding_state(String.valueOf(value));
			}
			if("msg_content".equals(col_name)){
				this.setMsg_content(String.valueOf(value));
			}
			if("link_address".equals(col_name)){
				this.setLink_address(String.valueOf(value));
			}
			if("msg_state".equals(col_name)){
				this.setMsg_state(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("msg_type".equals(col_name)){
				return this.getMsg_type();
			}
			if("binding_state".equals(col_name)){
				return this.getBinding_state();
			}
			if("msg_content".equals(col_name)){
				return this.getMsg_content();
			}
			if("link_address".equals(col_name)){
				return this.getLink_address();
			}
			if("msg_state".equals(col_name)){
				return this.getMsg_state();
			}
			return null;
		}
}