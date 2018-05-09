package service;

public interface RedisService {

	/**
	 * 保存用户验证字符
	 * @param id
	 * @param token
	 */
	void saveToken(String id, String token);

}
