package service;

import java.util.List;

import model.Seat;

public interface SeatService {

	/**
	 * 更新作为信息设置为留机状态
	 * @param name 用户名
	 * @param seatId 座位号
	 * @return
	 */
	int updateSeatsInfoForStaySeat(String name,int seatId);
	
	/**
	 * 更新数据库的座位信息
	 * @param seat 座位信息
	 * @param seatId 座位id
	 * @param isLeave 是否是离开座位的请求
	 */
	void updateSeat(Seat seat, int seatId, boolean isLeave);
	
	/**
	 * 根据seatID返回seat的Bean实例
	 * @param seatId
	 * @return
	 */
	Seat getSeatBean(int seatId);
	
	/**
	 * 获取房间数据，取有人坐的座位数据
	 * @param roomName 获取list对象的key
	 * @return 返回筛选过后的有人坐的位置
	 */
	List<String> getRoomSeatDownSeatsByKey(final int room);

	/**
	 * 将留机之后的座位格式化
	 * @param s
	 * @param seatId
	 */
	int formatSeatByStaySeats(Seat s, int seatId);

	/**
	 * 验证用户登录token
	 * @param userId
	 * @param token
	 * @return
	 */
	boolean validateUserToken(int userId, String token);
	
	/**
	 * 查询奖池分数
	 * @param i 
	 * @param parseInt
	 */
	int[] selPoolScores(int i);
}
