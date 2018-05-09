package service.impl;

import service.RedisService;
import utils.RedisTemplate;
import utils.RedisUtil;

public class RedisServiceImpl implements RedisService {
	
	//redis操作对象
	private RedisTemplate temp = new RedisTemplate(RedisUtil.getPool());

	@Override
	public void saveToken(String id, String token) {
		temp.update((redis)->{
			redis.set(id, token);
		});
	}

}
