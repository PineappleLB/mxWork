package utils;

import redis.clients.jedis.Jedis;

/**
 * 调用操作接口操作数据库返回泛型类型 
 * @author Administrator
 *
 * @param <T>
 */
public interface RedisCallback<T> {
	  public T handle(Jedis jedis);
}
