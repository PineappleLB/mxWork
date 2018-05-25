package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import model.Seat;
import net.sf.json.JSONObject;
import service.SeatService;
import service.UserService;
import service.impl.SeatServiceImpl;
import service.impl.UserServiceImpl;
import utils.Configs;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:30:07
 * @description 服务端的业务处理类
 */
public class LobbyServerHandler extends SimpleChannelInboundHandler<String>  {
	
	/**
	 * 客户端IP地址
	 */
	private String clientIP;
	
	/**
	 * 用户操作服务类
	 */
	private UserService userService = new UserServiceImpl();
	
	/**
	 * redis座位操作服务类
	 */
	private SeatService seatService = new SeatServiceImpl();
	
	/**
	 * 日志记录对象
	 */
	private static Logger logger = Logger.getLogger(LobbyServerHandler.class);  
	
	/**
	 * 当前用户的ID
	 */
	private int userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 超时断开连接的最大次数
	 */
	private int outTime = Integer.parseInt(Configs.configProps.getProperty("maxOutTime"));
	
	/**
	 * 当前超时的次数
	 */
	private int nowTime = 0;
	
	/**
	 * 当前用户是否坐在某个座位上
	 */
	private Seat seat;
	
	/**
	 * 属性文件加载类
	 */
	private Properties orderProps = Configs.orderProps;

	/**
	 * 当前通知消息
	 */
	private String notice;
	
	
	/**
	 * 当有客户端注册服务端时触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		clientIP = ctx.channel().remoteAddress().toString();
		
		logger.info("客户端"+clientIP+"请求链接大厅服务器成功！");
	}
	
	private String token = null;
	
	/**
	 * 当有客户端数据传入时触发
	 */
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, String str)
            throws Exception {
		
		logger.info("收到客户端数据："+clientIP+"[userid:"+userId+"] [userName:"+userName+"]"+str);
		nowTime = 0;//收到数据之后将计时次数置为0
		JSONObject obj = null;

        try
		{
        	obj = JSONObject.fromObject(str);
        	if(userId==0){
    			if(obj.getString("id").equals("")){
    				throw new Exception("id不能为空！");
    			}else{
    				userId = Integer.parseInt(obj.getString("id"));
    				userName = userService.getUserName(userId);
    				if(userName==null) {
    					throw new IllegalArgumentException("can not select username by id \"" + userId+"\"");
    				}
    			}
    		}
        	
        	if(token==null||"".equals(token)) {
        		token = obj.getString("token");
				if(!seatService.validateUserToken(userId,token)) {
					logger.error(userId+"尝试进入大厅错误，token不正确！");
					ctx.close();
				}
        	}
        	String order = obj.getString("order");
        	readOrder(ctx, obj, order);
		}
		catch (Exception e) 
		{
			//出错的情况下输出错误指令
			e.printStackTrace();
			logger.error("客户端"+clientIP+"【userid:"+userId+"】"+"请求出现异常，异常信息为："+e.getMessage());
			obj = new JSONObject();
			obj.put("order", "error");
			obj.put("message", e.getMessage());
			writeAndFlush(ctx, obj.toString());
		}
    }
	
	/**
	 * 像流里面写数据
	 * @param ctx
	 * @param msg
	 */
	private void writeAndFlush(ChannelHandlerContext ctx, String msg) {
		logger.info("返回客户端数据："+clientIP+"[userid:"+userId+"] [userName:"+userName+"]"+msg);
		ctx.writeAndFlush(msg);
	}
	
	/**
	 * 进入房间的指令操作方法
	 * @param ctx 操作读写对象
	 * @param obj json对象 
	 */
	private void getInRoom(ChannelHandlerContext ctx, JSONObject obj) {
		int roomNum = obj.getInt("room");
		if(roomNum<=0 || roomNum>3){
			throw new IllegalArgumentException("参数错误：room:"+roomNum);
		}
		userId = obj.getInt("id");
		List<String> seatList = seatService.getRoomSeatDownSeatsByKey(roomNum);//获取当前数据库中有人坐的座位
		
		obj = new JSONObject();
		//判断是否是正确的User对象，如果为空则返回失败信息，否则返回正确信息以及端口号和用户对象
		int multiplyPower = selectMutiplyPower(roomNum);
		obj.put("order", "success");
		Map<String,Object> m = new HashMap<>();
		m.put("seats", seatList);
		m.put("userNum", seatList.size());
		m.put("multiplyPower", multiplyPower);
		obj.put("room",m);
		writeAndFlush(ctx, obj.toString());
	}
	
	/**
	 * 根据房间号判断房间倍率
	 * @param room
	 * @return
	 */
	private int selectMutiplyPower(int room){
		switch(room){//根据房间号判断房间倍率
		case 1:
			return 20;
		case 2:
			return 5;
		case 3:
			return 2;
		}
		return 0;
	}
	
	/**
	 * 读取指令判断调用方法
	 * @param ctx channelhandlercontext独享
	 * @param obj json对象
	 * @param order 指令
	 * @throws Exception 
	 */
	public void readOrder(ChannelHandlerContext ctx, JSONObject obj,String order) throws Exception 
	{
		//加载属性文件中的命令并判断是否是进入房间命令
		if(orderProps.get("room").equals(order)){
			getInRoom(ctx, obj);
		}
		else if(orderProps.get("leaveSeat").equals(order)){
			leaveSeat(ctx,obj);
		}
		else if(orderProps.get("heart").equals(order)){
			readHeart(ctx,obj);
		}
		else if(orderProps.get("seat").equals(order)){
			seatDown(ctx,obj);
		}
		else if(orderProps.get("exit").equals(order)){
			userLogout(ctx,obj);
		}
		else if(orderProps.get("updateRoomInfo").equals(order)){
			updateRoomInfo(ctx,obj);
		}
		else if(orderProps.get("leaveRoom").equals(order)){
			if(seat!=null){
				seat.setSeatStatus(0);
				seat.setSeatUser(null);
				seatService.updateSeat(seat,seat.getId(),true);//更新数据库中的座位信息状态
				seat = null;
			}
		}
		else if(orderProps.get("staySeat").equals(order)) {
			staySeat(ctx,obj);
		}
		else{
			throw new IllegalArgumentException("\""+order+"\"命令不正确，不能处理的命令");
		}
	}
	
	/**
	 * 座位留机处理
	 * @param ctx
	 * @param obj
	 */
	private void staySeat(ChannelHandlerContext ctx, JSONObject obj) {
		int seatId = obj.getInt("seatNum");
		int userId = obj.getInt("id");
		int staySeatTime = userService.selectStaySeatTimeById(userId);
		if(staySeatTime<0) {
			throw new IllegalArgumentException("今日留机次数已用完，留机失败！");
		}
		seatService.updateSeatsInfoForStaySeat(userName, seatId);
		Seat s = new Seat(seat.getId());
		LobbyServer.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				int result = seatService.formatSeatByStaySeats(s, seatId);//更新座位信息
				if(result > 0) {
					logger.info(clientIP+"[userid:"+userId+"] [userName:"+userName+"]"+"清空了留机座位，seatId：" + s.getId());
				}
				this.cancel();
			}
		},Integer.parseInt(Configs.configProps.getProperty("staySeatTime")),Integer.MAX_VALUE);
		seat = null;
		writeAndFlush(ctx,"{\"order\":\"success\"}");
	}

	/**
	 * 用户正常退出操作
	 * @param ctx
	 * @param obj
	 */
	private void userLogout(ChannelHandlerContext ctx, JSONObject obj) {
		userService.UserLogout(userId,0);//用户直接离线，直接控制座位信息清空
		if(seat != null){
			seat.setSeatStatus(0);
			seat.setSeatUser(null);
			seatService.updateSeat(seat,seat.getId(),false);//更新数据库中的座位信息状态
			seat = null;
		}
		userId = 0;
	}

	/**
	 * 更新大厅信息
	 * @param ctx
	 * @param obj
	 */
	private void updateRoomInfo(ChannelHandlerContext ctx, JSONObject obj) {
		int roomId = obj.getInt("room");
		List<String> seatList = seatService.getRoomSeatDownSeatsByKey(roomId);
		obj = new JSONObject();
		obj.put("order", "success");
		Map<String,Object> m = new HashMap<>();
		m.put("seats", seatList);
		m.put("userNum", seatList.size());
		m.put("multiplyPower", selectMutiplyPower(roomId));
		obj.put("room", m);//重新获取座位信息返回客户端
		writeAndFlush(ctx, obj.toString());
	}

	/**
	 * 离开座位请求
	 * @param ctx
	 * @param obj
	 */
	private void leaveSeat(ChannelHandlerContext ctx, JSONObject obj) {
		//将该座位制空
		int seatId = obj.getInt("seatNum");
		seat = seatService.getSeatBean(seatId);
		seat.setSeatStatus(0);
		seat.setSeatUser(null);
		seatService.updateSeat(seat,seatId,true);//更新数据库中的座位信息状态
		seat = null;//将当前连接状态中的坐状态设置为正在坐
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("money", userService.getMoneyById(userId));
		writeAndFlush(ctx,obj.toString());
	}

	/**
	 * 请求进入座位开始游戏 进入座位成功增加房间玩家数量
	 * @param ctx 通信读写对象
	 * @param obj json对象
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void seatDown(ChannelHandlerContext ctx, JSONObject obj) throws FileNotFoundException, IOException {
		int num = obj.getInt("seatNum");
		Seat seat = seatService.getSeatBean(num);
		boolean seatLogic = false;
		if(seat==null){
			throw new IllegalArgumentException("参数错误，没有找到该座位信息：id-"+num);
		}
		if(seat.getSeatStatus()==2&&num==seat.getId()) {
			throw new IllegalArgumentException("参数错误，您不能进入其他用户正在留机的座位哦！");
		}
		
		//如果当前座位没人坐，则将座位状态改变并进入座位 
		if(!checkSeated(seat)){
			seatLogic = seatDown(seat);
		}
		obj = new JSONObject();
		if(seatLogic){
			obj.put("order", "success");
			obj.put("gamePort", Configs.configProps.getProperty("gamePort"));
			obj.put("maxScore", Configs.configProps.getProperty("maxScore"));
			obj.put("minScore", Configs.configProps.getProperty("minScore"));
			obj.put("bonus", seatService.selPoolScores(seat.getId()));
		}else{
			obj.put("order", "error");
			obj.put("message", "当前座位已有人坐");
			obj.put("seats", seatService.getRoomSeatDownSeatsByKey(num/30+1));
		}
		writeAndFlush(ctx, obj.toString());
	}


	/**
	 * 判断是否当前座位已有人坐
	 * @param seat 通过传入的座位号获取的座位信息
	 * @return true 有人坐 false 没人坐
	 */
	private boolean checkSeated(Seat seat) {
		return seat.getSeatStatus()==1&&seat.getSeatUser()!=null;
	}

	/**
	 * 进入座位改变座位当前状态 将当前座位状态改变，并将当前座位的用户名称设置
	 * @param seat 通过传入的座位号获取的座位信息
	 * @return true 状态改变成功 false 状态改变失败
	 */
	private boolean seatDown(Seat seat) {
		seat.setSeatStatus(1);
		if(userName==null){
			userName = userService.getUserName(userId);
		}
		seat.setSeatUser(userName);
		this.seat = seat;
		seatService.updateSeat(seat, seat.getId(),false);
		return seat.getSeatStatus()==1&&seat.getSeatUser()!=null;
	}

	/**
	 * 读取心跳打印日志
	 * @param obj 心跳包内容
	 */
	private void readHeart(ChannelHandlerContext ctx,JSONObject obj) {
		obj = new JSONObject();
		obj.put("order", orderProps.getProperty("heart"));
		String notice = LobbyServer.list.get(LobbyServer.list.size()-1);
		if(!(this.notice!=null && this.notice.equals(notice))) {
			obj.put("notices", notice);
			this.notice = notice;
		}
		writeAndFlush(ctx, obj.toString());
	}
	
	/**
	 * 检测未读写事件
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		//判断事件类型是否是IdleStateEvent
		if(evt instanceof IdleStateEvent){
			if(((IdleStateEvent) evt).state()==IdleState.WRITER_IDLE){
				if(nowTime >= outTime){
					writeAndFlush(ctx, "已断开连接");
					ctx.close();
				}else{
					nowTime++;
				}
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
	}
	
	/**
	 * 服务器断开连接触发
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("客户端"+clientIP+"【userid:"+userId+"】"+"断开了大厅链接"); 
		if(userId!=0){//控制将用户信息设置为暂离状态，可以随时登录回来
			userService.UserLogout(userId,3);
//			seatService.
		}
		if(seat!=null){//将座位信息清空
			seat.setSeatStatus(0);
			seat.setSeatUser(null);
			seatService.updateSeat(seat,seat.getId(),false);//更新数据库中的座位信息状态
			seat = null;
		}
	}
	
}
