package com.gwm.module.wechat.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gwm.common.RedisDao;
import com.wx.engine.Util;

@Service
public class WxJsapiTicketService{
	
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(WxJsapiTicketService.class);
	
	public Map<String, String> sign(Map<String, String> param) {
		
		String url = param.get("url");
		if(StringUtils.isEmpty(url)){
			return Util.returnMap("9999", "url参数错误，请联系客服");
		}
		log.info("参数[url]:"+url);
		String jsapi_ticket = redis.getString("jsapi_ticket");
		log.info("参数[jsapi_ticket]:"+jsapi_ticket);
		String appId = redis.getString("appid");
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        log.info("参数[nonce_str]:"+nonce_str);
        log.info("参数[timestamp]:"+timestamp);

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        log.info("签名字符串:"+string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
            log.info("SHA-A签名结果:"+signature);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.info("JS-SDK签名算法异常", e);
            return Util.returnMap("9999", "JS-SDK签名异常");
        }
        catch (UnsupportedEncodingException e)
        {
            log.info("JS-SDK签名算法异常", e);
            return Util.returnMap("9999", "JS-SDK签名异常");
        }

        ret.put("url", url);
        ret.put("appId", appId);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return Util.returnMap("0", "交易成功", ret);
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
	
}