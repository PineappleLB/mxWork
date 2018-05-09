package utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis操作模板
 * 
 * @author Administrator
 *
 */
public class RedisTemplate {

	private JedisPool jedisPool;

	public RedisTemplate(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 进行查询操作
	 * @param callback
	 * @return
	 */
	public <T> T execute(RedisCallback<T> callback) {
		Jedis jedis = jedisPool.getResource();
		try {
			return callback.handle(jedis);
		} catch (Exception e) {
			// throw your exception
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	
	public void update(RedisUpdate update){
		Jedis jedis = jedisPool.getResource();
		try {
			update.update(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 归还对象到连接池
	 * @param jedis
	 */
	@SuppressWarnings("all")
	private void returnResource(Jedis jedis) {
		if (jedis != null && jedisPool!=null) {
			jedisPool.returnResource(jedis);
		}
	}
}