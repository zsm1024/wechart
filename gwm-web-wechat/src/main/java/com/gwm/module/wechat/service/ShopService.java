package com.gwm.module.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wx.engine.Util;

@Service
public class ShopService{
	
	Logger log = LoggerFactory.getLogger(ShopService.class);
	
	public Map<String, String> queryShopByCar(Map<String, String> param){
		try{
			String model = param.get("model");
			log.info("车型model:"+model);
			if(StringUtils.isEmpty(model)){
				log.info("车型为空");
				return Util.returnMap("9999", "查询失败");
			}
			Object obj = com.gwm.common.service.Service.getShopInfoByCar();
			String retStr = (String)obj;
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			log.info("service服务返回:"+retMap);
			return Util.mapObjectToString(retMap);
		}catch(Exception ex){
			log.info("查询经销商信息异常", ex);
			return Util.returnMap("9999", "查询失败");
		}
	}
	
	/**
	 * 根据城市查询经销商信息
	 * @param param
	 * @return
	 */
	public Map<String, String> queryShop(Map<String, String> param){
		String city = param.get("city");
		String lat = param.get("lat");
		String lng = param.get("lng");
		log.info("查询经销商城市："+city);
		log.info("我的位置 维度:"+lat);
		log.info("为的位置 经度:"+lng);
		if(StringUtils.isEmpty(city)||"undefined".equals(city)){
			log.info("城市为空");
			return Util.returnMap("9999", "请选择城市来查询经销商信息");
		}
		Object obj = com.gwm.common.service.Service.getShopInfo();
		String retJson = (String)obj;
		Map<String, Object> retMap = JSONObject.parseObject(retJson);
		log.info("查询经销商结果:"+retMap);
		if("0".equals(retMap.get("errcode"))){
			log.info("添加距离");
			if(StringUtils.isEmpty(lat)||"undefined".equals(lat)||StringUtils.isEmpty(lng)||"undefined".equals(lng)){
				log.info("我的位置为空，不需要计算位置");
			}else{
				String shopsJson = retMap.get("shops")+"";
				List<Object> retList = new ArrayList<Object>();
				List<Object> list = JSONArray.parseArray(shopsJson);
				log.info("原条数:"+list.size());
				double minDis = 0;
				int index = 0;
				for(int i = 0; i < list.size(); i++){
					String shopJson = list.get(i)+"";
					Map<String, Object> shopMap = JSONObject.parseObject(shopJson);
					String sh_lat = (String)shopMap.get("sh_salelatitude");
					String sh_lng = (String)shopMap.get("sh_salelongitude");
					if(StringUtils.isEmpty(sh_lng)||StringUtils.isEmpty(sh_lat)){
						log.info("经纬度为空，继续下一个:"+shopMap);
						shopMap.put("distance", "未知");
						retList.add(shopMap);
						continue;
					}
					log.info("开始计算距离："+shopMap);
					double distance = -1;
					try{
						distance = getDistance(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(sh_lat), Double.parseDouble(sh_lng));
						shopMap.put("distance", distance);
						if(minDis == 0){
							minDis = distance;
						}
						if(minDis > distance){
							minDis = distance;
							index = i;
						}
					}catch(Exception ex){
						log.info("计算距离异常", ex);
					}
					retList.add(shopMap);
				}
				log.info("添加位置信息后条数:"+retList.size());
				List<Object> disList = new ArrayList<Object>();
				Object disObj = retList.get(index);
				disList.add(disObj);
				for(int i = 0; i < retList.size(); i++){
					if(i == index){
						continue;
					}
					Object mapObj = retList.get(i);
					disList.add(mapObj);
				}
				retMap.put("shops", disList);
			}
		}
		return Util.mapObjectToString(retMap);
	}
	
	private double rad(double d) {
        return d * Math.PI / 180.0;
    }
	
	private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        System.out.println(lat1 + "," + lng1 + "," + lat2 + "," + lng2);
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        s = Math.round(s * 10000.0) / 10000.0;
        return s * 1000.0;
    }
}