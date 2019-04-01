package com.gwm.outsideinterface.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_user_info;
import com.gwm.db.entity.Gw_user_loan_rel;
import com.gwm.db.entity.meta.Gw_user_infoMeta;
import com.gwm.db.entity.meta.Gw_user_loan_relMeta;
import com.gwm.engine.Util;
@Service
public class PersonalInfoService {
	
	Logger log = LoggerFactory.getLogger(PersonalInfoService.class);
	
	@Autowired
	Dao dao = null;
	@Autowired
	JdbcTemplate jdbc = null;
	
	/**
	 * 获取个人信息
	 * @param map
	 * @return
	 */
	public Map<String, String> personalInfo(Map<String, String> map){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<String,Object> inputMap = new HashMap<String, Object>();
		log.info("开始获取个人信息");
		String openId = map.get("openId");//openId
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取个人信息失败");
		}
		//获取openid下的个人信息
		try {
			Gw_user_loan_rel bindEntity = null;
			bindEntity = dao.fetch(Gw_user_loan_rel.class, Cnd
					.where(Gw_user_loan_relMeta.openid.eq(openId))
					.and(Gw_user_loan_relMeta.status.eq("2")));
			if(bindEntity == null){
				log.info("没有获取到该openid的个人信息，请确认是否绑定");
				return Util.returnMap("9999", "没有获取到该账户的个人信息，请确认是否绑定");
			}
			Gw_user_info infoEntity = null;
			infoEntity = dao.fetch(Gw_user_info.class, Cnd
					.where(Gw_user_infoMeta.openid.eq(openId)));
			if(infoEntity == null){
				log.info("没有获取到该openid的个人信息，请确认是否绑定");
				return Util.returnMap("9999", "没有获取到该账户的个人信息，请确认是否绑定");
			}
			String _idno = infoEntity.getUser_id();
			String age = "";
			String sex = "";
			String nowDate = Util.formatDate(System.currentTimeMillis());
			if(_idno.length() == 18){
				String nowYear = nowDate.substring(0, 4);
				String userYear = _idno.substring(6, 10);
				age = (Integer.parseInt(nowYear) - Integer.parseInt(userYear))+"";
				String userSex = _idno.substring(16, 17);
				if(Integer.parseInt(userSex)%2 == 1){
					sex = "男";
				}else{
					sex = "女";
				}
			}
			if(!age.equals(infoEntity.getUser_age()) || !sex.equals(infoEntity.getUser_sex())){
				log.info("更新信息 年龄:"+age);
				log.info("更新信息 性别:"+sex);
				infoEntity.setUser_age(age);
				infoEntity.setUser_sex(sex);
				dao.update(infoEntity, Cnd
						.where(Gw_user_infoMeta.openid.eq(openId)));
			}
			String phone = infoEntity.getUser_phone();
			String idno = infoEntity.getUser_id();
			String name = infoEntity.getUser_name();
			int length;
			if(!StringUtils.isEmpty(phone)){
				phone = Util.addMaskStr(phone, 3, 2);
				infoEntity.setUser_phone(phone);
			}
			if(!StringUtils.isEmpty(idno)){
				idno = Util.addMaskStr(idno, 6, 4);
				infoEntity.setUser_id(idno);
			}
			if(!StringUtils.isEmpty(name)){
				name = Util.addMaskStr(name, 1, 0);
				infoEntity.setUser_name(name);
			}
			returnMap = JSONObject.fromObject(infoEntity);
		} catch (Exception e) {
			// TODO: handle exception
			return Util.returnMap("9999", e.getMessage());
		}
		log.info("获取个人信息成功");
		return Util.returnMap("0","获取个人信息成功",Util.mapObjectToString(returnMap));
	}
	
	/**
	 * 个人信息-收入范围更新
	 * @param map
	 * @return
	 */
	public Map<String, String> incomeChange(Map<String, String> map)throws Exception{
		log.info("开始收入范围更新");
		String openId = map.get("openId");//openId
		String income = map.get("income");//openId
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "收入范围更新失败");
		}
		if(StringUtils.isEmpty(income)){
			log.error("income为空");
			return Util.returnMap("9999", "收入范围更新失败");
		}
		
		try {
			Gw_user_info queryEntity=null;
			queryEntity=dao.fetch(Gw_user_info.class, Cnd.where(Gw_user_infoMeta.openid.eq(openId)));
			if(queryEntity==null){
				log.info("没有获取到该openid的个人信息，请确认是否绑定");
				return Util.returnMap("9999", "没有获取到该账户的个人信息，请确认是否绑定");
			}
			else{
				queryEntity.setIncome(income);
				dao.update(queryEntity, Cnd.where(Gw_user_infoMeta.openid.eq(openId)));
				log.info("收入范围更新成功");
				return Util.returnMap("0", "收入范围更新成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}
	
	/**
	 * 个人信息-地区更新
	 * @param map
	 * @return
	 */
	public Map<String, String> zoneChange(Map<String, String> map)throws Exception{
		log.info("开始地区更新");
		String openId = map.get("openId");//openId
		String select_city = map.get("select_city");//openId
		
		if(StringUtils.isEmpty(openId)){
			log.error("openId为空");
			return Util.returnMap("9999", "获取用户信息失败");
		}
		if(StringUtils.isEmpty(select_city)){
			log.error("select_city为空");
			return Util.returnMap("9999", "地区更新失败");
		}
		try {
			Gw_user_info queryEntity=null;
			queryEntity=dao.fetch(Gw_user_info.class, Cnd.where(Gw_user_infoMeta.openid.eq(openId)));
			if(queryEntity==null){
				log.info("没有获取到该openid的个人信息，请确认是否绑定");
				return Util.returnMap("9999", "没有获取到该账户的个人信息，请确认是否绑定");
			}
			else{
				queryEntity.setUser_zone(select_city);
				dao.update(queryEntity, Cnd.where(Gw_user_infoMeta.openid.eq(openId)));
				log.info("地区更新成功");
				return Util.returnMap("0", "地区更新成功");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}