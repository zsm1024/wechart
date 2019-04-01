package com.gwm.db.entity.meta;
import com.gwm.db.dao.condition.Col;
public class ${tableName}Meta {
<#-- 生成实体属性-->
<#list columnInfo as column>
		public static Col ${column.column_name?lower_case} = new Col("${column.column_name?upper_case}","${tableName}");
</#list>
}