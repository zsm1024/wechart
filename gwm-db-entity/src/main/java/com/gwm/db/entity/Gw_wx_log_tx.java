package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_LOG_TX")
public class Gw_wx_log_tx extends IEntity implements RowMapper<Gw_wx_log_tx>  {
		@Column(type=ColType.INT,max_length="10")
		private int tx_date = 0;
		@Column(type=ColType.INT,max_length="10")
		private int tx_time = 0;
		@Column(type=ColType.STRING,max_length="80")
		private String tr_br_id = "";
		@Column(type=ColType.STRING,max_length="160")
		private String app_code = "";
		@Column(type=ColType.STRING,max_length="160")
		private String fuc_code = "";
		@Column(type=ColType.STRING,max_length="80")
		private String user_id = "";
		@Column(type=ColType.STRING,max_length="1")
		private String opr_type = "";
		@Column(type=ColType.STRING,max_length="4000")
		private String tx_content = "";
		@Column(type=ColType.STRING,max_length="200")
		private String remark = "";
		@Column(type=ColType.STRING,max_length="1")
		private String tx_status = "";
		private String table_name="Gw_wx_log_tx";
		
		public String getTable_name(){
			return table_name;
		}

		public void setTx_date(int tx_date){
			this.tx_date = tx_date;
		}
		public void setTx_date(String tx_date){
			if(tx_date==null||tx_date.length()==0){
				tx_date = "0";
			}
			this.tx_date = Integer.parseInt(tx_date);
		}
		public int getTx_date(){
			return this.tx_date ;
		}
		public void setTx_time(int tx_time){
			this.tx_time = tx_time;
		}
		public void setTx_time(String tx_time){
			if(tx_time==null||tx_time.length()==0){
				tx_time = "0";
			}
			this.tx_time = Integer.parseInt(tx_time);
		}
		public int getTx_time(){
			return this.tx_time ;
		}
		public void setTr_br_id(String tr_br_id){
			if(tr_br_id==null||"".equals(tr_br_id)){
				tr_br_id = "";
			}
			this.tr_br_id = tr_br_id;
		}
		public String getTr_br_id(){
			return this.tr_br_id ;
		}
		public void setApp_code(String app_code){
			if(app_code==null||"".equals(app_code)){
				app_code = "";
			}
			this.app_code = app_code;
		}
		public String getApp_code(){
			return this.app_code ;
		}
		public void setFuc_code(String fuc_code){
			if(fuc_code==null||"".equals(fuc_code)){
				fuc_code = "";
			}
			this.fuc_code = fuc_code;
		}
		public String getFuc_code(){
			return this.fuc_code ;
		}
		public void setUser_id(String user_id){
			if(user_id==null||"".equals(user_id)){
				user_id = "";
			}
			this.user_id = user_id;
		}
		public String getUser_id(){
			return this.user_id ;
		}
		public void setOpr_type(String opr_type){
			if(opr_type==null||"".equals(opr_type)){
				opr_type = "";
			}
			this.opr_type = opr_type;
		}
		public String getOpr_type(){
			return this.opr_type ;
		}
		public void setTx_content(String tx_content){
			if(tx_content==null||"".equals(tx_content)){
				tx_content = "";
			}
			this.tx_content = tx_content;
		}
		public String getTx_content(){
			return this.tx_content ;
		}
		public void setRemark(String remark){
			if(remark==null||"".equals(remark)){
				remark = "";
			}
			this.remark = remark;
		}
		public String getRemark(){
			return this.remark ;
		}
		public void setTx_status(String tx_status){
			if(tx_status==null||"".equals(tx_status)){
				tx_status = "";
			}
			this.tx_status = tx_status;
		}
		public String getTx_status(){
			return this.tx_status ;
		}
	
		public Gw_wx_log_tx mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_log_tx entity = new  Gw_wx_log_tx();
			entity.setTx_date(rs.getString("tx_date"));
			entity.setTx_time(rs.getString("tx_time"));
			entity.setTr_br_id(rs.getString("tr_br_id"));
			entity.setApp_code(rs.getString("app_code"));
			entity.setFuc_code(rs.getString("fuc_code"));
			entity.setUser_id(rs.getString("user_id"));
			entity.setOpr_type(rs.getString("opr_type"));
			entity.setTx_content(rs.getString("tx_content"));
			entity.setRemark(rs.getString("remark"));
			entity.setTx_status(rs.getString("tx_status"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("tx_date:["+getTx_date()+"];\n");
			info.append("tx_time:["+getTx_time()+"];\n");
			info.append("tr_br_id:["+getTr_br_id()+"];\n");
			info.append("app_code:["+getApp_code()+"];\n");
			info.append("fuc_code:["+getFuc_code()+"];\n");
			info.append("user_id:["+getUser_id()+"];\n");
			info.append("opr_type:["+getOpr_type()+"];\n");
			info.append("tx_content:["+getTx_content()+"];\n");
			info.append("remark:["+getRemark()+"];\n");
			info.append("tx_status:["+getTx_status()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("tx_date".equals(col_name)){
				this.setTx_date(String.valueOf(value));
			}
			if("tx_time".equals(col_name)){
				this.setTx_time(String.valueOf(value));
			}
			if("tr_br_id".equals(col_name)){
				this.setTr_br_id(String.valueOf(value));
			}
			if("app_code".equals(col_name)){
				this.setApp_code(String.valueOf(value));
			}
			if("fuc_code".equals(col_name)){
				this.setFuc_code(String.valueOf(value));
			}
			if("user_id".equals(col_name)){
				this.setUser_id(String.valueOf(value));
			}
			if("opr_type".equals(col_name)){
				this.setOpr_type(String.valueOf(value));
			}
			if("tx_content".equals(col_name)){
				this.setTx_content(String.valueOf(value));
			}
			if("remark".equals(col_name)){
				this.setRemark(String.valueOf(value));
			}
			if("tx_status".equals(col_name)){
				this.setTx_status(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("tx_date".equals(col_name)){
				return this.getTx_date();
			}
			if("tx_time".equals(col_name)){
				return this.getTx_time();
			}
			if("tr_br_id".equals(col_name)){
				return this.getTr_br_id();
			}
			if("app_code".equals(col_name)){
				return this.getApp_code();
			}
			if("fuc_code".equals(col_name)){
				return this.getFuc_code();
			}
			if("user_id".equals(col_name)){
				return this.getUser_id();
			}
			if("opr_type".equals(col_name)){
				return this.getOpr_type();
			}
			if("tx_content".equals(col_name)){
				return this.getTx_content();
			}
			if("remark".equals(col_name)){
				return this.getRemark();
			}
			if("tx_status".equals(col_name)){
				return this.getTx_status();
			}
			return null;
		}
}