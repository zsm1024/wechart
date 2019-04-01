package com.wx.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCurl{
	
	static Logger log = LoggerFactory.getLogger(HttpCurl.class);
	
	/**
	 * http get方法
	 * @param url
	 * @return
	 */
	public static String get(String url){
		String json ="";
		String str = "";
		String []cmds={"curl",url};
		for(int i = 0; i < cmds.length; i++){
			log.info("cmds["+i+"]:"+cmds[i]);
		}
		ProcessBuilder pb=new ProcessBuilder(cmds);
		pb.redirectErrorStream(true);
		Process p;
		try{
			p=pb.start();
			BufferedReader br=null;
			String line=null;
			br=new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line=br.readLine())!=null){
				str += line;
			}
			br.close();
			int n = str.indexOf("{");
			if(n != -1){
				json = str.substring(n);
			}
		}catch(Exception ex){
			log.error("curl错误！", ex);
		}
		return json;
	}
	
	/**
	 * http post方法
	 * @param url
	 * @return
	 */
	public static String post(String url, String data) throws Exception {
		String json = "";
		String str = "";
		log.info("发送数据："+data);
		String osName = System.getProperties().getProperty("os.name");
		if(osName.indexOf("Window")>=0){
			data = data.replaceAll("\\\"", "\\\\\"");
		}
        System.out.println("------------------------------------");
        log.info("发送数据--："+data);
		System.out.println(data);
        try{
            String []cmds = {"curl","-d", data, url, "-k"};
            ProcessBuilder pb = new ProcessBuilder(cmds);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = null;
            String line = null;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line = br.readLine()) != null)
            {
                str += line;
            }
            br.close();
            System.out.println("str:::"+str);
            int n = str.indexOf("{");
			if (n != -1) {
				json = str.substring(n);
			}
			return json;
        }catch(Exception ex){
            log.error("curl发送数据异常：", ex);
            return "9999";
        }
	}
	
	public static String downLoadFile(String url, String filePath, String fileName){
		
		String str = "";
		log.info("文件路径:"+filePath);
		log.info("文件名称："+fileName);
		String saveFile = filePath+fileName;
		log.info("保存文件:"+ saveFile);
		String []cmds={"curl", "-o", saveFile, url};
		for(int i = 0; i < cmds.length; i++){
			log.info("cmds["+i+"]:"+cmds[i]);
		}
		ProcessBuilder pb=new ProcessBuilder(cmds);
		pb.redirectErrorStream(true);
		try{
			Process p = pb.start();
            BufferedReader br = null;
            String line = null;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line = br.readLine()) != null)
            {
            }
            br.close();
			File file = new File(saveFile);
	        InputStream in = null;
	            // 一次读一个字节
	        in = new FileInputStream(file);
            byte b[] = new byte[10];
			if (in.read(b) != -1) {
				if (b[0] == 123) {
					str += new String(b, "UTF-8");
					while (in.read(b) != -1) {
						str += new String(b, "UTF-8");
					}
				}
			}
			log.info("上传结果:"+str);
			in.close();
		}catch(Exception ex){
			log.error("curl错误！", ex);
		}
		return str;
	}
	
	public static void main(String args[]){
		
	}
	
}