package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_SYS_MENU")
public class Gw_wx_sys_menu extends IEntity implements RowMapper<Gw_wx_sys_menu>  {
		private String table_name="Gw_wx_sys_menu";
		
		public String getTable_name(){
			return table_name;
		}

	
		public Gw_wx_sys_menu mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_sys_menu entity = new  Gw_wx_sys_menu();
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
		}
		@Override
		public Object get(String col_name) {
			return null;
		}
}