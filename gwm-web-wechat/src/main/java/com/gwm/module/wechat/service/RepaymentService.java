package com.gwm.module.wechat.service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.wx.engine.Util;
import com.wx.engine.WxProcess;

@Service
public class RepaymentService {
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	ContractService contractService = null;
	
	@Autowired
	MyLoanService myLoanService = null;
	
	@Autowired
	PaymentHistoryService paymentHistoryService = null;
	
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	
	Logger log = LoggerFactory.getLogger(RepaymentService.class);
	
	public void process(Map<String, String> map){
		try{
			log.info("map:"+map);
			map.put("openId", map.get("FromUserName"));
			Map<String, Object> retMap = paymentHistoryService.queryRepayRecord(map);
			log.info("retMap:"+retMap);
			if("0".equals(retMap.get("errcode"))){
				
				
				String EventKey = map.get("EventKey");
				Map<Object, Object> menuMap = redis.getMap("clickmenumap");
				String url = menuMap.get(EventKey+"_url")+"";
				String authUrl = WxProcess.getBaseAuthUrl(url);
				String content = "";
				
				Map<String, String> earlyMap = contractService.getCurrentContract(map);
				if("0".equals(earlyMap.get("errcode"))||"9993".equals(earlyMap.get("errcode"))||"9994".equals(earlyMap.get("errcode"))){
					String contracts = earlyMap.get("contracts");
					Map<String, Object> contractsMap = JSONObject.parseObject(contracts);
					Map<String, Object> contractMap = (Map<String, Object>)contractsMap.get("contract");
					String apply_date = (String)contractMap.get("apply_date");
					String apply_repay_date = ""+contractMap.get("apply_repay_date");
					String total_amt = ""+contractMap.get("total_amt");
					content = "您好，您已于"
							+apply_date.substring(0, 4)+"年"
							+apply_date.substring(4, 6)+"月"
							+apply_date.substring(6, 8)+"日申请于"
							+apply_repay_date.substring(0, 4)+"年"
							+apply_repay_date.substring(4, 6)+"月"
							+apply_repay_date.substring(6, 8)+"日提前还款，提前还款金额"
							+total_amt+"元。若需变更提前还款日请致电客服热线4006527606。";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return;
				}
				
				String repayment = (String)retMap.get("repayment");
				Map<String, Object> rpMap = JSONObject.parseObject(repayment);
				String repayment_term = (String)rpMap.get("repayment_term");
				if(StringUtils.isEmpty(repayment_term)){
					repayment_term = "0";
				}
				String repayment_amt = null;
				if(Integer.parseInt(repayment_term)<=0){
					repayment_amt = "0.00";
				}else{
					repayment_amt = (String)rpMap.get("repayment_amt");
				}
				content += "您好，您已还款"+repayment_term+"期，还款共计"+repayment_amt+"元,\n";
				Map<String, Object> bindMap = myLoanService.getBindingContract(map);
				if(!"0".equals(bindMap.get("errcode"))){
					log.error("errMsg:"+bindMap);
					content = "未查到合同信息！请联系客服";
					wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
					return ;
				}
				Map<String, Object> contractMap = (Map<String, Object>)bindMap.get("contract");
				String contract_status = (String)contractMap.get("contract_status");
				String contract_nbr = (String)contractMap.get("contract_nbr");
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
				String overdue = (String)contractMap.get("overdue");
				String overdue_amt=(String)contractMap.get("overdue_amt");
				if(StringUtils.isEmpty(overdue)){
					overdue = "0";
				}
				if(Integer.parseInt(repayment_term)<6){
					log.info("还款少于6个月");
					if(Integer.parseInt(overdue) <= 0){
						log.info("没有逾期记录");
						content += "当前您不可以申请提前还款，正常还款6个月以后才可申请，您可以";
						content += "<a href='"+authUrl+"'>点击这里</a>";
						content += "进行提前还款试算。";
					}else{
						log.info("有逾期记录");
						content += "当前逾期欠款"+overdue_amt+"元\n";
						content += "当前您不可以申请提前还款，请您结清逾期欠款，且还款6个月以后才可申请,您可以";
						content += "<a href='"+authUrl+"'>点击这里</a>";
						content += "进行提前还款试算。";
					}
				}else{
					log.info("还款满6个月");
					if(Integer.parseInt(overdue) <= 0){
						log.info("没有逾期记录");
						content += "若需进行提前还款计算或申请，请";
						content += "<a href='"+authUrl+"'>点击这里进行操作</a>";
					}else{
						log.info("有逾期记录");
						content += "当前逾期欠款"+overdue_amt+"元。请您结清逾期欠款后再申请提前还款，您可以";
						content += "<a href='"+authUrl+"'>点击这里</a>";
						content += "进行提前还款试算。";
					}
				}
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
			log.error("提前还款异常", ex);
			String content = "查询失败，请联系客服。";
			wxMsgDownsideService.textMessageIn(content, map.get("FromUserName"));
		}
	}

	/**
	 * 下载凭证
	 * @param param
	 * @return
	 */
	public Map<String, String> uploadCertificate(Map<String, String> param){
		String openid = param.get("openid");
		String serverid = param.get("serverid");
		String contract_nbr = param.get("contract_nbr");
		log.info("openid:"+openid);
		log.info("serverid:"+serverid);
		log.info("contract_nbr:"+contract_nbr);
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("下载凭证时openid为空");
			return Util.returnMap("9999", "验证用户信息出错");
		}
		if(StringUtils.isEmpty(serverid)||"undefined".equals(serverid)){
			log.info("下载凭证图片id为空");
			return Util.returnMap("9999", "上传图片失败");
		}
		if(StringUtils.isEmpty(contract_nbr)||"undefined".equals(contract_nbr)){
			log.info("合同号为空");
			return Util.returnMap("9999", "上传图片失败，请重新上传图片");
		}
		String osName = System.getProperties().getProperty("os.name");
		RedisDao redis = SpringUtil.getBean(RedisDao.class);
		String filePath = "";
		if (osName.indexOf("Linux") >= 0) {
			filePath = redis.getString("imgpathlinux");
			log.info("当前系统为Linux，获取文件路径:imgpathlinux" + filePath);
		} else {
			filePath = redis.getString("imgpathwin");
			log.info("当前系统为Windows，获取文件路径:imgpathwin" + filePath);
		}
		if (StringUtils.isEmpty(filePath)) {
			log.info("下载文件路径为空,请检查gw_wx_sys_param表中是否配置了imgpathlinux和imgpathwin参数");
			return Util.returnMap("9999", "获取文件路径失败");
		}
		log.info("文件名称为UUID.jpg");
		String fileName = UUID.randomUUID()+".jpg";
		Map<String, String> retMap = WxProcess.downloadFile(serverid, filePath, fileName);
		if(!"0".equals(retMap.get("errcode"))){
			return Util.returnMap("9999", "图片上传失败");
		}
		param.put("path", filePath+fileName);
		Object obj = com.gwm.common.service.Service.updateVoucherPath(param);
		String retStr = (String)obj;
		Map<String, Object> statusMap = JSONObject.parseObject(retStr);
		log.info("service服务返回："+statusMap);
		if("0".equals(statusMap.get("errcode")+"")){
			log.info("上传图片成功");
			return Util.returnMap("0", "上传成功");
		}else{
			return Util.returnMap("9999", "上传图片失败,请重新上传");
		}
	}
	
	/**
	 * 试算提前还款信息
	 * @param param
	 * @return
	 */
	public Map<String, String> getEarlyRepayInfo(Map<String, String> param){
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
			Object obj = com.gwm.common.service.Service.getEarlyRepayinfo();
			String retJson = obj+"";
			Map<String, Object> retMap = JSONObject.parseObject(retJson);
			log.info("service返回："+retMap);
			return Util.mapObjectToString(retMap);
		}catch(Exception ex){
			log.error("获取提前还款信息失败");
			return Util.returnMap("9999", "查询提前还款信息失败");
		}
	}
	
	public Map<String,Object> wxearlyrepaygetbank(Map<String,String> param){
		String openid = param.get("openid");
		Map<String, Object> retMap=new HashMap<String,Object>();
		if(StringUtils.isEmpty(openid) || "undefined".equals(openid)){
			log.info("微信识别号openid为空");
			retMap.put("errcode", "9999");
			retMap.put("errmsg", "获取信息失败，请尝试重新打开页面或联系客服");
			return retMap;
		}
		Object obj = com.gwm.common.service.Service.wxearlyrepaygetbank();
		log.info("============"+String.valueOf(obj));
//		Object obj = com.gwm.common.service.Service.earlyrepaymentapplycheck();
		String retJson = (String)obj;
		retMap= JSONObject.parseObject(retJson);
		return retMap;
	}
	
	public Map<String,String> wxearlyrepaycheck(Map<String,String> param){
		String openid = param.get("openid");
		String total = param.get("total");
		String contract_nbr = param.get("contract_nbr");
		String application_date = param.get("application_date");
		String repay_type = param.get("repay_type");
		log.info("微信识别号openid:"+openid);
		log.info("还款金额:"+total);
		log.info("合同号:"+contract_nbr);
		log.info("还款日期:"+application_date);
		log.info("还款方式:"+repay_type);
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("微信识别号openid为空");
			return Util.returnMap("9999", "获取用户信息失败，请刷新页面");
		}
		if(StringUtils.isEmpty(total)||"undefined".equals(total)){
			log.info("还款金额为空");
			return Util.returnMap("9999", "还款金额为空，请重新试算");
		}
		if(StringUtils.isEmpty(contract_nbr)||"undefined".equals(contract_nbr)){
			log.info("合同号为空");
			return Util.returnMap("9999", "合同信息错误，请刷新页面");
		}
		if(StringUtils.isEmpty(application_date)||"undefined".equals(application_date)){
			log.info("还款日期为空");
			return Util.returnMap("9999", "申请日期错误，请重新选择申请日期");
		}
		if(StringUtils.isEmpty(repay_type)||"undefined".equals(repay_type)){
			log.info("还款方式为空");
			return Util.returnMap("9999", "请选择提前还款方式");
		}
		Object obj = com.gwm.common.service.Service.wxearlyrepaycheck();
//		Object obj = com.gwm.common.service.Service.earlyrepaymentapplycheck();
		String retJson = (String)obj;
		Map<String, Object> retMap = JSONObject.parseObject(retJson);
		Map<String, String> errMap = new HashMap<String, String>();
		return Util.returnMap(retMap.get("errcode")+"", retMap.get("errmsg")+"", errMap);
	}
	
	public Map<String, String> repaymentApply(Map<String, String> param){
		String openid = param.get("openid");
		String total = param.get("total");
		String contract_nbr = param.get("contract_nbr");
		String application_date = param.get("application_date");
		String repay_type = param.get("repay_type");
		log.info("微信识别号openid:"+openid);
		log.info("还款金额:"+total);
		log.info("合同号:"+contract_nbr);
		log.info("还款日期:"+application_date);
		log.info("还款方式:"+repay_type);
		if(StringUtils.isEmpty(openid)||"undefined".equals(openid)){
			log.info("微信识别号openid为空");
			return Util.returnMap("9999", "获取用户信息失败，请刷新页面");
		}
		if(StringUtils.isEmpty(total)||"undefined".equals(total)){
			log.info("还款金额为空");
			return Util.returnMap("9999", "还款金额为空，请重新试算");
		}
		if(StringUtils.isEmpty(contract_nbr)||"undefined".equals(contract_nbr)){
			log.info("合同号为空");
			return Util.returnMap("9999", "合同信息错误，请刷新页面");
		}
		if(StringUtils.isEmpty(application_date)||"undefined".equals(application_date)){
			log.info("还款日期为空");
			return Util.returnMap("9999", "申请日期错误，请重新选择申请日期");
		}
		if(StringUtils.isEmpty(repay_type)||"undefined".equals(repay_type)){
			log.info("还款方式为空");
			return Util.returnMap("9999", "请选择提前还款方式");
		}
		Object obj = com.gwm.common.service.Service.earlyRepayApply();
		String retJson = (String)obj;
		Map<String, Object> retMap = JSONObject.parseObject(retJson);
		Map<String, String> errMap = new HashMap<String, String>();
		log.info("service服务返回："+retMap);
		if("0".equals(retMap.get("errcode")+"")){
			log.info("开始发送模板消息通知");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("template", retMap.get("template"));
			tempMap.put("title", retMap.get("title"));
			tempMap.put("msg_type", "2");
			tempMap.put("openid", openid);
			tempMap.put("template_id", retMap.get("template_id"));
			tempMap.put("color", retMap.get("color"));
			tempMap.put("url", getAuthUrl((String)retMap.get("url")));
			String first = "尊敬的"+retMap.get("name")+"先生/女士：\n我们已经受理您的提前结清申请。\n";
			tempMap.put("first", first);
			
			String appDt = application_date.replaceAll("-", "");
			String dt = Util.getSpecifiedDayBefore(appDt);
			if(dt.length()==8){
				String year=dt.substring(0,4);
				String month=dt.substring(4,6);
				String date=dt.substring(6,8);
				dt=year+"-"+month+"-"+date;
			}
			errMap.put("predt", dt);
			if("1".equals(repay_type)){
				log.info("预留银行卡还款");
				String keyword1 = "还款账户提前还款申请";
				tempMap.put("keyword1", keyword1);
				String remark = "提前结清金额："+retMap.get("total")+"元\n"
						+"提前结清日："+retMap.get("application_date")+"\n"
						+"\n☆温馨提示☆ 客服人员将于1个工作日内与您电话核实，请您注意接听，若电话无法接通，您本次申请将自动取消，感谢您的支持与配合！客服热线400 6527 606";
				tempMap.put("remark", remark);
			}else if("2".equals(repay_type)){
				log.info("对公账户还款");
				String keyword1 = "对公账户提前还款申请";
				tempMap.put("keyword1", keyword1);
				String remark = "提前结清金额："+retMap.get("total")+"元\n"
						+"提前结清日："+retMap.get("application_date")+"\n"
						+"";
				remark += "\n☆温馨提示☆ 请于"+dt+"前往柜台汇款或支付宝转账至对公账户并上传还款凭证。\n"
						+"对公账户信息：天津长城滨银汽车金融有限公司，账号：2739 8199 4006 开户行：中国银行股份有限公司天津滨海分行\n\n"
						+"点击详情查看如何上传还款凭证。";
				tempMap.put("remark", remark);
			}
			String keyword2 = Util.formatDate(System.currentTimeMillis());
			tempMap.put("keyword2", keyword2.substring(0, 4)+"-"+keyword2.substring(4, 6)+"-"+keyword2.substring(6, 8));
			log.info(tempMap.get("template")+"");
			log.info(Util.mapObjectToString(tempMap)+"");
			Object sendMsgObj = wxMsgDownsideService.templateMessageIn((String)tempMap.get("template"), Util.mapObjectToString(tempMap));
			log.info("发送结果:"+sendMsgObj);
		}
		return Util.returnMap(retMap.get("errcode")+"", retMap.get("errmsg")+"", errMap);
	}
	
	private String getAuthUrl(String url){
		String appid = redis.getString("appid");
		String urlEncode = null;
		try{
			urlEncode = URLEncoder.encode(url, "UTF-8");
		}catch(Exception ex){
			urlEncode = url;
		}
		String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid="+appid+"&"
				+ "redirect_uri="+urlEncode+"&"
				+ "response_type=code&"
				+ "scope=snsapi_base&"
				+ "state=true#wechat_redirect";
		return authUrl;
	}
	
	public static void main(String args[]){
		String preDay = Util.getSpecifiedDayBefore("20170301");
		System.out.println(preDay);
	}
}
