package com.gwm.manager.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RepaymentService{
	
	Logger log = LoggerFactory.getLogger(RepaymentService.class);
	
	public Object earlyrepayinfoForSub(Map<String,String> param){
		Object obj = com.gwm.common.service.Service.earlyrepayinfoForSub();
		String retStr = (String)obj;
		log.info("obj:"+obj);
		Map<String, Object> retMap = JSONObject.parseObject(retStr);
		String rows = (String)retMap.get("rows");
		log.info("返回rows:"+rows);
		List<Object> rowMap = JSONArray.parseArray(rows);
		retMap.put("rows", rowMap);
		return retMap;
	}
	/**
	 * 查询提前还款申请记录
	 * @param param
	 * @return
	 */
	public Object getRepaymentInfo(Map<String, String> param){
		Object obj = com.gwm.common.service.Service.mQueryEarlyRepayInfo();
		String retStr = (String)obj;
		log.info("obj:"+obj);
		Map<String, Object> retMap = JSONObject.parseObject(retStr);
		String rows = (String)retMap.get("rows");
		log.info("返回rows:"+rows);
		List<Object> rowMap = JSONArray.parseArray(rows);
		retMap.put("rows", rowMap);
		return retMap;
	}
	
	/**
	 * 处理提前还款
	 * @param param
	 * @return
	 */
	public Object dealRepayment(Map<String, String> param){
		Object obj = com.gwm.common.service.Service.mDealEarlyRepayInfo();
		String retStr = (String)obj;
		log.info("obj:"+retStr);
		return obj;
	}
	
	public void exportEarlyRepayInfo(HttpServletResponse response, Map<String, String> param){
		try{
			Object obj = com.gwm.common.service.Service.mQueryEarlyRepayInfo();
			String retStr = (String)obj;
			log.info("obj:"+obj);
			Map<String, Object> retMap = JSONObject.parseObject(retStr);
			String rows = (String)retMap.get("rows");
			log.info("返回rows:"+rows);
			List<Object> rowList = JSONArray.parseArray(rows);
			if("0".equals(retMap.get("errcode"))){
				log.info("开始进行excel处理");
				HSSFWorkbook wb = buileExcel(rowList);
				response.setContentType("application/vnd.ms-excel");
				String fileName = "earlyrepay.xls";
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
			}
		}catch(Exception ex){
			log.error("导出excel异常", ex);
		}
	}
	
	/**
	 * 构建提前还款excel结构
	 * */
	private HSSFWorkbook buileExcel(List<Object>  array){

		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet = wb.createSheet("提前还款");
		HSSFRow row = sheet.createRow((int)0);
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //创建一个居中格式
        
        HSSFCellStyle stylez = wb.createCellStyle();
        stylez.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);    
        stylez.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置背景色
        stylez.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.setColumnWidth(0, 12*256);  //状态
        sheet.setColumnWidth(1, 38*256);  //单号
        sheet.setColumnWidth(2, 12*256);  //申请时间
        sheet.setColumnWidth(3, 8*256);  //申请人
        sheet.setColumnWidth(4, 12*256);  //合同号
        sheet.setColumnWidth(5, 12*256);  //提前还款日
        sheet.setColumnWidth(6, 12*256);  //提前还款金额
        sheet.setColumnWidth(7, 18*256);  //还款方式
        sheet.setColumnWidth(8, 100*256);  //还款凭证

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("状态"); cell.setCellStyle(stylez);
        cell = row.createCell(1);
        cell.setCellValue("提前还款申请单编号"); cell.setCellStyle(stylez);
        cell = row.createCell(2);
        cell.setCellValue("申请时间"); cell.setCellStyle(stylez);
        cell = row.createCell(3);
        cell.setCellValue("申请人"); cell.setCellStyle(stylez);
        cell = row.createCell(4);
        cell.setCellValue("合同号"); cell.setCellStyle(stylez);
        cell = row.createCell(5);
        cell.setCellValue("提前还款日"); cell.setCellStyle(stylez);
        cell = row.createCell(6);
        cell.setCellValue("提前还款金额"); cell.setCellStyle(stylez);
        cell = row.createCell(7);
        cell.setCellValue("还款方式"); cell.setCellStyle(stylez);
        cell = row.createCell(8);
        cell.setCellValue("还款凭证"); cell.setCellStyle(stylez);

		for(int i=0;i<array.size();i++){
			Map<String, Object> m = (Map<String, Object>)array.get(i);
			row = sheet.createRow(i+1);
			
            cell = row.createCell(0);
            String status = (String)m.get("status");
            switch(status){
            case "1":
            	status = "已提交";
            	break;
            case "2":
            	status = "正常受理";
            	break;
            case "3":
            	status = "附条件受理";
            	break;
            case "4":
            	status = "关闭";
            	break;
            case "5":
            	status = "已结清";
            	break;
            default:
            	break;
            }
	        cell.setCellValue(status);
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(1);
	        cell.setCellValue((String)m.get("application_num")); 
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(2);
	        String apply_date = (String)m.get("apply_date");
	        if(!StringUtils.isEmpty(apply_date)&&apply_date.length()==8){
	        	apply_date = apply_date.substring(0, 4) +"-"+ apply_date.substring(4, 6)+"-"+apply_date.substring(6, 8);
	        }
	        cell.setCellValue(apply_date);
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(3);
	        cell.setCellValue((String)m.get("applicationer"));
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(4);
	        cell.setCellValue(m.get("contract_code")+"");
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(5);
	        String apply_repay_date = ""+m.get("apply_repay_date");
	        if(!StringUtils.isEmpty(apply_repay_date)&&apply_repay_date.length()==8){
	        	apply_repay_date = apply_repay_date.substring(0, 4) +"-"+ apply_repay_date.substring(4, 6)+"-"+apply_repay_date.substring(6, 8);
	        }
	        cell.setCellValue(apply_repay_date); 
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(6);
            cell.setCellValue(""+m.get("total_amt"));
            cell.setCellStyle(style);
            
	        cell = row.createCell(7);
	        String repay_type = (String)m.get("repay_type");
	        if("1".equals(repay_type)){
	        	repay_type = "预留银行卡还款";
	        }else if("2".equals(repay_type)){
	        	repay_type = "对公账户还款";
	        }
	        cell.setCellValue(repay_type);
	        cell.setCellStyle(style);
	        
	        cell = row.createCell(8);
	        cell.setCellValue(m.get("repay_voucher")+"");
	        cell.setCellStyle(style);
		}
		return wb;
	}
}