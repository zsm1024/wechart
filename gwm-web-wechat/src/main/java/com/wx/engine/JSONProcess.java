/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

import com.alibaba.fastjson.JSON;

/**
 * json数据类型解析类
 * @author admin
 */
public class JSONProcess {
    /**
     * 根据path 获取json字符串里面的数据
     *  path支持函数
     *  例子
     *   getJSON("{key:'hello world'}",".key.substring(0,5)");
     *   返回数据位 hello
     * @param jsonstr json字符串
     * @param path 请求路径
     * @return 
     */
    public static String getJSON(String jsonstr,String path){
        Context cx = Context.enter();
        try {
            String str="var s="+jsonstr+";s"+path+"";
            cx.setOptimizationLevel(-1);
            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, str, null, 1, null);
            return Context.toString(result);
        } catch (JavaScriptException ex) {
            Logger.getLogger(JSONProcess.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cx.exit();
        }
        return "";
    }
    
    /**
     * json转list
     * @param json
     * @return
     */
    public static List<Map<String, Object>> jsonToList(String json){
    	List list = JSON.parseArray(json);
    	return list;
    }
    
    public static String addElement(String jsonstr,String key,String value){
        Context cx = Context.enter();
        try {
            String str="var s="+jsonstr+";s."+key+"=\""+value+"\";JSON.stringify(s)";
            cx.setOptimizationLevel(-1);
            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, str, null, 1, null);
            return Context.toString(result);
        } catch (JavaScriptException ex) {
            Logger.getLogger(JSONProcess.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cx.exit();
        }
        return "";
    }
    
    public static String addElement(String jsonstr,String path,String key,String value){
        Context cx = Context.enter();
        try {
            String str="var s="+jsonstr+";s"+path+"."+key+"=\""+value+"\";JSON.stringify(s)";
            cx.setOptimizationLevel(-1);
            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, str, null, 1, null);
            return Context.toString(result);
        } catch (JavaScriptException ex) {
            Logger.getLogger(JSONProcess.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cx.exit();
        }
        return "";
    }
    
    
    
    /**
     * 获取number类型数据 通getJSON
     * @param jsonstr
     * @param path
     * @return 
     */
    public static Double getJSONforNumber(String jsonstr,String path){
        Context cx = Context.enter();
        try {
            String str="var s="+jsonstr+";s"+path+"";
            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, str, null, 1, null);
            return Context.toNumber(result);
        } catch (JavaScriptException ex) {
            Logger.getLogger(JSONProcess.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cx.exit();
        }
        return null;
    }
    
    /**
     * 通过js的function获取想要的返回值
     * @param jsonstr
     * @param function
     * @return 
     */
    public static String getJsonbyFunction(String jsonstr,String function){
        Context cx = Context.enter();
        try {
            String str="var s="+jsonstr+";("+function+")(s)";
            cx.setOptimizationLevel(-1);
            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, str, null, 1, null);
            return Context.toString(result);
        } catch (JavaScriptException ex) {
            Logger.getLogger(JSONProcess.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cx.exit();
        }
        return "";
    }
   
   /**
    * 根据map替换{key}为map.get(key)实现字符串格式化
    * 支持显示部分内容，其余显示*
    * {all|accno} 表示显示所有文字
    * {last4|accno} 后4位显示，其余使用*代替
    * @param formatter
    * @param map
    * @return 
    */
   public static String formatterString(String formatter,HashMap map){
        Matcher m = Pattern.compile("[{][^}]*[}]").matcher(formatter);
        while(m.find())      
        {
            String key=m.group();
            String group=key.replace("{","").replace("}","");
            String result = "";
            if(group.indexOf("|")!=-1){
                String[] elems = group.split("\\|");
                if("all".equals(elems[0].trim())){
                    result = (String)map.get(elems[1].trim());
                }else if("last4".equals(elems[0].trim())){
                    result = (String)map.get(elems[1].trim());
                    if(result.length()>4){
                        int len = result.length()-4;
                        String cover = "";
                        for(int i=0;i<len;i++){
                            cover+="*";
                        }
                        result = result.replace(result.substring(0,result.length()-4), cover);
                    }
                }else if("double".equals(elems[0].trim())){
                     result = (String)map.get(elems[1].trim());
                     if(result.length()>2){
                        String i = Long.parseLong(result.substring(0,result.length()-2))+"";
                        result = i+"."+result.substring(result.length()-2);
                     }else{
                         result = "0."+result;
                     }
                }
            }else{
                result = (String) map.get(group);
            }
            if(result!=null)
                formatter=formatter.replace(key,result);
        }
        return formatter;
    }
   
   public static void main(String ar[]){
       //String json = "{\"Data\":{\"id\":\"1\",\"name\":\"zhanglin\",\"course\":{\"moudle1\":\"math\"}}}";
       //System.out.println(JSONProcess.addElement(json,".Data.course", "moudle2", "physics"));
   }
}
