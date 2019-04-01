package com.gwm.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 功能描述：Spring 工具类
 * @autor:tianming
 * @date: 2015-8-5
 */
public class SpringUtil {
	private static ApplicationContext applicationContext;  
	public static void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}
	/**
	 * 功能描述：根据BeanID从容器获取bean
	 * @param beanID
	 * @return
	 * @ tianming 
	 * @ 2015-8-5
	 */
	public static Object getBean(String beanID){
		return applicationContext.getBean(beanID);
	}
	/**
	 * 功能描述：根据BeanID从容器获取bean
	 * @param beanID
	 * @return
	 * @ tianming 
	 * @ 2015-8-5
	 */
	public static <T> T getBean(String beanID,Class<T> clazz){
		return applicationContext.getBean(beanID,clazz);
	}
	/**
	 * 功能描述：根据BeanID从容器获取bean
	 * @param beanID
	 * @return
	 * @ tianming 
	 * @ 2015-8-5
	 */
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	/**
	 * 取得当前请求的request
	 * @return
	 */
	public static HttpServletRequest request(){
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder
			        .getRequestAttributes()).getRequest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return request;
	}
}

