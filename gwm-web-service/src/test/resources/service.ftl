package com.gwm.common.service;

import java.util.Map;
/**
 * 工具自动生成请勿修改
 * @author kaifa1
 * @date ${date}
 */
public class Service {
	private static ServiceInvoker invoker = new ServiceInvoker();
<#list services as service>
	/**
	 *功能描述：${service.desc}
	 */
	public static Object ${service.name}(Map<String,String> params) {
		return invoker.invokeService("${service.url}",params);
	}
	/**
	 *功能描述：${service.desc}
	 */
	public static Object ${service.name}() {
		return ${service.name}(null);
	}
</#list>
}