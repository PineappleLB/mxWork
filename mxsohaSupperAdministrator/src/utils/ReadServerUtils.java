package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author pineapple
 * @date 2017年12月28日 上午10:18:51
 * @description 服务端读取命令的方法
 */
public class ReadServerUtils 
{
	
	
	/**
	 * 判断命令是否是登录命令
	 * @param order 命令字符串
	 * @return 是否是登录命令
	 */
	public static boolean readOrder(String order)
	{
		try
		{
			//加载属性文件
			Properties props = new Properties();
			
			props.load(new FileInputStream(new File("E:/java/workspace/order.properties")));
			
			//加载属性文件中的命令并判断是否是进入房间命令
			String str = (String)props.get("login");
			
			return str.equals(order);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
}
