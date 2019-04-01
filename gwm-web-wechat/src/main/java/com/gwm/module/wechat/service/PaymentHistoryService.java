package com.gwm.module.wechat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class PaymentHistoryService {
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	MyLoanService myLoanService = null;
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	
	Logger log = LoggerFactory.getLogger(PaymentHistoryService.class);
	
	public void process(Map<String, String> map){
		try{
			log.info("map:"+map);
			map.put("openId", map.get("FromUserName"));
			Map<String, Object> retMap = queryRepayRecord(map);
			
			log.info("返回结果:"+retMap);
			if("0".equals(retMap.get("errcode"))){
				String content = null;
				String EventKey = map.get("EventKey");
				Map<Object, Object> menuMap = redis.getMap("clickmenumap");
				String url = menuMap.get(EventKey+"_url")+"";
				String authUrl = WxProcess.getBaseAuthUrl(url);
				Map<String, Object> bindMap = myLoanService.getBindingContract(map);
				log.info("查询绑定合同信息:"+bindMap);
				if(!"0".equals(bindMap.get("errcode"))){
					log.error("errMsg:"+bindMap);
					content = "未查到合同信息！请联系客服";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return ;
				}
				Map<String, Object> contractMap = (Map<String, Object>)bindMap.get("contract");
				String contract_status = (String)contractMap.get("contract_status");
				log.info("合同状态:"+contract_status);
				String contract_nbr = (String)contractMap.get("contract_nbr");
				String overdue = (String)contractMap.get("overdue");
				String overdue_amt = (String)contractMap.get("overdue_amt");
				log.info("逾期天数:"+overdue);
				log.info("逾期金额:"+overdue_amt);
				if("N".equals(contract_status)){
					log.info("已结清");
					url = redis.getString("repayrecordurl");
					authUrl = WxProcess.getBaseAuthUrl(url);
					content = "您好，您的合同单号"+contract_nbr+"下的债务已经结清，感谢您的支持！\n"
							+ "如需了解还款详情，请";
					content += "<a href='"+authUrl+"'>点击这里</a>";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return ;
				}
				
				String total_amt = null;
				String apply_repay_date = null;
				Map<String, Object> earlyMap = queryEarlyRepayApply(map);
				log.info("查询提前还款信息"+earlyMap);
				if("0".equals(earlyMap.get("errcode"))){
					List<Object> returnList = (List<Object>)earlyMap.get("returnList");
					for(int i = 0; i < returnList.size(); i++){
						Map<String, Object> listMap = (Map<String, Object>)returnList.get(i);
						String status = (String)listMap.get("status");
						log.info("状态:"+status);
						if(!"4".equals(status)&&!"5".equals(status)){
							apply_repay_date = ""+listMap.get("apply_repay_date");
							String dateStr = Util.formatDate(System.currentTimeMillis());
							String nowDate = dateStr.substring(0, 8);
							if(Integer.parseInt(apply_repay_date)>Integer.parseInt(nowDate)){
								total_amt = ""+listMap.get("total_amt");
								break;
							}
						}
					}
				}
				log.info("日期:"+apply_repay_date);
				log.info("金额："+total_amt);
				String repayment = (String)retMap.get("repayment");
				Map<String, Object> rpMap = JSONObject.parseObject(repayment);
				String repayment_term = (String)rpMap.get("repayment_term");
				if(StringUtils.isEmpty(repayment_term)){
					repayment_term = "0";
				}
				String norepayment = (String)retMap.get("norepayment");
				Map<String, Object> repayMap = JSONObject.parseObject(norepayment);
				String no_repayment_term = (String)repayMap.get("no_repayment_term");
				if(StringUtils.isEmpty(no_repayment_term)){
					no_repayment_term = "0";
				}
				content = "您好，您已经还款"+repayment_term+"期，未还款"+no_repayment_term+"期\n";
				content += "最近一期待还款:\n";
				
				if(!StringUtils.isEmpty(overdue)){
					if(Integer.parseInt(overdue) > 0){
						content += "还款金额："+overdue_amt+"元\n"
								+ "逾期天数："+overdue+"天\n"
								+"请尽快结清逾期欠款，感谢您的配合。\n";
						content += "<a href='"+authUrl+"'>点击查看详细信息>></a>";
						wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
						return ;
					}
				}
				
				if(Integer.parseInt(no_repayment_term)<=0){
					content += "暂没有还款信息";
					content += "<a href='"+authUrl+"'>点击查看详细信息</a>";
				}else{
					
					if(Integer.parseInt(no_repayment_term)==1)
					{
						Map<String, Object> planObject=(Map<String, Object>)repayMap.get("repayment_plan");
						Map<String, Object> record=(Map<String, Object>)planObject.get("record");
						if(StringUtils.isEmpty(total_amt)){
							content += "还款金额:"+record.get("money")+"元\n";
							String dt = record.get("date")+"";
							dt = dt.replaceAll("-", "");
							content += "扣款日期:"+dt.substring(4, 6)+"月"+dt.substring(6, 8)+"日\n";
						}else{
							content += "还款金额:"+total_amt+"元\n";
							String dt = record.get("date")+"";
							dt = dt.replaceAll("-", "");
							content += "扣款日期:"+apply_repay_date.substring(4, 6)+"月"+apply_repay_date.substring(6, 8)+"日\n";
						}
					}
					else {
						List<Object> planList = (List<Object>)repayMap.get("repayment_plan");
						Map<String, Object> planMap = (Map<String, Object>)planList.get(0);
						if(StringUtils.isEmpty(total_amt)){
							content += "还款金额:"+planMap.get("money")+"元\n";
							String dt = planMap.get("date")+"";
							dt = dt.replaceAll("-", "");
							content += "扣款日期:"+dt.substring(4, 6)+"月"+dt.substring(6, 8)+"日\n";
						}else{
							content += "还款金额:"+total_amt+"元\n";
							String dt = planMap.get("date")+"";
							dt = dt.replaceAll("-", "");
							content += "扣款日期:"+apply_repay_date.substring(4, 6)+"月"+apply_repay_date.substring(6, 8)+"日\n";
						}
					}	
				}
				content += "请于扣款日期前一天将足够金额存至还款银行卡中，感谢您的配合。建议您在扣款日后一个工作日再查询还款信息，感谢您的配合.\n";
				content += "<a href='"+authUrl+"'>点击查看详细信息>></a>";
				wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
			}else if("9998".equals(retMap.get("errcode"))){
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
			log.error("查询提前还款异常", ex);
			String content = "查询失败，请联系客服。";
			wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
		}
	}
	
	public Map<String, Object> queryEarlyRepayApply(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		Map<String, Object> returnMap=new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "获取微信账号失败");
			return returnMap;
		}
		Object obj = com.gwm.common.service.Service.queryEarlyRepayApply(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("提前还款申请记录查询成功");
			return msgMap;
		}
		else{
			log.info("提前还款申请记录查询失败");
			return msgMap;
		}
	}
	
	public Map<String, Object> queryRepayRecord(Map<String, String> map){
		log.info("请求参数："+map);
		String openId = map.get("openId");//微信openid
		Map<String, Object> returnMap=new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "获取微信账号失败");
			return returnMap;
		}
		Object obj = com.gwm.common.service.Service.queryRepayRecord(map);
		String retJson=(String)obj;//返回的map信息
		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		if("0".equals(msgMap.get("errcode"))){
			log.info("还款记录查询成功");
			return msgMap;
		}
		else{
			log.info("还款记录查询失败");
			return msgMap;
		}
	}
	
	
}
