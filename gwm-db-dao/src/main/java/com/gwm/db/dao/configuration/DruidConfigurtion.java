package com.gwm.db.dao.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidConfigurtion {
	private Logger logger = LoggerFactory.getLogger(DruidConfigurtion.class);
	/**
	 * 初始化数据源
	 * @param env
	 * @return
	 */
    @Bean(name="dynamicDataSource")
    public DynamicDataSource dynamicDataSource(Environment env ){
    	
    	String masterUrl = env.getProperty("spring.datasource.master.url");
    	String masterUser = env.getProperty("spring.datasource.master.username");
    	String maseterPasswd = env.getProperty("spring.datasource.master.password");
    	
    	DataSource master = initDataSource(env, masterUrl, masterUser, maseterPasswd);
    	
    	DynamicDataSource dynamic = new DynamicDataSource();
    	dynamic.setDefaultTargetDataSource(master);//设置默认数据源为主库
    	
    	Map<Object,Object> temp = new HashMap<Object,Object>();
    	int count = 0;
    	while(true){
    		String url = env.getProperty("spring.datasource.slave"+count+".url");
    		String username = env.getProperty("spring.datasource.slave"+count+".username");
    		String password = env.getProperty("spring.datasource.slave"+count+".password");
    		if(StringUtils.isEmpty(url)){
    			break;
    		}
    		DataSource slave = initDataSource(env, url, username, password);
    		temp.put("slave_"+count, slave);//设置从库
    		count++;
    	}
    	if(count == 0){
    		DataSourceSwitcher.hasSlave = false;
    	}
    	dynamic.setSlavelength(temp.keySet().size());//设置从库个数
    	dynamic.setTargetDataSources(temp);
    	return dynamic;
    }
	/**
	 * 设置事务管理器的数据源为动态数据源
	 * @param dataSource
	 * @return
	 */
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager(@Qualifier("dynamicDataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    /**
     * 初始化数据库
     * @param driver
     * @param url
     * @param username
     * @param password
     * @return
     */
    private DataSource initDataSource(Environment env,String url,String username,String password){
    	String driver = env.getProperty("spring.datasource.driverClassName");
    	String max = env.getProperty("spring.datasource.maxActive");
    	logger.debug("初始化数据库数据源......");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxActive(Integer.parseInt(max));
        
        return druidDataSource;
    }
}
