package mapper;

import org.apache.ibatis.annotations.Param;

import model.Promoter;
import model.User;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:04:03
 * @description 用户操作类接口
 */
public interface UserMapper {

	/**
	 * 根据用户名查询user对象
	 * @param name 查询的条件用户名
	 * @return 查询到的user对像
	 */
	User selectUserByName(@Param(value="name")String name);
	
	/**
	 * 更新IP地址并改变用户的状态
	 * @param id 根据ID检索用户
	 * @param ip 需要修改的用户的IP地址
	 */
	int updateUserIp(@Param(value="id")Integer id,@Param(value="ip")String ip,@Param(value="staySeatTime")int staySeatTime);
	
	/**
	 * 用户注册的方法
	 * @param u 封装的用户对象
	 * @return
	 */
	int userRegister(User u);
	
	/**
	 * 
	 * 查找邀请人是否存在
	 * @param beInvited 用户id，邀请码
	 */
	int userCount(int beInvited);

	/**
	 * 用户信息添加
	 * @param u
	 * @return
	 */
	int userInfoRegister(User u);

	/**
	 * 检验用户名是否存在
	 * @param name
	 * @return
	 */
	int verifyName(@Param(value="username")String name);
	

	/**
	 * 更新留机次数
	 * @param time 次数
	 * @param id 用户id
	 */
	void updateStaySeatTime(@Param(value="time")int time,@Param(value="id")int id);

	/**
	 * 验证更改IP地址状态
	 * @param ip
	 * @return
	 */
	int adminValidate();

	/**
	 * 更新修改服务器IP的状态
	 * @param i
	 */
	void updateControllerValue(@Param(value="status")int i);

	/**
	 * 根据推广员用户名查询
	 * @param name
	 * @return
	 */
	Promoter selectPromoterByName(@Param(value="name")String name);
	
	
}
