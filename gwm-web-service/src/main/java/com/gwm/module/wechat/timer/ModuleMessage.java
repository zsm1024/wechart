package com.gwm.module.wechat.timer;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.engine.HttpProcess;
import com.gwm.engine.Util;
import com.gwm.module.wechat.service.WxCCSInterfaceService;
import com.gwm.module.wechat.utils.SendStatusMQUtil;
/**
 * 模板消息定时
 * */
@Service
public class ModuleMessage {
	private Logger log=LoggerFactory.getLogger(ModuleMessage.class);
	
	
	WxCCSInterfaceService wxCCSInterfaceService=new WxCCSInterfaceService();
	public static void main(String[] args) {
		String a="15465465/313213*13132";
		String [] b=a.split("/");
		System.out.println(b[0]);
		String [] c=a.split("-");
		System.out.println(c[0]);
	}
	
	
	/**
	 * 模板消息定时
	 * @param taskname
	 * @return
	 */

	@Transactional
	public boolean ModuleMessageTimer(String taskname){
		//需要发送那些数据(库)
		try{
			RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			Format format = new SimpleDateFormat("yyyyMMddhhmmss");
			String tnow=format.format(new Date());
			String userMsgSql="select r.phone,r.content,l.openid,r.date,r.wx_msg_id from gw_msg_reg r "
					    + "left join gw_user_loan_rel l on l.phone=r.phone where l.status='2' and r.status='0' and  date<="+tnow+"";
			String staffMsgSql="select	msg.phone,msg.content,staff.openid,msg.date,msg.wx_msg_id from gw_msg_reg msg,gw_internal_staff staff"
					+ " where msg.phone = staff.phone and date<="+tnow+" and msg.`status`='0' and staff.`status`='1'";
			String generalsql="select * from gw_wx_general_information ";
			log.info("userSql="+userMsgSql);//需要将短信转成微信的记录
			log.info("sql="+generalsql);//微信类型模板
			List<Map<String,Object>> userMsglist=jdbc.queryForList(userMsgSql);
			List<Map<String,Object>> staffMsglist=jdbc.queryForList(staffMsgSql);
			List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
			lists.addAll(userMsglist);
			lists.removeAll(staffMsglist);
			lists.addAll(staffMsglist);
			List<Map<String,Object>> listg=jdbc.queryForList(generalsql);
			if(lists.size()>0 && listg.size()>0){
				log.info("需要将短信转成微信的记录的条数为"+lists.size());
				for(int i=0;i<lists.size();i++){
					String wx_msg_id=lists.get(i).get("wx_msg_id")==null?"":lists.get(i).get("wx_msg_id").toString();
					String date=lists.get(i).get("date")==null?"":lists.get(i).get("date").toString();
					String phone=lists.get(i).get("phone")==null?"":lists.get(i).get("phone").toString();
					String contents=lists.get(i).get("content")==null?"":lists.get(i).get("content").toString();
					if(date.equals("")||phone.equals("")||contents.equals("")){
						changeStatus("2",wx_msg_id);
						log.error("当前记录date、phone、contents有为空项"+date+"、"+phone+"、"+contents);
						continue;
					}
					String send_status="1";//默认成功
					String openid=lists.get(i).get("openid")==null?"":lists.get(i).get("openid").toString();
					String content=lists.get(i).get("content")==null?"":lists.get(i).get("content").toString();
					String information_name="";
					String template="";
					String template_id="";
					String color="";
					String information_url="";
					Map<String,String> map=new HashMap<String,String>();	
					Map<String,String> moduleMap=new HashMap<String,String>();
					Map<String,String> statusMap=new HashMap<String,String>(); 			//创建状态map
					statusMap.put("wx_msg_id",wx_msg_id);								//微信id存入map
					//WxCCSInterfaceService wxCCSInterfaceService = new WxCCSInterfaceService();
					String [] init=content.split("type:");
					if(init.length!=2){
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						log.error("该信息参数内容不符规则："+content);
						continue;
					}
					String [] data=init[1].split(";");
					//消息参数内容类型
					String messageType=data[0].toString().trim();
					for (int j = 0; j < listg.size(); j++) {
						if(messageType.equals(listg.get(j).get("id")==null?"":listg.get(j).get("id").toString())){
							information_name=listg.get(j).get("information_name")==null?"":listg.get(j).get("information_name").toString();
							template=listg.get(j).get("content")==null?"":listg.get(j).get("content").toString();
							template_id=listg.get(j).get("information_id")==null?"":listg.get(j).get("information_id").toString();
							color=listg.get(j).get("color")==null?"":listg.get(j).get("color").toString();
							information_url=listg.get(j).get("information_url")==null?"":listg.get(j).get("information_url").toString();
							break;
						}
					}
					if("".equals(template_id)){
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						log.error("模板消息发送失败>>>>>>>template_id:"+template_id+",原因template_id为空");
						continue;
					}
					if("".equals(information_name)){
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						log.error("模板消息发送失败>>>>>>>information_name:"+information_name+",原因information_name为空");
						continue;
					}
					if("".equals(content)){
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						log.error("模板消息发送失败>>>>>>>content:"+content+",原因content为空");
						continue;
					}
					if("".equals(template)){
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						log.error("模板消息发送失败>>>>>>>c1:"+template+",原因模板为空");
						continue;
					}
					
					//Type:07首次还款提醒,08还款提醒,09还款失败提醒,10逾期还款提醒2天,11逾期还款提醒(多天),12结清通知
					//13保险到期提醒(续保),14额度调整通知(利率变更),15扣款提醒(收款确认)
					if("07".equals(messageType)){
						moduleMap=FirstLoanRepayMsgRemind(content);
						map.put("first", moduleMap.get("first"));
						map.put("headinfo", moduleMap.get("headinfo"));
						map.put("payDate", moduleMap.get("payDate"));
						map.put("payMoney", moduleMap.get("payMoney"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "2");//还款通知
					}else if("08".equals(messageType)){
						moduleMap=LoanRepayMsgRemind(content);
						map.put("first", moduleMap.get("first"));
						map.put("headinfo", moduleMap.get("headinfo"));
						map.put("payDate", moduleMap.get("payDate"));
						map.put("payMoney", moduleMap.get("payMoney"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "2");//还款通知
					}else if("09".equals(messageType)){
						moduleMap=RepayFailRemind(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "2");//还款提醒
					}else if("10".equals(messageType)){
						moduleMap=OverdueLoanRemindTwo(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "3");//逾期提醒
					}else if("11".equals(messageType)){
						moduleMap=OverdueLoanRemind(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "3");//逾期提醒
					}else if("12".equals(messageType)){
						moduleMap=SettlementNotice(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "2");//还款通知
					}else if("13".equals(messageType)){
						moduleMap=InsuranceMaturity(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "1");//通知公告
					}else if("14".equals(messageType)){
						moduleMap=QuotaAdjustNotice(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("keyword3", moduleMap.get("keyword3"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "1");//通知公告
					}else if("15".equals(messageType)){
						moduleMap=ChargebackReminder(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "1");//通知公告
					}else if("17".equals(messageType)){
						moduleMap=StaffMessagePush(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						//map.put("keyword2", moduleMap.get("keyword2"));
						//map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "1");//通知公告
					}else if("18".equals(messageType)){
						moduleMap=ChargebackReminder(content);
						map.put("first", moduleMap.get("first"));
						map.put("keyword1", moduleMap.get("keyword1"));
						map.put("keyword2", moduleMap.get("keyword2"));
						map.put("remark", moduleMap.get("remark"));
						map.put("msg_type", "1");//通知公告
					}else{
						log.info("该content内容中type不是定时微信所需类型："+content+"  进入下一循环");
						/*String sql="";
						sql="update gw_msg_reg set status='1' where date='"+date+"' and status='0' and phone='"+phone+"' and content='"+contents+"'";
						jdbc.update(sql);
						String now=format.format(new Date());
						statusMap.put("updateTime", now);
						statusMap.put("send_status", send_status);
						Object flag = SendStatusMQUtil.sendStatusMQ(statusMap);			//发送微信mq
						if("false".equals(flag)){
							log.info("发送StatusMQ队列失败！");
						}*/
						send_status="2";
						changeStatus(send_status,wx_msg_id);
						continue;
					}
					
					map.put("title", information_name);
					map.put("openid", openid);
					map.put("template", template);
					map.put("template_id", template_id);

					if(!"".equals(information_url)){
						String appid=redisDao.getString("appid");
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
					//调wechat发送模板消息
					String wx_url=redisDao.getString("WECHAT_URL");
					String url=wx_url+"/sendtemplatemessage";
					String result=HttpProcess.post(url, map);
					
					JSONObject jsonObject = JSONObject.fromObject(result);  
					Map<String,String> resultmap=new HashMap<String,String>();
					Iterator it = jsonObject.keys();  
					// 遍历jsonObject数据，添加到Map对象  
				       while (it.hasNext()) {  
				           String key = String.valueOf(it.next());  
				           String value = (String) jsonObject.get(key);  
				           resultmap.put(key, value);  
				       }  
				       if(!"0".equals(resultmap.get("errcode"))){
				    	   send_status="2";
				    	   changeStatus(send_status,wx_msg_id);
				    	   log.error("模板消息发送失败>>>>>>>errcode:"+resultmap.get("errcode")+",原因模板为:"+resultmap.get("errmsg"));
				       }
				       else{
				    	   send_status="1";
				       }
				       /*String sql="";
				       sql="update gw_msg_reg set status='1' where date='"+date+"' and status='0' and phone='"+phone+"' and content='"+contents+"'";
				       jdbc.update(sql);
				       String now=format.format(new Date());
				       
				       statusMap.put("updateTime", now);
					   statusMap.put("send_status", send_status);
					   Object flag = SendStatusMQUtil.sendStatusMQ(statusMap);			//发送微信mq
					   if("false".equals(flag)){
						log.info("发送StatusMQ队列失败！");
					   }*/
				       changeStatus(send_status,wx_msg_id);
					   String sql="";
					   String now=format.format(new Date());
				       sql="insert into gw_msg_wechat_reg (msg_date,msg_phone,msg_type,openid,send_time,status)"
				       		+ "values('"+date+"','"+phone+"','"+messageType+"','"+openid+"','"+now+"','"+send_status+"')";
				       jdbc.update(sql);
				}
				return true;
			}else{
				log.info("需要将短信转成微信的记录的条数为"+lists.size());
				return true;
			}
		}catch(Exception e){
			log.error("模板消息异常：", e);
			return false;
		}
		
	}
	
	/**
	 * 改变消息发送状态
	 * @param msgStatus
	 * @return
	 */
	public String changeStatus(String msgStatus,String wx_msg_id){
		log.info("改变消息发送状态");
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		Format format = new SimpleDateFormat("yyyyMMddhhmmss");
		Map<String,String> statusMap=new HashMap<String,String>(); 			//创建状态map
		
		String sql="";
		sql="update gw_msg_reg set status='"+msgStatus+"' where wx_msg_id='"+wx_msg_id+"'";
		jdbc.update(sql);
		String now=format.format(new Date());
		statusMap.put("updateTime", now);
		statusMap.put("send_status", msgStatus);
		Object flag = SendStatusMQUtil.sendStatusMQ(statusMap);			//发送微信mq
		if("false".equals(flag)){
			log.info("发送StatusMQ队列失败！");
		}
		return "";
	}

	/**
	 * 日期转换由2016/01/01转2016年01月01日
	 * @param content
	 * @return
	 */
	public String checkDate(String nowDate){
		nowDate=nowDate.replaceFirst("/", "年");
		nowDate=nowDate.replaceFirst("/", "月");
		return nowDate+"日";
	}

	/**
	 * 贷款还款提醒(首次还款日)
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> FirstLoanRepayMsgRemind(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：");
		map.put("headinfo", "您的长城汽车金融贷款首次还款日快到了");
		String [] payDate=(content.split("fd_day:"))[1].split(";");
		map.put("payDate", checkDate(payDate[0].toString().trim()));
		String [] payMoney=(content.split("f_amount:"))[1].split(";");
		String tmpdate=payDate[0].toString().trim().replace("/", "");
		tmpdate=ttDate(tmpdate);
		map.put("payMoney", payMoney[0].toString().trim()+"元");
		map.put("remark","\n☆温馨提示☆ 为了您的贷款能按时还款，请于"+checkDate(tmpdate)+"前确保您的还款账户余额充足。您可以点击“详情”查看详细信息。");
		return map;
	}
	/**
	 * 贷款还款提醒
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> LoanRepayMsgRemind(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：");
		map.put("headinfo", "您的长城汽车金融贷款还款日快到了");
		String [] payDate=(content.split("cd_day:"))[1].split(";");
		map.put("payDate", checkDate(payDate[0].toString().trim()));
		String [] payMoney=(content.split("c_amount:"))[1].split(";");
		String tmpdate=payDate[0].toString().trim().replace("/", "");
		tmpdate=ttDate(tmpdate);
		map.put("payMoney", payMoney[0].toString().trim()+"元");
		map.put("remark","\n☆温馨提示☆ 为了您的贷款能按时还款，请于"+checkDate(tmpdate)+"前确保您的还款账户余额充足。您可以点击“详情”查看详细信息。");
		return map;
	}
	public String ttDate(String date){
		if(date.length()!=8){
			return date;
		}
		String beforedate=Util.getSpecifiedDayBefore(date);//前一天日期
		String year=beforedate.substring(0,4);
		String month=beforedate.substring(4,6);
		String day=beforedate.substring(6,8);
		return year+"/"+month+"/"+day;
	}
	/**
	 * 还款失败提醒
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> RepayFailRemind(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n很抱歉您本次长城汽车金融贷款还款失败！\n");
		String [] keyword1=(content.split("c_amount:"))[1].split(";");
		map.put("keyword1", keyword1[0].toString().trim()+"元");
		map.put("keyword2", "绑定卡余额不足");
		map.put("remark","\n请于今日下午16:00前存入本期月还款金额，如今日扣款成功，将视同按时还款。如已存入请忽略。");
		return map;
	}
	/**
	 * 贷款逾期提醒 2天
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> OverdueLoanRemindTwo(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n您的还款账户余额不足！");
		map.put("keyword1", "2天");
		String [] keyword2=(content.split("c_amount:"))[1].split(";");
		map.put("keyword2", keyword2[0].toString().trim()+"元");
		map.put("remark","\n请于今日16:00前存入本期月还款金额，避免征信进一步恶化。如已存入请忽略。");
		return map;
	}
	/**
	 * 贷款逾期提醒  多天(7,10,15)
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> OverdueLoanRemind(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n您的长城汽车金融贷款已逾期！");
		String [] keyword1=(content.split("a_day:"))[1].split(";");
		map.put("keyword1", keyword1[0].toString().trim()+"天");
		String [] keyword2=(content.split("c_amount:"))[1].split(";");
		map.put("keyword2", keyword2[0].toString().trim()+"元");
		map.put("remark","\n请马上存入逾期欠款，避免您违约产生严重的法律后果。");
		return map;
	}
	/**
	 * 结清通知
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> SettlementNotice(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		String [] contract=(content.split("app_no:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n您合同编号为"+contract[0].toString().trim()+"《汽车抵押贷款合同》项下的债务已经结清。");
		map.put("keyword1", first[0].toString().trim());
		Format format = new SimpleDateFormat("yyyyMMdd");
		String nowdate=format.format(new Date());
		String keyword2=nowdate.substring(0, 4)+"年"+nowdate.substring(4,6)+"月"+nowdate.substring(6,8)+"日";
		map.put("keyword2", keyword2);
		map.put("remark","\n更多活动请点击“详情”查看");
		return map;
	}
	/**
	 * 保险到期提醒
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> InsuranceMaturity(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n您的贷款车辆保单即将到期。\n");
		map.put("keyword1", first[0].toString().trim());
		String [] keyword2=(content.split("ie_day:"))[1].split(";");
		map.put("keyword2", checkDate(keyword2[0].toString().trim()));
		map.put("remark","\n请在保单到期前进行续保。");
		return map;
	}
	/**
	 * 额度调整通知
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> QuotaAdjustNotice(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n因央行贷款基准利率调整，我公司将执行新的个人贷款利率，您的月度还款金额会有所调整。\n");
		map.put("keyword1", "汽车抵押贷款");
		map.put("keyword2", first[0].toString().trim());
		map.put("keyword3", "央行基准利率调整");
		map.put("remark","\n☆温馨提示☆ 您的下一期还款金额敬请留意我司每月发出的月还款提醒短信，您还可以点击“详情”及时关注还款信息。");
		return map;
	}
	/**
	 * 扣款提醒
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> ChargebackReminder(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n您的长城汽车金融贷款本期扣款成功。\n");
		String [] keyword1=(content.split("c_amount:"))[1].split(";");
		map.put("keyword1", keyword1[0].toString().trim()+"元 \n");
		String [] keyword2=(content.split("paid_month:"))[1].split(";");
		map.put("keyword2", keyword2[0].toString().trim()+"期\n");
		String [] keyword3=(content.split("sum_amount:"))[1].split(";");
		map.put("keyword3", keyword2[0].toString().trim()+"元 \n");
		String [] keyword4=(content.split("remain_month:"))[1].split(";");
		map.put("keyword4", keyword2[0].toString().trim()+"期");
		map.put("remark","\n☆温馨提示☆ 下期还款信息请点击“详情”查看。");
		return map;
	}
	/**
	 * 员工消息推送
	 * @param content
	 * @return
	 */
	@Transactional
	public Map<String,String> StaffMessagePush(String content){
		Map<String,String> map=new HashMap<String,String>();	
		String [] first=(content.split("name:"))[1].split(";");
		map.put("first", ("尊敬的"+first[0].toString().trim())+"先生/女士：\n");
		String [] keyword1=(content.split("content:"))[1].split(";");
		map.put("keyword1", keyword1[0].toString().trim()+"\n");
		//map.put("remark","\n☆温馨提示☆ 下期还款信息请点击“详情”查看。");
		return map;
	}
}
