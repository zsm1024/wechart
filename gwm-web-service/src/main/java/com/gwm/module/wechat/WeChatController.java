package com.gwm.module.wechat;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gwm.common.SpringUtil;
import com.gwm.module.wechat.service.GlobalCodeService;
import com.gwm.module.wechat.service.HomeService;
import com.gwm.module.wechat.service.SChangeContactService;
import com.gwm.module.wechat.service.SLoanApplyService;
import com.gwm.module.wechat.service.SMessageService;
import com.gwm.module.wechat.service.TimerTaskService;
import com.gwm.module.wechat.service.WxCCSInterfaceService;
import com.gwm.module.wechat.service.WxMenuService;
import com.gwm.module.wechat.service.WxMsgLogService;
import com.gwm.module.wechat.service.WxUpsideService;
import com.gwm.module.wechat.service.WxUserInfoService;

/**
 * 微信模块
 * @author tianming
 *
 */
@RestController
@RequestMapping(value="/wechat")
@Description("微信")
public class WeChatController {
	
	Logger log = LoggerFactory.getLogger(WeChatController.class);
	
	@Autowired
	private WxMsgLogService logService = null;
	
	@RequestMapping(value="/upsidemsglog")
	@Description("记录微信上行消息日志")
	public Object upsideMsgLog(){
		log.info("---------------记录上行消息日志  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj = logService.saveUpsideMsgLog(msgMap);
		log.info("---------------记录上行消息日志  结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/downsidemsglog")
	@Description("记录微信下行消息日志")
	public Object downsideMsgLog(){
		log.info("---------------记录下行消息日志  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object obj = logService.saveDownsideMsgLog(msgMap);
		log.info("---------------记录下行消息日志  结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/templatemsglog")
	@Description("记录微信模板消息日志")
	public Object templateMsgLog(){
		log.info("---------------记录模板消息日志  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("记录微信模板消息日志："+msgMap);
		Object obj = logService.saveTemplateMsgLog(msgMap);
		log.info("---------------记录模板消息日志  结束---------------");
		return obj;
	}
	
	@Autowired
	WxUpsideService wxUpsideService = null;
	
	@RequestMapping(value="/subscribe")
	@Description("微信用户关注")
	public Object userSubscribe(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		return wxUpsideService.userSubscribe(msgMap);
	}
	
	@RequestMapping(value="/unsubscribe")
	@Description("微信用户取消关注")
	public Object userUnSubscribe(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		return wxUpsideService.userUnSubscribe(msgMap);
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
	
	/**
	 * 
	 * */
	@Autowired
	private GlobalCodeService globalcodeservice=null;
	@RequestMapping(value="/replaceAccessInfo")
	@Description("更新access信息")
	public int replaceAccessInfo(){
		return globalcodeservice.replaceAccessInfo();
	}
	

	@Autowired
	private TimerTaskService timertaskservice=null;
	@RequestMapping(value="/getTimerTaskList")
	@Description("获取定时任务列表")
	public void getTimerTaskList()
	{
		timertaskservice.getTimerTaskList();
	}

	@Autowired
	private WxMenuService wxMenuService = null;
	@RequestMapping(value="/getmenubyparentid")
	@Description("根据菜单的father_menu_id获取菜单")
	public Object getMenuByParentId(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		return wxMenuService.getMenuByParentId(msgMap.get("father_menu_id"));
	}
	
	@Autowired
	private SChangeContactService sChangeContactService = null;
	@RequestMapping(value="/getcheckcode")
	@Description("生成并发送验证码")
	public Object getCheckCode(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("生成并发送验证码:"+msgMap);
		return sChangeContactService.getCheckCode(msgMap);
	}
	
	@RequestMapping(value="/changecontactmode")
	@Description("更改联系方式")
	public Object changeContactMode(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("更改联系方式:"+msgMap);
		return sChangeContactService.changeContactMode(msgMap);
	}
	
	@RequestMapping(value="/getUserLoanInfo")
	@Description("查询绑定信息")
	public Object getUserLoanInfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询绑定信息:"+msgMap);
		return sChangeContactService.getUserLoanInfo(msgMap);
	}
	
	@Autowired
	SMessageService sMessageService = null;
	@RequestMapping(value="/getmymsg")
	@Description("获取我的信息")
	public Object getMyMsg(){
		log.info("---------------查询我的消息  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取我的信息:"+msgMap);
		Object obj = sMessageService.getMsg(msgMap);
		log.info("---------------查询我的消息  结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/getmymsgtype")
	@Description("获取我的信息类型")
	public Object getMyMsgType(){
		log.info("---------------查询我的消息类型  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取我的信息类型:"+msgMap);
		Object obj = sMessageService.getMsgType(msgMap);
		log.info("---------------查询我的消息类型  结束---------------");
		return obj;
	}
	
	@Autowired
	SLoanApplyService sLoanApplyService = null;
	@RequestMapping(value="/getloanapplycode")
	@Description("获取贷款申请验证码")
	public Object getLoanApplyCode(){
		log.info("---------------获取贷款申请验证码  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取贷款申请验证码:"+msgMap);
		Object obj = sLoanApplyService.getApplyCode(msgMap);
		log.info("---------------获取贷款申请验证码  结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/loanapplysubmit")
	@Description("贷款申请")
	public Object LoanApply(){
		log.info("---------------贷款申请  开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("贷款申请:"+msgMap);
		Object obj = sLoanApplyService.loanApply(msgMap);
		log.info("---------------贷款申请  结束---------------");
		return obj;
	}
	
	@Autowired
	HomeService homwService = null;
	@RequestMapping(value="/messageNotify")
	@Description("获取新消息通知")
	public Object messageNotify(){
		log.info("---------------获取新消息通知 开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取新消息通知:"+msgMap);
		Object obj = homwService.messageNotify(msgMap);
		log.info("---------------获取新消息通知  结束---------------");
		return obj;
	}
	
	@Autowired
	WxUserInfoService wxUserInfoService = null;
	@RequestMapping(value="/getWxUserInfo")
	@Description("获取微信用户信息")
	public Object getWxUserInfo(){
		log.info("---------------获取微信用户信息 开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("openid:"+msgMap);
		Object obj = wxUserInfoService.getWxUserInfo(msgMap);
		log.info("---------------获取微信用户信息  结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/updateWxUserInfo")
	@Description("更新微信用户信息")
	public Object updateWxUserInfo(){
		log.info("---------------更新微信用户信息开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("更新微信用户信息:"+msgMap);
		Object obj = wxUserInfoService.updateWxUserInfo(msgMap);
		log.info("---------------更新微信用户信息  结束---------------");
		return obj;
	}
	@Autowired
	WxCCSInterfaceService wxCCSInterfaceService = null;
	@RequestMapping(value="/online")
	@Description("online")
	public List<Map<String,Object>> online(){
		log.info("---------------判断微信客户是否CCS在线开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("判断是否CCS在线:"+msgMap);
		List<Map<String,Object>> obj = wxCCSInterfaceService.onLine();
		log.info("---------------判断微信客户是否CCS在线开始---------------");
		return obj;
	}
	@RequestMapping(value="/isVip")
	@Description("isVip")
	public Object isVip(){
		log.info("---------------判断微信客户是否VIP开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("判断是否VIP:"+msgMap);
		Object obj = wxCCSInterfaceService.isVip(msgMap);
		log.info("---------------判断微信客户是否VIP结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/sendRabbitMQ")
	@Description("sendRabbitMQ")
	public Object sendRabbitMQ(){
		log.info("---------------发送MQ消息开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("发送MQ消息:"+msgMap);
		Object obj = wxCCSInterfaceService.sendRabbitMQ(msgMap);
		log.info("---------------发送MQ消息结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/SetCustomerOnLine")
	@Description("SetCustomerOnLine")
	public Object SetCustomerOnLine(){
		log.info("---------------设置客户CCS在线开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("用户:"+msgMap);
		Object obj = wxCCSInterfaceService.SetCustomerOnLine(msgMap);
		log.info("---------------设置客户CCS在线结束---------------");
		return obj;
	}
}
