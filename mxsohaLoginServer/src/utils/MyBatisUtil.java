package utils;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;

/**
 * mybatis的获取会话工厂类
 * @author Pineapple
 *
 */
public class MyBatisUtil {
	private static SqlSessionFactory factory;
	static{
		InputStream is = null;
		try {
			is = Resources.getResourceAsStream("config.xml");
			factory = new SqlSessionFactoryBuilder().build(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void resetSqlSessionFactory() {
		InputStream is = null;
		try {
			is = Resources.getResourceAsStream("config.xml");
			factory = new SqlSessionFactoryBuilder().build(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSession getSession(){
		return SqlSessionManager.newInstance(factory);
	}
}
