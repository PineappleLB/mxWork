package mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import model.SeatsInfo;
import model.User;
import model.UserInfo;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:04:03
 * @description 用户操作接口
 */
public interface UserMapper {

	/**
	 * 更新用户金币
	 * @param d
	 * @return 
	 */
	int updateMoney(@Param(value="money")double d,@Param(value="id")int id);

	/**
	 * 通过id查询用户名
	 * @param userId
	 * @return
	 */
	String getUserNameById(@Param(value="id")int userId);

	/**
	 * 更新用户校验id
	 * @param checkId
	 * @return
	 */
	int updateCheckId(@Param(value="checkId")int checkId,@Param(value="id")int id,@Param(value="score")int score);

	/**
	 * 获取座位信息
	 * @param roomId
	 * @param seatId
	 * @return
	 */
	SeatsInfo getSeatsInfo(@Param(value="id")int seatId);

	/**
	 * 根据用户id获取用户信息
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfo(@Param(value="userId")int userId);

	/**
	 * 根据用户id查询用户对象
	 * @param userId
	 * @return
	 */
	User getUserById(@Param(value="userId")int userId);

	/**
	 * 更新用户游戏信息
	 * @param score
	 * @param gameScore
	 */
	int updateUserInfo(@Param(value="payScore")int score, @Param(value="gotScore")int gameScore,@Param(value="userId")int id);

	/**
	 * 更新座位游戏信息
	 * @param score
	 * @param gameScore
	 * @param seatId
	 * @return
	 */
	int updateSeatInfo(@Param(value="payScore")int score, @Param(value="gotScore")int gameScore, @Param(value="id")int seatId);

	/**
	 * 更新座位信息中的座位集合值
	 * @param b
	 * @param roomId
	 * @param seatId
	 * @return
	 */
	int updateSeatListNum(@Param(value="num")int b, @Param(value="id")int seatId,@Param(value="payScore")int score,@Param(value="resetNum")int resetNum);

	/**
	 * 更新座位信息的比倍信息
	 * @param score
	 * @param gameScore
	 * @param seatId
	 * @return
	 */
	int updateSeatDoubleScore(@Param(value="payScore")int score,  @Param(value="gotScore")int gameScore, @Param(value="id")int seatId);

	/**
	 * 查询座位的游戏记录信息
	 * @param seatId
	 * @return
	 */
	Map<String, Object> selectSeatRecords(@Param(value="seatId")int seatId);

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
	void addRecords(@Param(value="userId")int userId, @Param(value="seatId")Integer seatId, @Param(value="isDouble")int i, 
			@Param(value="payScore")int gameScore, @Param(value="double")int result, @Param(value="gotScore")int j, @Param(value="roomDouble")int multiplyPower);
	
	

}