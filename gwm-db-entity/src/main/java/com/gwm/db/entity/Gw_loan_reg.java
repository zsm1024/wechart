package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_LOAN_REG")
public class Gw_loan_reg extends IEntity implements RowMapper<Gw_loan_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="100")
		private String application_num = "";
		@Column(type=ColType.STRING,max_length="100")
		private String brand = "";
		@Column(type=ColType.STRING,max_length="100")
		private String model = "";
		@Column(type=ColType.STRING,max_length="200")
		private String style = "";
		@Column(type=ColType.DOUBLE,max_length="14")
		private double first_amt = 0.0d;
		@Column(type=ColType.STRING,max_length="10")
		private String province = "";
		@Column(type=ColType.STRING,max_length="10")
		private String city = "";
		@Column(type=ColType.STRING,max_length="100")
		private String franchiser = "";
		@Column(type=ColType.STRING,max_length="200")
		private String name = "";
		@Column(type=ColType.STRING,max_length="50")
		private String card_id = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sex = "";
		@Column(type=ColType.STRING,max_length="20")
		private String phone = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		@Column(type=ColType.INT,max_length="10")
		private int apply_date = 0;
		@Column(type=ColType.INT,max_length="10")
		private int apply_time = 0;
		@Column(type=ColType.STRING,max_length="1")
		private String source = "";
		private String table_name="Gw_loan_reg";
		
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
		public void setApplication_num(String application_num){
			if(application_num==null||"".equals(application_num)){
				application_num = "";
			}
			this.application_num = application_num;
		}
		public String getApplication_num(){
			return this.application_num ;
		}
		public void setBrand(String brand){
			if(brand==null||"".equals(brand)){
				brand = "";
			}
			this.brand = brand;
		}
		public String getBrand(){
			return this.brand ;
		}
		public void setModel(String model){
			if(model==null||"".equals(model)){
				model = "";
			}
			this.model = model;
		}
		public String getModel(){
			return this.model ;
		}
		public void setStyle(String style){
			if(style==null||"".equals(style)){
				style = "";
			}
			this.style = style;
		}
		public String getStyle(){
			return this.style ;
		}
		public void setFirst_amt(String first_amt){
			if(first_amt!=null&&!"".equals(first_amt)){
				this.first_amt = Double.parseDouble(first_amt);
			}
		}
		public void setFirst_amt(double first_amt){
			this.first_amt = first_amt;
		}
		public double getFirst_amt(){
			return this.first_amt ;
		}
		public void setProvince(String province){
			if(province==null||"".equals(province)){
				province = "";
			}
			this.province = province;
		}
		public String getProvince(){
			return this.province ;
		}
		public void setCity(String city){
			if(city==null||"".equals(city)){
				city = "";
			}
			this.city = city;
		}
		public String getCity(){
			return this.city ;
		}
		public void setFranchiser(String franchiser){
			if(franchiser==null||"".equals(franchiser)){
				franchiser = "";
			}
			this.franchiser = franchiser;
		}
		public String getFranchiser(){
			return this.franchiser ;
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
		public void setCard_id(String card_id){
			if(card_id==null||"".equals(card_id)){
				card_id = "";
			}
			this.card_id = card_id;
		}
		public String getCard_id(){
			return this.card_id ;
		}
		public void setSex(String sex){
			if(sex==null||"".equals(sex)){
				sex = "";
			}
			this.sex = sex;
		}
		public String getSex(){
			return this.sex ;
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
		public void setStatus(String status){
			if(status==null||"".equals(status)){
				status = "";
			}
			this.status = status;
		}
		public String getStatus(){
			return this.status ;
		}
		public void setApply_date(int apply_date){
			this.apply_date = apply_date;
		}
		public void setApply_date(String apply_date){
			if(apply_date==null||apply_date.length()==0){
				apply_date = "0";
			}
			this.apply_date = Integer.parseInt(apply_date);
		}
		public int getApply_date(){
			return this.apply_date ;
		}
		public void setApply_time(int apply_time){
			this.apply_time = apply_time;
		}
		public void setApply_time(String apply_time){
			if(apply_time==null||apply_time.length()==0){
				apply_time = "0";
			}
			this.apply_time = Integer.parseInt(apply_time);
		}
		public int getApply_time(){
			return this.apply_time ;
		}
		public void setSource(String source){
			if(source==null||"".equals(source)){
				source = "";
			}
			this.source = source;
		}
		public String getSource(){
			return this.source ;
		}
	
		public Gw_loan_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_loan_reg entity = new  Gw_loan_reg();
			entity.setOpenid(rs.getString("openid"));
			entity.setApplication_num(rs.getString("application_num"));
			entity.setBrand(rs.getString("brand"));
			entity.setModel(rs.getString("model"));
			entity.setStyle(rs.getString("style"));
			entity.setFirst_amt(rs.getString("first_amt"));
			entity.setProvince(rs.getString("province"));
			entity.setCity(rs.getString("city"));
			entity.setFranchiser(rs.getString("franchiser"));
			entity.setName(rs.getString("name"));
			entity.setCard_id(rs.getString("card_id"));
			entity.setSex(rs.getString("sex"));
			entity.setPhone(rs.getString("phone"));
			entity.setStatus(rs.getString("status"));
			entity.setApply_date(rs.getString("apply_date"));
			entity.setApply_time(rs.getString("apply_time"));
			entity.setSource(rs.getString("source"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("application_num:["+getApplication_num()+"];\n");
			info.append("brand:["+getBrand()+"];\n");
			info.append("model:["+getModel()+"];\n");
			info.append("style:["+getStyle()+"];\n");
			info.append("first_amt:["+getFirst_amt()+"];\n");
			info.append("province:["+getProvince()+"];\n");
			info.append("city:["+getCity()+"];\n");
			info.append("franchiser:["+getFranchiser()+"];\n");
			info.append("name:["+getName()+"];\n");
			info.append("card_id:["+getCard_id()+"];\n");
			info.append("sex:["+getSex()+"];\n");
			info.append("phone:["+getPhone()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			info.append("apply_date:["+getApply_date()+"];\n");
			info.append("apply_time:["+getApply_time()+"];\n");
			info.append("source:["+getSource()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("application_num".equals(col_name)){
				this.setApplication_num(String.valueOf(value));
			}
			if("brand".equals(col_name)){
				this.setBrand(String.valueOf(value));
			}
			if("model".equals(col_name)){
				this.setModel(String.valueOf(value));
			}
			if("style".equals(col_name)){
				this.setStyle(String.valueOf(value));
			}
			if("first_amt".equals(col_name)){
				this.setFirst_amt(String.valueOf(value));
			}
			if("province".equals(col_name)){
				this.setProvince(String.valueOf(value));
			}
			if("city".equals(col_name)){
				this.setCity(String.valueOf(value));
			}
			if("franchiser".equals(col_name)){
				this.setFranchiser(String.valueOf(value));
			}
			if("name".equals(col_name)){
				this.setName(String.valueOf(value));
			}
			if("card_id".equals(col_name)){
				this.setCard_id(String.valueOf(value));
			}
			if("sex".equals(col_name)){
				this.setSex(String.valueOf(value));
			}
			if("phone".equals(col_name)){
				this.setPhone(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
			if("apply_date".equals(col_name)){
				this.setApply_date(String.valueOf(value));
			}
			if("apply_time".equals(col_name)){
				this.setApply_time(String.valueOf(value));
			}
			if("source".equals(col_name)){
				this.setSource(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("application_num".equals(col_name)){
				return this.getApplication_num();
			}
			if("brand".equals(col_name)){
				return this.getBrand();
			}
			if("model".equals(col_name)){
				return this.getModel();
			}
			if("style".equals(col_name)){
				return this.getStyle();
			}
			if("first_amt".equals(col_name)){
				return this.getFirst_amt();
			}
			if("province".equals(col_name)){
				return this.getProvince();
			}
			if("city".equals(col_name)){
				return this.getCity();
			}
			if("franchiser".equals(col_name)){
				return this.getFranchiser();
			}
			if("name".equals(col_name)){
				return this.getName();
			}
			if("card_id".equals(col_name)){
				return this.getCard_id();
			}
			if("sex".equals(col_name)){
				return this.getSex();
			}
			if("phone".equals(col_name)){
				return this.getPhone();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			if("apply_date".equals(col_name)){
				return this.getApply_date();
			}
			if("apply_time".equals(col_name)){
				return this.getApply_time();
			}
			if("source".equals(col_name)){
				return this.getSource();
			}
			return null;
		}
}