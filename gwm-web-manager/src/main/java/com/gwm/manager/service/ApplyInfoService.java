package com.gwm.manager.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gwm.common.RedisDao;




@Service
public class ApplyInfoService {
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(ApplyInfoService.class);
	/**
	 * 导入excel文件
	 * */
	public Object importExcel(Map<String, String> map){
		Object retObj=com.gwm.common.service.Service.importExcel(map);
		String retJson=(String)retObj;
		log.info("导入excel文件，rejson====="+retJson);
		return retObj;
	}
	/**
	 * 验证登录有效性参数
	 * */
	public Object verification(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.verification(map);
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("验证登录有效性参数，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 单页面验证
	 * */
	public Object checkUser(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.checkUser(map);
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("单页面验证有效性参数，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 查询热销车型信息
	 * */
	public Object selectHotCarInfo(Map<String,String> map){
		Object retObj=com.gwm.common.service.Service.selectHotCarInfo(map);
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("查询热销车型信息，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 导出热销车型excel
	 * */
	public Object exportHotCarInfo(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.exportHotCarInfo();
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("导出热销车型excel，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 查询在线申请信息
	 * @param map
	 * @return
	 */
	public Object selectApplyInfo(Map<String, String> map){
		
		Object retObj = com.gwm.common.service.Service.selectApplyInfo();
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("查询在线申请信息，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 导出在线申请excel
	 * */
	public Object exportApplyInfo(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.exportApplyInfo();
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("导出在线申请excel，rejson======"+retJson);
		return retObj;
	}
	/**
	 * 更新在线申请信息
	 * */
	public Object updateApplyInfo(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.updateApplyInfo();
		String retJson=(String)retObj;//返回的map信息
//		Map<String, Object> msgMap = JSONObject.parseObject(retJson);
		log.info("更新在线申请信息，rejson======"+retJson);
		return retObj;
	}
}
