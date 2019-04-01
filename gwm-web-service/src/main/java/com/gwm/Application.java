package com.gwm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.configuration.RedisCacheConfig;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.dao.configuration.DaoConfiguration;
import com.gwm.db.entity.Gw_param;
import com.gwm.db.entity.Gw_wx_menu;
import com.gwm.db.entity.Gw_wx_sys_param;
import com.gwm.db.entity.meta.Gw_wx_menuMeta;
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@Import({DaoConfiguration.class,RedisCacheConfig.class})
public class Application extends SpringBootServletInitializer  {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		SpringApplicationBuilder sab = application.sources(Application.class).bannerMode(Banner.Mode.OFF);
		sab.listeners(new ApplicationListener<ContextRefreshedEvent>() {
			private Logger logger = LoggerFactory.getLogger(Application.class);
			@Override
			public void onApplicationEvent(ContextRefreshedEvent arg0) {
				logger.debug("------------加载系统参数开始-------------");
				RedisDao redis = arg0.getApplicationContext().getBean(RedisDao.class);
				Dao dao = arg0.getApplicationContext().getBean(Dao.class);
				//Environment env = arg0.getApplicationContext().getBean(Environment.class);
				SpringUtil.setApplicationContext(arg0.getApplicationContext());
				try {
					List<Gw_param> list = dao.list(Gw_param.class, null);
					for(Gw_param gw_param : list){
						redis.save(gw_param.getParam_name(), gw_param.getParam_value());
					}
				} catch (Exception e) {
					logger.error("加载系统参数失败：",e);
				}
				logger.debug("------------加载系统参数结束-------------");
				logger.debug("------------加载微信参数开始-------------");
				try {
					List<Gw_wx_sys_param> list = dao.list(Gw_wx_sys_param.class, null);
					for(Gw_wx_sys_param gw_param : list){
						redis.save(gw_param.getSys_key(), gw_param.getSys_value());
					}
				} catch (Exception e) {
					logger.error("加载系统参数失败：",e);
				}
				logger.debug("------------加载微信参数结束-------------");
				logger.debug("------------加载微信菜单开始-------------");
				try {
					List<Gw_wx_menu> list = dao.list(Gw_wx_menu.class, Cnd
							.where(Gw_wx_menuMeta.menu_type.eq("4")));
					Map<String, String> clickMenuMap = new HashMap<String, String>();
					for(Gw_wx_menu gw_wx_menu : list){
						clickMenuMap.put(gw_wx_menu.getMenu_key(), gw_wx_menu.getFunction_id());
						clickMenuMap.put(gw_wx_menu.getMenu_key()+"_url", gw_wx_menu.getMenu_url());
					}
					redis.save("clickmenumap", clickMenuMap);
				} catch (Exception e) {
					logger.error("加载系统参数失败：",e);
				}
				logger.debug("------------加载微信菜单结束-------------");
			}

		});
		return sab;
	}
}
