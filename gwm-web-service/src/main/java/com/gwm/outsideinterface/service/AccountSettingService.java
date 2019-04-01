package com.gwm.outsideinterface.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_suggest_reg;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;
import com.gwm.module.wechat.service.SCheckCodeService;
@Service
public class AccountSettingService {
	
	Logger log = LoggerFactory.getLogger(AccountSettingService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	SCheckCodeService service = null;
	
	/**
	 * 解绑账号
	 * @param map
	 * @return
	 */
	public Map<String, String> removeBindAccount(Map<String, String> map)throws Exception{
		log.info("开始验证解绑账号信息");
		String openId = map.get("openId");//openId
		String idType = map.get("idType");//证件类型
		String idNumber = map.get("idNumber");//证件号码
		String reservePhone = map.get("reservePhone");//预留手机号
		String validateCode = map.get("validateCode");//验证码
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取用户信息失败");
		}
		try {
			//查询该微信id是否已经绑定过
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, 
					Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)).and(Gw_user_loan_relMeta.status.eq("2")));
			//判断该账号是否曾经绑定过
			if(queryEntity==null){
				log.info("该微信号还没有进行过绑定，无需进行解绑操作");
				return Util.returnMap("9999", "该微信号还没有进行过绑定，无需进行解绑操作");
			}
			else{
				//如果该账号已经处于绑定状态，则返回并提示用户
				if(queryEntity.getId_type()!=null && !idType.equals(queryEntity.getId_type())){
					log.info("选择的证件类型与绑定时的信息不符");
					return Util.returnMap("9999", "选择的证件类型与绑定时的信息不符");
				}
				if(queryEntity.getId_num()!=null && !idNumber.equals(queryEntity.getId_num())){
					log.info("输入的证件号码与绑定时的信息不符");
					return Util.returnMap("9999", "输入的证件号码与绑定时的信息不符");
				}
				if(queryEntity.getPhone()!=null && !reservePhone.equals(queryEntity.getPhone())){
					log.info("输入的手机号与绑定时的信息不符");
					return Util.returnMap("9999", "输入的手机号与绑定时的信息不符");
				}
				//验证码校验
				Map<String, String> param=new HashMap<String, String>();
				param.put("openid", openId);
				param.put("phone", reservePhone);
				param.put("flag", "unbind");
				param.put("check_code", validateCode);
				Map<String, String> checkMap = service.authCheckCode(param);
				if("0".equals(checkMap.get("errcode"))){
					//插入用户绑定信息表
					queryEntity.setStatus("3");
					dao.update(queryEntity, Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)));
					log.info("解绑成功");
					return Util.returnMap("0", "解绑成功");
				}
				else{
					log.info("请输入正确的验证码");
					return Util.returnMap("9999", "请输入正确的验证码");
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
	}
	
	/**
	 * 意见反馈
	 * @param map
	 * @return
	 */
	public Map<String, String> feedBack(Map<String, String> map)throws Exception{
		log.info("开始获取提交人信息");
		String openId = map.get("openId");//openId
		String suggest_type = map.get("suggest_type");//反馈类型
		String suggest_cont = map.get("suggest_cont");//反馈信息
		String contact_type = map.get("contact_type");//联系方式
		String submit_person="";//提报人
		String status="1";//状态 1已提交 2处理中 3关闭
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取用户信息失败");
		}
		try {
			//查询该微信id下的信息
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)));
			if(queryEntity!=null){
				submit_person=queryEntity.getUser_name();
			}
			String randomid=UUID.randomUUID().toString();
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			String submit_time=format.format(date);
			//提交意见反馈到登记表中
			Gw_suggest_reg insertEntity = new Gw_suggest_reg();
			insertEntity.setOpenid(openId);
			insertEntity.setId(randomid);
			insertEntity.setSuggest_cont(suggest_cont);
			insertEntity.setSuggest_type(suggest_type);
			insertEntity.setSubmit_time(submit_time);
			insertEntity.setSubmit_person(submit_person);
			insertEntity.setContact_type(contact_type);
			insertEntity.setStatus(status);
			dao.insert(insertEntity);
			log.info("提交意见反馈成功");
			return Util.returnMap("0", "提交意见反馈成功");
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	
}