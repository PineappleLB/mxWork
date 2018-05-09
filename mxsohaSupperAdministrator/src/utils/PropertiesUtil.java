package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author pineapple
 * @date 2017年12月28日 上午11:34:37
 * @description 获取属性文件的帮助类
 */
public class PropertiesUtil {

	
	/**
	 * 获取大厅服务器端端口号
	 * @return
	 */
	public static int getLobbyPort(){
		
		try
		{
			//加载属性文件中的端口号
			Properties props = new Properties();
			props.load(new FileInputStream(new File("src/db.properties")));
			int port = Integer.parseInt((String)props.get("port"));
			return port;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	
}
