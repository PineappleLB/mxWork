package model;

import java.sql.Timestamp;

/**
 *  推广员对象实体类
 * @author Administrator
 *
 */
public class Promoter {
	
	/**
	 * 推广员id
	 */
	private int id;
	
	/**
	 * 推广员姓名
	 */
	private String name;
	
	/**
	 * 推广员密码
	 */
	private String password;
	
	/**
	 * 推广员密码随机盐
	 */
	private String token;
	
	/**
	 * 推广员上一级id
	 */
	private int parentId;
	
	/**
	 * 推广员账户余额
	 */
	private int balance;
	
	/**
	 * 推广员上一次登录时间	
	 */
	private Timestamp logTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Timestamp getLogTime() {
		return logTime;
	}

	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}
	
	
	
}
