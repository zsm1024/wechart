package com.gwm.module.wechat.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_modify_tel_reg;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.Gw_wx_general_information;
import com.gwm.db.entity.meta.Gw_modify_tel_regMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.db.entity.meta.Gw_wx_general_informationMeta;
import com.gwm.engine.Util;

@Service
public class SChangeContactService{
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	SCheckCodeService service = null;
	
	Logger log = LoggerFactory.getLogger(SChangeContactService.class);
	
	/**
	 * 查询绑定信息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> getUserLoanInfo(Map<String, String> param){
		try{
			String openid = param.get("openid");
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "获取绑定信息失败");
			}
			Gw_user_loan_rel bindEntity = null;
			bindEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(bindEntity == null){
				log.info("未找到绑定信息");
				return Util.returnMap("9998", "未绑定");
			}
			String str = JSON.toJSONString(bindEntity);
			Map<String, Object> retMap = JSONObject.parseObject(str);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		}catch(Exception ex){
			log.error("查询绑定信息异常", ex);
			return Util.returnMap("9999", "未查到绑定信息");
		}
	}
	
	/**
	 * 获取验证码
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> getCheckCode(Map<String, String> map){
		log.info("开始查询用户绑定信息");
		String openid = map.get("openid");
//		String ori_phone = map.get("ori_phone");
		String new_phone = map.get("new_phone");
		Gw_user_loan_rel userEntity = null;
		userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
				.where(Gw_user_loan_relMeta.openid.eq(openid))
				.and(Gw_user_loan_relMeta.status.eq("2")));
		if(userEntity == null){
			log.info("未查到用户绑定信息");
			return Util.returnMap("9999", "未绑定手机号码！");
		}
		log.info("校验原手机号是否为绑定手机号");
		String phone = userEntity.getPhone();
		if(!phone.equals(map.get("ori_phone"))){
			log.info("原手机号与绑定手机号不符["+new_phone+"=="+phone+"]");
			return Util.returnMap("9999", "请输入正确的手机号");
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("openid", openid);
		param.put("phone", new_phone);
		param.put("flag", "cc");
		return service.getCheckCode(param);
	}
	
	/**
	 * 修改联系方式
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> changeContactMode(Map<String, String> map){
		try{
			String ori_phone = map.get("ori_phone");
			String openid = map.get("openid");
			String new_phone = map.get("new_phone");
			String check_code = map.get("check_code");
			Map<String, String> param = new HashMap<String, String>();
			param.put("openid", openid);
			param.put("phone", new_phone);
			param.put("flag", "cc");
			param.put("check_code", check_code);
			Map<String, String> checkMap = service.authCheckCode(param);
			if(!"0".equals(checkMap.get("errcode"))){
				log.info("验证码校验失败");
				return checkMap;
			}
			
			Map<String, String> retMap = new HashMap<String, String>();
			log.info("从绑定表获取用户姓名信息");
			Gw_user_loan_rel userEntity = null;
			userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(userEntity == null){
				log.info("未查到用户绑定信息");
				return Util.returnMap("9999", "您还没有绑定，无法变更联系方式！");
			}
			retMap.put("user_name", userEntity.getUser_name());
			
			log.info("校验成功,开始将更改信息记录到申请表中......");
			Gw_modify_tel_reg modifyEntity = null;
			//一位用户处于已提交状态的变更信息只能有一条
			modifyEntity = dao.fetch(Gw_modify_tel_reg.class, Cnd.where(Gw_modify_tel_regMeta.contract_code.eq(userEntity.getContract_code())).and(Gw_modify_tel_regMeta.status.eq("2")));
			if(modifyEntity != null){
				return Util.returnMap("9999", "您提交的联系方式变更业务正在受理中，无法提交新的申请。");
			}
			String date = Util.formatDate(System.currentTimeMillis());
			modifyEntity = new Gw_modify_tel_reg();
			modifyEntity.setApplicationer(retMap.get("user_name"));
			modifyEntity.setApply_date(date.substring(0,8));
			modifyEntity.setApply_time(date.substring(8, 14));
			modifyEntity.setContract_code(userEntity.getContract_code());
			modifyEntity.setNew_phone(new_phone);
			modifyEntity.setOld_phone(ori_phone);
			modifyEntity.setOpenid(openid);
			modifyEntity.setStatus("1");
			modifyEntity.setTel_reg_num(UUID.randomUUID()+"");
			dao.insert(modifyEntity);
			Gw_wx_general_information tempEntity = null;
			tempEntity = dao.fetch(Gw_wx_general_information.class, Cnd
					.where(Gw_wx_general_informationMeta.id.eq("02")));
			if(tempEntity != null){
				retMap.put("color", tempEntity.getColor());
				retMap.put("template", tempEntity.getContent());
				retMap.put("template_id", tempEntity.getInformation_id());
				retMap.put("title", tempEntity.getInformation_name());
				retMap.put("url", tempEntity.getInformation_url());
			}
			try{
				log.info("========通过新短信平台转发=======");
				String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
				String source=redis.getString("MSG_WECHAT_FLAG");
				String user=redis.getString("MSG_USER");
				String pass=redis.getString("MSG_PASS");
				String name=userEntity.getUser_name();
				String duxin_xml="【长城金融】尊敬的"+name+"：我们已经受理您的联系方式变更申请，为了您更好的使用微信查询办理业务，请于1个工作日后使用变更后手机号绑定微信。客服热线400 6527 606，祝您万事如意！";
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
				stringBuffer.append("<root>");
				stringBuffer.append("<msgs>");
				stringBuffer.append("<msg>");
				stringBuffer.append("<account>"+user+"</account>");
				stringBuffer.append("<pwd>"+pass+"</pwd>");
				stringBuffer.append("<origin>"+source+"</origin>");
				stringBuffer.append("<phone>"+new_phone+"</phone>");
				stringBuffer.append("<content>"+duxin_xml+"</content>");
				stringBuffer.append("</msg>");
				stringBuffer.append("</msgs>");
				stringBuffer.append("</root>");
				String requestXML=stringBuffer.toString();
				log.info("requestXML="+requestXML);
				boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
				if(f){
					log.info("======联系方式变更短信成功======");
					Map<String,Object> count_map=new HashMap<String,Object>();
					JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
					count_map.put("type", "2");//微信短信类型:2.验证码
					Format format = new SimpleDateFormat("yyyyMMdd");
					String sysdate=format.format(new Date());
					count_map.put("date", sysdate);
					count_map.put("phone", new_phone);
					log.info("联系方式变更短信,手机号："+new_phone);
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
			return Util.returnMap("0", "联系方式已变更，请于一个工作日后重新绑定！", retMap);
		}catch(Exception ex){
			log.info("更改联系方式异常：", ex);
			return Util.returnMap("9999", "更改信息失败");
		}
	}
}