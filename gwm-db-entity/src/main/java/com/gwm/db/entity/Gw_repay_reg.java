package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_REPAY_REG")
public class Gw_repay_reg extends IEntity implements RowMapper<Gw_repay_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="100")
		private String application_num = "";
		@Column(type=ColType.STRING,max_length="100")
		private String applicationer = "";
		@Column(type=ColType.INT,max_length="10")
		private int apply_repay_date = 0;
		@Column(type=ColType.DOUBLE,max_length="14")
		private double surplus_amt = 0.0d;
		@Column(type=ColType.DOUBLE,max_length="14")
		private double interest = 0.0d;
		@Column(type=ColType.DOUBLE,max_length="14")
		private double penalty = 0.0d;
		@Column(type=ColType.DOUBLE,max_length="14")
		private double total_amt = 0.0d;
		@Column(type=ColType.STRING,max_length="100")
		private String contract_code = "";
		@Column(type=ColType.STRING,max_length="1")
		private String repay_type = "";
		@Column(type=ColType.STRING,max_length="200")
		private String repay_voucher = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		@Column(type=ColType.STRING,max_length="100")
		private String reclaim_person = "";
		@Column(type=ColType.STRING,max_length="8")
		private String apply_date = "";
		@Column(type=ColType.STRING,max_length="6")
		private String apply_time = "";
		@Column(type=ColType.STRING,max_length="14")
		private String apply_date_time = "";
		private String table_name="Gw_repay_reg";
		
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
		public void setApplicationer(String applicationer){
			if(applicationer==null||"".equals(applicationer)){
				applicationer = "";
			}
			this.applicationer = applicationer;
		}
		public String getApplicationer(){
			return this.applicationer ;
		}
		public void setApply_repay_date(int apply_repay_date){
			this.apply_repay_date = apply_repay_date;
		}
		public void setApply_repay_date(String apply_repay_date){
			if(apply_repay_date==null||apply_repay_date.length()==0){
				apply_repay_date = "0";
			}
			this.apply_repay_date = Integer.parseInt(apply_repay_date);
		}
		public int getApply_repay_date(){
			return this.apply_repay_date ;
		}
		public void setSurplus_amt(String surplus_amt){
			if(surplus_amt!=null&&!"".equals(surplus_amt)){
				this.surplus_amt = Double.parseDouble(surplus_amt);
			}
		}
		public void setSurplus_amt(double surplus_amt){
			this.surplus_amt = surplus_amt;
		}
		public double getSurplus_amt(){
			return this.surplus_amt ;
		}
		public void setInterest(String interest){
			if(interest!=null&&!"".equals(interest)){
				this.interest = Double.parseDouble(interest);
			}
		}
		public void setInterest(double interest){
			this.interest = interest;
		}
		public double getInterest(){
			return this.interest ;
		}
		public void setPenalty(String penalty){
			if(penalty!=null&&!"".equals(penalty)){
				this.penalty = Double.parseDouble(penalty);
			}
		}
		public void setPenalty(double penalty){
			this.penalty = penalty;
		}
		public double getPenalty(){
			return this.penalty ;
		}
		public void setTotal_amt(String total_amt){
			if(total_amt!=null&&!"".equals(total_amt)){
				this.total_amt = Double.parseDouble(total_amt);
			}
		}
		public void setTotal_amt(double total_amt){
			this.total_amt = total_amt;
		}
		public double getTotal_amt(){
			return this.total_amt ;
		}
		public void setContract_code(String contract_code){
			if(contract_code==null||"".equals(contract_code)){
				contract_code = "";
			}
			this.contract_code = contract_code;
		}
		public String getContract_code(){
			return this.contract_code ;
		}
		public void setRepay_type(String repay_type){
			if(repay_type==null||"".equals(repay_type)){
				repay_type = "";
			}
			this.repay_type = repay_type;
		}
		public String getRepay_type(){
			return this.repay_type ;
		}
		public void setRepay_voucher(String repay_voucher){
			if(repay_voucher==null||"".equals(repay_voucher)){
				repay_voucher = "";
			}
			this.repay_voucher = repay_voucher;
		}
		public String getRepay_voucher(){
			return this.repay_voucher ;
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
		public void setReclaim_person(String reclaim_person){
			if(reclaim_person==null||"".equals(reclaim_person)){
				reclaim_person = "";
			}
			this.reclaim_person = reclaim_person;
		}
		public String getReclaim_person(){
			return this.reclaim_person ;
		}
		public void setApply_date(String apply_date){
			if(apply_date==null||"".equals(apply_date)){
				apply_date = "";
			}
			this.apply_date = apply_date;
		}
		public String getApply_date(){
			return this.apply_date ;
		}
		public void setApply_time(String apply_time){
			if(apply_time==null||"".equals(apply_time)){
				apply_time = "";
			}
			this.apply_time = apply_time;
		}
		public String getApply_time(){
			return this.apply_time ;
		}
		public void setApply_date_time(String apply_date_time){
			if(apply_date_time==null||"".equals(apply_date_time)){
				apply_date_time = "";
			}
			this.apply_date_time = apply_date_time;
		}
		public String getApply_date_time(){
			return this.apply_date_time ;
		}
	
		public Gw_repay_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_repay_reg entity = new  Gw_repay_reg();
			entity.setOpenid(rs.getString("openid"));
			entity.setApplication_num(rs.getString("application_num"));
			entity.setApplicationer(rs.getString("applicationer"));
			entity.setApply_repay_date(rs.getString("apply_repay_date"));
			entity.setSurplus_amt(rs.getString("surplus_amt"));
			entity.setInterest(rs.getString("interest"));
			entity.setPenalty(rs.getString("penalty"));
			entity.setTotal_amt(rs.getString("total_amt"));
			entity.setContract_code(rs.getString("contract_code"));
			entity.setRepay_type(rs.getString("repay_type"));
			entity.setRepay_voucher(rs.getString("repay_voucher"));
			entity.setStatus(rs.getString("status"));
			entity.setReclaim_person(rs.getString("reclaim_person"));
			entity.setApply_date(rs.getString("apply_date"));
			entity.setApply_time(rs.getString("apply_time"));
			entity.setApply_date_time(rs.getString("apply_date_time"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("application_num:["+getApplication_num()+"];\n");
			info.append("applicationer:["+getApplicationer()+"];\n");
			info.append("apply_repay_date:["+getApply_repay_date()+"];\n");
			info.append("surplus_amt:["+getSurplus_amt()+"];\n");
			info.append("interest:["+getInterest()+"];\n");
			info.append("penalty:["+getPenalty()+"];\n");
			info.append("total_amt:["+getTotal_amt()+"];\n");
			info.append("contract_code:["+getContract_code()+"];\n");
			info.append("repay_type:["+getRepay_type()+"];\n");
			info.append("repay_voucher:["+getRepay_voucher()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			info.append("reclaim_person:["+getReclaim_person()+"];\n");
			info.append("apply_date:["+getApply_date()+"];\n");
			info.append("apply_time:["+getApply_time()+"];\n");
			info.append("apply_date_time:["+getApply_date_time()+"];\n");
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
			if("applicationer".equals(col_name)){
				this.setApplicationer(String.valueOf(value));
			}
			if("apply_repay_date".equals(col_name)){
				this.setApply_repay_date(String.valueOf(value));
			}
			if("surplus_amt".equals(col_name)){
				this.setSurplus_amt(String.valueOf(value));
			}
			if("interest".equals(col_name)){
				this.setInterest(String.valueOf(value));
			}
			if("penalty".equals(col_name)){
				this.setPenalty(String.valueOf(value));
			}
			if("total_amt".equals(col_name)){
				this.setTotal_amt(String.valueOf(value));
			}
			if("contract_code".equals(col_name)){
				this.setContract_code(String.valueOf(value));
			}
			if("repay_type".equals(col_name)){
				this.setRepay_type(String.valueOf(value));
			}
			if("repay_voucher".equals(col_name)){
				this.setRepay_voucher(String.valueOf(value));
			}
			if("status".equals(col_name)){
				this.setStatus(String.valueOf(value));
			}
			if("reclaim_person".equals(col_name)){
				this.setReclaim_person(String.valueOf(value));
			}
			if("apply_date".equals(col_name)){
				this.setApply_date(String.valueOf(value));
			}
			if("apply_time".equals(col_name)){
				this.setApply_time(String.valueOf(value));
			}
			if("apply_date_time".equals(col_name)){
				this.setApply_date_time(String.valueOf(value));
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
			if("applicationer".equals(col_name)){
				return this.getApplicationer();
			}
			if("apply_repay_date".equals(col_name)){
				return this.getApply_repay_date();
			}
			if("surplus_amt".equals(col_name)){
				return this.getSurplus_amt();
			}
			if("interest".equals(col_name)){
				return this.getInterest();
			}
			if("penalty".equals(col_name)){
				return this.getPenalty();
			}
			if("total_amt".equals(col_name)){
				return this.getTotal_amt();
			}
			if("contract_code".equals(col_name)){
				return this.getContract_code();
			}
			if("repay_type".equals(col_name)){
				return this.getRepay_type();
			}
			if("repay_voucher".equals(col_name)){
				return this.getRepay_voucher();
			}
			if("status".equals(col_name)){
				return this.getStatus();
			}
			if("reclaim_person".equals(col_name)){
				return this.getReclaim_person();
			}
			if("apply_date".equals(col_name)){
				return this.getApply_date();
			}
			if("apply_time".equals(col_name)){
				return this.getApply_time();
			}
			if("apply_date_time".equals(col_name)){
				return this.getApply_date_time();
			}
			return null;
		}
}