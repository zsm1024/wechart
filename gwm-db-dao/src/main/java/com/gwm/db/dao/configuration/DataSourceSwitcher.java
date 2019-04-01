package com.gwm.db.dao.configuration;

import java.lang.reflect.Method;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
/**
 * 数据源切换工具
 * @author kaifa1
 *
 */
public class DataSourceSwitcher {
	private static Logger logger = LoggerFactory.getLogger(DataSourceSwitcher.class);
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal contextHolder = new ThreadLocal();
	
	private static int slaveno = 0;
	
	protected static boolean hasSlave = true;//是否有从库
	@SuppressWarnings("unchecked")
	public static void setDataSource(String dataSource) {
		Assert.notNull(dataSource, "数据源不能为空！");
		contextHolder.set(dataSource);
	}
	public static void switchSource(Method m){
		if(hasSlave){//当有从库时才需要切换数据源，否则默认使用主库
			if ( m!=null && m.isAnnotationPresent(Transactional.class)) {//有事务注解的方法使用主库
				DataSourceSwitcher.setMaster();
			} else {
				DataSourceSwitcher.setSlave();
			}
		}
	}
	public static void setMaster() {
		logger.debug("切换数据源至主库");
		clearDataSource();
	}

	public static void setSlave() {
		if(hasSlave){
			logger.debug("切换数据源至从库");
			Random random = new Random();
			int no = random.nextInt(slaveno);
			setDataSource("slave_"+no);
		}
	}

	public static void setSlaveno(int slaveno) {
		DataSourceSwitcher.slaveno = slaveno;
	}

	public static String getDataSource() {
		return (String) contextHolder.get();
	}

	public static void clearDataSource() {
		contextHolder.remove();
	}
}