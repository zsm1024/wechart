package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_GENERAL_INFORMATION")
public class Gw_wx_general_information extends IEntity implements RowMapper<Gw_wx_general_information>  {
		@Column(type=ColType.STRING,max_length="20",nullable=false)
		private String id = "";
		@Column(type=ColType.STRING,max_length="2")
		private String information_type = "";
		@Column(type=ColType.STRING,max_length="200")
		private String information_name = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String content = "";
		@Column(type=ColType.STRING,max_length="20")
		private String color = "";
		@Column(type=ColType.STRING,max_length="80")
		private String information_id = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String information_url = "";
		private String table_name="Gw_wx_general_information";
		
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
		public void setInformation_type(String information_type){
			if(information_type==null||"".equals(information_type)){
				information_type = "";
			}
			this.information_type = information_type;
		}
		public String getInformation_type(){
			return this.information_type ;
		}
		public void setInformation_name(String information_name){
			if(information_name==null||"".equals(information_name)){
				information_name = "";
			}
			this.information_name = information_name;
		}
		public String getInformation_name(){
			return this.information_name ;
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
		public void setColor(String color){
			if(color==null||"".equals(color)){
				color = "";
			}
			this.color = color;
		}
		public String getColor(){
			return this.color ;
		}
		public void setInformation_id(String information_id){
			if(information_id==null||"".equals(information_id)){
				information_id = "";
			}
			this.information_id = information_id;
		}
		public String getInformation_id(){
			return this.information_id ;
		}
		public void setInformation_url(String information_url){
			if(information_url==null||"".equals(information_url)){
				information_url = "";
			}
			this.information_url = information_url;
		}
		public String getInformation_url(){
			return this.information_url ;
		}
	
		public Gw_wx_general_information mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_general_information entity = new  Gw_wx_general_information();
			entity.setId(rs.getString("id"));
			entity.setInformation_type(rs.getString("information_type"));
			entity.setInformation_name(rs.getString("information_name"));
			entity.setContent(rs.getString("content"));
			entity.setColor(rs.getString("color"));
			entity.setInformation_id(rs.getString("information_id"));
			entity.setInformation_url(rs.getString("information_url"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("information_type:["+getInformation_type()+"];\n");
			info.append("information_name:["+getInformation_name()+"];\n");
			info.append("content:["+getContent()+"];\n");
			info.append("color:["+getColor()+"];\n");
			info.append("information_id:["+getInformation_id()+"];\n");
			info.append("information_url:["+getInformation_url()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("information_type".equals(col_name)){
				this.setInformation_type(String.valueOf(value));
			}
			if("information_name".equals(col_name)){
				this.setInformation_name(String.valueOf(value));
			}
			if("content".equals(col_name)){
				this.setContent(String.valueOf(value));
			}
			if("color".equals(col_name)){
				this.setColor(String.valueOf(value));
			}
			if("information_id".equals(col_name)){
				this.setInformation_id(String.valueOf(value));
			}
			if("information_url".equals(col_name)){
				this.setInformation_url(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("information_type".equals(col_name)){
				return this.getInformation_type();
			}
			if("information_name".equals(col_name)){
				return this.getInformation_name();
			}
			if("content".equals(col_name)){
				return this.getContent();
			}
			if("color".equals(col_name)){
				return this.getColor();
			}
			if("information_id".equals(col_name)){
				return this.getInformation_id();
			}
			if("information_url".equals(col_name)){
				return this.getInformation_url();
			}
			return null;
		}
}