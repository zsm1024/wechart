package com.gwm.manager.service;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gwm.common.RedisDao;

@Service
public class ContactChangeService {
	@Autowired
	RedisDao redis = null;
	Logger log = LoggerFactory.getLogger(ContactChangeService.class);
	
	public Object selectContactChangeForSub(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.selectContactChangeForSub();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	/**
	 * 查询联系方式变更记录
	 * @param map
	 * @return
	 */
	public Object selectContactChange(Map<String, String> map){
		Object retObj = com.gwm.common.service.Service.selectContactChange();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	
	/**
	 * 更新联系方式变更记录状态
	 * @param map
	 * @return
	 */
	public Object updateContactChange(Map<String,String> map){
		Object retObj = com.gwm.common.service.Service.updateContactChange();
		String retJson=(String)retObj;//返回的map信息
		log.info("rejson======"+retJson);
		return retObj;
	}
	
	/**
	 * 导出联系方式变更记录excel
	 * @param map
	 * @return
	 */
	public void exportContactChange(HttpServletResponse response,Map<String, String> msgMap){
		Object retObj = com.gwm.common.service.Service.exportContactChange(msgMap);
		String retJson = retObj+"";
		List<Object> list=JSONArray.parseArray(retJson);
		Map<String,Object> m=JSON.parseObject(list.get(0).toString());
		if(list.size() > 0 && m.get("errcode")==null){//证明查询都记录
			log.info("开始进行excel处理");			
		    HSSFWorkbook wb = buileExcel(list);
		    response.setContentType("application/vnd.ms-excel");
		    String fileName = "Export.xls";
		    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    
		    BufferedInputStream bis = null;
		    BufferedOutputStream bos = null;
            try {
            	wb.write(os);
            	byte[] content = os.toByteArray();
            	InputStream is = new ByteArrayInputStream(content);
            	// 设置response参数，可以打开下载页面
            	ServletOutputStream out = response.getOutputStream();
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
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                if (bos != null)
					try {
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
			
		}else{
			log.info("不需要导出excel");
		}
		log.info("导出excel成功");
	}
	
	public HSSFWorkbook buileExcel(List<Object>  list){
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet = wb.createSheet("联系方式变更记录统计表");
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
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("状态"); cell.setCellStyle(stylez);
        cell = row.createCell(1);
        cell.setCellValue("联系方式变更单号"); cell.setCellStyle(stylez);
        cell = row.createCell(2);
        cell.setCellValue("申请时间"); cell.setCellStyle(stylez);
        cell = row.createCell(3);
        cell.setCellValue("申请人"); cell.setCellStyle(stylez);
        cell = row.createCell(4);
        cell.setCellValue("合同号"); cell.setCellStyle(stylez);
        cell = row.createCell(5);
        cell.setCellValue("原手机号"); cell.setCellStyle(stylez);
        cell = row.createCell(6);
        cell.setCellValue("变更后手机号"); cell.setCellStyle(stylez);
        cell = row.createCell(7);
        for(int i=0;i<list.size();i++){
        	Map<String,Object> mm=JSON.parseObject(list.get(i).toString());
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
	        if((mm.get("status").toString()).equals("1")){
	        	cell.setCellValue("未受理"); cell.setCellStyle(style);
	        }else if((mm.get("status").toString()).equals("2")){
	        	cell.setCellValue("正常受理"); cell.setCellStyle(style);
	        }else if((mm.get("status").toString()).equals("3")){
	        	cell.setCellValue("已失效"); cell.setCellStyle(style);
	        }else if((mm.get("status").toString()).equals("4")){
	        	cell.setCellValue("完成"); cell.setCellStyle(style);
	        }
	        cell = row.createCell(1);
	        cell.setCellValue(mm.get("tel_reg_num").toString()); cell.setCellStyle(style);
	        cell = row.createCell(2);
	        cell.setCellValue(mm.get("dtime").toString()); cell.setCellStyle(style);
	        cell = row.createCell(3);
	        cell.setCellValue(mm.get("applicationer").toString()); cell.setCellStyle(style);
	        cell = row.createCell(4);
            cell.setCellValue(mm.get("contract_code").toString()); cell.setCellStyle(style);
	        cell = row.createCell(5);
	        cell.setCellValue(mm.get("old_phone").toString()); cell.setCellStyle(style);
	        cell = row.createCell(6);
	        cell.setCellValue(mm.get("new_phone").toString()); cell.setCellStyle(style);
	        cell = row.createCell(7);
        }
        
		return wb;
	}
	
}
