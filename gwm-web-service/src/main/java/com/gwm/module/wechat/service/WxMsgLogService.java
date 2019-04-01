package com.gwm.module.wechat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Col;
import com.gwm.db.entity.Gw_history_news_reg;
import com.gwm.db.entity.Gw_wx_msg_log;
import com.gwm.db.entity.meta.Gw_wx_msg_logMeta;
import com.gwm.engine.Util;

@Service
public class WxMsgLogService {
	
	static Logger log = LoggerFactory.getLogger(WxMsgLogService.class);
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	RedisDao redis = null;
	
	/**
	 * 微信上行消息记录日志
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String,String> saveUpsideMsgLog(Map<String, String> param){
		
		log.info("WxMsgLogService.saveMsgLog");

		Gw_wx_msg_log logEntity = new Gw_wx_msg_log();
		
		logEntity.setSenter(param.get("FromUserName"));
		logEntity.setWx_unit(param.get("ToUserName"));
		logEntity.setSenttime(param.get("CreateTime"));
		logEntity.setReceiver("system");
		logEntity.setFlag("1");
		logEntity.setExtend(StringUtils.isEmpty(param.get("MsgId"))?"请求成功":param.get("MsgId"));
		if (param.get("MsgType").equals("event")) {
            if (param.get("Event").equals("subscribe")) {
                logEntity.setContent("subscribe");
            }
            else if (param.get("Event").equals("unsubscribe")) {
                logEntity.setContent("unsubscribe");
            }
            else if (param.get("Event").equals("CLICK")) {
                logEntity.setContent("CLICK " + param.get("EventKey"));
            }
            else if(param.get("Event").equals("VIEW")){
            	logEntity.setContent("[VIEW]");
            	logEntity.setExtend(param.get("EventKey"));
            }
            else if ("TEMPLATESENDJOBFINISH".equals(param.get("Event"))){
                logEntity.setContent("TEMPLATESENDJOBFINISH ["+param.get("MsgID")+"]");
                logEntity.setExtend(param.get("Status"));
            }
            else if("MASSSENDJOBFINISH".equals(param.get("Event"))){
                logEntity.setContent("MASSSENDJOBFINISH ["+param.get("MsgId")+"]");
                logEntity.setExtend(param.get("Status"));
            }
        }else if(param.get("MsgType").equals("text")){
            logEntity.setContent(param.get("Content"));
        }else if(param.get("MsgType").equals("image")){
            logEntity.setContent(param.get("PicUrl"));
            logEntity.setExtend(param.get("MediaId"));
        }else if(param.get("MsgType").equals("voice")){
            logEntity.setContent(param.get("MediaId"));
            logEntity.setExtend("voice");
        }else if(param.get("MsgType").equals("video")){
            logEntity.setContent(param.get("MediaId"));
            logEntity.setExtend("video");
        }else if(param.get("MsgType").equals("location")){
            logEntity.setContent(param.get("Label"));
            logEntity.setExtend(param.get("Location_X")+" "+param.get("Location_Y"));
        }else if("fromclass".equals(param.get("MsgType"))){
            logEntity.setContent("com.dhc.wx.function.RichMsg");
            logEntity.setExtend(param.get("fromclass"));
        }
		dao.insert(logEntity);
		
		return Util.returnMap("0", "记录上行消息日志成功");
	}
	
	/**
	 * 微信下行消息记录日志
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> saveDownsideMsgLog(Map<String, String> map){
		
		Gw_wx_msg_log logEntity = new Gw_wx_msg_log();
		logEntity.setContent(map.get("content"));
		logEntity.setExtend(map.get("extend"));
		logEntity.setFlag(map.get("flag"));
		logEntity.setReceiver(map.get("receiver"));
		logEntity.setSenter(map.get("senter"));
		logEntity.setSenttime(map.get("senttime"));
		logEntity.setWx_unit(StringUtils.isEmpty(map.get("wx_unit"))?redis.getString("wx_unit"):map.get("wx_unit"));
		dao.insert(logEntity);
		
		return Util.returnMap("0", "记录下行消息日志成功");
	}
	
	/**
	 * 按消息类型记录模板消息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> saveTemplateMsgLog(Map<String, String> param){
		String content = param.get("template");
		String openid = param.get("openid");
		String template_id = param.get("template_id");
		String msg_type = param.get("msg_type");
		String title = param.get("title");
		log.info("content:"+content);
		log.info("openid:"+openid);
		log.info("template_id+"+template_id);
		log.info("msg_type:"+msg_type);
		log.info("title:"+title);
		if(param == null || param.isEmpty()){
			return Util.returnMap("9999", "数据错误，请检查！！");
		}
		if(StringUtils.isEmpty(template_id)){
			log.info("模板消息id为空");
			return Util.returnMap("9999", "模板消息template_id为空");
		}
		if(StringUtils.isEmpty(openid)){
			log.info("微信识别码openid为空");
			return Util.returnMap("9999", "用户id为空！！");
		}
		if(StringUtils.isEmpty(msg_type)){
			log.info("消息类型为空");
			return Util.returnMap("9999", "消息类型为空");
		}
		if(StringUtils.isEmpty(content)){
			log.info("模板消息内容为空");
			return Util.returnMap("9999", "模板内容template为空！！！");
		}
		String msg_cont = getTemplateMsg(content, param);
		Gw_history_news_reg newsEntity = new Gw_history_news_reg();
		String msgDT = Util.formatDate(System.currentTimeMillis());
		log.info("msgDT:"+msgDT);
		String id = UUID.randomUUID()+"";
		log.info("id:"+id);
		newsEntity.setId(id);
		newsEntity.setMsg_cont(msg_cont);
		newsEntity.setMsg_date(msgDT.substring(0, 8));
		newsEntity.setMsg_time(msgDT.substring(8, 14));
		newsEntity.setMsg_title(title);
		newsEntity.setMsg_type(msg_type);
		newsEntity.setOpenid(openid);
		newsEntity.setStatus("0");
		dao.insert(newsEntity);
		return Util.returnMap("0", "模板消息记录成功");
	}
	
	/**
	 * 组模板消息内容
	 * @param json
	 * @param map
	 * @return
	 */
	public String getTemplateMsg(String json, Map<String, String> map){
		log.info("json:"+json);
		
		Pattern pat = Pattern.compile("(?<=\\{\\{)(.+?)(?=.DATA\\}\\})");
		Matcher mat = pat.matcher(json);
		
		List<String> list = new ArrayList<String>();
		while(mat.find()){
			list.add(mat.group());
		}
		for(int i = 0; i < list.size(); i++){
			String key = list.get(i);
			String value = map.get(key)==null?"":map.get(key);
			json = json.replace("{{"+key+".DATA}}", value+"\n");
		}
		log.info("模板消息json:"+json);
		return json;
	}
}
