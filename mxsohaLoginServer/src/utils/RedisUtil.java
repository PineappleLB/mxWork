package utils;

import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utils.Configs;

/**
 * Redis操作接口
 * 
 * @author pineapple
 * @date 2018-1-8 上午11:54:14
 */
public class RedisUtil {
	private static JedisPool pool = null;
	/**
	 * 构建redis连接池
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool() {
		if (pool == null) {
			Properties props = Configs.configProps;
			
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(Integer.parseInt(props.getProperty("maxTotal")));
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(Integer.parseInt(props.getProperty("maxIdle")));
			// 控制一个pool最少有多少个状态为idle(空闲的)的jedis实例。
			config.setMinIdle(Integer.parseInt(props.getProperty("minIdle")));
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(Integer.parseInt(props.getProperty("MaxWaitMillis")));
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			
			pool = new JedisPool(config, props.getProperty("host"), Integer.parseInt(props.getProperty("port")));
		}
		return pool;
	}

}
