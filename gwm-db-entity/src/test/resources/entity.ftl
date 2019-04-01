package com.gwm.db.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.gwm.db.dao.IEntity;
import com.gwm.db.dao.entity.annotation.Column;
import com.gwm.db.dao.entity.annotation.Table;
import com.gwm.db.dao.sql.ColType;
@Table(name="${tableName?upper_case}")
public class ${tableName} extends IEntity implements RowMapper<${tableName}>  {
<#-- 生成实体属性-->
<#list columnInfo as column>
		@Column(type=ColType.${column.data_type?upper_case},max_length="${column.data_length}"<#if column.nullable=='NO'>,nullable=false</#if>)
	<#if column.data_type=="String">
		private String ${column.column_name?lower_case} = "";
	<#elseif column.data_type=="Long">
		private long ${column.column_name?lower_case} = 0;
	<#elseif column.data_type=="Int">
		private int ${column.column_name?lower_case} = 0;
	<#elseif column.data_type=="Double" >
		private double ${column.column_name?lower_case} = 0.0d;
	 <#elseif column.data_type=="File" >
		private java.io.File ${column.column_name?lower_case} = null;
	 </#if>
</#list>
		private String table_name="${tableName}";
		
		public String getTable_name(){
			return table_name;
		}

<#-- 生成  	setter	和	getter 方法-->
<#list columnInfo as column>
	<#-- 字符串-->
	<#if column.data_type=="String">
		public void set${column.column_name?cap_first}(String ${column.column_name?lower_case}){
			if(${column.column_name?lower_case}==null||"".equals(${column.column_name?lower_case})){
				${column.column_name?lower_case} = "";
			}
			this.${column.column_name?lower_case} = ${column.column_name?lower_case};
		}
		public String get${column.column_name?cap_first}(){
			return this.${column.column_name?lower_case} ;
		}
	<#-- 长整型-->
	<#elseif column.data_type=="Long">
		public void set${column.column_name?cap_first}(long ${column.column_name?lower_case}){
			this.${column.column_name?lower_case} = ${column.column_name?lower_case};
		}
		public void set${column.column_name?cap_first}(String ${column.column_name?lower_case}){
			if(${column.column_name?lower_case}==null||${column.column_name?lower_case}.length()==0){
				${column.column_name?lower_case} = "0";
			}
			this.${column.column_name?lower_case} = Long.parseLong(${column.column_name?lower_case});
		}
		public long get${column.column_name?cap_first}(){
			return this.${column.column_name?lower_case} ;
		}
	<#-- 整形 -->
	<#elseif column.data_type=="Int">
		public void set${column.column_name?cap_first}(int ${column.column_name?lower_case}){
			this.${column.column_name?lower_case} = ${column.column_name?lower_case};
		}
		public void set${column.column_name?cap_first}(String ${column.column_name?lower_case}){
			if(${column.column_name?lower_case}==null||${column.column_name?lower_case}.length()==0){
				${column.column_name?lower_case} = "0";
			}
			this.${column.column_name?lower_case} = Integer.parseInt(${column.column_name?lower_case});
		}
		public int get${column.column_name?cap_first}(){
			return this.${column.column_name?lower_case} ;
		}
	<#-- 浮点型-->
	<#elseif column.data_type=="Double">
		public void set${column.column_name?cap_first}(String ${column.column_name?lower_case}){
			if(${column.column_name?lower_case}!=null&&!"".equals(${column.column_name?lower_case})){
				this.${column.column_name?lower_case} = Double.parseDouble(${column.column_name?lower_case});
			}
		}
		public void set${column.column_name?cap_first}(double ${column.column_name?lower_case}){
			this.${column.column_name?lower_case} = ${column.column_name?lower_case};
		}
		public double get${column.column_name?cap_first}(){
			return this.${column.column_name?lower_case} ;
		}
	<#-- 二进制类型 -->
	<#elseif column.data_type=="File">
		public void set${column.column_name?cap_first}(java.io.File ${column.column_name?lower_case}){
			this.${column.column_name?lower_case} = ${column.column_name?lower_case};
		}
		public java.io.File get${column.column_name?cap_first}(){
			return this.${column.column_name?lower_case} ;
		}
	</#if>
</#list>
	
		public ${tableName} mapRow(ResultSet rs, int arg1) throws SQLException {
			${tableName} entity = new  ${tableName}();
<#list columnInfo as column>
			entity.set${column.column_name?cap_first}(rs.getString("${column.column_name}"));
</#list>
			return entity;
		}
		public String toString(){
			StringBuffer info = new StringBuffer();
<#list columnInfo as column>
			info.append("${column.column_name}:["+get${column.column_name?cap_first}()+"];\n");
</#list>
			return info.toString();
		}
		@Override
		public <T> void set(String col_name, T value) {
<#list columnInfo as column>
			if("${column.column_name?lower_case}".equals(col_name)){
				this.set${column.column_name?cap_first}(String.valueOf(value));
			}
</#list>
		}
		@Override
		public Object get(String col_name) {
<#list columnInfo as column>
			if("${column.column_name?lower_case}".equals(col_name)){
				return this.get${column.column_name?cap_first}();
			}
</#list>
			return null;
		}
}