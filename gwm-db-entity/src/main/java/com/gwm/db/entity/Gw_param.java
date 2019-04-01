package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_PARAM")
public class Gw_param extends IEntity implements RowMapper<Gw_param>  {
		@Column(type=ColType.STRING,max_length="40")
		private String param_name = "";
		@Column(type=ColType.STRING,max_length="500")
		private String param_value = "";
		@Column(type=ColType.STRING,max_length="100")
		private String brf = "";
		private String table_name="Gw_param";
		
		public String getTable_name(){
			return table_name;
		}

		public void setParam_name(String param_name){
			if(param_name==null||"".equals(param_name)){
				param_name = "";
			}
			this.param_name = param_name;
		}
		public String getParam_name(){
			return this.param_name ;
		}
		public void setParam_value(String param_value){
			if(param_value==null||"".equals(param_value)){
				param_value = "";
			}
			this.param_value = param_value;
		}
		public String getParam_value(){
			return this.param_value ;
		}
		public void setBrf(String brf){
			if(brf==null||"".equals(brf)){
				brf = "";
			}
			this.brf = brf;
		}
		public String getBrf(){
			return this.brf ;
		}
	
		public Gw_param mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_param entity = new  Gw_param();
			entity.setParam_name(rs.getString("param_name"));
			entity.setParam_value(rs.getString("param_value"));
			entity.setBrf(rs.getString("brf"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("param_name:["+getParam_name()+"];\n");
			info.append("param_value:["+getParam_value()+"];\n");
			info.append("brf:["+getBrf()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("param_name".equals(col_name)){
				this.setParam_name(String.valueOf(value));
			}
			if("param_value".equals(col_name)){
				this.setParam_value(String.valueOf(value));
			}
			if("brf".equals(col_name)){
				this.setBrf(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("param_name".equals(col_name)){
				return this.getParam_name();
			}
			if("param_value".equals(col_name)){
				return this.getParam_value();
			}
			if("brf".equals(col_name)){
				return this.getBrf();
			}
			return null;
		}
}