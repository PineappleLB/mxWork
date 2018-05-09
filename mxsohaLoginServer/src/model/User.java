package model;

import java.sql.Timestamp;

/**
 * @author pineapple
 * @date 2017年12月21日 下午2:53:14
 * @description 用户实体类
 */
public class User{


	/**
	 * 用户id
	 */
	private int id;
	
	/**
	 * 用户最后一次操作时间
	 */
	private Timestamp lastPlayTime;
	
	/**
	 * 用户名称
	 */
	private String username;
	
	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 密码随机验证字符
	 */
	private String token;
	
	/**
	 * 上一次登录ip地址
	 */
	private String ip;
	
	/**
	 * 用户手机号
	 */
	private String phone;
	
	/**
	 * 用户当前状态
	 */
	private int status;
	
	/**
	 * 用户余额
	 */
	private double money;
	
	/**
	 * 当前用户邀请码
	 */
	private int invitationCode;
	
	/**
	 * 被邀请人的邀请码
	 */
	private int beInvited;
	
	/**
	 * 留机次数
	 */
	private int staySeatTime;

	public int getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(int invitationCode) {
		this.invitationCode = invitationCode;
	}

	public int getBeInvited() {
		return beInvited;
	}

	public void setBeInvited(int beInvited) {
		this.beInvited = beInvited;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public Timestamp getLastPlayTime() {
		return lastPlayTime;
	}

	public void setLastPlayTime(Timestamp lastPlayTime) {
		this.lastPlayTime = lastPlayTime;
	}

	public int getStaySeatTime() {
		return staySeatTime;
	}

	public void setStaySeatTime(int staySeatTime) {
		this.staySeatTime = staySeatTime;
	}
	
	
}
