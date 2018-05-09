package model;

public class UserInfo {
	
	/**
	 * 用户id
	 */
	private int userId;
	
	/**
	 * 用户总花费的分数
	 */
	private int payScore;
	
	/**
	 * 用户总获得的分数
	 */
	private int gotScore;
	
	/**
	 * 用户游戏局数
	 */
	private int gameTime;
	
	/**
	 * 用户的校验id
	 */
	private int checkId;
	
	/**
	 * 用户当前难度
	 */
	private int hard;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPayScore() {
		return payScore;
	}

	public void setPayScore(int payScore) {
		this.payScore = payScore;
	}

	public int getGotScore() {
		return gotScore;
	}

	public void setGotScore(int gotScore) {
		this.gotScore = gotScore;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	public int getCheckId() {
		return checkId;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public int getHard() {
		return hard;
	}

	public void setHard(int hard) {
		this.hard = hard;
	}
	
	
}
