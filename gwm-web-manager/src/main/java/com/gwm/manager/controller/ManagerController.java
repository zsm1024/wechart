package com.gwm.manager.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gwm.common.SpringUtil;
import com.gwm.manager.service.ContactChangeService;
import com.gwm.manager.service.FeedBackService;
import com.gwm.manager.service.MsgContentService;
import com.gwm.manager.service.RepaymentService;

@RestController
public class ManagerController {
	
	static Logger log = LoggerFactory.getLogger(ManagerController.class);
	/**
	 * 查询意见反馈
	 * */
	@Autowired
	private FeedBackService fbService = null;
	
	
	@RequestMapping(value="/selectFBManagerForSub")
	public Object selectFBManagerForSub(){
		log.info("***************查询当前用户意见反馈 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=fbService.selectFBManagerForSub(msgMap);	
		log.info("查询当前用户意见反馈:"+msgMap);
		log.info("***************查询当前用户意见反馈 结束***************");
		return result;
	}
	
	@RequestMapping(value="/selectFBManager")
	public Object selectFBManager(){
		log.info("***************查询意见反馈 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=fbService.selectFBManager(msgMap);	
		log.info("意见反馈信息:"+msgMap);
		log.info("***************查询意见反馈 结束***************");
		return result;
	}
	
	/**
	 * 导出意见反馈excel
	 * */
	@RequestMapping(value="/exportFBManager")
	public void exportFBManager(HttpServletResponse response){
		log.info("***************导出意见反馈excel 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		fbService.exportFBManager(response,msgMap);	
		log.info("导出意见反馈excel:"+msgMap);
		log.info("***************导出意见反馈excel 结束***************");
	}
	
	/**
	 * 更新意见反馈状态
	 * */
	@RequestMapping(value="/updateFBManager")
	public Object updateFBManager(){
		log.info("***************更新意见反馈状态 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=fbService.updateFBManager(msgMap);	
		log.info("更新意见反馈状态:"+msgMap);
		log.info("***************更新意见反馈状态 结束***************");
		return result;
	}
	
	
	
	/**
	 * 查询联系方式变更记录申请
	 * */
	@Autowired
	private ContactChangeService ccService = null;
	
	@RequestMapping(value="/selectContactChangeForSub")
	public Object selectContactChangeForSub(){
		log.info("***************查询当前合同联系方式变更申请 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=ccService.selectContactChangeForSub(msgMap);	
		log.info("查询联系方式变更申请记录:"+msgMap);
		log.info("***************查询当前合同联系方式变更申请结束***************");
		return result;
	}
	@RequestMapping(value="/selectContactChange")
	public Object selectContactChange(){
		log.info("***************查询联系方式变更申请 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=ccService.selectContactChange(msgMap);	
		log.info("查询联系方式变更申请记录:"+msgMap);
		log.info("***************查询联系方式变更申请 结束***************");
		return result;
	}
	
	/**
	 * 更新联系方式变更记录状态
	 * */
	@RequestMapping(value="/updateContactChange")
	public Object updateContactChange(){
		log.info("***************更新联系方式变更记录状态 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=ccService.updateContactChange(msgMap);	
		log.info("更新联系方式变更记录状态:"+msgMap);
		log.info("***************更新联系方式变更记录状态 结束***************");
		return result;
	}
	
	/**
	 * 导出联系方式变更记录excel
	 * */
	@RequestMapping(value="/exportContactChange")
	public void exportContactChange(HttpServletResponse response){
		log.info("***************导出联系方式变更记录excel 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		ccService.exportContactChange(response, msgMap);	
		log.info("导出联系方式变更记录excel:"+msgMap);
		log.info("***************导出联系方式变更记录excel 结束***************");
	}
	
	/**
	 * 查询消息内容管理
	 * */
	@Autowired
	private MsgContentService mcService = null;
	@RequestMapping(value="/selectMsgContent")
	public Object selectMsgContent(){
		log.info("***************查询消息内容管理 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=mcService.selectMsgContent(msgMap);	
		log.info("查询消息内容管理记录:"+msgMap);
		log.info("***************查询消息内容管理结束***************");
		return result;
	}
	
	/**
	 * 更新消息内容管理
	 * */
	@RequestMapping(value="/updateMsgContent")
	public Object updateMsgContent(){
		log.info("***************更新消息内容管理 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=mcService.updateMsgContent(msgMap);	
		log.info("更新消息内容管理:"+msgMap);
		log.info("***************更新消息内容管理 结束***************");
		return result;
	}
	
	/**
	 * 删除消息内容管理
	 * */
	@RequestMapping(value="/delMsgContent")
	public Object delMsgContent(){
		log.info("***************删除消息内容管理 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=mcService.delMsgContent(msgMap);	
		log.info("删除消息内容管理:"+msgMap);
		log.info("***************删除消息内容管理 结束***************");
		return result;
	}
	
	/**
	 * 新增消息内容管理
	 * */
	@RequestMapping(value="/addMsgContent")
	public Object addMsgContent(){
		log.info("***************新增消息内容管理 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=mcService.addMsgContent(msgMap);	
		log.info("新增消息内容管理:"+msgMap);
		log.info("***************新增消息内容管理 结束***************");
		return result;
	}
	
	
	/**
	 * 查询提前还款申请记录
	 */
	@Autowired
	RepaymentService repaymentService = null;
	@RequestMapping(value="/earlyrepayinfoForSub")
	public Object earlyrepayinfoForSub(){
		log.info("***************当前合同提前还款申请查询  开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj=repaymentService.earlyrepayinfoForSub(msgMap);	
		log.info("当前合同提前还款申请查询:"+msgMap);
		log.info("***************当前合同提前还款申请查询  结束***************");
		return obj;
	}
	@RequestMapping(value="/mgetrepayinfo")
	public Object getEarlyPaymentInfo(){
		log.info("***************提前还款申请查询  开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj=repaymentService.getRepaymentInfo(msgMap);	
		log.info("提前还款申请查询:"+msgMap);
		log.info("***************提前还款申请查询  结束***************");
		return obj;
	}
	/**
	 * 处理提前还款申请
	 * @return
	 */
	@RequestMapping(value="/mdealrepayinfo")
	public Object dealEarlyRepaymentInfo(){
		log.info("***************处理提前还款申请  开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj=repaymentService.dealRepayment(msgMap);	
		log.info("处理提前还款申请:"+msgMap);
		log.info("***************处理提前还款申请  结束***************");
		return obj;
	}
	/**
	 * 导出提前还款excel
	 * */
	@RequestMapping(value="/exportearlyrepayinfo")
	public void exportEarlyRepayInfo(HttpServletResponse response){
		log.info("***************导出提前还款excel 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("导出提前还款excel:"+msgMap);
		repaymentService.exportEarlyRepayInfo(response,msgMap);	
		log.info("***************导出提前还款excel 结束***************");
	}
	
	/**
	 * 取得请求参数(key=value)
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
	        	log.debug("请求参数：" + paramName + "=" + paramValue);
	        	if(map.containsKey(paramName)){
	        		log.warn("参数 {}值将被覆盖,原值:{},新值:{}",paramName,map.get(paramName),paramValue);
	        	}
	        	map.put(paramName, paramValue);  
	        }
	      }  
	    }
	    return map;
	}
}