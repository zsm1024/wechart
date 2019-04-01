package com.gwm.common.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
/**
 * 调用远程服务
 * @author kaifa1
 *
 */
public class ServiceInvoker {
	private Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);
	
	/**
	 * 调用远程服务
	 * @param url 请求URL
	 * @param request 
	 * @param redisDao
	 * @return
	 * @throws Exception
	 */
	public Object invokeService(String url,Map<String,String> otherParam) {
		 Assert.isTrue(!StringUtils.isEmpty(url), "url地址不能为空");
		 RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		 RestTemplate restTemplate = new RestTemplate();
		 //取得提交的参数
		 HttpServletRequest request = SpringUtil.request();
		 String service = redisDao.getString("SERVICE_URL");
		 logger.debug("调用服务:{}",service+url);
		 //取得页面提交的参数
		 Map<String,String> params = getParameters(request,otherParam);
		 //设置转换器
		 FormHttpMessageConverter fc = new FormHttpMessageConverter();
		 StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		 restTemplate.setMessageConverters(Arrays.asList(fc,s));
		 
		 MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		 map.setAll(params);
		 
		 String result = restTemplate.postForObject(service+url, map, String.class);
		 logger.debug("服务返回值:{}",result);
		 return result;
 
	}
	/**
	 * 取得请求参数
	 * @param request
	 * @return
	 */
	private Map<String,String> getParameters(HttpServletRequest request,Map<String,String> map){
		if(map==null){
			map = new HashMap<String,String>();
		}
		if(request==null){
			return map;
		}
		Enumeration<String> paramNames = request.getParameterNames();
	    while (paramNames.hasMoreElements()) {  
	      String paramName = (String) paramNames.nextElement();  
	      String[] paramValues = request.getParameterValues(paramName);  
	      if (paramValues.length == 1) {
	        String paramValue = paramValues[0];  
	        if (paramValue.length() != 0) {  
	        	logger.debug("请求参数：{} = {}" , paramName , paramValue);
	        	if(map.containsKey(paramName)){
	        		logger.warn("参数{}值将被覆盖,原值:{},新值:{}",paramName,map.get(paramName),paramValue);
	        	}
	        	map.put(paramName, paramValue);  
	        }
	      }
	    }
	    return map;
	}
}
