package com.gwm.common;


import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SqlManager {

//    private FileSqlManager fsm;
    
    private static Logger log = LoggerFactory.getLogger(SqlManager.class);
    
    private static String OS = System.getProperty("os.name").toLowerCase(); 


    
    /**
     * 取得对应id的sql并从map处理{$paramId$}替换
     *
     * @param name sql的id
     * @param param Parameter用来取得参数
     * @return
     * @throws Exception 
     */
    public static String getSql(String sql, Map<String,?> map) throws Exception {
    	String tmp=getRealSql(sql,map);
    	logInfo(tmp);
    	return tmp;
    }

    private static String getRealSql(String sql,Map<String,?> map) throws Exception{
        Matcher m = Pattern.compile("[{][\\s\\S][^}]*[}]").matcher(sql);
        log.info("********检查字符集开始********");
        if(checkParam(map)){
        	log.info("发现非法字符集");
        	throw new Exception("非法字符集");
        }
        while(m.find())      
        {
            String key=m.group();
            String group=key.replace("{","").replace("}","");
            Matcher m1 = Pattern.compile("[$][\\s\\S][^$]*[$]").matcher(key);
            if(m1.find()){
                String xgroup=m1.group();
                String xkey=xgroup.replace("$","");
                if(map.get(xkey)!=null){
                    group=group.replace(xgroup,(map.get(xkey)+"").trim()).replaceAll("\\$","#@9912831923#@");
                    sql=sql.replace(key,group);
                }
                else{
                    sql=sql.replace(key,"");
                }
            }else{
                sql=sql.replace(key,"");
            }
        }
        Matcher m1 = Pattern.compile("[$][\\s\\S][^$]*[$]").matcher(sql);
        while(m1.find()){
            String group=m1.group();
            String key=group.replace("$","");
            if(map.get(key)==null)
                throw new Exception("缺少参数"+key);
            sql=sql.replace(group,(map.get(key)+"").trim());
        }
        return sql.replaceAll("#@9912831923#@","\\$");
    }
    /**
     * 只用更新语句记录日志
     * @param sql
     */
    private static void logInfo(String sql){
    	if(sql==null||sql.contains("$")){
    		return;
    	}
    	String loweerCaseSql=sql.toLowerCase();
    	String[] strs={"insert ","update ","delete ","merge ","select "};
    	boolean flag=false;
    	for(String str:strs){
    		if(loweerCaseSql.contains(str)){
    			flag=true;
    			break;
    		}
    	}
    	if(flag)
    		log.info("SQL="+sql);
    }
    
    /**
     *  sql注入验证
     * */
    private static boolean checkParam(Map<String,?> mp){
    	if(mp.size()>0){
    		for(Map.Entry<String, ?> entry :mp.entrySet()){
    			String str=entry.getValue()==null?"":entry.getValue().toString();
    			str = str.toLowerCase();//统一转为小写
    			String badStr = "and |insert |select |delete |update | count|drop | like|create |from |table |or |order |alter |where |information_schema.columns|table_schema|union |net user|xp_cmdshell" +
    	        		"|truncate|column_name |script|alert";
    			String[] badStrs = badStr.split("\\|");
    	        for (int i = 0; i < badStrs.length; i++) {
    	            if (str.indexOf(badStrs[i]) >= 0) {            	
    	            	System.out.println("非法字符="+str+":"+badStrs[i]);
    	                return true;
    	            }
    	        }
    		}
    	}
    	return false;
    }
    public static void main(String args[]){
    	Map<String,String> map=new HashMap<String,String>();
    	map.put("openid", "o5iChjnU-LkOpFBYNpA4y4Jqgpa4");
    	boolean f=checkParam(map);
    	System.out.println(f);
    }
}