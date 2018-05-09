package model;

public class SeatsInfo {
	
	/**
	 * 座位id
	 */
	private int seatId;
	
	/**
	 * 房间号
	 */
	private int roomId;
	
	/**
	 * 座位游戏局数
	 */
	private int gameTime;
	
	/**
	 * 座位游戏总花费的分数
	 */
	private int payScore;
	
	/**
	 * 座位游戏总赢得的分数
	 */
	private int gotScore;
	
	/**
	 * 座位集合的值，用于串口算法计算需要
	 */
	private int seatListNum;
	
	/**
	 * 当前座位花在比倍上的积分
	 */
	private int doublePayScore;
	
	/**
	 * 当前座位再比倍上得过的分
	 */
	private int doubleGotScore;
	
	/**
	 * 座位当前难度
	 */
	private int hard;
	
	/**
	 * 串口保存重置位
	 */
	private int resetNum;


	public int getResetNum() {
		return resetNum;
	}

	public void setResetNum(int resetNum) {
		this.resetNum = resetNum;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
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

	public int getHard() {
		return hard;
	}

	public void setHard(int hard) {
		this.hard = hard;
	}

	public int getSeatListNum() {
		return seatListNum;
	}

	public void setSeatListNum(int seatListNum) {
		this.seatListNum = seatListNum;
	}
	
	public int getDoublePayScore() {
		return doublePayScore;
	}

	public void setDoublePayScore(int doublePayScore) {
		this.doublePayScore = doublePayScore;
	}

	public int getDoubleGotScore() {
		return doubleGotScore;
	}

	public void setDoubleGotScore(int doubleGotScore) {
		this.doubleGotScore = doubleGotScore;
	}

	
}
