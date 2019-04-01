package com.gwm.outsideinterface.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_repay_reg;
import com.gwm.db.entity.Gw_repay_reg_hst;
import com.gwm.db.entity.Gw_user_info;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_repay_regMeta;
import com.gwm.db.entity.meta.Gw_user_infoMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;


@Service
public class GetContractInfo {
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	JdbcTemplate jdbc = null;
	
	@Autowired
	RedisDao redis = null;
	
	private static Logger log=LoggerFactory.getLogger(GetContractInfo.class);

	/**
	 * 获取合同信息
	 * @param phone手机号
	 * @param idnum 身份证号
	 * */
	@Transactional
	public Map<String, String> getContractInfo(String openid){
		Gw_user_loan_rel userEntity = null;
		userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
				.where(Gw_user_loan_relMeta.openid.eq(openid)));
		if(userEntity == null){
			log.info("未查到绑定信息");
			return Util.returnMap("9999", "未查到合同信息");
		}
		String status = userEntity.getStatus();
		log.info("status:"+status);
		switch(status){
		case "1":
			log.info("绑定时验证成功");
			break;
		case "2":
			log.info("绑定成功");
			break;
		case "3":
			log.info("解绑状态");
			return Util.returnMap("9999", "已解绑，无法查到合同信息");
		case "4":
			log.info("合同审核阶段");
			return Util.returnMap("9999", "合同审核中...");
		default:
			log.info("未知状态");
			return Util.returnMap("9999", "查询合同信息失败");
		}
		String phone = userEntity.getPhone();
		String idnum = userEntity.getId_num();
		String url=redis.getString("CONTRACT_URL");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		stringBuffer.append("<root>");
		stringBuffer.append("<application_phone>"+phone+"</application_phone>");
		stringBuffer.append("<application_id_card>"+idnum+"</application_id_card>");
		stringBuffer.append("</root>");
		// 定义客户端对象
		Map<String, Object>  map=null;
		try {		 
			String requestXML=stringBuffer.toString();
			log.info("requestXML="+requestXML);
			log.info("phone ="+phone);
			log.info("idnum ="+idnum);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getContract");
			log.info("返回报文"+repxml); 
			//解析返回报文等到map
			map=  DecodeXmlImpl.decode(repxml);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(map));
		} catch (Exception ex) {
			log.error("合同信息接口异常:", ex);
			return Util.returnMap("9999", "合同信息接口异常");
		}
	}
	
	@Transactional
	public Map<String, String> changeContractInfo(Map<String, String> param){
		try{
			String openid = param.get("openid");
			String contract_id = param.get("contract_id");
			String contract_nbr = param.get("contract_nbr");
			String business_partner_name = param.get("business_partner_name");
			log.info("openid:"+openid);
			log.info("contract_id:"+contract_id);
			log.info("contract_nbr:"+contract_nbr);
			log.info("business_partner_name:"+business_partner_name);
			if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
				log.info("微信openid空");
				return Util.returnMap("9999", "修改合同信息失败");
			}
			if(StringUtils.isEmpty(contract_id) || "undefined".equals(contract_id)){
				log.info("合同id空");
				return Util.returnMap("9999", "修改合同信息失败");
			}
			if(StringUtils.isEmpty(contract_nbr) || "undefined".equals(contract_nbr)){
				log.info("合同号空");
				return Util.returnMap("9999", "修改合同信息失败");
			}
			Gw_user_loan_rel userEntity = null;
			userEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid)));
			if(userEntity == null){
				log.info("未找到绑定信息");
				return Util.returnMap("9999", "未查到绑定信息，请重新绑定");
			}
			userEntity.setContract_id(contract_id);
			userEntity.setContract_code(contract_nbr);
			userEntity.setStatus("2");
			userEntity.setUser_name(business_partner_name);
			String idno = userEntity.getId_num();
			dao.update(userEntity, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid)));
			log.info("修改个人信息");
			Gw_user_info infoEntity = null;
			infoEntity = dao.fetch(Gw_user_info.class, Cnd
					.where(Gw_user_infoMeta.openid.eq(openid)));
			String age = "";
			String sex = "";
			String nowDate = Util.formatDate(System.currentTimeMillis());
			if(idno.length() == 18){
				String nowYear = nowDate.substring(0, 4);
				String userYear = idno.substring(6, 10);
				age = (Integer.parseInt(nowYear) - Integer.parseInt(userYear))+"";
				String userSex = idno.substring(16, 17);
				if(Integer.parseInt(userSex)%2 == 1){
					sex = "男";
				}else{
					sex = "女";
				}
			}
			if(infoEntity == null){
				infoEntity = new Gw_user_info();
				infoEntity.setIncome("");
				infoEntity.setOpenid(openid);
				infoEntity.setProfession("");
				infoEntity.setUser_age(age);
				infoEntity.setUser_code(contract_nbr);
				infoEntity.setUser_id(userEntity.getId_num());
				infoEntity.setUser_name(business_partner_name);
				infoEntity.setUser_phone(userEntity.getPhone());
				infoEntity.setUser_sex(sex);
				infoEntity.setUser_zone("");
				dao.insert(infoEntity);
			}else{
				infoEntity.setOpenid(openid);
				infoEntity.setUser_code(contract_nbr);
				infoEntity.setUser_id(userEntity.getId_num());
				infoEntity.setUser_name(business_partner_name);
				infoEntity.setUser_phone(userEntity.getPhone());
				infoEntity.setUser_age(age);
				infoEntity.setUser_sex(sex);
				dao.update(infoEntity, Cnd
						.where(Gw_user_infoMeta.openid.eq(openid)));
			}
			return Util.returnMap("0", "修改成功");
		}catch(Exception ex){
			log.error("修改绑定合同信息异常", ex);
			return Util.returnMap("9999", "修改合同信息失败");
		}
	}
	
	/**
	 * 查询还款凭证信息
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> getBindContractInfo(Map<String, String> param){
		
		log.info("查询绑定信息");
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
			}
			Map<String, Object> conMap = (Map<String, Object>)contractMap.get("contract");
			log.info("合同状态(L正常 N结清):"+contract_status);
			if("N".equals(contract_status)){
				log.info("合同已结清");
				contractMap.put("contract", conMap);
				map.put("contracts", contractMap);
				return Util.returnMap("9995", "您的本次贷款已全部结清，无需上传凭证", Util.mapObjectToString(map));
			}
			log.info("查到对应合同:"+contractMap);
			log.info("开始查询还款申请记录...");
			String contract_nbr = conMap.get("contract_nbr")+"";
			Gw_repay_reg repayEntity = null;
			repayEntity = dao.fetch(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_nbr))
					.and(Gw_repay_regMeta.status.notEq("4"))
					.and(Gw_repay_regMeta.status.notEq("5")));
			if(repayEntity == null){
				log.info("未查到您的提前还款申请，无法上传还款凭证");
				List<Gw_repay_reg> repayList = dao.list(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_nbr))
					.and(Gw_repay_regMeta.status.eq("5"))
					.orderby(Gw_repay_regMeta.apply_date_time.desc()));
				if(repayList.size() > 0){
					log.info("提前申请已结清，核心合同未结清，以未结清处理");
					repayEntity = repayList.get(0);
					conMap.put("total_amt", repayEntity.getTotal_amt());
					conMap.put("apply_date", repayEntity.getApply_date());
					conMap.put("apply_repay_date", repayEntity.getApply_repay_date());
					contractMap.put("contract", conMap);
					map.put("contracts", contractMap);
					return Util.returnMap("9994", "您的申请正在处理中，请耐心等待", Util.mapObjectToString(map));
				}
				return Util.returnMap("9997", "您需要在微信中申请提前还款才可上传凭证");
			}
			String status = repayEntity.getStatus();
			log.info("申请状态：status:"+status);
			String repay_type = repayEntity.getRepay_type();
			log.info("还款方式repay_type:"+repay_type);
			switch(status){
			case "1":
				log.info("贷款申请未处理状态，可以上传凭证");
				break;
			case "4":
				log.info("贷款申请已失效，无法上传凭证");
				return Util.returnMap("9996", "您的提前还款申请未审批通过，请与客服联系");
			case "5":
				log.info("贷款申请已结清，无法上传凭证");
				contractMap.put("contract", conMap);
				map.put("contracts", contractMap);
				return Util.returnMap("9995", "您的本次贷款已全部结清，无需上传凭证", Util.mapObjectToString(map));
			case "2":
				log.info("申请已正常处理，请耐心等待");
				if("1".equals(repay_type)){
					log.info("预留银行卡还款，无需上传凭证");
					conMap.put("total_amt", repayEntity.getTotal_amt());
					conMap.put("apply_date", repayEntity.getApply_date());
					conMap.put("apply_repay_date", repayEntity.getApply_repay_date());
					contractMap.put("contract", conMap);
					map.put("contracts", contractMap);
					return Util.returnMap("9993", "您的还款方式为预留银行卡还款，无需上传凭证", Util.mapObjectToString(map));
				}
				return Util.returnMap("9994", "您的申请正在处理中，请耐心等待");
			case "3":
				log.info("上传凭证不符合条件，可以重新上传凭证");
				if("1".equals(repay_type)){
					log.info("预留银行卡还款，无需上传凭证");
					conMap.put("total_amt", repayEntity.getTotal_amt());
					conMap.put("apply_date", repayEntity.getApply_date());
					conMap.put("apply_repay_date", repayEntity.getApply_repay_date());
					contractMap.put("contract", conMap);
					map.put("contracts", contractMap);
					return Util.returnMap("9993", "您的还款方式为预留银行卡还款，无需上传凭证", Util.mapObjectToString(map));
				}
				conMap.put("reupload", "1");
				break;
			default:
				log.info("！！！未知的申请状态！！！:"+status);
				return Util.returnMap("9999", "系统出现错误，请联系客服人员");
			}
			if("1".equals(repay_type)){
				log.info("预留银行卡还款，无需上传凭证");
				conMap.put("total_amt", repayEntity.getTotal_amt());
				conMap.put("apply_date", repayEntity.getApply_date());
				conMap.put("apply_repay_date", repayEntity.getApply_repay_date());
				contractMap.put("contract", conMap);
				map.put("contracts", contractMap);
				return Util.returnMap("9993", "您的还款方式为预留银行卡还款，无需上传凭证", Util.mapObjectToString(map));
			}
			log.info("条件审核通过，可以上传凭证");
			conMap.put("total_amt", repayEntity.getTotal_amt());
			conMap.put("apply_date", repayEntity.getApply_date());
			conMap.put("apply_repay_date", repayEntity.getApply_repay_date());
			contractMap.put("contract", conMap);
			map.put("contracts", contractMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(map));
		} catch (Exception ex) {
			log.error("合同信息接口异常:", ex);
			return Util.returnMap("9999", "合同信息接口异常");
		}
	}
	
	/**
	 * 更新还款凭证路径
	 * @param param
	 * @return
	 */
	@Transactional
	public Map<String, String> updateReplayVoucherPath(Map<String, String> param){
			try{
			String openid = param.get("openid");
			String voucherPath = param.get("path");
			String contract_code = param.get("contract_nbr");
			log.info("校验信息");
			log.info("openid:"+openid);
			log.info("voucherPath:"+voucherPath);
			log.info("contract_code"+contract_code);
			if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
				log.info("openid为空");
				return Util.returnMap("9999", "微信识别号为空");
			}
			if(StringUtils.isEmpty(voucherPath)||"undefined".equals(voucherPath)){
				log.info("凭证图片路径为空");
				return Util.returnMap("9999", "图片存放路径为空");
			}
			if(StringUtils.isEmpty(contract_code)||"undefined".equals(contract_code)){
				log.info("合同号为空");
				return Util.returnMap("9999", "合同号为空");
			}
			Gw_repay_reg repayEntity = dao.fetch(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_code))
					.and(Gw_repay_regMeta.status.notEq("4"))
					.and(Gw_repay_regMeta.status.notEq("5")));
			if(repayEntity == null){
				log.info("为查到提前还款申请记录！");
				return Util.returnMap("9999", "未查到您的提前还款申请，无法上传还款凭证");
			}
			String status = repayEntity.getStatus();
			switch(status){
			case "1":
				log.info("贷款申请未处理状态，可以上传凭证");
				break;
			case "4":
				log.info("贷款申请已失效，无法上传凭证");
				return Util.returnMap("9999", "您的提前还款申请未审批通过，请与客服联系");
			case "5":
				log.info("贷款申请已结清，无法上传凭证");
				return Util.returnMap("9999", "您的本次贷款已全部结清，无需上传凭证");
			case "2":
				log.info("申请已正常处理，请耐心等待");
				return Util.returnMap("9999", "您的申请正在处理中，请耐心等待");
			case "3":
				log.info("上传凭证不符合条件，可以重新上传凭证");
				break;
			default:
				log.info("！！！未知的申请状态！！！:"+status);
				return Util.returnMap("9999", "系统出现错误，请联系客服人员");
			}
			String repay_type = repayEntity.getRepay_type();
			log.info("还款方式repay_type:"+repay_type);
			if("1".equals(repay_type)){
				log.info("预留银行卡还款，无需上传凭证");
				return Util.returnMap("9999", "您的还款方式为预留银行卡还款，无需上传凭证");
			}
			repayEntity.setRepay_voucher(voucherPath);
			dao.update(repayEntity, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(contract_code))
					.and(Gw_repay_regMeta.status.notEq("4"))
					.and(Gw_repay_regMeta.status.notEq("5")));
			log.info("开始记录申请记录表");
			String dateStr = Util.formatDate(System.currentTimeMillis());
			log.info("日期:"+dateStr);
			Gw_repay_reg_hst repayHstEntity = new Gw_repay_reg_hst();
			repayHstEntity.setOpenid(openid);
			repayHstEntity.setApplication_num(repayEntity.getApplication_num());
			repayHstEntity.setApplicationer(repayEntity.getApplicationer());
			repayHstEntity.setApply_repay_date(repayEntity.getApply_repay_date());
			repayHstEntity.setSurplus_amt(repayEntity.getSurplus_amt());
			repayHstEntity.setInterest(repayEntity.getInterest());
			repayHstEntity.setPenalty(repayEntity.getPenalty());
			repayHstEntity.setTotal_amt(repayEntity.getTotal_amt());
			repayHstEntity.setContract_code(repayEntity.getContract_code());
			repayHstEntity.setRepay_type(repayEntity.getRepay_type());
			repayHstEntity.setRepay_voucher(voucherPath);
			repayHstEntity.setStatus(repayEntity.getStatus());
			repayHstEntity.setApply_date(repayEntity.getApply_date());
			repayHstEntity.setApply_time(repayEntity.getApply_time());
			repayHstEntity.setFlag("2");
			repayHstEntity.setOperator(repayEntity.getApplicationer());
			repayHstEntity.setRev_date(dateStr.substring(0, 8));
			repayHstEntity.setRev_time(dateStr.substring(8, 14));
			dao.insert(repayHstEntity);
			return Util.returnMap("0", "更新状态成功");
		}catch(Exception ex){
			log.error("更新凭证路径异常", ex);
			return Util.returnMap("9999", "上传凭证失败，请重新上传");
		}
	}
	
	public static void main(String args[])throws Exception{
		//new GetContractInfo().getContractInfo("18864802010","370902195301202131");
	}
}
