package com.gwm.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.wx.engine.HttpProcess;

/**
 * 微信接口 
 * @author kaifa3
 *
 */
public class WxTempInterFace{
	
	static Logger log = LoggerFactory.getLogger(WxTempInterFace.class);

    public static void stop(){
        try {

        } catch (Exception ex) {
            log.error("WxTempInterFace stop error", ex);
        }
    }

    public static void run() {
    	RedisDao redis = SpringUtil.getBean(RedisDao.class);
    	String tempurl = redis.getString("tempurl");
    	String mainurl = redis.getString("mainurl");
    	
    	
    	if(tempurl == null || "".equals(tempurl)){
    		log.info("tempurl:"+tempurl);
    		log.info("微信转发地址为空");
    		return;
    	}
    	if(mainurl == null || "".equals(mainurl)){
    		log.info("mainurl:"+mainurl);
    		log.info("微信项目主入口为空");
    		return;
    	}
        while (true) {
            try {
                String xml = HttpProcess.get(tempurl);
                if(xml == null){
                	continue;
                }
            	log.info("xml:"+xml);
                if(xml.length()==0){
                    continue;
                }
                if("this is 20s return".equals(xml)){
                    System.err.println("server msg:"+xml);
                    continue;
                }
                if("sleep interrupted".equals(xml)){
                    System.err.println("service msg:"+xml);
                    continue;
                }
                if(xml.startsWith("空")){
                    System.err.println("service msg:"+xml);
                    continue;
                }
                log.info("！！！！！开始转发啦  post xml:"+xml);
                String retStr = HttpProcess.post(mainurl, xml);
                log.info("！！！！！转发返回啦  post ret:"+retStr);
            }catch (Exception e) {
                log.error("微信临时接口异常：", e);
            }
        }
    }
}