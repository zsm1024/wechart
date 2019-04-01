package com.gwm.module.wechat.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;
import com.alibaba.fastjson.JSONObject;

@Service
public class WeChatService {
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	@Autowired
	RedisDao redis = null;
	
	Logger log = LoggerFactory.getLogger(WeChatService.class);
	
	public void wxExecute(Map<String, String> map,String xml){
		/**
		 * map内容
		 * ToUserName 公众号wx_unit
		 * FromUserName 用户openid
		 * MsgType 消息类型
		 * CreateTime 消息创建时间
		 * MsgId 消息id 64位整型
		 */
		
		log.info("******************START******************");		
		//MsgType = event 事件类型
		if(map.get("MsgType").equals("event")){
			
			if(map.get("Event").equals("MASSSENDJOBFINISH")){
				log.info("消息发送成功消息");
			}
			
			//Event = subscribe 用户关注
			else if(map.get("Event").equals("subscribe")){
				log.info("[subscribe]用户关注");
				subscribe(map);
			}
			
			//Event = unsubscribe 用户取消关注
			else if(map.get("Event").equals("unsubscribe")){
				log.info("[unsubscribe]用户取消关注");
				unSubscribe(map);
			}
			
			//Event = SCAN 扫描二维码
			else if(map.get("Event").equals("SCAN")){
				log.info("[SCAN]扫描二维码");
			}
			
			//Event = LOCATION 上报地理位置
			else if(map.get("Event").equals("LOCATION")){
				log.info("[LOCATION]上报地理位置");
			}
			
			//Event = CLICK 用户点击菜单拉取消息
			else if(map.get("Event").equals("CLICK")){
				log.info("[CLICK]用户点击菜单");
				//WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"), "嘿嘿！/::D"));
				try{
					String EventKey = map.get("EventKey");
					if("kefu".equals(EventKey) ){
						//String isVip=com.gwm.common.service.Service.isVip(map).toString();
						//log.info("是否VIP"+isVip);
						//if(isVip.equals("true")){
							List<String> onlines=new ArrayList<String>();
							if(redis.exists("customer_online"))
							   onlines= redis.getList("customer_online");
							//log.info("openid"+map.get("FromUserName"));
							if(onlines!=null && onlines.contains(map.get("FromUserName"))){
								log.info("验证是否连接呼叫中心");
								String content = "您已经接通在线客服。请勿重复连接！";
								wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
								//com.gwm.common.service.Service.sendRabbitMQ(map);
								//return;
							}else{
								Map<String,String> xmlMap=new HashMap<String,String>();
								xmlMap.put("xml", xml);
								Object flag=com.gwm.common.service.Service.sendRabbitMQ(xmlMap);
								if("false".equals(flag)){
									log.info("发送MQ队列失败！");
									String content = "连接失败。请重新连接！";
									wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
								}
								//com.gwm.common.service.Service.SetCustomerOnLine(map);
							}
						/*}else{
							String url = redis.getString("wxbindurl");
							String content = "您好，您暂未绑定账户，无法进行相应操作。";
							String authUrl = WxProcess.getBaseAuthUrl(url);
							content += "<a href='"+authUrl+"'>点击这里，绑定账户</a>";
							wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
						}*/
					}else{
						Map<Object, Object> menuMap = redis.getMap("clickmenumap");
						String serviceName = menuMap.get(EventKey)+"";
						log.info("serviceName:"+serviceName);
						log.info("***************"+serviceName+" start***************");
						Object bean = SpringUtil.getBean(serviceName);
						Method method =bean.getClass().getMethod("process", Map.class);
						method.invoke(bean, map);
						log.info("***************"+serviceName+" stop***************");
					}
				}catch(Exception ex){
					log.error("点击菜单处理异常", ex);
				}
				
			}
			
			//Event = VIEW 用户点击菜单直接跳转
			else if(map.get("Event").equals("VIEW")){
				log.info("[VIEW]用户点击菜单直接跳转");
			}
			
			//Event = TEMPLATESENDJOBFINISH 模板消息发送结果通知
			else if(map.get("Event").equals("TEMPLATESENDJOBFINISH")){
				log.info("模板消息发送结果通知:"+map.get("Event"));
			}
			
			//scancode_push：扫码推事件的事件推送
			else if(map.get("Event").equals("scancode_push")){
				log.info("扫码推事件的事件推送:"+map.get("Event"));
			}
			
			//scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框的事件推送
			else if(map.get("Event").equals("scancode_waitmsg")){
				log.info("扫码推事件且弹出“消息接收中”提示框的事件推送:"+map.get("Event"));
			}
			
			//pic_sysphoto：弹出系统拍照发图的事件推送
			else if(map.get("Event").equals("pic_sysphoto")){
				log.info("弹出系统拍照发图的事件推送:"+map.get("Event"));
			}
			
			//pic_photo_or_album：弹出拍照或者相册发图的事件推送
			else if(map.get("Event").equals("pic_photo_or_album")){
				log.info("弹出拍照或者相册发图的事件推送:"+map.get("Event"));
			}
			
			//pic_weixin：弹出微信相册发图器的事件推送
			else if(map.get("Event").equals("pic_weixin")){
				log.info("弹出微信相册发图器的事件推送:"+map.get("Event"));
			}
			
			//location_select：弹出地理位置选择器的事件推送
			else if(map.get("Event").equals("location_select")){
				log.info("弹出地理位置选择器的事件推送:"+map.get("Event"));
			}else{
				log.info("未知事件 event:"+map.get("Event"));
			}
		}
		else{
			
			//Boolean online=(Boolean)com.gwm.common.service.Service.online(map);
			log.info("验证是否连接呼叫中心");
			List<String> onlines=new ArrayList<String>();
			if(redis.exists("customer_online"))
				onlines= redis.getList("customer_online");
			log.info("openid"+map.get("FromUserName"));
			if(onlines.contains(map.get("FromUserName"))){
				log.info("验证是否连接呼叫中心");
				Map<String,String> xmlMap=new HashMap<String,String>();
				xmlMap.put("xml", xml);
				Object flag=com.gwm.common.service.Service.sendRabbitMQ(xmlMap);
				if("false".equals(flag)){
					log.info("发送MQ队列失败！");
					String content = "连接失败。请重新连接！";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
				}
				return;
			}
			else if(map.get("Content").equals("员工绑定")){
				String staffBindUrl = redis.getString("staffBindUrl");
				String authUrl = WxProcess.getBaseAuthUrl(staffBindUrl); 
			//	WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"),"<a href=\""+authUrl+"\">员工绑定</a>"));
				String content = "<a href=\""+authUrl+"\">员工绑定</a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}
			else 
			{
				Date dt=new Date();
				Date begin=new Date();
				begin.setHours(8);
				begin.setMinutes(30);
				begin.setSeconds(0);
				Date end=new Date();
				end.setHours(17);
				end.setMinutes(30);
				end.setSeconds(0);
				if(dt.getTime()<=begin.getTime() || dt.getTime()>=end.getTime()){
					
					log.info(JSONObject.toJSONString(map));
					if(map.get("MsgType").equals("text")){
						if(map.get("Content").equals("员工购车"))
						{
							WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"),"<a href='"
									+ "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64df46fa7208efb6&redirect_uri=http://wechat.gwmfc.com/gwm-web-wechat/apply.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect"
									+ "'>点击这里，进入内部员工购车入口。</a>"));
							return;
						}
					}
					WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"),"您好，我司工作时间为早上8:30至下午17:30。您可以在工作时间内点击“菜单栏【我要买车】-【在线客服】”或拨打400-6527-606进行咨询，感谢您的支持与配合。"));
					
					
				}
				else{
					log.info(JSONObject.toJSONString(map));
					if(map.get("MsgType").equals("text")){
						if(map.get("Content").equals("员工购车"))
						{
							WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"),"<a href='"
									+ "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64df46fa7208efb6&redirect_uri=http://wechat.gwmfc.com/gwm-web-wechat/apply.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect"
									+ "'>点击这里，进入内部员工购车入口。</a>"));
							return;
						}
					}
					
					
					WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"),"您好，请您点击“菜单栏【我要买车】-【在线客服】”或拨打400-6527-606进行咨询，感谢您的支持与配合。"));
				}
				
			}
			/*//MsgType = text 文本消息
			 if(map.get("MsgType").equals("text")){
				文本功能点，测试用
				//WxProcess.sendMessage(WxProcess.getTextMsg(map.get("FromUserName"), map.get("Content")));
				
				
				
				文本功能点，测试用
				log.info("文本消息");
			}
			
			//MsgType = image 图片消息
			else if(map.get("MsgType").equals("image")){
				log.info("图片消息");
			}
			
			//MsgType = voice 语音消息
			else if(map.get("MsgType").equals("voice")){
				log.info("[voice]语音消息");
			}
			
			//MsgType = video 视频消息
			else if(map.get("MsgType").equals("video")){
				log.info("[video]视频消息");
			}
			
			//MsgType = shortvideo 小视频消息
			else if(map.get("MsgType").equals("shortvideo")){
				log.info("[shortvideo]小视频消息");
			}
			
			//MsgType = location 地理位置消息
			else if(map.get("MsgType").equals("location")){
				log.info("[location]地理位置消息");
			}
			
			//MsgType = link 连接消息
			else if(map.get("MsgType").equals("link")){
				log.info("[link]连接消息");
			}
			else{
				log.info("未知消息类型 MsgType:"+map.get("MsgType"));
			}*/
		}
		//记录日志 (优先处理用户操作，将日志处理放最后)
		if(map.get("CreateTime")!=null&&!"".equals(map.get("CreateTime"))){
			map.put("CreateTime", Util.formatDate(map.get("CreateTime")));
		}
		com.gwm.common.service.Service.upsideMsgLog(map);
		log.info("******************END******************");
	}
	
	private void subscribe(Map<String, String> map){
		log.debug("******************用户关注开始******************");
		Map<String, String> userMap = WxProcess.getUserInfo(map.get("FromUserName"));
		if(!"0".equals(userMap.get("errcode"))){
			log.error("获取用户信息出错:"+userMap);
			return;
		}
		Object retObj = com.gwm.common.service.Service.userSubscribe(userMap);
		log.info(retObj.toString());
		String content = "您好，欢迎光临长城汽车金融，我是您的服务小助手。\n\n";
		String url = redis.getString("wxbindurl");
		String authUrl = WxProcess.getBaseAuthUrl(url);
		content += "温馨提示：请将身份信息与微信号进行绑定，即可享受还款信息提醒、还款历史查询、提前还款在线办理、联系方式变更等便捷服务。\n";
		content += "<a href='"+authUrl+"'>点击这里，绑定账户。</a>";
		wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
		log.debug("******************用户关注结束******************");
	}
	
	private void unSubscribe(Map<String, String> map){
		log.debug("******************取消关注开始******************");
		Object retObj = com.gwm.common.service.Service.userUnSubscribe(map);
		log.info(retObj.toString());
		log.debug("******************取消关注结束******************");
	}
	
	public static void main(String []args){
		/*WeChatService wc =new WeChatService();
		Map<String,String> map=new HashMap<String,String>();
		map.put("ToUserName", "1111");
		map.put("FromUserName", "2222");
		map.put("MsgType", "EVENT");
		map.put("CreateTime", "20170506");
		map.put("MsgId", "6366");
		wc.wxExecute(map,"");*/
		Date dt=new Date();
		Date begin=new Date();
		begin.setHours(8);
		begin.setMinutes(30);
		begin.setSeconds(0);
		Date end=new Date();
		end.setHours(17);
		end.setMinutes(30);
		end.setSeconds(0);
		
		if(dt.getTime()<=begin.getTime() && dt.getTime()>=end.getTime()){
			System.out.print("you are right!");
		}
		else{
			System.out.print("you are wrong!");
		}
	}
}
