package com.wx.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;

public class HttpProcess {
	
	static Logger log = LoggerFactory.getLogger(HttpProcess.class);
	
	public static String get(String url) throws Exception{
		
//		String osName = System.getProperties().getProperty("os.name");
//		if(osName.indexOf("Linux")>=0&&url.indexOf("https")>=0){
//			log.info("当前系统为Linux，并且请求腾讯数据，开始转为用curl get请求:"+url);
//			return HttpCurl.get(url);
//		}
		
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
		
//		String osName = System.getProperties().getProperty("os.name");
//		if(osName.indexOf("Linux")>=0&&url.indexOf("https")>=0){
//			log.info("当前系统为Linux，并且请求腾讯数据，开始转为用curl post请求:"+url);
//			return HttpCurl.post(url, json);
//		}
		
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
		
//		String osName = System.getProperties().getProperty("os.name");
//		if(osName.indexOf("Linux")>=0&&url.indexOf("https")>=0){
//			log.info("当前系统为Linux，并且请求腾讯数据，开始转为用curl post请求:"+url);
//			log.info("转map为key=value形式");
//			String data = "";
//			int i = 0;
//			for(Map.Entry<String, String> entity:param.entrySet())
//			{
//				if(i == 0)
//				{
//					data += entity.getKey()+"="+entity.getValue();
//				}else{
//					data += "&"+entity.getKey()+"="+entity.getValue();
//				}
//				i++;
//			}
//			return HttpCurl.post(url, data);
//		}
		
		RestTemplate restTemplate = new RestTemplate();

		// 设置转换器
		FormHttpMessageConverter fc = new FormHttpMessageConverter();
		StringHttpMessageConverter s = new StringHttpMessageConverter(
				StandardCharsets.UTF_8);
		restTemplate.setMessageConverters(Arrays.asList(fc, s));

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.setAll(param);
		// 发送http post请求
		log.info("HttpProcess post:" + url);
		log.info("HttpProcess post data:"+param);
		String result = restTemplate.postForObject(url, map, String.class);
		log.info("HttpProcess result:"+result);
		return result;
	}
	
	public static String downLoadFile(String url, String filePath, String fileName) throws Exception{
//		String osName = System.getProperties().getProperty("os.name");
//		if(osName.indexOf("Linux")>=0&&url.indexOf("http")>=0){
//			log.info("当前系统为Linux，并且请求腾讯数据，开始转为用curl get请求:"+url);
//			return HttpCurl.downLoadFile(url, filePath, fileName);
//		}
		RestTemplate restTemplate = new RestTemplate();

		final String APPLICATION_ALL = "*/*";
		String json = "";
		HttpHeaders headers = new HttpHeaders();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
		    List list = new ArrayList<>();
		    list.add(MediaType.valueOf(APPLICATION_ALL));
		    headers.setAccept(list);
		    ResponseEntity<byte[]> response = restTemplate.exchange(
		        url,
		        HttpMethod.GET,
		        new HttpEntity<byte[]>(headers),
		        byte[].class);
		    byte[] result = response.getBody();
		    int b = result[0];
		    if(b == 123){
		    	json = new String(result, "UTF-8");
		    	log.info("上传文件失败："+json);
		    	return json;
		    }
		    inputStream = new ByteArrayInputStream(result);
		    log.info("下载文件路径:"+filePath);
		    log.info("下载文件名："+fileName);
		    String file111 = filePath + fileName;
		    log.info(file111);
		    File file = new File(file111);
		    if (!file.exists())
		    {
		        file.createNewFile();
		    }

		    outputStream = new FileOutputStream(file);
		    int len = 0;
		    byte[] buf = new byte[1024];
		    while ((len = inputStream.read(buf, 0, 1024)) != -1) {
		        outputStream.write(buf, 0, len);
		    }
		    outputStream.flush();
			return "";
		}catch(Exception ex){
			log.info("下载异常：", ex);
			return "9999";
		}
		finally {
		    if(inputStream != null){
		        inputStream.close();
		    }
		    if(outputStream != null){
		        outputStream.close();
		    }
		}
	}
	
	public static void main(String args[]){
		try{
//			String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=QoOY1lT2izVZjDRCTMdIaffLotY29Bl5TT2q7LwF3o1JKL3gnihOwsXaJiYkj0qf1KNRTojNYqhn0DWuZ907vU-WJpZfyEIIzp29Ij1EXkAKZYgAAAZON&media_id=sN2IQ-5GpPS9JXc-0T80HSAsO9pXdZUf3VOFZUVlxk2lNQqmrFg_0T2eP4t1OOcT";
//			downLoadFile(url, "D:\\", "test.jpg");
			
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
//			param.put("title", "贷款申请");
//			param.put("msg_type", "1");
//			//param.put("test", "test");
			
//			String str = "{{first.DATA}} 业务种类：{{keyword1.DATA}} 申请时间：{{keyword2.DATA}} {{remark.DATA}}";
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("first", "你的在线征信授权申请成功\n");
//			param.put("keyword1", "在线征信授权");
//			param.put("keyword2", "2016-11-16 14:34");
//			param.put("remark", "\n通过审核后客户经理会与您联系，请耐心等待。");
//			param.put("openid", "o5iChjnU-LkOpFBYNpA4y4Jqgpa4");
//			param.put("template_id", "-8qp7G_83M1-5p9Nu7vjg5DRGDnsZK9uWrevSI-Cemg");
//			param.put("template", str);
//			param.put("title", "通知公告");
//			param.put("msg_type", "1");
//			param.put("url", "http://www.baidu.com");
//
//			Object obj = post("http://localhost:80/gwm-web-wechat/sendtemplatemessage", param);
//			System.out.println(obj);
			//post("http://localhost:80/gwm-web-manager/fwdcon?test=test", param);
			//String json = JSON.toJSONString(param);
			//post("http://localhost:80/gwm-web-manager/fwdcon?test=test", json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
