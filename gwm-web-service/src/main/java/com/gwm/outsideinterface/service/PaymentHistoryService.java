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

import com.gwm.common.DecodeXmlImpl;
import com.gwm.common.RedisDao;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_repay_reg;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_repay_regMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;
@Service
public class PaymentHistoryService {
	
	Logger log = LoggerFactory.getLogger(PaymentHistoryService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	RedisDao redis = null;
	
	/**
	 * 提前还款申请记录查询
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryEarlyRepayApply(Map<String, String> map){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		log.info("开始提前还款申请记录查询");
		String openId = map.get("openId");//openId
		
		if(StringUtils.isEmpty(openId)){
			log.info("openId为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "提前还款申请记录查询失败");
			return returnMap;
		}
		//获取openid下的个人信息
		try {
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openId))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			
			//判断该账号是否曾经绑定过
			if(queryEntity==null){
				log.info("该微信号还没有进行过绑定");
				returnMap.put("errcode", "9999");
				returnMap.put("errmsg", "请绑定后再进行相关操作");
				return returnMap;
			}
			String contract_nbr = queryEntity.getContract_code();
			log.info("合同号:"+contract_nbr);
			List<Gw_repay_reg> queryList = dao.list(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.contract_code.eq(queryEntity.getContract_code())));
			if(queryList==null || queryList.size()<=0){
				log.info("未查到提前还款申请记录");
				returnMap.put("errcode", "9999");
				returnMap.put("errmsg", "您还没有申请过提前还款");
				return returnMap;
			}
			else{
				returnMap.put("errcode", "0");
				returnMap.put("errmsg", "提前还款申请记录查询成功");
				returnMap.put("returnList", queryList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info(e.getMessage());
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "提前还款申请记录查询失败");
			return returnMap;
		}
		log.info("提前还款申请记录查询成功");
		return returnMap;
	}
	

	/**
	 * 还款记录查询
	 * @param map
	 * @return
	 */
	public Map<String, String> queryRepayRecord(Map<String, String> map){
		log.info("开始还款记录记录查询");
		Map<String,Object> returnMap = new HashMap<String, Object>();
		String openId = map.get("openId");//openId
		String contract_id="";//合同id
		String contract_nbr="";//合同号
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取微信账号失败");
		}
		
		try {
			//获取openid下的个人信息
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openId))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(queryEntity==null){
				log.info("没有获取到该openid的绑定合同信息");
				return Util.returnMap("9998", "请绑定后再进行相关操作");
			}
			else{
				contract_id=queryEntity.getContract_id();
				contract_nbr=queryEntity.getContract_code();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("还款记录接口异常:", e);
			return Util.returnMap("9998", "还款记录查询失败");
		}
		
		String url=redis.getString("RepaymentPlan_URL");//还款计划查询接口
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		stringBuffer.append("<root>");
		stringBuffer.append("<contract_id>"+contract_id+"</contract_id>");
		stringBuffer.append("<contract_nbr>"+contract_nbr+"</contract_nbr>");
		stringBuffer.append("</root>");
		// 定义客户端对象
		try {		 
			String requestXML=stringBuffer.toString();
			log.info("requestXML="+requestXML);
			String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getRepaymentHistory");
			log.info("返回报文"+repxml); 
			//解析返回报文等到map
			returnMap = DecodeXmlImpl.decode(repxml);
			if(returnMap.get("flag") != null){
				log.error("errcode:"+returnMap.get("flag"));
				log.error("errmsg:"+returnMap.get("reason"));
				return Util.returnMap("9999", returnMap.get("reason")+"，请联系客服");
			}
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(returnMap));
		} catch (Exception ex) {
			log.error("还款记录接口异常:", ex);
			return Util.returnMap("9999", "还款记录查询失败");
		}
	}
}