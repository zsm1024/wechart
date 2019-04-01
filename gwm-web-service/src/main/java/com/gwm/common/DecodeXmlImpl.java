package com.gwm.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;

import net.sf.json.xml.XMLSerializer;  

public class DecodeXmlImpl {
	public static Map<String, Object> decode(String xml) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		XMLSerializer xmlSerializer = new XMLSerializer(); 
//		xml="<?xml version='1.0' encoding='utf-8'?><root><application_phone>18864802010</application_phone><application_id_card>370902195301202131</application_id_card><contracts><contract><contract_id>310</contract_id><contract_nbr>GW100154</contract_nbr><business_partner_name>徐太昌</business_partner_name><business_partner_nbr>370902195301202131</business_partner_nbr><phone_nbr>18864802010</phone_nbr><asset_brand>哈弗H6 升级版 2014款</asset_brand><financial_product>等额本息标准产品</financial_product><financed_amt>70000</financed_amt><contract_term>36</contract_term><start_date>2014-09-22</start_date><end_date>2017-09-22</end_date><contract_status>L</contract_status><overdue>0</overdue><account_nbr>6222021604011864731</account_nbr><overdue_amt>0</overdue_amt></contract><contract><contract_id>310</contract_id><contract_nbr>GW100154</contract_nbr><business_partner_name>徐太昌</business_partner_name><business_partner_nbr>370902195301202131</business_partner_nbr><phone_nbr>18864802010</phone_nbr><asset_brand>哈弗H6 升级版 2014款</asset_brand><financial_product>等额本息标准产品</financial_product><financed_amt>70000</financed_amt><contract_term>36</contract_term><start_date>2014-09-22</start_date><end_date>2017-09-22</end_date><contract_status>L</contract_status><overdue>0</overdue><account_nbr>6222021604011864731</account_nbr><overdue_amt>0</overdue_amt></contract></contracts></root>";
		JSON json = xmlSerializer.read(xml);
		map=JSONObject.fromObject(json.toString());
//		String s=map.get("contracts").toString();
//		if(s.startsWith("[")){
//		List list=JSONArray.parseArray(s);//.parseObject(s);//.fromObject(s);
//		System.out.println("3333333333:"+list.toString());
//		}
//		else if(s.startsWith("{")){
//			Map<String, Object> map2=JSONObject.fromObject(s);
//			System.out.println("44444444:"+map2.toString());
//		}
//		System.out.println(json.toString());
		return map;
	}
}
