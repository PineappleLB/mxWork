package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class JDBCUtil {
	
	//数据库连接对象
	private static Connection conn;
	
	//sql语句
	private static String sql;
	
	private static ResultSet rs;

	/**
	 * 判断用户名和密码是否正确
	 * @param name
	 * @param pass
	 * @return
	 */
	public static int userLogin(String name, String pass) {
		User u = selectUserByName(name);
		if(u != null && u.getPass().equals(pass)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 根据姓名查询用户信息
	 * @param name
	 * @return
	 */
	private static User selectUserByName(String name) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "select id, name, pass, token, build, unit, room from user where name = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			User u = null;
			while(rs.next()) {
				u = new User();
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("name"));
				u.setPass(rs.getString("pass"));
				u.setToken(rs.getString("token"));
				u.setBuild(rs.getInt("build"));
				u.setUnit(rs.getInt("unit"));
				u.setRoom(rs.getString("room"));
			}
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int userRegist(String name, String pass) {
		try {
			conn = Jdbc.getMyConnection();
			//TODO 修改sql语句
			sql = "insert into user (id, name, pass, token, build, unit, room) "
					+ "values(default, ?, ?, ?, 1, 1, '1001')";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, pass);
			stmt.setString(3, pass);
			int result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
}
