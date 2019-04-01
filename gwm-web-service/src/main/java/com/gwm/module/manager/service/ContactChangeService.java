package com.gwm.module.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.gwm.common.SqlExecException;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_modify_tel_reg;
import com.gwm.db.entity.Gw_user_info;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_modify_tel_regMeta;
import com.gwm.db.entity.meta.Gw_user_infoMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;

@Service
public class ContactChangeService {
	private Logger log = LoggerFactory.getLogger(ContactChangeService.class);
	@Autowired
	private Dao dao = null;
	@Autowired
	private RedisDao redisDao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	
	public Object selectContactChangeForSub(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		try{
			String contract_code=map.get("contract_code");
			inputMap.put("contract_code", contract_code);
			String sql="select * ,(case when length(apply_time)=0 then concat(apply_date,'000000')"
					+" when length(apply_time)=1 then concat(apply_date,'00000',apply_time)"
					+" when length(apply_time)=2 then concat(apply_date,'0000',apply_time)"
					+" when length(apply_time)=3 then concat(apply_date,'000',apply_time)"
					+" when length(apply_time)=4 then concat(apply_date,'00',apply_time)"
					+" when length(apply_time)=5 then concat(apply_date,'0',apply_time)"
					+" when length(apply_time)>=6 then concat(apply_date,apply_time) end )as dtime "
					+ "from gw_modify_tel_reg where contract_code='$contract_code$' order by apply_date,apply_time desc";
			sql=SqlManager.getSql(sql, inputMap);
			log.info("sql:"+sql);
			retList=jdbc.queryForList(sql);
			retMap.put("rows", retList);
			retMap.put("errcode", "0000");	
			retMap.put("errmsg", "查询当前合同联系方式变更记录成功");
			JSONObject jsonObject = JSONObject.fromObject(retMap);
			log.info("当前用户查询意见反馈条数:"+"rows=", retList.size());
			return jsonObject;
		}catch(Exception e){
			log.error("查询当前合同联系方式变更记录出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
		
	}
	/**
	 * 后台管理-查询联系方式变更记录
	 * @param map
	 * @return
	 */
	public Object selectContactChange(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		Map<String,Object> inputMap2=new HashMap<String,Object>();
		try{
			String applicationer=map.get("applicationer");
			String contract_code=map.get("contract_code");
			String search_status=map.get("search_status");
			String search_startdate=map.get("search_startdate");
			String search_enddate=map.get("search_enddate");
			String index=map.get("pagenumber").equals("0")?"1":map.get("pagenumber");//第几页
			String size=map.get("pagesize");//每页条数
			int startnum=(Integer.parseInt(index)-1)*Integer.parseInt(size);

			String sql="select r.* from(select b.contract_code,max(b.dtime) as dtime "
					+ "from ( select * from"
					+" (select *,("
					+ "case when length(apply_time)=0 then concat(apply_date,'000000')"
					+" when length(apply_time)=1 then concat(apply_date,'00000',apply_time)"
					+" when length(apply_time)=2 then concat(apply_date,'0000',apply_time)"
					+" when length(apply_time)=3 then concat(apply_date,'000',apply_time)"
					+" when length(apply_time)=4 then concat(apply_date,'00',apply_time)"
					+" when length(apply_time)=5 then concat(apply_date,'0',apply_time)"
					+" when length(apply_time)>=6 then concat(apply_date,apply_time) end )as dtime "
					+" from gw_modify_tel_reg)a where 1=1 ";
			String allSql=sql;
			if(applicationer!=null && !"".equals(applicationer.toString())){
				sql+=" and applicationer like '%$applicationer$%' ";
				allSql+=" and applicationer like '%$applicationer$%' ";
			}
			if(contract_code!=null && !"".equals(contract_code.toString())){
				sql+=" and contract_code='$contract_code$' ";
				allSql+=" and contract_code='$contract_code$' ";
			}
			if(search_status!=null && !"".equals(search_status.toString()) && !"0".equals(search_status.toString())){
				sql+=" and status='$search_status$' ";
				allSql+=" and status='$search_status$' ";
			}
			if(search_startdate!=null && !"".equals(search_startdate.toString())){
				search_startdate=formatDateForKongjian(search_startdate);
				sql+=" and dtime>'$search_startdate$' ";
				allSql+=" and dtime>'$search_startdate$' ";
			}
			if(search_enddate!=null && !"".equals(search_enddate.toString())){
				search_enddate=formatDateForKongjian(search_enddate);
				sql+=" and dtime<'$search_enddate$' ";
				allSql+=" and dtime<'$search_enddate$' ";
			}
			allSql+=" order by dtime desc ) b group by b.contract_code";
			sql+=" order by dtime desc ) b group by b.contract_code";
			
			sql+=" ) c left join ( select *,( "
					+ "case when length(apply_time)=0 then concat(apply_date,'000000') "
					+ "when length(apply_time)=1 then concat(apply_date,'00000',apply_time) "
					+ "when length(apply_time)=2 then concat(apply_date,'0000',apply_time) "
					+ "when length(apply_time)=3 then concat(apply_date,'000',apply_time) "
					+ "when length(apply_time)=4 then concat(apply_date,'00',apply_time) "
					+ "when length(apply_time)=5 then concat(apply_date,'0',apply_time) "
					+ "when length(apply_time)>=6 then concat(apply_date,apply_time) end"
					+ " )as dtime "
					+ "from gw_modify_tel_reg ) r on r.contract_code=c.contract_code and r.dtime=c.dtime order by dtime desc limit $startnum$,$size$  ";
			allSql+=" ) c left join ( select *,( "
					+ "case when length(apply_time)=0 then concat(apply_date,'000000') "
					+ "when length(apply_time)=1 then concat(apply_date,'00000',apply_time) "
					+ "when length(apply_time)=2 then concat(apply_date,'0000',apply_time) "
					+ "when length(apply_time)=3 then concat(apply_date,'000',apply_time) "
					+ "when length(apply_time)=4 then concat(apply_date,'00',apply_time) "
					+ "when length(apply_time)=5 then concat(apply_date,'0',apply_time) "
					+ "when length(apply_time)>=6 then concat(apply_date,apply_time) end"
					+ " )as dtime "
					+ "from gw_modify_tel_reg ) r on r.contract_code=c.contract_code and r.dtime=c.dtime";
			
			inputMap.put("applicationer", applicationer);
			inputMap.put("contract_code", contract_code);
			inputMap.put("search_status", search_status);
			inputMap.put("search_startdate", search_startdate);
			inputMap.put("search_enddate", search_enddate);
			inputMap.put("startnum", startnum);
			inputMap.put("size", size);
			
			inputMap2.put("applicationer", applicationer);
			inputMap2.put("contract_code", contract_code);
			inputMap2.put("search_status", search_status);
			inputMap2.put("search_startdate", search_startdate);
			inputMap2.put("search_enddate", search_enddate);
			
			sql=SqlManager.getSql(sql, inputMap);
			allSql=SqlManager.getSql(allSql, inputMap2);//查询所有计算条数
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			log.info("执行sql: "+allSql);
			allList=jdbc.queryForList(allSql);
			if(retList==null || retList.size()<=0){
				log.info("未找到符合当前查询的记录");
				retMap.put("rows", retList);
				retMap.put("total", allList.size());
				retMap.put("errcode", "0001");	
				retMap.put("errmsg", "未找到符合当前查询的记录");
				JSONObject jsonObject = JSONObject.fromObject(retMap);
				log.info("查询联系方式变更记录条数:"+allList.size());
				return jsonObject;
			}
			retMap.put("rows", retList);
			retMap.put("total", allList.size());
			retMap.put("errcode", "0000");	
			retMap.put("errmsg", "查询联系方式变更记录成功");
			JSONObject jsonObject = JSONObject.fromObject(retMap);
			log.info("查询联系方式变更记录条数:"+allList.size());
			return jsonObject;
		}catch(Exception e){
			log.error("查询联系方式变更记录出现异常",e);
			Map<String,Object> listMap=new HashMap<String,Object>();
			listMap.put("errcode", "0001");	
			listMap.put("errmsg", "未找到符合当前查询的记录。");
			JSONObject jsonObject = JSONObject.fromObject(listMap);
			return jsonObject;
		}
		
	}
	
	/**
	 * 后台管理-更新联系方式变更记录
	 * @param map
	 * @return
	 */
	@Transactional
	public Map<String, String> updateContactChange(Map<String,String> map) throws Exception{
		try{
			String tel_reg_num=map.get("tel_reg_num");
			String status=map.get("status");
			String userid=map.get("userid");
			
			Gw_modify_tel_reg updateEntity=null;
			updateEntity=dao.fetch(Gw_modify_tel_reg.class, Cnd.where(Gw_modify_tel_regMeta.tel_reg_num.eq(tel_reg_num)));
			if(updateEntity!=null){
				String openid=updateEntity.getOpenid();
				String newPhone=updateEntity.getNew_phone();
				updateEntity.setStatus(status);
				updateEntity.setReclaim_person(userid);
				dao.update(updateEntity, Cnd.where(Gw_modify_tel_regMeta.tel_reg_num.eq(tel_reg_num)));
				log.info("联系方式变更表变更成功");
				if(status.equals("4")){
					Gw_user_info infoEnt=null;
					infoEnt=dao.fetch(Gw_user_info.class, Cnd.where(Gw_user_infoMeta.openid.eq(openid)));
					if(infoEnt!=null){
						infoEnt.setUser_phone(newPhone);
						dao.update(infoEnt, Cnd.where(Gw_user_infoMeta.openid.eq(openid)));
						log.info("客户信息变更表变更成功");
						Gw_user_loan_rel relEnt=null;
						relEnt=dao.fetch(Gw_user_loan_rel.class, Cnd.where(Gw_user_loan_relMeta.openid.eq(openid)));
						if(relEnt!=null){
							relEnt.setPhone(newPhone);
							relEnt.setStatus("3");//联系方式变更后，状态为解绑
							dao.update(relEnt, Cnd.where(Gw_user_loan_relMeta.openid.eq(openid)));
							log.info("更新状态成功");
							return Util.returnMap("0000", "更新状态成功");
						}
						else{
							throw new SqlExecException("用户信息绑定表异常");
						}
					}
					else{
						throw new SqlExecException("用户信息表异常");
					}
				}
				log.info("更新状态成功");
				return Util.returnMap("0000", "更新状态成功");
			}
			else{
				log.error("更新状态失败");
				return Util.returnMap("0001", "更新状态失败");
			}
		}catch(Exception e){
			log.error("更新联系方式变更记录状态出现异常",e);
			throw e;
//			return Util.returnMap("0001", "更新联系方式变更记录状态异常");
			
		}
		
	}
	
	/**
	 * 后台管理-导出联系方式变更记录excel
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> exportContactChange(Map<String,String> map){
		List<Map<String,Object>> retList=new ArrayList<Map<String,Object>>();
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> inputMap=new HashMap<String,Object>();
		try{
			String applicationer=map.get("applicationer");
			String contract_code=map.get("search_startdate");
			String search_status=map.get("search_status");
			String search_startdate=map.get("search_startdate");
			String search_enddate=map.get("search_enddate");

			String sql="select tel_reg_num,applicationer,status as dostatus,contract_code,"
					+" old_phone,new_phone,status,apply_date,apply_time,dtime from"
					+" (select *,(case when length(apply_time)=0 then concat(apply_date,'000000')"
					+" when length(apply_time)=1 then concat(apply_date,'00000',apply_time)"
					+" when length(apply_time)=2 then concat(apply_date,'0000',apply_time)"
					+" when length(apply_time)=3 then concat(apply_date,'000',apply_time)"
					+" when length(apply_time)=4 then concat(apply_date,'00',apply_time)"
					+" when length(apply_time)=5 then concat(apply_date,'0',apply_time)"
					+" when length(apply_time)>=6 then concat(apply_date,apply_time) end )as dtime "
					+" from gw_modify_tel_reg)a where 1=1 ";
			if(applicationer!=null && !"".equals(applicationer.toString())){
				sql+=" and applicationer like '%$applicationer$%' ";
			}
			if(contract_code!=null && !"".equals(contract_code.toString())){
				sql+=" and contract_code='$contract_code$' ";
			}
			if(search_status!=null && !"".equals(search_status.toString()) && !"0".equals(search_status.toString())){
				sql+=" and status='$search_status$' ";
			}
			if(search_startdate!=null && !"".equals(search_startdate.toString())){
				search_startdate=formatDateForKongjian(search_startdate);
				sql+=" and time>'$search_startdate$' ";
			}
			if(search_enddate!=null && !"".equals(search_enddate.toString())){
				search_enddate=formatDateForKongjian(search_enddate);
				sql+=" and time<'$search_enddate$' ";
			}
			sql+=" order by dtime desc";
			inputMap.put("applicationer", applicationer);
			inputMap.put("contract_code", contract_code);
			inputMap.put("search_status", search_status);
			inputMap.put("search_startdate", search_startdate);
			inputMap.put("search_enddate", search_enddate);
			
			sql=SqlManager.getSql(sql, inputMap);
			log.info("执行sql: "+sql);
			retList=jdbc.queryForList(sql);
			if(retList==null || retList.size()<=0){
				log.info("查询联系方式变更记录为空");
				retMap.put("errcode", "0001");
				retMap.put("errmsg", "未找到符合当前查询的记录。");
				retList.add(retMap);
				return retList;
			}
			log.info("查询联系方式变更记录条数:"+retList.size());
			return retList;
		}catch(Exception e){
			log.error("查询联系方式变更记录异常");
			retMap.put("errcode", "0001");
			retMap.put("errmsg", "查询联系方式变更记录异常，请稍后再试");
			retList.add(retMap);
			return retList;
		}
	}
	
	public String formatDateForKongjian(String datetime){
		if(datetime==null||datetime==""){
			return null;
		}
		datetime=datetime.replace(" ", "");
		datetime=datetime.replaceAll("-", "");
		datetime=datetime.replaceAll(":", "");
		return datetime;
	}
	
}
