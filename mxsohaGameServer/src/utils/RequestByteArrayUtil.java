package utils;

import java.sql.SQLException;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;

import mapper.UserMapper;
import model.SeatsInfo;
import model.UserInfo;
import redis.RedisSession;
import server.GameServerHandler;

public class RequestByteArrayUtil {
	
	private ByteArrayTemplate byteTemp;
	
	private SeatsInfo seat;
	
	private UserInfo userinfo;
	
	private Random random = new Random();
	
	public RequestByteArrayUtil() {
		byteTemp = new ByteArrayTemplate();
	}

	
	/**
	 * 获取请求消息的byte数组
	 * 在此方法中更新作为信息表中的payscore
	 * @param userId
	 * @param roomNum
	 * @return
	 * @throws SQLException 
	 */
	public byte[] createRequestMessage(int userId, int roomId,int seatId,int score,GameServerHandler handler) throws SQLException {
		SqlSession session = MyBatisUtil.getSession();//加载seat对象和userInfo对象
		UserMapper mapper = session.getMapper(UserMapper.class);
		seat = mapper.getSeatsInfo(seatId);
		if(seat.getGameTime()%5==0) {
			handler.setAddScoreLogic(true);
		}
		mapper.updateSeatInfo(score, 0, seatId);
		userinfo = mapper.getUserInfo(userId);
		
		byteTemp.setUserCheckId(userinfo.getCheckId());
		byteTemp.setDoubleScore(seat.getDoublePayScore()-seat.getDoubleGotScore());
		
		byteTemp.setUserAllScore(userinfo.getPayScore());
		byteTemp.setUserGotScore(userinfo.getGotScore());
		byteTemp.setUserScore(score);
		byteTemp.setSeatAllScore(seat.getPayScore());
		byteTemp.setSeatGotScore(seat.getGotScore());
		byteTemp.setUserHard((byte)userinfo.getHard());
		byteTemp.setSeatHard((byte)seat.getHard());
		byteTemp.setUserPlayTime(userinfo.getGameTime());
		byteTemp.setSeatPlayTime((byte)seat.getGameTime());
		byteTemp.setSeatListNum((byte)seat.getSeatListNum());//下标31与下标34交换
		byteTemp.setRandom(random.nextInt());
		byteTemp.setResetNum((byte)seat.getResetNum());
		byteTemp.setControlNum((byte)RedisSession.getControlNum());
		
		return byteTemp.getArray();
	}

}
