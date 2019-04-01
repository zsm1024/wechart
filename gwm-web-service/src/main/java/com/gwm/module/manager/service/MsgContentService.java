package com.gwm.module.manager.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.RedisDao;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_prompt_info;
import com.gwm.db.entity.meta.Gw_prompt_infoMeta;
import com.gwm.engine.Util;

@Service
public class MsgContentService {
	private Logger log = LoggerFactory.getLogger(MsgContentService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	
	
	/**
	 * 后台管理-查询消息内容管理
	 * @param map
	 * @return
	 */
	public Object selectMsgContent(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		try{
			String index=map.get("page");//第几页
			String size=map.get("rows");//每页条数
			int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);
			
			String sql="select * from gw_prompt_info where 1=1";
			String allSql=sql;
			sql+=" limit $startnum$,$size$";//数字   m,n 从第m+1条开始查询 查到m+n+1条
			inputMap.put("startnum", startnum);
			inputMap.put("size", size);
			sql=SqlManager.getSql(sql, inputMap);
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			log.info("执行sql: "+allSql);
			allList=jdbc.queryForList(allSql);
			log.info("*"+retList);
			log.info("*"+allList);
			if(retList==null || retList.size()<=0){
				log.info("未找到符合当前查询的记录");
				retMap.put("rows", retList);
				retMap.put("total", allList.size());
				retMap.put("errcode", "0001");	
				retMap.put("errmsg", "未找到符合当前查询的记录");
				JSONObject jsonObject = JSONObject.fromObject(retMap);
				log.info("查询消息内容管理记录条数:"+allList.size());
				return jsonObject;
			}
			retMap.put("rows", retList);
			retMap.put("total", allList.size());
			retMap.put("errcode", "0000");	
			retMap.put("errmsg", "查询消息内容管理记录成功");
			log.info("查询消息内容管理记录条数:"+allList.size());
			return retMap;
		}catch(Exception e){
			log.error("查询消息内容管理记录出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			return listMap;
		}
		
	}
	
	/**
	 * 后台管理-新增消息内容管理
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> addMsgContent(Map<String,String> map){
		try{
			String id=UUID.randomUUID().toString();
			Gw_prompt_info updateEntity=new Gw_prompt_info();
			updateEntity.setMsg_type(map.get("msg_type"));
			updateEntity.setBinding_state(map.get("binding_state"));
			String str=URLDecoder.decode(map.get("msg_content"));
			updateEntity.setMsg_content(str);
			updateEntity.setLink_address(map.get("link_address"));
			updateEntity.setMsg_state(map.get("msg_state"));
			updateEntity.setId(id);
			dao.insert(updateEntity);
			log.info("新增消息内容管理成功");
			return Util.returnMap("0000", "新增消息内容管理成功");
		}catch(Exception e){
			log.error("新增消息内容管理出现异常",e);
			return Util.returnMap("0001", "新增消息内容管理异常");
		}
		
	}
	
	/**
	 * 后台管理-更新消息内容管理
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> updateMsgContent(Map<String,String> map){
		try{
			Gw_prompt_info updateEntity=null;
			updateEntity=dao.fetch(Gw_prompt_info.class, Cnd.where(Gw_prompt_infoMeta.id.eq(map.get("id"))));
			if(updateEntity!=null){
				updateEntity.setMsg_type(map.get("msg_type"));
				updateEntity.setBinding_state(map.get("binding_state"));
				String str=URLDecoder.decode(map.get("msg_content"));
				updateEntity.setMsg_content(str);
				updateEntity.setLink_address(map.get("link_address"));
				updateEntity.setMsg_state(map.get("msg_state"));
				dao.update(updateEntity, Cnd.where(Gw_prompt_infoMeta.id.eq(map.get("id"))));
				log.info("更新消息内容管理成功");
				return Util.returnMap("0000", "更新消息内容管理成功");
			}
			else{
				log.error("该消息不存在，请刷新后再试");
				return Util.returnMap("0001", "该消息不存在，请刷新后再试");
			}
		}catch(Exception e){
			log.error("更新消息内容管理出现异常",e);
			return Util.returnMap("0001", "更新消息内容管理异常");
		}
		
	}
	
	/**
	 * 后台管理-删除消息内容管理
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> delMsgContent(Map<String,String> map){
		try{
			Gw_prompt_info delEntity=null;
			delEntity=dao.fetch(Gw_prompt_info.class, Cnd.where(Gw_prompt_infoMeta.id.eq(map.get("id"))));
			if(delEntity!=null){
				dao.delete(Gw_prompt_info.class, Cnd.where(Gw_prompt_infoMeta.id.eq(map.get("id"))));
				log.info("删除消息内容管理成功");
				return Util.returnMap("0000", "删除消息内容管理成功");
			}
			else{
				log.error("该消息不存在，请刷新后再试");
				return Util.returnMap("0001", "该消息不存在，请刷新后再试");
			}
		}catch(Exception e){
			log.error("删除消息内容管理出现异常",e);
			return Util.returnMap("0001", "删除消息内容管理异常");
		}
		
	}
	
}
