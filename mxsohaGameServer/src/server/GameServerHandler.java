package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import client.CountClient;
import client.CountClientHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.RedisSession;
import serial.SerialPortSession;
import service.RedisService;
import service.UserService;
import service.impl.RedisServiceImpl;
import service.impl.UserServiceImpl;
import utils.AwardCards;
import utils.CardUtil;
import utils.Configs;
import utils.GameScoreRecords;
import utils.GameUtil;
import utils.RequestByteArrayUtil;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:30:07
 * @description 服务端的业务处理类
 */
public class GameServerHandler extends SimpleChannelInboundHandler<String> {

	/**
	 * 当前客户端的ip地址
	 */
	private String clientIP;
	
	/**
	 * 用户服务类对象
	 */
	private UserService service = new UserServiceImpl();

	/**
	 * 游戏发牌的工具对象
	 */
	private GameUtil game;
	
	/**
	 * log4j日志记录对象
	 */
	private static Logger logger = Logger.getLogger(GameServerHandler.class);  
	
	/**
	 * 默认的分数
	 */
	private int score = 20;
	
	/**
	 * 当前本局游戏的分数
	 */
	private int gameScore = 0;
	
	/**
	 * 当前链接用户的用户名
	 */
	private String userName;
	
	/**
	 * 当前本局游戏的用户的id
	 */
	private int userId;
	
	/**
	 * 心跳包超时的最大次数，超过该数量即断开连接
	 */
	private int outTime = Integer.MAX_VALUE;
	
	/**
	 * 当前心跳包超时的次数
	 */
	private int nowTime = 0;
	
	/**
	 * 本次游戏的倍率
	 */
	private int multiplyPower;
	
	/**
	 * 要计算的列的倍数
	 */
	private int lineNum;
	
	/**
	 * 属性文件加载
	 */
	private Properties orderProps = Configs.orderProps;
	
	/**
	 * 当局牌的游戏信息
	 */
	private int[] cardMessage;
	
	/**
	 * 当前比倍的次数
	 */
	private int doubleCardTime = 1;
	
	/**
	 * 当前游戏进行中的座位id
	 */
	private Integer seatId;
	
	/**
	 * 当前游戏进行中的房间id
	 */
	private int roomId;
	
	private String username;
	
	private RequestByteArrayUtil requestUtil = new RequestByteArrayUtil();
	
	RedisService redis = new RedisServiceImpl();
	
	/**
	 * 是否此时应该增加奖池分数
	 */
	private boolean addScoreLogic = false;
	
	public CountClientHandler handler;
	
	/**
	 * 当有客户端注册服务端时触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		clientIP = ctx.channel().remoteAddress().toString();
		logger.info(clientIP+"请求游戏链接成功");
		new Thread(() ->{
			try {
				new CountClient().connect(Configs.countServerPort, Configs.countServerIp, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("客户端"+clientIP+"【userid:"+userId+"】"+"断开了游戏链接");
	}
	
	/**
	 * 当有客户端数据传入时触发
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {

		logger.info("收到客户端数据："+clientIP+"[userid:"+userId+"] [userName:"+userName+"]"+str);
		nowTime = 0;//收到数据之后将计时次数置为0
		JSONObject obj = null;

		try {
			obj = JSONObject.fromObject(str);
			if(userId==0){
				String idStr = obj.getString("id");
    			if(idStr==null||idStr.equals("")){
    				throw new Exception("id不能为空！");
    			}else{
    				userId = Integer.parseInt(idStr);
    				userName = service.getUserName(userId);
    			}
    		}
			String order = obj.getString("order");
			readOrder(ctx, obj, order);
		} catch (Exception e) {
			// 出错的情况下输出错误指令
			logger.error("客户端"+clientIP+"【userName:"+userName+"】"+"请求出现异常，异常信息为："+e.getMessage());
			e.printStackTrace();
			obj = new JSONObject();
			obj.put("order", "error");
			obj.put("message", e.getMessage());
			writeAndFlush(ctx, obj.toString());
		}
	}

	
	private void writeAndFlush(ChannelHandlerContext ctx, String str) {
		logger.info("返回客户端数据："+clientIP+"[userid:"+userId+"] [userName:"+userName+"]"+str);
		ctx.writeAndFlush(str);
	}
	
	/**
	 * 读取指令判断调用方法
	 * @param ctx channelhandlercontext独享
	 * @param obj json对象
	 * @param order 指令
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void readOrder(ChannelHandlerContext ctx, JSONObject obj, String order)
			throws FileNotFoundException, IOException, SQLException {
		// 加载属性文件
		
		// 加载属性文件中的命令并判断是否是登录命令
		if (orderProps.get("getCard").equals(order)) {
			getCards(ctx, obj);
		} 
		else if (orderProps.get("replaceCard").equals(order)) {
			replaceCard(ctx, obj);
		} 
		else if (orderProps.get("double").equals(order)) {
			doubleCard(ctx, obj);
		} 
		else if (orderProps.get("getScore").equals(order)) {
			getScore(ctx, obj);
		}
		else if(orderProps.getProperty("heart").equals(order)){
			readHeart(ctx,obj);
		}
		else if(orderProps.getProperty("selectSeatRecords").equals(order)) {
			selectSeatRecords(ctx, obj);
		}
		else {
			throw new IllegalArgumentException("参数错误，未能处理的指令："+order);
		}
	}

	/**
	 * 查询座位的游戏记录
	 * @param ctx
	 * @param obj
	 */
	private void selectSeatRecords(ChannelHandlerContext ctx, JSONObject obj) {
		int seatId = obj.getInt("seatId");
		Map<String,Object> map = service.selectSeatRecords(seatId);
//		JSONObject map = service.selectSeatRecords(seatId);
		obj = new JSONObject();
		obj.put("order", "success");
		int[] info = GameScoreRecords.gameRecords[seatId];
		
//		Integer i = (Integer)map.get("pay");
		System.out.println(Arrays.toString(info));
		info[1] += map.get("pay")==null?0:Integer.parseInt(map.get("pay").toString());
		info[2] += map.get("got")==null?0:Integer.parseInt(map.get("got").toString());
		info[3] += map.get("playTime")==null?0:Integer.parseInt(map.get("playTime").toString());
		info[4] += map.get("winTime")==null?0:Integer.parseInt(map.get("winTime").toString());
		info[5] += map.get("doublePay")==null?0:Integer.parseInt(map.get("doublePay").toString());
		info[6] += map.get("doubleGot")==null?0:Integer.parseInt(map.get("doubleGot").toString());
		info[7] += map.get("doublePlayTime")==null?0:Integer.parseInt(map.get("doublePlayTime").toString());
		info[8] += map.get("doubleWinTime")==null?0:Integer.parseInt(map.get("doubleWinTime").toString());
		info[9] += map.get("handsel")==null?0:Integer.parseInt(map.get("handsel").toString());
		info[10] += map.get("handSelTime")==null?0:Integer.parseInt(map.get("handSelTime").toString());
		info[11] += map.get("noPair")==null?0:Integer.parseInt(map.get("noPair").toString());
		info[12] += map.get("onePair")==null?0:Integer.parseInt(map.get("onePair").toString());
		info[13] += map.get("twoPair")==null?0:Integer.parseInt(map.get("twoPair").toString());
		info[14] += map.get("three")==null?0:Integer.parseInt(map.get("three").toString());
		info[15] += map.get("straight")==null?0:Integer.parseInt(map.get("straight").toString());
		info[16] += map.get("flush")==null?0:Integer.parseInt(map.get("flush").toString());
		info[17] += map.get("fullHouse")==null?0:Integer.parseInt(map.get("fullHouse").toString());
		info[18] += map.get("4K")==null?0:Integer.parseInt(map.get("4K").toString());
		info[19] += map.get("SF")==null?0:Integer.parseInt(map.get("SF").toString());
		info[20] += map.get("RS")==null?0:Integer.parseInt(map.get("RS").toString());
		info[21] += map.get("5K")==null?0:Integer.parseInt(map.get("5K").toString());
		obj.put("info", info);
		writeAndFlush(ctx, obj.toString());
	}

	/**
	 * 获取分数结果
	 * @param ctx
	 * @param obj
	 * @throws SQLException 
	 */
	private void getScore(ChannelHandlerContext ctx, JSONObject obj) throws SQLException {
		obj = new JSONObject();
		obj.put("order", "success");
		//计算总得分，用随机的列数来获取列数对应的倍数乘以押的分数
//		gameScore = CardUtil.scores[lineNum][game.getType()] * score * CardUtil.doubleScore[doubleCardIndex];
		obj.put("score", gameScore);
		if(gameScore>0){
			service.updateMoney(gameScore*1.0/multiplyPower, userId);
		}
		
		if(doubleCardTime==1){//如果已经比倍过了则不保存用户座位信息
			service.updateSeatInfo(score,gameScore,seatId);
		}
		if(doubleCardTime>1) {
			service.updateUserInfo(0,gameScore,userId);
		}else {
			service.updateUserInfo(score,gameScore,userId);
		}
		
		writeAndFlush(ctx, obj.toString());
	}

	/**
	 * 获取比倍牌的方法
	 * 比倍的时候第一次将gameScore分数同步到score，赢得时候每次更新gameScore的值
	 * 每次更新完比倍数据之后将gameScore同步到score，每次更新的比倍分数是gameScore-score
	 * 比倍判断 ： 0 输 ，1 赢 2 平
	 * @param ctx
	 * @param obj
	 * @throws SQLException 
	 */
	private void doubleCard(ChannelHandlerContext ctx, JSONObject obj) throws SQLException {
//		if(doubleCardTime==1){//第一次操作比倍先保存分数
//			service.updateUserInfo(score,gameScore,userId);
//			service.updateSeatInfo(score,gameScore,seatId);
//		}
		
//		score = gameScore;
		if(doubleCardTime==1) {//第一次保存，之后不保存
			score=gameScore;
		}
		int result = cardMessage[doubleCardTime++];//直接获取当前必备的结果
		service.addRecords(userId,seatId,1,gameScore,result,gameScore*result,multiplyPower);
		if(result == 0){//比倍输的情况
			if(doubleCardTime==2) {//doubleCardTime++=2
				service.updateSeatDoubleScore(gameScore, 0,seatId);//更新比倍信息
			}
			doubleCardTime=0;
		}
		else{//比倍赢得情况
			if(doubleCardTime==1) {
				service.updateSeatDoubleScore(score, gameScore,seatId);//更新比倍信息
			}else {
				service.updateSeatDoubleScore(0, gameScore,seatId);//更新比倍信息
			}
			gameScore = gameScore*result;//其他情况将分数计算出来  先将gamescore计算出来然后保存到gotScore字段中
		}
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("doubleWin", result);
		writeAndFlush(ctx, obj.toString());
	}

	/**
	 * 替换牌的方法
	 * 
	 * @param ctx 通信的对象
	 * @param obj 传入的字符串解析为json对象
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void replaceCard(ChannelHandlerContext ctx, JSONObject obj) {
		JSONArray arr= JSONArray.fromObject(obj.get("cards"));

		int beginSize = arr.size();//记录之前保留的牌的数量
		List<Integer> cards = game.getCardsList();
		
		//给数组对象增加牌
		for (int i = 0; i < 5-beginSize; i++) {
			arr.add(cards.get(5+i));
		}
		
		game.setNowCardList(JSONArray.toList(arr,ArrayList.class ));
		
		
		//判断当前手牌的类型
		int type = getCardType(game.getNowCardList());
		gameScore = CardUtil.scores[lineNum][type] * score;
		game.setType(type);
		
		if(type >= 7) { //判断是否是四条以上
			int score = getPoolScore(type,seatId);
			gameScore += score;
			int room = seatId/30+1;
			int seat = seatId%30+1;
			redis.addNotices(
				"恭喜房间"+room+" 座位"+seat+" 的用户："+userName+"获得了"+(type==7?"四条":type==8?"同花顺":type==9?"同花大顺":type==10?"五条":"")+",得到彩金："+score);
		}
		List<Integer> list = new ArrayList<>();
		
		for (int i = beginSize; i < arr.size(); i++) {
			list.add(arr.getInt(i));
		}
		
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("type", type);
		obj.put("cards", list);
		
		writeAndFlush(ctx, obj.toString());
		logger.debug("客户端"+clientIP+"【userName:"+userName+"】"+"当前的手牌为："+game.getNowCardList().toString());
	}

	/**
	 * 根据牌型获取奖池分数
	 * @param type
	 * @param seatId 
	 * @return
	 */
	private int getPoolScore(int type, Integer seatId) {
		int score = 0;
		switch(type) {
		case 7:
			score = redis.selPoolScore("4K",seatId);
			break;
		case 8:
			score = redis.selPoolScore("SF",seatId);
			break;
		case 9:
			score = redis.selPoolScore("RS",seatId);
			break;
		case 10:
			score = redis.selPoolScore("5K",seatId);
			break;
		}
		return score;
	}

	/**
	 * 获取牌的类型
	 * @param list 要判断的5张牌
	 * @return
	 */
	private int getCardType(List<Integer> list) {
		int [] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		int result = AwardCards.award(arr);
		return result;
	}
	
	/**
	 * 获取牌信息
	 * 
	 * @param ctx 通信读写对象
	 * @param obj json对象
	 * @throws SQLException 
	 */
	@SuppressWarnings("all")
	private void getCards(ChannelHandlerContext ctx, JSONObject obj) throws SQLException {
		doubleCardTime = 1;//重新发牌时重置比倍次数
		
		seatId = obj.getInt("seatId");
		
		multiplyPower=selectMutiplyPower(seatId);
		
		this.score = obj.getInt("score");//同步初始分数
		if(score>Integer.parseInt(Configs.configProps.getProperty("maxScore"))){
			throw new IllegalArgumentException("参数错误，分数超过最大押分限制");
		}
//		else if(score<GameUtil.minScore){
//			throw new IllegalArgumentException("参数错误，分数不满足最小押分需求");
//		}
		if(this.score / multiplyPower > service.getUserMoneyById(userId)) {
			throw new IllegalArgumentException("发牌失败，您的余额不足！");
		}
		if(username==null || "".equals(username)){
			username = service.getUserName(userId);
		}
		//获取需要发送到串口的数据
		seatId = RedisSession.getSeatsInfo(username,seatId);
		if(seatId==null){
			throw new IllegalArgumentException("参数错误，您的座位信息异常，您未在当前座位！");
		}
		byte request[] = request = requestUtil.createRequestMessage(userId,roomId,seatId,score,this);
		//获取串口的数据，返回牌的类型与比倍的结果，同时更新用户信息表中的payscore字段
//		cardMessage = SerialPortSession.getCardMessage(request, userId, roomId, seatId,score);//发送数据并等待返回数据
		
		//TODO 新的tcp连接模式获取数据
		cardMessage = handler.sendMessage(request, userId, roomId, seatId,score);
		
		int type = CardUtil.selectCardTypeByPower(cardMessage[0]);
//		int type = (int)(Math.random()*5)+6;
		
		game = new GameUtil(type);
		logger.info("客户端"+clientIP+"【userName:"+userName+"】"+"生成牌"+game.getCardsList().toString()+",牌型为："+type);
		service.updateMoney(-1.0*score/multiplyPower,userId);
		obj = new JSONObject();
		if(this.addScoreLogic) {//当现在需要增加奖池的分数的时候
			int[] result = redis.addPoolScores(Integer.parseInt(Configs.configProps.getProperty("poolScore","1")),seatId);
			obj.put("bonus", result);
			this.addScoreLogic=!this.addScoreLogic;
		}
		//将前五张的卡牌放入输出对象中
		obj.put("cards", game.getNowCardList());
		lineNum = (int)(Math.random()*3);
		obj.put("double", lineNum);
		obj.put("order", "success");
//		obj.put("牌型", type);
		writeAndFlush(ctx, obj.toString());
	}
	
	

	/**
	 * 根据座位号判断房间倍率
	 * @param room
	 * @return
	 */
	private int selectMutiplyPower(int seatId){
		if(seatId >= 90 && seatId < 0) {
			throw new IllegalArgumentException("error message :seatId "+seatId);
		}else if(seatId < 30) {
			return 20;
		}else if(seatId < 60) {
			return 5;
		}else {
			return 2;
		}
	}
	
	
	/**
	 * 读取心跳打印日志
	 * @param obj 心跳包内容
	 */
	private void readHeart(ChannelHandlerContext ctx, JSONObject obj) {
		writeAndFlush(ctx, "{\"order\":"+orderProps.getProperty("heart")+"}");
	}

	/**
	 * 检测未读写事件
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		// 判断事件类型是否是IdleStateEvent
		if (evt instanceof IdleStateEvent) {

			if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
				if(nowTime >= outTime){
					writeAndFlush(ctx, clientIP+"即将断开连接");
					ctx.close();
				}else{
					nowTime++;
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	public boolean isAddScoreLogic() {
		return addScoreLogic;
	}

	public void setAddScoreLogic(boolean logic) {
		this.addScoreLogic = logic;
	}

}
