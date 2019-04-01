package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_MODIFY_TEL_REG")
public class Gw_modify_tel_reg extends IEntity implements RowMapper<Gw_modify_tel_reg>  {
		@Column(type=ColType.STRING,max_length="100")
		private String openid = "";
		@Column(type=ColType.STRING,max_length="100")
		private String tel_reg_num = "";
		@Column(type=ColType.STRING,max_length="100")
		private String applicationer = "";
		@Column(type=ColType.STRING,max_length="100")
		private String contract_code = "";
		@Column(type=ColType.STRING,max_length="20")
		private String old_phone = "";
		@Column(type=ColType.STRING,max_length="20")
		private String new_phone = "";
		@Column(type=ColType.STRING,max_length="1")
		private String status = "";
		@Column(type=ColType.STRING,max_length="100")
		private String reclaim_person = "";
		@Column(type=ColType.INT,max_length="10")
		private int apply_date = 0;
		@Column(type=ColType.INT,max_length="10")
		private int apply_time = 0;
		private String table_name="Gw_modify_tel_reg";
		
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
		public void setTel_reg_num(String tel_reg_num){
			if(tel_reg_num==null||"".equals(tel_reg_num)){
				tel_reg_num = "";
			}
			this.tel_reg_num = tel_reg_num;
		}
		public String getTel_reg_num(){
			return this.tel_reg_num ;
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
		public void setContract_code(String contract_code){
			if(contract_code==null||"".equals(contract_code)){
				contract_code = "";
			}
			this.contract_code = contract_code;
		}
		public String getContract_code(){
			return this.contract_code ;
		}
		public void setOld_phone(String old_phone){
			if(old_phone==null||"".equals(old_phone)){
				old_phone = "";
			}
			this.old_phone = old_phone;
		}
		public String getOld_phone(){
			return this.old_phone ;
		}
		public void setNew_phone(String new_phone){
			if(new_phone==null||"".equals(new_phone)){
				new_phone = "";
			}
			this.new_phone = new_phone;
		}
		public String getNew_phone(){
			return this.new_phone ;
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
	
		public Gw_modify_tel_reg mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_modify_tel_reg entity = new  Gw_modify_tel_reg();
			entity.setOpenid(rs.getString("openid"));
			entity.setTel_reg_num(rs.getString("tel_reg_num"));
			entity.setApplicationer(rs.getString("applicationer"));
			entity.setContract_code(rs.getString("contract_code"));
			entity.setOld_phone(rs.getString("old_phone"));
			entity.setNew_phone(rs.getString("new_phone"));
			entity.setStatus(rs.getString("status"));
			entity.setReclaim_person(rs.getString("reclaim_person"));
			entity.setApply_date(rs.getString("apply_date"));
			entity.setApply_time(rs.getString("apply_time"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("tel_reg_num:["+getTel_reg_num()+"];\n");
			info.append("applicationer:["+getApplicationer()+"];\n");
			info.append("contract_code:["+getContract_code()+"];\n");
			info.append("old_phone:["+getOld_phone()+"];\n");
			info.append("new_phone:["+getNew_phone()+"];\n");
			info.append("status:["+getStatus()+"];\n");
			info.append("reclaim_person:["+getReclaim_person()+"];\n");
			info.append("apply_date:["+getApply_date()+"];\n");
			info.append("apply_time:["+getApply_time()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("tel_reg_num".equals(col_name)){
				this.setTel_reg_num(String.valueOf(value));
			}
			if("applicationer".equals(col_name)){
				this.setApplicationer(String.valueOf(value));
			}
			if("contract_code".equals(col_name)){
				this.setContract_code(String.valueOf(value));
			}
			if("old_phone".equals(col_name)){
				this.setOld_phone(String.valueOf(value));
			}
			if("new_phone".equals(col_name)){
				this.setNew_phone(String.valueOf(value));
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
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("tel_reg_num".equals(col_name)){
				return this.getTel_reg_num();
			}
			if("applicationer".equals(col_name)){
				return this.getApplicationer();
			}
			if("contract_code".equals(col_name)){
				return this.getContract_code();
			}
			if("old_phone".equals(col_name)){
				return this.getOld_phone();
			}
			if("new_phone".equals(col_name)){
				return this.getNew_phone();
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
			return null;
		}
}