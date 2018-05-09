package service.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import mapper.UserMapper;
import model.User;
import service.UserService;
import utils.MyBatisUtil;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:10:25
 * @description 用户操作接口实现类
 */
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class); 
	
	//sql回话对象
	private SqlSession session;
	
	@Override
	public void UserLogout(int id, int status) {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		try {
			m.userLoginout(id, status);
			logger.info("id为"+id+"的用户设置登录状态为离线");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("id为："+id+"的用户断开连接异常："+e.getMessage());
		}
	}

	@Override
	public String getUserName(int id) {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		String name = null;
		try {
			User u = m.selectUserById(id);
//			if(u.getStatus()!=3) {
//				throw new IllegalArgumentException("登录状态异常！");
//			}
			m.updateUserStatus(1,id);
			name = u.getUsername();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public double getMoneyById(int userId) {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		double d = 0;
		try {
			d = m.getMoneyById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	@Override
	public void updateTimeOutUser() {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		try {
			m.updateTimeOutUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateOutBoundsGameTime() {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		try {
			m.updateOutBoundsUserInfoGameTime();
			m.updateOutBoundsSeatsInfoGameTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int selectStaySeatTimeById(int userId) {
		session = MyBatisUtil.getSession();
		UserMapper m = session.getMapper(UserMapper.class);
		try {
			User u = m.selectUserById(userId);
			if(u==null) {
				throw new IllegalArgumentException("参数错误，id不正确，不能找到用户id："+userId);
			}
			if(u.getStaySeatTime()>0) {
				m.updateStaySeatTime(u.getStaySeatTime()-1,userId);
				return u.getStaySeatTime()-1;
			}else {
				throw new IllegalArgumentException("您今日留机次数已用完！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
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

	
}
