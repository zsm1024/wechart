package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_SYS_PARAM")
public class Gw_wx_sys_param extends IEntity implements RowMapper<Gw_wx_sys_param>  {
		@Column(type=ColType.STRING,max_length="100",nullable=false)
		private String sys_key = "";
		@Column(type=ColType.STRING,max_length="200")
		private String sys_value = "";
		@Column(type=ColType.STRING,max_length="500")
		private String memo = "";
		private String table_name="Gw_wx_sys_param";
		
		public String getTable_name(){
			return table_name;
		}

		public void setSys_key(String sys_key){
			if(sys_key==null||"".equals(sys_key)){
				sys_key = "";
			}
			this.sys_key = sys_key;
		}
		public String getSys_key(){
			return this.sys_key ;
		}
		public void setSys_value(String sys_value){
			if(sys_value==null||"".equals(sys_value)){
				sys_value = "";
			}
			this.sys_value = sys_value;
		}
		public String getSys_value(){
			return this.sys_value ;
		}
		public void setMemo(String memo){
			if(memo==null||"".equals(memo)){
				memo = "";
			}
			this.memo = memo;
		}
		public String getMemo(){
			return this.memo ;
		}
	
		public Gw_wx_sys_param mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_sys_param entity = new  Gw_wx_sys_param();
			entity.setSys_key(rs.getString("sys_key"));
			entity.setSys_value(rs.getString("sys_value"));
			entity.setMemo(rs.getString("memo"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("sys_key:["+getSys_key()+"];\n");
			info.append("sys_value:["+getSys_value()+"];\n");
			info.append("memo:["+getMemo()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("sys_key".equals(col_name)){
				this.setSys_key(String.valueOf(value));
			}
			if("sys_value".equals(col_name)){
				this.setSys_value(String.valueOf(value));
			}
			if("memo".equals(col_name)){
				this.setMemo(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("sys_key".equals(col_name)){
				return this.getSys_key();
			}
			if("sys_value".equals(col_name)){
				return this.getSys_value();
			}
			if("memo".equals(col_name)){
				return this.getMemo();
			}
			return null;
		}
}