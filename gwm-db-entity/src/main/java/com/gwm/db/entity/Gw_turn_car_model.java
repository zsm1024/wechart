package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_TURN_CAR_MODEL")
public class Gw_turn_car_model extends IEntity implements RowMapper<Gw_turn_car_model>  {
		@Column(type=ColType.STRING,max_length="200")
		private String shop_car_model = "";
		@Column(type=ColType.STRING,max_length="200")
		private String ifc_car_model = "";
		private String table_name="Gw_turn_car_model";
		
		public String getTable_name(){
			return table_name;
		}

		public void setShop_car_model(String shop_car_model){
			if(shop_car_model==null||"".equals(shop_car_model)){
				shop_car_model = "";
			}
			this.shop_car_model = shop_car_model;
		}
		public String getShop_car_model(){
			return this.shop_car_model ;
		}
		public void setIfc_car_model(String ifc_car_model){
			if(ifc_car_model==null||"".equals(ifc_car_model)){
				ifc_car_model = "";
			}
			this.ifc_car_model = ifc_car_model;
		}
		public String getIfc_car_model(){
			return this.ifc_car_model ;
		}
	
		public Gw_turn_car_model mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_turn_car_model entity = new  Gw_turn_car_model();
			entity.setShop_car_model(rs.getString("shop_car_model"));
			entity.setIfc_car_model(rs.getString("ifc_car_model"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("shop_car_model:["+getShop_car_model()+"];\n");
			info.append("ifc_car_model:["+getIfc_car_model()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("shop_car_model".equals(col_name)){
				this.setShop_car_model(String.valueOf(value));
			}
			if("ifc_car_model".equals(col_name)){
				this.setIfc_car_model(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("shop_car_model".equals(col_name)){
				return this.getShop_car_model();
			}
			if("ifc_car_model".equals(col_name)){
				return this.getIfc_car_model();
			}
			return null;
		}
}