package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class LoanApplyService{
	
	Logger log = LoggerFactory.getLogger(LoanApplyService.class);
	
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
			Object obj = com.gwm.common.service.Service.getLoanApplyCode();
			String retStr = (String)obj;
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			return Util.mapObjectToString(retMap);
		}catch(Exception ex){
			return Util.returnMap("9999", "获取验证码失败");
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
			String brand = param.get("brand");
			String card_id = param.get("card_id");
			String city = param.get("city");
			String first_amt = param.get("first_amt");
			String franchiser = param.get("franchiser");
			String model = param.get("model");
			String name = param.get("name");
			String province = param.get("province");
			String sex = param.get("sex");
			String style = param.get("style");
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
			Object obj = com.gwm.common.service.Service.LoanApply();
			String retStr = (String) obj;
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			log.info("service服务返回："+retMap);
			if("0".equals(retMap.get("errcode")+"")){
				log.info("申请成功");
				return Util.returnMap("0", "尊敬的客户，我们已经受理您的在线申请，经销商会尽快与您联系，您可以关注“长城汽车金融”服务号查看最新活动信息和贷后自助操作，感谢您的支持。", Util.mapObjectToString(retMap));
			}else if("8888".equals(retMap.get("errcode")+"")){
				log.info("验证码验证失败");
				return Util.returnMap("9999", "验证码错误，请重新输入");
			}else{
				log.info("申请失败");
				return Util.returnMap("9999", "很抱歉，申请提交失败，请您重新操作。");
			}
		}catch(Exception ex){
			log.error("贷款申请异常");
			return Util.returnMap("9999", "很抱歉，申请提交失败，请您重新操作。");
		}
	}
	
	/**
	 * 查询专属方案
	 * @param param
	 * @return
	 */
	public Map<String, String> getSchemePromote(Map<String, String> param){
		try{
			String min_bal = param.get("min_bal");
			String max_bal = param.get("max_bal");
			String first_amt = param.get("first_amt");
			String state = param.get("state");
			String openid = param.get("openid");
			log.info("最低车价min_bal:"+min_bal);
			log.info("最高车价max_bal:"+max_bal);
			log.info("首付金额first_amt:"+first_amt);
			log.info("还款期限state:"+state);
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(min_bal)||"undefined".equals(min_bal)){
				log.info("最低车价为空");
				return Util.returnMap("999", "未查到推荐车型");
			}
			if(StringUtils.isEmpty(max_bal)||"undefined".equals(max_bal)){
				log.info("最高车价为空");
				return Util.returnMap("999", "未查到推荐车型");
			}
			if(StringUtils.isEmpty(first_amt)||"undefined".equals(first_amt)){
				log.info("首付金额为空");
				return Util.returnMap("999", "未查到推荐车型");
			}
			if(StringUtils.isEmpty(state)||"undefined".equals(state)){
				log.info("还款期限为空");
				return Util.returnMap("999", "未查到推荐车型");
			}
			String price = min_bal+"@"+max_bal;
			String source = "weixin";
			String downpayment = first_amt;
			String repayPeriod = state;
			param.put("price", price);
			param.put("source", "weixin");
			param.put("downpayment", downpayment);
			param.put("repayPeriod", repayPeriod);
			Object obj = com.gwm.common.service.Service.getProgrammeForMe(param);
			String retStr = (String)obj;
			log.info("service返回"+retStr);
			List<Object> retList = JSONArray.parseArray(retStr);
			String mapStr = retList.get(0).toString();
			if(mapStr.indexOf("error") >= 0){
				return Util.returnMap("9999", "未查到推荐车型");
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("schemes", retStr);
			log.info("返回:"+retMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		}catch(Exception ex){
			log.error("获取专属方案异常", ex);
			return Util.returnMap("9999", "未找到推荐车型");
		}
	}
}