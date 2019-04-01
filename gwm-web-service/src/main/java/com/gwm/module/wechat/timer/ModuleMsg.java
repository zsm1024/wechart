package com.gwm.module.wechat.timer;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.engine.HttpProcess;
import com.gwm.module.wechat.service.GlobalCodeService;
/**
 * 模板消息测试例子（预约取款）
 * */
@Service
public class ModuleMsg {
	private Logger log=LoggerFactory.getLogger(GlobalCodeService.class);
	
	@Transactional
	public boolean ModuleMsg(String taskname){
		//需要发送那些数据(库)
		try{
		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
		Format format = new SimpleDateFormat("yyyyMMddhhmmss");
		String tnow=format.format(new Date());
		String type="1";//预约取款模板消息
		String sql="select s.*,i.information_name,i.content as c1,i.information_url,i.color "
				+ "from (select r.phone,r.content,r.wx_msg_id,l.openid,r.date from gw_msg_reg r "
				+ "left join gw_user_loan_rel l on l.phone=r.phone where  r.status='0' and  date<="+tnow+" and type='"+type+"') s "
				+ "left join gw_wx_general_information i on s.wx_msg_id=i.information_id ";
		log.info("sql="+sql);
		List<Map<String,Object>> lists=jdbc.queryForList(sql);
		if(lists.size()>0){
			log.info("需要将短信转成微信的记录的条数为"+lists.size());
			for(int i=0;i<lists.size();i++){
				String send_status="1";//默认成功
				String wx_msg_id=lists.get(i).get("wx_msg_id")==null?"":lists.get(i).get("wx_msg_id").toString();
				String openid=lists.get(i).get("openid")==null?"":lists.get(i).get("openid").toString();
				String information_name=lists.get(i).get("information_name")==null?"":lists.get(i).get("information_name").toString();
				String content=lists.get(i).get("content")==null?"":lists.get(i).get("content").toString();
				String information_url=lists.get(i).get("information_url")==null?"":lists.get(i).get("information_url").toString();
				String module=lists.get(i).get("c1")==null?"":lists.get(i).get("c1").toString();
				String color=lists.get(i).get("color")==null?"":lists.get(i).get("color").toString();
				if("".equals(wx_msg_id)){
					send_status="2";
					log.error("模板消息发送失败>>>>>>>wx_msg_id:"+wx_msg_id+",原因wx_msg_id为空");
					continue;
				}
				if("".equals(openid)){
					send_status="2";
					log.error("模板消息发送失败>>>>>>>openid:"+openid+",原因openid为空");
					continue;
				}
				if("".equals(information_name)){
					send_status="2";
					log.error("模板消息发送失败>>>>>>>information_name:"+information_name+",原因information_name为空");
					continue;
				}
				if("".equals(content)){
					send_status="2";
					log.error("模板消息发送失败>>>>>>>content:"+content+",原因content为空");
					continue;
				}
				if("".equals(module)){
					send_status="2";
					log.error("模板消息发送失败>>>>>>>c1:"+module+",原因模板为空");
					continue;
				}
				//尊敬的张先生：申请人姓名：张先生 意向金额：2000.00 预约取款时间：2016-11-29，联系电话：15566812881 这是remark
				Map<String,String> map=new HashMap<String,String>();				
				String [] first=content.split("申请人姓名：");
				map.put("first", first[0].toString().trim());
				String [] keyword1=first[1].toString().trim().split("意向金额：");
				map.put("keyword1", keyword1[0].toString().trim());
				String [] keyword2=keyword1[1].toString().trim().split("预约取款时间：");
				map.put("keyword2", keyword2[0].toString().trim());
				String [] keyword3=keyword2[1].toString().trim().split("联系电话：");
				map.put("keyword3", keyword3[0].toString().trim());
				map.put("keyword4", keyword3[1].toString().trim().substring(0,11));
				map.put("remark",keyword3[1].toString().trim().substring(11,keyword3[1].toString().length()).trim());
				map.put("template", module);
				map.put("openid", openid);
				map.put("template_id", wx_msg_id);
				if(!"".equals(information_url)){
					map.put("url", information_url);
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
			       while (it.hasNext())  
			       {  
			           String key = String.valueOf(it.next());  
			           String value = (String) jsonObject.get(key);  
			           resultmap.put(key, value);  
			       }  
			       if(!"0".equals(resultmap.get("errcode"))){
			    	   send_status="2";
			    	   log.error("模板消息发送失败>>>>>>>errcode:"+map.get("errcode")+",原因模板为:"+map.get("errmsg"));
			       }
			       else{
			    	   send_status="1";
			       }
			       String date=lists.get(i).get("date")==null?"":lists.get(i).get("date").toString();
			       String phone=lists.get(i).get("phone")==null?"":lists.get(i).get("phone").toString();
			       sql="update gw_msg_reg set status='1' where date='"+date+"' and status='0' and phone='"+phone+"'";
			       jdbc.update(sql);
			       String now=format.format(new Date());
			       sql="insert into gw_msg_wechat_reg values('"+date+"','"+phone+"','"+type+"','"+openid+"','"+now+"','"+send_status+"')";
			       jdbc.update(sql);
			       
			}
			return true;
		}else
		{
			log.info("需要将短信转成微信的记录的条数为"+lists.size());
			return true;
		}
		}catch(Exception e){
			log.error("模板消息测试例子（预约取款）异常：", e);
			return false;
		}
		
		//修改发送状态
	}
	
	public static void main(String args[]){
		String content="尊敬的张先生：申请人姓名：张先生 意向金额：2000.00 预约取款时间：2016-11-29，联系电话：15566812881 这是remark";
		Map<String,String> map=new HashMap<String,String>();
		String [] first=content.split("申请人姓名：");
		map.put("first", first[0].toString().trim());
		String [] keyword1=first[1].toString().trim().split("意向金额：");
		map.put("keyword1", keyword1[0].toString().trim());
		String [] keyword2=keyword1[1].toString().trim().split("预约取款时间：");
		map.put("keyword2", keyword2[0].toString().trim());
		String [] keyword3=keyword2[1].toString().trim().split("联系电话：");
		map.put("keyword3", keyword3[0].toString().trim());
		map.put("keyword4", keyword3[1].toString().trim().substring(0,11));
		map.put("remark",keyword3[1].toString().trim().substring(11,keyword3[1].toString().length()).trim());
	}
}
