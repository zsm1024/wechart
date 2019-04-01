package com.gwm.outsideinterface.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.gwm.common.RedisDao;
import com.gwm.common.SqlManager;
import com.gwm.db.dao.Dao;
import com.gwm.db.dao.condition.Cnd;
import com.gwm.db.entity.Gw_shop;
import com.gwm.db.entity.Gw_turn_car_model;
import com.gwm.db.entity.meta.Gw_turn_car_modelMeta;
import com.gwm.engine.Util;

import cn.gwm.www.BrandEnum;
import cn.gwm.www.DDCServiceSoapProxy;
import cn.gwm.www.EnResponse_Shop;
import cn.gwm.www.User;

@Service
public class QueryShopService {
	
	@Autowired
	Dao dao = null;
	
	@Autowired
	RedisDao redis = null;
	
	@Autowired
	JdbcTemplate jdbc = null;
	
	static Logger log = LoggerFactory.getLogger(QueryShopService.class);
	
	@Transactional
	public Map<String, String> queryShop(Map<String, String> map){
		try{
			Map<String, String> sqlMap = new HashMap<String, String>();
			String province = map.get("province");
			String city = map.get("city");
			if(!StringUtils.isEmpty(province)&&!"请选择".equals(province)&&!"undefined".equals(province)){
				sqlMap.put("sh_province", province.trim());
			}
			if(!StringUtils.isEmpty(city)&&!"请选择".equals(city)&&!"undefined".equals(city)){
				sqlMap.put("sh_city", city.trim());
			}
			String oriSql = "select * from gw_shop where 1=1 and sh_state='正常' and sh_financecoorp='1' "
					+ "{and sh_province like '%$sh_province$%'} "
					+ "{and sh_city like '%$sh_city$%'}";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("sql:"+sql);
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("shops", JSON.toJSONString(list));
			log.info("查询结果："+retMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		}catch(Exception ex){
			log.error("查询经销商信息异常", ex);
			return Util.returnMap("9999", "未查到经销商信息");
		}
	}
	
	@Transactional
	public Map<String, String> queryShopByCar(Map<String, String> map){
		try{
			String model = map.get("model");
			if(StringUtils.isEmpty(model)||"请选择".equals(model)||"undefined".equals(model)){
				log.info("参数为空model");
				return Util.returnMap("9999", "查询失败");
			}
			Map<String, String> sqlMap = new HashMap<String, String>();
			//转换车型
			Gw_turn_car_model modelEntity = null;
			modelEntity = dao.fetch(Gw_turn_car_model.class, Cnd.where(Gw_turn_car_modelMeta.ifc_car_model.eq(model)));
			sqlMap.put("model", modelEntity.getShop_car_model());
			//查询经销商信息
			String oriSql = "select * from gw_shop where sh_state='正常' and sh_financecoorp='1' and sh_salecarbrand like '%$model$%'";
			String sql = SqlManager.getSql(oriSql, sqlMap);
			log.info("sql:"+sql);
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("shops", JSON.toJSONString(list));
			log.info("查询结果："+retMap);
			return Util.returnMap("0", "查询成功", Util.mapObjectToString(retMap));
		}catch(Exception ex){
			log.error("查询经销商信息异常", ex);
			return Util.returnMap("9999", "未查到经销商信息");
		}
	}
	
	public Map<String, String> updateShopInfo(Map<String, String> param) throws Exception{
		try{
			String url = redis.getString("SHOPWSDL_URL");
			log.info("经销商wsdl url:"+url);
			DDCServiceSoapProxy ddc = new DDCServiceSoapProxy(url);
			User u=new User();
			u.setUserName(redis.getString("SHOPWSDL_USERNAME"));
			u.setPassword(redis.getString("SHOPWSDL_PASSWORD"));
			u.setBrand(BrandEnum.ALL);
			
			EnResponse_Shop enList[] = ddc.queryShop(u);
			log.info("更新经销商信息条数size:"+enList.length);
			Gw_shop shopEntity = null;
			dao.delete(Gw_shop.class, new Cnd());
			int num = 0;
			for(EnResponse_Shop en : enList){
				num++;
				shopEntity = new Gw_shop();
				shopEntity.setSh_id(en.getSh_ID()+"");
				shopEntity.setSh_number(en.getSh_number());
				shopEntity.setSh_name(en.getSh_name());
				shopEntity.setSh_shortname(en.getSh_shortName());
				shopEntity.setSh_state(en.getSh_state());
				shopEntity.setSh_province(en.getSh_province());
				shopEntity.setSh_city(en.getSh_city());
				shopEntity.setSh_county(en.getSh_county());
				shopEntity.setSh_address(en.getSh_address());
				shopEntity.setAgentbrands(en.getAgentBrands());
				shopEntity.setSh_salehotline(en.getSh_saleHotline());
				shopEntity.setSh_saletime(en.getSh_saleHours());
				shopEntity.setSh_salelongitude(en.getSh_salelongitude()+"");
				shopEntity.setSh_salelatitude(en.getSh_salelatitude()+"");
				shopEntity.setSh_financecoorp(en.getSh_financeCoorp());
				shopEntity.setSh_financecoorpdate(en.getSh_financeCoorpDate());
				shopEntity.setSh_financecoorpenddate(en.getSh_financeCoorpEndDate());
				shopEntity.setSh_salecarbrand(en.getSh_saleCarBrand());
				shopEntity.setSh_createtime(en.getSh_createTime());
				shopEntity.setSh_lastupdatetime(en.getSh_lastUpdateTime());
				dao.insert(shopEntity);
				if(num % 100 == 0){
					log.info("已更新"+num+"条");
				}
			}
			log.info("更新经销商信息完成");
			return Util.returnMap("0", "更新经销商信息成功");
		}catch(Exception ex){
			log.error("更新经销商信息异常", ex);
			throw ex;
		}
	}
	
	public static void main(String args[]){
		try {
			String url = "http://10.255.16.157:5678/DDC/DDCService.asmx";
			DDCServiceSoapProxy ddc = new DDCServiceSoapProxy(url);
			User u=new User();
			u.setUserName("TGBAFC");
			u.setPassword("tgbafc12345");
			u.setBrand(BrandEnum.ALL);
			
			EnResponse_Shop enList[] = ddc.queryShop(u);
			//EnResponse_Shop enList[] = ddc.queryShop("CCJR");
			int i = 1;
			System.out.println("size:"+enList.length);
			for(EnResponse_Shop en : enList){
				en.getSh_ID();
				en.getSh_number();
				en.getSh_name();
				en.getSh_shortName();
				en.getSh_state();
				en.getSh_province();
				en.getSh_city();
				en.getSh_county();
				en.getSh_address();
				en.getAgentBrands();
				en.getSh_saleHotline();
				en.getSh_salelongitude();
				en.getSh_salelatitude();
				en.getSh_financeCoorp();
				en.getSh_createTime();
				en.getSh_lastUpdateTime();
//				System.out.println("***************"+i+"***************");
//				System.out.println("专营店ID:"+en.getSh_ID());
//				System.out.println("专营店号:"+en.getSh_number());
//				System.out.println("专营店名称:"+en.getSh_name());
//				System.out.println("专营店简称:"+en.getSh_shortName());
//				System.out.println("销售状态:"+en.getSh_state());
//				System.out.println("省份:"+en.getSh_province());
//				System.out.println("城市:"+en.getSh_city());
//				System.out.println("区县:"+en.getSh_county());
//				System.out.println("销售店地址:"+en.getSh_address());
//				System.out.println("代理品牌:"+en.getAgentBrands());
//				System.out.println("销售热线:"+en.getSh_saleHotline());
//				System.out.println("销售店经度:"+en.getSh_salelongitude());
//				System.out.println("销售店纬度:"+en.getSh_salelatitude());
//				System.out.println("金融公司合作:"+en.getSh_financeCoorp());
//				System.out.println("创建时间:"+en.getSh_createTime());
//				System.out.println("上次更新时间:"+en.getSh_lastUpdateTime());
//				i++;
				String sql = "insert into gw_shop("
						+"sh_id"
						+",sh_number"
						+",sh_name"
						+",sh_shortname"
						+",sh_state"
						+",sh_province"
						+",sh_city"
						+",sh_county"
						+",sh_address"
						+",agentbrands"
						+",sh_salehotline"
						+",sh_saletime"
						+",sh_salelongitude"
						+",sh_salelatitude"
						+",sh_financecoorp"
						+",sh_financecoorpdate"
						+",sh_financecoorpenddate"
						+",sh_salecarbrand"
						+",sh_createtime"
						+",sh_lastupdatetime) values("
						+"'"+en.getSh_ID()+"'"
						+",'"+en.getSh_number()+"'"
						+",'"+en.getSh_name()+"'"
						+",'"+en.getSh_shortName()+"'"
						+",'"+en.getSh_state()+"'"
						+",'"+en.getSh_province()+"'"
						+",'"+en.getSh_city()+"'"
						+",'"+en.getSh_county()+"'"
						+",'"+en.getSh_address()+"'"
						+",'"+en.getAgentBrands()+"'"
						+",'"+en.getSh_saleHotline()+"'"
						+",'"+en.getSh_saleHours()+"'"
						+",'"+en.getSh_salelongitude()+"'"
						+",'"+en.getSh_salelatitude()+"'"
						+",'"+en.getSh_financeCoorp()+"'"
						+",'"+en.getSh_financeCoorpDate()+"'"
						+",'"+en.getSh_financeCoorpEndDate()+"'"
						+",'"+en.getSh_saleCarBrand()+"'"
						+",'"+en.getSh_createTime()+"'"
						+",'"+en.getSh_lastUpdateTime()+"');";
				System.out.println(sql);
			}
			
		} catch (Exception ex) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			ex.printStackTrace();
		}
	}
}
