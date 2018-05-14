package service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import mapper.UserMapper;
import model.Admin;
import model.Promoter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.UserService;
import utils.MyBatisUtil;
import utils.StringUtils;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:10:25
 * @description 用户操作接口实现类
 */
public class UserServiceImpl implements UserService {
	
	//session数据库会话对象
	private SqlSession session;
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);  
	
	//用户登录
	@Override
	public Admin userLogin(String username, String password) 
	{
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		Admin u = null;
		try {
			//调用mapper的方法获取user对象比较密码是否一致
			u = mapper.selectUserByName(username);
			
			if(u.getStatus()==1){
				mapper.closureAdmin(username);
				throw new Exception("管理员已登录，不可尝试再次登录");
			}
			
			if(u!=null && u.getPassword().equals(StringUtils.encode(password+u.getToken())))
			{
				logger.info(username+"请求登录成功");
				return u;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error(username+"请求登录异常，异常信息："+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	//更新用户IP
	@Override
	public void updateuserIp(String name, String ip) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		
		try {
			int result = mapper.updateUserIp(name, ip);
			if(result > 0){
				logger.info("修改用户名为"+name+"的用户状态成功！当前登录ip为："+ip);
			}
		} catch (Exception e) {
			logger.error("修改id为"+name+"的登录信息失败，异常信息："+e.getMessage());
			e.printStackTrace();
		}
		
	}

	//管理员为推广员充值
	@Override
	public double addPromoterBalance(int promId, int userId, int money) {
		session = MyBatisUtil.getTransactionSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.addPromoterBalance(promId, money * -1);
			if(result>0){
				result = mapper.addUserMoney(userId, money);
				if(result > 0) {
					double userMoney = mapper.selectUserMoneyById(userId);
					mapper.addPromoterRecords(promId, money, 0, userId);//添加记录
					session.commit();
					return userMoney;
				}else {
					throw new Exception("添加用户金币失败");
				}
			}else{
				throw new Exception("充值失败，用户id错误或用户已被封禁");
			}
		} catch (Exception e) {
			session.rollback();
			logger.error("推广员"+promId+"尝试给id为"+userId+"的用户增加"+money+"个金币失败，错误信息为："+e.getMessage());
			return -1;
		}
	}

	//退出登录
	@Override
	public void logout(String name) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.logout(name);
			if(result > 0){
				logger.debug("管理员退出登录");
			}
		} catch (Exception e) {
			logger.debug("管理员退出登录操作失败"+e.getMessage());
		}
	}

	//推广员登录
	@Override
	public Promoter promLogin(String name, String pass) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		Promoter prom = null;
		try {
			//调用mapper的方法获取user对象比较密码是否一致
			prom = mapper.selectPromoterByName(name);
			if(prom!=null && prom.getPassword().equals(StringUtils.encode(pass+prom.getToken())))
			{
				prom.setPassword(null);
				prom.setToken(null);
				mapper.updatePromoterLogTime(prom.getId());
				return prom;
			}else{
				return null;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	//推广员更新密码
	@Override
	public int promUpdatePwd(int id, String oldPass, String newPass) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		Promoter prom = null;
		try {
			//调用mapper的方法获取user对象比较密码是否一致
			prom = mapper.selectPromoterById(id);
			if(prom!=null && prom.getPassword().equals(StringUtils.encode(oldPass+prom.getToken())))
			{
				newPass = StringUtils.encode(newPass+prom.getToken());
				int result = mapper.updatePromoterPass(id,newPass);
				return result;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//推广员为用户减值
	@Override
	public double reduceUserMoney(int promId, int userId, int money) {
		session = MyBatisUtil.getTransactionSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.addUserMoney(userId, money * -1);
			if(result>0){
				double userMoney = mapper.selectUserMoneyById(userId);
				if(userMoney<0) {
					throw new IllegalArgumentException("金币不足，无法减值");
				}
				mapper.addPromoterBalance(promId, money);
				mapper.addPromoterRecords(promId, 0, money, userId);//添加记录
				session.commit();
				return userMoney;
			}else{
				throw new Exception("充值失败，用户id错误或用户已被封禁");
			}
		} catch (Exception e) {
			session.rollback();
			logger.error("推广员"+promId+"尝试给id为"+userId+"的用户增加"+money+"个金币失败，错误信息为："+e.getMessage());
			return -1;
		}
	}

	//查询当前推广员的祖级推广员
	@Override
	public Promoter getSupperParentPromoter(String promId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		Promoter prom = null;
		try {
			prom = mapper.getSupperParentPromoter(promId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prom;
	}

	//管理员查询充值减值记录
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectAdminRecords(int promId, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		List<Map<String,Object>> list = mapper.selectAdminRecords(promId, month, year);
		JSONArray arrList = new JSONArray();
		for (Map<String,Object> obj : list) {
			arrList.add(obj);
		}
		return arrList;
	}

	//推广员添加下一级推广员
	@Override
	public int addChildPromoter(Promoter prom) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.addChildPromoter(prom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	//推广员查询直属下级推广员
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectClindPromInfo(int promId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectClindPromInfo(promId);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//推广员查询直属会员
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectUsersInfo(int promId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectUsersInfo(promId);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//推广员申请管理员上分
	@Override
	public int addMoneyByAdmin(int promId, int money) {
		session = MyBatisUtil.getTransactionSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.addMoneyByAdmin(promId, money);
			if(result > 0) {
				money =  mapper.selectPromoterBalanceById(promId);
				mapper.addPromoterRecords(0, money, 0, promId);
				session.commit();
				return money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return -1;
	}

	//推广员申请管理员下分
	@Override
	public int reduceMoneyByAdmin(int promId, int money) {
		session = MyBatisUtil.getTransactionSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.addMoneyByAdmin(promId, money * -1);
			if(result > 0) {
				money =  mapper.selectPromoterBalanceById(promId);
				if(money > 0) {
					mapper.addPromoterRecords(0, 0, money, promId);
					session.commit();
					return money;	
				}else {
					throw new IllegalArgumentException("金币不足无法减值");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return -1;
	}

	//推广员上分查询
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectUserAddRecords(int promId, int day, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectUserAddRecords(promId, day, month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//推广员下分查询
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectReduceRecords(int promId, int day, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectReduceRecords(promId, day, month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//管理员每日上分报告 
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectReduceMoneyByDay(int day, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectReduceMoneyByDay(day, month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//管理员每日下分报告 
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectAddMoneyByDay(int day, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectAddMoneyByDay(day, month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//管理员查询每月详情 
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectMoneyByMonth(int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectMoneyByMonth(month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//管理员查询游戏运营统计
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectGameScoreRecords(int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectGameScoreRecords(month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//管理员系统日志查询
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> selectSystemLogs(int day, int month, int year) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			List<Map<String,Object>> list = mapper.selectSystemLogs(day, month, year);
			JSONArray arr = new JSONArray();
			for (Map<String, Object> map : list) {
				arr.add(map);
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addSystemLog(String str, int id) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.addSystemLog(str, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String selectPromNameById(int promId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.selectPromNameById(promId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int ctrlUserHard(int hard, String userName) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int userId = mapper.selectUserIdByName(userName);
			return mapper.ctrlUserHard(hard, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int ctrlSeatHard(int hard, int seatId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.ctrlSeatIdHard(hard, seatId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int clearInfoTable() {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.clearUserInfo() & mapper.clearSeatsInfo() & mapper.clearUsers();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteUserInfo(int id, String name) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			if(id > 0) {
				mapper.deleteUserBeInvited(id);
				return mapper.deleteUserInfoTable(id) & mapper.deleteUserTable(id);
			} else if(name != null && !name.equals("")){
				id = mapper.selectUserIdByName(name);
				mapper.deleteUserBeInvited(id);
				return mapper.deleteUserInfoTable(id) & mapper.deleteUserTable(id);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateAdminPass(String oldPass, String newPass) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			Admin admin = mapper.selectAdminPass();
			if(StringUtils.encode(oldPass + admin.getToken()).equals(admin.getPassword())) {
				admin.setPassword(StringUtils.encode(newPass + admin.getToken()));
				return mapper.updateAdminInfo(admin);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean checkPromoterName(String name) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int count = mapper.checkPromoterName(name);
			return count > 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String addTopPromoter(Promoter p) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int result = mapper.addChildPromoter(p);
			if(result > 0) {
				return mapper.selectPromoterByName(p.getName()).getInvitedCode();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Promoter> selectAllTopPromoters() {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.selectAllTopPromoters();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int deletePromoterByName(String name) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.deletePromoterByName(name);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteAllUser() {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.deleteAllRecords();
			return mapper.deleteAllUserInfo() & mapper.deleteAllUsers();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteAllPromoter() {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.deleteAllPromotersRecords();
			return mapper.deleteAllPromoters();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}



}
