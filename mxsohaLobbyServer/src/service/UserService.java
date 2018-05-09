package service;
/**
 * @author pineapple
 * @date 2017年12月21日 下午3:09:48
 * @description 用户操作接口
 */
public interface UserService {
	
	/**
	 * 用户退出登录
	 * @param id 根据用户id检索
	 */
	void UserLogout(int id,int status);

	/**
	 * 根据ID查找用户名
	 * @param userId
	 * @return
	 */
	String getUserName(int userId);

	/**
	 * 根据ID查询当前用户余额
	 * @param userId
	 * @return
	 */
	double getMoneyById(int userId);

	/**
	 * 检查数据库用户信息，将暂离用户设置状态为离线
	 */
	void updateTimeOutUser();

	/**
	 * 检查表中是否有超过局数上限的玩家或者座位
	 */
	void updateOutBoundsGameTime();

	/**
	 * 根据用户Id查找用户剩余留机次数
	 * @param userId
	 * @return
	 */
	int selectStaySeatTimeById(int userId);

	/**
	 * 更新修改服务器端IP的方法
	 * @param i
	 */
	void updateControllerValue(int i);
	
}
