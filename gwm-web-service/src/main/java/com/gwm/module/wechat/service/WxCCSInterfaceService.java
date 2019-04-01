package com.gwm.module.wechat.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.common.SqlManager;
import com.gwm.db.entity.Gw_ccs_online;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Service
public class WxCCSInterfaceService {
	@Autowired
	JdbcTemplate jdbc = null;
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(WxMenuService.class);
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 验证客户在线客服是否在线
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> onLine(){
		try{
			Map<String, String> sqlMap = new HashMap<String, String>();
			String oriSql = "select * from gw_ccs_online  ";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			return list;
		}catch(Exception ex){
			log.error("查询gw_ccs_online异常", ex);
			return null;
		}
	}
	
	/**
	 * 验证客户在线客服是否在线
	 * @param param
	 * @return
	 */
	public Boolean SetCustomerOnLine(Map<String,String> map){
		try{
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("openid", map.get("FromUserName"));
			sqlMap.put("createTime",sdf.format(new Date()));
			String oriSql = "insert into gw_ccs_online(openid,createTime) values('$openid$','$createTime$') ";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			int i = jdbc.update(sql);
			if(i==0)
			{
				return false;
			}else{
				return true;
			}
		}catch(Exception ex){
			log.error("插入gw_ccs_online异常", ex);
			return false;
		}
	}
	public Boolean isVip(Map<String,String> map){
		try{
			Map<String, String> sqlMap = new HashMap<String, String>();
			sqlMap.put("openid", map.get("FromUserName"));
			String oriSql = "select * from gw_user_info where openid='$openid$' ";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			if(list.size()==0)
			{
				return false;
			}else{
				return true;
			}
		}catch(Exception ex){
			log.error("查询gw_user_info异常", ex);
			return false;
		}
	}
	
	public Boolean sendRabbitMQ(Map<String,String> map){
		String host=redis.getString("mq_host");
		String port=redis.getString("mq_port");
		String username=redis.getString("mq_username");
		String password=redis.getString("mq_password");
		String queue=redis.getString("mq_queue");
		String vhost=redis.getString("mq_vhost");
		String exchange=redis.getString("mq_exchange");
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(Integer.parseInt(port));
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vhost);
		Connection connection=null;
		Channel channel=null;
		try{	
			connection=factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchange, "direct", true, false, null);
			channel.queueDeclare(queue, true, false, false, null);	
			log.info(map.get("xml"));
			channel.basicPublish(exchange, queue, null,map.get("xml").getBytes("UTF-8"));
			
		
			return true;
		}catch(Exception ex){
			log.error("发送MQ异常", ex);
			return false;
		}finally{
			try{
			
			if(connection!=null)
			{
				connection.close();
			}
			}catch(Exception e)
			{
				log.error("MQ关闭失败！", e);
				e.printStackTrace();
			}
		}
		
	}
	public Boolean sendStatusMQ(Map<String,String> map){
		String host=redis.getString("status_mq_host");
		String port=redis.getString("status_mq_port");
		String username=redis.getString("status_mq_username");
		String password=redis.getString("status_mq_password");
		String queue=redis.getString("status_mq_queue");
		String vhost=redis.getString("status_mq_vhost");
		String exchange=redis.getString("status_mq_exchange");
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(Integer.parseInt(port));
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vhost);
		Connection connection=null;
		Channel channel=null;
		try{	
			connection=factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchange, "direct", true, false, null);
			channel.queueDeclare(queue, true, false, false, null);
			JSONObject jsonObject = new JSONObject();
			String status = jsonObject.toJSONString(map);
			log.info(map.get("xml"));
			channel.basicPublish(exchange, queue, null,status.getBytes("UTF-8"));
			
		
			return true;
		}catch(Exception ex){
			log.error("发送MQ异常", ex);
			return false;
		}finally{
			try{
			
			if(connection!=null)
			{
				connection.close();
			}
			}catch(Exception e)
			{
				log.error("MQ关闭失败！", e);
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String []args){
		String host="10.50.128.150";
		String port="5672";
		String username="weixin";
		String password="weixinmq";
		String queue="wechatMessage";
		String vhost="weixin";
		String exchange="wechatExchange";
		
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(Integer.parseInt(port));
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vhost);
		Connection connection=null;
		Channel channel=null;
		try{
			
			
			connection=factory.newConnection();
			channel=connection.createChannel();
			channel.exchangeDeclare(exchange, "direct", true, false, null);
			channel.queueDeclare(queue, true, false, false, null);
			
			channel.basicPublish(exchange, queue, null,JSONObject.toJSONString("ccccc").getBytes("UTF-8"));
			
		
			
		}catch(Exception ex){
			//log.error("发送MQ异常", ex);
			
		}finally{
			try{
			
			if(connection!=null)
			{
				connection.close();
			}
			}catch(Exception e)
			{
				
				e.printStackTrace();
			}
		}
	}
}
