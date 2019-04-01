package com.gwm.module.loanmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.ethz.ssh2.crypto.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.common.SpringUtil;
import com.gwm.db.dao.Ipage;
import com.gwm.module.loanmanager.service.LoanManagerService;

/**
 * 贷款管理
 * @author kaifa1
 *
 */
@RestController
@RequestMapping(value="/loan")
@Description("贷款管理")
public class LoanManagerController {
	private Logger logger = LoggerFactory.getLogger(LoanManagerController.class);
	@Autowired
	private LoanManagerService service = null;
	
	@RequestMapping(value="/apply")
	@Description("在线申请")
	public Object apply(@RequestParam String source,@RequestParam String brand,
			@RequestParam String Model ,@RequestParam String ckuan,@RequestParam String price,
			@RequestParam String province, @RequestParam String city,@RequestParam String shop,
			@RequestParam String name,@RequestParam String sex,@RequestParam String id,
			@RequestParam String phone,@RequestParam String openid,@RequestParam String yx_wait) throws Exception{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date1 = sdf.parse(yx_wait);
			Date date2 = new Date();
			long seconds = date2.getTime()- date1.getTime();
			logger.info("seconds===="+seconds);
			if((seconds/60/1000)>=10){
				logger.error("****在线申请异常验证码已超时");
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("error", "0001");
				map.put("error_msg", "验证码已超时。请重新获取。");
				list.add(map);
				ObjectMapper mapper=new ObjectMapper();
				return "successCallback("+mapper.writeValueAsString(list)+")";
//				return list;
			}
			list=service.insertParam(source,decodeBase64(brand, 2),decodeBase64(Model, 2),
				decodeBase64(ckuan, 2),price,decodeBase64(province,2),decodeBase64(city, 2),
				decodeBase64(shop, 2),decodeBase64(name, 2),sex,id,phone,openid);
		}catch(Exception e){
			logger.error("****在线申请异常",e);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("error", "0001");
			map.put("error_msg", "贷款申请登记失败！");
			list.add(map);
			return list;
		}
		ObjectMapper mapper=new ObjectMapper();
		if("site".equals(source)){
			return "successCallback("+mapper.writeValueAsString(list)+")";
		}
		return list;
	}
	
	@RequestMapping(value="/programmeforme")
	@Description("我的专属方案")
	public Object getProgrammeForMe(
									@RequestParam String source,@RequestParam String price,
									@RequestParam String downpayment, @RequestParam String repayPeriod
			) throws JsonProcessingException{
		List<Map<String,String>> list = service.caculteRecommand(price,downpayment,repayPeriod);
		ObjectMapper mapper = new ObjectMapper(); 
		if("site".equals(source)){//来源是官网时要处理跨域问题
			return "successCallback("+mapper.writeValueAsString(list)+")";
		}
		return list;
	}
	
	@RequestMapping(value="/getCarPic")
	@Description("获取车辆信息图片")
	public Object getCarPic(@RequestParam String source,@RequestParam String ctype)throws Exception{
		List<Map<String,Object>> list=null;
		list=service.getgetCarPic_gw(decodeBase64(ctype, 2));
		ObjectMapper mapper = new ObjectMapper(); 
		if("site".equals(source)){//来源是官网时要处理跨域问题
			return "successCallback("+mapper.writeValueAsString(list)+")";
		}
		return list;
	}
	@RequestMapping(value="/getShopInfo")
	@Description("车型对应经销商查询")
	public Object getShopInfo_gw(@RequestParam String source,@RequestParam String carModel) throws Exception{
		List<Map<String,Object>> list=null;
		list=service.getShopInfo_gw(decodeBase64(carModel, 2));
		ObjectMapper mapper = new ObjectMapper(); 
		if("site".equals(source)){//来源是官网时要处理跨域问题
			return "successCallback("+mapper.writeValueAsString(list)+")";
		}
		return list;
	}
	
	@RequestMapping(value="/sendCodeToPhone")
	@Description("官网手机验证码发送")
	public Object sendCodeToPhone(@RequestParam String source,@RequestParam String phone,@RequestParam String randomNum)throws Exception{
		List<Map<String,Object>> list=null;
		list=service.sendCodeToPhone(decodeBase64(phone,2),decodeBase64(randomNum,2));
		ObjectMapper mapper=new ObjectMapper();
		if("site".equals(source)){
			return "successCallback("+mapper.writeValueAsString(list)+")";
		}
		return list;
	}
	@RequestMapping(value="/getIp")
	@Description("获取本机IP")
	public Object getIp() throws Exception
	{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		HttpServletRequest request=SpringUtil.request();
		String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
//        return ip; 
        map.put("ip", ip);
        list.add(map);
        Object obj = list;
        ObjectMapper mapper = new ObjectMapper(); 
		return "successCallback("+mapper.writeValueAsString(obj)+")";
//        return list;
	}
	
	//对应提供解密N次  
    public static String decodeBase64(String mi,int times){  
        int num=(times<=0)?1:times;  
        String mingwen="";  
        if(mi==null||mi.equals("")){  
        }else{  
            mingwen=mi;  
            for(int i=0;i<num;i++){  
                mingwen=decodeBase64(mingwen);  
            }  
        }  
        return mingwen;  
    }  
    public static String decodeBase64(String mi){  
        String mingwen="";  
        if(mi==null||mi.equals("")){  
              
        }else{  
        	try {
        	mingwen = new String(Base64.decode(mi.toCharArray()), "UTF-8");
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return mingwen;  
    }  
    
    
    public static void main(String args[]) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date1 = sdf.parse("20161227182600");
		Date date2 = new Date();
		long seconds = date2.getTime()- date1.getTime();
		System.out.println(seconds/60/1000+"分");
    }
}
