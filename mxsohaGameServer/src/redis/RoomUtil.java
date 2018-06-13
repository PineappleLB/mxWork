package redis;

import model.Seat;
import redis.clients.jedis.JedisPool;
import utils.Configs;
import utils.RedisTemplate;

/**
 * 初始化房间
 * @author pineapple
 * @date 2018-1-8 14:08
 */
public class RoomUtil {
	
	private JedisPool pool = null;
	private int index = 0;
	
	public RoomUtil(){
		pool = RedisUtil.getPool();//初始化连接池和redis对象
	}
	
	/**
	 * 初始化座位信息
	 * @param redis redis操作对象
	 * @param key 数据库list键名称
	 * @param multiplyPower 该房间倍率
	 */
	private synchronized void saveSeats(int multiplyPower,RedisTemplate temp){
		temp.update( (redis) -> {
			Seat s = new Seat();
			int i = Integer.parseInt(Configs.configProps.getProperty("seat.num"))-1;
			for (; i >= 0; i--) {
				s.setId(index--);
				s.setMultiplyPower(multiplyPower);
//				System.out.println(s.toString());
				redis.lpush("room", s.toString());
			}
		});
	}

	/**
	 * 初始化大厅
	 */
	public void initLobby(){
		RedisTemplate temp = new RedisTemplate(pool);
		index = Integer.parseInt(Configs.configProps.getProperty("seat.num"))*Integer.parseInt(Configs.configProps.getProperty("roomNum"))-1;
		//重置房间信息
		temp.update((jedis) -> jedis.del("room"));
		saveSeats(2,temp);
		saveSeats(5,temp);
		saveSeats(20,temp);
	}

}
