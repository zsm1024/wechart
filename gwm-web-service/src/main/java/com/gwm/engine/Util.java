package com.gwm.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.common.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 工具类
 * @author kaifa3
 *
 */
public class Util {
	
	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(StringUtils.isEmpty(str)||"undefined".equals(str)||"null".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * 对数据添加*掩码
	 * @param res
	 * @param topNum
	 * @param endNum
	 * @return
	 */
	public static String addMaskStr(String res, int topNum, int endNum){
		StringBuilder str = new StringBuilder();
		int length = res.length();
		if(topNum < 0 || endNum < 0){
			return res;
		}
		if(length==0||length<=(topNum+endNum)){
			return res;
		}
		if(topNum != 0){
			str.append(res.substring(0, topNum));
		}
		for(int i = 0; i < length-topNum-endNum; i++){
			str.append("*");
		}
		if(endNum != 0){
			str.append(res.substring(length-endNum, length));
		}
		return str.toString();
	}
	
	/**
	 * 取得请求参数
	 * @param request
	 * @return
	 */
	public static Map<String,String> getParameters(HttpServletRequest request){
		Map<String, String> map = new HashMap<String,String>();
		if(request == null){
			return map;
		}
		Enumeration<String> paramNames = request.getParameterNames();
	    while (paramNames.hasMoreElements()) {  
	      String paramName = (String) paramNames.nextElement();  
	      String[] paramValues = request.getParameterValues(paramName);  
	      if (paramValues.length == 1) {
	        String paramValue = paramValues[0];  
	        if (paramValue.length() != 0) {  
	        	map.put(paramName, paramValue);  
	        }
	      }  
	    }
	    return map;
	}
	
	/**
	 * 获取长度为len的随机数字
	 * @param len
	 * @return
	 */
	public static String generateNumbers(int len){
		Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append(""+r.nextInt(10));
        }
        return sb.toString();
    }
	
	/**
	 * 获取指定日期的前一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay){ 
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
			date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay); 
		} catch (Exception e) { 
			return specifiedDay;
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day-1); 

		String dayBefore=new SimpleDateFormat("yyyyMMdd").format(c.getTime()); 
		return dayBefore; 
	} 
	
	/**
	 * 根据当前时间获取下一个月
	 * @return
	 */
	public static String getPreMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, 1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        String preMonth = dft.format(cal.getTime());
        return preMonth;
    }
	
	/**
	 * 根据时间返回星期几[1-7][星期一-星期日]
	 * @param t
	 * @return
	 */
	public static int getDayOfWeek(long t){
		try{
			int n = -1;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(t);
			n = cal.get(Calendar.DAY_OF_WEEK);
			return n-1==0?7:n-1;
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 日期-->转为毫秒
	 * @param t
	 * @return
	 */
	public static long dateToLong(String tStr){
		try{
			if(tStr==null || tStr.length() != 14){
				return 0;
			}
			
			DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date dt = formatter.parse(tStr);
			
			return dt.getTime();
		}catch(Exception ex){
			return 0;
		}
	}
	
	/**
	 * 秒或毫秒-->转为日期
	 * @param t
	 * @return
	 */
	public static String formatDate(long t){
		
		int len = String.valueOf(t).length();
		if(len != 10 && len != 13){
			return "00000000000000";
		}else if(len == 10){
			t = t*1000;
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(t);
		
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 秒或毫秒-->转为日期
	 * @param t
	 * @return
	 */
	public static String formatDate(String tStr){
		long t = Long.parseLong(tStr);
		int len = tStr.length();
		if(len != 10 && len != 13){
			return "00000000000000";
		}else if(len == 10){
			t = t*1000;
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(t);
		
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 组返回map
	 * @param errcode
	 * @param errmsg
	 * @return
	 */
	public static Map<String, String> returnMap(String errcode, String errmsg){
		Map<String,String> retMap = new HashMap<String, String>();
		retMap.put("errcode", errcode==null?"":errcode);
		retMap.put("errmsg", errmsg==null?"":errmsg);
		return retMap;
	}
	
	/**
	 * 组返回map
	 * @param errcode
	 * @param errmsg
	 * @param retMap
	 * @return
	 */
	public static Map<String, String> returnMap(String errcode, String errmsg, Map<String, String> retMap){
		if(retMap == null){
			retMap = new HashMap<String, String>();
		}
		retMap.put("errcode", errcode==null?"":errcode);
		retMap.put("errmsg", errmsg==null?"":errmsg);
		return retMap;
	}
	
	/**
	 * 将Map<Object, Object>转为Map<String, String>
	 * @param map
	 * @return
	 */
	public static Map<String, String> mapObjectKeyToString(Map<Object, Object> map){
		if(map == null){
			return null;
		}
		Map<String, String> retMap = new HashMap<String,String>();
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
		    retMap.put(entry.getKey()+"", entry.getValue()+"");
		}
		return retMap;
	}
	
	/**
	 * 将Map<String, Object>转为Map<String, String>
	 * @param map
	 * @return
	 */
	public static Map<String, String> mapObjectToString(Map<String, Object> map){
		if(map == null){
			return null;
		}
		Map<String, String> retMap = new HashMap<String,String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
		    retMap.put(entry.getKey(), entry.getValue()+"");
		}
		return retMap;
	}
	
	/**
     * 微信信息转换成map
     *
     * @param xmlstr
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static HashMap<String, String> getXMLMap(String xmlstr) 
    		throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(false);
        domFactory.setNamespaceAware(true);
        DocumentBuilder domBuilder;
        domBuilder = domFactory.newDocumentBuilder();
        InputStream bis = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
        Document doc = domBuilder.parse(bis);
        Element root = doc.getDocumentElement();
        NodeList list = root.getChildNodes();
        HashMap<String,String> map = new HashMap<String,String>();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element e = (Element) node;
            map.put(e.getTagName(), e.getTextContent());
        }
        return map;
    }
}
