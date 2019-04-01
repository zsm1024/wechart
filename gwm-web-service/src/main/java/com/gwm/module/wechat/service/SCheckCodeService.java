package com.gwm.module.wechat.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;
import com.gwm.engine.Util;

@Service
public class SCheckCodeService{
	
	@Autowired
	RedisDao redis = null;
	
	static Logger log = LoggerFactory.getLogger(SCheckCodeService.class);
	
	public Map<String, String> getCheckCode(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String phone = param.get("phone");
			String flag = param.get("flag");
			log.info("openid:"+openid);
			log.info("电话phone:"+phone);
			log.info("验证码表示flag:"+flag);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.error("微信识别码openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			if(StringUtils.isEmpty(phone) || "undefined".equals(phone)){
				log.error("电话号码为空");
				return Util.returnMap("9999", "电话号码错误");
			}
			if(StringUtils.isEmpty(flag) || "undefined".equals(flag)){
				log.error("验证码标识flag为空");
				return Util.returnMap("9999", "发送验证码失败");
			}
			log.info("生成验证码和验证码时间戳");
			String check_code = Util.generateNumbers(6);
			//check_code = "666666";//测试用
			String nowDate = Util.formatDate(System.currentTimeMillis());
			log.info(check_code+"-"+nowDate);
			log.info("将验证码信息记录到redis中");
			Map<String, String> userRedisMap = Util.mapObjectKeyToString(redis.getMap(phone));
//			Map<String, String> userRedisMap = Util.mapObjectKeyToString(redis.getMap(openid));
			if(userRedisMap == null){
				userRedisMap = new HashMap<String, String>();
			}
			String limit = redis.getString("check_code_limit");
			if(StringUtils.isEmpty(limit)){
				limit = "3";
			}
			String currnum = userRedisMap.get(flag+"_currnum");
			if(StringUtils.isEmpty(currnum)){
				currnum = "0";
			}
			String la_nowdate = userRedisMap.get(flag+"_nowdate");
			log.info("校验验证码获取时间（不允许10秒内重新发送验证码）");
			if(!StringUtils.isEmpty(la_nowdate)){
				if(Util.dateToLong(nowDate)-Util.dateToLong(la_nowdate) < 10*1000){
					log.info("10秒内无法重复获取验证码["+nowDate+"=="+la_nowdate+"]");
					return Util.returnMap("9999", "10秒内无法重复获取验证码");
				}
				if(la_nowdate.substring(0, 8).equals(nowDate.substring(0, 8))){
					log.info("["+nowDate+"]已发送验证码["+currnum+"]次，限制发送["+limit+"]次");
					if(Integer.parseInt(currnum) >= Integer.parseInt(limit)){
						log.info("发送次数超过限制，不允许发送");
						return Util.returnMap("9999", "您今日发送验证码次数已超过限制，请明日再试！");
					}
				}else{
					log.info("["+nowDate+"]已发送验证码["+currnum+"]次，限制发送["+limit+"]次");
				}
			}
			currnum = ""+(Integer.parseInt(currnum)+1);
			userRedisMap.put(flag+"_currnum", currnum);
			userRedisMap.put(flag+"_check_code", check_code);
			userRedisMap.put(flag+"_nowdate", nowDate);
			userRedisMap.put(flag+"_phone", phone);
//			redis.save(phone, userRedisMap);
			redis.save(phone, userRedisMap, 24*60*60);
//			redis.save(openid, userRedisMap);
			log.info("redis记录完毕，开始发送验证码");
			/*此处需要添加发送验证码的接口*/
			try{
				log.info("========通过新短信平台转发=======");
				String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
				String source=redis.getString("MSG_WECHAT_FLAG");
				String user=redis.getString("MSG_USER");
				String pass=redis.getString("MSG_PASS");
				//
				//String duxin_xml="【长城金融】您的验证码为："+check_code+"，10分钟内有效。为保证账户安全，请勿向任何人透露。如非本人操作，请致电客服热线400 6527 606。";
				String duxin_xml="您的验证码为："+check_code+"，10分钟内有效。为保证账户安全，请勿向任何人透露。如非本人操作，请致电客服热线400 6527 606。";
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
				stringBuffer.append("<root>");
				stringBuffer.append("<msgs>");
				stringBuffer.append("<msg>");
				stringBuffer.append("<account>"+user+"</account>");
				stringBuffer.append("<pwd>"+pass+"</pwd>");
				stringBuffer.append("<origin>"+source+"</origin>");
				stringBuffer.append("<phone>"+phone+"</phone>");
				stringBuffer.append("<content>"+duxin_xml+"</content>");
				stringBuffer.append("</msg>");
				stringBuffer.append("</msgs>");
				stringBuffer.append("</root>");
				String requestXML=stringBuffer.toString();
				log.info("requestXML="+requestXML);
				boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
				if(f){
					log.info("======验证码发送成功======");
					Map<String,Object> count_map=new HashMap<String,Object>();
					JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
					count_map.put("type", "2");//微信短信类型:2.验证码
					Format format = new SimpleDateFormat("yyyyMMdd");
					String sysdate=format.format(new Date());
					count_map.put("date", sysdate);
					count_map.put("phone", phone);
					log.info("验证码发送成功,手机号："+phone+"，验证码:"+check_code);
					String sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
							+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
					count_map.put("content", duxin_xml);
					count_map.put("wx_msg_id", "");
					count_map.put("status", "1");
					sql=SqlManager.getSql(sql, count_map);
					jdbc.update(sql);
					log.info("短信登记成功");
				}else{
					log.error("调用短信发送接口异常");
					return Util.returnMap("9999", "调用短信发送接口异常");
				}
			}catch(Exception ex){
				log.error("获取验证码失败", ex);
				return Util.returnMap("9999", "获取验证码异常");
			}
			/*此处需要添加发送验证码的接口*/
			return Util.returnMap("0", "发送验证码成功");
		}catch(Exception ex){
			log.error("获取验证码失败", ex);
			return Util.returnMap("9999", "获取验证码异常");
		}
	}

	public Map<String, String> authCheckCode(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String phone = param.get("phone");
			String flag = param.get("flag");
			String check_code = param.get("check_code");
			log.info("openid:"+openid);
			log.info("电话phone:"+phone);
			log.info("验证码表示flag:"+flag);
			log.info("check_code验证码:"+check_code);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.error("微信识别码openid为空");
				return Util.returnMap("9999", "验证码校验失败");
			}
			if(StringUtils.isEmpty(phone) || "undefined".equals(phone)){
				log.error("电话号码为空");
				return Util.returnMap("9999", "验证码校验失败");
			}
			if(StringUtils.isEmpty(flag) || "undefined".equals(flag)){
				log.error("验证码标识flag为空");
				return Util.returnMap("9999", "验证码校验失败");
			}
			if(StringUtils.isEmpty(check_code) || "undefined".equals(check_code)){
				log.error("验证码为空");
				return Util.returnMap("9999", "验证码校验失败");
			}
			
			log.info("开始校验数据");
			String nowDate = Util.formatDate(System.currentTimeMillis());
			log.info("当前时间："+nowDate);
			log.info("从redis中取验证码信息");
			Map<Object, Object> userObjectMap = redis.getMap(phone);
			if(userObjectMap == null){
				log.info("未查到验证码信息key:"+phone);
				return Util.returnMap("9999", "验证码错误");
			}
//			Map<Object, Object> userObjectMap = redis.getMap(openid);
			Map<String, String> userRedisMap = Util.mapObjectKeyToString(userObjectMap);
			log.info("用户缓存信息："+userRedisMap);
			String r_nowdate = userRedisMap.get(flag+"_nowdate");
			String r_phone = userRedisMap.get(flag+"_phone");
			String r_check_code = userRedisMap.get(flag+"_check_code");
			log.info("校验验证码时效（10分钟）");
			if(Util.dateToLong(nowDate)-Util.dateToLong(r_nowdate) > 10*60*1000){
				log.info("验证码超时["+nowDate+"-"+r_nowdate+"]");
				return Util.returnMap("9999", "验证码超时，请重新获取！");
			}
			log.info("校验原手机号码:"+r_phone);
			if(!phone.equals(r_phone)){
				log.info("手机号校验失败["+phone+"=="+r_phone+"]");
				return Util.returnMap("9999", "手机号校验失败");
			}

			log.info("校验验证码:"+check_code);
			if(!check_code.equals(r_check_code)){
				log.info("验证码校验失败["+check_code+"=="+r_check_code+"]");
				return Util.returnMap("9999", "验证码验证失败，请重新输入验证码");
			}
			
			log.info("校验成功");
			return Util.returnMap("0", "验证成功");
		}catch(Exception ex){
			log.error("验证验证码失败", ex);
			return Util.returnMap("9999", "验证验证码异常");
		}
	}
}