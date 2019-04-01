package com.wx.engine;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;

/**
 * 与腾讯接口类
 * @author kaifa3
 *
 */
public class WxProcess {
	
	static Logger log = LoggerFactory.getLogger(WxProcess.class);
	
    /**
     * 微信系统返回消息全部类型
     */
    static String errorjson="{\"-1\":\"系统繁忙\",\"0\":\"请求成功\","
    		+ "\"40001\":\"获取access_token时AppSecret错误，或者access_token无效\","
    		+ "\"40002\":\"不合法的凭证类型\","
    		+ "\"40003\":\"不合法的OpenID\","
    		+ "\"40004\":\"不合法的媒体文件类型\","
    		+ "\"40005\":\"不合法的文件类型\","
    		+ "\"40006\":\"不合法的文件大小\","
    		+ "\"40007\":\"不合法的媒体文件id\","
    		+ "\"40008\":\"不合法的消息类型\","
    		+ "\"40009\":\"不合法的图片文件大小\","
    		+ "\"40010\":\"不合法的语音文件大小\","
    		+ "\"40011\":\"不合法的视频文件大小\","
    		+ "\"40012\":\"不合法的缩略图文件大小\","
    		+ "\"40013\":\"不合法的APPID\","
    		+ "\"40014\":\"不合法的access_token\","
    		+ "\"40015\":\"不合法的菜单类型\","
    		+ "\"40016\":\"不合法的按钮个数\","
    		+ "\"40017\":\"不合法的按钮个数\","
    		+ "\"40018\":\"不合法的按钮名字长度\","
    		+ "\"40019\":\"不合法的按钮KEY长度\","
    		+ "\"40020\":\"不合法的按钮URL长度\","
    		+ "\"40021\":\"不合法的菜单版本号\","
    		+ "\"40022\":\"不合法的子菜单级数\","
    		+ "\"40023\":\"不合法的子菜单按钮个数\","
    		+ "\"40024\":\"不合法的子菜单按钮类型\","
    		+ "\"40025\":\"不合法的子菜单按钮名字长度\","
    		+ "\"40026\":\"不合法的子菜单按钮KEY长度\","
    		+ "\"40027\":\"不合法的子菜单按钮URL长度\","
    		+ "\"40028\":\"不合法的自定义菜单使用用户\","
    		+ "\"40029\":\"不合法的oauth_code\","
    		+ "\"40030\":\"不合法的refresh_token\","
    		+ "\"40031\":\"不合法的openid列表\","
    		+ "\"40032\":\"不合法的openid列表长度\","
    		+ "\"40033\":\"不合法的请求字符，不能包含\\uxxxx格式的字符\","
    		+ "\"40035\":\"不合法的参数\","
    		+ "\"40038\":\"不合法的请求格式\","
    		+ "\"40039\":\"不合法的URL长度\","
    		+ "\"40050\":\"不合法的分组id\","
    		+ "\"40051\":\"分组名字不合法\","
    		+ "\"41001\":\"缺少access_token参数\","
    		+ "\"41002\":\"缺少appid参数\","
    		+ "\"41003\":\"缺少refresh_token参数\","
    		+ "\"41004\":\"缺少secret参数\","
    		+ "\"41005\":\"缺少多媒体文件数据\","
    		+ "\"41006\":\"缺少media_id参数\","
    		+ "\"41007\":\"缺少子菜单数据\","
    		+ "\"41008\":\"缺少oauth code\","
    		+ "\"41009\":\"缺少openid\","
    		+ "\"42001\":\"access_token超时\","
    		+ "\"42002\":\"refresh_token超时\","
    		+ "\"42003\":\"oauth_code超时\","
    		+ "\"43001\":\"需要GET请求\","
    		+ "\"43002\":\"需要POST请求\","
    		+ "\"43003\":\"需要HTTPS请求\","
    		+ "\"43004\":\"需要接收者关注\","
    		+ "\"43005\":\"需要好友关系\","
    		+ "\"44001\":\"多媒体文件为空\","
    		+ "\"44002\":\"POST的数据包为空\","
    		+ "\"44003\":\"图文消息内容为空\","
    		+ "\"44004\":\"文本消息内容为空\","
    		+ "\"45001\":\"多媒体文件大小超过限制\","
    		+ "\"45002\":\"消息内容超过限制\","
    		+ "\"45003\":\"标题字段超过限制\","
    		+ "\"45004\":\"描述字段超过限制\","
    		+ "\"45005\":\"链接字段超过限制\","
    		+ "\"45006\":\"图片链接字段超过限制\","
    		+ "\"45007\":\"语音播放时间超过限制\","
    		+ "\"45008\":\"图文消息超过限制\","
    		+ "\"45009\":\"接口调用超过限制\","
    		+ "\"45010\":\"创建菜单个数超过限制\","
    		+ "\"45015\":\"回复时间超过限制\","
    		+ "\"45016\":\"系统分组，不允许修改\","
    		+ "\"45017\":\"分组名字过长\","
    		+ "\"45018\":\"分组数量超过上限\","
    		+ "\"46001\":\"不存在媒体数据\","
    		+ "\"46002\":\"不存在的菜单版本\","
    		+ "\"46003\":\"不存在的菜单数据\","
    		+ "\"46004\":\"不存在的用户\","
    		+ "\"47001\":\"解析JSON/XML内容错误\","
    		+ "\"48001\":\"api功能未授权\","
    		+ "\"50001\":\"用户未授权该api\"}";
    
    public static String getBaseAuthUrl(String url){
    	try{
    		RedisDao redis = SpringUtil.getBean(RedisDao.class);
    		String appid = redis.getString("appid");
    		String urlEncode = URLEncoder.encode(url, "UTF-8");
    		String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid="+appid+"&"
					+ "redirect_uri="+urlEncode+"&"
					+ "response_type=code&"
					+ "scope=snsapi_base&"
					+ "state=true#wechat_redirect";
			log.info("页面授权链接："+authUrl);
			return authUrl;
    	}catch(Exception ex){
    		log.error("获取基础授权url异常", ex);
    		return url;
    	}
    }
    
    /**
     * 获取腾讯服务器ip
     * @param accessToken
     * @return
     */
	public static Map<String, String> getCallBackIp(String accessToken){
		try{
			Map<String, String> retMap = new HashMap<String, String>();
			log.info("accessToken:"+accessToken);
			String json = HttpProcess.get("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token="+ accessToken);
			log.info("ip_list:::" + json);
			String ip_list = JSONProcess.getJSON(json, ".ip_list");
			if ("undefined".equals(ip_list)) {
				String errcode = JSONProcess.getJSON(json, ".errcode");
				String msg = JSONProcess.getJSON(errorjson, "[\"" + errcode + "\"]");
				log.info("errcode:"+errcode);
				log.info("errmsg:"+msg);
				return Util.returnMap(errcode, msg);
			}else{
				retMap.put("ip_list", ip_list);
				return Util.returnMap("0", "交易成功", retMap);
			}
		}catch(Exception ex){
			return Util.returnMap("9999", "交易异常");
		}
	}
	
	/**
	 * 获取accesstoken
	 * @param appid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(String appid, String appsecret) throws Exception {
		String jsonstr=HttpProcess.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appsecret);
		log.info(appid);
		log.info("getAccessToken:::" + jsonstr);
		String access_token = JSONProcess.getJSON(jsonstr, ".access_token");
		if (access_token.equals("undefined")) {
			String errcode = JSONProcess.getJSON(jsonstr, ".errcode");
			String msg = JSONProcess.getJSON(errorjson, "[\"" + errcode + "\"]");
			throw new Exception(errcode+":"+msg);
		} else {
			return access_token;
		}
	}
	
	/**
	 * accessToken异常处理
	 * @return
	 */
	private static String getAccessToken(Boolean bool) throws Exception{
		RedisDao redis = SpringUtil.getBean(RedisDao.class);
		String access_token = null;
		if (bool) {
			redis.lock("access_token");
			access_token = redis.getString("access_token");
			redis.unlock();
			log.info("accessToken:" + access_token);
			if (StringUtils.isEmpty(access_token)) {
				return getAccessToken(false);
			}
			return access_token;
		}else{
			String wechatUrl = redis.getString("WECHAT_URL");
			String host = wechatUrl + "/accesstokenerr";
			log.info("accessToken错误处理:" + host);
			access_token = HttpProcess.get(host);
			log.info("accessToken:" + access_token);
			if (StringUtils.isEmpty(access_token)) {
				log.error("发送消息时 获取accessToken异常:" + access_token);
				throw new Exception("发送消息时 获取accessToken异常");
			}
			if ("error".equals(access_token)) {
				log.error("发送消息时 获取accessToken异常:" + access_token);
				throw new Exception("发送消息时 获取accessToken异常");
			}
			return access_token;
		}
	}
	
	/**
	 * 判断错误信息是否为accessToken错误
	 * 40014   不合法的access_token
	 * 41001   缺少access_token参数
	 * 42001 access_token超时
	 * @param errcode
	 * @return
	 */
	private static Boolean isAccessTokenErr(String errcode){
		if("42001".equals(errcode)||"40001".equals(errcode)||"40014".equals(errcode)||"40014".equals(errcode)||"40014".equals(errcode)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取网页授权access_token
	 * @param code
	 * @return
	 */
	public static Map<String, String> getOauthAccessToken(String code){
		log.info("***************获取网页授权access_token开始***************");
		try{
			RedisDao redis = SpringUtil.getBean(RedisDao.class);
			String appid = redis.getString("appid");
			String appsecret = redis.getString("appsecret");
			String jsonStr = HttpProcess.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code");
			log.info("获取网页授权返回:::"+jsonStr);
			String errcode = JSONProcess.getJSON(jsonStr, ".errcode");
			Map<String, Object> map = JSONObject.parseObject(jsonStr);
			if("undefined".equals(errcode)||"0".equals(errcode)){
				Map<String, String> strMap = Util.mapObjectToString(map);
				return Util.returnMap("0", "交易成功", strMap);
			}else{
				return Util.returnMap(errcode, JSONProcess.getJSON(errorjson, "[\""+errcode+"\"]"));
			}
		}catch(Exception ex){
			log.error("获取网页授权access_token错误");
			return Util.returnMap("9999", "获取网页授权access_token异常");
		}
	}
	
	/**
	 * 获取js-sdk票据jsapiTicket
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public static String getJsapiTicket(String accessToken) throws Exception{
		log.info("***************获取JsapiTicket***************");
		log.info("accessToken:"+accessToken);
		String jsonstr=HttpProcess.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi");
			
		log.info("getJsapiTicket:::" + jsonstr);
		String errcode=JSONProcess.getJSON(jsonstr,".errcode");
		String errmsg = JSONProcess.getJSON(errorjson,"[\""+errcode+"\"]");
		String jsapiTicket = JSONProcess.getJSON(jsonstr,".ticket");
		if("undefined".equals(jsapiTicket)){
			throw new Exception("获取jsapiTicket错误"+errcode+":"+errmsg);
		}else{
			return jsapiTicket;
		}
	}
	
	/**
	 * 获取用户信息
	 * @param userid
	 * @return
	 */
    public static Map<String, String> getUserInfo(String userid){
        try {
        	Boolean bool = true;
        	String access_token=getAccessToken(true);
        	while(true)
        	{
	            log.info("getUserInfo:"+userid);
	            log.info("accessToken:"+access_token);
	            String jsonstr=HttpProcess.get("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+userid+"&lang=zh_CN");
	            log.info("userinfo:::"+jsonstr);
	            String errcode=JSONProcess.getJSON(jsonstr,".errcode");
	            if(errcode.equals("undefined")){
	            	Map<String, Object> map = JSONObject.parseObject(jsonstr);
	            	Map<String, String> strMap = Util.mapObjectToString(map);
	            	strMap.put("nickname", URLEncoder.encode(strMap.get("nickname"), "UTF-8"));
	                return Util.returnMap("0", "交易成功", strMap);
	            }else{
	            	if(isAccessTokenErr(errcode)&&bool){
	            		bool = false;
	            		access_token=getAccessToken(false);
	            		if(StringUtils.isEmpty(access_token)){
	            			throw new Exception("access_token错误");
	            		}
	            		continue;
	            	}
	                return Util.returnMap(errcode, JSONProcess.getJSON(errorjson,"[\""+errcode+"\"]"));
	            }
        	}
        } catch (Exception ex) {
            log.error("获取用户信息异常", ex);
            return Util.returnMap("9999", "获取用户信息异常");
        }
    }
    
    /**
     * 客服消息接口
     * @param msgjson
     * @return
     */
    public static Map<String, String> sendMessage(String msgJson){
        try {
        	Boolean bool = true;
            String access_token=getAccessToken(true);
            while(true)
            {
	            log.info("发送消息："+msgJson);
	            String jsonstr=HttpProcess.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token,msgJson);
	            log.info("发送消息返回:"+jsonstr);
	            String errcode=JSONProcess.getJSON(jsonstr,".errcode");
	            log.info("发送消息errcode:"+errcode);
	            if(errcode.equals("undefined")){
	                return Util.returnMap("undefined", "发送消息无返回信息");
	            }else{
	            	if(isAccessTokenErr(errcode)&&bool){
	            		bool = false;
	            		access_token=getAccessToken(false);
	            		if(StringUtils.isEmpty(access_token)){
	            			throw new Exception("access_token错误");
	            		}
	            		continue;
	            	}
	            	return Util.returnMap(errcode, JSONProcess.getJSON(errorjson,"[\""+errcode+"\"]"));
	            }
            }
        } catch (Exception ex) {
            log.error("发送客服消息异常", ex);
            return Util.returnMap("9999", "发送客服消息异常");
        }
    }
    
    /**
     * 发送模板消息
     * @param msgJson
     * @return
     */
    public static Map<String, String> sendTemplateMsg(String msgJson){
    	try{
    		Boolean bool = true;
    		String access_token = getAccessToken(true);
    		while(true)
    		{
	    		log.info("accessToken:"+access_token);
	    		log.info("模板消息json:"+msgJson);
	    		String jsonstr = HttpProcess.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token, msgJson);
	    		log.info("发送模板消息返回:"+jsonstr);
	    		String errcode = JSONProcess.getJSON(jsonstr, ".errcode");
	    		if("undefined".equals(errcode)){
	    			return Util.returnMap("undefined", "发送模板消息无返回信息");
	    		}else{
	    			if(isAccessTokenErr(errcode)&&bool){
	            		bool = false;
	            		access_token=getAccessToken(false);
	            		if(StringUtils.isEmpty(access_token)){
	            			throw new Exception("access_token错误");
	            		}
	            		continue;
	            	}
	    			return Util.returnMap(errcode, JSONProcess.getJSON(errorjson, "[\""+errcode+"\"]"));
	    		}
    		}
    	}catch(Exception ex){
    		log.error("发送模板消息异常", ex);
    		return Util.returnMap("9999", "发送模板消息异常");
    	}
    }
    
    /**
     * 创建菜单
     * @param menujson
     * @return
     */
    public static Map<String, String> createMenu(String menujson) {
        try {
        	Boolean bool = true;
            String access_token = getAccessToken(true);
            while(true)
            {
	            String jsonstr=HttpProcess.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token, menujson);
	            log.info("更新菜单返回jsonstr:::"+jsonstr);
	            String errcode = JSONProcess.getJSON(jsonstr, ".errcode");
	            if (errcode.equals("undefined")) {
	                return Util.returnMap("undefined", "创建菜单为返回错误信息");
	            } else {
	            	if(isAccessTokenErr(errcode)&&bool){
	            		bool = false;
	            		access_token=getAccessToken(false);
	            		if(StringUtils.isEmpty(access_token)){
	            			throw new Exception("access_token错误");
	            		}
	            		continue;
	            	}
	            	return Util.returnMap(errcode, JSONProcess.getJSON(errorjson,"[\""+errcode+"\"]"));
	            }
            }
        } catch (Exception ex) {
            log.error("创建菜单异常：", ex);
            return Util.returnMap("9999", "创建菜单异常");
        }
    }
    
    /**
     * 下载文件
     * @param id
     * @param filePath
     * @param fileName
     * @return
     */
    public static Map<String, String> downloadFile(String id, String filePath, String fileName){
    	try{
			Boolean bool = true;
			String access_token = getAccessToken(true);
			while (true) {
				log.info("accessToken:" + access_token);
				String jsonstr = HttpProcess.downLoadFile("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+ access_token + "&media_id=" + id, 
						filePath,
						fileName);
				if ("".equals(jsonstr)) {
					log.info("返回为空，下载成功");
					return Util.returnMap("0", "下载成功");
				} else if ("9999".equals(jsonstr)) {
					log.info("返回9999，下载异常");
					return Util.returnMap("9999", "下载图片异常");
				} else {
					log.info("下载图片返回:" + jsonstr);
					String errcode = JSONProcess.getJSON(jsonstr, ".errcode");
					if ("undefined".equals(errcode)) {
						return Util.returnMap("undefined", "下载图片无返回信息");
					} else {
						if (isAccessTokenErr(errcode) && bool) {
							bool = false;
							access_token = getAccessToken(false);
							if (StringUtils.isEmpty(access_token)) {
								throw new Exception("access_token错误");
							}
							continue;
						}
						return Util.returnMap(errcode,JSONProcess.getJSON(errorjson, "[\"" +errcode+ "\"]"));
					}
				}
			}
    	}catch(Exception ex){
    		log.error("下载图片异常", ex);
    		return Util.returnMap("9999", "图片下载失败");
    	}
    }
	
	/**
	 * 组客服文本消息json
	 * @param openid
	 * @param content
	 * @return
	 */
	public static String getTextMsg(String openid,String content){
		log.info("***************组客服文本消息开始***************");
		log.info("openid:"+openid);
		log.info("content:"+content);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> textMap = new HashMap<String, Object>();
        
        textMap.put("content", content);
        
        jsonMap.put("touser", openid);
        jsonMap.put("msgtype", "text");
        jsonMap.put("text", textMap);
        
        String jsonStr = JSONObject.toJSONString(jsonMap);
        log.info("客服文本消息json:"+jsonStr);
        log.info("***************组客服文本消息结束***************");
        return jsonStr;
    }
	
	/**
	 * 组模板消息json
	 * map中需要包含（openid,template_id,url(可为空),color,模板内容）
	 * json中{{first.DATA}}不能有空格
	 * @param json
	 * @param map
	 * @return
	 */
	public static String getTemplateMsg(String json, Map<String, String> map){
		
		log.info("***************组模板消息开始***************");
		log.info("json:"+json);
		log.info("map"+map);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> itemMap = null;//new HashMap<String, Object>();
		
		Pattern pat = Pattern.compile("(?<=\\{\\{)(.+?)(?=.DATA\\}\\})");
		Matcher mat = pat.matcher(json);
		
		List<String> list = new ArrayList<String>();
		while(mat.find()){
			list.add(mat.group());
		}
		String color = "";
		if(map.get("color") == null || "".equals(map.get("color"))){
			color = "#000000";
		}else{
			color = map.get("color");
		}

		for(int i = 0; i < list.size(); i++){
			itemMap = new HashMap<String, Object>();
			String key = list.get(i);
			itemMap.put("value", map.get(key));
			itemMap.put("color", color);
			dataMap.put(key, itemMap);
		}
		jsonMap.put("touser", map.get("openid"));
		jsonMap.put("template_id", map.get("template_id"));
		jsonMap.put("url", map.get("url"));
		jsonMap.put("data", dataMap);
		String jsonStr = JSONObject.toJSONString(jsonMap);
		log.info("模板消息json:"+jsonStr);
		log.info("***************组模板消息结束***************");
		return jsonStr;
		
	}
	
}
