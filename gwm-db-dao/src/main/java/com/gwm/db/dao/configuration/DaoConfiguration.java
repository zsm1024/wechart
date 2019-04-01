package com.gwm.db.dao.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gwm.db.dao.Dao;
import com.gwm.db.dao.callback.impl.OnePojoCallBackImpl;
import com.gwm.db.dao.dbexec.SpringExector;
@Configuration
@Import(DruidConfigurtion.class)
public class DaoConfiguration {
	@Bean
	public Dao dao(SpringExector springExector,OnePojoCallBackImpl onePojoCallBackImpl){
		Dao dao = new Dao();
		dao.setExec(springExector);
		dao.setOneEntityCallBack(onePojoCallBackImpl);
		return dao;
	}
	@Bean
	public SpringExector springExector(JdbcTemplate jdbcTemplate){
		SpringExector springExector = new SpringExector();
		springExector.setJt(jdbcTemplate);
		return springExector;
	}
	@Bean 
	public OnePojoCallBackImpl onePojoCallBackImpl(){
		return new OnePojoCallBackImpl();
	}
}
