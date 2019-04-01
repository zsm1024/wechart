package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_CCS_ONLINE")
public class Gw_ccs_online extends IEntity implements RowMapper<Gw_ccs_online>  {
		@Column(type=ColType.STRING,max_length="30",nullable=false)
		private String openid = "";
		@Column(type=ColType.STRING,max_length="20",nullable=false)
		private String createtime = "";
		private String table_name="Gw_ccs_online";
		
		public String getTable_name(){
			return table_name;
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
		public void setCreatetime(String createtime){
			if(createtime==null||"".equals(createtime)){
				createtime = "";
			}
			this.createtime = createtime;
		}
		public String getCreatetime(){
			return this.createtime ;
		}
	
		public Gw_ccs_online mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_ccs_online entity = new  Gw_ccs_online();
			entity.setOpenid(rs.getString("openid"));
			entity.setCreatetime(rs.getString("createtime"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("createtime:["+getCreatetime()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("createtime".equals(col_name)){
				this.setCreatetime(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("createtime".equals(col_name)){
				return this.getCreatetime();
			}
			return null;
		}
}