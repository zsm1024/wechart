package com.gwm.module.manager.service;

import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gwm.common.DecodeXmlImpl;
//import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.dao.condition.Order;
import com.gwm.db.dao.sql.Pager;
import com.gwm.db.entity.Gw_repay_reg;
import com.gwm.db.entity.Gw_repay_reg_hst;
import com.gwm.db.entity.Gw_shop;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.Gw_wx_general_information;
import com.gwm.db.entity.Gw_wx_menu;
import com.gwm.db.entity.meta.Gw_repay_regMeta;
import com.gwm.db.entity.meta.Gw_repay_reg_hstMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.db.entity.meta.Gw_wx_general_informationMeta;
import com.gwm.engine.HttpProcess;
import com.gwm.engine.Util;
import com.gwm.outsideinterface.service.BankService;
import com.gwm.outsideinterface.service.WsdlInterfaceService;

@Service
public class SMRepaymentService{
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	JdbcTemplate jdbc = null;
	
	static Logger log = LoggerFactory.getLogger(SMRepaymentService.class);
	public Map<String,String> earlyrepayinfoForSub(Map<String,String> param){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		try{
			String contract_code=param.get("contract_code");
			String sql="select * ,(case when length(apply_time)=0 then concat(rev_date,'000000')"
					+" when length(rev_time)=1 then concat(rev_date,'00000',rev_time)"
					+" when length(rev_time)=2 then concat(rev_date,'0000',rev_time)"
					+" when length(rev_time)=3 then concat(rev_date,'000',rev_time)"
					+" when length(rev_time)=4 then concat(rev_date,'00',rev_time)"
					+" when length(rev_time)=5 then concat(rev_date,'0',rev_time)"
					+" when length(rev_time)>=6 then concat(rev_date,rev_time) end )as apply_date_time from gw_repay_reg_hst where application_num!='' and contract_code='$contract_code$'";
			Map<String,String> inmap=new HashMap<String,String>();
			inmap.put("contract_code", contract_code);
			sql=SqlManager.getSql(sql, inmap);
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			String rows = JSON.toJSONString(retList);
			log.info("====================rows:"+rows);
			List<Object> rowList = JSONArray.parseArray(rows);
			String pic_url = redis.getString("PIC_URL");
			for(int i=0;i<rowList.size();i++){

				Map<String, Object> map = (Map<String, Object>)rowList.get(i);
//				map.put("id", i+"");
//				map.put("state", "closed");
				String repay_voucher = (String)map.get("repay_voucher");
				if(!StringUtils.isEmpty(repay_voucher)){
					String fStr = "";
					if(repay_voucher.startsWith("/")){
						String[] p = repay_voucher.split("/");
						fStr = p[p.length-1];
					}else{
						String[] p = repay_voucher.split("\\\\");
						fStr = p[p.length-1];
					}
					map.put("repay_voucher", pic_url+fStr);
				}
			}
			rows = JSON.toJSONString(rowList);
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("rows", rows);
			return Util.returnMap("0", "查询成功", retMap);
		}catch(Exception e){
			log.error("当前合同提前还款查询出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
	}
	
	public Map<String, String> queryRepaymentInfo(Map<String, String> param){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList=new ArrayList<Map<String,Object>>();
		try{
		String index = param.get("pagenumber").equals("0")?"1":param.get("pagenumber");
		String size = param.get("pagesize");
		String status = param.get("status");
		String applicationer = param.get("applicationer");
		String contract_code = param.get("contract_code");
		String repay_type = param.get("repay_type");
		String apply_repay_date = param.get("apply_repay_date");
		String start_apply_date = param.get("start_apply_date");
		String end_apply_date = param.get("end_apply_date");
		int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);
		Map<String,Object> inputMap=new HashMap<String,Object>();
		inputMap.put("startnum", startnum);
		inputMap.put("size", size);
		
		String sql="select r.* from ("
				+ "select a.contract_code,max(a.apply_date_time) as apply_date_time from"
				+ " (select * from gw_repay_reg where application_num!='' ";
		String countsql=sql;
		if(!Util.isNull(status)){
			inputMap.put("status", status);
			sql+=" and status='$status$'";
			countsql+=" and status='$status$'";
		}
		if(!Util.isNull(applicationer)){
			inputMap.put("applicationer", applicationer);
			sql+=" and applicationer='$applicationer$'";
			countsql+=" and applicationer='$applicationer$'";
		}
		if(!Util.isNull(contract_code)){
			inputMap.put("contract_code", contract_code);
			sql+=" and contract_code='$contract_code$'";
			countsql+=" and contract_code='$contract_code$'";
		}
		if(!Util.isNull(repay_type)){
			inputMap.put("repay_type", repay_type);
			sql+=" and repay_type='$repay_type$'";
			countsql+=" and repay_type='$repay_type$'";
		}
		if(!Util.isNull(apply_repay_date)){
			apply_repay_date = apply_repay_date.replaceAll("-", "");
			inputMap.put("apply_repay_date", apply_repay_date);
			sql+=" and apply_repay_date='$apply_repay_date$'";
			countsql+=" and apply_repay_date='$apply_repay_date$'";
		}
		if(!Util.isNull(start_apply_date)){
			start_apply_date = start_apply_date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			inputMap.put("start_apply_date", start_apply_date);
			sql+=" and apply_date>='$start_apply_date$'";
			countsql+=" and apply_date='$start_apply_date$'";
		}
		if(!Util.isNull(end_apply_date)){
			end_apply_date = end_apply_date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			inputMap.put("end_apply_date", end_apply_date);
			sql+=" and apply_date<='$end_apply_date$'";
			countsql+=" and apply_date<='$end_apply_date$'";
		}
		sql+=" order by apply_date_time desc ) a group by a.contract_code";
		countsql+=" order by apply_date_time desc ) a group by a.contract_code";
		sql+=" ) b left join gw_repay_reg r on "
				+ "r.contract_code=b.contract_code and r.apply_date_time=b.apply_date_time  order by r.apply_date_time desc  limit $startnum$,$size$ ";
		countsql+=" ) b left join gw_repay_reg r on "
				+ "r.contract_code=b.contract_code and r.apply_date_time=b.apply_date_time";
		//数字   m,n 从第m+1条开始查询 查到m+n+1条
		sql=SqlManager.getSql(sql, inputMap);
		countsql=SqlManager.getSql(countsql, inputMap);
		log.info("执行sql: "+sql);
		retList=jdbc.queryForList(sql);
		log.info("执行sql: "+countsql);
		allList=jdbc.queryForList(countsql);
		

		String rows = JSON.toJSONString(retList);
		log.info("====================rows:"+rows);
		List<Object> rowList = JSONArray.parseArray(rows);
		
		String pic_url = redis.getString("PIC_URL");
		
		for(int i = 0; i < rowList.size(); i++){
			Map<String, Object> map = (Map<String, Object>)rowList.get(i);
//			map.put("id", i+"");
//			map.put("state", "closed");
			String repay_voucher = (String)map.get("repay_voucher");
			if(!StringUtils.isEmpty(repay_voucher)){
				String fStr = "";
				if(repay_voucher.startsWith("/")){
					String[] p = repay_voucher.split("/");
					fStr = p[p.length-1];
				}else{
					String[] p = repay_voucher.split("\\\\");
					fStr = p[p.length-1];
				}
				map.put("repay_voucher", pic_url+fStr);
			}
//			String application_num = (String)map.get("application_num");
//			List<Gw_repay_reg_hst> hstList = dao.list(Gw_repay_reg_hst.class, Cnd
//					.where(Gw_repay_reg_hstMeta.application_num.eq(application_num))
//					.orderby(Gw_repay_reg_hstMeta.rev_date.desc()));
//			String jsonStr = JSON.toJSONString(hstList);
//			List<Object> jsonList = JSONArray.parseArray(jsonStr);
//			for(int j = 0; j < jsonList.size(); j++){
//				Map<String, Object> hstMap = (Map<String, Object>)jsonList.get(j);
//				hstMap.put("id", i+"-"+j);
//				repay_voucher = (String)hstMap.get("repay_voucher");
//				if(!StringUtils.isEmpty(repay_voucher)){
//					String fStr = "";
//					if(repay_voucher.startsWith("/")){
//						String[] p = repay_voucher.split("/");
//						fStr = p[p.length-1];
//					}else{
//						String[] p = repay_voucher.split("\\\\");
//						fStr = p[p.length-1];
//					}
//					hstMap.put("repay_voucher", pic_url+fStr);
//				}
//			}
//			map.put("children", jsonList);
		}
		rows = JSON.toJSONString(rowList);
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("rows", rows);
		retMap.put("total", allList.size()+"");
		return Util.returnMap("0", "查询成功", retMap);
		}catch(Exception e){
			log.error("查询提前还款出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
	}
	
	@Transactional
	public Map<String, String> dealRepayment(Map<String, String> param) throws Exception {
		try{
			String dostatus = param.get("dostatus");
			String application_num = param.get("application_num");
			if(dostatus.equals("4")){//修改状态为已关闭时，调用核心若返回已结清，则不允许关闭，若未结清，则允许继续操作
				String loanStatus=getLoanInfo(application_num);
				if(loanStatus.equals("N")){
					log.info("该合同已结清不允许执行关闭操作");
					return Util.returnMap("9999", "该合同已结清，不允许执行关闭操作");
				}
			}
			if(dostatus.equals("5")){//修改状态为已结清时，调用核心若返回已结清，则允许结清，若未结清，则不允许继续操作
				String loanStatus=getLoanInfo(application_num);
				if(!loanStatus.equals("N")){
					log.info("该合同未结清不允许执行结清操作");
					return Util.returnMap("9999", "该合同未结清不允许执行结清操作");
				}
			}
			log.info("处理状态:"+dostatus);
			log.info("申请单号:"+application_num);
			if(StringUtils.isEmpty(dostatus)||"undefined".equals(dostatus)||"null".equals(dostatus)){
				log.info("处理状态为空");
				return Util.returnMap("9999", "处理状态为空");
			}
			if(StringUtils.isEmpty(application_num)||"undefined".equals(application_num)||"null".equals(application_num)){
				log.info("申请单号为空");
				return Util.returnMap("9999", "申请单号为空");
			}
			Gw_repay_reg repayEntity = null;
			repayEntity = dao.fetch(Gw_repay_reg.class, Cnd
					.where(Gw_repay_regMeta.application_num.eq(application_num)));
			if(repayEntity == null){
				log.info("提前还款申请记录为空");
				return Util.returnMap("9999", "未查到记录");
			}
			String status = repayEntity.getStatus();
			log.info("原状态:"+status);
			if("4".equals(status)){
				log.info("该记录已结清，无法进行变更");
				return Util.returnMap("9999", "提前还款申请已结清，无法完成修改");
			}
			if("5".equals(status)){
				log.info("已关闭，无法进行变更");
				return Util.returnMap("9999", "提前还款申请已关闭，无法完成修改");
			}
			if(dostatus.equals(status)){
				log.info("处理状态与原状态相同,直接返回");
				return Util.returnMap("0", "变更成功");
			}
			log.info("开始更新状态"+status+"->"+dostatus);
			repayEntity.setStatus(dostatus);
			dao.update(repayEntity, Cnd
					.where(Gw_repay_regMeta.application_num.eq(application_num)));
			log.info("更新状态完成...");
			log.info("开始登记变更记录...");
			String dateStr = Util.formatDate(System.currentTimeMillis());
			log.info("日期:"+dateStr);
			Gw_repay_reg_hst repayHstEntity = new Gw_repay_reg_hst();
			repayHstEntity.setOpenid(repayEntity.getOpenid());
			repayHstEntity.setApplication_num(application_num);
			repayHstEntity.setApplicationer(repayEntity.getApplicationer());
			repayHstEntity.setApply_repay_date(repayEntity.getApply_repay_date());
			repayHstEntity.setSurplus_amt(repayEntity.getSurplus_amt());
			repayHstEntity.setInterest(repayEntity.getInterest());
			repayHstEntity.setPenalty(repayEntity.getPenalty());
			repayHstEntity.setTotal_amt(repayEntity.getTotal_amt());
			repayHstEntity.setContract_code(repayEntity.getContract_code());
			repayHstEntity.setRepay_type(repayEntity.getRepay_type());
			repayHstEntity.setRepay_voucher(repayEntity.getRepay_voucher());
			repayHstEntity.setStatus(dostatus);
			repayHstEntity.setApply_date(repayEntity.getApply_date());
			repayHstEntity.setApply_time(repayEntity.getApply_time());
			repayHstEntity.setFlag("3");
			repayHstEntity.setOperator("管理员");
			repayHstEntity.setRev_date(dateStr.substring(0, 8));
			repayHstEntity.setRev_time(dateStr.substring(8, 14));
			dao.insert(repayHstEntity);
			//当状态为“关闭”处理成功后发送短信及微信
			if(dostatus.equals("4")){
				try{
					sendMsg(repayEntity);
				}
				catch (Exception e){
					log.error("发送短信出现异常",e);
				}
				sendModelMsg(repayEntity,"16");
			}
			return Util.returnMap("0", "处理成功");
		}catch(Exception ex){
			log.info("处理异常", ex);
			throw ex;
		}
	}
	
//	zxc
	public String getLoanInfo(String application_num){
		String result="";
		Gw_repay_reg regEntity=dao.fetch(Gw_repay_reg.class, Cnd.where(Gw_repay_reg_hstMeta.application_num.eq(application_num)));
		if(regEntity!=null){
			String openid=regEntity.getOpenid();
			Gw_user_loan_rel relEntity=dao.fetch(Gw_user_loan_rel.class, Cnd.where(Gw_user_loan_relMeta.openid.eq(openid)));
			if(relEntity!=null){
				String contractcode=relEntity.getContract_code();//合同号
				String url=redis.getString("OVERDUE_URL");
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
				stringBuffer.append("<root>");
				stringBuffer.append("<contract_nbr>"+contractcode+"</contract_nbr>");
				stringBuffer.append("</root>");
				// 定义客户端对象
				String requestXML=stringBuffer.toString();
				log.info("requestXML:"+requestXML);
				String repxml="";
				try{
					repxml=WsdlInterfaceService.sendXML(url, requestXML, "http://service.gwmfc.com", "getOverDue");
					log.info("返回报文:"+repxml); 
					Map<String,Object> returnMap = new HashMap<String, Object>();
					returnMap =  DecodeXmlImpl.decode(repxml);
					if(returnMap.get("flag") != null){
						log.error("errcode:"+returnMap.get("flag"));
						log.error("errmsg:"+returnMap.get("reason"));
						return result;
					}
					String contract_status=returnMap.get("live_sts")+"";//合同状态
					result=contract_status;
					return result;
				}catch(Exception e){
					log.error("查询核心信息出现异常:",e);
					return result;
				}
			}
		}
		return result;
	}
	
	@Transactional
	public void sendModelMsg(Gw_repay_reg repayEntity,String num){
		if(repayEntity!=null){
			if(num!=""){
				Gw_wx_general_information inEntity=null;
				inEntity=dao.fetch(Gw_wx_general_information.class, Cnd.where(Gw_wx_general_informationMeta.id.eq(num)));
				if(inEntity!=null){
					Map<String,String> map=new HashMap<String,String>();	
					String openid=repayEntity.getOpenid();//openid
					String name=repayEntity.getApplicationer();//用户姓名
					String applyRepayDate=repayEntity.getApply_repay_date()+"";//申请还款日期
					String applyDate=repayEntity.getApply_date();
					String amt=repayEntity.getTotal_amt()+"";//提前还款金额
					String information_name=inEntity.getInformation_name();//标题
					String template=inEntity.getContent();//模板
					String template_id=inEntity.getInformation_id();//模板id
					String information_url=inEntity.getInformation_url()==null?"":inEntity.getInformation_url();//跳转地址
					String color=inEntity.getColor()==null?"":inEntity.getColor();
					map.put("first", ("尊敬的"+name+"：\n"
							+ "您于"+applyDate.substring(0,4)+""
									+ "年"+applyDate.substring(4, 6)+""
											+ "月"+applyDate.substring(6,8)+""
													+ "日申请于"+applyRepayDate.substring(0, 4)+""
															+ "年"+applyRepayDate.substring(4, 6)+""
																	+ "月"+applyRepayDate.substring(6, 8)+""
																			+ "日的提前还款业务扣款失败！\n"));
					
					map.put("keyword1", amt+"元");
					map.put("keyword2", "还款账户余额不足\n");
					map.put("remark","\n温馨提示：若需提前还款，请点击详情进行申请。\n");
					map.put("msg_type", "2");
					map.put("title", information_name);
					map.put("openid", openid);
					map.put("template", template);
					map.put("template_id", template_id);
					if(!information_url.equals("")){
						String appid=redis.getString("appid");
						String redirect_uri="";
						try {
							redirect_uri=URLEncoder.encode(information_url,"utf-8");
						} catch (Exception e) {
							// TODO: handle exception
							log.error(e.getMessage());
						}
						String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="
								   +appid+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=true#wechat_redirect";
						map.put("url", url);
					}
					if(!"".equals(color)){
						map.put("color", color);
					}
					String wx_url=redis.getString("WECHAT_URL");
					String url=wx_url+"/sendtemplatemessage";
					try{
						String result=HttpProcess.post(url, map);
						JSONObject jsonObject = JSONObject.fromObject(result);  
						Map<String,String> resultmap=new HashMap<String,String>();
						Iterator it = jsonObject.keys();  
						while (it.hasNext()) {  
							String key = String.valueOf(it.next());  
					        String value = (String) jsonObject.get(key);  
					        resultmap.put(key, value);  
					    }  
					    if(!"0".equals(resultmap.get("errcode"))){
					    	log.error("模板消息发送失败>>>>>>>errcode:"+resultmap.get("errcode")+",原因模板为:"+resultmap.get("errmsg"));
					    }
					}catch(Exception e){
						log.error("发送模板失败",e);
					}
				}
				
			}
		}
	}
	@Transactional
	public void sendMsg(Gw_repay_reg repayEntity)throws Exception{
		if(repayEntity!=null){
			String openid=repayEntity.getOpenid();//openid
			String name=repayEntity.getApplicationer();//用户姓名
			String applyRepayDate=repayEntity.getApply_repay_date()+"";//申请还款日期
			String applyDate=repayEntity.getApply_date();
			String amt=repayEntity.getTotal_amt()+"";//提前还款金额
			Gw_user_loan_rel relEntity=null;
			//查询当前openid，状态为绑定的绑定信息，获取手机号
			relEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openid)).and(Gw_user_loan_relMeta.status.eq("2")));
			if(relEntity==null){
				log.info("openId为："+openid+"不是绑定用户");
			}else{
				String phone=relEntity.getPhone();//手机号
				String duxin_xml="尊敬的"+name+",您于"+applyDate.substring(0, 4)+"年"+applyDate.substring(4,6)+""
						+ "月"+applyDate.substring(6, 8)+"日申请于"+applyRepayDate.substring(0, 4)+"年"+applyRepayDate.substring(4, 6)+""
								+ "月"+applyRepayDate.substring(6, 8)+"日提前还款（"+amt+"元）业务扣款失败,若需提前还款，请通过微信公众号或者致电客服热线  4006527606进行申请，祝您万事如意！";
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
				stringBuffer.append("<phone>"+phone+"</phone>");
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
					map.put("phone", phone);
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
		}
		else{
			log.info("repayEntity为空");
		}
	}
	
	
	
	
	public static void main(String args[]){
		try{
			String path = "/home/gwm/tomcat/apache-tomcat-7.0.63/webapps/data/213.jpg";
			System.out.println(path.startsWith("/"));
			String p[] = path.split("/");
			System.out.println(p[p.length-1]);
			path = "D:\\data\\123.jpg";
			System.out.println(path.startsWith("/"));
			p = path.split("\\\\");
			System.out.println(p[p.length-1]);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}