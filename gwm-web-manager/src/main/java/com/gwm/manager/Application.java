package com.gwm.manager;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;

import com.gwm.common.SpringUtil;
import com.gwm.common.configuration.RedisCacheConfig;
import com.gwm.db.dao.configuration.DaoConfiguration;
@Configuration
@EnableAutoConfiguration(exclude={JmxAutoConfiguration.class,FreeMarkerAutoConfiguration.class})
@ComponentScan
@Import({DaoConfiguration.class,RedisCacheConfig.class})
public class Application extends SpringBootServletInitializer  {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		SpringApplicationBuilder sab = application.sources(Application.class).bannerMode(Banner.Mode.OFF);
		sab.listeners(new ApplicationListener<ContextRefreshedEvent>(){
			@Override
			public void onApplicationEvent(ContextRefreshedEvent arg0) {
				SpringUtil.setApplicationContext(arg0.getApplicationContext());
			}
		});
		return sab;
	}
}
