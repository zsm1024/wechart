package com.gwm.outsideinterface.service;

import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapIterator;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.DecodeXmlImpl;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlExecException;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_repay_reg;
import com.gwm.db.entity.Gw_repay_reg_hst;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.Gw_wx_general_information;
import com.gwm.db.entity.meta.Gw_repay_regMeta;
import com.gwm.db.entity.meta.Gw_repay_reg_hstMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.db.entity.meta.Gw_wx_general_informationMeta;
import com.gwm.engine.Util;

@Service
public class SRepaymentService{
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	RedisDao redis = null;
	
	static Logger log = LoggerFactory.getLogger(SRepaymentService.class);
	
	/**
	 * 查询提前还款信息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> getEarlyPayment(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String application_date = param.get("application_date");
			log.info("openid:"+openid);
			log.info("还款日期application_date"+application_date);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.info("微信识别号openid为空");
				return Util.returnMap("9999", "获取信息失败，请尝试重新打开页面或联系客服");
			}
			if(StringUtils.isEmpty(application_date)||"undefined".equals(application_date)){
				log.info("还款日期为空");
				return Util.returnMap("9999", "请输入提前还款日期");
			}
			Gw_user_loan_rel bindEntity = null;
			bindEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(bindEntity == null){
				log.info("未查到绑定信息");
				return Util.returnMap("9999", "您还没有绑定任何合同");
			}
			String user_name = bindEntity.getUser_name();
			String contract_id = bindEntity.getContract_id();
			String contract_code = bindEntity.getContract_code();
			log.info("用户姓名："+user_name);
			log.info("合同id:"+contract_id);
			log.info("合同编号："+contract_code);
			if(StringUtils.isEmpty(contract_id)){
				log.info("合同id未空");
				return Util.returnMap("9999", "您还没有绑定合同");
			}
			if(StringUtils.isEmpty(contract_code)){
				log.info("合同编号为空");
				return Util.returnMap("9999", "您还没有绑定合同");
			}
			log.info("---------------判断是否节假日 开始---------------");
			StringBuffer stringBuffer = new StringBuffer();
			String url = redis.getString("HOLIDAYCALCULATE_URL");
			log.info("判断节假日url:"+url);
			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
			stringBuffer.append("<root>");
			stringBuffer.append("<application_date>"+application_date+"</application_date>");
			stringBuffer.append("</root>");
			
			String requestXML=stringBuffer.toString();
			log.info("是否节假日请求报文xml："+requestXML);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "holidayCalculate");
			log.info("是否节假日请求报文xml："+requestXML);
			log.info("是否节假日应答报文xml："+requestXML);
			//解析返回报文等到map
			Map<String, Object> map = DecodeXmlImpl.decode(repxml);
			log.info("是否节假日返回json:"+map);
			String flag = (String)map.get("flag");
			log.info("flag:"+flag);
			if(!StringUtils.isEmpty(flag)){
				log.info("返回时间："+map.get("date"));
				if("true".equals(flag)){
					log.info("未查到结果");
					return Util.returnMap("9999", "校验申请日期失败");
				}else{
					log.info("查询异常："+map.get("reason"));
					return Util.returnMap("9999", "校验申请日期失败");
				}
			}
			String is_holiday = map.get("is_holiday")+"";
			log.info("is_holiday:"+is_holiday);
			if("true".equals(is_holiday)){
				log.info(application_date+"为节假日，不能作为还款日");
				return Util.returnMap("9999", application_date+"为节假日，不提供提前还款服务，请选择其他日期");
			}
			int dayOfWeek = Util.getDayOfWeek(Util.dateToLong(application_date.replaceAll("-", "")+"000001"));
			log.info("dayOfWeek"+dayOfWeek);
			if(-1 == dayOfWeek){
				log.info("获取星期几异常日期["+application_date+"]");
				return Util.returnMap("9999", "日期查询失败，请联系客服");
			}
			if(6==dayOfWeek || 7==dayOfWeek){
				log.info(application_date+"为星期"+dayOfWeek+",不能作为还款日");
				return Util.returnMap("9999", application_date+"为休息日，不提供提前还款服务，请选择其他日期");
			}
			log.info("---------------判断是否节假日 结束---------------");
			log.info("开始校验是否有逾期记录");
			Map<String, String> overdueMap = getOverDueInfo(contract_code);
			if(!"0".equals(overdueMap.get("errcode"))){
				log.info("未查到逾期记录，申请失败");
				return Util.returnMap("9999", "申请失败，请重新申请");
			}
			String overdue_dd = overdueMap.get("overdue_dd");
			String live_sts = overdueMap.get("live_sts");
			String asset_brand_dsc = overdueMap.get("asset_brand_dsc");
			String dd = overdueMap.get("dd");
			dd = dd.length()==1?"0"+dd:dd;
			log.info("逾期天数:"+overdue_dd);
			log.info("合同状态："+live_sts);
			log.info("车型："+asset_brand_dsc);
			log.info("还款日:"+dd);
			if(!"0".equals(overdue_dd)){
				log.info("有逾期记录，无法申请提前还款");
				return Util.returnMap("9999", "需要还清逾期费用才可申请提前还款！");
			}
			if("N".equals(live_sts)){
				log.info("贷款合同已结清，无法申请提前还款");
				return Util.returnMap("9999", "您的贷款已全部还清，无法申请提前还款！");
			}
			String nowTM = Util.formatDate(System.currentTimeMillis());
			Calendar calendar = Calendar.getInstance(); 
			Date date=new Date();
	        calendar.setTime(date);  
	        calendar.add(Calendar.DATE, 2);  
	        date = calendar.getTime(); 
	        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String startTM=format.format(date);
	        log.info("startTM======="+startTM);
			String nowDT = nowTM.substring(0, 8);
			String day = nowDT.substring(6, 8);
			log.info("当前日期:"+nowDT);
			String nextDT = "";
			if(Integer.parseInt(day) == Integer.parseInt(dd)){
				log.info("当前日期等于还款日期");
				return Util.returnMap("9999", "请选择当期还款日之前日期作为提前还款日！");
			}else if(Integer.parseInt(day) < Integer.parseInt(dd)){
				log.info("当前日期小于还款日期");
				nextDT = nowDT.substring(0, 6) + dd;
			}else{
				log.info("当前日期大于还款日期");
				nextDT = Util.getPreMonth() + dd;
			}
			log.info("下一个还款日："+nextDT);
			if(Integer.parseInt(application_date.replaceAll("-", ""))==Integer.parseInt(nowDT)){
				log.info(" 申请日期等于当前日期");
				return Util.returnMap("9999", "不能选择申请日当日作为提前还款日！");
			}
			if(Integer.parseInt(application_date.replaceAll("-", ""))<Integer.parseInt(nowDT)){
				log.info("申请日期小于当前日期");
				return Util.returnMap("9999", "不能选择过去日期作为提前还款日！");
			}
			if(Integer.parseInt(application_date.replace("-", ""))<Integer.parseInt(startTM)){
				log.info("申请日期小于当前日期+2");
				return Util.returnMap("9999", "请至少提前2个工作日申请提前还款！");
			}
			if(Integer.parseInt(application_date.replaceAll("-", ""))>=Integer.parseInt(nextDT)){
				log.info("当前日期大于等于下一个还款日");
				return Util.returnMap("9999", "请选择当期还款日之前日期作为提前还款日！");
			}
			log.info("---------------查询提前还款信息 开始---------------");
			stringBuffer = new StringBuffer();
			url=redis.getString("EARLYPAYMENT_URL");
			log.info("查询提前还款url:"+url);
			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
			stringBuffer.append("<root>");
			stringBuffer.append("<application_date>"+application_date+"</application_date>");
			stringBuffer.append("<contract_id>"+contract_id+"</contract_id>");
			stringBuffer.append("<contract_nbr>"+contract_code+"</contract_nbr>");
			stringBuffer.append("</root>");
			
			requestXML=stringBuffer.toString();
			log.info("提前还款请求报文xml："+requestXML);
			repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getEarlyPayment");
			log.info("提前还款请求报文xml："+requestXML);
			log.info("提前还款应答报文xml："+requestXML);
			//解析返回报文等到map
			map = DecodeXmlImpl.decode(repxml);
			log.info("提前还款返回json:"+map);
			flag = (String)map.get("flag");
			log.info("flag:"+flag);
			if(!StringUtils.isEmpty(flag)){
				log.info("返回时间："+map.get("date"));
				if("true".equals(flag)){
					log.info("未查到结果");
					return Util.returnMap("9999", "未查到提前还款信息");
				}else{
					log.info("查询异常："+map.get("reason"));
					return Util.returnMap("9999", "未查到提前还款信息");
				}
			}
			log.info("---------------查询提前还款信息 结束---------------");
			String early_settlement = map.get("early_settlement")+"";
			Map<String, Object> earlyMap = JSONObject.parseObject(early_settlement);
			earlyMap.put("contract_nbr", contract_code);
			earlyMap.put("contract_id", contract_id);
			earlyMap.put("user_name", user_name);
			earlyMap.put("asset_brand_dsc", asset_brand_dsc);
			log.info("返回提前还款信息:"+earlyMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(earlyMap));
		}catch(Exception ex){
			log.error("提前还款查询异常", ex);
			return Util.returnMap("9999", "查询还款信息失败，请重新打开页面");
		}
	}
	/**
	 * 提前还款获取预留银行卡
	 * */
	public Map<String,String> wxearlyrepaygetbank(Map<String,String> param) throws Exception{
		log.info("查询提前还款预留银行卡 ");
		String openid = param.get("openid");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.error("openid为空！");
			return Util.returnMap("9999", "获取用户信息失败");
		}
		Gw_user_loan_rel userEntity = null;
		userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
				.where(Gw_user_loan_relMeta.openid.eq(openid))
				.and(Gw_user_loan_relMeta.status.eq("2")));
		if(userEntity == null){
			log.error("查询绑定信息失败（gw_user_loan_rel）");
			return Util.returnMap("9998", "未找到绑定信息");
		}
		String phone = userEntity.getPhone();
		String id_num = userEntity.getId_num();
		String contract_code = userEntity.getContract_code();
		log.info("手机号："+phone);
		log.info("身份证号："+id_num);
		log.info("当前合同号："+contract_code);
		if(StringUtils.isEmpty(phone)){
			log.error("绑定表中预留手机号为空");
			return Util.returnMap("9999", "查询预留手机号失败");
		}
		if(StringUtils.isEmpty(id_num)){
			log.error("绑定表中身份证号为空");
			return Util.returnMap("9999", "查询身份信息失败");
		}
		if(StringUtils.isEmpty(contract_code)){
			log.error("绑定表中合同号为空");
			return Util.returnMap("9999", "您当前未绑定任何合同");
		}
		String url=redis.getString("CONTRACT_URL");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		stringBuffer.append("<root>");
		stringBuffer.append("<application_phone>"+phone+"</application_phone>");
		stringBuffer.append("<application_id_card>"+id_num+"</application_id_card>");
		stringBuffer.append("</root>");
		// 定义客户端对象
		Map<String, Object>  map=null;
		try {
			String requestXML=stringBuffer.toString();
			log.info("requestXML:"+requestXML);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getContract");
			log.info("返回报文:"+repxml); 
			//解析返回报文等到map
			map=  DecodeXmlImpl.decode(repxml);
			if(map.get("flag") != null){
				log.error("errcode:"+map.get("flag"));
				log.error("errmsg:"+map.get("reason"));
				return Util.returnMap("9999", map.get("reason")+"");
			}
			String contracts = map.get("contracts")+"";
			log.info("contracts:"+contracts);
			Map<String, Object> contractMap = null;
			String contract_status = null;
			String bankNum="";
			String business_partner_name="";
			String business_partner_nbr="";
			String phone_nbr="";
			if(contracts.startsWith("{")){
				log.info("只有一条合同信息");
				contractMap = JSONObject.parseObject(contracts);
				String dataStr = contractMap.get("contract")+"";
				Map<String, Object> dataMap = JSONObject.parseObject(dataStr);
				if(!contract_code.equals(dataMap.get("contract_nbr")+"")){
					log.info("当前绑定合同号与查询结果的合同号不符["+contract_code+dataMap.get("contract_nbr")+"]");
					return Util.returnMap("9998", "未查到当前绑定合同信息");
				}
				contractMap.put("contract", dataMap);
				contract_status = (String)dataMap.get("contract_status");
				bankNum=(String)dataMap.get("account_nbr");//卡号
				business_partner_name=(String)dataMap.get("business_partner_name");//姓名
				business_partner_nbr=(String)dataMap.get("business_partner_nbr");//身份证号
				phone_nbr=(String)dataMap.get("phone_nbr");
			}else{

				log.info("有多条合同信息！");
				List<Object> contractsList = JSONArray.parseArray(contracts);
				int i = -1;
				Map<String, Object> dataMap = null;
				for(i = 0; i < contractsList.size(); i++){
					String contractStr = contractsList.get(i)+"";
					dataMap = JSONObject.parseObject(contractStr);
					log.info(contract_code+"=="+dataMap.get("contract_nbr"));
					if(contract_code.equals(dataMap.get("contract_nbr")+"")){
						break;
					}
				}
				if(i == contractsList.size()){
					return Util.returnMap("9998", "未查到当前绑定合同信息");
				}
				contractMap = new HashMap<String, Object>();
				contractMap.put("contract", dataMap);
				contract_status = (String)dataMap.get("contract_status");
				bankNum=(String)dataMap.get("account_nbr");//卡号
				business_partner_name=(String)dataMap.get("business_partner_name");//姓名
				business_partner_nbr=(String)dataMap.get("business_partner_nbr");//身份证号
				phone_nbr=(String)dataMap.get("phone_nbr");
			}
			Map<String, String> retMap=new HashMap<String,String>();
			retMap.put("bandNum", bankNum);
			retMap.put("business_partner_name", business_partner_name);
			retMap.put("business_partner_nbr", business_partner_nbr);
			retMap.put("phone_nbr", phone_nbr);
			retMap.put("errcode", "0");
			retMap.put("errmsg", "查询预留信息成功");
			return retMap;
		}catch(SqlExecException sqlEx){
			log.error("sql执行异常,继续上抛，触发回滚");
			throw sqlEx;
		}
		catch(Exception ex){
			log.error("申请异常", ex);
			throw ex;
		}
	}
	/**
	 * 提前还款校验
	 * */
	public Map<String,String> wxearlyrepaycheck(Map<String,String> param) throws Exception{

		try{
			String openid = param.get("openid");
			String application_date = param.get("application_date");
			String total = param.get("total");
			String contract_nbr = param.get("contract_nbr");
			String repay_type = param.get("repay_type");
			log.info("openid:"+openid);
			log.info("还款日期application_date"+application_date);
			log.info("还款总金额total:"+total);
			log.info("合同编号contract_nbr"+contract_nbr);
			log.info("还款方式："+repay_type);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.info("微信识别号openid为空");
				return Util.returnMap("9999", "获取信息失败，请尝试重新打开页面或联系客服");
			}
			if(StringUtils.isEmpty(application_date)||"undefined".equals(application_date)){
				log.info("还款日期为空");
				return Util.returnMap("9999", "请输入提前还款日期");
			}
			if(StringUtils.isEmpty(total)||"undefined".equals(total)){
				log.info("还款总金额为空");
				return Util.returnMap("9999", "还款总金额为空，请检查预算结果");
			}
			if(StringUtils.isEmpty(contract_nbr)||"undefined".equals(contract_nbr)){
				log.info("合同号为空");
				return Util.returnMap("9999", "合同号为空，请检查数据");
			}
			if(StringUtils.isEmpty(repay_type) || "undefined".equals(repay_type)){
				log.info("还款方式为空");
				return Util.returnMap("9999", "还款方式错误，请检查");
			}
			log.info("开始重新查询试算结果");
			Map<String, String> earlyMap = getEarlyPayment(param);
			if(!"0".equals(earlyMap.get("errcode"))){
				log.info("查询还款试算结果失败：ret:"+earlyMap);
				return earlyMap;
			}
			log.info("开始校验总金额");
			if(!total.equals(earlyMap.get("total"))){
				log.info("申请总金额与试算结果不符，["+total+"=="+earlyMap.get("total")+"]");
				return Util.returnMap("9999", "申请总金额错误，请重新试算");
			}
			log.info("开始校验合同号");
			if(!contract_nbr.equals(earlyMap.get("contract_nbr"))){
				log.info("申请合同号与试算结果不符，["+contract_nbr+"=="+earlyMap.get("contract_nbr")+"]");
				return Util.returnMap("9999", "合同号与绑定合同号不符，重新试算");
			}
			String contract_id = earlyMap.get("contract_id");
			String capital = earlyMap.get("capital");
			String interest = earlyMap.get("interest");
			String penalty = earlyMap.get("penalty");
			String user_name = earlyMap.get("user_name");
			log.info("合同id:"+contract_id);
			log.info("未还本金:"+capital);
			log.info("未还利息:"+interest);
			log.info("违约金:"+penalty);
			log.info("总金额:"+total);
			log.info("申请者姓名："+user_name);
			log.info("开始校验已还款期数");
			Map<String, String> repayHisMap = getRepaymentHistory(param);
			log.info("还款计划："+repayHisMap);
			if(!"0".equals(repayHisMap.get("errcode"))){
				return Util.returnMap("9999", "未获取到还款记录");
			}
			String repayJson = repayHisMap.get("repayment");
			Map<String, Object> repayMap = JSONObject.parseObject(repayJson);
			String repayment_term = repayMap.get("repayment_term")+"";
			int repayNum = Integer.parseInt(repayment_term);
			if(repayNum < 6){
				log.info("已还款"+repayNum+"期，未满足超过6期的条件");
				return Util.returnMap("9999", "您需要正常还款6个月以后才可以申请提前还款");
			}
			log.info("开始查询是否有提前还款申请");
			Gw_repay_reg repayEntity = null;
			repayEntity = dao.fetch(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_nbr))
					.and(Gw_repay_regMeta.status.notEq("4"))
					.and(Gw_repay_regMeta.status.notEq("5")));
			if(repayEntity == null){
				Map<String, String> retMap = new HashMap<String, String>();
				return Util.returnMap("0", "提前还款检查成功", retMap);
			}else{
				log.info("已有提前还款记录，开始校验信息");
				String status = repayEntity.getStatus();
				log.info("申请状态:"+status);
				switch(status){
				case "1":
					//未处理
					log.info("申请未处理，可以对原申请进行更改");
					break;
				case "2":
					log.info("申请已正常处理，不可以对原申请进行更改");
					//已正常处理
					return Util.returnMap("9999", "申请已正常处理，不可以进行更改");
				case "3":
					log.info("申请已附条件处理，不可以对原申请进行更改");
					//已附条件处理（上传凭证不符合条件时）
					return Util.returnMap("9999", "申请已正常处理，不可以进行更改");
				case "4":
					log.info("申请已失效，不可以对原申请进行更改");
					//已失效
					return Util.returnMap("9999", "申请已正常处理，不可以对原申请进行更改");
				case "5":
					log.info("申请已结清，不可以对原申请进行更改");
					//已结清
					return Util.returnMap("9999", "申请已正常处理，不可以对原申请进行更改");
				default:
					log.error("未知状态");
					return Util.returnMap("9999", "申请失败，请联系客服");
				}
				log.info("开始查询当日的更改记录条数");
				String dateStr = Util.formatDate(System.currentTimeMillis());
				log.info("更改时间dateStr:"+dateStr);
				String application_num = repayEntity.getApplication_num();
				log.info("申请单号:"+application_num);
				List<Gw_repay_reg_hst> repayHstList = dao.list(Gw_repay_reg_hst.class, Cnd
						.where(Gw_repay_reg_hstMeta.application_num.eq(application_num))
						.and(Gw_repay_reg_hstMeta.flag.eq("1"))
						.and(Gw_repay_reg_hstMeta.rev_date.eq(dateStr.substring(0, 8))));
				int updateNum = repayHstList.size();
				log.info("更改提前还款记录第"+updateNum+"次");
				if(updateNum >= 3){
					log.info("今日修改次数超过三次");
					return Util.returnMap("9999", "当日提交次数超限，请明日再试");
				}
				
				Map<String, String> retMap = new HashMap<String, String>();
				return Util.returnMap("0", "提前还款检查成功", retMap);
			}
		}
		catch(SqlExecException sqlEx){
			log.error("sql执行异常,继续上抛，触发回滚");
			throw sqlEx;
		}
		catch(Exception ex){
			log.error("申请异常", ex);
			throw ex;
		}
	
	}
	/**
	 * 提前还款申请
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> earlyPaymentApply(Map<String, String> param) throws Exception{
		try{
			String openid = param.get("openid");
			String application_date = param.get("application_date");
			String total = param.get("total");
			String contract_nbr = param.get("contract_nbr");
			String repay_type = param.get("repay_type");
			String phone_nbr=param.get("phone_nbr");
			log.info("openid:"+openid);
			log.info("还款日期application_date"+application_date);
			log.info("还款总金额total:"+total);
			log.info("合同编号contract_nbr"+contract_nbr);
			log.info("还款方式："+repay_type);
			log.info("合同关联手机"+phone_nbr);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.info("微信识别号openid为空");
				return Util.returnMap("9999", "获取信息失败，请尝试重新打开页面或联系客服");
			}
			if(StringUtils.isEmpty(application_date)||"undefined".equals(application_date)){
				log.info("还款日期为空");
				return Util.returnMap("9999", "请输入提前还款日期");
			}
			if(StringUtils.isEmpty(total)||"undefined".equals(total)){
				log.info("还款总金额为空");
				return Util.returnMap("9999", "还款总金额为空，请检查预算结果");
			}
			if(StringUtils.isEmpty(contract_nbr)||"undefined".equals(contract_nbr)){
				log.info("合同号为空");
				return Util.returnMap("9999", "合同号为空，请检查数据");
			}
			if(StringUtils.isEmpty(repay_type) || "undefined".equals(repay_type)){
				log.info("还款方式为空");
				return Util.returnMap("9999", "还款方式错误，请检查");
			}
			log.info("开始重新查询试算结果");
			Map<String, String> earlyMap = getEarlyPayment(param);
			if(!"0".equals(earlyMap.get("errcode"))){
				log.info("查询还款试算结果失败：ret:"+earlyMap);
				return earlyMap;
			}
			log.info("开始校验总金额");
			if(!total.equals(earlyMap.get("total"))){
				log.info("申请总金额与试算结果不符，["+total+"=="+earlyMap.get("total")+"]");
				return Util.returnMap("9999", "申请总金额错误，请重新试算");
			}
			log.info("开始校验合同号");
			if(!contract_nbr.equals(earlyMap.get("contract_nbr"))){
				log.info("申请合同号与试算结果不符，["+contract_nbr+"=="+earlyMap.get("contract_nbr")+"]");
				return Util.returnMap("9999", "合同号与绑定合同号不符，重新试算");
			}
			String contract_id = earlyMap.get("contract_id");
			String capital = earlyMap.get("capital");
			String interest = earlyMap.get("interest");
			String penalty = earlyMap.get("penalty");
			String user_name = earlyMap.get("user_name");
			log.info("合同id:"+contract_id);
			log.info("未还本金:"+capital);
			log.info("未还利息:"+interest);
			log.info("违约金:"+penalty);
			log.info("总金额:"+total);
			log.info("申请者姓名："+user_name);
			log.info("开始校验已还款期数");
			Map<String, String> repayHisMap = getRepaymentHistory(param);
			log.info("还款计划："+repayHisMap);
			if(!"0".equals(repayHisMap.get("errcode"))){
				return Util.returnMap("9999", "未获取到还款记录");
			}
			String repayJson = repayHisMap.get("repayment");
			Map<String, Object> repayMap = JSONObject.parseObject(repayJson);
			String repayment_term = repayMap.get("repayment_term")+"";
			int repayNum = Integer.parseInt(repayment_term);
			if(repayNum < 6){
				log.info("已还款"+repayNum+"期，未满足超过6期的条件");
				return Util.returnMap("9999", "您需要正常还款6个月以后才可以申请提前还款");
			}
			log.info("开始查询是否有提前还款申请");
			Gw_repay_reg repayEntity = null;
			repayEntity = dao.fetch(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_nbr))
					.and(Gw_repay_regMeta.status.notEq("4"))
					.and(Gw_repay_regMeta.status.notEq("5")));
			if(repayEntity == null){
				repayEntity = new Gw_repay_reg();
				log.info("未查到提前还款申请，直接登记申请表");
				String application_num = getApplicationNum();
				log.info("申请单编号:"+application_num);
				String dateStr = Util.formatDate(System.currentTimeMillis());
				log.info("申请日期:"+dateStr);
				repayEntity.setOpenid(openid);
				repayEntity.setApplication_num(application_num);
				repayEntity.setApplicationer(user_name);
				repayEntity.setApply_repay_date(application_date.replaceAll("-", ""));
				repayEntity.setSurplus_amt(capital);
				repayEntity.setInterest(interest);
				repayEntity.setPenalty(penalty);
				repayEntity.setTotal_amt(total);
				repayEntity.setContract_code(contract_nbr);
				repayEntity.setRepay_type(param.get("repay_type"));
				repayEntity.setRepay_voucher("");
				repayEntity.setStatus("1");
				repayEntity.setReclaim_person("");
				repayEntity.setApply_date(dateStr.substring(0, 8));
				repayEntity.setApply_time(dateStr.substring(8, 14));
				repayEntity.setApply_date_time(dateStr.substring(0, 14));
				try{
					log.info("登记提前还款记录");
					dao.insert(repayEntity);
				}catch(Exception ex){
					throw new SqlExecException("登记申请记录表异常");
				}
				log.info("开始登记申请记录表");
				Gw_repay_reg_hst repayHstEntity = new Gw_repay_reg_hst();
				repayHstEntity.setOpenid(openid);
				repayHstEntity.setApplication_num(application_num);
				repayHstEntity.setApplicationer(user_name);
				repayHstEntity.setApply_repay_date(application_date.replaceAll("-", ""));
				repayHstEntity.setSurplus_amt(capital);
				repayHstEntity.setInterest(interest);
				repayHstEntity.setPenalty(penalty);
				repayHstEntity.setTotal_amt(total);
				repayHstEntity.setContract_code(contract_nbr);
				repayHstEntity.setRepay_type(param.get("repay_type"));
				repayHstEntity.setRepay_voucher("");
				repayHstEntity.setStatus("1");
				repayHstEntity.setApply_date(dateStr.substring(0, 8));
				repayHstEntity.setApply_time(dateStr.substring(8, 14));
				repayHstEntity.setFlag("1");
				repayHstEntity.setOperator(user_name);
				repayHstEntity.setRev_date(dateStr.substring(0, 8));
				repayHstEntity.setRev_time(dateStr.substring(8, 14));
				try{
					log.info("登记申请记录表");
					dao.insert(repayHstEntity);
				}catch(Exception ex){
					throw new SqlExecException("登记申请记录表异常");
				}
				log.info("申请流程处理成功");
				/*
				 * 需添加【通知客户申请成功的短信和微信】的代码
				 * */
				String tempType = "";
				if("1".equals(repay_type)){//绑定银行卡
					tempType = "03";
					String a=application_date.replaceAll("-", "");
					Util.getSpecifiedDayBefore(a);//前一日
					String b=a.substring(0,4)+"/"+a.substring(4,6)+"/"+a.substring(6,8);
					String duxin_xml="尊敬的"+user_name+"：我们已经受理您的提前结清申请，提前结清金额"+total+"元，提前结清日："+application_date.replaceAll("-", "/")+"日。客服人员将于1个工作日内与您电话核实，请您注意接听，若电话无法接通，您本次申请将自动取消，感谢您的支持与配合！客服热线400 6527 606，祝您万事如意！";
					log.info("短信内容"+duxin_xml);
					log.info("========通过新短信平台转发=======");
					String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
					String source=redis.getString("MSG_MANAGER_FLAG");
					String user=redis.getString("MSG_USER");
					String pass=redis.getString("MSG_PASS");
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
					stringBuffer.append("<root>");
					stringBuffer.append("<msgs>");
					stringBuffer.append("<msg>");
					stringBuffer.append("<account>"+user+"</account>");
					stringBuffer.append("<pwd>"+pass+"</pwd>");
					stringBuffer.append("<origin>"+source+"</origin>");
					stringBuffer.append("<phone>"+phone_nbr+"</phone>");
					stringBuffer.append("<content>"+duxin_xml+"</content>");
					stringBuffer.append("</msg>");
					stringBuffer.append("</msgs>");
					stringBuffer.append("</root>");
					String requestXML=stringBuffer.toString();
					log.info("requestXML="+requestXML);
					boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
					if(f){
						log.info("短信发送成功");
						JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
						Map<String,String> map=new HashMap<String,String>();
						String sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
								+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
						Format format = new SimpleDateFormat("yyyyMMdd");
						String sysdate=format.format(new Date());
						map.put("date", sysdate);
						map.put("phone", phone_nbr);
						map.put("type", "1");
						map.put("content", duxin_xml);
						map.put("status", "1");
						map.put("wx_msg_id", "");
						try{
							sql=SqlManager.getSql(sql, map);
							jdbc.update(sql);
						}catch(Exception e){
							log.error("短信验证码登记失败sql:"+sql);
						}
					}else{
						log.error("调用短信发送接口异常");
					}
				}else if("2".equals(repay_type)){//对公
					tempType = "04";
					String a=application_date.replaceAll("-", "");
					Util.getSpecifiedDayBefore(a);//前一日
					String b=a.substring(0,4)+"/"+a.substring(4,6)+"/"+a.substring(6,8);
					String duxin_xml="尊敬的"+user_name+"：我们已经受理您的提前结清申请，提前结清金额"+total+"元，提前结清日："+application_date.replaceAll("-", "/")+"日，请于"+b+"日前柜台汇款或支付宝转账至对公账户：天津长城滨银汽车金融有限公司，账号：273981994006 开户行：中国银行股份有限公司天津滨海分行。客服热线400 6527 606，祝您万事如意！";
					log.info("短信内容"+duxin_xml);
					log.info("========通过新短信平台转发=======");
					String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
					String source=redis.getString("MSG_MANAGER_FLAG");
					String user=redis.getString("MSG_USER");
					String pass=redis.getString("MSG_PASS");
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
					stringBuffer.append("<root>");
					stringBuffer.append("<msgs>");
					stringBuffer.append("<msg>");
					stringBuffer.append("<account>"+user+"</account>");
					stringBuffer.append("<pwd>"+pass+"</pwd>");
					stringBuffer.append("<origin>"+source+"</origin>");
					stringBuffer.append("<phone>"+phone_nbr+"</phone>");
					stringBuffer.append("<content>"+duxin_xml+"</content>");
					stringBuffer.append("</msg>");
					stringBuffer.append("</msgs>");
					stringBuffer.append("</root>");
					String requestXML=stringBuffer.toString();
					log.info("requestXML="+requestXML);
					boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
					if(f){
						log.info("短信发送成功");
						JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
						Map<String,String> map=new HashMap<String,String>();
						String sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
								+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
						Format format = new SimpleDateFormat("yyyyMMdd");
						String sysdate=format.format(new Date());
						map.put("date", sysdate);
						map.put("phone", phone_nbr);
						map.put("type", "1");
						map.put("content", duxin_xml);
						map.put("status", "1");
						map.put("wx_msg_id", "");
						try{
							sql=SqlManager.getSql(sql, map);
							jdbc.update(sql);
						}catch(Exception e){
							log.error("短信验证码登记失败sql:"+sql);
						}
					}else{
						log.error("调用短信发送接口异常");
					}
				}
				Map<String, String> retMap = new HashMap<String, String>();
				Gw_wx_general_information tempEntity = null;
				tempEntity = dao.fetch(Gw_wx_general_information.class, Cnd
						.where(Gw_wx_general_informationMeta.id.eq(tempType)));
				if(tempEntity != null){
					retMap.put("color", tempEntity.getColor());
					retMap.put("template", tempEntity.getContent());
					retMap.put("template_id", tempEntity.getInformation_id());
					retMap.put("title", tempEntity.getInformation_name());
					retMap.put("url", tempEntity.getInformation_url());
					retMap.put("name", user_name);
					retMap.put("application_date", application_date);
					retMap.put("total", total);
				}
				return Util.returnMap("0", "申请成功，请等待申请结果", retMap);
			}else{
				log.info("已有提前还款记录，开始校验信息");
				String status = repayEntity.getStatus();
				log.info("申请状态:"+status);
				switch(status){
				case "1":
					//未处理
					log.info("申请未处理，可以对原申请进行更改");
					break;
				case "2":
					log.info("申请已正常处理，不可以对原申请进行更改");
					//已正常处理
					return Util.returnMap("9999", "申请已正常处理，不可以进行更改");
				case "3":
					log.info("申请已附条件处理，不可以对原申请进行更改");
					//已附条件处理（上传凭证不符合条件时）
					return Util.returnMap("9999", "申请已正常处理，不可以进行更改");
				case "4":
					log.info("申请已失效，不可以对原申请进行更改");
					//已失效
					return Util.returnMap("9999", "申请已正常处理，不可以对原申请进行更改");
				case "5":
					log.info("申请已结清，不可以对原申请进行更改");
					//已结清
					return Util.returnMap("9999", "申请已正常处理，不可以对原申请进行更改");
				default:
					log.error("未知状态");
					return Util.returnMap("9999", "申请失败，请联系客服");
				}
				log.info("开始查询当日的更改记录条数");
				String dateStr = Util.formatDate(System.currentTimeMillis());
				log.info("更改时间dateStr:"+dateStr);
				String application_num = repayEntity.getApplication_num();
				log.info("申请单号:"+application_num);
				List<Gw_repay_reg_hst> repayHstList = dao.list(Gw_repay_reg_hst.class, Cnd
						.where(Gw_repay_reg_hstMeta.application_num.eq(application_num))
						.and(Gw_repay_reg_hstMeta.flag.eq("1"))
						.and(Gw_repay_reg_hstMeta.rev_date.eq(dateStr.substring(0, 8))));
				int updateNum = repayHstList.size();
				log.info("更改提前还款记录第"+updateNum+"次");
				if(updateNum >= 3){
					log.info("今日修改次数超过三次");
					return Util.returnMap("9999", "当日提交次数超限，请明日再试");
				}
				log.info("当然修改次数少于3次，修改申请表信息");
				repayEntity.setApply_repay_date(application_date.replaceAll("-", ""));
				repayEntity.setSurplus_amt(capital);
				repayEntity.setInterest(interest);
				repayEntity.setPenalty(penalty);
				repayEntity.setTotal_amt(total);
				repayEntity.setContract_code(contract_nbr);
				repayEntity.setRepay_type(param.get("repay_type"));
				if("1".equals(repay_type)){
					repayEntity.setRepay_voucher("");
				}
				try{
					dao.update(repayEntity, Cnd
							.where(Gw_repay_regMeta.application_num.eq(application_num)));
				}catch(Exception ex){
					throw new SqlExecException("登记申请记录表异常");
				}
				log.info("开始登记申请记录表");
				Gw_repay_reg_hst repayHstEntity = new Gw_repay_reg_hst();
				repayHstEntity.setOpenid(openid);
				repayHstEntity.setApplication_num(application_num);
				repayHstEntity.setApplicationer(user_name);
				repayHstEntity.setApply_repay_date(application_date.replaceAll("-", ""));
				repayHstEntity.setSurplus_amt(capital);
				repayHstEntity.setInterest(interest);
				repayHstEntity.setPenalty(penalty);
				repayHstEntity.setTotal_amt(total);
				repayHstEntity.setContract_code(contract_nbr);
				repayHstEntity.setRepay_type(param.get("repay_type"));
				repayHstEntity.setRepay_voucher("");
				repayHstEntity.setStatus("1");
				repayHstEntity.setApply_date(dateStr.substring(0, 8));
				repayHstEntity.setApply_time(dateStr.substring(8, 14));
				repayHstEntity.setFlag("1");
				repayHstEntity.setOperator(user_name);
				repayHstEntity.setRev_date(dateStr.substring(0, 8));
				repayHstEntity.setRev_time(dateStr.substring(8, 14));
				try{
					log.info("登记申请记录表");
					dao.insert(repayHstEntity);
				}catch(Exception ex){
					throw new SqlExecException("登记申请记录表异常");
				}
				log.info("申请流程处理成功");
				/*
				 * 需添加【通知客户申请成功的短信和微信】的代码
				 * */
				String tempType = "";
				if("1".equals(repay_type)){
					tempType = "03";
					String a=application_date.replaceAll("-", "");
					Util.getSpecifiedDayBefore(a);//前一日
					String b=a.substring(0,4)+"/"+a.substring(4,6)+"/"+a.substring(6,8);
					String duxin_xml="尊敬的"+user_name+"：我们已经受理您的提前结清申请，提前结清金额"+total+"元，提前结清日："+application_date.replaceAll("-", "/")+"日。客服人员将于1个工作日内与您电话核实，请您注意接听，若电话无法接通，您本次申请将自动取消，感谢您的支持与配合！客服热线400 6527 606，祝您万事如意！";
					log.info("短信内容"+duxin_xml);
					log.info("========通过新短信平台转发=======");
					String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
					String source=redis.getString("MSG_MANAGER_FLAG");
					String user=redis.getString("MSG_USER");
					String pass=redis.getString("MSG_PASS");
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
					stringBuffer.append("<root>");
					stringBuffer.append("<msgs>");
					stringBuffer.append("<msg>");
					stringBuffer.append("<account>"+user+"</account>");
					stringBuffer.append("<pwd>"+pass+"</pwd>");
					stringBuffer.append("<origin>"+source+"</origin>");
					stringBuffer.append("<phone>"+phone_nbr+"</phone>");
					stringBuffer.append("<content>"+duxin_xml+"</content>");
					stringBuffer.append("</msg>");
					stringBuffer.append("</msgs>");
					stringBuffer.append("</root>");
					String requestXML=stringBuffer.toString();
					log.info("requestXML="+requestXML);
					boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
					if(f){
						log.info("短信发送成功");
						JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
						Map<String,String> map=new HashMap<String,String>();
						String sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
								+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
						Format format = new SimpleDateFormat("yyyyMMdd");
						String sysdate=format.format(new Date());
						map.put("date", sysdate);
						map.put("phone", phone_nbr);
						map.put("type", "1");
						map.put("content", duxin_xml);
						map.put("status", "1");
						map.put("wx_msg_id", "");
						try{
							sql=SqlManager.getSql(sql, map);
							jdbc.update(sql);
						}catch(Exception e){
							log.error("短信验证码登记失败sql:"+sql);
						}
					}else{
						log.error("调用短信发送接口异常");
					}
				}else if("2".equals(repay_type)){
					tempType = "04";
					tempType = "04";
					String a=application_date.replaceAll("-", "");
					Util.getSpecifiedDayBefore(a);//前一日
					String b=a.substring(0,4)+"/"+a.substring(4,6)+"/"+a.substring(6,8);
					String duxin_xml="尊敬的"+user_name+"：我们已经受理您的提前结清申请，提前结清金额"+total+"元，提前结清日："+application_date.replaceAll("-", "/")+"日，请于"+b+"日前柜台汇款或支付宝转账至对公账户：天津长城滨银汽车金融有限公司，账号：273981994006 开户行：中国银行股份有限公司天津滨海分行。客服热线400 6527 606，祝您万事如意！";
					log.info("短信内容"+duxin_xml);
					log.info("========通过新短信平台转发=======");
					String url=redis.getString("NEW_SENDMSG_URL");//新短信平台地址
					String source=redis.getString("MSG_MANAGER_FLAG");
					String user=redis.getString("MSG_USER");
					String pass=redis.getString("MSG_PASS");
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
					stringBuffer.append("<root>");
					stringBuffer.append("<msgs>");
					stringBuffer.append("<msg>");
					stringBuffer.append("<account>"+user+"</account>");
					stringBuffer.append("<pwd>"+pass+"</pwd>");
					stringBuffer.append("<origin>"+source+"</origin>");
					stringBuffer.append("<phone>"+phone_nbr+"</phone>");
					stringBuffer.append("<content>"+duxin_xml+"</content>");
					stringBuffer.append("</msg>");
					stringBuffer.append("</msgs>");
					stringBuffer.append("</root>");
					String requestXML=stringBuffer.toString();
					log.info("requestXML="+requestXML);
					boolean f=com.gwm.module.loanmanager.service.LoanManagerService.NewSendmsg(url,requestXML);
					if(f){
						log.info("短信发送成功");
						JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
						Map<String,String> map=new HashMap<String,String>();
						String sql="insert into gw_msg_reg (date,phone,content,type,wx_msg_id,status) "
								+ "values ('$date$','$phone$','$content$','$type$','$wx_msg_id$','$status$')";
						Format format = new SimpleDateFormat("yyyyMMdd");
						String sysdate=format.format(new Date());
						map.put("date", sysdate);
						map.put("phone", phone_nbr);
						map.put("type", "1");
						map.put("content", duxin_xml);
						map.put("status", "1");
						map.put("wx_msg_id", "");
						try{
							sql=SqlManager.getSql(sql, map);
							jdbc.update(sql);
						}catch(Exception e){
							log.error("短信验证码登记失败sql:"+sql);
						}
					}else{
						log.error("调用短信发送接口异常");
					}
				}
				Map<String, String> retMap = new HashMap<String, String>();
				Gw_wx_general_information tempEntity = null;
				tempEntity = dao.fetch(Gw_wx_general_information.class, Cnd
						.where(Gw_wx_general_informationMeta.id.eq(tempType)));
				if(tempEntity != null){
					retMap.put("color", tempEntity.getColor());
					retMap.put("template", tempEntity.getContent());
					retMap.put("template_id", tempEntity.getInformation_id());
					retMap.put("title", tempEntity.getInformation_name());
					retMap.put("url", tempEntity.getInformation_url());
					retMap.put("name", user_name);
					retMap.put("application_date", application_date);
					retMap.put("total", total);
				}
				return Util.returnMap("0", "申请成功，请等待申请结果", retMap);
			}
		}
		catch(SqlExecException sqlEx){
			log.error("sql执行异常,继续上抛，触发回滚");
			throw sqlEx;
		}
		catch(Exception ex){
			log.error("申请异常", ex);
			throw ex;
		}
	}
	
	/**
	 * 根据合同id查询逾期信息
	 * @param contract_id
	 * @return
	 */
	public Map<String, String> getOverDueInfo(String contract_code){
		try{
			StringBuffer stringBuffer = new StringBuffer();
			String method = "";
			method = "getOverDue";
			String url=redis.getString("OVERDUE_URL");
			log.info("查询逾期信息:"+url);
			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
			stringBuffer.append("<root>");
			stringBuffer.append("<contract_nbr>"+contract_code+"</contract_nbr>");
			stringBuffer.append("</root>");
			String requestXML=stringBuffer.toString();
			log.info("请求报文："+requestXML);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", method);
			//解析返回报文等到map
			log.info("请求报文："+requestXML);
			log.info("返回报文："+repxml);
			Map<String, Object> map = DecodeXmlImpl.decode(repxml);
			String flag = (String)map.get("flag");
			if(StringUtils.isEmpty(flag)||"undefined".equals(flag)){
				return Util.returnMap("0", "查询成功", Util.mapObjectToString(map));
			}else{
				return Util.returnMap("9999", "未查到记录");
			}
		}catch(Exception ex){
			log.error("查询异常", ex);
			return Util.returnMap("9999", "查询逾期信息异常");
		}
	}
	
	
	/**
	 * 获取还款计划
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> getRepaymentHistory(Map<String, String> param){
		try {
			String openid = param.get("openid");
			log.info("openid:"+openid);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("参数openid为空");
				return Util.returnMap("9999", "获取用户信息失败");
			}
			Gw_user_loan_rel userEntity = null;
			userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid)));
			if(userEntity == null){
				log.info("用户绑定信息为空");
				return Util.returnMap("9999", "您还没有绑定合同，无法进行查询");
			}
			String contract_id = userEntity.getContract_id();
			String contract_code = userEntity.getContract_code();
			log.info("合同id:"+contract_id);
			log.info("合同编号："+contract_code);
			if(StringUtils.isEmpty(contract_id)){
				log.info("合同id为空");
				return Util.returnMap("9999", "您还没有绑定合同，无法进行查询");
			}
			if(StringUtils.isEmpty(contract_code)){
				log.info("合同编号为空");
				return Util.returnMap("9999", "您还没有绑定合同，无法进行查询");
			}
			String url=redis.getString("RepaymentPlan_URL");
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
			stringBuffer.append("<root>");
			stringBuffer.append("<contract_id>"+contract_id+"</contract_id>");
			stringBuffer.append("<contract_nbr>"+contract_code+"</contract_nbr>");
			stringBuffer.append("</root>");
			
			log.info("webservice URL:"+url);
			log.info("查询还款计划 请求报文："+stringBuffer.toString());
			String requestXML=stringBuffer.toString();
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getRepaymentHistory");
			log.info("查询还款计划 请求报文："+stringBuffer.toString());
			log.info("查询还款计划 返回报文"+repxml); 
			//解析返回报文等到map
			Map<String, Object> map = DecodeXmlImpl.decode(repxml);
			log.info("查询还款计划 JSON："+map);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(map));
		} catch (Exception ex) {
			log.error("查询还款信息异常", ex);
			return Util.returnMap("9999", "查询失败");
		}
	}
	
	private String getApplicationNum(){
		String applicationNum = UUID.randomUUID()+"";
		return applicationNum;
	}
	
	public static void main(String args[]){
		try{
			StringBuffer stringBuffer = new StringBuffer();
			String method = "";
			
//			method = "getOverDue";
////			String url="http://10.255.56.161:8080/cap_data_interface/services/overdue";
//			String url="https://10.50.24.60:8443/cap_data_interface/services/overdue";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<contract_nbr>GW102095</contract_nbr>");
//			stringBuffer.append("</root>");
			
//			method = "send";
//			String url="http://10.50.128.186:8080/smswebservice/services/sendsms";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<msgs>");
//			stringBuffer.append("<msg>");
//			stringBuffer.append("<account>weixin</account>");
//			stringBuffer.append("<pwd>weixintest</pwd>");
//			stringBuffer.append("<phone>15566812881</phone>");
//			stringBuffer.append("<content>您的验证码为：675991，2分钟内有效。为保证账户安全，请勿向任何人透露。如非本人操作，请致电客服热线400 6527 606。</content>");
//			stringBuffer.append("</msg>");
//			stringBuffer.append("</msgs>");
//			stringBuffer.append("</root>");
			
//			method = "holidayCalculate";
//			String url="http://10.50.128.186:8080/cap_data_interface/services/holidaycalculate";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<application_date>"+"2016-12-15"+"</application_date>");
//			stringBuffer.append("</root>");
			
//			method = "getEarlyPayment";
//			String url="http://10.50.128.186:8080/cap_data_interface/services/earlypayment";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<application_date>"+"2016-12-13"+"</application_date>");
//			stringBuffer.append("<contract_id>"+"2170"+"</contract_id>");
//			stringBuffer.append("<contract_nbr>"+"GW100154"+"</contract_nbr>");
//			stringBuffer.append("</root>");
			
//			method = "getRepaymentHistory";
//			//String url="http://10.50.24.60:8080/cap_data_interface/services/repaymenthistory";
//			String url="https://10.50.24.60:8443/cap_data_interface/services/repaymenthistory";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<contract_id>"+"2170"+"</contract_id>");
//			stringBuffer.append("<contract_nbr>"+"GW100154"+"</contract_nbr>");
//			stringBuffer.append("</root>");
			
			method = "getContract";
//			String url="https://10.50.24.60:8443/cap_data_interface/services/contract";
			String url="http://10.50.128.150:9090/cap_data_interface/services/contract";
//			String url="http://10.50.128.186:8080/cap_data_interface/services/contract";
			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
			stringBuffer.append("<root>");
			stringBuffer.append("<application_phone>"+"18864802010"+"</application_phone>");
			stringBuffer.append("<application_id_card>"+"370902195301202131"+"</application_id_card>");
			stringBuffer.append("</root>");
			
//			method = "getOverDue";
////			String url="http://10.255.56.161:8080/cap_data_interface/services/overdue";
//			String url="http://127.0.0.1:8080/cap_data_interface/services/overdue";
//			stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//			stringBuffer.append("<root>");
//			stringBuffer.append("<contract_nbr>GW102095</contract_nbr>");
//			stringBuffer.append("</root>");
			
			String requestXML=stringBuffer.toString();

			log.info("请求:"+requestXML);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", method);
			//解析返回报文等到map
			log.info("返回:"+repxml);
			Map<String, Object> map = DecodeXmlImpl.decode(repxml);
			log.info("返回map:"+map);
			//}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("!!!!异常");
		}
	}
}