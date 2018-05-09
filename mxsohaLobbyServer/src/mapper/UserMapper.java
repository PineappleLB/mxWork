package mapper;

import org.apache.ibatis.annotations.Param;

import model.User;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:04:03
 * @description 用户操作类接口
 */
public interface UserMapper {

	/**
	 * 用户退出登录
	 * @param id 检索用户对象的ID
	 * @return 返回受影响的行数
	 */
	int userLoginout(@Param(value="id")Integer id, @Param(value="status")int status);
	
	/**
	 * 根据ID查找用户名
	 * @param id
	 * @return
	 */
	String getUserName(@Param(value="id")Integer id);

	/**
	 * 根据用户ID查询用户金币余额
	 * @param userId
	 * @return
	 */
	double getMoneyById(@Param(value="id")int userId);

	/**
	 * 将超时的暂离用户状态设置为离线
	 */
	void updateTimeOutUser();

	/**
	 * 更新超过游戏次数上限的用户信息表数据
	 */
	void updateOutBoundsUserInfoGameTime();

	/**
	 * 更新超过游戏次数上限的座位信息表数据
	 */
	void updateOutBoundsSeatsInfoGameTime();

	/**
	 * 根据Id查找user对象
	 * @param userId 用户id
	 * @return
	 */
	User selectUserById(@Param(value="id")int userId);

	/**
	 * 更新留机次数
	 * @param time 次数
	 * @param id 用户id
	 */
	void updateStaySeatTime(@Param(value="time")int time,@Param(value="id")int id);

	/**
	 * 更新用户状态
	 * @param i
	 * @param id
	 */
	void updateUserStatus(@Param(value="status")int status, @Param(value="id")int id);

	/**
	 * 更新修改服务器IP的状态
	 * @param i
	 */
	void updateControllerValue(@Param(value="status")int i);

	
}
