package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="GW_WX_USER")
public class Gw_wx_user extends IEntity implements RowMapper<Gw_wx_user>  {
		@Column(type=ColType.STRING,max_length="255",nullable=false)
		private String openid = "";
		@Column(type=ColType.STRING,max_length="1")
		private String subsribe = "";
		@Column(type=ColType.STRING,max_length="30")
		private String pwid = "";
		@Column(type=ColType.STRING,max_length="3000")
		private String nickname = "";
		@Column(type=ColType.STRING,max_length="1")
		private String sex = "";
		@Column(type=ColType.STRING,max_length="20")
		private String city = "";
		@Column(type=ColType.STRING,max_length="30")
		private String country = "";
		@Column(type=ColType.STRING,max_length="32")
		private String province = "";
		@Column(type=ColType.STRING,max_length="30")
		private String language = "";
		@Column(type=ColType.STRING,max_length="300")
		private String headimgurl = "";
		@Column(type=ColType.STRING,max_length="20")
		private String first_subscribe_time = "";
		@Column(type=ColType.STRING,max_length="20")
		private String subscribe_time = "";
		@Column(type=ColType.STRING,max_length="10")
		private String groupid = "";
		@Column(type=ColType.STRING,max_length="8")
		private String latest_date = "";
		private String table_name="Gw_wx_user";
		
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
		public void setSubsribe(String subsribe){
			if(subsribe==null||"".equals(subsribe)){
				subsribe = "";
			}
			this.subsribe = subsribe;
		}
		public String getSubsribe(){
			return this.subsribe ;
		}
		public void setPwid(String pwid){
			if(pwid==null||"".equals(pwid)){
				pwid = "";
			}
			this.pwid = pwid;
		}
		public String getPwid(){
			return this.pwid ;
		}
		public void setNickname(String nickname){
			if(nickname==null||"".equals(nickname)){
				nickname = "";
			}
			this.nickname = nickname;
		}
		public String getNickname(){
			return this.nickname ;
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
		public void setCity(String city){
			if(city==null||"".equals(city)){
				city = "";
			}
			this.city = city;
		}
		public String getCity(){
			return this.city ;
		}
		public void setCountry(String country){
			if(country==null||"".equals(country)){
				country = "";
			}
			this.country = country;
		}
		public String getCountry(){
			return this.country ;
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
		public void setLanguage(String language){
			if(language==null||"".equals(language)){
				language = "";
			}
			this.language = language;
		}
		public String getLanguage(){
			return this.language ;
		}
		public void setHeadimgurl(String headimgurl){
			if(headimgurl==null||"".equals(headimgurl)){
				headimgurl = "";
			}
			this.headimgurl = headimgurl;
		}
		public String getHeadimgurl(){
			return this.headimgurl ;
		}
		public void setFirst_subscribe_time(String first_subscribe_time){
			if(first_subscribe_time==null||"".equals(first_subscribe_time)){
				first_subscribe_time = "";
			}
			this.first_subscribe_time = first_subscribe_time;
		}
		public String getFirst_subscribe_time(){
			return this.first_subscribe_time ;
		}
		public void setSubscribe_time(String subscribe_time){
			if(subscribe_time==null||"".equals(subscribe_time)){
				subscribe_time = "";
			}
			this.subscribe_time = subscribe_time;
		}
		public String getSubscribe_time(){
			return this.subscribe_time ;
		}
		public void setGroupid(String groupid){
			if(groupid==null||"".equals(groupid)){
				groupid = "";
			}
			this.groupid = groupid;
		}
		public String getGroupid(){
			return this.groupid ;
		}
		public void setLatest_date(String latest_date){
			if(latest_date==null||"".equals(latest_date)){
				latest_date = "";
			}
			this.latest_date = latest_date;
		}
		public String getLatest_date(){
			return this.latest_date ;
		}
	
		public Gw_wx_user mapRow(ResultSet rs, int arg1) throws SQLException {
			Gw_wx_user entity = new  Gw_wx_user();
			entity.setOpenid(rs.getString("openid"));
			entity.setSubsribe(rs.getString("subsribe"));
			entity.setPwid(rs.getString("pwid"));
			entity.setNickname(rs.getString("nickname"));
			entity.setSex(rs.getString("sex"));
			entity.setCity(rs.getString("city"));
			entity.setCountry(rs.getString("country"));
			entity.setProvince(rs.getString("province"));
			entity.setLanguage(rs.getString("language"));
			entity.setHeadimgurl(rs.getString("headimgurl"));
			entity.setFirst_subscribe_time(rs.getString("first_subscribe_time"));
			entity.setSubscribe_time(rs.getString("subscribe_time"));
			entity.setGroupid(rs.getString("groupid"));
			entity.setLatest_date(rs.getString("latest_date"));
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
			info.append("openid:["+getOpenid()+"];\n");
			info.append("subsribe:["+getSubsribe()+"];\n");
			info.append("pwid:["+getPwid()+"];\n");
			info.append("nickname:["+getNickname()+"];\n");
			info.append("sex:["+getSex()+"];\n");
			info.append("city:["+getCity()+"];\n");
			info.append("country:["+getCountry()+"];\n");
			info.append("province:["+getProvince()+"];\n");
			info.append("language:["+getLanguage()+"];\n");
			info.append("headimgurl:["+getHeadimgurl()+"];\n");
			info.append("first_subscribe_time:["+getFirst_subscribe_time()+"];\n");
			info.append("subscribe_time:["+getSubscribe_time()+"];\n");
			info.append("groupid:["+getGroupid()+"];\n");
			info.append("latest_date:["+getLatest_date()+"];\n");
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
			if("openid".equals(col_name)){
				this.setOpenid(String.valueOf(value));
			}
			if("subsribe".equals(col_name)){
				this.setSubsribe(String.valueOf(value));
			}
			if("pwid".equals(col_name)){
				this.setPwid(String.valueOf(value));
			}
			if("nickname".equals(col_name)){
				this.setNickname(String.valueOf(value));
			}
			if("sex".equals(col_name)){
				this.setSex(String.valueOf(value));
			}
			if("city".equals(col_name)){
				this.setCity(String.valueOf(value));
			}
			if("country".equals(col_name)){
				this.setCountry(String.valueOf(value));
			}
			if("province".equals(col_name)){
				this.setProvince(String.valueOf(value));
			}
			if("language".equals(col_name)){
				this.setLanguage(String.valueOf(value));
			}
			if("headimgurl".equals(col_name)){
				this.setHeadimgurl(String.valueOf(value));
			}
			if("first_subscribe_time".equals(col_name)){
				this.setFirst_subscribe_time(String.valueOf(value));
			}
			if("subscribe_time".equals(col_name)){
				this.setSubscribe_time(String.valueOf(value));
			}
			if("groupid".equals(col_name)){
				this.setGroupid(String.valueOf(value));
			}
			if("latest_date".equals(col_name)){
				this.setLatest_date(String.valueOf(value));
			}
		}
		@Override
		public Object get(String col_name) {
			if("openid".equals(col_name)){
				return this.getOpenid();
			}
			if("subsribe".equals(col_name)){
				return this.getSubsribe();
			}
			if("pwid".equals(col_name)){
				return this.getPwid();
			}
			if("nickname".equals(col_name)){
				return this.getNickname();
			}
			if("sex".equals(col_name)){
				return this.getSex();
			}
			if("city".equals(col_name)){
				return this.getCity();
			}
			if("country".equals(col_name)){
				return this.getCountry();
			}
			if("province".equals(col_name)){
				return this.getProvince();
			}
			if("language".equals(col_name)){
				return this.getLanguage();
			}
			if("headimgurl".equals(col_name)){
				return this.getHeadimgurl();
			}
			if("first_subscribe_time".equals(col_name)){
				return this.getFirst_subscribe_time();
			}
			if("subscribe_time".equals(col_name)){
				return this.getSubscribe_time();
			}
			if("groupid".equals(col_name)){
				return this.getGroupid();
			}
			if("latest_date".equals(col_name)){
				return this.getLatest_date();
			}
			return null;
		}
}