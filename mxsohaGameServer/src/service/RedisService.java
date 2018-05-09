package service;

public interface RedisService {

	/**
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	boolean validateUserToken(int userId, String token);

	/**
	 * 添加奖池分数
	 * @param parseInt
	 * @param seatId 
	 */
	int[] addPoolScores(int parseInt, Integer seatId);

	/**
	 * 根据键获取奖池的分数
	 * @param string
	 * @param seatId 
	 * @return
	 */
	int selPoolScore(String string, Integer seatId);

	/**
	 * 添加玩家中奖通知
	 * @param string
	 */
	void addNotices(String string);

}
