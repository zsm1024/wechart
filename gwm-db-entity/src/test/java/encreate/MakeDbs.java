package encreate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class MakeDbs {
	/*****可修改部分start******/
	public static String[] creatTables = new String[]{//生成dbs函数的表
				"gw_loan_reg",
				"gw_loan_reg_hst",
				"gw_turn_car_model",
				"gw_repay_reg",
				"gw_repay_reg_hst",
				"gw_modify_tel_reg",
				"gw_user_info",
				"gw_user_loan_rel",
				"gw_suggest_reg",
				"gw_history_news_reg",
				"gw_holiday_reg",
				"GW_WX_SYS_MENU",
				"gw_prompt_info",
				"gw_wx_user",
				"gw_param",
				"gw_ccs_online",
				"Gw_internal_staff"
			};
	static String schema = "ydhldbtest";
	public static String url = "jdbc:mysql://10.50.24.222:3306/information_schema?useUnicode=true&characterEncoding=UTF-8";
	public static String username = "root";
	public static String password = "123@456.Com"; 
	/*****可修改部分 end******/
	
	public static String templatePath = "/src/test/resources/";
	public static String driver = "com.mysql.jdbc.Driver";
	public static String[] entity = new String[]{"entity.ftl","main/java/com/gwm/db/entity",""};
	public static String[] entity_meta = new String[]{"entity_meta.ftl","main/java/com/gwm/db/entity/meta","Meta"};
	
	public static Connection con = null;
	
	public static Map<String,List<Map<String,Object>>> tableStruts = new HashMap<String,List<Map<String,Object>>>();
	public static void main(String[] args){
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,username,password);
			make();
			System.out.println(getProject());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void make() throws Exception{
		int i = 0;
		for(String table : creatTables) {
			System.out.println(++i + "-----------------[" + table + "]处理开始-------------------");
			String historyTable = table.replaceAll("_","")+"_H";
			boolean is_exist = prepareTableMysql(table,historyTable);
			if(is_exist){
				createCode(entity,table,historyTable);
				createCode(entity_meta,table,historyTable);
			}else {
				is_exist = true;
				continue;
			}
			System.out.println(i + "--------------------[" + table + "]处理完毕!--------------------");
		}
	}
	
	
	/**
	 * 功能描述：生成DAO 
	 * @ tianming 
	 * @ 2015-8-4
	 */
	@SuppressWarnings("deprecation")
	public static void createCode (String[] type,String table,String historyTableName){
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		/**查找配置文件**/
		/**freemark加载配置文件**/
		Map<String,Object> root = new HashMap<String,Object>();
		String project = getProject();
		try {
			String name = firstUpper(table);
			root.put("tableName", name);
			root.put("columnInfo",tableStruts.get(table));
			cfg.setDirectoryForTemplateLoading(new File(project+"/"+templatePath+"/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper()); 
			Template temp = cfg.getTemplate(type[0],"UTF-8"); //加载代码模版文件
			temp.setEncoding("UTF-8");
			FileOutputStream fileout = new FileOutputStream(project+"/src/"+type[1]+"/"+name+type[2]+".java");
			Writer out = new OutputStreamWriter(fileout,"UTF-8");
		    temp.process(root, out); //将数据输出到模版生成代码文件
		    out.flush();
		    out.close();
		    out = null;
		    temp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 功能描述：取当前工程路径
	 * @return
	 * @ tianming 
	 * @ 2015-8-4
	 */
	public static String getProject(){
		URL directory = Thread.currentThread().getContextClassLoader().getResource("");
		return directory.getPath().split("target")[0];
	}
	/**
	 * 功能：获取表结构等信息
	 * @param tableName
	 */
	public static boolean prepareTable(String tableName,String historyTableName) {
		StringBuffer sql_struts = new StringBuffer();
	 
		//判断是否含有历史表sql语句
		StringBuffer sql_ishistory = new StringBuffer();
		sql_ishistory.append("SELECT count(*) FROM user_tab_columns WHERE table_name='"+historyTableName.toUpperCase()+"'");
		System.out.println("检查是否含有历史表语句，sql_isbak:" + sql_ishistory);
		
		//获取表结构
		sql_struts.append("SELECT a.table_name,a.column_name,a.data_type,a.char_col_decl_length,a.data_precision,a.data_scale,a.nullable ");
		sql_struts.append("FROM user_tab_columns a ");
		sql_struts.append("WHERE a.TABLE_NAME='");
		sql_struts.append(tableName.toUpperCase());
		sql_struts.append("'  order by a.column_id");//按照表结构内顺序生成实体类和dbs函数
		System.out.println("stl_struts:" + sql_struts);
		
		PreparedStatement ps_struts = null;
		ResultSet rs_struts = null;
		
		try {
			 
			Map<String,String[]> map = new HashMap<String,String[]>();
		 
			ps_struts = con.prepareStatement(sql_struts.toString());
			rs_struts = ps_struts.executeQuery();
			
			List<Map<String,Object>> struts = new ArrayList<Map<String,Object>>();
			tableStruts.put(tableName, struts);
			while (rs_struts.next()) {
				Map<String,Object> column = new HashMap<String,Object>();
				String col_name = rs_struts.getString("column_name").toLowerCase();
				column.put("column_name",col_name);
				String data_type = rs_struts.getString("data_type");
				int data_precision = rs_struts.getInt("data_precision");
				int data_scale = rs_struts.getInt("data_scale");
				if("BLOB".equals(data_type)|| "NVARCHAR2".equals(data_type)  || "VARCHAR2".equals(data_type) || "VARCHAR".equals(data_type)|| "CHAR".equals(data_type)||"DATE".equals(data_type)) {
					data_type = "String";
				} else if("NUMBER".equals(data_type) && 0 == data_scale) {
					if(data_precision<=9 && 0 == data_scale){
						data_type = "Int";
					}else{
						data_type = "Long";
					}
				} else if("NUMBER".equals(data_type) && 0 != data_scale) {
					data_type = "Double";
				}
				column.put("data_type",data_type);
				String[] info = map.get(col_name);
				if(info!=null){
					column.put("func_type", info[0]);
					column.put("col_dsc", info[1]);
				}else{
					column.put("func_type", "");
					column.put("col_dsc", "");
				}
				
				column.put("nullable", rs_struts.getString("nullable"));
				if("String".equals(data_type)){
				column.put("data_length", rs_struts.getString("char_col_decl_length"));//字段长度
				}else{
				column.put("data_length", rs_struts.getString("data_precision"));
				}
				struts.add(column);
			}
			if(struts.size()==0){
				return false;
			}
		} catch (Exception e) {
			System.out.println("获取表结构错误！");
			e.printStackTrace();
			System.exit(0);
		} finally {
			//sql_isbak = null;
			sql_struts = null;
		}
		return true;
	}
	
	public static boolean prepareTableMysql(String tableName,String historyTableName) throws Exception {
        PreparedStatement pStemt = con.prepareStatement("SELECT * FROM COLUMNS WHERE TABLE_SCHEMA='"+schema+"' AND TABLE_NAME='"+tableName+"'");  
        ResultSet rsmd = pStemt.executeQuery();  
        List<Map<String,Object>> struts = new ArrayList<Map<String,Object>>();
		tableStruts.put(tableName, struts);
        while(rsmd.next()) {  
            String col_name = rsmd.getString("column_name");  
            String data_type = rsmd.getString("data_type");
            Map<String,Object> column = new HashMap<String,Object>();
			column.put("column_name",col_name.toLowerCase());
	        if(data_type.equalsIgnoreCase("bit")){  
	        	data_type = "Boolean";  
	        }else if(data_type.equalsIgnoreCase("tinyint")){  
	            data_type =  "byte";  
	        }else if(data_type.equalsIgnoreCase("smallint")){  
	            data_type =  "short";  
	        }else if(data_type.equalsIgnoreCase("int")){  
	            data_type =  "Int";  
	        }else if(data_type.equalsIgnoreCase("bigint")){  
	            data_type =  "Long";  
	        }else if(data_type.equalsIgnoreCase("float")){  
	            data_type =  "Float";
	        }else if(data_type.equalsIgnoreCase("decimal") || data_type.equalsIgnoreCase("numeric")   
	                || data_type.equalsIgnoreCase("real") || data_type.equalsIgnoreCase("money")   
	                || data_type.equalsIgnoreCase("smallmoney")){  
	            data_type =  "Double";  
	        }else if(data_type.equalsIgnoreCase("varchar") || data_type.equalsIgnoreCase("char")   
	                || data_type.equalsIgnoreCase("nvarchar") || data_type.equalsIgnoreCase("nchar")   
	                || data_type.equalsIgnoreCase("text")){  
	            data_type =  "String";  
	        }else if(data_type.equalsIgnoreCase("datetime")){  
	            data_type =  "Date";  
	        }else if(data_type.equalsIgnoreCase("mediumblob")){  
	            data_type =  "File";
	        }
	        
	        
	        
			column.put("data_type",data_type);
			column.put("nullable", rsmd.getString("is_nullable"));
			if("String".equals(data_type)){
				column.put("data_length", rsmd.getString("CHARACTER_MAXIMUM_LENGTH"));//字段长度
			}else{
				column.put("data_length", rsmd.getString("NUMERIC_PRECISION"));
			}
			struts.add(column);
        }  
        return true;
	}
	/**
	 * 功能：首字母大写
	 * @param needHead
	 * @return
	 */
	private static String firstUpper(String needHead) {
		return needHead.substring(0,1).toUpperCase()+needHead.substring(1).toLowerCase();
	}
	
}
