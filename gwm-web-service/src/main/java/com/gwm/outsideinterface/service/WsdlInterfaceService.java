package com.gwm.outsideinterface.service;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwm.common.DecodeXmlImpl;
import com.gwm.common.RedisDao;
import com.gwm.common.SpringUtil;
import com.gwm.engine.Util;

public class WsdlInterfaceService {
	
	static Logger log = LoggerFactory.getLogger(WsdlInterfaceService.class);
	
	/**
	 * wsdl通用方法
	 * @param url
	 * @param reqXml
	 * @param qName
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public static String sendXML(String url, String reqXml, String qName,
			String method) throws Exception {
		Object result = null;
		String resultstrVal = "";
		RedisDao redis = SpringUtil.getBean(RedisDao.class);
		try {
			// 使用RPC方式调用WebService
			RPCServiceClient serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			// 指定调用WebService的URL
			EndpointReference targetEPR = new EndpointReference(url);
			options.setTo(targetEPR);
			options.setManageSession(true);
			options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
			//String cer_filename = redis.getString("cer_filename"); //信任证书
			//String cer_password = redis.getString("cer_password"); //信任证书密码
			String secure_key = redis.getString("SECURE_KEY");  //秘钥
//			String cer_filename = "D:\\cer\\client.truststore";
//			String cer_password = "gwmfc1234";
//			String secure_key = "1111111111111111";
			//options.setProperty("javax.net.ssl.trustStore", cer_filename);
			//options.setProperty("javax.net.ssl.trustStorePassword", cer_password);
			options.setAction(method);
			// 调用方法的参数值
			Object[] entryArgs = new Object[] { reqXml };
			// 调用方法返回值的数据类型的Class对象
			Class[] classes = new Class[] { String.class };
			// 调用方法名及WSDL文件的命名空间
			// 命名空间是http://localhost:8080/axis2/services/CalculateService?wsdl中wsdlefinitions标签targetNamespace属性
			QName opName = new QName(qName, method);
			// 执行方法获取返回值
			// 没有返回值的方法使用
			/*
			QName md5Name = new QName(qName, "identification_no");
			String identification_no = getIdentification(reqXml);
			serviceClient.addStringHeader(md5Name, identification_no+"");
			*/
			String identification_no = getIdentification(reqXml, secure_key);
			serviceClient.addHeader(createHeaderOMElement(identification_no));
			result = serviceClient.invokeBlocking(opName, entryArgs, classes)[0];
			serviceClient.cleanupTransport();
			resultstrVal = (String) result;
		} catch (AxisFault e) {
			e.printStackTrace();
			throw new Exception("请求接口失败！");
		}
		return resultstrVal;
	}
	/**
	 * 报文头中添加秘钥串
	 * @param str
	 * @return
	 */
	public static OMElement createHeaderOMElement(String str) {
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMNamespace SecurityElementNamespace = factory.createOMNamespace(
				"http://service.gwmfc.com", "gwmfc");
		OMElement authenticationOM = factory.createOMElement("Authentication",
				SecurityElementNamespace);
		OMElement usernameOM = factory.createOMElement("identification_no",
				SecurityElementNamespace);
		usernameOM.setText(str);
		authenticationOM.addChild(usernameOM);
		return authenticationOM;
	}
	
	/**
	 * 生成秘钥串
	 * @param xml
	 * @param secure_key
	 * @return
	 */
	private static String getIdentification(String xml, String secure_key){
		try{
			Map<String, Object> map = DecodeXmlImpl.decode(xml);
			String a = secure_key==null?"":secure_key;
			
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				a += entry.getValue();
			}
			log.debug("md5加密前:"+a);
			String iden = getMd5(a);
			log.debug("md5加密后:"+iden);
			return iden;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * MD5加密算法
	 * @param plainText
	 * @return
	 */
	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
