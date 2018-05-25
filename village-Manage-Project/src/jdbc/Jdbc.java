package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class Jdbc {
	private static String DBDriver = "com.mysql.jdbc.Driver";
	private static String DBURL = "jdbc:mysql://localhost:3306/Home";
	private static String DBUser = "root";
	private static String DBPass = "123456";
	private static Connection conn = null;

	/**
	 * 创建数据库连接
	 * @param driver
	 * @param dburl
	 * @param user
	 * @param pass
	 * @return
	 */
	private static Connection initConnection(String driver, String dburl, String user, String pass) {
		DBDriver = driver == null ? DBDriver : driver;
		DBURL = dburl == null ? DBURL : dburl;
		DBUser = user == null ? DBUser : user;
		DBPass = pass == null ? DBPass : pass;
		try {
			Class.forName(DBDriver);
			return DriverManager.getConnection(DBURL, DBUser, DBPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public static Connection getMyConnection() {
		if(conn == null) {
			conn = initConnection(null, null, null, null);
		}
		return conn;
	}

	
	public void closeMyConnetion() {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
