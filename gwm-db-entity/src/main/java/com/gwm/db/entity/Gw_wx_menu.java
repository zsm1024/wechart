package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_MENU")
public class Gw_wx_menu extends IEntity implements RowMapper<Gw_wx_menu>  {
		@Column(type=ColType.STRING,max_length="20",nullable=false)
		private String id = "";
		@Column(type=ColType.STRING,max_length="200")
		private String menu_content = "";
		@Column(type=ColType.STRING,max_length="200")
		private String menu_type = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String menu_url = "";
		@Column(type=ColType.STRING,max_length="20")
		private String menu_key = "";
		@Column(type=ColType.STRING,max_length="20")
		private String father_menu_id = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String function_id = "";
		@Column(type=ColType.STRING,max_length="200")
		private String function_title = "";
		@Column(type=ColType.STRING,max_length="2")
		private String menu_order = "";
		private String table_name="Gw_wx_menu";
		
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
		public void setMenu_content(String menu_content){
			if(menu_content==null||"".equals(menu_content)){
				menu_content = "";
			}
			this.menu_content = menu_content;
		}
		public String getMenu_content(){
			return this.menu_content ;
		}
		public void setMenu_type(String menu_type){
			if(menu_type==null||"".equals(menu_type)){
				menu_type = "";
			}
			this.menu_type = menu_type;
		}
		public String getMenu_type(){
			return this.menu_type ;
		}
		public void setMenu_url(String menu_url){
			if(menu_url==null||"".equals(menu_url)){
				menu_url = "";
			}
			this.menu_url = menu_url;
		}
		public String getMenu_url(){
			return this.menu_url ;
		}
		public void setMenu_key(String menu_key){
			if(menu_key==null||"".equals(menu_key)){
				menu_key = "";
			}
			this.menu_key = menu_key;
		}
		public String getMenu_key(){
			return this.menu_key ;
		}
		public void setFather_menu_id(String father_menu_id){
			if(father_menu_id==null||"".equals(father_menu_id)){
				father_menu_id = "";
			}
			this.father_menu_id = father_menu_id;
		}
		public String getFather_menu_id(){
			return this.father_menu_id ;
		}
		public void setFunction_id(String function_id){
			if(function_id==null||"".equals(function_id)){
				function_id = "";
			}
			this.function_id = function_id;
		}
		public String getFunction_id(){
			return this.function_id ;
		}
		public void setFunction_title(String function_title){
			if(function_title==null||"".equals(function_title)){
				function_title = "";
			}
			this.function_title = function_title;
		}
		public String getFunction_title(){
			return this.function_title ;
		}
		public void setMenu_order(String menu_order){
			if(menu_order==null||"".equals(menu_order)){
				menu_order = "";
			}
			this.menu_order = menu_order;
		}
		public String getMenu_order(){
			return this.menu_order ;
		}
	
		public Gw_wx_menu mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_menu entity = new  Gw_wx_menu();
			entity.setId(rs.getString("id"));
			entity.setMenu_content(rs.getString("menu_content"));
			entity.setMenu_type(rs.getString("menu_type"));
			entity.setMenu_url(rs.getString("menu_url"));
			entity.setMenu_key(rs.getString("menu_key"));
			entity.setFather_menu_id(rs.getString("father_menu_id"));
			entity.setFunction_id(rs.getString("function_id"));
			entity.setFunction_title(rs.getString("function_title"));
			entity.setMenu_order(rs.getString("menu_order"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("menu_content:["+getMenu_content()+"];\n");
			info.append("menu_type:["+getMenu_type()+"];\n");
			info.append("menu_url:["+getMenu_url()+"];\n");
			info.append("menu_key:["+getMenu_key()+"];\n");
			info.append("father_menu_id:["+getFather_menu_id()+"];\n");
			info.append("function_id:["+getFunction_id()+"];\n");
			info.append("function_title:["+getFunction_title()+"];\n");
			info.append("menu_order:["+getMenu_order()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("menu_content".equals(col_name)){
				this.setMenu_content(String.valueOf(value));
			}
			if("menu_type".equals(col_name)){
				this.setMenu_type(String.valueOf(value));
			}
			if("menu_url".equals(col_name)){
				this.setMenu_url(String.valueOf(value));
			}
			if("menu_key".equals(col_name)){
				this.setMenu_key(String.valueOf(value));
			}
			if("father_menu_id".equals(col_name)){
				this.setFather_menu_id(String.valueOf(value));
			}
			if("function_id".equals(col_name)){
				this.setFunction_id(String.valueOf(value));
			}
			if("function_title".equals(col_name)){
				this.setFunction_title(String.valueOf(value));
			}
			if("menu_order".equals(col_name)){
				this.setMenu_order(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("menu_content".equals(col_name)){
				return this.getMenu_content();
			}
			if("menu_type".equals(col_name)){
				return this.getMenu_type();
			}
			if("menu_url".equals(col_name)){
				return this.getMenu_url();
			}
			if("menu_key".equals(col_name)){
				return this.getMenu_key();
			}
			if("father_menu_id".equals(col_name)){
				return this.getFather_menu_id();
			}
			if("function_id".equals(col_name)){
				return this.getFunction_id();
			}
			if("function_title".equals(col_name)){
				return this.getFunction_title();
			}
			if("menu_order".equals(col_name)){
				return this.getMenu_order();
			}
			return null;
		}
}