package com.gwm.manager.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.gwm.common.SpringUtil;
import com.gwm.manager.service.ApplyInfoService;

@RestController
public class LoanApplyListController {
	
	@Autowired
	private ApplyInfoService service = null;
	static Logger log = LoggerFactory.getLogger(LoanApplyListController.class);
	/**
	 * 导入热销车型
	 * */
	@RequestMapping(value="/importExcel")
 	public Object importExcel(@RequestParam MultipartFile  file) throws Exception{
		log.info("***************导入热销车型开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		String str="";

		Object obj=null;
		if (file.isEmpty()){  
			System.out.println("文件空");
		}else{
			System.out.println("文件不为空");
			String fileName = file.getOriginalFilename(); 
		     log.info("上传的文件名为：" + fileName); 
		     String suffixName = fileName.substring(fileName.lastIndexOf(".")); 
		     log.info("上传的后缀名为：" + suffixName); 
		     HSSFWorkbook wb = null;
		     wb = new HSSFWorkbook(file.getInputStream());
		     String[][] ss =getData(wb,1);
		     System.out.println(ss.length);
		     str="{"+
		    		 "\"List\":[";
		     String str_tmp="";
		     for(int i=0;i<ss.length;i++){
		    	 str_tmp+="{"+
		    			 "\"id\":\""+ss[i][0]+"\"," +
		    			 "\"brand\":\""+ss[i][1]+"\","+
		    			 "\"models\":\""+ss[i][2]+"\","+
		    			 "\"configure\":\""+ss[i][3]+"\","+
		    			 "\"price\":\""+ss[i][4]+"\","+
		    			 "\"amount_product\":\""+ss[i][5]+"\","+
		    			 "\"purchase\":\""+ss[i][6]+"\","+
		    			 "\"pic\":\""+ss[i][7]+"\","+
		    			 "\"rate\":\""+ss[i][8]+"\","+
		    			 "\"loanterm\":\""+ss[i][9]+"\","+
		    			 "\"minloanprice\":\""+ss[i][10]+"\","+
		    			 "\"maxloanprice\":\""+ss[i][11]+"\""+
		    			 "},";
		     }
		     str_tmp=str_tmp.substring(0,str_tmp.length()-1);
 			str=str+str_tmp;
 			str+="]}";
		    log.info("str:"+str);
		}
		msgMap.put("excelInfo", str);
		Object result=service.importExcel(msgMap);	
		log.info("导入热销车型信息:"+msgMap);
		log.info("***************导入热销车型结束***************");
		return result;
	}
	/**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     * @param file 读取数据的源Excel
     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     * @return 读出的Excel中数据的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getData(HSSFWorkbook wb, int ignoreRows)
           throws FileNotFoundException, IOException {
       List<String[]> result = new ArrayList<String[]>();
       int rowSize = 0;
       HSSFCell cell = null;
       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
           HSSFSheet st = wb.getSheetAt(sheetIndex);
           // 第一行为标题，不取
           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
              HSSFRow row = st.getRow(rowIndex);
              if (row == null) {
                  continue;
              }
              int tempRowSize = row.getLastCellNum() + 1;
              if (tempRowSize > rowSize) {
                  rowSize = tempRowSize;
              }
              String[] values = new String[rowSize];
              Arrays.fill(values, "");
              boolean hasValue = false;
              for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                  String value = "";
                  cell = row.getCell(columnIndex);
                  if (cell != null) {
                     // 注意：一定要设成这个，否则可能会出现乱码
//                     cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                     switch (cell.getCellType()) {
                     case HSSFCell.CELL_TYPE_STRING:
                         value = cell.getStringCellValue();
                         break;
                     case HSSFCell.CELL_TYPE_NUMERIC:
                         if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date != null) {
                                value = new SimpleDateFormat("yyyy-MM-dd")
                                       .format(date);
                            } else {
                                value = "";
                            }
                         } else {
                        	 String tmp=cell.getNumericCellValue()+"";
                        	 if(tmp.indexOf(".")<=-1){
                        		 value = new DecimalFormat("0").format(cell
                                         .getNumericCellValue());
                        	 }else{
                        		 value = new DecimalFormat("0.00").format(cell
                                         .getNumericCellValue());
                        	 }
                         }
                         break;
                     case HSSFCell.CELL_TYPE_FORMULA:
                         // 导入时如果为公式生成的数据则无值
                         if (!cell.getStringCellValue().equals("")) {
                            value = cell.getStringCellValue();
                         } else {
                            value = cell.getNumericCellValue() + "";
                         }
                         break;
                     case HSSFCell.CELL_TYPE_BLANK:
                         break;
                     case HSSFCell.CELL_TYPE_ERROR:
                         value = "";
                         break;
                     case HSSFCell.CELL_TYPE_BOOLEAN:
                         value = (cell.getBooleanCellValue() == true ? "Y"
                                : "N");
                         break;
                     default:
                         value = "";
                     }
                  }
                  if (columnIndex == 0 && value.trim().equals("")) {
                     break;
                  }
                  values[columnIndex] = rightTrim(value);
                  hasValue = true;
              }

              if (hasValue) {
                  result.add(values);
              }
           }
       }
       String[][] returnArray = new String[result.size()][rowSize];
       for (int i = 0; i < returnArray.length; i++) {
           returnArray[i] = (String[]) result.get(i);
       }
       return returnArray;
    }
    /**
     * 去掉字符串右边的空格
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
     public static String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }
     /**
      * 导出热销车型excel
      * */
	@RequestMapping(value="/exportHotCarInfo")
	public void exportHotCarInfo(HttpServletResponse response) throws Exception{
		log.info("***************热销车型导出开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=service.exportHotCarInfo(msgMap);
		JSONArray  array= JSONArray.fromObject(result);
		HashMap<String,Object> m = (HashMap<String,Object>)
				(JSONObject.toBean(JSONObject.fromObject(array.get(0)+""),HashMap.class));
		if(m.get("error")==null){//证明查询都记录
			log.info("开始进行excel处理");			
		    HSSFWorkbook wb = buileHotCarExcel(array);
		    response.setContentType("application/vnd.ms-excel");
		    String fileName = "Export.xls";
		    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    
		    wb.write(os);
		    byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
			
		}else{
			log.info("不需要导出excel");
		}
		
		log.info("***************热销车型导出结束***************");
	}
	/**
	 * 导出在线申请
	 * */
	@RequestMapping(value="/exportApplyInfo")
	public void exportApplyInfo(HttpServletResponse response) throws Exception {
		log.info("***************在线申请导出开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=service.exportApplyInfo(msgMap);
		JSONArray  array= JSONArray.fromObject(result);
		HashMap<String,Object> m = (HashMap<String,Object>)
				(JSONObject.toBean(JSONObject.fromObject(array.get(0)+""),HashMap.class));
		if(m.get("error")==null){//证明查询都记录
			log.info("开始进行excel处理");			
		    HSSFWorkbook wb = buileExcel(array);
		    response.setContentType("application/vnd.ms-excel");
		    String fileName = "Export.xls";
		    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    
		    wb.write(os);
		    byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
			
		}else{
			log.info("不需要导出excel");
		}
		
		log.info("***************在线申请导出结束***************");
	}
	/**
	 * 构建热销车型excel结构及文件
	 * */
	public HSSFWorkbook buileHotCarExcel(JSONArray  array){

		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet = wb.createSheet("gw_hot_car");
		HSSFRow row = sheet.createRow((int)0);
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //创建一个居中格式
        
        HSSFCellStyle stylez = wb.createCellStyle();
        stylez.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);    
        stylez.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置背景色
        stylez.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.setColumnWidth(0, 25*256);
        sheet.setColumnWidth(1, 25*256);
        sheet.setColumnWidth(2, 25*256);
        sheet.setColumnWidth(3, 25*256);
        sheet.setColumnWidth(4, 25*256);
        sheet.setColumnWidth(5, 25*256);
        sheet.setColumnWidth(6, 25*256);
        sheet.setColumnWidth(7, 25*256);
        sheet.setColumnWidth(8, 25*256);
        sheet.setColumnWidth(9, 25*256);
        sheet.setColumnWidth(10, 25*256);
        sheet.setColumnWidth(11, 25*256);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("编号"); cell.setCellStyle(stylez);
        cell = row.createCell(1);
        cell.setCellValue("品牌"); cell.setCellStyle(stylez);
        cell = row.createCell(2);
        cell.setCellValue("车型"); cell.setCellStyle(stylez);
        cell = row.createCell(3);
        cell.setCellValue("配置"); cell.setCellStyle(stylez);
        cell = row.createCell(4);
        cell.setCellValue("车价"); cell.setCellStyle(stylez);
        cell = row.createCell(5);
        cell.setCellValue("金融产品"); cell.setCellStyle(stylez);
        cell = row.createCell(6);
        cell.setCellValue("销量"); cell.setCellStyle(stylez);
        cell = row.createCell(7);
        cell.setCellValue("图片"); cell.setCellStyle(stylez);
        cell = row.createCell(8);
        cell.setCellValue("比例"); cell.setCellStyle(stylez);
        cell = row.createCell(9);
        cell.setCellValue("贷款期限"); cell.setCellStyle(stylez);
        cell = row.createCell(10);
        cell.setCellValue("最小贷款额"); cell.setCellStyle(stylez);
        cell = row.createCell(11);
        cell.setCellValue("最大贷款额"); cell.setCellStyle(stylez);
		for(int i=0;i<array.size();i++){
			HashMap<String,Object> m = (HashMap<String,Object>)
					(JSONObject.toBean(JSONObject.fromObject(array.get(i)+""),HashMap.class));
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
	        cell.setCellValue(m.get("id")+""); cell.setCellStyle(style);
	        cell = row.createCell(1);
	        cell.setCellValue((String)m.get("brand")); cell.setCellStyle(style);
	        cell = row.createCell(2);
	        cell.setCellValue((String)m.get("models")); cell.setCellStyle(style);
	        cell = row.createCell(3);
	        cell.setCellValue((String)m.get("configure")); cell.setCellStyle(style);
	        cell = row.createCell(4);
	        cell.setCellValue(m.get("price")+""); cell.setCellStyle(style);
	        cell = row.createCell(5);
	        cell.setCellValue((String)m.get("amount_product")); cell.setCellStyle(style);
	        cell = row.createCell(6);
            cell.setCellValue(m.get("purchase")+""); cell.setCellStyle(style);
	        cell = row.createCell(7);
	        cell.setCellValue((String)m.get("pic")); cell.setCellStyle(style);
	        cell = row.createCell(8);
	        cell.setCellValue(m.get("rate")+""); cell.setCellStyle(style);
	        cell = row.createCell(9);
	        cell.setCellValue(m.get("loanterm")+""); cell.setCellStyle(style);
	        cell = row.createCell(10);
	        cell.setCellValue(m.get("minloanprice")+""); cell.setCellStyle(style);
	        cell = row.createCell(11);
	        cell.setCellValue(m.get("maxloanprice")+""); cell.setCellStyle(style);
		}
		return wb;
	
	}
	/**
	 * 构造在线申请信息excel结构及文件
	 * */
	public HSSFWorkbook buileExcel(JSONArray  array){
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet = wb.createSheet("在线申请信息表");
		HSSFRow row = sheet.createRow((int)0);
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //创建一个居中格式
        
        HSSFCellStyle stylez = wb.createCellStyle();
        stylez.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);    
        stylez.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置背景色
        stylez.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.setColumnWidth(0, 25*256);
        sheet.setColumnWidth(1, 25*256);
        sheet.setColumnWidth(2, 25*256);
        sheet.setColumnWidth(3, 25*256);
        sheet.setColumnWidth(4, 25*256);
        sheet.setColumnWidth(5, 25*256);
        sheet.setColumnWidth(6, 25*256);
        sheet.setColumnWidth(7, 25*256);
        sheet.setColumnWidth(8, 25*256);
        sheet.setColumnWidth(9, 25*256);
        sheet.setColumnWidth(10, 25*256);
        sheet.setColumnWidth(11, 25*256);
        sheet.setColumnWidth(12, 25*256);
        sheet.setColumnWidth(13, 25*256);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("申请单号"); cell.setCellStyle(stylez);
        cell = row.createCell(1);
        cell.setCellValue("申请时间"); cell.setCellStyle(stylez);
        cell = row.createCell(2);
        cell.setCellValue("姓名"); cell.setCellStyle(stylez);
        cell = row.createCell(3);
        cell.setCellValue("称呼"); cell.setCellStyle(stylez);
        cell = row.createCell(4);
        cell.setCellValue("手机号"); cell.setCellStyle(stylez);
        cell = row.createCell(5);
        cell.setCellValue("身份证"); cell.setCellStyle(stylez);
        cell = row.createCell(6);
        cell.setCellValue("品牌"); cell.setCellStyle(stylez);
        cell = row.createCell(7);
        cell.setCellValue("车型"); cell.setCellStyle(stylez);
        cell = row.createCell(8);
        cell.setCellValue("首付"); cell.setCellStyle(stylez);
        cell = row.createCell(9);
        cell.setCellValue("省份"); cell.setCellStyle(stylez);
        cell = row.createCell(10);
        cell.setCellValue("城市"); cell.setCellStyle(stylez);
        cell = row.createCell(11);
        cell.setCellValue("经销商"); cell.setCellStyle(stylez);
        cell = row.createCell(12);
        cell.setCellValue("来源"); cell.setCellStyle(stylez);
        cell = row.createCell(13);
        cell.setCellValue("状态"); cell.setCellStyle(stylez);
		for(int i=0;i<array.size();i++){
			HashMap<String,Object> m = (HashMap<String,Object>)
					(JSONObject.toBean(JSONObject.fromObject(array.get(i)+""),HashMap.class));
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
	        cell.setCellValue((String)m.get("application_num")); cell.setCellStyle(style);
	        cell = row.createCell(1);
	        cell.setCellValue((String)m.get("time")); cell.setCellStyle(style);
	        cell = row.createCell(2);
	        cell.setCellValue((String)m.get("name")); cell.setCellStyle(style);
	        cell = row.createCell(3);
	        cell.setCellValue(((String)m.get("sex")).equals("1")?"男":"女"); cell.setCellStyle(style);
	        cell = row.createCell(4);
	        cell.setCellValue((String)m.get("phone")); cell.setCellStyle(style);
	        cell = row.createCell(5);
	        cell.setCellValue((String)m.get("card_id")); cell.setCellStyle(style);
	        cell = row.createCell(6);
            cell.setCellValue((String)m.get("brand")); cell.setCellStyle(style);
	        cell = row.createCell(7);
	        cell.setCellValue((String)m.get("model")); cell.setCellStyle(style);
	        cell = row.createCell(8);
	        cell.setCellValue(m.get("first_amt")+""); cell.setCellStyle(style);
	        cell = row.createCell(9);
	        cell.setCellValue((String)m.get("province")); cell.setCellStyle(style);
	        cell = row.createCell(10);
	        cell.setCellValue((String)m.get("city")); cell.setCellStyle(style);
	        cell = row.createCell(11);
	        cell.setCellValue((String)m.get("franchiser")); cell.setCellStyle(style);
	        cell = row.createCell(12);
	        if(((String)m.get("source")).equals("1")){
	        	cell.setCellValue("官网"); cell.setCellStyle(style);
	        }else if(((String)m.get("source")).equals("2")){
	        	cell.setCellValue("微信公众号"); cell.setCellStyle(style);
	        }else if(((String)m.get("source")).equals("3")){
	        	cell.setCellValue("哈弗商城"); cell.setCellStyle(style);
	        }else if(((String)m.get("source")).equals("4")){
	        	cell.setCellValue("品牌官网"); cell.setCellStyle(style);
	        }
	        cell = row.createCell(13);
	        cell.setCellValue(((String)m.get("status")).equals("1")?"已提交":"已受理"); cell.setCellStyle(style);
		}
		return wb;
	}
	
	/**
	 * 验证接口
	 * */
	@RequestMapping(value="/verification")
	public Object verification() throws Exception{
		log.info("*****验证接收参数开始*****");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("前台返回的ssouser"+msgMap.get("ssouser"));
		Object obj=service.verification(msgMap);
		log.info("*****验证接收参数结束*****");
		return obj;
	}
	
	/**
	 * 单页面验证
	 * */
	@RequestMapping(value="/checkUser")
	public Object checkUser() throws Exception{
		log.info("*****单页面验证接收参数开始*****");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		log.info("前台返回的ssouser"+msgMap.get("ssouser"));
		log.info("前台返回的access_token"+msgMap.get("access_token"));
		Object obj=service.checkUser(msgMap);
		log.info("*****单页面验证接收参数结束*****");
		return obj;
	}
	/**
	 * 查询热销车型
	 * */
	@RequestMapping(value="/selectHotCarInfo")
	public Object selectHotCarInfo(){
		log.info("******查询热销车型开始*******");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=service.selectHotCarInfo(msgMap);	
		log.info("在线申请信息:"+msgMap);
		log.info("***************查询热销车型结束***************");
		return result;
	}
	
	/**
	 * 查询在线申请
	 * */
	@RequestMapping(value="/selectApplyInfo")
	public Object selectApplyInfo(){
		log.info("***************在线申请查询开始***************");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=service.selectApplyInfo(msgMap);	
		log.info("在线申请信息:"+msgMap);
		log.info("***************在线申请查询 结束***************");
		return result;
	}
	
	/**
	 * 更新在线申请状态
	 * */
	@RequestMapping(value="/updateApplyInfo")
	public Object updateApplyInfo(){
		log.info("*********更新在线申请状态*********");
		HttpServletRequest request = SpringUtil.request();
		Map<String, String> msgMap = getParameters(request, null);
		Object result=service.updateApplyInfo(msgMap);	
		log.info("更新申请信息:"+msgMap);
		log.info("***************更新在线在线申请 结束***************");
		return result;
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
	
	
	public static void main(String args[]){
//		String a="[{'application_num':'d0ce3669-b24c-4419-9fa3-847dac11bfda','brand':'哈弗','model':'蓝标哈弗H1','style':'16款手动尊贵型','first_amt':1.00,'province':'云南省','city':'丽江市','franchiser':'丽江迪鑫汽车贸易有限公司','name':'张三','card_id':'210211198811095812','sex':'1','phone':'15566812881','status':'1','dostatus':'1','source':'1','time':'20161219201843','apply_date':20161219,'apply_time':201843},{'application_num':'a2b8e86f-eed8-42fb-8fba-fdfede870674','brand':'哈弗','model':'蓝标哈弗H1','style':'16款手动尊贵型','first_amt':1.00,'province':'云南省','city':'丽江市','franchiser':'丽江迪鑫汽车贸易有限公司','name':'张三','card_id':'210211198811095812','sex':'1','phone':'15566812881','status':'1','dostatus':'1','source':'1','time':'20161219201846','apply_date':20161219,'apply_time':201846},{'application_num':'3f005bdf-272d-4119-8387-43c311b394ff','brand':'哈弗','model':'蓝标哈弗H1','style':'16款手动尊贵型','first_amt':1.00,'province':'云南省','city':'丽江市','franchiser':'丽江迪鑫汽车贸易有限公司','name':'张三','card_id':'210211198811095812','sex':'1','phone':'15566812881','status':'1','dostatus':'1','source':'1','time':'20161219201941','apply_date':20161219,'apply_time':201941},{'application_num':'e067998d-3082-4bb0-baca-555b8a609e6d','brand':'长城','model':'2016款长城C30','style':'手动挡舒适型','first_amt':1.00,'province':'上海市','city':'上海市','franchiser':'上海金琥汽车销售有限公司','name':'战三','card_id':'210211198811095812','sex':'1','phone':'15566812881','status':'1','dostatus':'1','source':'1','time':'20161219202223','apply_date':20161219,'apply_time':202223}]";
		String a="0.5";
		if(a.indexOf(".")<=-1){
			System.out.println("不存在");
		}else{
			System.out.println("存在");
		}
	}
}
