package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MyDBConnection{
	private String DBDriver = "com.mysql.jdbc.Driver";
    private String DBURL = "jdbc:mysql://localhost:3306/Home";
    private String DBUser = "root";
    private String DBPass = "123456";
    private Connection conn=null;
    public MyDBConnection(String driver,String dburl,String user,String pass){
    	DBDriver = driver == null ? DBDriver : driver;
    	DBURL    = dburl == null ? DBDriver : dburl;
    	DBUser   = user == null ? DBDriver : user;
    	DBPass   = pass == null ? DBDriver : pass;
    	try {
			Class.forName(DBDriver);
			conn = DriverManager.getConnection(DBURL, DBUser, DBPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public Connection getMyConnection() {
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
public class Jdbc {
	public static void main(String[] args) throws SQLException {
		String DBDriver = "com.mysql.jdbc.Driver";
		String DBURL    = "jdbc:mysql://localhost:3306/Home";
		String DBUser   = "root";
		String DBPass   = "123456";
		MyDBConnection  myDB= new MyDBConnection(DBDriver,DBURL,DBUser,DBPass);
		Connection conn = myDB.getMyConnection();
		System.out.println(myDB);
		conn.close();
	}
}
