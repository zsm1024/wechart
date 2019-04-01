package com.gwm.module.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class ContractService {
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	
	Logger log = LoggerFactory.getLogger(ContractService.class);
	
	public void process(Map<String, String> map){
//		getCurrentContract;
		try{
			map.put("openid", map.get("FromUserName"));
			Map<String, String> retMap = getCurrentContract(map);
			if("0".equals(retMap.get("errcode"))){
				log.info("查到提前还款申请信息");
				
				String EventKey = map.get("EventKey");
				Map<Object, Object> menuMap = redis.getMap("clickmenumap");
				String url = menuMap.get(EventKey+"_url")+"";
				String authUrl = WxProcess.getBaseAuthUrl(url);
				
				String contracts = retMap.get("contracts");
				Map<String, Object> contractsMap = JSONObject.parseObject(contracts);
				Map<String, Object> contractMap = (Map<String, Object>)contractsMap.get("contract");
				String apply_date = (String)contractMap.get("apply_date");
				String apply_repay_date = ""+contractMap.get("apply_repay_date");
				String total_amt = ""+contractMap.get("total_amt");
				String reupload = (String)contractMap.get("reupload");
				String content = null;
				if("1".equals(reupload)){
					log.info("已附条件处理");
					content = "您好，重新上传还款凭证请";
					content += "<a href='"+authUrl+"'>点击这里</a>";
				}else{
					content = "您好，您已于"
							+apply_date.substring(0, 4)+"年"
							+apply_date.substring(4, 6)+"月"
							+apply_date.substring(6, 8)+"日申请于"
							+apply_repay_date.substring(0, 4)+"年"
							+apply_repay_date.substring(4, 6)+"月"
							+apply_repay_date.substring(6, 8)+"日提前还款，提前还款金额"
							+total_amt+"元。请";
					content += "<a href='"+authUrl+"'>点击此处</a>";
					content += "上传还款凭证。";
				}
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9993".equals(retMap.get("errcode"))){
				log.info("预留银行卡还款，无需上传凭证");
				String contracts = retMap.get("contracts");
				Map<String, Object> contractsMap = JSONObject.parseObject(contracts);
				Map<String, Object> contractMap = (Map<String, Object>)contractsMap.get("contract");
				String apply_date = (String)contractMap.get("apply_date");
				String apply_repay_date = ""+contractMap.get("apply_repay_date");
				String total_amt = ""+contractMap.get("total_amt");
				String content = "您好，您已于"
						+apply_date.substring(0, 4)+"年"+apply_date.substring(4, 6)+"月"
						+apply_date.substring(6, 8)+"日申请通过预留银行卡提前还款，无须上传还款凭证。请于"
						+apply_repay_date.substring(0, 4)+"年"
						+apply_repay_date.substring(4, 6)+"月"
						+apply_repay_date.substring(6, 8)+"日前向预留银行卡中存入"
						+total_amt+"元，感谢您的支持！";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9994".equals(retMap.get("errcode"))){
				log.info("申请已正常处理");
				String content = "您好，您已上传还款凭证，稍后工作人员进行结清操作，请及时关注相关短信通知。";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9995".equals(retMap.get("errcode"))){
				log.info("贷款申请已结清");
				String url = redis.getString("repayrecordurl");
				String authUrl = WxProcess.getBaseAuthUrl(url);
				String contracts = retMap.get("contracts");
				Map<String, Object> contractsMap = JSONObject.parseObject(contracts);
				Map<String, Object> contractMap = (Map<String, Object>)contractsMap.get("contract");
				String contract_nbr = (String)contractMap.get("contract_nbr");
				String content = "您好，您的合同单号"+contract_nbr+"下的债务已经结清，感谢您的支持！\n"
						+ "如需了解还款详情，请";
				content += "<a href='"+authUrl+"'>点击这里</a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9997".equals(retMap.get("errcode"))){
				log.info("未查到您的提前还款申请");
				String content = "您好，当前仅支持微信端提前还款申请的还款凭证上传。";
				content += "很抱歉，您未在微信端申请过提前还款，不能进行此操作。";
				content += "若您需要上传相关凭证，请致电客服热线4006527606。";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9998".equals(retMap.get("errcode"))){
				log.info("未绑定");
				String url = redis.getString("wxbindurl");
				String authUrl = WxProcess.getBaseAuthUrl(url);
				String content = "您好，您暂未绑定账户，无法进行相应操作。";
				content += "<a href='"+authUrl+"'>点击这里，绑定账户</a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else{
				String content = "查询失败，请联系客服。";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}
		}catch(Exception ex){
			log.error("查询提前还款信息异常", ex);
			String content = "查询失败，请联系客服。";
			wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
		}
	}
	
	public Map<String, String> getContractList(Map<String, String> map){
		
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		log.info("请求参数："+map);
		String openid = map.get("openid");
		if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
			log.error("微信编号openid为空");
			return Util.returnMap("9999", "查询失败");
		}
		Object obj = com.gwm.common.service.Service.getContractInfo(map);
		String retJson = (String)obj;
		log.info("查询合同信息返回："+retJson);
		if(StringUtils.isEmpty(retJson)){
			log.info("查询合同信息失败");
			return Util.returnMap("9999", "获取合同信息失败");
		}
		
		Map<String, Object> rootMap = JSONObject.parseObject(retJson);
		if(rootMap.get("flag") != null){
			log.info("errcode:"+rootMap.get("flag"));
			log.info("errmsg:"+rootMap.get("reason"));
			if("true".equals(rootMap.get("flag"))){
				return Util.returnMap("9999", "未检索到相关合同信息");
			}
			return Util.returnMap("9999", rootMap.get("reason")+"");
		}
		if(!"0".equals(rootMap.get("errcode"))){
			return Util.mapObjectToString(rootMap);
		}
		String contracts = rootMap.get("contracts")+"";
		log.info("contracts:"+contracts);
		if(contracts.startsWith("{")){
			log.info("只有一条合同信息，转为list");
			Map<String, Object> contractMap = null;
			contractMap = JSONObject.parseObject(contracts);
			String dataStr = contractMap.get("contract")+"";
			Map<String, Object> dataMap = JSONObject.parseObject(dataStr);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(dataMap);
			log.info("list:"+list);
			rootMap.put("contracts", list);
		}
		log.info("合同信息结果:"+rootMap);
		
		return Util.returnMap("0", "查询成功", Util.mapObjectToString(rootMap));
	}
	
	/**
	 * 修改合同信息
	 * @param param
	 * @return
	 */
	public Map<String, String> changeContractInfo(Map<String, String> param){
		String openid = param.get("openid");
		String contract_id = param.get("contract_id");
		String contract_nbr = param.get("contract_nbr");
		log.info("openid:"+openid);
		log.info("contract_id:"+contract_id);
		log.info("contract_nbr:"+contract_nbr);
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
		
		Object obj = com.gwm.common.service.Service.changeContractInfo();
		Map<String, Object> retMap = JSONObject.parseObject(obj+"");
		log.info("查询结果："+retMap);
		return Util.mapObjectToString(retMap);
	}
	
	public Map<String, String> getCurrentContract(Map<String, String> map){
		
		String openid = map.get("openid");
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("用户openid为空！");
			return Util.returnMap("9999", "获取用户信息失败");
		}
		Object obj = com.gwm.common.service.Service.getCurrentContract(map);
		Map<String, Object> retMap = JSONObject.parseObject(obj+"");
		log.info("查询结果："+retMap);
		return Util.mapObjectToString(retMap);
	}
}
