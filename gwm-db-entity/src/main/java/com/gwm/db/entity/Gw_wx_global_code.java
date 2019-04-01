package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_GLOBAL_CODE")
public class Gw_wx_global_code extends IEntity implements RowMapper<Gw_wx_global_code>  {
		@Column(type=ColType.STRING,max_length="32",nullable=false)
		private String code_name = "";
		@Column(type=ColType.STRING,max_length="2000")
		private String code_flag = "";
		@Column(type=ColType.STRING,max_length="100",nullable=false)
		private String wx_unit = "";
		private String table_name="Gw_wx_global_code";
		
		public String getTable_name(){
			return table_name;
		}

		public void setCode_name(String code_name){
			if(code_name==null||"".equals(code_name)){
				code_name = "";
			}
			this.code_name = code_name;
		}
		public String getCode_name(){
			return this.code_name ;
		}
		public void setCode_flag(String code_flag){
			if(code_flag==null||"".equals(code_flag)){
				code_flag = "";
			}
			this.code_flag = code_flag;
		}
		public String getCode_flag(){
			return this.code_flag ;
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
	
		public Gw_wx_global_code mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_global_code entity = new  Gw_wx_global_code();
			entity.setCode_name(rs.getString("code_name"));
			entity.setCode_flag(rs.getString("code_flag"));
			entity.setWx_unit(rs.getString("wx_unit"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("code_name:["+getCode_name()+"];\n");
			info.append("code_flag:["+getCode_flag()+"];\n");
			info.append("wx_unit:["+getWx_unit()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("code_name".equals(col_name)){
				this.setCode_name(String.valueOf(value));
			}
			if("code_flag".equals(col_name)){
				this.setCode_flag(String.valueOf(value));
			}
			if("wx_unit".equals(col_name)){
				this.setWx_unit(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("code_name".equals(col_name)){
				return this.getCode_name();
			}
			if("code_flag".equals(col_name)){
				return this.getCode_flag();
			}
			if("wx_unit".equals(col_name)){
				return this.getWx_unit();
			}
			return null;
		}
}