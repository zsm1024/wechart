package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_loan_reg;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;

@Service
public class SLoanApplyService{
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	SCheckCodeService service = null;
	
	static Logger log = LoggerFactory.getLogger(SLoanApplyService.class);
	
	/**
	 * 获取验证码
	 * @param param
	 * @return
	 */
	public Map<String, String> getApplyCode(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String phone = param.get("phone");
			log.info("openid:"+openid);
			log.info("电话phone:"+phone);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.error("微信识别码openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			if(StringUtils.isEmpty(phone) || "undefined".equals(phone)){
				log.error("电话号码为空");
				return Util.returnMap("9999", "电话号码错误");
			}
			param.put("flag", "la");
			Map<String, String> retMap = service.getCheckCode(param);
			
			return retMap;
		}catch(Exception ex){
			log.error("获取验证码异常", ex);
			return Util.returnMap("9999", "获取验证码异常");
		}
	}
	
	/**
	 * 贷款申请
	 * @param param
	 * @return
	 */
	public Map<String, String> loanApply(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String phone = param.get("phone");
			String check_code = param.get("check_code");
			log.info("openid:"+openid);
			log.info("电话phone:"+phone);
			log.info("验证码:"+check_code);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.error("微信识别码openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			if(StringUtils.isEmpty(phone) || "undefined".equals(phone)){
				log.error("电话号码为空");
				return Util.returnMap("9999", "电话号码错误");
			}
			if(StringUtils.isEmpty(check_code) || "undefined".equals(check_code)){
				log.error("验证码为空");
				return Util.returnMap("9999", "验证失败");
			}
			param.put("flag", "la");
			Map<String, String> retMap = service.authCheckCode(param);
			if(!"0".equals(retMap.get("errcode"))){
				log.info("验证码验证失败");
				return Util.returnMap("8888", "验证码验证失败");
			}
			String application_num = UUID.randomUUID()+"";
			String dateStr = Util.formatDate(System.currentTimeMillis());
			String brand = param.get("brand");
			String card_id = param.get("card_id");
			String city = param.get("city");
			String first_amt = param.get("first_amt");
			String franchiser = param.get("franchiser");
			String model = param.get("model");
			String name = param.get("name");
			String province = param.get("province");
			String sex = param.get("sex");
			String source = "2";  //2:微信
			String status = "1";  //1：已提交
			String style = param.get("style");
			log.info("申请单号application_num"+application_num);
			log.info("申请时间dateStr:"+dateStr);
			log.info("车型brand:"+brand);
			log.info("身份证号:"+card_id);
			log.info("城市city:"+city);
			log.info("首付金额first_amt:"+first_amt);
			log.info("经销商franchiser:"+franchiser);
			log.info("车型model:"+model);
			log.info("姓名name:"+name);
			log.info("省份province:"+province);
			log.info("性别sex:"+sex);
			log.info("车辆样式style:"+style);
			if(StringUtils.isEmpty(brand)||"undefined".equals(brand)){
				log.error("品牌为空brand");
				return Util.returnMap("9999", "请选择品牌");
			}
			if(StringUtils.isEmpty(card_id)||"undefined".equals(card_id)){
				log.error("身份证号为空card_id");
				return Util.returnMap("9999", "请输入身份证号");
			}
			if(StringUtils.isEmpty(city)||"undefined".equals(city)){
				log.error("城市为空city");
				return Util.returnMap("9999", "请选择城市");
			}
			if(StringUtils.isEmpty(first_amt)||"undefined".equals(first_amt)){
				log.error("首付金额为空first_amt");
				return Util.returnMap("9999", "请输入首付金额");
			}
			if(StringUtils.isEmpty(franchiser)||"undefined".equals(franchiser)){
				log.error("经销商为空franchiser");
				return Util.returnMap("9999", "请选择经销商");
			}
			if(StringUtils.isEmpty(model)||"undefined".equals(model)){
				log.error("车型为空model");
				return Util.returnMap("9999", "请选择车型");
			}
			if(StringUtils.isEmpty(name)||"undefined".equals(name)){
				log.error("姓名为空name");
				return Util.returnMap("9999", "请输入姓名");
			}
			if(StringUtils.isEmpty(province)||"undefined".equals(province)){
				log.error("省份为空province");
				return Util.returnMap("9999", "请选择省份");
			}
			if(StringUtils.isEmpty(sex)||"undefined".equals(sex)){
				log.error("性别为空sex");
				return Util.returnMap("9999", "请选择称呼");
			}
			Gw_loan_reg loanEntity = new Gw_loan_reg();
			loanEntity.setApplication_num(application_num);
			loanEntity.setApply_date(dateStr.substring(0, 8));
			loanEntity.setApply_time(dateStr.substring(8, 14));
			loanEntity.setBrand(brand);
			loanEntity.setCard_id(card_id);
			loanEntity.setCity(city);
			loanEntity.setFirst_amt(first_amt);
			loanEntity.setFranchiser(franchiser);
			loanEntity.setModel(model);
			loanEntity.setName(name);
			loanEntity.setOpenid(openid);
			loanEntity.setPhone(phone);
			loanEntity.setProvince(province);
			loanEntity.setSex(sex);
			loanEntity.setSource(source);
			loanEntity.setStatus(status);
			loanEntity.setStyle(style);
			dao.insert(loanEntity);
			return Util.returnMap("0", "申请成功");
		}catch(Exception ex){
			log.error("贷款申请异常", ex);
			return Util.returnMap("9999", "申请失败");
		}
	}
}