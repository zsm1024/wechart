package com.gwm.module.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.ethz.ssh2.crypto.Base64;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.db.dao.Ipage;
import com.gwm.engine.HttpProcess;
import com.gwm.module.manager.service.ContactChangeService;
import com.gwm.module.manager.service.FeedBackManagerService;
import com.gwm.module.manager.service.ManagerService;
import com.gwm.module.manager.service.SMRepaymentService;
import com.gwm.module.manager.service.MsgContentService;

/**
 * 贷款管理
 * @author kaifa1
 *
 */
@RestController
@RequestMapping(value="/manager")
@Description("贷款管理")
public class ManagerController {
	private Logger logger = LoggerFactory.getLogger(ManagerController.class);
	@Autowired
	private ManagerService service = null;
	/**
	 * 单页面有效性验证
	 * */
	@RequestMapping(value="/checkUser")
	@Description("单页面有效性验证")
	public Object checkUser() throws Exception{

		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		List<Map<String,Object>>res_list=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		Map<String,String> check_map=new HashMap<String,String>();
		String wx_url=redisDao.getString("verification_url");
		try{
			check_map.put("SSOCookie", msgMap.get("ssouser"));
			String c_result=HttpProcess.postHttps(wx_url+"?access_token="+msgMap.get("access_token"), check_map);
			logger.info("获取哈弗商城校验接口返回信息："+c_result);
			JSONObject jsons=JSONObject.fromObject(c_result);
			String status=jsons.get("status").toString();
			if(!status.equals("1")){
				result_map.put("error", "0001");
				String tmp_code=jsons.get("status").toString();
				if(tmp_code.equals("2")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString());
				}else if(tmp_code.equals("20001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",参数为空");
				}else if(tmp_code.equals("-90001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",数据解码失败");
				}else if(tmp_code.equals("-90002")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",token加密失败");
				}else if(tmp_code.equals("10001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录，请重新登录");
				}else if(tmp_code.equals("10002")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录，请重新登录");
				}else if(tmp_code.equals("10003")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录");
				}
				logger.error("验证用户登录失败，请重新登录后尝试。");
				res_list.add(result_map);
				return	res_list;
			}
			result_map.put("error", "0000");
			result_map.put("error_msg", "验证用户登录有效性信息成功");
			logger.info("验证用户登录有效性信息成功");
			res_list.add(result_map);
			return	res_list;
		}catch(Exception e){
			logger.error("验证用户登录有效性信息异常",e);
			result_map.put("error", "0001");
			result_map.put("error_msg", "验证用户登录有效性信息异常"+e);
			res_list.add(result_map);
			return res_list;
		}
	
	}
	/**
	 * 验证用户登录有效性
	 * */
	@RequestMapping(value="/verification")
	@Description("验证用户登录有效性")
	public Object verification() throws Exception{
		RedisDao redisDao = SpringUtil.getBean(RedisDao.class);
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		List<Map<String,Object>>res_list=new ArrayList<Map<String,Object>>();
		Map<String,Object> result_map=new HashMap<String,Object>();
		
		
		Map<String,String> token_map=new HashMap<String,String>();
		Map<String,String> check_map=new HashMap<String,String>();
		//先获取token
		String wx_url=redisDao.getString("verification_url");
		String appId=redisDao.getString("hf_appid");//哈弗商城appid
		String appPwd=redisDao.getString("hf_passwd");//哈弗商城密码
		String token_url=redisDao.getString("hf_gettoken_url");//获得接口
		token_map.put("appid", appId);
		token_map.put("password", appPwd);
		token_map.put("grant_type", "get");
		String result="";
		try{
//			result="{'code':10000,'message':'success','access_token':'dT5y0vuIzO2gBZgApgbCzjuW9I6zlMASdP86muuaZp2cmem1ec','expire':7200}";
			result=HttpProcess.postHttps(token_url, token_map);
			logger.info("获取哈弗商城token接口返回信息："+result);
			JSONObject json=JSONObject.fromObject(result);
			String code=json.get("code").toString();
			if(!code.equals("10000")){
				result_map.put("error", "0001");
				result_map.put("error_msg", "哈弗商城获取token异常，请稍后重试。响应码："+code);
				logger.info("哈弗商城获取token异常，响应码："+code);
				res_list.add(result_map);
				return	res_list;
			}
			String acc_token=json.getString("access_token");
			if(acc_token==null||acc_token.equals("")){
				result_map.put("error", "0001");
				result_map.put("error_msg", "哈弗商城获取token失败");
				logger.error("哈弗商城获取token失败");
				res_list.add(result_map);
				return	res_list;
			}
			logger.info("获取哈弗商城token成功，token："+acc_token);
			JSONObject jd = new JSONObject();
			jd.put("SSOCookie", msgMap.get("ssouser"));
			check_map.put("SSOCookie", msgMap.get("ssouser"));
			String data = JSON.toJSONString(check_map);
//			String c_result=HttpProcess.post(wx_url+"?acc_token="+acc_token, data);
			String c_result=HttpProcess.postHttps(wx_url+"?access_token="+acc_token, check_map);
			logger.info("获取哈弗商城校验接口返回信息："+c_result);
//			String c_result="{status:1, message:'已登录', token:'dfsdfaksdgakglkaflkdgjr'}";
			JSONObject jsons=JSONObject.fromObject(c_result);
			String status=jsons.get("status").toString();
			if(!status.equals("1")){
				result_map.put("error", "0001");
				String tmp_code=jsons.get("status").toString();
				if(tmp_code.equals("2")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString());
				}else if(tmp_code.equals("20001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",参数为空");
				}else if(tmp_code.equals("90001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",数据解码失败");
				}else if(tmp_code.equals("90002")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",token加密失败");
				}else if(tmp_code.equals("10001")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录，请重新登录");
				}else if(tmp_code.equals("10002")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录，请重新登录");
				}else if(tmp_code.equals("10003")){
					result_map.put("error_msg", "验证用户登录失败，请重新登录后尝试。code:"+jsons.get("status").toString()+",未登录");
				}
				logger.error("验证用户登录失败，请重新登录后尝试。");
				res_list.add(result_map);
				return	res_list;
			}
			result_map.put("error", "0000");
			result_map.put("acc_token", acc_token);
			result_map.put("userid", jsons.get("userid").toString());
			res_list.add(result_map);
			return	res_list;
		}catch(Exception e){
			logger.error("验证用户登录有效性信息异常",e);
			result_map.put("error", "0001");
			result_map.put("error_msg", "验证用户登录有效性信息异常"+e);
			res_list.add(result_map);
			return res_list;
		}
	}
	/**
	 * 导入热销车型excel
	 * */
	@RequestMapping(value="/importExcel")
	@Description("导入热销车型excel")
	public Object importExcel() throws Exception{
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("导入热销车型记录:"+msgMap);
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		try{
			result=service.importExcel(msgMap);
			if(result.get(0).get("error").equals("0000")){
				return "<script>alert('导入热销车型成功');"
						+ "window.parent.document.getElementById('btn_select').click();"
						+ "</script>";
			}else{
				return "<script>alert('导入热销车型失败');</script>";
			}
			
		}catch(Exception e){
			Map<String,Object> result_map=new HashMap<String,Object>();
	    	result_map.put("error", "0001");
			result_map.put("error_msg", "导入失败");
			result.add(result_map);
			return result;
		}
	}
	/**
	 * 导出热销车型excel
	 * */
	@RequestMapping(value="/exportHotCarInfo")
	@Description("导出热销车型excel")
	public Object exportHotCarInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("导出在线申请记录:"+msgMap);
		return service.exportHotCarInfo(msgMap);
	}
	/**
	 * 查询热销车型信息
	 * */
	@RequestMapping(value="/selectHotCarInfo")
	@Description("查询热销车型信息")
	public Object selectHotCarInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询热销车型记录:"+msgMap);
		return service.selectHotCarInfo(msgMap);
	}
	/**
	 * 导出在线申请excel查询
	 * */
	@RequestMapping(value="/exportApplyInfo")
	@Description("导出excel查询")
	public Object exportApplyInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("导出在线申请记录:"+msgMap);
		return service.exportApplyInfo(msgMap);
	}
	/**
	 * 查询在线申请记录
	 * */
	@RequestMapping(value="/selectApplyInfo")
	@Description("查询在线申请记录")
	public Object selectApplyInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询在线申请记录:"+msgMap);
		return service.selectApplyInfo(msgMap);
	}
	/**
	 * 更新在线申请记录状态
	 * */
	@RequestMapping(value="/updateApplyInfo")
	@Description("更新在线申请记录状态")
	public Object updateApplyInfo(){
		HttpServletRequest request=SpringUtil.request();
		Map<String,String> msgMap=getParameters(request,null);
		logger.info("更新在线申请记录"+msgMap);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			list=service.updateApplyInfo(msgMap);
		}catch(Exception e){
			logger.error("****更新在线申请异常",e);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("error", "0001");
			map.put("error_msg", "更新在线申请出现异常！");
			list.add(map);
			return list;
		}
		return list;
	}
	
	@Autowired
	SMRepaymentService smrepaymentService = null;
	@RequestMapping(value="/earlyrepayinfoForSub")
	@Description("当前合同查询提前还款信息")
	public Object earlyrepayinfoForSub(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj = smrepaymentService.earlyrepayinfoForSub(msgMap);
		return obj;
	}
	@RequestMapping(value="/earlyrepayinfo")
	@Description("查询提前还款信息")
	public Object mQueryEarlyRepayInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj = smrepaymentService.queryRepaymentInfo(msgMap);
		return obj;
	}
	@RequestMapping(value="/dealearlyrepayinfo")
	@Description("处理提前还款信息")
	public Object mDealEarlyRepayInfo()throws Exception{
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj = smrepaymentService.dealRepayment(msgMap);
		return obj;
	}
	@Autowired
	private FeedBackManagerService fbManagerService = null;
	@RequestMapping(value="/selectFBManagerForSub")
	@Description("当前用户查询意见反馈记录")
	public Object selectFBManagerForSub(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询当前用户意见反馈记录:"+msgMap);
		return fbManagerService.selectFBManagerForSub(msgMap);
		
	}
	@RequestMapping(value="/selectFBManager")
	@Description("查询意见反馈记录")
	public Object selectFBManager(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询意见反馈记录:"+msgMap);
		return fbManagerService.selectFBManager(msgMap);
	}
	@RequestMapping(value="/updateFBManager")
	@Description("更新意见反馈状态")
	public Object updateFBManager(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("更新意见反馈状态:"+msgMap);
		return fbManagerService.updateFBManager(msgMap);
	}
	@RequestMapping(value="/exportFBManager")
	@Description("导出意见反馈excel")
	public Object exportFBManager(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("导出意见反馈excel:"+msgMap);
		return fbManagerService.exportFBManager(msgMap);
	}
	
	@Autowired
	private ContactChangeService ccService = null;
	@RequestMapping(value="/selectContactChangeForSub")
	@Description("查询当前合同联系方式变更记录")
	public Object selectContactChangeForSub(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询当前合同联系方式变更记录:"+msgMap);
		return ccService.selectContactChangeForSub(msgMap);
	}
	
	@RequestMapping(value="/selectContactChange")
	@Description("查询联系方式变更记录")
	public Object selectContactChange(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询联系方式变更记录:"+msgMap);
		return ccService.selectContactChange(msgMap);
	}
	@RequestMapping(value="/updateContactChange")
	@Description("更新联系方式变更记录状态")
	public Object updateContactChange(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("更新联系方式变更记录状态:"+msgMap);
		try{
			return ccService.updateContactChange(msgMap);
		}
		catch(Exception e){
			logger.error("联系方式变更异常",e);
			Map<String,String> map=new HashMap<String,String>();
			map.put("errcode", "0001");
			map.put("errmsg", "联系方式变更异常，请稍后重试");
			return map;
		}
	}
	@RequestMapping(value="/exportContactChange")
	@Description("导出联系方式变更记录excel")
	public Object exportContactChange(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("导出联系方式变更记录excel:"+msgMap);
		return ccService.exportContactChange(msgMap);
	}
	
	@Autowired
	private MsgContentService mcService = null;
	@RequestMapping(value="/selectMsgContent")
	@Description("查询消息内容管理")
	public Object selectMsgContent(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("查询消息内容管理记录:"+msgMap);
		return mcService.selectMsgContent(msgMap);
//		return "123";
	}
	@RequestMapping(value="/updateMsgContent")
	@Description("更新消息内容管理")
	public Object updateMsgContent(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("更新消息内容管理:"+msgMap);
		return mcService.updateMsgContent(msgMap);
	}
	@RequestMapping(value="/addMsgContent")
	@Description("新增消息内容管理")
	public Object addMsgContent(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("新增消息内容管理:"+msgMap);
		return mcService.addMsgContent(msgMap);
	}
	@RequestMapping(value="/delMsgContent")
	@Description("删除消息内容管理")
	public Object delMsgContent(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		logger.info("删除消息内容管理:"+msgMap);
		return mcService.delMsgContent(msgMap);
	}
	//对应提供解密N次  
    public String decodeBase64(String mi,int times){  
        int num=(times<=0)?1:times;  
        String mingwen="";  
        if(mi==null||mi.equals("")){  
        }else{  
            mingwen=mi;  
            for(int i=0;i<num;i++){  
                mingwen=decodeBase64(mingwen);  
            }  
        }  
        return mingwen;  
    }  
    public String decodeBase64(String mi){  
        String mingwen="";  
        if(mi==null||mi.equals("")){  
              
        }else{  
        	try {
        	mingwen = new String(Base64.decode(mi.toCharArray()), "UTF-8");
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return mingwen;  
    }  
    /**
     * 取得请求参数
     * @param request
     * @return
     */
    private Map<String,String> getParameters(HttpServletRequest request,Map<String,String> map){
    	if(map==null){
    		map = new HashMap<String,String>();
    	}
    	Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {  
          String paramName = (String) paramNames.nextElement();  
          String[] paramValues = request.getParameterValues(paramName);  
          if (paramValues.length == 1) {
            String paramValue = paramValues[0];  
            if (paramValue.length() != 0) {  
            	logger.debug("请求参数：" + paramName + "=" + paramValue);
            	if(map.containsKey(paramName)){
            		logger.warn("参数 {}值将被覆盖,原值:{},新值:{}",paramName,map.get(paramName),paramValue);
            	}
            	map.put(paramName, paramValue);  
            }
          }  
        }
        return map;
    }
}

