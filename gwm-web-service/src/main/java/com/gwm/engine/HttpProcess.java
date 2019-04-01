package com.gwm.engine;


import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.json.JSONUtils;

import com.gwm.common.SpringUtil;

public class HttpProcess {
	
	static Logger log = LoggerFactory.getLogger(HttpProcess.class);
	
	public static String get(String url) throws Exception{
		Assert.isTrue(!StringUtils.isEmpty(url), "url地址不能为空");
		RestTemplate restTemplate = new RestTemplate();
		//设置转换器
		FormHttpMessageConverter fc = new FormHttpMessageConverter();
		StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		restTemplate.setMessageConverters(Arrays.asList(fc,s));
		//发送http get请求
		//log.info("HttpProcess get:"+url);
		String result = restTemplate.getForObject(url, String.class);
		
		return result;
	}
	
	public static String post(String url, String json) throws Exception{
		Assert.isTrue(!StringUtils.isEmpty(url), "url地址不能为空");
		RestTemplate restTemplate = new RestTemplate();
		//设置转换器
		FormHttpMessageConverter fc = new FormHttpMessageConverter();
		StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		restTemplate.setMessageConverters(Arrays.asList(fc, s));
		//发送http post请求
		log.info("HttpProcess post:"+url);
		log.info("HttpProcess post data:"+json);
		String result = restTemplate.postForObject(url, json, String.class);
		log.info("HttpProcess result:"+result);
		return result;
	}
	
	public static String post(String url, Map<String, String> param) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		 //取得提交的参数
		 HttpServletRequest request = SpringUtil.request();
		 //设置转换器
		 FormHttpMessageConverter fc = new FormHttpMessageConverter();
		 StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		 restTemplate.setMessageConverters(Arrays.asList(fc,s));
		 
		 MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		 map.setAll(param);
		//发送http post请求
		log.info("HttpProcess post:"+url);
		log.info("HttpProcess post data:"+param);
		String result = restTemplate.postForObject(url, map, String.class);
		log.info("HttpProcess result:"+result);
		return result;
	}
	
	public static String postHttps(String url, Map<String, String> param){
		String result=null;
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
			@Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
		};
		SSLContext sslContext;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom()
			            .loadTrustMaterial(null, acceptingTrustStrategy)
			            .build();
			 SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		        CloseableHttpClient httpClient = HttpClients.custom()
		                .setSSLSocketFactory(csf)
		                .build();

		        HttpComponentsClientHttpRequestFactory requestFactory =
		                new HttpComponentsClientHttpRequestFactory();

		        requestFactory.setHttpClient(httpClient);
		        RestTemplate restTemplate = new RestTemplate(requestFactory);
				 //设置转换器
				 //FormHttpMessageConverter fc = new FormHttpMessageConverter();
				// FastJsonHttpMessageConverter s=new FastJsonHttpMessageConverter();
				// StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
				// restTemplate.setMessageConverters(Arrays.asList(fc,s));
				 
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				 map.setAll(param);
				//发送http post请求
				log.info("HttpsProcess post:"+url);
				log.info("HttpsProcess post data:"+param);
				result = restTemplate.postForObject(url,  map, String.class);
				log.info("HttpsProcess result:"+result);
				return result;
				
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result; 
	      
	}
	
	public static String postHttps(String url, String json){
		String result=null;
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
			@Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
		};
		SSLContext sslContext;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom()
			            .loadTrustMaterial(null, acceptingTrustStrategy)
			            .build();
			 SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		        CloseableHttpClient httpClient = HttpClients.custom()
		                .setSSLSocketFactory(csf)
		                .build();

		        HttpComponentsClientHttpRequestFactory requestFactory =
		                new HttpComponentsClientHttpRequestFactory();

		        requestFactory.setHttpClient(httpClient);
		        RestTemplate restTemplate = new RestTemplate(requestFactory);
		        Assert.isTrue(!StringUtils.isEmpty(url), "url地址不能为空");
				//设置转换器
				FormHttpMessageConverter fc = new FormHttpMessageConverter();
				StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
				restTemplate.setMessageConverters(Arrays.asList(fc, s));
				//发送http post请求
				log.info("HttpsProcess post:"+url);
				log.info("HttpsProcess post data:"+json);
				result = restTemplate.postForObject(url, json, String.class);
				log.info("HttpsProcess result:"+result);
				
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result; 
	      
	}
	
	
	public static void main(String args[]){
		try{
//			String str = "{{first.DATA}} 申请人姓名：{{keyword1.DATA}} 意向金额：{{keyword2.DATA}} 预约取款时间：{{keyword3.DATA}} 联系电话：{{keyword4.DATA}} {{remark.DATA}}";
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("first", "模板消息头");
//			param.put("keyword1", "关键字1");
//			param.put("keyword2", "关键字2");
//			param.put("keyword3", "关键字3");
//			param.put("keyword4", "关键字4");
//			param.put("remark", "新增字段:你好\n备注");
//			param.put("openid", "o5iChjnU-LkOpFBYNpA4y4Jqgpa4");
//			param.put("template_id", "O3xR8jY1bwsx6gCarls3-yyb8ZboytunKSjMtQnNm1E");
//			param.put("template", str);
			Map<String, String> msgMap = new HashMap<String,String>();
			//msgMap.put("appid", "dfdertyyo8gur6pyti");
			//msgMap.put("password", "Wouiyop4Em5t8CICbR5VtTIpgmGiZ9HsZzT3HY73maHVE5AvJe");
			//msgMap.put("grant_type", "get");
			msgMap.put("SSOCookie", "HuvMM%2F3NDHac0WqQI5SnLafduaOa0POboP4%2BbJbZais4mXPGHQReIa7yLLEKU4S9siBeau8oNnwAmeZJPWEpKQ%3D%3D");
			System.out.println(JSONUtils.toJSONString(msgMap));
			String result=postHttps("https://testmall.haval.com.cn/ec-appchannel/app/system/auth/cookieAuth.do?access_token=DXwtzf8e5fms3FsgXTpNozCaCjYu3fyK6VYijXimqYOapkZ9ix", msgMap);
			//String result=postHttps("https://testmall.haval.com.cn/ec-appchannel/app/auth/get_access_token.do", msgMap);

			System.out.println( result);
			
			//JSONObject jd = new JSONObject();
			//jd.put("SSOCookie", "ejJeE6T2Fhmu%2FV0%2Bmq%2BiIw0KwZRggcDPMv1vcve0aVg7Ni3WCcukfnW0ARRunVYKJ8HKmzNooryd%0D%0AIugql%2BogBg%3D%3D ");
		  
			//System.out.println( jd.toString());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
