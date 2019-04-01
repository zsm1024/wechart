package com.gwm.module.wechat.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;
import com.gwm.module.wechat.service.WxMenuService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>Description:发送状态发送到MQ工具类<p>	
 * @author hzn
 * @date 2018年5月7日
 */
@Component
public class SendStatusMQUtil {
	
	private static RedisDao redis;
	private static Logger log = LoggerFactory.getLogger(WxMenuService.class);
	/**
	 * 配置redis
	 */
	@Autowired
	public void setRedisDao(RedisDao redisDao) {
		SendStatusMQUtil.redis = redisDao;
	}
	
	/**
	 * 	
	 * @param map
	 */
	public static boolean sendStatusMQ(Map<String,String> map) {
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
			channel.exchangeDeclare(exchange, "topic", true, false, null);
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

}
