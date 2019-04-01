package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_HOLIDAY_REG")
public class Gw_holiday_reg extends IEntity implements RowMapper<Gw_holiday_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String year = "";
		@Column(type=ColType.STRING,max_length="100")
		private String date = "";
		@Column(type=ColType.STRING,max_length="1")
		private String date_type = "";
		private String table_name="Gw_holiday_reg";
		
		public String getTable_name(){
			return table_name;
		}

		public void setYear(String year){
			if(year==null||"".equals(year)){
				year = "";
			}
			this.year = year;
		}
		public String getYear(){
			return this.year ;
		}
		public void setDate(String date){
			if(date==null||"".equals(date)){
				date = "";
			}
			this.date = date;
		}
		public String getDate(){
			return this.date ;
		}
		public void setDate_type(String date_type){
			if(date_type==null||"".equals(date_type)){
				date_type = "";
			}
			this.date_type = date_type;
		}
		public String getDate_type(){
			return this.date_type ;
		}
	
		public Gw_holiday_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_holiday_reg entity = new  Gw_holiday_reg();
			entity.setYear(rs.getString("year"));
			entity.setDate(rs.getString("date"));
			entity.setDate_type(rs.getString("date_type"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("year:["+getYear()+"];\n");
			info.append("date:["+getDate()+"];\n");
			info.append("date_type:["+getDate_type()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("year".equals(col_name)){
				this.setYear(String.valueOf(value));
			}
			if("date".equals(col_name)){
				this.setDate(String.valueOf(value));
			}
			if("date_type".equals(col_name)){
				this.setDate_type(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("year".equals(col_name)){
				return this.getYear();
			}
			if("date".equals(col_name)){
				return this.getDate();
			}
			if("date_type".equals(col_name)){
				return this.getDate_type();
			}
			return null;
		}
}