package model;

/**
 * @author pineapple
 * @date 2017年12月22日 上午10:04:23
 * @description 座位
 */
public class Seat{
	
	/**
	 * 座位号
	 */
	private int seatId;

	/**
	 * 座位当前人用户名
	 * 当空位是此值为null
	 */
	private String seatUser;
	
	/**
	 * 座位当前状态
	 * 0 ： 可坐
	 * 1 ： 正在游戏
	 * 2 ： 留机
	 */
	private int seatStatus;

	/**
	 * 当前座位所在房间的的倍率
	 */
	private int multiplyPower;
	
	/**
	 * 构造函数
	 * 设置默认值
	 */
	public Seat(){
		this.seatStatus=0;
		this.seatUser=null;
	}
	
	/**
	 * 
	 * @param i
	 */
	public Seat(int i){
		this();
		this.setId(i);
	}
	
	public String getSeatUser() {
		return seatUser;
	}

	public void setSeatUser(String seatUser) {
		this.seatUser = seatUser;
	}

	public int getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(int seatStatus) {
		this.seatStatus = seatStatus;
	}

	public int getId() {
		return seatId;
	}

	public void setId(int id) {
		this.seatId = id;
	}
	
	@Override
	public String toString() {
		return "{\"id\":" + seatId + ", \"multiplyPower\":"+multiplyPower+", \"seatUser\":\"" + seatUser  + "\", \"seatStatus\":" + seatStatus + "}";
	}

	public int getMultiplyPower() {
		return multiplyPower;
	}

	public void setMultiplyPower(int multiplyPower) {
		this.multiplyPower = multiplyPower;
	}
	
}
