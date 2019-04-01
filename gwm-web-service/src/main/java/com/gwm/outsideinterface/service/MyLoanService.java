package com.gwm.outsideinterface.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwm.common.DecodeXmlImpl;
import com.gwm.common.RedisDao;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;

@Service
public class MyLoanService {
	
	Logger log = LoggerFactory.getLogger(MyLoanService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	RedisDao redis = null;
	
	
	/**
	 * 获取绑定合同信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBindingContract(Map<String, String> map)throws Exception{
		Map<String,Object> returnMap = new HashMap<String, Object>();
		log.info("开始提绑定判断");
		String openId = map.get("openId");//openId
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			returnMap.put("errcode", "9999");
			returnMap.put("errmsg", "获取用户信息失败");
			returnMap.put("bindingJudg", "false");
			returnMap.put("promptinfo",getPromptInfo("1")==null?"":getPromptInfo("1"));
			return returnMap;
		}
		try {
			//获取openid下的个人信息
			Gw_user_loan_rel queryEntity = null;
			queryEntity = dao.fetch(Gw_user_loan_rel.class, 
					Cnd.where(Gw_user_loan_relMeta.openid.eq(openId)).and(Gw_user_loan_relMeta.status.eq("2")));
			if(queryEntity==null){
				log.info("没有获取到该openid的个人信息，请确认是否进行过绑定");
				returnMap.put("errcode", "9998");
				returnMap.put("errmsg", "未找到绑定信息");
				returnMap.put("bindingJudg", "false");
				returnMap.put("promptinfo",getPromptInfo("1")==null?"":getPromptInfo("1"));
				return returnMap;
			}
			else{
				if(queryEntity.getPhone()==null || StringUtils.isEmpty(queryEntity.getPhone())){
					log.error("绑定表中预留手机号为空");
					returnMap.put("errcode", "9999");
					returnMap.put("errmsg", "查询预留手机号失败");
					returnMap.put("bindingJudg", "false");
					returnMap.put("promptinfo",getPromptInfo("1")==null?"":getPromptInfo("1"));
					return returnMap;
				}
				if(queryEntity.getId_num()==null || StringUtils.isEmpty(queryEntity.getId_num())){
					log.error("绑定表中身份证号为空");
					returnMap.put("errcode", "9999");
					returnMap.put("errmsg", "查询身份信息失败");
					returnMap.put("bindingJudg", "false");
					returnMap.put("promptinfo",getPromptInfo("1")==null?"":getPromptInfo("1"));
					return returnMap;
				}
				if(queryEntity.getContract_code()==null || StringUtils.isEmpty(queryEntity.getContract_code())){
					log.error("绑定表中合同号为空");
					returnMap.put("errcode", "9999");
					returnMap.put("errmsg", "您当前未绑定任何合同");
					returnMap.put("bindingJudg", "false");
					returnMap.put("promptinfo",getPromptInfo("1")==null?"":getPromptInfo("1"));
					return returnMap;
				}
				String phone=queryEntity.getPhone();
				String id_num=queryEntity.getId_num();
				String contract_code=queryEntity.getContract_code();
				
				//根据id_num，phone发送报文请求合同信息
				String url=redis.getString("CONTRACT_URL");
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
				stringBuffer.append("<root>");
				stringBuffer.append("<application_phone>"+phone+"</application_phone>");
				stringBuffer.append("<application_id_card>"+id_num+"</application_id_card>");
				stringBuffer.append("</root>");
				// 定义客户端对象
				String requestXML=stringBuffer.toString();
				log.info("requestXML:"+requestXML);
				String repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getContract");
				log.info("返回报文:"+repxml); 
				//解析返回报文等到map
				returnMap =  DecodeXmlImpl.decode(repxml);
				//如果返回的报文数据为空
				if(returnMap.get("flag") != null){
					log.error("errcode:"+returnMap.get("flag"));
					log.error("errmsg:"+returnMap.get("reason"));
					returnMap.put("errcode", "9999");
					returnMap.put("errmsg", returnMap.get("reason")+"请联系客服");
					return returnMap;
				}
				//所有对应的合同信息
				String contracts = returnMap.get("contracts")+"";
				log.info("contracts:"+contracts);
				Map<String, Object> contractMap = null;
				if(contracts.startsWith("{")){
					log.info("只有一条合同信息");
					contractMap = JSONObject.parseObject(contracts);
					String dataStr = contractMap.get("contract")+"";
					Map<String, Object> dataMap = JSONObject.parseObject(dataStr);
					if(!contract_code.equals(dataMap.get("contract_nbr")+"")){
						log.info("当前绑定合同号与查询结果的合同号不符["+contract_code+dataMap.get("contract_nbr")+"]");
						returnMap.put("errcode", "9999");
						returnMap.put("errmsg", "未查到当前绑定合同信息");
						return returnMap;
					}
					String contract_status=dataMap.get("account_nbr").toString();
					String binding_state="2";//绑定未结清
					if(contract_status.equals("N")){
						binding_state="3";//绑定已结清
					}
					dataMap.put("promptinfo",getPromptInfo(binding_state)==null?"":getPromptInfo(binding_state));
					String bankNum=dataMap.get("account_nbr").toString();
					String bankName=BankService.getname(bankNum);
					String phone_nbr = (String)dataMap.get("phone_nbr");
					String business_partner_name = (String)dataMap.get("business_partner_name");
					business_partner_name = Util.addMaskStr(business_partner_name, 1, 0);
					dataMap.put("business_partner_name", business_partner_name);
					phone_nbr = Util.addMaskStr(phone_nbr, 3, 2);
					dataMap.put("phone_nbr", phone_nbr);
					//银行名称
					dataMap.put("bankName", bankName);
					//拥有的合同数量
					dataMap.put("contract_amount","1");
					contractMap = new HashMap<String, Object>();
					contractMap.put("contract", dataMap);
				}else{
					log.info("有多条合同信息！");
					List<Object> contractsList = JSONArray.parseArray(contracts);
					int i = -1;
					Map<String, Object> dataMap = null;
					//拥有的合同数量
					int contract_amount=contractsList.size();
					for(i = 0; i < contractsList.size(); i++){
						String contractStr = contractsList.get(i)+"";
						dataMap = JSONObject.parseObject(contractStr);
						log.info(contract_code+"=="+dataMap.get("contract_nbr"));
						if(contract_code.equals(dataMap.get("contract_nbr")+"")){
							break;
						}
					}
					if(i == contractsList.size()){
						log.error("未查到当前绑定合同信息");
						returnMap.put("errcode", "9999");
						returnMap.put("errmsg", "未查到当前绑定合同信息");
						return returnMap;
					}
					if(dataMap.get("account_nbr")==null || dataMap.get("account_nbr").toString().length()<6){
						log.error("未查到当合同的还款账户，请联系客服");
						returnMap.put("errcode", "9999");
						returnMap.put("errmsg", "未查到当合同的还款账户，请联系客服");
						return returnMap;
					}
					String contract_status=dataMap.get("account_nbr").toString();
					String binding_state="2";//绑定未结清
					if(contract_status.equals("N")){
						binding_state="3";//绑定已结清
					}
					dataMap.put("promptinfo",getPromptInfo(binding_state)==null?"":getPromptInfo(binding_state));
					String bankNum=dataMap.get("account_nbr").toString();
					String bankName=BankService.getname(bankNum);
					String phone_nbr = (String)dataMap.get("phone_nbr");
					String business_partner_name = (String)dataMap.get("business_partner_name");
					business_partner_name = Util.addMaskStr(business_partner_name, 1, 0);
					dataMap.put("business_partner_name", business_partner_name);
					phone_nbr = Util.addMaskStr(phone_nbr, 3, 2);
					dataMap.put("phone_nbr", phone_nbr);
					//银行名称
					dataMap.put("bankName", bankName);
					//合同数量
					dataMap.put("contract_amount",contract_amount);
					
					contractMap = new HashMap<String, Object>();
					contractMap.put("contract", dataMap);
				}
				log.info("查到对应合同:"+contractMap);
				contractMap.put("errcode", "0");
				contractMap.put("errmsg", "获取当前绑定合同信息成功");
				return contractMap;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	
	/**
	 * 根据绑定状态获取对应的提示信息
	 * @param String 绑定状态
	 * @return
	 */
	public List<Map<String, Object>> getPromptInfo(String binding_state){
		//1 未绑定 2 绑定未结清 3绑定已结清
		String querySql="select * from gw_prompt_info where binding_state='$binding_state$' and msg_state='$msg_state$' order by msg_type";
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		Map<String, String> sqlMap=new HashMap<String, String>();
		Map<String, Object> retMap;
		sqlMap.put("binding_state", binding_state);
		sqlMap.put("msg_state", "1");//1启用 0未启用
		List<Map<String, Object>> queryList;
		try {
			log.info("执行sql: "+SqlManager.getSql(querySql, sqlMap));
			queryList = jdbc.queryForList(SqlManager.getSql(querySql, sqlMap));
			if(queryList==null || queryList.size()<=0){
				log.error("该状态下数据库中没有对应的提示信息");
				return null;
			}
			else{
				for (int i = 0; i < queryList.size(); i++) {
					retMap=new HashMap<String, Object>();
					retMap.put("msg_type", queryList.get(i).get("msg_type")==null?"":queryList.get(i).get("msg_type").toString());
					retMap.put("msg_content", queryList.get(i).get("msg_content")==null?"":queryList.get(i).get("msg_content").toString());
					retMap.put("link_address", queryList.get(i).get("link_address")==null?"":queryList.get(i).get("link_address").toString());
					list.add(retMap);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return list;
	}
	
}