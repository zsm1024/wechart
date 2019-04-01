package com.gwm.db.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gwm.db.dao.sql.ColType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Column {
	ColType type() default ColType.STRING;//字段类型
	boolean indexColumn() default false;//是否索引列
	boolean isPk() default false;//是否主键字段
	String func_type() default "5";//字段功能类型
	String comment() default "";//字段功能类型
	String max_length() default "";//字段最大长度
	boolean nullable() default true;//是否允许为空
}
