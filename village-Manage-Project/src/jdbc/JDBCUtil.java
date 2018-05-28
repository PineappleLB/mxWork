package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Admin;
import model.WarningConfig;

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
	public static int adminLogin(String name, String pass) {
		Admin admin = selectAdminByName(name);
		if(admin != null && admin.getPassword().equals(pass)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 根据姓名查询用户信息
	 * @param name
	 * @return
	 */
	private static Admin selectAdminByName(String name) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "select id, name, password, token from admin where name = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			Admin admin = null;
			while(rs.next()) {
				admin = new Admin();
				admin.setId(rs.getInt("id"));
				admin.setName(rs.getString("name"));
				admin.setPassword(rs.getString("password"));
				admin.setToken(rs.getString("token"));
			}
			return admin;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int adminRegist(String name, String pass) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "insert into admin (id, name, password, token) "
					+ "values(default, ?, ?, ?)";
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

	/**
	 * 查询所有单元
	 * @return
	 */
	public static String[] selectUnits() {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT unit FROM `USER` GROUP BY unit;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<String> strs = new ArrayList<>();
			while(rs.next()) {
				strs.add(rs.getInt("unit") + "单元");
			}
			String []arr = new String[strs.size()];
			strs.toArray(arr);
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] selectUsernamesByUnit(int unit) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT `NAME` FROM `USER`";
			if(unit > 0) {
				sql += " WHERE unit = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, unit);	
				rs = stmt.executeQuery();
			} else {
				PreparedStatement stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
			}
			List<String> strs = new ArrayList<>();
			while(rs.next()) {
				strs.add(rs.getString("name"));
			}
			String []arr = new String[strs.size()];
			strs.toArray(arr);
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] selectAlertItems() {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT alertName FROM alertSelection;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<String> strs = new ArrayList<>();
			while(rs.next()) {
				strs.add(rs.getString("alertName"));
			}
			String []arr = new String[strs.size()];
			strs.toArray(arr);
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static WarningConfig selectWarningConfigByUsername(String username, String alertItem) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT warningConfig.id, alertSelection.alertName, safeMin, safeMax, `NAME` FROM warningConfig "
				+ "LEFT JOIN USER ON user.id=userId LEFT JOIN alertSelection ON alertSelection.id=warningConfig.alertName"
				+ "WHERE alertSelection.alertName = ? AND `NAME` = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, alertItem);
			stmt.setString(2, username);
			rs = stmt.executeQuery();
			WarningConfig config = null;
			while(rs.next()) {
				config = new WarningConfig();
				config.setAlertname("alertSelection.alertName");
				config.setSafemax(rs.getInt("safeMax"));
				config.setSafemin(rs.getInt("safeMin"));
				config.setUserid("name");
			}
			return config;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int saveWarningMessage(String username, String alertItem, int value) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "INSERT INTO alertInfo(alertName, `TIME`, safeMin, safeMax, userId,VALUE) " + 
					"SELECT DISTINCT alertName,NOW(),safeMin,safeMax,userId,? FROM `USER` LEFT JOIN warningConfig ON warningConfig.userId=user.id " + 
					"WHERE warningConfig.alertName=(SELECT id FROM alertSelection WHERE alertName= ? ) " + 
					"AND user.id=(SELECT id FROM `USER` WHERE user.name= ? )";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, value);
			stmt.setString(2, alertItem);
			stmt.setString(3, username);
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int saveWarningMessage(String username, String alertItem, int value, String date) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "INSERT INTO alertInfo(alertName, `TIME`, safeMin, safeMax, userId,VALUE) " + 
					"SELECT DISTINCT alertName, ?,safeMin,safeMax,userId,? FROM `USER` LEFT JOIN warningConfig ON warningConfig.userId=user.id " + 
					"WHERE warningConfig.alertName=(SELECT id FROM alertSelection WHERE alertName= ? ) " + 
					"AND user.id=(SELECT id FROM `USER` WHERE user.name= ? )";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, date);
			stmt.setInt(2, value);
			stmt.setString(3, alertItem);
			stmt.setString(4, username);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String selectRoomByUsername(String username) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT room FROM `USER` WHERE `NAME` = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getString("room");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] selectRoomsByUnit(int unit) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT room FROM `USER`";
			if(unit > 0) {
				sql += " WHERE unit = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, unit);	
				rs = stmt.executeQuery();
			} else {
				PreparedStatement stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
			}
			List<String> strs = new ArrayList<>();
			while(rs.next()) {
				strs.add(rs.getString("room"));
			}
			String []arr = new String[strs.size()];
			strs.toArray(arr);
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String selectUsernameByRoom(String room) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT `NAME` FROM `USER` WHERE room = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, room);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object[][] selectAlertTableModels(String username, String alertType) {
		try {
			conn = Jdbc.getMyConnection();
			sql = "SELECT alertSelection.alertName as alertType,`TIME`,safeMin,safeMax,`VALUE`,`NAME` FROM alertInfo " + 
					"LEFT JOIN alertSelection ON alertInfo.alertName=alertSelection.id LEFT JOIN `USER` ON user.id=alertInfo.userId" + 
					" WHERE user.id IN (SELECT id FROM `USER` WHERE `NAME`=?)";
			//alertSelection.alertName=? AND 
			if("任意".equals(alertType)) {
				PreparedStatement stmt = conn.prepareStatement(sql);
//				stmt.setString(1, alertType);
				stmt.setString(1, username);
				rs = stmt.executeQuery();
			} else {
				sql += " AND alertSelection.alertName=?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, username);
				stmt.setString(2, alertType);
				rs = stmt.executeQuery();
			}
			List<Object[]> list = new ArrayList<>();
			while(rs.next()) {
				Object[] objs = new Object[5];
				objs[0] = rs.getString("alertType");
				objs[1] = rs.getString("time");
				objs[2] = rs.getInt("safeMin") + "-" + rs.getInt("safeMax");
				objs[3] = rs.getInt("value");
				objs[4] = rs.getString("name");
				list.add(objs);
			}
			if(list.size() > 0 && list.get(0) != null && list.get(0).length > 0) {
				Object[][] returnObjs = new Object[list.size()][list.get(0).length];
				list.toArray(returnObjs);
				return returnObjs;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
