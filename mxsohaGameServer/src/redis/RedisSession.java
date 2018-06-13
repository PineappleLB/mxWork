package redis;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.RedisCallback;
import utils.RedisTemplate;

public class RedisSession {
	
	private static JedisPool pool;
	
	static{
		pool = RedisUtil.getPool();
	}

	/**
	 * redis中查询座位信息数据
	 * @param username
	 * @param roomId
	 * @return
	 */
	public static synchronized Integer getSeatsInfo(String username, Integer seatId) {
		RedisTemplate temp = new RedisTemplate(pool);
		//匿名内部类实现查询接口
		Integer result = temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis jedis) {
				String seat = jedis.lindex("room", seatId);
				//判断是否是redis room key被删除了
				if(seat == null && seatId < 90) {
					//如果确实不存在该key
					if(!jedis.exists("room")) {
						new RoomUtil().initLobby();//重新初始化大厅
						return getSeatsInfo(username, seatId);
					}
				}
				JSONObject obj = JSONObject.fromObject(seat);
				if(obj.getString("seatUser").equals(username)&&obj.getInt("id")==seatId) {
					return seatId;
				}
				return null;
			}
		});
		
		return result;
	}
	
	
	/**
	 * 获取控制位消息
	 * @return
	 */
	public static synchronized int getControlNum(){
		RedisTemplate temp = new RedisTemplate(pool);
		int result = temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis jedis) {
				String str = jedis.get("control");
				if(str == null)
					return 0;
				else{
					int result = Integer.parseInt(str);
					if(result > 0){
						jedis.set("control", "0");
					}
					return result;
				}
			}
		});
		return result;
	}
	
	
}
