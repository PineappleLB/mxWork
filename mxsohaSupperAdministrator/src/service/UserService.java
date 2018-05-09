package service;

import java.util.List;

import model.Admin;
import model.Promoter;
import net.sf.json.JSONObject;

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
	Admin userLogin(String username, String password);

	/**
	 * 修改用户的登录ip地址 
	 * @param id 根据ID检索
	 * @param ip 需要修改的ip地址值
	 */
	void updateuserIp(String name, String ip);
	
	
	/**
	 * 根据ID为用户充值金额 并且返回充值之后的用户金币，充值失败则返回-1
	 * @param promId
	 * @param userId
	 * @param money
	 * @return
	 */
	double addPromoterBalance(int promId, int userId,int money);

	/**
	 * 退出登录操作
	 */
	void logout(String name);
	
	/**
	 * 查询推广员用户信息
	 * @param name
	 * @param pass
	 * @return
	 */
	Promoter promLogin(String name, String pass);

	/**
	 * 更新用户密码
	 * @param id
	 * @param oldPass
	 * @param newPass
	 * @return
	 */
	int promUpdatePwd(int id, String oldPass, String newPass);

	/**
	 * 
	 * @param promId
	 * @param userid
	 * @param i
	 * @return
	 */
	double reduceUserMoney(int promId, int userId,int money);

	/**
	 * 根据当前推广员id查询最上级推广员信息
	 * @param promId
	 * @return
	 */
	Promoter getSupperParentPromoter(int promId);

	/**
	 * 查询用户金币记录
	 * @param promId
	 * @param month
	 * @return
	 */
	List<JSONObject> selectAdminRecords(int promId, int month, int year);

	/**
	 * 添加下一级推广员
	 * @param prom
	 * @return
	 */
	int addChildPromoter(Promoter prom);

	/**
	 * 根据id查询直属推广员的信息
	 * @param promId
	 * @return
	 */
	List<JSONObject> selectClindPromInfo(int promId);

	/**
	 * 查询推广员直属用户信息
	 * @param promId
	 * @return
	 */
	List<JSONObject> selectUsersInfo(int promId);

	/**
	 * 推广员充值
	 * @param promId 推广员id
	 * @param money 充值的金额
	 * @return
	 */
	int addMoneyByAdmin(int promId, int money);

	/**
	 * 推广员申请减值
	 * @param promId
	 * @param money
	 * @return
	 */
	int reduceMoneyByAdmin(int promId, int money);

	/**
	 * 推广员查询某天的充值记录
	 * @param promId
	 * @param day
	 * @param year 
	 * @param month 
	 * @return
	 */
	List<JSONObject> selectUserAddRecords(int promId, int day, int month, int year);

	/**
	 * 推广员查询某天的兑奖记录
	 * @param promId
	 * @param day
	 * @param year 
	 * @param month 
	 * @return
	 */
	List<JSONObject> selectReduceRecords(int promId, int day, int month, int year);

	/**
	 * 每日下分报告 
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<JSONObject> selectReduceMoneyByDay(int day, int month, int year);

	/**
	 * 每日上分报告查询
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<JSONObject> selectAddMoneyByDay(int day, int month, int year);

	/**
	 * 每月营收详情
	 * @param month
	 * @param year
	 * @return
	 */
	List<JSONObject> selectMoneyByMonth(int month, int year);

	/**
	 * 游戏运营统计
	 * @param month
	 * @param year
	 * @return
	 */
	List<JSONObject> selectGameScoreRecords(int month, int year);

	/**
	 * 系统日志查询
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<JSONObject> selectSystemLogs(int day, int month, int year);

	/**
	 * 记录系统日志
	 * @param string
	 * @param i
	 */
	void addSystemLog(String string, int i);

	/**
	 * 查询推广员名称
	 * @param promId
	 * @return
	 */
	String selectPromNameById(int promId);

	/**
	 * 调节玩家难度
	 * @param hard
	 * @param userName
	 * @return
	 */
	int ctrlUserHard(int hard, String userName);

	/**
	 * 调节座位难度
	 * @param hard
	 * @param seatId
	 * @return
	 */
	int ctrlSeatHard(int hard, int seatId);

	/**
	 * 清除座位以及玩家数据
	 * @return
	 */
	int clearInfoTable();

	/**
	 * 删除某个玩家
	 * @param id
	 * @param name
	 * @return
	 */
	int deleteUserInfo(int id, String name);

	/**
	 * 修改后台密码
	 * @param oldPass
	 * @param newPass
	 * @return
	 */
	int updateAdminPass(String oldPass, String newPass);

}
