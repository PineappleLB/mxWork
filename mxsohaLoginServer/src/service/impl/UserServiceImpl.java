package service.impl;

import java.security.NoSuchAlgorithmException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import mapper.UserMapper;
import model.Promoter;
import model.User;
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
	
	/**
	 * 用户登录
	 */
	@Override
	public User userLogin(String username, String password)
	{
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		User u = null;
		try {
			//调用mapper的方法获取user对象比较密码是否一致
			u = mapper.selectUserByName(username);
			if(u!=null && u.getPassword().equals(StringUtils.encode(password+u.getToken())))
			{
				if(u.getStatus()==0){//正常用户状态为离线
					return u;
				}
				else if(u.getStatus()==3){//用户暂离
					return u;
				}
				else if(u.getStatus()==1){//用户在线状态
					if(u.getLastPlayTime().getTime()+6000000<System.currentTimeMillis()){//判断是否已经十分钟没有操作，如果超过10分钟则可以登陆
						return u;
					}
					logger.error(u.getUsername()+"用户正在其他地方登陆，登录失败！");
					throw new IllegalStateException("用户正在其他地方登陆，登录失败！");
				}
				else{
					logger.error("被封禁的用户："+u.getUsername()+"正在进行登陆操作");
					throw new IllegalStateException("您的账号由于异常操作已被封禁！");
				}
			}else{
				return null;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateuserIp(User u, String ip) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
//			Date d = new Date();
//			if(!(u.getLastPlayTime().getDate()==d.getDate()&&u.getLastPlayTime().getMonth()==d.getMonth()&&u.getLastPlayTime().getYear()==d.getYear())) {//判断上一次游戏时间是否是上一天，如果超过一天则重置留机次数
//				mapper.updateStaySeatTime(3,u.getId());
//			}
			mapper.updateUserIp(u.getId(), ip,u.getStaySeatTime());
		} catch (Exception e) {
			logger.error("修改id为"+u.getId()+"的登录信息失败，异常信息："+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public int userRegister(User u) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		int result = 0;
		try {
			result = mapper.userRegister(u);
			mapper.userInfoRegister(u);
			logger.info(u.getUsername()+"用户注册成功！注册信息为："+u.toString());
		}catch (Exception e) {
			if(e instanceof MySQLIntegrityConstraintViolationException) {
				throw new IllegalArgumentException("注册失败，用户名已经存在！");
			}
			logger.error(u.getUsername()+"用户注册失败，失败信息为："+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int userCount(int beInvited) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		int count = 0;
		try {
			count = mapper.userCount(beInvited);
			return count;
		} catch (Exception e) {
			logger.error("查找邀请码"+beInvited+"的时候出现异常："+e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean verifyName(String name) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.verifyName(name)>0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean adminValidate() {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			int status = mapper.adminValidate();
			return status>0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void updateControllerValue(int i) {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		try {
			m.updateControllerValue(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
				return prom;
			}else{
				return null;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
