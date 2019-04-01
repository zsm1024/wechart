package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_INTERNAL_STAFF")
public class Gw_internal_staff extends IEntity implements RowMapper<Gw_internal_staff>  {
		@Column(type=ColType.STRING,max_length="50",nullable=false)
		private String id = "";
		@Column(type=ColType.STRING,max_length="1")
		private String type = "";
		@Column(type=ColType.STRING,max_length="50")
		private String name = "";
		@Column(type=ColType.STRING,max_length="15")
		private String phone = "";
		@Column(type=ColType.STRING,max_length="18")
		private String cardnbr = "";
		@Column(type=ColType.STRING,max_length="20")
		private String worknbr = "";
		@Column(type=ColType.STRING,max_length="50")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		private String table_name="Gw_internal_staff";
		
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
		public void setType(String type){
			if(type==null||"".equals(type)){
				type = "";
			}
			this.type = type;
		}
		public String getType(){
			return this.type ;
		}
		public void setName(String name){
			if(name==null||"".equals(name)){
				name = "";
			}
			this.name = name;
		}
		public String getName(){
			return this.name ;
		}
		public void setPhone(String phone){
			if(phone==null||"".equals(phone)){
				phone = "";
			}
			this.phone = phone;
		}
		public String getPhone(){
			return this.phone ;
		}
		public void setCardnbr(String cardnbr){
			if(cardnbr==null||"".equals(cardnbr)){
				cardnbr = "";
			}
			this.cardnbr = cardnbr;
		}
		public String getCardnbr(){
			return this.cardnbr ;
		}
		public void setWorknbr(String worknbr){
			if(worknbr==null||"".equals(worknbr)){
				worknbr = "";
			}
			this.worknbr = worknbr;
		}
		public String getWorknbr(){
			return this.worknbr ;
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
		public void setStatus(String status){
			if(status==null||"".equals(status)){
				status = "";
			}
			this.status = status;
		}
		public String getStatus(){
			return this.status ;
		}
	
		public Gw_internal_staff mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_internal_staff entity = new  Gw_internal_staff();
			entity.setId(rs.getString("id"));
			entity.setType(rs.getString("type"));
			entity.setName(rs.getString("name"));
			entity.setPhone(rs.getString("phone"));
			entity.setCardnbr(rs.getString("cardnbr"));
			entity.setWorknbr(rs.getString("worknbr"));
			entity.setOpenid(rs.getString("openid"));
			entity.setStatus(rs.getString("status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("type:["+getType()+"];\n");
			info.append("name:["+getName()+"];\n");
			info.append("phone:["+getPhone()+"];\n");
			info.append("cardnbr:["+getCardnbr()+"];\n");
			info.append("worknbr:["+getWorknbr()+"];\n");
			info.append("openid:["+getOpenid()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("type".equals(col_name)){
				this.setType(String.valueOf(value));
			}
			if("name".equals(col_name)){
				this.setName(String.valueOf(value));
			}
			if("phone".equals(col_name)){
				this.setPhone(String.valueOf(value));
			}
			if("cardnbr".equals(col_name)){
				this.setCardnbr(String.valueOf(value));
			}
			if("worknbr".equals(col_name)){
				this.setWorknbr(String.valueOf(value));
			}
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("type".equals(col_name)){
				return this.getType();
			}
			if("name".equals(col_name)){
				return this.getName();
			}
			if("phone".equals(col_name)){
				return this.getPhone();
			}
			if("cardnbr".equals(col_name)){
				return this.getCardnbr();
			}
			if("worknbr".equals(col_name)){
				return this.getWorknbr();
			}
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			return null;
		}
}