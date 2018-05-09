package mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import model.Admin;
import model.Promoter;

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
	Admin selectUserByName(@Param(value="name")String name);
	
	/**
	 * 更新IP地址并改变用户的状态
	 * @param id 根据ID检索用户
	 * @param ip 需要修改的用户的IP地址
	 */
	int updateUserIp(@Param(value="name")String name,@Param(value="ip")String ip);
	
	/**
	 * 根据id添加金币
	 * @param id 用户id
	 * @param money 用户需要添加的金币
	 * @return
	 */
	int addUserMoney(@Param(value="id")Integer id,@Param(value="money")Integer money);
	
	/**
	 * 根据用户id查询金币
	 * @param id 用户的id
	 * @return 新的金币数量
	 */
	double selectUserMoneyById(@Param(value="id")Integer id);

	/**
	 * 退出登录
	 */
	int logout(@Param(value="name")String name);

	/**
	 * 将用户封禁
	 * @param username
	 */
	int closureAdmin(@Param(value="name")String name);

	/**
	 * 根据推广员用户名查询
	 * @param name
	 * @return
	 */
	Promoter selectPromoterByName(@Param(value="name")String name);

	/**
	 * 根据id返回推广员对象
	 * @param id
	 * @return
	 */
	Promoter selectPromoterById(@Param(value="id")int id);

	/**
	 * 更新推广员密码
	 * @param id
	 * @param newPass
	 */
	int updatePromoterPass(@Param(value="id")int id, @Param(value="pass")String newPass);

	/**
	 * 减少推广员的余额
	 * @param id
	 * @param balance
	 * @return
	 */
	int addPromoterBalance(@Param(value="id")int id, @Param(value="balance")int balance);

	/**
	 * 添加操作记录
	 * @param promId
	 * @param does
	 * @param change
	 */
	void addPromoterRecords(@Param(value="id")int promId, @Param(value="add")int add, @Param(value="reduce")int reduce,@Param(value="by")int by);

	/**
	 * 查询金币操作记录集
	 * @param promId
	 * @param month
	 * @return
	 */
	List<Map<String, Object>> selectAdminRecords(@Param(value="id")int promId, @Param(value="month")int month,@Param(value="year")int year);

	/**
	 * 根据子推广员查询上一级推广员
	 * @param promId
	 * @return
	 */
	Promoter getSupperParentPromoter(@Param(value="id")int promId);

	/**
	 * 添加子级推广员
	 * @param prom
	 * @return
	 */
	int addChildPromoter(Promoter prom);

	/**
	 * 查询直属推广员信息
	 * @param promId
	 * @return
	 */
	List<Map<String, Object>> selectClindPromInfo(@Param(value="id")int promId);

	/**
	 * 查询推广员下面的直属会员信息
	 * @param promId
	 * @return
	 */
	List<Map<String, Object>> selectUsersInfo(@Param(value="id")int promId);

	/**
	 * 推广员充值金币
	 * @param promId
	 * @param money
	 * @return
	 */
	int addMoneyByAdmin(@Param(value="id")int promId, @Param(value="money")int money);

	/**
	 * 根据推广员id查询余额
	 * @param promId
	 * @return
	 */
	int selectPromoterBalanceById(@Param(value="id")int promId);

	/**
	 * 推广员查询金币充值记录
	 * @param promId
	 * @param day
	 * @param year 
	 * @param month 
	 * @return
	 */
	List<Map<String, Object>> selectUserAddRecords(@Param(value="id")int promId, @Param(value="day")int day, @Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 推广员查询金币兑奖记录
	 * @param promId
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectReduceRecords(@Param(value="id")int promId, @Param(value="day")int day, @Param(value="month")int month, @Param(value="year")int year);
	
	
	/**
	 * 每日下分报告 
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectReduceMoneyByDay(@Param(value="day")int day, @Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 每日上分报告查询
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectAddMoneyByDay(@Param(value="day")int day, @Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 每月营收详情
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectMoneyByMonth(@Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 游戏运营统计
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectGameScoreRecords(@Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 系统日志查询
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> selectSystemLogs(@Param(value="day")int day, @Param(value="month")int month, @Param(value="year")int year);

	/**
	 * 更新推广员登录时间
	 * @param id
	 */
	void updatePromoterLogTime(@Param(value="id")int id);

	/**
	 * 记录系统日志
	 * @param str
	 * @param id
	 */
	void addSystemLog(@Param(value="log")String str, @Param(value="id")int id);

	/**
	 * 根据id查询推广员名称
	 * @param promId
	 * @return
	 */
	String selectPromNameById(@Param(value="promId")int promId);

	/**
	 * 调节玩家难度
	 * @param hard
	 * @param userId
	 * @return
	 */
	int ctrlUserHard(@Param(value="hard")int hard, @Param(value="id")int userId);

	/**
	 * 调节座位难度
	 * @param hard
	 * @param seatId
	 * @return
	 */
	int ctrlSeatIdHard(@Param(value="hard")int hard, @Param(value="id")int seatId);

	/**
	 * 根据用户名查找id
	 * @param userName
	 * @return
	 */
	int selectUserIdByName(@Param(value="name")String userName);

	/**
	 * 删除所有userInfo表数据
	 * @return
	 */
	int clearUserInfo();
	
	/**
	 * 删除所有玩家
	 * @return
	 */
	int clearUsers();

	/**
	 * 重置seatsInfo表数据
	 * @return
	 */
	int clearSeatsInfo();

	int deleteUserInfoTable(int id);

	int deleteUserTable(int id);

	int deleteUserBeInvited(int id);

	/**
	 * 查询管理员信息
	 * @return
	 */
	Admin selectAdminPass();

	int updateAdminInfo(@Param(value="admin")Admin admin);
}
