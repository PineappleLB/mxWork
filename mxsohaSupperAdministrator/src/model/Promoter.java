package model;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import utils.StringUtils;

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
	private String parentId;
	
	/**
	 * 推广员账户余额
	 */
	private int balance;
	
	/**
	 * 推广员邀请码
	 */
	private String invitedCode;
	
	/**
	 * 推广员上一次登录时间	
	 */
	private Timestamp logTime;

	public Promoter(String name, String password, String promId) {
		try {
			this.name = name;
			this.token = StringUtils.getRandomUUID();
			this.password = StringUtils.encode(password+token);
			this.parentId = promId;
			this.invitedCode = this.token.substring(0, 6).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public Promoter() {}

	public Promoter(String name, String pass) {
		try {
			this.name = name;
			this.token = StringUtils.getRandomUUID();
			this.password = StringUtils.encode(pass+token);
			this.invitedCode = this.token.substring(0, 6).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
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

	public String getInvitedCode() {
		return invitedCode;
	}

	public void setInvitedCode(String invitedCode) {
		this.invitedCode = invitedCode;
	}

	@Override
	public String toString() {
		return "Promoter [id=" + id + ", name=" + name + ", password=" + password + ", token=" + token + ", parentId="
				+ parentId + ", balance=" + balance + ", invitedCode=" + invitedCode + ", logTime=" + logTime + "]";
	}

}
