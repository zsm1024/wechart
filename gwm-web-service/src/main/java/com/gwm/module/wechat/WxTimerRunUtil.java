package com.gwm.module.wechat;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.gwm.common.SpringUtil;
import com.gwm.common.SqlManager;

/**
 * 微信项目定时类
 * */
public class WxTimerRunUtil {
	
	private static Logger log = LoggerFactory.getLogger(WxTimerRunUtil.class);
	/**
	 * 执行定时任务入口
	 * @param map
	 */
	@Transactional
	public static void run(Map<String,Object> task){
		String task_name=(String)task.get("task_name");//任务名称
		log.info("进入定时任务【"+task_name+"】>>>task="+task);
		boolean flag=false;
        try {
        	flag=startTask(task_name);
        	log.info("startTask>>>>>>执行状态="+flag);
        	//修改状态成功才能继续执行
        	if(flag)
        	{
        		try{
        			String class_name=(String)task.get("class_name");//class  路径及类名
    	            String method_name=(String)task.get("method_name");//方法名
    				Object bean =Class.forName(class_name).newInstance();
    				Method method =bean.getClass().getMethod(method_name,String.class);
    				flag=(Boolean)method.invoke(bean,task_name);        		
	        	} catch (Exception e) {
	            	log.error("执行定时任务方法[task="+task+"]出现异常：", e);
	            	flag=false;
	            }
        	}
        	flag=endTask(task,flag);
        	log.info("endTask>>>>>>执行状态="+flag);            
        } catch (Exception e) {
        	System.out.println("可配置定时线程任务["+task_name+"]出现异常："+e);
        	log.error("可配置定时线程任务["+task_name+"]出现异常：", e);
        }finally{
        	log.info("定时任务【"+task_name+"执行结束，执行结果【"+"】");
        }
        
	}
	
	/**
	 * 任务开始执行
	 * @param taskName
	 * @return
	 */
	private static boolean startTask(String taskName){
		try {
			String sql ="update gw_wx_timer_task set execute_flag='1' where  task_name='"+taskName+"' and execute_flag='0'";
			log.info("SQL="+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			int i=jdbc.update(sql);
			if(i==1)
			{
				return true; 
		    }
        } catch (Exception e) {
        	log.error("出现异常：", e);
        }
		return false;
	}
	
	/**
	 * 任务开始执行
	 * @param mp   
	 * @param flag 
	 * @return
	 */
	private static boolean endTask(Map<String,Object> mp,boolean flag){
		try {
			String execute_cycle=(String)mp.get("execute_cycle");
			String last_execute_time=(String)mp.get("execute_time");
			String execute_time="20991201000000";
			//默认执行一次
			if(!"0".equals(execute_cycle))
			{
				SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = sdf.parse(last_execute_time);
				Calendar calendar=Calendar.getInstance();   
				calendar.setTime(date);
				if("3".equals(execute_cycle)){
					//周
					calendar.add(Calendar.DAY_OF_YEAR,7); 
				}else if("2".equals(execute_cycle)){
					//月
					calendar.add(Calendar.MONTH,1); 
				}else if("1".equals(execute_cycle)){
					//年
					calendar.add(Calendar.YEAR,1); 
				}else if("5".equals(execute_cycle)){
					//小时
					calendar.add(Calendar.HOUR, 3);
				}else if("6".equals(execute_cycle)){
					//不变
				}else {
					//日
					calendar.add(Calendar.DAY_OF_YEAR,1); 
				}
				execute_time=sdf.format(calendar.getTime());
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name", mp.get("task_name"));
			String sql="";
			if(flag)//执行成功
			{
				map.put("execute_flag", "0");
				map.put("execute_time", execute_time);
				map.put("last_execute_flag", "2");
				map.put("last_execute_time", last_execute_time);
				sql="update gw_wx_timer_task set execute_flag='$execute_flag$',execute_time='$execute_time$',last_execute_flag='$last_execute_flag$',last_execute_time='$last_execute_time$' where  task_name='$name$'";
				sql = SqlManager.getSql(sql,map);
			}
			else //执行失败
			{
				map.put("execute_flag", "3");
				sql="update gw_wx_timer_task set execute_flag='$execute_flag$' where  task_name='$name$'";
				sql = SqlManager.getSql(sql,map);
			}
			log.info("SQL="+sql);
			JdbcTemplate jdbc=SpringUtil.getBean(JdbcTemplate.class);
			int i=jdbc.update(sql);
			if(i==1)
			{
				return true; 
		    }
        } catch (Exception e) {
        	log.error("出现异常：", e);
        }
		return false;
	}
}
