package com.gwm.manager.service;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwm.common.RedisDao;

@Service
public class MsgContentService {
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(MsgContentService.class);
	/**
	 * 查询消息内容
	 * @param map
	 * @return
	 */
	public Object selectMsgContent(Map<String, String> map){
		Object retObj = com.gwm.common.service.Service.selectMsgContent();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	
	/**
	 * 更新消息内容
	 * @param map
	 * @return
	 */
	public Object updateMsgContent(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.updateMsgContent();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	
	/**
	 * 删除消息内容
	 * @param map
	 * @return
	 */
	public Object delMsgContent(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.delMsgContent();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	
	/**
	 * 新增消息内容
	 * @param map
	 * @return
	 */
	public Object addMsgContent(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.addMsgContent();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
}
