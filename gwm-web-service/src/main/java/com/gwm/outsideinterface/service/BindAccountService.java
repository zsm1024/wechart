package com.gwm.outsideinterface.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gwm.db.entity.Gw_internal_staff;
import com.gwm.db.entity.meta.Gw_internal_staffMeta;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;
import com.gwm.module.wechat.service.SCheckCodeService;
@Service
public class BindAccountService {
	
	Logger log = LoggerFactory.getLogger(BindAccountService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	SCheckCodeService service = null;
	/**
	 * 账户绑定
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> bindAccount(Map<String, String> map) throws Exception{
		log.info("开始验证绑定账号信息");
		String openId = map.get("openId");//openId
		String idType = map.get("idType");//证件类型
		String idNumber = map.get("idNumber");//证件号码
		String reservePhone = map.get("reservePhone");//预留手机号
		String validateCode = map.get("validateCode");//验证码
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "绑定失败");
		}
		//验证码校验
		Map<String, String> param=new HashMap<String, String>();
		param.put("openid", openId);
		param.put("phone", reservePhone);
		param.put("flag", "bind");
		param.put("check_code", validateCode);
		Map<String, String> checkMap = service.authCheckCode(param);
		if(!"0".equals(checkMap.get("errcode"))){
			log.info("请输入正确的验证码");
			return Util.returnMap("9999", "请输入正确的验证码");
		}
		try {
			//查询该微信id是否已经绑定过
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)));
			//判断该账号是否曾经绑定过
			if(queryEntity!=null){
				if("2".equals(queryEntity.get("status").toString())){
					log.info("该账号已经绑定，请勿重复绑定");
					return Util.returnMap("9999", "该账号已经绑定过，请勿重复绑定");
				}else{
					queryEntity.setId_type(idType);
					queryEntity.setId_num(idNumber);
					queryEntity.setPhone(reservePhone);
					queryEntity.setStatus("1");
					dao.update(queryEntity,Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)));
					return Util.returnMap("0", "进入查询合同");
				}
			}
			else{
				Gw_user_loan_rel insertEntity = new Gw_user_loan_rel();
				insertEntity.setContract_code("");
				insertEntity.setContract_id("");
				insertEntity.setId_num(idNumber);
				insertEntity.setId_type(idType);
				insertEntity.setOpenid(openId);
				insertEntity.setPhone(reservePhone);
				insertEntity.setStatus("1");
				insertEntity.setUser_name("");
				dao.insert(insertEntity);
				return Util.returnMap("0", "进入查询合同");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	/**
	 * acs接口认证
	 * @param map
	 */
	@Transactional
	public Map<String, String> authenticationACS(Map<String, String> map) throws Exception{
		/*
		* acs验证功能
		* */
		return Util.returnMap("0", "通过acs接口进行认证");
	}
	/**
	 * acs接口认证
	 * @param map
	 */
	@Transactional
	public Map<String, String> bindingStaff(Map<String, String> map) throws Exception{
		String openId = map.get("openId");
		String staffType = map.get("staffType");
		String staffName = map.get("staffName");
		String idNumber = map.get("idNumber");
		String jobNumber = map.get("jobNumber");
		String staffPhone = map.get("staffPhone");

		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "绑定失败");
		}
		try {
			//查询该微信id是否已经绑定过
			Gw_internal_staff queryEntity = null;
			queryEntity = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.openid.eq(openId)));
			//判断该账号是否曾经绑定过
			if(queryEntity!=null){
				if("1".equals(queryEntity.get("status").toString())){
					log.info("该账号已经绑定，请勿重复绑定");
					return Util.returnMap("1", "该账号已经绑定过，请勿重复绑定");
				}else{
					/*//判断手机号、身份证号、工号是否有过其他微信绑定，若已被其他微信绑定更新该绑定信息为解绑再绑定当前微信号
					Gw_internal_staff queryEntityByPhone = null;
					queryEntityByPhone = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.phone.eq(staffPhone)));
					Gw_internal_staff queryEntityByIdNumber = null;
					queryEntityByIdNumber = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.cardnbr.eq(idNumber)));
					Gw_internal_staff queryEntityByJobNumber = null;
					queryEntityByJobNumber = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.worknbr.eq(jobNumber)));
					if(queryEntityByPhone!=null&&!queryEntityByPhone.getOpenid().equals(openId)){
						queryEntityByPhone.setStatus("2");
						dao.update(queryEntityByPhone,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByPhone.getOpenid())));
						//return Util.returnMap("9999", "该手机号已经绑定过，请更换手机号再次绑定");
					}else if(queryEntityByIdNumber!=null&&!queryEntityByIdNumber.getOpenid().equals(openId)){
						queryEntityByIdNumber.setStatus("2");
						dao.update(queryEntityByIdNumber,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByIdNumber.getOpenid())));
						//return Util.returnMap("9999", "该身份证号已经绑定过，请更换身份证号再次绑定");
					}else if(queryEntityByJobNumber!=null&&!queryEntityByJobNumber.getOpenid().equals(openId)){
						queryEntityByJobNumber.setStatus("2");
						dao.update(queryEntityByJobNumber,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByJobNumber.getOpenid())));
						//return Util.returnMap("9999", "该工号已经绑定过，请更换工号再次绑定");
					}*/

					queryEntity.setOpenid(openId);
					queryEntity.setType(staffType);
					queryEntity.setName(staffName);
					queryEntity.setCardnbr(idNumber);
					queryEntity.setWorknbr(jobNumber);
					queryEntity.setPhone(staffPhone);
					queryEntity.setStatus("1");
					dao.update(queryEntity,Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)));
					return Util.returnMap("0", "内部员工资料已更新");
				}
			}
			else{
				/*Gw_internal_staff queryEntityByPhone = null;
				queryEntityByPhone = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.phone.eq(staffPhone)));
				Gw_internal_staff queryEntityByIdNumber = null;
				queryEntityByIdNumber = dao.fetch(Gw_internal_staff.class, Cnd.where(Gw_internal_staffMeta.cardnbr.eq(idNumber)));

				if(queryEntityByPhone!=null&&!queryEntityByPhone.getOpenid().equals(openId)){
					return Util.returnMap("9999", "该手机号已经绑定过，请更换手机号再次绑定");
				}else if(queryEntityByIdNumber!=null&&!queryEntityByIdNumber.getOpenid().equals(openId)){
					return Util.returnMap("9999", "该身份证号已经绑定过，请更换身份证号再次绑定");
				}*/
				/*if(queryEntityByPhone!=null&&!queryEntityByPhone.getOpenid().equals(queryEntity.getOpenid())){
					queryEntityByPhone.setStatus("2");
					dao.update(queryEntityByPhone,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByPhone.getOpenid())));
				}else if(queryEntityByIdNumber!=null&&!queryEntityByIdNumber.getOpenid().equals(queryEntity.getOpenid())){
					queryEntityByIdNumber.setStatus("2");
					dao.update(queryEntityByIdNumber,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByIdNumber.getOpenid())));
				}else if(queryEntityByJobNumber!=null&&!queryEntityByJobNumber.getOpenid().equals(queryEntity.getOpenid())){
					queryEntityByJobNumber.setStatus("2");
					dao.update(queryEntityByJobNumber,Cnd.where(Gw_user_loan_relMeta.openid.eq(queryEntityByJobNumber.getOpenid())));
				}*/
				Gw_internal_staff insertEntity = new Gw_internal_staff();
				String uuid = UUID.randomUUID().toString();
				String id = uuid.replace("-", "");
				insertEntity.setId(id);
				insertEntity.setOpenid(openId);
				insertEntity.setType(staffType);
				insertEntity.setName(staffName);
				insertEntity.setCardnbr(idNumber);
				insertEntity.setWorknbr(jobNumber);
				insertEntity.setPhone(staffPhone);
				insertEntity.setStatus("1");
				dao.insert(insertEntity);
				return Util.returnMap("0", "内部员工账号绑定成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	/**
	 * 发送手机验证码
	 * @param map
	 * @return
	 */
	
	public Map<String, String> sendVerificationCode(Map<String, String> map){
		String openid = map.get("openid");
		String ori_phone = map.get("ori_phone");
		String flag = map.get("flag");
		Map<String, String> param = new HashMap<String, String>();
		param.put("openid", openid);
		param.put("phone", ori_phone);
		param.put("flag", flag);
		return service.getCheckCode(param);
	}
}