package service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import redis.RedisUtil;
import redis.clients.jedis.Jedis;
import service.RedisService;
import utils.RedisCallback;
import utils.RedisTemplate;
import utils.StringUtils;

public class RedisServiceImpl implements RedisService {

	private RedisTemplate temp = new RedisTemplate(RedisUtil.getPool());
	
	@Override
	public int saveControllerValue(int val) {
		return temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis jedis) {
				String query = jedis.set("control", val+"");
				if(query.equalsIgnoreCase("ok")) {
					return 1;
				}
				return null;
			}
		});
	}

	@Override
	public void addPromtersOrderRecords(int promId, int money, String promName) {
		JSONObject obj = new JSONObject();
		obj.put("messageId", StringUtils.getRandomUUID().substring(0, 6));
		obj.put("id", promId);
		obj.put("userName", promName);
		obj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		obj.put("money", money);
		temp.update((jedis)->{
			jedis.lpush("promtersOrderRecords", obj.toString());
		});
	}

	@Override
	public JSONObject excutePromoterOrder(String messageId, int sure) {
//		List<String> list = 
		return temp.execute(new RedisCallback<JSONObject>() {
			@Override
			public JSONObject handle(Jedis jedis) {
				List<String> list = jedis.lrange("promtersOrderRecords", 0, -1);
				jedis.del("promtersOrderRecords");
				JSONObject obj = null;
				for (int i = 0;i < list.size();i++) {
					obj = JSONObject.fromObject(list.get(i));
					if(messageId.equals(obj.getString("messageId"))) {
						if(sure==1) {
							list.remove(i);//移除匹配的那一位
//							String[] strs = new String[list.size()];
//							list.toArray(strs);
//							jedis.lpush("promtersOrderRecords", strs);
							for (String string : list) {
								jedis.lpush("promtersOrderRecords", string);
							}
							
							return obj;
						}else {
							return null;//管理员拒绝
						}
					}
				}
				for (String string : list) {
					jedis.lpush("promtersOrderRecords", string);
				}
				return null;
			}
			
		});
	}		

	@Override
	public List<String> selectPromoterOrder() {
		return temp.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> handle(Jedis jedis) {
				return jedis.lrange("promtersOrderRecords", 0, -1);
			}
		});
	}

	@Override
	public int selectInfoSize() {
		return temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis jedis) {
				return jedis.llen("promtersOrderRecords").intValue();
			}
		}); 
	}
	
}
