package com.gwm.common.service;

import java.util.Map;
/**
 * 工具自动生成请勿修改
 * @author kaifa1
 * @date 2018年05月30日
 */
public class Service {
	private static ServiceInvoker invoker = new ServiceInvoker();
	/**
	 *功能描述：none
	 */
	public static Object findAll(Map<String,String> params) {
		return invoker.invokeService("/distributor/findAll",params);
	}
	/**
	 *功能描述：none
	 */
	public static Object findAll() {
		return findAll(null);
	}
	/**
	 *功能描述：贷款管理-在线申请
	 */
	public static Object apply(Map<String,String> params) {
		return invoker.invokeService("/loan/apply",params);
	}
	/**
	 *功能描述：贷款管理-在线申请
	 */
	public static Object apply() {
		return apply(null);
	}
	/**
	 *功能描述：贷款管理-获取车辆信息图片
	 */
	public static Object getCarPic(Map<String,String> params) {
		return invoker.invokeService("/loan/getCarPic",params);
	}
	/**
	 *功能描述：贷款管理-获取车辆信息图片
	 */
	public static Object getCarPic() {
		return getCarPic(null);
	}
	/**
	 *功能描述：贷款管理-车型对应经销商查询
	 */
	public static Object getShopInfo_gw(Map<String,String> params) {
		return invoker.invokeService("/loan/getShopInfo",params);
	}
	/**
	 *功能描述：贷款管理-车型对应经销商查询
	 */
	public static Object getShopInfo_gw() {
		return getShopInfo_gw(null);
	}
	/**
	 *功能描述：贷款管理-官网手机验证码发送
	 */
	public static Object sendCodeToPhone(Map<String,String> params) {
		return invoker.invokeService("/loan/sendCodeToPhone",params);
	}
	/**
	 *功能描述：贷款管理-官网手机验证码发送
	 */
	public static Object sendCodeToPhone() {
		return sendCodeToPhone(null);
	}
	/**
	 *功能描述：贷款管理-获取本机IP
	 */
	public static Object getIp(Map<String,String> params) {
		return invoker.invokeService("/loan/getIp",params);
	}
	/**
	 *功能描述：贷款管理-获取本机IP
	 */
	public static Object getIp() {
		return getIp(null);
	}
	/**
	 *功能描述：贷款管理-我的专属方案
	 */
	public static Object getProgrammeForMe(Map<String,String> params) {
		return invoker.invokeService("/loan/programmeforme",params);
	}
	/**
	 *功能描述：贷款管理-我的专属方案
	 */
	public static Object getProgrammeForMe() {
		return getProgrammeForMe(null);
	}
	/**
	 *功能描述：贷款管理-单页面有效性验证
	 */
	public static Object checkUser(Map<String,String> params) {
		return invoker.invokeService("/manager/checkUser",params);
	}
	/**
	 *功能描述：贷款管理-单页面有效性验证
	 */
	public static Object checkUser() {
		return checkUser(null);
	}
	/**
	 *功能描述：贷款管理-验证用户登录有效性
	 */
	public static Object verification(Map<String,String> params) {
		return invoker.invokeService("/manager/verification",params);
	}
	/**
	 *功能描述：贷款管理-验证用户登录有效性
	 */
	public static Object verification() {
		return verification(null);
	}
	/**
	 *功能描述：贷款管理-导入热销车型excel
	 */
	public static Object importExcel(Map<String,String> params) {
		return invoker.invokeService("/manager/importExcel",params);
	}
	/**
	 *功能描述：贷款管理-导入热销车型excel
	 */
	public static Object importExcel() {
		return importExcel(null);
	}
	/**
	 *功能描述：贷款管理-导出热销车型excel
	 */
	public static Object exportHotCarInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/exportHotCarInfo",params);
	}
	/**
	 *功能描述：贷款管理-导出热销车型excel
	 */
	public static Object exportHotCarInfo() {
		return exportHotCarInfo(null);
	}
	/**
	 *功能描述：贷款管理-查询热销车型信息
	 */
	public static Object selectHotCarInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/selectHotCarInfo",params);
	}
	/**
	 *功能描述：贷款管理-查询热销车型信息
	 */
	public static Object selectHotCarInfo() {
		return selectHotCarInfo(null);
	}
	/**
	 *功能描述：贷款管理-导出excel查询
	 */
	public static Object exportApplyInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/exportApplyInfo",params);
	}
	/**
	 *功能描述：贷款管理-导出excel查询
	 */
	public static Object exportApplyInfo() {
		return exportApplyInfo(null);
	}
	/**
	 *功能描述：贷款管理-查询在线申请记录
	 */
	public static Object selectApplyInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/selectApplyInfo",params);
	}
	/**
	 *功能描述：贷款管理-查询在线申请记录
	 */
	public static Object selectApplyInfo() {
		return selectApplyInfo(null);
	}
	/**
	 *功能描述：贷款管理-更新在线申请记录状态
	 */
	public static Object updateApplyInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/updateApplyInfo",params);
	}
	/**
	 *功能描述：贷款管理-更新在线申请记录状态
	 */
	public static Object updateApplyInfo() {
		return updateApplyInfo(null);
	}
	/**
	 *功能描述：贷款管理-查询意见反馈记录
	 */
	public static Object selectFBManager(Map<String,String> params) {
		return invoker.invokeService("/manager/selectFBManager",params);
	}
	/**
	 *功能描述：贷款管理-查询意见反馈记录
	 */
	public static Object selectFBManager() {
		return selectFBManager(null);
	}
	/**
	 *功能描述：贷款管理-更新意见反馈状态
	 */
	public static Object updateFBManager(Map<String,String> params) {
		return invoker.invokeService("/manager/updateFBManager",params);
	}
	/**
	 *功能描述：贷款管理-更新意见反馈状态
	 */
	public static Object updateFBManager() {
		return updateFBManager(null);
	}
	/**
	 *功能描述：贷款管理-导出意见反馈excel
	 */
	public static Object exportFBManager(Map<String,String> params) {
		return invoker.invokeService("/manager/exportFBManager",params);
	}
	/**
	 *功能描述：贷款管理-导出意见反馈excel
	 */
	public static Object exportFBManager() {
		return exportFBManager(null);
	}
	/**
	 *功能描述：贷款管理-查询消息内容管理
	 */
	public static Object selectMsgContent(Map<String,String> params) {
		return invoker.invokeService("/manager/selectMsgContent",params);
	}
	/**
	 *功能描述：贷款管理-查询消息内容管理
	 */
	public static Object selectMsgContent() {
		return selectMsgContent(null);
	}
	/**
	 *功能描述：贷款管理-更新消息内容管理
	 */
	public static Object updateMsgContent(Map<String,String> params) {
		return invoker.invokeService("/manager/updateMsgContent",params);
	}
	/**
	 *功能描述：贷款管理-更新消息内容管理
	 */
	public static Object updateMsgContent() {
		return updateMsgContent(null);
	}
	/**
	 *功能描述：贷款管理-新增消息内容管理
	 */
	public static Object addMsgContent(Map<String,String> params) {
		return invoker.invokeService("/manager/addMsgContent",params);
	}
	/**
	 *功能描述：贷款管理-新增消息内容管理
	 */
	public static Object addMsgContent() {
		return addMsgContent(null);
	}
	/**
	 *功能描述：贷款管理-删除消息内容管理
	 */
	public static Object delMsgContent(Map<String,String> params) {
		return invoker.invokeService("/manager/delMsgContent",params);
	}
	/**
	 *功能描述：贷款管理-删除消息内容管理
	 */
	public static Object delMsgContent() {
		return delMsgContent(null);
	}
	/**
	 *功能描述：贷款管理-当前合同查询提前还款信息
	 */
	public static Object earlyrepayinfoForSub(Map<String,String> params) {
		return invoker.invokeService("/manager/earlyrepayinfoForSub",params);
	}
	/**
	 *功能描述：贷款管理-当前合同查询提前还款信息
	 */
	public static Object earlyrepayinfoForSub() {
		return earlyrepayinfoForSub(null);
	}
	/**
	 *功能描述：贷款管理-查询提前还款信息
	 */
	public static Object mQueryEarlyRepayInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/earlyrepayinfo",params);
	}
	/**
	 *功能描述：贷款管理-查询提前还款信息
	 */
	public static Object mQueryEarlyRepayInfo() {
		return mQueryEarlyRepayInfo(null);
	}
	/**
	 *功能描述：贷款管理-处理提前还款信息
	 */
	public static Object mDealEarlyRepayInfo(Map<String,String> params) {
		return invoker.invokeService("/manager/dealearlyrepayinfo",params);
	}
	/**
	 *功能描述：贷款管理-处理提前还款信息
	 */
	public static Object mDealEarlyRepayInfo() {
		return mDealEarlyRepayInfo(null);
	}
	/**
	 *功能描述：贷款管理-当前用户查询意见反馈记录
	 */
	public static Object selectFBManagerForSub(Map<String,String> params) {
		return invoker.invokeService("/manager/selectFBManagerForSub",params);
	}
	/**
	 *功能描述：贷款管理-当前用户查询意见反馈记录
	 */
	public static Object selectFBManagerForSub() {
		return selectFBManagerForSub(null);
	}
	/**
	 *功能描述：贷款管理-查询当前合同联系方式变更记录
	 */
	public static Object selectContactChangeForSub(Map<String,String> params) {
		return invoker.invokeService("/manager/selectContactChangeForSub",params);
	}
	/**
	 *功能描述：贷款管理-查询当前合同联系方式变更记录
	 */
	public static Object selectContactChangeForSub() {
		return selectContactChangeForSub(null);
	}
	/**
	 *功能描述：贷款管理-查询联系方式变更记录
	 */
	public static Object selectContactChange(Map<String,String> params) {
		return invoker.invokeService("/manager/selectContactChange",params);
	}
	/**
	 *功能描述：贷款管理-查询联系方式变更记录
	 */
	public static Object selectContactChange() {
		return selectContactChange(null);
	}
	/**
	 *功能描述：贷款管理-更新联系方式变更记录状态
	 */
	public static Object updateContactChange(Map<String,String> params) {
		return invoker.invokeService("/manager/updateContactChange",params);
	}
	/**
	 *功能描述：贷款管理-更新联系方式变更记录状态
	 */
	public static Object updateContactChange() {
		return updateContactChange(null);
	}
	/**
	 *功能描述：贷款管理-导出联系方式变更记录excel
	 */
	public static Object exportContactChange(Map<String,String> params) {
		return invoker.invokeService("/manager/exportContactChange",params);
	}
	/**
	 *功能描述：贷款管理-导出联系方式变更记录excel
	 */
	public static Object exportContactChange() {
		return exportContactChange(null);
	}
	/**
	 *功能描述：微信-SetCustomerOnLine
	 */
	public static Object SetCustomerOnLine(Map<String,String> params) {
		return invoker.invokeService("/wechat/SetCustomerOnLine",params);
	}
	/**
	 *功能描述：微信-SetCustomerOnLine
	 */
	public static Object SetCustomerOnLine() {
		return SetCustomerOnLine(null);
	}
	/**
	 *功能描述：微信-根据菜单的father_menu_id获取菜单
	 */
	public static Object getMenuByParentId(Map<String,String> params) {
		return invoker.invokeService("/wechat/getmenubyparentid",params);
	}
	/**
	 *功能描述：微信-根据菜单的father_menu_id获取菜单
	 */
	public static Object getMenuByParentId() {
		return getMenuByParentId(null);
	}
	/**
	 *功能描述：微信-更新微信用户信息
	 */
	public static Object updateWxUserInfo(Map<String,String> params) {
		return invoker.invokeService("/wechat/updateWxUserInfo",params);
	}
	/**
	 *功能描述：微信-更新微信用户信息
	 */
	public static Object updateWxUserInfo() {
		return updateWxUserInfo(null);
	}
	/**
	 *功能描述：微信-记录微信上行消息日志
	 */
	public static Object upsideMsgLog(Map<String,String> params) {
		return invoker.invokeService("/wechat/upsidemsglog",params);
	}
	/**
	 *功能描述：微信-记录微信上行消息日志
	 */
	public static Object upsideMsgLog() {
		return upsideMsgLog(null);
	}
	/**
	 *功能描述：微信-记录微信模板消息日志
	 */
	public static Object templateMsgLog(Map<String,String> params) {
		return invoker.invokeService("/wechat/templatemsglog",params);
	}
	/**
	 *功能描述：微信-记录微信模板消息日志
	 */
	public static Object templateMsgLog() {
		return templateMsgLog(null);
	}
	/**
	 *功能描述：微信-记录微信下行消息日志
	 */
	public static Object downsideMsgLog(Map<String,String> params) {
		return invoker.invokeService("/wechat/downsidemsglog",params);
	}
	/**
	 *功能描述：微信-记录微信下行消息日志
	 */
	public static Object downsideMsgLog() {
		return downsideMsgLog(null);
	}
	/**
	 *功能描述：微信-获取我的信息
	 */
	public static Object getMyMsg(Map<String,String> params) {
		return invoker.invokeService("/wechat/getmymsg",params);
	}
	/**
	 *功能描述：微信-获取我的信息
	 */
	public static Object getMyMsg() {
		return getMyMsg(null);
	}
	/**
	 *功能描述：微信-获取我的信息类型
	 */
	public static Object getMyMsgType(Map<String,String> params) {
		return invoker.invokeService("/wechat/getmymsgtype",params);
	}
	/**
	 *功能描述：微信-获取我的信息类型
	 */
	public static Object getMyMsgType() {
		return getMyMsgType(null);
	}
	/**
	 *功能描述：微信-获取贷款申请验证码
	 */
	public static Object getLoanApplyCode(Map<String,String> params) {
		return invoker.invokeService("/wechat/getloanapplycode",params);
	}
	/**
	 *功能描述：微信-获取贷款申请验证码
	 */
	public static Object getLoanApplyCode() {
		return getLoanApplyCode(null);
	}
	/**
	 *功能描述：微信-贷款申请
	 */
	public static Object LoanApply(Map<String,String> params) {
		return invoker.invokeService("/wechat/loanapplysubmit",params);
	}
	/**
	 *功能描述：微信-贷款申请
	 */
	public static Object LoanApply() {
		return LoanApply(null);
	}
	/**
	 *功能描述：微信-online
	 */
	public static Object online(Map<String,String> params) {
		return invoker.invokeService("/wechat/online",params);
	}
	/**
	 *功能描述：微信-online
	 */
	public static Object online() {
		return online(null);
	}
	/**
	 *功能描述：微信-更新access信息
	 */
	public static Object replaceAccessInfo(Map<String,String> params) {
		return invoker.invokeService("/wechat/replaceAccessInfo",params);
	}
	/**
	 *功能描述：微信-更新access信息
	 */
	public static Object replaceAccessInfo() {
		return replaceAccessInfo(null);
	}
	/**
	 *功能描述：微信-更改联系方式
	 */
	public static Object changeContactMode(Map<String,String> params) {
		return invoker.invokeService("/wechat/changecontactmode",params);
	}
	/**
	 *功能描述：微信-更改联系方式
	 */
	public static Object changeContactMode() {
		return changeContactMode(null);
	}
	/**
	 *功能描述：微信-生成并发送验证码
	 */
	public static Object getCheckCode(Map<String,String> params) {
		return invoker.invokeService("/wechat/getcheckcode",params);
	}
	/**
	 *功能描述：微信-生成并发送验证码
	 */
	public static Object getCheckCode() {
		return getCheckCode(null);
	}
	/**
	 *功能描述：微信-获取新消息通知
	 */
	public static Object messageNotify(Map<String,String> params) {
		return invoker.invokeService("/wechat/messageNotify",params);
	}
	/**
	 *功能描述：微信-获取新消息通知
	 */
	public static Object messageNotify() {
		return messageNotify(null);
	}
	/**
	 *功能描述：微信-查询绑定信息
	 */
	public static Object getUserLoanInfo(Map<String,String> params) {
		return invoker.invokeService("/wechat/getUserLoanInfo",params);
	}
	/**
	 *功能描述：微信-查询绑定信息
	 */
	public static Object getUserLoanInfo() {
		return getUserLoanInfo(null);
	}
	/**
	 *功能描述：微信-获取定时任务列表
	 */
	public static Object getTimerTaskList(Map<String,String> params) {
		return invoker.invokeService("/wechat/getTimerTaskList",params);
	}
	/**
	 *功能描述：微信-获取定时任务列表
	 */
	public static Object getTimerTaskList() {
		return getTimerTaskList(null);
	}
	/**
	 *功能描述：微信-isVip
	 */
	public static Object isVip(Map<String,String> params) {
		return invoker.invokeService("/wechat/isVip",params);
	}
	/**
	 *功能描述：微信-isVip
	 */
	public static Object isVip() {
		return isVip(null);
	}
	/**
	 *功能描述：微信-sendRabbitMQ
	 */
	public static Object sendRabbitMQ(Map<String,String> params) {
		return invoker.invokeService("/wechat/sendRabbitMQ",params);
	}
	/**
	 *功能描述：微信-sendRabbitMQ
	 */
	public static Object sendRabbitMQ() {
		return sendRabbitMQ(null);
	}
	/**
	 *功能描述：微信-微信用户关注
	 */
	public static Object userSubscribe(Map<String,String> params) {
		return invoker.invokeService("/wechat/subscribe",params);
	}
	/**
	 *功能描述：微信-微信用户关注
	 */
	public static Object userSubscribe() {
		return userSubscribe(null);
	}
	/**
	 *功能描述：微信-微信用户取消关注
	 */
	public static Object userUnSubscribe(Map<String,String> params) {
		return invoker.invokeService("/wechat/unsubscribe",params);
	}
	/**
	 *功能描述：微信-微信用户取消关注
	 */
	public static Object userUnSubscribe() {
		return userUnSubscribe(null);
	}
	/**
	 *功能描述：微信-获取微信用户信息
	 */
	public static Object getWxUserInfo(Map<String,String> params) {
		return invoker.invokeService("/wechat/getWxUserInfo",params);
	}
	/**
	 *功能描述：微信-获取微信用户信息
	 */
	public static Object getWxUserInfo() {
		return getWxUserInfo(null);
	}
	/**
	 *功能描述：外部接口-修改绑定合同信息
	 */
	public static Object changeContractInfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/changecontractinfo",params);
	}
	/**
	 *功能描述：外部接口-修改绑定合同信息
	 */
	public static Object changeContractInfo() {
		return changeContractInfo(null);
	}
	/**
	 *功能描述：外部接口-获取当前绑定合同信息
	 */
	public static Object getCurrentContract(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/getcurrentcontract",params);
	}
	/**
	 *功能描述：外部接口-获取当前绑定合同信息
	 */
	public static Object getCurrentContract() {
		return getCurrentContract(null);
	}
	/**
	 *功能描述：外部接口-更新凭证路径
	 */
	public static Object updateVoucherPath(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/updateReplayVoucherPath",params);
	}
	/**
	 *功能描述：外部接口-更新凭证路径
	 */
	public static Object updateVoucherPath() {
		return updateVoucherPath(null);
	}
	/**
	 *功能描述：外部接口-acs接口认证
	 */
	public static Object toACSAuthentication(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/toACSAuthentication",params);
	}
	/**
	 *功能描述：外部接口-acs接口认证
	 */
	public static Object toACSAuthentication() {
		return toACSAuthentication(null);
	}
	/**
	 *功能描述：外部接口-发送手机验证码
	 */
	public static Object sendVerificationCode(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/sendVerificationCode",params);
	}
	/**
	 *功能描述：外部接口-发送手机验证码
	 */
	public static Object sendVerificationCode() {
		return sendVerificationCode(null);
	}
	/**
	 *功能描述：外部接口-获取提前还款信息
	 */
	public static Object getEarlyRepayinfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/getearlyrepayinfo",params);
	}
	/**
	 *功能描述：外部接口-获取提前还款信息
	 */
	public static Object getEarlyRepayinfo() {
		return getEarlyRepayinfo(null);
	}
	/**
	 *功能描述：外部接口-提前还款获取银行卡号
	 */
	public static Object wxearlyrepaygetbank(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/wxearlyrepaygetbank",params);
	}
	/**
	 *功能描述：外部接口-提前还款获取银行卡号
	 */
	public static Object wxearlyrepaygetbank() {
		return wxearlyrepaygetbank(null);
	}
	/**
	 *功能描述：外部接口-提前还款验证
	 */
	public static Object wxearlyrepaycheck(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/wxearlyrepaycheck",params);
	}
	/**
	 *功能描述：外部接口-提前还款验证
	 */
	public static Object wxearlyrepaycheck() {
		return wxearlyrepaycheck(null);
	}
	/**
	 *功能描述：外部接口-提前还款申请记录查询
	 */
	public static Object queryEarlyRepayApply(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/queryEarlyRepayApply",params);
	}
	/**
	 *功能描述：外部接口-提前还款申请记录查询
	 */
	public static Object queryEarlyRepayApply() {
		return queryEarlyRepayApply(null);
	}
	/**
	 *功能描述：外部接口-我的贷款-获取绑定合同
	 */
	public static Object getBindingContract(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/getBindingContract",params);
	}
	/**
	 *功能描述：外部接口-我的贷款-获取绑定合同
	 */
	public static Object getBindingContract() {
		return getBindingContract(null);
	}
	/**
	 *功能描述：外部接口-账号设置-解绑账号
	 */
	public static Object removeBindAccount(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/removeBindAccount",params);
	}
	/**
	 *功能描述：外部接口-账号设置-解绑账号
	 */
	public static Object removeBindAccount() {
		return removeBindAccount(null);
	}
	/**
	 *功能描述：外部接口-获取合同信息
	 */
	public static Object getContractInfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/getContractInfo",params);
	}
	/**
	 *功能描述：外部接口-获取合同信息
	 */
	public static Object getContractInfo() {
		return getContractInfo(null);
	}
	/**
	 *功能描述：外部接口-查询经销商信息
	 */
	public static Object getShopInfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/queryshopinfo",params);
	}
	/**
	 *功能描述：外部接口-查询经销商信息
	 */
	public static Object getShopInfo() {
		return getShopInfo(null);
	}
	/**
	 *功能描述：外部接口-根据车型查询经销商信息
	 */
	public static Object getShopInfoByCar(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/queryshopinfobycar",params);
	}
	/**
	 *功能描述：外部接口-根据车型查询经销商信息
	 */
	public static Object getShopInfoByCar() {
		return getShopInfoByCar(null);
	}
	/**
	 *功能描述：外部接口-更新经销商信息
	 */
	public static Object updateShopInfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/updateshopinfo",params);
	}
	/**
	 *功能描述：外部接口-更新经销商信息
	 */
	public static Object updateShopInfo() {
		return updateShopInfo(null);
	}
	/**
	 *功能描述：外部接口-账号绑定
	 */
	public static Object bindAccount(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/bindAccount",params);
	}
	/**
	 *功能描述：外部接口-账号绑定
	 */
	public static Object bindAccount() {
		return bindAccount(null);
	}
	/**
	 *功能描述：外部接口-员工账号绑定
	 */
	public static Object bindingStaff(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/bindingStaff",params);
	}
	/**
	 *功能描述：外部接口-员工账号绑定
	 */
	public static Object bindStaff(Map<String, String> map) {
		return bindingStaff(map);
	}
	/**
	 *功能描述：外部接口-提前还款申请
	 */
	public static Object earlyRepayApply(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/earlyrepaymentapply",params);
	}
	/**
	 *功能描述：外部接口-提前还款申请
	 */
	public static Object earlyRepayApply() {
		return earlyRepayApply(null);
	}
	/**
	 *功能描述：外部接口-获取个人信息
	 */
	public static Object personalInfo(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/personalInfo",params);
	}
	/**
	 *功能描述：外部接口-获取个人信息
	 */
	public static Object personalInfo() {
		return personalInfo(null);
	}
	/**
	 *功能描述：外部接口-个人信息-收入范围更新
	 */
	public static Object incomeChange(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/incomeChange",params);
	}
	/**
	 *功能描述：外部接口-个人信息-收入范围更新
	 */
	public static Object incomeChange() {
		return incomeChange(null);
	}
	/**
	 *功能描述：外部接口-个人信息-地区更新
	 */
	public static Object zoneChange(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/zoneChange",params);
	}
	/**
	 *功能描述：外部接口-个人信息-地区更新
	 */
	public static Object zoneChange() {
		return zoneChange(null);
	}
	/**
	 *功能描述：外部接口-还款记录查询
	 */
	public static Object queryRepayRecord(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/queryRepayRecord",params);
	}
	/**
	 *功能描述：外部接口-还款记录查询
	 */
	public static Object queryRepayRecord() {
		return queryRepayRecord(null);
	}
	/**
	 *功能描述：外部接口-账号设置-意见反馈
	 */
	public static Object feedBack(Map<String,String> params) {
		return invoker.invokeService("/outsideInterface/feedBack",params);
	}
	/**
	 *功能描述：外部接口-账号设置-意见反馈
	 */
	public static Object feedBack() {
		return feedBack(null);
	}
}