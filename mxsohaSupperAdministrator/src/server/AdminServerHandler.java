package server;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Admin;
import model.Promoter;
import net.sf.json.JSONObject;
import service.RedisService;
import service.UserService;
import service.impl.RedisServiceImpl;
import service.impl.UserServiceImpl;
import utils.Configs;
import utils.StringUtils;

/**
 * @author pineapple
 * @date 2017年12月28日 上午10:11:40
 * @description 登录请求处理类
 */
public class AdminServerHandler extends SimpleChannelInboundHandler<String>{
	
	private UserService service = new UserServiceImpl();
	
	private RedisService redisService = new RedisServiceImpl();
	
	private static Logger logger = Logger.getLogger(AdminServerHandler.class);  
	
	private String clientIp;
	
	private Properties orderProps = Configs.orderProps;
	
	private Properties configProps = Configs.configProps;
	
	private Admin admin;
	
	/**
	 * 客户端激活时触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		clientIp = ctx.channel().remoteAddress().toString();
		logger.info(clientIp+"请求链接登录服务端成功");
	}

	/**
	 * 处理客户端传入数据的方法
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext context, String str) throws Exception {
		
		logger.info(clientIp+"收到客户端数据："+str);
		JSONObject obj = null;
		try
		{
			obj = JSONObject.fromObject(str);
			String order = obj.getString("order");
			//读取命令判断是否是登录命令
			if(orderProps.getProperty("adminLogin").equals(order)){
				adminLogin(context,obj);
			}
			//读取命令判断是否是添加金币命令
			else if(orderProps.getProperty("addPromoterMoney").equals(order)){
//				addMoneyByAdmin(context,obj);
				addReduceMoneyByAdminRecords(context, obj);
			}
			else if(orderProps.getProperty("promotersLogin").equals(order)) {
				promoterLogin(context, obj);
			}
			else if(orderProps.getProperty("selectAdminRecords").equals(order)) {
				selectAdminRecords(context, obj);
			}
			else if(orderProps.getProperty("control").equals(order)){
				saveControllerValue(context,obj);
			}
			else if(orderProps.getProperty("reducePromoterMoney").equals(order)) {
//				reduceMoneyByAdmin(context, obj);
				addReduceMoneyByAdminRecords(context, obj);
			}
			else if(orderProps.getProperty("updatePassword").equals(order)) {
				updatePassword(context, obj);
			}
			else if(orderProps.getProperty("download").equals(order)) {
				gameDownload(context,obj);
			}
			else if(orderProps.getProperty("addPromoter").equals(order)) {
				addPromoter(context, obj);
			}
			else if(orderProps.getProperty("selectChildProm").equals(order)) {
				selectChildProm(context, obj);
			}
			else if(orderProps.getProperty("selectUsersInfo").equals(order)) {
				selectUsersInfo(context, obj);
			}
			else if(orderProps.getProperty("addUserMoney").equals(order)) {
				addUserMoney(context, obj);
			}
			else if(orderProps.getProperty("reduceUserMoney").equals(order)) {
				reduceUserMoney(context, obj);
			}
			else if(orderProps.getProperty("selectUserAddRecords").equals(order)) {
				selectUserAddRecords(context, obj);
			}
			else if(orderProps.getProperty("selectUserReduceRecords").equals(order)) {
				selectUserReduceRecords(context, obj);
			}
			else if(orderProps.getProperty("selectAddMoneyByDay").equals(order)) {
				selectAddMoneyByDay(context, obj);
			}
			else if(orderProps.getProperty("selectReduceMoneyByDay").equals(order)) {
				selectReduceMoneyByDay(context, obj);
			}
			else if(orderProps.getProperty("selectMoneyByMonth").equals(order)) {
				selectMoneyByMonth(context, obj);
			}
			else if(orderProps.getProperty("selectGameScoreRecords").equals(order)) {
				selectGameScoreRecords(context, obj);
			}
			else if(orderProps.getProperty("selectSystemLogs").equals(order)) {
				selectSystemLogs(context, obj);
			}
			else if(orderProps.getProperty("selectPromoterOrder").equals(order)) {
				selectPromoterOrder(context, obj);
			}
			else if(orderProps.getProperty("executeMessage").equals(order)) {
				executeMessage(context, obj);
			}
			else if(orderProps.getProperty("updatePromoterInfo").equals(order)) {
				updatePromoterInfo(context, obj);
			}
			else if(orderProps.getProperty("ctrlUserHard").equals(order)) {
				ctrlUserHard(context, obj);
			}
			else if(orderProps.getProperty("ctrlSeatHard").equals(order)) {
				ctrlSeatHard(context, obj);
			} 
			else if(orderProps.getProperty("clearInfoTable").equals(order)) {
				clearInfoTable(context, obj);
			}
			else if(orderProps.getProperty("delUserInfoById").equals(order)) {
				deleteUserInfoById(context, obj);
			}
			else if(orderProps.getProperty("delUserInfoByName").equals(order)) {
				deleteUserInfoByName(context, obj);
			}
			else{
				throw new Exception("错误的请求命令:"+order);//如果读取不到以上命令则抛出异常
			}
		}
		catch (Exception e) 
		{
			//出错的情况下输出错误指令
			e.printStackTrace();
			logger.error(clientIp+"请求出现异常："+e.getMessage());
			obj = new JSONObject();
			obj.put("order", "error");
			obj.put("message", e.getMessage());
			writeAndFlush(context, obj.toString());
		}
	}
	
	private void deleteUserInfoByName(ChannelHandlerContext context, JSONObject obj) {
		String name = obj.getString("name");
		int result = service.deleteUserInfo(0, name);
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	private void deleteUserInfoById(ChannelHandlerContext context, JSONObject obj) {
		int id = obj.getInt("id");
		int result = service.deleteUserInfo(id, null);
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	private void clearInfoTable(ChannelHandlerContext context, JSONObject obj) {
		int result = service.clearInfoTable();
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	private void ctrlSeatHard(ChannelHandlerContext context, JSONObject obj) {
		int seatId = obj.getInt("seatId");
		int hard = obj.getInt("hard");
		int result = service.ctrlSeatHard(hard, seatId);
		obj = new JSONObject();
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 调节玩家控制难度
	 * @param context
	 * @param obj
	 */
	private void ctrlUserHard(ChannelHandlerContext context, JSONObject obj) {
		int hard = obj.getInt("hard");
//		int userId = obj.getInt("userId");
		String userName = obj.getString("userName");
		int result = service.ctrlUserHard(hard, userName);
		obj = new JSONObject();
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 更新推广员信息
	 * @param context
	 * @param obj
	 */
	private void updatePromoterInfo(ChannelHandlerContext context, JSONObject obj) {
		String name = obj.getString("name");
		String pass = obj.getString("pass");
		//调用service中的方法验证用户信息，并返回完整的用户信息
		Promoter pro = service.promLogin(name, pass);
		obj = new JSONObject();
		//判断是否是正确的User对象，如果为空则返回失败信息，否则返回正确信息以及端口号和用户对象
		if(pro==null){
			obj.put("order", "error");
			obj.put("message", "登录失败，用户名或密码错误");
			writeAndFlush(context, obj.toString());
			return;
		}
		obj.put("order", "success");
		obj.put("promoter", pro);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 处理消息
	 * @param context
	 * @param obj
	 */
	private void executeMessage(ChannelHandlerContext context, JSONObject obj) {
		String messageId = obj.getString("messageId");
		int sure = obj.getInt("sure");
		obj = redisService.excutePromoterOrder(messageId, sure);
//		System.out.println(obj);
		if(obj != null) {
			addMoneyByAdmin(context, obj);
		}else {
			writeAndFlush(context, "{\"order\":\"success\"}");
		}
	}

	/**
	 * 请求查看推广员的申请消息
	 * @param context
	 * @param obj
	 */
	private void selectPromoterOrder(ChannelHandlerContext context, JSONObject obj) {
		List<String> list = redisService.selectPromoterOrder();
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 添加处理消息的信息
	 * @param context
	 * @param obj
	 */
	private void addReduceMoneyByAdminRecords(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int money = obj.getInt("money");
		String promName = service.selectPromNameById(promId);
		redisService.addPromtersOrderRecords(promId,money,promName);
		obj = new JSONObject();
		obj.put("order", "success");
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 像流写入数据
	 * @param context
	 * @param str
	 */
	private void writeAndFlush(ChannelHandlerContext context, String str) {
		logger.info("返回客户端"+clientIp+"数据："+str);
		context.writeAndFlush(str);
	}
	
	/**
	 * 系统日志查询
	 * @param context
	 * @param obj
	 */
	private void selectSystemLogs(ChannelHandlerContext context, JSONObject obj) {
		int day = obj.getInt("day");
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectSystemLogs(day, month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 游戏运营统计
	 * @param context
	 * @param obj
	 */
	private void selectGameScoreRecords(ChannelHandlerContext context, JSONObject obj) {
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectGameScoreRecords(month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 每月营收详情
	 * @param context
	 * @param obj
	 */
	private void selectMoneyByMonth(ChannelHandlerContext context, JSONObject obj) {
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectMoneyByMonth(month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 每日下分报告 
	 * @param context
	 * @param obj
	 */
	private void selectReduceMoneyByDay(ChannelHandlerContext context, JSONObject obj) {
		int day = obj.getInt("day");
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectReduceMoneyByDay(day, month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 每日上分报告查询
	 * @param context
	 * @param obj
	 */
	private void selectAddMoneyByDay(ChannelHandlerContext context, JSONObject obj) {
		int day = obj.getInt("day");
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectAddMoneyByDay(day, month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 推广员查询用户兑奖记录
	 * @param context
	 * @param obj
	 */
	private void selectUserReduceRecords(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int day = obj.getInt("day");
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectReduceRecords(promId, day, month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 推广员查询用户上分记录
	 * @param context
	 * @param obj
	 */
	private void selectUserAddRecords(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int day = obj.getInt("day");
		int month = obj.getInt("month");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectUserAddRecords(promId, day, month, year);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 推广员申请减值
	 * @param context
	 * @param obj
	 */
	private void reduceMoneyByAdmin(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int money = obj.getInt("money");
		int promMoney = service.reduceMoneyByAdmin(promId,money);
		obj = new JSONObject();
		if(promMoney > 0) {
			obj.put("order", "success");
			obj.put("balance", promMoney);
			service.addSystemLog("推广员("+promId+")兑奖"+money+"币", 0);
		}else {
			obj.put("order", "error");
			obj.put("message", "兑奖失败！");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 推广员申请充值
	 * @param context
	 * @param obj
	 */
	private void addMoneyByAdmin(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int money = obj.getInt("money");
		int promMoney = service.addMoneyByAdmin(promId,money);
		obj = new JSONObject();
		if(promMoney > 0) {
			obj.put("order", "success");
			obj.put("balance", promMoney);
			service.addSystemLog("推广员("+promId+")充值"+money+"币", 0);
		}else {
			obj.put("order", "error");
			obj.put("message", "充值失败！");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 查询推广员下面的直属会员
	 * @param context
	 * @param obj
	 */
	private void selectUsersInfo(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		List<JSONObject> list = service.selectUsersInfo(promId);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 查询子级的下一级推广员
	 * @param context
	 * @param obj
	 */
	private void selectChildProm(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		List<JSONObject> list = service.selectClindPromInfo(promId);
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("info", list);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 添加推广员
	 * @param context
	 * @param obj
	 */
	private void addPromoter(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		Promoter prom = service.getSupperParentPromoter(promId);
		if(prom!=null) {
			throw new IllegalArgumentException("没有开通管理权限！");
		}
		String name = obj.getString("name");
		String password = obj.getString("password");
		prom = new Promoter(name,password,promId);
		int result = service.addChildPromoter(prom);
		obj = new JSONObject();
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 获取下载地址
	 * @param context
	 * @param obj
	 */
	private void gameDownload(ChannelHandlerContext context, JSONObject obj) {
		String address = configProps.getProperty("downloadAddress");
		int promId = obj.getInt("id");
		obj = new JSONObject();
		obj.put("order", "success");
		obj.put("src", address+"?promId="+promId);
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 查询管理员充值或减值记录
	 * @param context
	 * @param obj
	 */
	private void selectAdminRecords(ChannelHandlerContext context, JSONObject obj) {
		int month = obj.getInt("month");
		int promId = obj.getInt("id");
		int year = obj.getInt("year");
		List<JSONObject> list = service.selectAdminRecords(promId, month, year);
		obj = new JSONObject();
		obj.put("info", list);
		obj.put("order", "success");
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 更新推广员密码
	 * @param context
	 * @param obj
	 */
	private void updatePassword(ChannelHandlerContext context, JSONObject obj) {
		int id = obj.getInt("id");
		String oldPass = obj.getString("oldPass");
		String newPass = obj.getString("newPass");
		int result = service.promUpdatePwd(id,oldPass,newPass);
		obj = new JSONObject();
		if(result > 0) {
			obj.put("order", "success");
		}else {
			obj.put("order", "error");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 推广员登录
	 * @param context
	 * @param obj
	 */
	private void promoterLogin(ChannelHandlerContext context, JSONObject obj) {
		String name = obj.getString("name");
		String pass = obj.getString("pass");
		//调用service中的方法验证用户信息，并返回完整的用户信息
		Promoter pro = service.promLogin(name, pass);
		
		obj = new JSONObject();
		//判断是否是正确的User对象，如果为空则返回失败信息，否则返回正确信息以及端口号和用户对象
		if(pro==null){
			obj.put("order", "error");
			obj.put("message", "登录失败，用户名或密码错误");
			writeAndFlush(context, obj.toString());
			return;
		}
		obj.put("order", "success");
		obj.put("promPort", configProps.getProperty("promPort"));
		obj.put("promHost", configProps.getProperty("host"));
		obj.put("promoter", pro);
		writeAndFlush(context, obj.toString());
		service.addSystemLog("登录系统", pro.getId());
	}

	/**
	 * 保存控制位难度
	 * @param context
	 * @param obj
	 */
	private void saveControllerValue(ChannelHandlerContext context, JSONObject obj) {
		int val = obj.getInt("value");
		redisService.saveControllerValue(val);
		writeAndFlush(context, "{\"order\":\"success\"}");
	}

	/**
	 * 金币充值
	 * @param context
	 * @param obj
	 * @throws Exception 
	 */
	private void addUserMoney(ChannelHandlerContext context, JSONObject obj) throws Exception {
		
		int promId = obj.getInt("id");
		int userid = obj.getInt("userId");//需要充值的用户ID
		int money = obj.getInt("money");//需要充值的金额
		
		double userMoney = service.addPromoterBalance(promId, userid, money);
		obj = new JSONObject();
		if(userMoney < 0){
			obj.put("order", "error");
			obj.put("message", "操作异常，添加金币失败");
		}else {
			obj.put("order", "success");
			obj.put("money", userMoney);
			service.addSystemLog("会员("+promId+")充值"+money+"币", promId);
		}
		writeAndFlush(context, obj.toString());
	}
	
	/**
	 * 金币减值
	 * @param context
	 * @param obj
	 */
	private void reduceUserMoney(ChannelHandlerContext context, JSONObject obj) {
		int promId = obj.getInt("id");
		int userid = obj.getInt("userId");//需要减值的用户ID
		int money = obj.getInt("money");//需要减值的金额
		
		double userMoney = service.reduceUserMoney(promId, userid, money);
		obj = new JSONObject();
		if(userMoney < 0){
			obj.put("order", "error");
			obj.put("message", "操作异常，金币减值失败");
		}else {
			obj.put("order", "success");
			obj.put("money", userMoney);
			service.addSystemLog("会员("+promId+")兑奖"+money+"币", 0);
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 管理员登录
	 * @param context
	 * @param str
	 */
	private void adminLogin(ChannelHandlerContext context, JSONObject obj) {
		String name = obj.getString("name");
		
		String pass = obj.getString("pass");
		
		//调用service中的方法验证用户信息，并返回完整的用户信息
		Admin u = service.userLogin(name, pass);
		//promtersOrderRecords
		int infoSize = redisService.selectInfoSize();
		obj = new JSONObject();
		//判断是否是正确的User对象，如果为空则返回失败信息，否则返回正确信息以及端口号和用户对象
		if(u==null){
			obj.put("order", "error");
			obj.put("message", "登录失败，用户名或密码错误");
			writeAndFlush(context, obj.toString());
			return;
		}
		obj.put("order", "success");
		obj.put("infoSize", infoSize);
		this.admin = u;
		service.updateuserIp(u.getUsername(), StringUtils.getIpFromRemoteIp(context.channel().remoteAddress().toString()));
		service.addSystemLog("登录系统",0);
		writeAndFlush(context, obj.toString());
	}

	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info(clientIp+":断开了链接");
		adminlogout();
	}

	/**
	 * 管理员退出登录
	 */
	private void adminlogout() {
		if(admin != null) {
			service.logout(admin.getUsername());
			service.addSystemLog("退出系统",0);
		}
	}

	
}
