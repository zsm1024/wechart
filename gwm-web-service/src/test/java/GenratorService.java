
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
public class GenratorService {
	public static void main(String[] args) {
		String path = getProject()+"target/classes";
		File file = new File(path);
		try {
			System.out.println("==============================生成服务访问代码 start============================");
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			readJavaFile(path,file,list);
			createJavaCode(list);
			System.out.println("==============================生成服务访问代码   end ============================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void readJavaFile(String root,File file,List<Map<String,String>> list)throws Exception{
		File[] files = file.listFiles();
		for(File f:files){
			if(f.isDirectory()){
				readJavaFile(root,f,list);
			}else if(f.getName().endsWith("class")&&f.getName().indexOf("$")==-1){
				Class<?> cl;
				String classpath = getPackage(f,root);
				cl = Class.forName(classpath);
				RestController rc = cl.getAnnotation(RestController.class);
				Description classDesc = cl.getAnnotation(Description.class);
				if(rc!=null){
					RequestMapping rm = cl.getAnnotation(RequestMapping.class);
					Method[] methods = cl.getMethods();
					for(Method m:methods){
						RequestMapping mapping = m.getAnnotation(RequestMapping.class);
						Description desc = m.getAnnotation(Description.class);
						if(mapping!=null){
							String temp = rm.value()[0]+mapping.value()[0];
							Map<String,String> map = new HashMap<String,String>();
							map.put("name", m.getName());
							map.put("url", temp);
							
							String classDs = classDesc==null?"":classDesc.value();
							if(desc!=null){
								map.put("desc", classDs+"-"+desc.value());
							}else{
								map.put("desc", classDs+"none");
							}
							list.add(map);
						}
					}
				}
			}
		}
	}
	private static String getPackage(File f,String root){
		String path = f.getAbsolutePath();
		root = root.replaceFirst("/","").replace("/", "\\");
		path = path.replace(root+"\\", "").replace("\\", ".").replace(".class", "");
		return path;
	}
	
	@SuppressWarnings("deprecation")
	public static void createJavaCode(List<Map<String,String>> list){
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		/**查找配置文件**/
		/**freemark加载配置文件**/
		Map<String,Object> root = new HashMap<String,Object>();
		String project = getProject();
		try {
			root.put("services", list);
			root.put("date", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			cfg.setDirectoryForTemplateLoading(new File(project+"/src/test/resources/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper()); 
			Template temp = cfg.getTemplate("service.ftl","UTF-8"); //加载代码模版文件
			temp.setEncoding("UTF-8");
			FileOutputStream fileout = new FileOutputStream(project+"../gwm-common/src/main/java/com/gwm/common/service/Service.java");
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
}
