package service.impl;

import net.sf.json.JSONObject;
import redis.RedisUtil;
import redis.clients.jedis.Jedis;
import service.RedisService;
import utils.Configs;
import utils.RedisCallback;
import utils.RedisTemplate;

public class RedisServiceImpl implements RedisService {
	
	//redis操作对象
	private RedisTemplate temp = new RedisTemplate(RedisUtil.getPool());

	@Override
	public boolean validateUserToken(int userId, String token) {
		String vToken = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				return jedis.get(userId+"");
			}
		});
		return token.equals(vToken);
	}

	@Override
	public int [] addPoolScores(int score,Integer seatId) {
		String str = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				return jedis.lindex("poolScore",seatId);
			}
		});
		JSONObject obj = JSONObject.fromObject(str);
		int K4 = obj.getInt("4K");
		int SF = obj.getInt("SF");
		int RS = obj.getInt("RS");
		int K5 = obj.getInt("5K");
		K4 = K4 >= Configs.max_4K ? Configs.max_4K : K4+1;
		SF = SF >= Configs.max_SF ? Configs.max_SF : SF+1;
		RS = RS >= Configs.max_RS ? Configs.max_RS : RS+1;
		K5 = K5 >= Configs.max_5K ? Configs.max_5K : K5+1;
		obj.put("4K", K4);
		obj.put("SF", SF);
		obj.put("RS", RS);
		obj.put("5K", K5);
		temp.update((jedis)->{
			jedis.lset("poolScore", seatId, obj.toString());
		});
		return new int[] {K4, SF, RS, K5};
	}

	@Override
	public int selPoolScore(String string, Integer seatId) {
		String str = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				return jedis.lindex("poolScore", seatId);
			}
		});
		JSONObject obj = JSONObject.fromObject(str);
		int result = obj.getInt(string);
		int min_score = 0;
		switch(string) {
		case "4K":
			min_score = Configs.min_4K;
			break;
		case "5K":
			min_score = Configs.min_5K;
			break;
		case "RS":
			min_score = Configs.min_RS;
			break;
		case "SF":
			min_score = Configs.min_SF;
			break;
		}
		obj.put(string, min_score);
		temp.update((jedis)->{
			jedis.lset("poolScore", seatId, obj.toString());
		});
		
		return result;
	}

	@Override
	public void addNotices(String str) {
		temp.update((jedis)->{
			jedis.lpush("notices", str);
		});
		
	}

	
}
