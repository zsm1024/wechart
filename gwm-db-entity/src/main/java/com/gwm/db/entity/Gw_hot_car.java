package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_HOT_CAR")
public class Gw_hot_car extends IEntity implements RowMapper<Gw_hot_car>  {
		@Column(type=ColType.INT,max_length="10",nullable=false)
		private int id = 0;
		@Column(type=ColType.STRING,max_length="20",nullable=false)
		private String brand = "";
		@Column(type=ColType.STRING,max_length="20",nullable=false)
		private String models = "";
		@Column(type=ColType.STRING,max_length="500",nullable=false)
		private String configure = "";
		@Column(type=ColType.DOUBLE,max_length="14",nullable=false)
		private double price = 0.0d;
		@Column(type=ColType.STRING,max_length="100",nullable=false)
		private String amount_product = "";
		@Column(type=ColType.INT,max_length="10",nullable=false)
		private int purchase = 0;
		@Column(type=ColType.STRING,max_length="100")
		private String pic = "";
		@Column(type=ColType.DOUBLE,max_length="3")
		private double rate = 0.0d;
		@Column(type=ColType.STRING,max_length="10")
		private String loanterm = "";
		@Column(type=ColType.DOUBLE,max_length="14")
		private double minloanprice = 0.0d;
		@Column(type=ColType.DOUBLE,max_length="14")
		private double maxloanprice = 0.0d;
		private String table_name="Gw_hot_car";
		
		public String getTable_name(){
			return table_name;
		}

		public void setId(int id){
			this.id = id;
		}
		public void setId(String id){
			if(id==null||id.length()==0){
				id = "0";
			}
			this.id = Integer.parseInt(id);
		}
		public int getId(){
			return this.id ;
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
		public void setModels(String models){
			if(models==null||"".equals(models)){
				models = "";
			}
			this.models = models;
		}
		public String getModels(){
			return this.models ;
		}
		public void setConfigure(String configure){
			if(configure==null||"".equals(configure)){
				configure = "";
			}
			this.configure = configure;
		}
		public String getConfigure(){
			return this.configure ;
		}
		public void setPrice(String price){
			if(price!=null&&!"".equals(price)){
				this.price = Double.parseDouble(price);
			}
		}
		public void setPrice(double price){
			this.price = price;
		}
		public double getPrice(){
			return this.price ;
		}
		public void setAmount_product(String amount_product){
			if(amount_product==null||"".equals(amount_product)){
				amount_product = "";
			}
			this.amount_product = amount_product;
		}
		public String getAmount_product(){
			return this.amount_product ;
		}
		public void setPurchase(int purchase){
			this.purchase = purchase;
		}
		public void setPurchase(String purchase){
			if(purchase==null||purchase.length()==0){
				purchase = "0";
			}
			this.purchase = Integer.parseInt(purchase);
		}
		public int getPurchase(){
			return this.purchase ;
		}
		public void setPic(String pic){
			if(pic==null||"".equals(pic)){
				pic = "";
			}
			this.pic = pic;
		}
		public String getPic(){
			return this.pic ;
		}
		public void setRate(String rate){
			if(rate!=null&&!"".equals(rate)){
				this.rate = Double.parseDouble(rate);
			}
		}
		public void setRate(double rate){
			this.rate = rate;
		}
		public double getRate(){
			return this.rate ;
		}
		public void setLoanterm(String loanterm){
			if(loanterm==null||"".equals(loanterm)){
				loanterm = "";
			}
			this.loanterm = loanterm;
		}
		public String getLoanterm(){
			return this.loanterm ;
		}
		public void setMinloanprice(String minloanprice){
			if(minloanprice!=null&&!"".equals(minloanprice)){
				this.minloanprice = Double.parseDouble(minloanprice);
			}
		}
		public void setMinloanprice(double minloanprice){
			this.minloanprice = minloanprice;
		}
		public double getMinloanprice(){
			return this.minloanprice ;
		}
		public void setMaxloanprice(String maxloanprice){
			if(maxloanprice!=null&&!"".equals(maxloanprice)){
				this.maxloanprice = Double.parseDouble(maxloanprice);
			}
		}
		public void setMaxloanprice(double maxloanprice){
			this.maxloanprice = maxloanprice;
		}
		public double getMaxloanprice(){
			return this.maxloanprice ;
		}
	
		public Gw_hot_car mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_hot_car entity = new  Gw_hot_car();
			entity.setId(rs.getString("id"));
			entity.setBrand(rs.getString("brand"));
			entity.setModels(rs.getString("models"));
			entity.setConfigure(rs.getString("configure"));
			entity.setPrice(rs.getString("price"));
			entity.setAmount_product(rs.getString("amount_product"));
			entity.setPurchase(rs.getString("purchase"));
			entity.setPic(rs.getString("pic"));
			entity.setRate(rs.getString("rate"));
			entity.setLoanterm(rs.getString("loanterm"));
			entity.setMinloanprice(rs.getString("minloanprice"));
			entity.setMaxloanprice(rs.getString("maxloanprice"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("id:["+getId()+"];\n");
			info.append("brand:["+getBrand()+"];\n");
			info.append("models:["+getModels()+"];\n");
			info.append("configure:["+getConfigure()+"];\n");
			info.append("price:["+getPrice()+"];\n");
			info.append("amount_product:["+getAmount_product()+"];\n");
			info.append("purchase:["+getPurchase()+"];\n");
			info.append("pic:["+getPic()+"];\n");
			info.append("rate:["+getRate()+"];\n");
			info.append("loanterm:["+getLoanterm()+"];\n");
			info.append("minloanprice:["+getMinloanprice()+"];\n");
			info.append("maxloanprice:["+getMaxloanprice()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("id".equals(col_name)){
				this.setId(String.valueOf(value));
			}
			if("brand".equals(col_name)){
				this.setBrand(String.valueOf(value));
			}
			if("models".equals(col_name)){
				this.setModels(String.valueOf(value));
			}
			if("configure".equals(col_name)){
				this.setConfigure(String.valueOf(value));
			}
			if("price".equals(col_name)){
				this.setPrice(String.valueOf(value));
			}
			if("amount_product".equals(col_name)){
				this.setAmount_product(String.valueOf(value));
			}
			if("purchase".equals(col_name)){
				this.setPurchase(String.valueOf(value));
			}
			if("pic".equals(col_name)){
				this.setPic(String.valueOf(value));
			}
			if("rate".equals(col_name)){
				this.setRate(String.valueOf(value));
			}
			if("loanterm".equals(col_name)){
				this.setLoanterm(String.valueOf(value));
			}
			if("minloanprice".equals(col_name)){
				this.setMinloanprice(String.valueOf(value));
			}
			if("maxloanprice".equals(col_name)){
				this.setMaxloanprice(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("id".equals(col_name)){
				return this.getId();
			}
			if("brand".equals(col_name)){
				return this.getBrand();
			}
			if("models".equals(col_name)){
				return this.getModels();
			}
			if("configure".equals(col_name)){
				return this.getConfigure();
			}
			if("price".equals(col_name)){
				return this.getPrice();
			}
			if("amount_product".equals(col_name)){
				return this.getAmount_product();
			}
			if("purchase".equals(col_name)){
				return this.getPurchase();
			}
			if("pic".equals(col_name)){
				return this.getPic();
			}
			if("rate".equals(col_name)){
				return this.getRate();
			}
			if("loanterm".equals(col_name)){
				return this.getLoanterm();
			}
			if("minloanprice".equals(col_name)){
				return this.getMinloanprice();
			}
			if("maxloanprice".equals(col_name)){
				return this.getMaxloanprice();
			}
			return null;
		}
}