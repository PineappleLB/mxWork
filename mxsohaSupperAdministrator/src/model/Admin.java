package model;

import java.io.Serializable;

/**
 * @author pineapple
 * @date 2017年12月21日 下午2:53:14
 * @description 用户实体类
 */
public class Admin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
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
	 * 用户当前状态
	 */
	private int status;
	


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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", token=" + token + ", ip="
				+ ip + ", status=" + status + "]";
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
