package utils;

import redis.clients.jedis.Jedis;

/**
 * 更新数据库的处理接口
 * @author Administrator
 *
 */
public interface RedisUpdate {
	/**
	 * 更新数据库操作
	 * @param jedis
	 */
	public void update(Jedis jedis);
}
