package com.gwm.outsideinterface;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.common.SpringUtil;
import com.gwm.engine.Util;
import com.gwm.outsideinterface.service.AccountSettingService;
import com.gwm.outsideinterface.service.BindAccountService;
import com.gwm.outsideinterface.service.GetContractInfo;
import com.gwm.outsideinterface.service.MyLoanService;
import com.gwm.outsideinterface.service.PaymentHistoryService;
import com.gwm.outsideinterface.service.PersonalInfoService;
import com.gwm.outsideinterface.service.QueryShopService;
import com.gwm.outsideinterface.service.SRepaymentService;

@RestController
@RequestMapping(value="/outsideInterface")
@Description("外部接口")
public class OutsideInterfaceController {
	
	Logger log = LoggerFactory.getLogger(OutsideInterfaceController.class);
	
	@Autowired
	private GetContractInfo service=null;
	@RequestMapping(value="/getContractInfo")
	@Description("获取合同信息")
	public Object getContractInfo(){
		Object obj=null;
		log.info("获取合同信息");
		HttpServletRequest request=SpringUtil.request();
		String openid=request.getParameter("openid");
		log.info("openid:"+openid);
		if(openid==null||openid.equals("undifend")||openid.equals("")){
			return Util.returnMap("9999", "手机号为空");
		}
		try{
			obj=service.getContractInfo(openid);
			return obj;
		}catch(Exception ex){
			log.error("获取合同信息异常", ex);
			return Util.returnMap("9999", "获取合同信息异常");
		}
	}
	
	@RequestMapping(value="/changecontractinfo")
	@Description("修改绑定合同信息")
	public Object changeContractInfo(){
		log.info("---------------修改合同信息 开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		log.info("修改绑定合同信息msgMap:"+msgMap);
		Object obj = service.changeContractInfo(msgMap);
		log.info("---------------修改合同信息 结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/getcurrentcontract")
	@Description("获取当前绑定合同信息")
	public Object getCurrentContract(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		log.info("获取当前绑定合同信息msgMap:"+msgMap);
		Object obj = service.getBindContractInfo(msgMap);
		return obj;
	}
	
	@RequestMapping(value="/updateReplayVoucherPath")
	@Description("更新凭证路径")
	public Object updateVoucherPath(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		log.info("更新凭证路径msgMap:"+msgMap);
		Object obj = service.updateReplayVoucherPath(msgMap);
		return obj;
	}
	
	@Autowired
	private QueryShopService queryShopService = null;
	@RequestMapping(value="/queryshopinfo")
	@Description("查询经销商信息")
	public Object getShopInfo()throws Exception{
		log.info("***************查询经销商信息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		if("site".equals(msgMap.get("source"))){//来源是官网时要处理跨域问题
			if(!StringUtils.isEmpty(msgMap.get("province"))){
				msgMap.put("province", com.gwm.module.loanmanager.LoanManagerController.decodeBase64(msgMap.get("province"), 2));
			}
			if(!StringUtils.isEmpty(msgMap.get("city"))){
				msgMap.put("city", com.gwm.module.loanmanager.LoanManagerController.decodeBase64(msgMap.get("city"), 2));
			}
		}
		log.info("查询经销商信息msgMap:"+msgMap);
		Object obj = queryShopService.queryShop(msgMap);
		String source = msgMap.get("source");
		ObjectMapper mapper = new ObjectMapper(); 
		if("site".equals(source)){//来源是官网时要处理跨域问题
			log.info("***************查询经销商信息 结束***************");
			return "successCallback("+mapper.writeValueAsString(obj)+")";
		}
		log.info("***************查询经销商信息 结束***************");
		return obj;
	}
	
	@RequestMapping(value="/queryshopinfobycar")
	@Description("根据车型查询经销商信息")
	public Object getShopInfoByCar(){
		log.info("---------------根据车型查询经销商 开始---------------");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		log.info("根据车型查询经销商信息");
		Object obj = queryShopService.queryShopByCar(msgMap);
		log.info("---------------根据车型查询经销商 结束---------------");
		return obj;
	}
	
	@RequestMapping(value="/updateshopinfo")
	@Description("更新经销商信息")
	public Object updateShopInfo(){
		try{
			log.info("---------------更新经销商信息 开始---------------");
			HttpServletRequest request = SpringUtil.request();
			Map<String, String> msgMap = Util.getParameters(request);
			log.info("根据车型查询经销商信息");
			Object obj = queryShopService.updateShopInfo(msgMap);
			log.info("---------------更新经销商信息 结束---------------");
			return obj;
		}catch(Exception ex){
			log.info("更新经销商信息异常", ex);
			return Util.returnMap("9999", "更新经销商信息异常");
		}
	}
	
	@Autowired
	private BindAccountService bindAccountService=null;
	@RequestMapping(value="/bindAccount")
	@Description("账号绑定")
	public Object bindAccount(){
		Object obj=null;
		log.info("账号绑定");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=bindAccountService.bindAccount(map);
			return obj;
		}catch(Exception ex){
			log.error("绑定账号失败", ex);
			return Util.returnMap("9999", "绑定账号失败");
		}
	}

	@RequestMapping(value="/toACSAuthentication")
	@Description("acs接口认证")
	public Object toACSAuthentication(){
		Object obj=null;
		log.info("ACS接口认证");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=bindAccountService.authenticationACS(map);
			return obj;
		}catch(Exception ex){
			log.error("认证失败", ex);
			return Util.returnMap("9999", "acs接口认证失败");
		}
	}

	@RequestMapping(value="/bindingStaff")
	@Description("员工账号绑定")
	public Object bindingStaff(){
		Object obj=null;
		log.info("内部员工绑定");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=bindAccountService.bindingStaff(map);
			return obj;
		}catch(Exception ex){
			log.error("绑定失败", ex);
			return Util.returnMap("9999", "内部员工绑定失败");
		}
	}
	
	@RequestMapping(value="/sendVerificationCode")
	@Description("发送手机验证码")
	public Object sendVerificationCode(){
		Object obj=null;
		log.info("发送手机验证码");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=bindAccountService.sendVerificationCode(map);
			return obj;
		}catch(Exception ex){
			log.error("发送手机验证码失败", ex);
			return Util.returnMap("9999", "发送手机验证码失败");
		}
	}
	
	@Autowired
	SRepaymentService sRepaymentService = null;
	@RequestMapping(value="/getearlyrepayinfo")
	@Description("获取提前还款信息")
	public Object getEarlyRepayinfo(){
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = Util.getParameters(request);
		log.info("获取提前还款信息msgMap:"+msgMap);
		Object obj = sRepaymentService.getEarlyPayment(msgMap);
		return obj;
	}
	@RequestMapping(value="/wxearlyrepaygetbank")
	@Description("提前还款获取银行卡号")
	public Object wxearlyrepaygetbank(){
		try{
			HttpServletRequest request = SpringUtil.request();
			Map<String, String> msgMap = Util.getParameters(request);
			log.info("提前还款获取银行卡号msgMap:"+msgMap);
			Object obj = sRepaymentService.wxearlyrepaygetbank(msgMap);
//			Object obj = sRepaymentService.earlyrepaymentapplycheck(msgMap);
			return obj;
		}catch(Exception ex){
			log.error("校验失败", ex);
			return Util.returnMap("9999", "提前还款校验异常，请使用提前还款申请记录查询功能查看申请结果");
		}
	}
	@RequestMapping(value="/wxearlyrepaycheck")
	@Description("提前还款验证")
	public Object wxearlyrepaycheck(){
		try{
			HttpServletRequest request = SpringUtil.request();
			Map<String, String> msgMap = Util.getParameters(request);
			log.info("提前还款验证msgMap:"+msgMap);
			Object obj = sRepaymentService.wxearlyrepaycheck(msgMap);
//			Object obj = sRepaymentService.earlyrepaymentapplycheck(msgMap);
			return obj;
		}catch(Exception ex){
			log.error("校验失败", ex);
			return Util.returnMap("9999", "提前还款校验异常，请使用提前还款申请记录查询功能查看申请结果");
		}
	}
	
	
	@RequestMapping(value="/earlyrepaymentapply")
	@Description("提前还款申请")
	public Object earlyRepayApply(){
		try{
			HttpServletRequest request = SpringUtil.request();
			Map<String, String> msgMap = Util.getParameters(request);
			log.info("提前还款申请msgMap:"+msgMap);
			Object obj = sRepaymentService.earlyPaymentApply(msgMap);
			return obj;
		}catch(Exception ex){
			log.error("申请失败", ex);
			return Util.returnMap("9999", "提前还款申请异常，请使用提前还款申请记录查询功能查看申请结果");
		}
	}
	
	@Autowired
	private PersonalInfoService personalInfoService=null;
	@RequestMapping(value="/personalInfo")
	@Description("获取个人信息")
	public Object personalInfo(){
		Object obj=null;
		log.info("获取个人信息");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=personalInfoService.personalInfo(map);
			return obj;
		}catch(Exception ex){
			log.error("获取个人信息失败", ex);
			return Util.returnMap("9999", "获取个人信息失败");
		}
	}
	
	@RequestMapping(value="/incomeChange")
	@Description("个人信息-收入范围更新")
	public Object incomeChange(){
		Object obj=null;
		log.info("收入范围更新");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=personalInfoService.incomeChange(map);
			return obj;
		}catch(Exception ex){
			log.error("收入范围更新失败", ex);
			return Util.returnMap("9999", "收入范围更新失败");
		}
	}
	
	@RequestMapping(value="/zoneChange")
	@Description("个人信息-地区更新")
	public Object zoneChange(){
		Object obj=null;
		log.info("地区更新");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=personalInfoService.zoneChange(map);
			return obj;
		}catch(Exception ex){
			log.error("地区更新失败", ex);
			return Util.returnMap("9999", "地区更新失败");
		}
	}
	
	@Autowired
	private PaymentHistoryService paymentHistoryService=null;
	@RequestMapping(value="/queryEarlyRepayApply")
	@Description("提前还款申请记录查询")
	public Object queryEarlyRepayApply(){
		Object obj=null;
		log.info("提前还款申请记录查询");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=paymentHistoryService.queryEarlyRepayApply(map);
			return obj;
		}catch(Exception ex){
			log.error("提前还款申请记录查询失败", ex);
			return Util.returnMap("9999", "提前还款申请记录查询失败");
		}
	}
	
	@RequestMapping(value="/queryRepayRecord")
	@Description("还款记录查询")
	public Object queryRepayRecord(){
		Object obj=null;
		log.info("还款记录查询");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=paymentHistoryService.queryRepayRecord(map);
			return obj;
		}catch(Exception ex){
			log.error("还款记录查询失败", ex);
			return Util.returnMap("9999", "还款记录查询失败");
		}
	}
	
	@Autowired
	private MyLoanService myLoanService=null;
	@RequestMapping(value="/getBindingContract")
	@Description("我的贷款-获取绑定合同")
	public Object getBindingContract(){
		Object obj=null;
		log.info("我的贷款-获取绑定合同");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=myLoanService.getBindingContract(map);
			return obj;
		}catch(Exception ex){
			log.error("获取绑定合同失败", ex);
			return Util.returnMap("9999", "获取绑定合同失败");
		}
	}
	
	@Autowired
	private AccountSettingService accountSettingService=null;
	@RequestMapping(value="/removeBindAccount")
	@Description("账号设置-解绑账号")
	public Object removeBindAccount(){
		Object obj=null;
		log.info("解绑账号");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=accountSettingService.removeBindAccount(map);
			return obj;
		}catch(Exception ex){
			log.error("解绑账号失败", ex);
			return Util.returnMap("9999", "解绑账号失败");
		}
	}
	
	@RequestMapping(value="/feedBack")
	@Description("账号设置-意见反馈")
	public Object feedBack(){
		Object obj=null;
		log.info("意见反馈");
		HttpServletRequest request=SpringUtil.request();
		Map<String, String> map = Util.getParameters(request);
		try{
			obj=accountSettingService.feedBack(map);
			return obj;
		}catch(Exception ex){
			log.error("提交意见反馈失败", ex);
			return Util.returnMap("9999", "提交意见反馈失败");
		}
	}
	
}
