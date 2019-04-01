package com.gwm.module.wechat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gwm.common.SpringUtil;
import com.gwm.module.wechat.service.AccessTokenErrService;
import com.gwm.module.wechat.service.AccountSettingService;
import com.gwm.module.wechat.service.BindAccountService;
import com.gwm.module.wechat.service.ChangeContactService;
import com.gwm.module.wechat.service.ContractService;
import com.gwm.module.wechat.service.HomeService;
import com.gwm.module.wechat.service.LoanApplyService;
import com.gwm.module.wechat.service.MenuTempService;
import com.gwm.module.wechat.service.MessageService;
import com.gwm.module.wechat.service.MyLoanService;
import com.gwm.module.wechat.service.PaymentHistoryService;
import com.gwm.module.wechat.service.PersonalInfoService;
import com.gwm.module.wechat.service.RepaymentService;
import com.gwm.module.wechat.service.ShopService;
import com.gwm.module.wechat.service.WeChatService;
import com.gwm.module.wechat.service.WxAuthorizeService;
import com.gwm.module.wechat.service.WxJsapiTicketService;
import com.gwm.module.wechat.service.WxMsgDownsideService;
import com.wx.engine.Util;

@RestController
public class WeChatController {

	@Autowired
	WeChatService service = null;
	
	static Logger log = LoggerFactory.getLogger(WeChatController.class);
	
	/**
	 * 腾讯服务器入口
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/main")
	public Object TencentRequest() throws Exception{
		log.info("******************腾讯请求 开始******************");
		HttpServletRequest request = SpringUtil.request();
		
		//公众平台填写URL验证
        String echo = request.getParameter("echostr");
        if (echo != null) {
            log.info("echo:"+echo);
            return echo.getBytes("utf-8");
        }
        //将腾讯报文解析为map<String,String>形式
        InputStream is = request.getInputStream();
        InputStreamReader ir = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(ir);
        String buffer;
        StringBuilder buff = new StringBuilder();
        while ((buffer = br.readLine()) != null) {
            buff.append(buffer);
        }
        br.close();
        buffer=buff.toString();
        log.info("腾讯报文："+buffer);
        if(StringUtils.isEmpty(buffer)){
        	log.info("接收报文为空，直接返回");
        	return Util.returnMap("9999", "接收数据错误");
        }
        final HashMap<String,String> map = Util.getXMLMap(buff.toString());
        map.put("localAddr",request.getLocalAddr());
        final String xml=buff.toString();
        //启动线程处理微信报文
        new Thread(new Runnable(){
            public void run(){
            	log.info("******************启动线程处理微信报文 开始******************");
            	service.wxExecute(map,xml);
            	log.info("******************启动线程处理微信报文 结束******************");
            }
        }).start();
        log.info("******************腾讯请求 结束******************");
		return "";
	}
	
	/**
	 * 更新菜单请求
	 * @return
	 */
	@Autowired
	MenuTempService menuTempService = null;
	@RequestMapping(value="/updatewxmenu")
	public Object updateWxMenu(){
		log.info("***************创建菜单 开始***************");
		Object obj = menuTempService.updateWxMenu();
		log.info("***************创建菜单 结束***************");
		return obj;
	}
	
	/**
	 * 处理accesstoken 失败问题
	 * @return
	 */
	@RequestMapping(value="/accesstokenerr")
	public Object accessTokenErr(){
		log.info("***************token错误处理 开始***************");
		Object obj = AccessTokenErrService.getAccessToken();
		log.info("***************token错误处理 结束***************");
		return obj;
	}
	
	/**
	 * 发送模板消息
	 */
	@Autowired
	WxMsgDownsideService wxMsgDownsideService = null;
	@RequestMapping(value="/sendtemplatemessage")
	public Object sendTemplateMessage(){
		log.info("***************发送模板消息开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("接收到模板消息："+msgMap);
		Object obj = wxMsgDownsideService.templateMessageIn(msgMap.get("template"), msgMap);
		log.info("***************发送模板消息结束***************");
		return obj;
	}
	
	/**
	 * JsapiSdk签名服务
	 */
	@Autowired
	WxJsapiTicketService wxJsapiTicketService = null;
	@RequestMapping(value="/jssdksign")
	public Object jsSdkSign(){
		log.info("***************生成签名开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("JS-SDK签名请求："+msgMap);
		Object obj = wxJsapiTicketService.sign(msgMap);
		log.info("***************生成签名结束***************");
		return obj;
	}
	
	/**
	 * 网页授权获取openid
	 */
	@Autowired
	WxAuthorizeService wxAuthorizeService = null;
	@RequestMapping(value="/snsapibase")
	public Object snsapiBase(){
		log.info("***************基础授权开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("授权snsapi_base方式："+msgMap);
		Object obj = wxAuthorizeService.snsapiBase(msgMap);
		log.info("***************基础授权结束***************");
		return obj;
	}
	
	/**
	 * 根据openid获取微信用户信息
	 */
	@RequestMapping(value="/getWxUserInfo")
	public Object getWxUserInfo(){
		log.info("***************获取微信用户信息开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("openid："+msgMap);
		Object obj = wxAuthorizeService.getWxUserInfo(msgMap);
		log.info("***************获取微信用户信息结束***************");
		return obj;
	}
	
	/**
	 * 绑定账号
	 */
	@Autowired
	BindAccountService bindAccountService = null;
	@RequestMapping(value="/bindAccount")
	public Object bindAccount(){
		log.info("***************绑定账号开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定信息:"+msgMap);
		Object obj = bindAccountService.bindAccount(msgMap);
		log.info("***************绑定账号结束***************");
		return obj;
	}
	
	/**
	 * 绑定员工账号
	 */
	@RequestMapping(value="/bindStaff")
	public Object bindStaff(){
		log.info("***************绑定员工账号开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定信息:"+msgMap);
		Object obj = bindAccountService.bindStaff(msgMap);
		log.info("***************员工绑定账号结束***************");
		return obj;
	}

	/**
	 * 员工ACS验证
	 */
	@RequestMapping(value="/toACSAuthentication")
	public Object toACSAuthentication(){
		log.info("***************ACS认证开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取员工信息:"+msgMap);
		Object obj = bindAccountService.bindAccount(msgMap);
		log.info("***************绑定账号结束***************");
		return obj;
	}
	
	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value="/sendVerificationCode")
	public Object sendVerificationCode(){
		log.info("***************发送手机验证码开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定信息:"+msgMap);
		Object obj = bindAccountService.sendVerificationCode(msgMap);
		log.info("***************发送手机验证码结束***************");
		return obj;
	}
	
	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value="/sendVerificationCodeUn")
	public Object sendVerificationCodeUn(){
		log.info("***************发送手机验证码开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定信息:"+msgMap);
		Object obj = bindAccountService.sendVerificationCodeUn(msgMap);
		log.info("***************发送手机验证码结束***************");
		return obj;
	}
	
	/**
	 * 获取个人信息
	 */
	@Autowired
	PersonalInfoService personalInfoService = null;
	@RequestMapping(value="/personalInfo")
	public Object personalInfo(){
		log.info("***************获取个人信息开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取个人信息:"+msgMap);
		Object obj = personalInfoService.personalInfo(msgMap);
		log.info("***************获取个人信息结束***************");
		return obj;
	}
	
	/**
	 * 个人信息-收入范围更新
	 */
	@RequestMapping(value="/incomeChange")
	public Object incomeChange(){
		log.info("***************收入范围更新开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("收入范围更新:"+msgMap);
		Object obj = personalInfoService.incomeChange(msgMap);
		log.info("***************收入范围更新结束***************");
		return obj;
	}
	
	/**
	 * 个人信息-地区更新
	 */
	@RequestMapping(value="/zoneChange")
	public Object zoneChange(){
		log.info("***************地区更新开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("地区更新:"+msgMap);
		Object obj = personalInfoService.zoneChange(msgMap);
		log.info("***************地区更新结束***************");
		return obj;
	}
	
	/**
	 * 提前还款申请记录查询
	 */
	@Autowired
	PaymentHistoryService paymentHistoryService = null;
	@RequestMapping(value="/queryEarlyRepayApply")
	public Object queryEarlyRepayApply(){
		log.info("***************提前还款申请记录查询开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("提前还款申请记录查询:"+msgMap);
		Object obj = paymentHistoryService.queryEarlyRepayApply(msgMap);
		log.info("***************提前还款申请记录查询结束***************");
		return obj;
	}
	
	/**
	 * 还款记录查询
	 */
	@RequestMapping(value="/queryRepayRecord")
	public Object queryRepayRecord(){
		log.info("***************还款记录查询开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("还款记录查询:"+msgMap);
		Object obj = paymentHistoryService.queryRepayRecord(msgMap);
		log.info("***************还款记录查询结束***************");
		return obj;
	}
	
	/**
	 * 我的贷款-获取绑定合同
	 */
	@Autowired
	MyLoanService myLoanService = null;
	@RequestMapping(value="/getBindingContract")
	public Object getBindingContract(){
		log.info("***************获取绑定合同开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定合同:"+msgMap);
		Object obj = myLoanService.getBindingContract(msgMap);
		log.info("***************获取绑定合同结束***************");
		return obj;
	}
	
	/**
	 * 获取合同信息
	 */
	@Autowired
	ContractService contractService = null;
	@RequestMapping(value="/getcontractinfo")
	public Object getContractInfo(){
		log.info("***************获取合同信息开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取合同信息:"+msgMap);
		Object obj = contractService.getContractList(msgMap);
		log.info("***************获取合同信息***************");
		return obj;
	}
	
	/**
	 * 修改绑定合同信息
	 */
	@RequestMapping(value="/wxchangecontract")
	public Object changeContract(){
		log.info("***************获取合同信息开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("修改绑定合同信息:"+msgMap);
		Object obj = contractService.changeContractInfo(msgMap);
		log.info("***************获取合同信息***************");
		return obj;
	}
	
	/**
	 * 根据Openid获取当前绑定的合同信息
	 * @return
	 */
	@RequestMapping(value="/getcurrentcontract")
	public Object getCurrentContract(){
		log.info("***************获取绑定合同信息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取绑定合同信息msgMap:"+msgMap);
		Object obj = contractService.getCurrentContract(msgMap);
		log.info("***************获取绑定合同信息 开始***************");
		return obj;
	}
	
	/**
	 * 用户信息变更获取验证码
	 */
	@Autowired
	ChangeContactService changeContactService = null;
	@RequestMapping(value="/getcheckcode")
	public Object getCCCheckCode(){
		log.info("***************获取验证码开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("用户信息变更获取验证码:"+msgMap);
		Object obj = changeContactService.getCheckCode(msgMap);
		log.info("***************获取验证码结束***************");
		return obj;
	}
	
	/**
	 * 更改联系方式
	 * @return
	 */
	@RequestMapping(value="/changecontactmode")
	public Object changeContactMode(){
		log.info("***************更改联系方式 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("更改用户信息:"+msgMap);
		Object obj = changeContactService.changeContactMode(msgMap);
		log.info("***************更改联系方式 结束***************");
		return obj;
	}
	
	/**
	 * 下载凭证
	 */
	@Autowired
	RepaymentService repaymentService = null;
	@RequestMapping(value="/uploadcert")
	public Object uploadCertificate(){
		log.info("***************上传凭证图片 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("上传凭证图片msgMap:"+msgMap);
		Object obj = repaymentService.uploadCertificate(msgMap);
		log.info("***************上传凭证图片 结束***************");
		return obj;
	}
	
	/**
	 * 查询提前还款信息
	 * @return
	 */
	@RequestMapping(value = "/getearlyrepayinfo")
	public Object getEarlyRepayInfo(){
		log.info("***************查询提前还款信息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询提前还款信息msgMap:"+msgMap);
		Object obj = repaymentService.getEarlyRepayInfo(msgMap);
		log.info("***************查询提前还款信息 结束***************");
		return obj;
	}
	/**
	 * 提前还款查询银行卡号
	 * */
	@RequestMapping(value="/wxearlyrepaygetbank")
	public Object wxearlyrepaygetbank(){
		log.info("***************提前还款查询银行卡号 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("提前还款查询银行卡号msgMap:"+msgMap);
		Object obj = repaymentService.wxearlyrepaygetbank(msgMap);
		log.info("***************提前还款查询银行卡号 结束***************");
		return obj;
	}
	/**
	 * 提前还款校验
	 * @return
	 */
	@RequestMapping(value="/wxearlyrepaycheck")
	public Object wxearlyrepaycheck(){
		log.info("***************提前还款验证 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("提前还款验证msgMap:"+msgMap);
		Object obj = repaymentService.wxearlyrepaycheck(msgMap);
		log.info("***************提前还款验证 结束***************");
		return obj;
	}
	/**
	 * 提前还款申请
	 * @return
	 */
	@RequestMapping(value="/wxearlyrepayapply")
	public Object earlyRepayApply(){
		log.info("***************提前还款申请 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("提前还款申请msgMap:"+msgMap);
		Object obj = repaymentService.repaymentApply(msgMap);
		log.info("***************提前还款申请 结束***************");
		return obj;
	}
	
	/**
	 * 经销商查询
	 */
	@Autowired
	ShopService shopService = null;
	@RequestMapping(value="/wxqueryshop")
	public Object queryShop(){
		log.info("***************查询经销商信息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询经销商信息msgMap:"+msgMap);
		Object obj = shopService.queryShop(msgMap);
		log.info("***************查询经销商信息 结束***************");
		return obj;
	}
	
	@RequestMapping(value="/wxqueryshopbycar")
	public Object queryShopByCar(){
		log.info("***************根据车型查询经销商信息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("根据车型查询经销商信息msgMap:"+msgMap);
		Object obj = shopService.queryShopByCar(msgMap);
		log.info("***************根据车型查询经销商信息 结束***************");
		return obj;
	}
	
	@Autowired
	MessageService messageService = null;
	@RequestMapping(value="/wxgetmymsg")
	public Object getMyMsg(){
		log.info("***************查询我的消息 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询我的消息msgMap:"+msgMap);
		Object obj = messageService.getMyMsg(msgMap);
		log.info("***************查询我的消息 结束***************");
		return obj;
	}
	
	@RequestMapping(value="/wxgetmymsgtype")
	public Object getMyMsgType(){
		log.info("***************查询我的消类型 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询我的消息类型msgMap:"+msgMap);
		Object obj = messageService.getMyMsgType(msgMap);
		log.info("***************查询我的消息类型 结束***************");
		return obj;
	}
	
	/**
	 * 账号设置-解绑账号
	 */
	@Autowired
	AccountSettingService accountSettingService = null;
	@RequestMapping(value="/removeBindAccount")
	public Object removeBindAccount(){
		log.info("***************解绑账号 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("解绑账号msgMap:"+msgMap);
		Object obj = accountSettingService.removeBindAccount(msgMap);
		log.info("***************解绑账号 结束***************");
		return obj;
	}
	
	/**
	 * 贷款验证码
	 */
	@Autowired
	LoanApplyService loanApplyService = null;
	@RequestMapping(value="/wxgetapplycode")
	public Object getApplyCode(){
		log.info("***************获取贷款申请验证码 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取贷款申请验证码msgMap:"+msgMap);
		Object obj = loanApplyService.getApplyCode(msgMap);
		log.info("***************获取贷款申请验证码 结束***************");
		return obj;
	}
	
	/**
	 * 贷款申请
	 * @return
	 */
	@RequestMapping(value="/wxloanapply")
	public Object loanApply(){
		log.info("***************贷款申请 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("贷款申请msgMap:"+msgMap);
		Object obj = loanApplyService.loanApply(msgMap);
		log.info("***************贷款申请 结束***************");
		return obj;
	}
	
	/**
	 * 查询推荐车型
	 * @return
	 */
	@RequestMapping(value="/wxgetschemes")
	public Object getSchemes(){
		log.info("***************查询推荐车型 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("查询推荐车型msgMap:"+msgMap);
		Object obj = loanApplyService.getSchemePromote(msgMap);
		log.info("***************查询推荐车型  结束***************");
		return obj;
	}
	
	/**
	 * 账号设置-意见反馈
	 */
	@RequestMapping(value="/feedBack")
	public Object feedBack(){
		log.info("***************提交意见反馈 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("意见反馈msgMap:"+msgMap);
		Object obj = accountSettingService.feedBack(msgMap);
		log.info("***************提交意见反馈 结束***************");
		return obj;
	}
	
	/**
	 * 主页-获取新消息通知
	 */
	@Autowired
	HomeService homeService = null;
	@RequestMapping(value="/messageNotify")
	public Object messageNotify(){
		log.info("***************获取新消息通知 开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("获取新消息通知msgMap:"+msgMap);
		Object obj = homeService.messageNotify(msgMap);
		log.info("***************获取新消息通知 结束***************");
		return obj;
	}
	
//	/**
//	 * 文件上传
//	 * @param file
//	 * @return
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/fileUpload")
//	public String uploadMulti(@RequestParam("file") MultipartFile file)
//			throws IOException {
//		log.info("上传文件");
//		HttpServletRequest request = SpringUtil.request();
//
//		// 判断文件是否为空  
//        if (!file.isEmpty()) {  
//            try {  
//                // 文件保存路径  
//            	String filePath = "d:\\test\\"+file.getOriginalFilename();
//                // 转存文件  
//                file.transferTo(new File(filePath));  
//            } catch (Exception e) {  
//                e.printStackTrace();  
//            }  
//        }  
//        // 重定向  
//        return "redirect:/list.html"; 
//	}
	
	/*
	 * Download a file from 
	 *   - inside project, located in resources folder.
	 *   - outside project, located in File system somewhere. 
	 */
	@RequestMapping(value="/wxdownload")
	public void downloadFile(HttpServletResponse response) throws IOException {
	
		File file = null;
		
		file = new File("D:/data/1.jpg");
		
		if(!file.exists()){
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		
		String mimeType= URLConnection.guessContentTypeFromName(file.getName());
		if(mimeType==null){
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}
		
		System.out.println("mimetype : "+mimeType);
		
        response.setContentType(mimeType);
        
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
//        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() +"\""));

        
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        
        response.setContentLength((int)file.length());

		//InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
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
