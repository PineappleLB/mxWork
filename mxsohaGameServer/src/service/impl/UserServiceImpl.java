package service.impl;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import mapper.UserMapper;
import model.SeatsInfo;
import model.User;
import model.UserInfo;
import service.UserService;
import utils.MyBatisUtil;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:10:25
 * @description 用户操作接口实现类
 */
public class UserServiceImpl implements UserService {
	
	private SqlSession session;
	private Logger logger = Logger.getLogger(UserService.class);

	@Override
	public String getUserName(int userId) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			String username = mapper.getUserNameById(userId);
			if(username==null || "".equals(username)){
				return null;
			}
			return username;
	}

	@Override
	public void updateMoney(double d, int id) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			if(id==0){
				throw new IllegalArgumentException("参数异常，id不能为0");
			}
			int result = mapper.updateMoney(d,id);
			if(result > 0){
				logger.info("id为："+id+"的用户进行了金币更新操作，金币增加："+d);
			}else{
				logger.info("id为："+id+"的用户进行金币更新操作失败");
			}
	}


	@Override
	public SeatsInfo getSeatsInfo(int roomId, int seatId, int payScore) throws SQLException {
		try {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			SeatsInfo seat = null;
			seat = mapper.getSeatsInfo(seatId);
			mapper.updateSeatInfo(payScore, 0, seatId);
			return seat;
		} catch(Exception e) {
			logger.debug("获取座位信息错误："+e.getMessage());
			return null;
		}
	}

	@Override
	public UserInfo getUserInfo(int userId) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			UserInfo user = null;
			user = mapper.getUserInfo(userId);
			return user;
	}

	@Override
	public User getUserById(int userId) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			User user = null;
			user = mapper.getUserById(userId);
			return user;
	}

	@Override
	public void updateUserInfo(int score, int gameScore,int id) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateUserInfo(score,gameScore,id);
	}

	@Override
	public void updateSeatInfo(int score, int gameScore, Integer seatId) throws SQLException {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateSeatInfo(score,gameScore, seatId);
	}


	@Override
	public void updateUserAndSeatInfo(int checkId, int id, byte b, int roomId, int seatId,int score,int resetNum) {
			session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateCheckId(checkId,id,score);
			mapper.updateSeatListNum(b,seatId,score,resetNum);
	}

	@Override
	public void updateSeatDoubleScore(int score, int gameScore, int seatId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		mapper.updateSeatDoubleScore(score,gameScore,seatId);
	}

	@Override
	public Map<String, Object> selectSeatRecords(int seatId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			return mapper.selectSeatRecords(seatId);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void addRecords(int userId, Integer seatId, int i, int gameScore, int result, int j, int multiplyPower) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.addRecords(userId, seatId, i, gameScore, result, j, multiplyPower);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getUserMoneyById(int userId) {
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			mapper.selectUserMoneyById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	
//	public static void main(String[] args) {
//		Map<String,Integer> list = new UserServiceImpl().selectSeatRecords(2);
//		
//	}
	
}
