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
	
	/**
	 * 5条的最大分数上线
	 */
	public static int max_5K;
	
	/**
	 * 5条的最小分数下限
	 */
	public static int min_5K;
	
	/**
	 * 4条的最大分数上限
	 */
	public static int max_4K;
	
	/**
	 * 4条的最小分数下限
	 */
	public static int min_4K;
	
	/**
	 * 同花顺的最大分数上限
	 */
	public static int max_SF;
	
	/**
	 * 同花顺的最小分数下限
	 */
	public static int min_SF;
	
	/**
	 * 同花大顺的最大分数上线
	 */
	public static int max_RS;
	
	/**
	 * 同花顺的最小分数下限
	 */
	public static int min_RS;
	
	public static int seatNum;
	
	public static int roomNum;
	
	/**
	 * 随机牌的json文件路径
	 */
	public static String jsonCardFileDir;
	
	static {
		FileInputStream fin = null;
		try {
			Properties props = new Properties();
			fin = new FileInputStream(new File("../mxwork.properties"));
			props.load(fin);
			jsonCardFileDir = props.getProperty("jsonFileDir");
			fin.close();
			fin = new FileInputStream(new File(props.getProperty("orderDir")));
			orderProps.load(fin);//通过属性文件加载地址
			fin.close();
			fin = new FileInputStream(new File(props.getProperty("gameConfigDir")));
			configProps.load(fin);
			fin.close();
			max_5K=Integer.parseInt(configProps.getProperty("max_5K"));
			min_5K=Integer.parseInt(configProps.getProperty("min_5K"));
			max_4K=Integer.parseInt(configProps.getProperty("max_4K"));
			min_4K=Integer.parseInt(configProps.getProperty("min_4K"));
			max_SF=Integer.parseInt(configProps.getProperty("max_SF"));
			min_SF=Integer.parseInt(configProps.getProperty("min_SF"));
			max_RS=Integer.parseInt(configProps.getProperty("max_RS"));
			min_RS=Integer.parseInt(configProps.getProperty("min_RS"));
			seatNum = Integer.parseInt(configProps.getProperty("seat.num"));
			roomNum = Integer.parseInt(configProps.getProperty("roomNum"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
