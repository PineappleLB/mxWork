package mxsohaGameServer;

import org.junit.Test;

import net.sf.json.JSONObject;
import redis.RoomUtil;
import redis.clients.jedis.Jedis;

public class RedisTest {

	@Test
	public void redisTest() {
		Jedis redis = null;
		try {
			redis = new Jedis();
			String seat = redis.lindex("room", 1);
			JSONObject obj = JSONObject.fromObject(seat);
			System.out.println(obj);
			if(seat == null) {
				new RoomUtil().initLobby();//重新初始化大厅
			}
			seat = redis.lindex("room", 1);
			obj = JSONObject.fromObject(seat);
			System.out.println(obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			redis.close();
		}
	}
	
}
