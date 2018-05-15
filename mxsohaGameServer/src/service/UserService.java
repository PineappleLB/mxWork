package service;

import java.sql.SQLException;
import java.util.Map;

import model.SeatsInfo;
import model.User;
import model.UserInfo;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:09:48
 * @description 用户操作接口
 */
public interface UserService {

	/**
	 * 根据用户id获取用户名
	 * @param userId
	 * @return
	 */
	String getUserName(int userId) throws SQLException ;

	/**
	 * 更新金币
	 * @param d
	 */
	void updateMoney(double d,int id) throws SQLException ;
	
//	/**
//	 * 更新数据库中的用户校验id
//	 * @param bytesToInt
//	 * @return
//	 */
//	int updateCheckId(int checkId,int id) throws SQLException ;

	/**
	 * 查询座位信息
	 * @param roomId
	 * @param seatId
	 * @return
	 */
	SeatsInfo getSeatsInfo(int roomId, int seatId, int payScore) throws SQLException ;

	/**
	 * 根据用户id查询用户信息
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfo(int userId) throws SQLException ;

	/**
	 * 根据用户id查询用户对象
	 * @param userId
	 * @return
	 */
	User getUserById(int userId) throws SQLException ;

	/**
	 * 更新用户的游戏信息
	 * @param score
	 * @param gameScore
	 */
	void updateUserInfo(int score, int gameScore,int id) throws SQLException ;

	/**
	 * 更新座位的游戏信息
	 * @param score
	 * @param gameScore
	 * @param seatId
	 * @param roomId
	 */
	void updateSeatInfo(int score, int gameScore, Integer seatId) throws SQLException ;


	/**
	 * 更新用户和座位的信息
	 * @param checkId
	 * @param id
	 * @param b
	 * @param roomid
	 * @param seatId
	 */
	void updateUserAndSeatInfo(int checkId, int id, byte b, int roomid, int seatId,int score,int resetNum);

	/**
	 * 更新座位的比倍信息
	 * @param score 
	 * @param gameScore
	 * @param seatId
	 */
	void updateSeatDoubleScore(int score, int gameScore, int seatId);

	/**
	 * 查询座位的游戏记录信息
	 * @param seatId
	 * @return
	 */
	Map<String,Object> selectSeatRecords(int seatId);
//	JSONObject selectSeatRecords(int seatId);

	/**
	 * 添加游戏记录
	 * @param userId
	 * @param seatId
	 * @param i
	 * @param gameScore
	 * @param result
	 * @param j
	 * @param multiplyPower
	 */
	void addRecords(int userId, Integer seatId, int i, int gameScore, int result, int j, int multiplyPower);

	/**
	 * 查询用户金币余额
	 * @param userId
	 * @return
	 */
	double getUserMoneyById(int userId);
	

}
