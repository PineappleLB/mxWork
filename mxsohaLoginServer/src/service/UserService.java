package service;

import model.Promoter;
import model.User;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:09:48
 * @description 用户操作接口
 */
public interface UserService {
	
	/**
	 * 判断用户是否登录成功
	 * @param username 用户名
	 * @param password 密码
	 * @return 登录成功/失败
	 */
	User userLogin(String username, String password);

	/**
	 * 修改用户的登录ip地址 
	 * @param id 根据ID检索
	 * @param ip 需要修改的ip地址值
	 */
	void updateuserIp(User u, String ip);

	/**
	 * 注册用户
	 * @param u
	 */
	int userRegister(User u);
	
	/**
	 * 查找对应邀请人的邀请码是否存在
	 * @param beInvited 用户id，邀请码
	 */
	int userCount(int beInvited);

	/**
	 * 验证用户名是否存在
	 * @param name
	 * @return
	 */
	boolean verifyName(String name);

	/**
	 * 验证管理员账号密码是否正确
	 * @param name 姓名
	 * @param pass 密码
	 * @return 返回验证结果
	 */
	boolean adminValidate();

	/**
	 * 更新修改服务器端IP的方法
	 * @param status 修改的状态码
	 */
	void updateControllerValue(int status);

	/**
	 * 查询推广员用户信息
	 * @param name
	 * @param pass
	 * @return
	 */
	Promoter promLogin(String name, String pass);
	
}
