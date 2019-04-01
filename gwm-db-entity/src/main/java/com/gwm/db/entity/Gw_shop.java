package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_SHOP")
public class Gw_shop extends IEntity implements RowMapper<Gw_shop>  {
		@Column(type=ColType.STRING,max_length="20")
		private String sh_id = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sh_number = "";
		@Column(type=ColType.STRING,max_length="200")
		private String sh_name = "";
		@Column(type=ColType.STRING,max_length="50")
		private String sh_shortname = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sh_state = "";
		@Column(type=ColType.STRING,max_length="60")
		private String sh_province = "";
		@Column(type=ColType.STRING,max_length="60")
		private String sh_city = "";
		@Column(type=ColType.STRING,max_length="60")
		private String sh_county = "";
		@Column(type=ColType.STRING,max_length="200")
		private String sh_address = "";
		@Column(type=ColType.STRING,max_length="60")
		private String agentbrands = "";
		@Column(type=ColType.STRING,max_length="100")
		private String sh_salehotline = "";
		@Column(type=ColType.STRING,max_length="100")
		private String sh_saletime = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sh_salelongitude = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sh_salelatitude = "";
		@Column(type=ColType.STRING,max_length="20")
		private String sh_financecoorp = "";
		@Column(type=ColType.STRING,max_length="30")
		private String sh_financecoorpdate = "";
		@Column(type=ColType.STRING,max_length="30")
		private String sh_financecoorpenddate = "";
		@Column(type=ColType.STRING,max_length="3000")
		private String sh_salecarbrand = "";
		@Column(type=ColType.STRING,max_length="30")
		private String sh_createtime = "";
		@Column(type=ColType.STRING,max_length="30")
		private String sh_lastupdatetime = "";
		private String table_name="Gw_shop";
		
		public String getTable_name(){
			return table_name;
		}

		public void setSh_id(String sh_id){
			if(sh_id==null||"".equals(sh_id)){
				sh_id = "";
			}
			this.sh_id = sh_id;
		}
		public String getSh_id(){
			return this.sh_id ;
		}
		public void setSh_number(String sh_number){
			if(sh_number==null||"".equals(sh_number)){
				sh_number = "";
			}
			this.sh_number = sh_number;
		}
		public String getSh_number(){
			return this.sh_number ;
		}
		public void setSh_name(String sh_name){
			if(sh_name==null||"".equals(sh_name)){
				sh_name = "";
			}
			this.sh_name = sh_name;
		}
		public String getSh_name(){
			return this.sh_name ;
		}
		public void setSh_shortname(String sh_shortname){
			if(sh_shortname==null||"".equals(sh_shortname)){
				sh_shortname = "";
			}
			this.sh_shortname = sh_shortname;
		}
		public String getSh_shortname(){
			return this.sh_shortname ;
		}
		public void setSh_state(String sh_state){
			if(sh_state==null||"".equals(sh_state)){
				sh_state = "";
			}
			this.sh_state = sh_state;
		}
		public String getSh_state(){
			return this.sh_state ;
		}
		public void setSh_province(String sh_province){
			if(sh_province==null||"".equals(sh_province)){
				sh_province = "";
			}
			this.sh_province = sh_province;
		}
		public String getSh_province(){
			return this.sh_province ;
		}
		public void setSh_city(String sh_city){
			if(sh_city==null||"".equals(sh_city)){
				sh_city = "";
			}
			this.sh_city = sh_city;
		}
		public String getSh_city(){
			return this.sh_city ;
		}
		public void setSh_county(String sh_county){
			if(sh_county==null||"".equals(sh_county)){
				sh_county = "";
			}
			this.sh_county = sh_county;
		}
		public String getSh_county(){
			return this.sh_county ;
		}
		public void setSh_address(String sh_address){
			if(sh_address==null||"".equals(sh_address)){
				sh_address = "";
			}
			this.sh_address = sh_address;
		}
		public String getSh_address(){
			return this.sh_address ;
		}
		public void setAgentbrands(String agentbrands){
			if(agentbrands==null||"".equals(agentbrands)){
				agentbrands = "";
			}
			this.agentbrands = agentbrands;
		}
		public String getAgentbrands(){
			return this.agentbrands ;
		}
		public void setSh_salehotline(String sh_salehotline){
			if(sh_salehotline==null||"".equals(sh_salehotline)){
				sh_salehotline = "";
			}
			this.sh_salehotline = sh_salehotline;
		}
		public String getSh_salehotline(){
			return this.sh_salehotline ;
		}
		public void setSh_saletime(String sh_saletime){
			if(sh_saletime==null||"".equals(sh_saletime)){
				sh_saletime = "";
			}
			this.sh_saletime = sh_saletime;
		}
		public String getSh_saletime(){
			return this.sh_saletime ;
		}
		public void setSh_salelongitude(String sh_salelongitude){
			if(sh_salelongitude==null||"".equals(sh_salelongitude)){
				sh_salelongitude = "";
			}
			this.sh_salelongitude = sh_salelongitude;
		}
		public String getSh_salelongitude(){
			return this.sh_salelongitude ;
		}
		public void setSh_salelatitude(String sh_salelatitude){
			if(sh_salelatitude==null||"".equals(sh_salelatitude)){
				sh_salelatitude = "";
			}
			this.sh_salelatitude = sh_salelatitude;
		}
		public String getSh_salelatitude(){
			return this.sh_salelatitude ;
		}
		public void setSh_financecoorp(String sh_financecoorp){
			if(sh_financecoorp==null||"".equals(sh_financecoorp)){
				sh_financecoorp = "";
			}
			this.sh_financecoorp = sh_financecoorp;
		}
		public String getSh_financecoorp(){
			return this.sh_financecoorp ;
		}
		public void setSh_financecoorpdate(String sh_financecoorpdate){
			if(sh_financecoorpdate==null||"".equals(sh_financecoorpdate)){
				sh_financecoorpdate = "";
			}
			this.sh_financecoorpdate = sh_financecoorpdate;
		}
		public String getSh_financecoorpdate(){
			return this.sh_financecoorpdate ;
		}
		public void setSh_financecoorpenddate(String sh_financecoorpenddate){
			if(sh_financecoorpenddate==null||"".equals(sh_financecoorpenddate)){
				sh_financecoorpenddate = "";
			}
			this.sh_financecoorpenddate = sh_financecoorpenddate;
		}
		public String getSh_financecoorpenddate(){
			return this.sh_financecoorpenddate ;
		}
		public void setSh_salecarbrand(String sh_salecarbrand){
			if(sh_salecarbrand==null||"".equals(sh_salecarbrand)){
				sh_salecarbrand = "";
			}
			this.sh_salecarbrand = sh_salecarbrand;
		}
		public String getSh_salecarbrand(){
			return this.sh_salecarbrand ;
		}
		public void setSh_createtime(String sh_createtime){
			if(sh_createtime==null||"".equals(sh_createtime)){
				sh_createtime = "";
			}
			this.sh_createtime = sh_createtime;
		}
		public String getSh_createtime(){
			return this.sh_createtime ;
		}
		public void setSh_lastupdatetime(String sh_lastupdatetime){
			if(sh_lastupdatetime==null||"".equals(sh_lastupdatetime)){
				sh_lastupdatetime = "";
			}
			this.sh_lastupdatetime = sh_lastupdatetime;
		}
		public String getSh_lastupdatetime(){
			return this.sh_lastupdatetime ;
		}
	
		public Gw_shop mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_shop entity = new  Gw_shop();
			entity.setSh_id(rs.getString("sh_id"));
			entity.setSh_number(rs.getString("sh_number"));
			entity.setSh_name(rs.getString("sh_name"));
			entity.setSh_shortname(rs.getString("sh_shortname"));
			entity.setSh_state(rs.getString("sh_state"));
			entity.setSh_province(rs.getString("sh_province"));
			entity.setSh_city(rs.getString("sh_city"));
			entity.setSh_county(rs.getString("sh_county"));
			entity.setSh_address(rs.getString("sh_address"));
			entity.setAgentbrands(rs.getString("agentbrands"));
			entity.setSh_salehotline(rs.getString("sh_salehotline"));
			entity.setSh_saletime(rs.getString("sh_saletime"));
			entity.setSh_salelongitude(rs.getString("sh_salelongitude"));
			entity.setSh_salelatitude(rs.getString("sh_salelatitude"));
			entity.setSh_financecoorp(rs.getString("sh_financecoorp"));
			entity.setSh_financecoorpdate(rs.getString("sh_financecoorpdate"));
			entity.setSh_financecoorpenddate(rs.getString("sh_financecoorpenddate"));
			entity.setSh_salecarbrand(rs.getString("sh_salecarbrand"));
			entity.setSh_createtime(rs.getString("sh_createtime"));
			entity.setSh_lastupdatetime(rs.getString("sh_lastupdatetime"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("sh_id:["+getSh_id()+"];\n");
			info.append("sh_number:["+getSh_number()+"];\n");
			info.append("sh_name:["+getSh_name()+"];\n");
			info.append("sh_shortname:["+getSh_shortname()+"];\n");
			info.append("sh_state:["+getSh_state()+"];\n");
			info.append("sh_province:["+getSh_province()+"];\n");
			info.append("sh_city:["+getSh_city()+"];\n");
			info.append("sh_county:["+getSh_county()+"];\n");
			info.append("sh_address:["+getSh_address()+"];\n");
			info.append("agentbrands:["+getAgentbrands()+"];\n");
			info.append("sh_salehotline:["+getSh_salehotline()+"];\n");
			info.append("sh_saletime:["+getSh_saletime()+"];\n");
			info.append("sh_salelongitude:["+getSh_salelongitude()+"];\n");
			info.append("sh_salelatitude:["+getSh_salelatitude()+"];\n");
			info.append("sh_financecoorp:["+getSh_financecoorp()+"];\n");
			info.append("sh_financecoorpdate:["+getSh_financecoorpdate()+"];\n");
			info.append("sh_financecoorpenddate:["+getSh_financecoorpenddate()+"];\n");
			info.append("sh_salecarbrand:["+getSh_salecarbrand()+"];\n");
			info.append("sh_createtime:["+getSh_createtime()+"];\n");
			info.append("sh_lastupdatetime:["+getSh_lastupdatetime()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("sh_id".equals(col_name)){
				this.setSh_id(String.valueOf(value));
			}
			if("sh_number".equals(col_name)){
				this.setSh_number(String.valueOf(value));
			}
			if("sh_name".equals(col_name)){
				this.setSh_name(String.valueOf(value));
			}
			if("sh_shortname".equals(col_name)){
				this.setSh_shortname(String.valueOf(value));
			}
			if("sh_state".equals(col_name)){
				this.setSh_state(String.valueOf(value));
			}
			if("sh_province".equals(col_name)){
				this.setSh_province(String.valueOf(value));
			}
			if("sh_city".equals(col_name)){
				this.setSh_city(String.valueOf(value));
			}
			if("sh_county".equals(col_name)){
				this.setSh_county(String.valueOf(value));
			}
			if("sh_address".equals(col_name)){
				this.setSh_address(String.valueOf(value));
			}
			if("agentbrands".equals(col_name)){
				this.setAgentbrands(String.valueOf(value));
			}
			if("sh_salehotline".equals(col_name)){
				this.setSh_salehotline(String.valueOf(value));
			}
			if("sh_saletime".equals(col_name)){
				this.setSh_saletime(String.valueOf(value));
			}
			if("sh_salelongitude".equals(col_name)){
				this.setSh_salelongitude(String.valueOf(value));
			}
			if("sh_salelatitude".equals(col_name)){
				this.setSh_salelatitude(String.valueOf(value));
			}
			if("sh_financecoorp".equals(col_name)){
				this.setSh_financecoorp(String.valueOf(value));
			}
			if("sh_financecoorpdate".equals(col_name)){
				this.setSh_financecoorpdate(String.valueOf(value));
			}
			if("sh_financecoorpenddate".equals(col_name)){
				this.setSh_financecoorpenddate(String.valueOf(value));
			}
			if("sh_salecarbrand".equals(col_name)){
				this.setSh_salecarbrand(String.valueOf(value));
			}
			if("sh_createtime".equals(col_name)){
				this.setSh_createtime(String.valueOf(value));
			}
			if("sh_lastupdatetime".equals(col_name)){
				this.setSh_lastupdatetime(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("sh_id".equals(col_name)){
				return this.getSh_id();
			}
			if("sh_number".equals(col_name)){
				return this.getSh_number();
			}
			if("sh_name".equals(col_name)){
				return this.getSh_name();
			}
			if("sh_shortname".equals(col_name)){
				return this.getSh_shortname();
			}
			if("sh_state".equals(col_name)){
				return this.getSh_state();
			}
			if("sh_province".equals(col_name)){
				return this.getSh_province();
			}
			if("sh_city".equals(col_name)){
				return this.getSh_city();
			}
			if("sh_county".equals(col_name)){
				return this.getSh_county();
			}
			if("sh_address".equals(col_name)){
				return this.getSh_address();
			}
			if("agentbrands".equals(col_name)){
				return this.getAgentbrands();
			}
			if("sh_salehotline".equals(col_name)){
				return this.getSh_salehotline();
			}
			if("sh_saletime".equals(col_name)){
				return this.getSh_saletime();
			}
			if("sh_salelongitude".equals(col_name)){
				return this.getSh_salelongitude();
			}
			if("sh_salelatitude".equals(col_name)){
				return this.getSh_salelatitude();
			}
			if("sh_financecoorp".equals(col_name)){
				return this.getSh_financecoorp();
			}
			if("sh_financecoorpdate".equals(col_name)){
				return this.getSh_financecoorpdate();
			}
			if("sh_financecoorpenddate".equals(col_name)){
				return this.getSh_financecoorpenddate();
			}
			if("sh_salecarbrand".equals(col_name)){
				return this.getSh_salecarbrand();
			}
			if("sh_createtime".equals(col_name)){
				return this.getSh_createtime();
			}
			if("sh_lastupdatetime".equals(col_name)){
				return this.getSh_lastupdatetime();
			}
			return null;
		}
}