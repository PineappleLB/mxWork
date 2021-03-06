package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configs {
	
	/**
	 * 登录配置文件对象属性文件
	 */
	public static Properties configProps = new Properties();
	
	/**
	 * 命令对象属性文件
	 */
	public static Properties orderProps = new Properties();
	
	static {
		FileInputStream fin = null;
		try {
			Properties props = new Properties();
			fin = new FileInputStream(new File("../mxwork.properties"));
			props.load(fin);
			fin.close();
			fin = new FileInputStream(new File(props.getProperty("orderDir")));
			orderProps.load(fin);//通过属性文件加载地址
			fin.close();
			fin = new FileInputStream(new File(props.getProperty("lobbyConfigDir")));
			configProps.load(fin);
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
