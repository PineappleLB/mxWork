package service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Seat;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redisUtil.RedisUtil;
import service.SeatService;
import utils.Configs;
import utils.RedisCallback;
import utils.RedisTemplate;

public class SeatServiceImpl implements SeatService {
	
	private RedisTemplate temp = new RedisTemplate(RedisUtil.getPool());
	private JSONObject obj;

	@Override
	public int updateSeatsInfoForStaySeat(String name, int seatId) {
		int result = temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis jedis) {
				String seatStr = jedis.lindex("room", seatId);
				obj = JSONObject.fromObject(seatStr);
				if(obj.getInt("seatStatus")==1&&obj.getString("seatUser").equals(name)) {
					obj.put("seatStatus", 2);
					obj.put("date", new Date(System.currentTimeMillis()+Integer.parseInt(Configs.configProps.getProperty("staySeatTime"))).getTime());
					jedis.lset("room", seatId, obj.toString());
					return 1;
				}else {
					throw new IllegalArgumentException("座位信息错误，无法正确留机！");
				}
			}
		});
		return result;//1854159
		//1348284
	}

	@Override
	public void updateSeat(Seat seat, int seatId, boolean isLeave) {
		if(!isLeave){//如果是更新座位的请求则查询一遍是否还有其他座位
			if(seat.getSeatUser()!=null)
				checkSeatsByUserName(seat.getSeatUser(),seatId,temp);
		}
		temp.update((redis) -> 
			redis.lset("room", seatId, seat.toString())//更新座位
		);
	}

	
	/**
	 * 依赖checkAllSeats 检查座位信息
	 * @param list
	 * @param seatUser
	 */
	private void checkSeatsByUserName(String seatUser, int seatId, RedisTemplate temp2) {
		//更新数据
		temp.update( (jedis) -> {
			List<String> list = jedis.lrange("room", 0, -1);
			JSONObject obj = null;
			for (String string : list) {
				obj = JSONObject.fromObject(string);
				if(seatUser.equals(obj.getString("seatUser"))){//判断数据库中有没有当前用户正在坐，如果有则将之前的清除 排除留机可能是需要进入留机的座位
					if(obj.getInt("seatStatus")==2 && obj.getInt("id")!=seatId) {
						throw new IllegalArgumentException("您在其他座位还有留机，不能进入这个座位哦！");
					}
					obj.put("seatStatus", 0);
					obj.put("seatUser", "\"null\"");
					jedis.lset("room", obj.getInt("id"), obj.toString());
				}
			}
		});
	}

	@Override
	public Seat getSeatBean(int seatId) {
		if(seatId<0||seatId>=90){
			throw new IllegalArgumentException("参数错误，seatId:"+seatId);
		}
		String str = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				String seatStr = jedis.lindex("room", seatId);
				return seatStr;
			}
		});
		Seat s = null;
		if(str != null){
			s = (Seat) JSONObject.toBean(JSONObject.fromObject(str), Seat.class);
		}
		return s;
	}

	@Override
	public List<String> getRoomSeatDownSeatsByKey(int room) {
		if(room<=0||room>3){
			throw new IllegalArgumentException("参数错误，room:"+room);
		}
		List<String> list = temp.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> handle(Jedis jedis) {
				int seatNum = Integer.parseInt(Configs.configProps.getProperty("seat.num"));
				List<String> temp = jedis.lrange("room", seatNum*(room-1), seatNum*room-1);
				List<String> list = new ArrayList<>();
				JSONObject obj = null;
				for (String string : temp) {
					obj = JSONObject.fromObject(string);
					if(obj.getInt("seatStatus")==0){
						continue;
					}
					if(obj.getInt("seatStatus")==2) {
						long date = (obj.getLong("date")) - (new Date().getTime());
						System.out.println("时间差是："+date);
						obj.put("date", date);
						string = obj.toString();
					}
					list.add(string);
				}
				return list;
			}
		});
		return list;
	}

	@Override
	public int formatSeatByStaySeats(Seat s, int seatId) {
		int seatNum = Integer.parseInt(Configs.configProps.getProperty("seat.num"));
		int roomNum = Integer.parseInt(Configs.configProps.getProperty("roomNum"));
		if(seatId<0||seatId>roomNum*seatNum) {
			throw new IllegalArgumentException("seats index out of bounds!");
		}
		return temp.execute(new RedisCallback<Integer>() {
			@Override
			public Integer handle(Jedis redis) {
				JSONObject obj = JSONObject.fromObject(redis.lindex("room", seatId));
				if(obj.getInt("seatStatus")==2) {
					redis.lset("room", seatId, s.toString());//更新座位
					return 1;
				}
				return 0;
			}
		});
	}

	@Override
	public boolean validateUserToken(int userId, String token) {
		String vToken = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				return jedis.get(userId+"");
			}
		});
		System.out.println(vToken);
		return token.equals(vToken);
	}

	@Override
	public int[] selPoolScores(int seatId) {
		String str = temp.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				return jedis.lindex("poolScore", seatId);
			}
		});
		JSONObject obj = JSONObject.fromObject(str);
		int K4 = obj.getInt("4K");
		int SF = obj.getInt("SF");
		int RS = obj.getInt("RS");
		int K5 = obj.getInt("5K");
		return new int[] {K4, SF, RS, K5};
	}


}
