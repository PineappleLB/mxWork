package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import model.User;
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
public class LoginServerHandler extends SimpleChannelInboundHandler<String>{
	
	private UserService service = new UserServiceImpl();
	
	private RedisService redis = new RedisServiceImpl();
	
	private static Logger logger = Logger.getLogger(LoginServerHandler.class);  
	
	
	private String clientIp;
	
	/**
	 * 加载指令的properties文件
	 */
	private Properties orderProps = Configs.orderProps;
	
	private Properties configProps = Configs.configProps;
	

	/**
	 * 客户端激活时触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		clientIp =ctx.channel().remoteAddress().toString();
		logger.info(clientIp+"请求链接登录服务端成功");
		String str = "abc";
		str.contains(new String(new byte[]{3}));
	}

	/**
	 * 处理客户端传入数据的方法
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext context, String str) throws Exception {
		logger.info("收到客户端"+clientIp+"数据："+str);
		JSONObject obj = null;
		try 
		{
			obj = JSONObject.fromObject(str);
			String order = obj.getString("order");
			
			//判断是否是登录命令
			if(orderProps.getProperty("login").equals(order))
			{
				userLogin(context, obj);//进入登录操作
			}
			else if(orderProps.getProperty("regist").equals(order)){
				userRegist(context, obj);
			}
			else if(orderProps.getProperty("replaceHost").equals(order)) {
				replaceLobbyHost(context, obj);
			}
			else{
				throw new Exception("错误的请求命令:"+order);
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
	 * 更换大厅主机地址请求
	 * @param context
	 * @param obj
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void replaceLobbyHost(ChannelHandlerContext context, JSONObject obj) throws FileNotFoundException, IOException {
		String host = StringUtils.getIpFromRemoteIp(context.channel().remoteAddress().toString());
		String host2 = configProps.getProperty("lobbyHost");//暂时存储的信息
		String url = configProps.getProperty("jdbc.url");
		configProps.setProperty("lobbyHost", host);
		configProps.setProperty("jdbc.url", "jdbc:mysql://"+host+":3306/mxsoha");
		try {
			configProps.store(new FileOutputStream(new File(Configs.loginConfigDir)), "UTF-8");//将这个配置文件写到原来的文件中
			if(!service.adminValidate()) {
				throw new IllegalArgumentException("controller state exception");
			}
			service.updateControllerValue(0);//处理结束后将控制位清零
			if(host!=null && host.equals(configProps.getProperty("lobbyHost"))) {//如果IP相同则直接结束方法
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			configProps.setProperty("lobbyHost", host2);
			configProps.setProperty("jdbc.url", url);
			configProps.store(new FileOutputStream(new File(Configs.loginConfigDir)), "UTF-8");//将这个配置文件写到原来的文件中
			
		}
		
		
	}

	/**
	 * 用户注册的方法
	 * 
	 * @param context
	 * @param obj
	 * @throws NoSuchAlgorithmException 
	 */
	private void userRegist(ChannelHandlerContext context, JSONObject obj) throws NoSuchAlgorithmException {

		// 获取注册用户信息
		String name = obj.getString("name");
		if(name!=null && name.length()>10) {
			throw new IllegalArgumentException("用户名过长！");
		}
		if(service.verifyName(name)) {
			throw new IllegalArgumentException("用户名已经存在！");
		}
		int beInvited = obj.getInt("beInvited");
		//判断邀请码是否存在
		if(service.userCount(beInvited)<=0) {
			throw new IllegalArgumentException("您的邀请码不正确！");
		}
		
		String pass = obj.getString("pass");
		// 生成随机的验证码
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString().replace("-", "");
		
		User u = new User();
		// 获取注册账号的ip地址
		u.setIp(StringUtils.getIpFromRemoteIp(clientIp));
		// 注册信息的添加
		u.setUsername(name);
		u.setPassword(StringUtils.encode(pass+token));
		u.setToken(token);
		u.setBeInvited(beInvited);
		// 调用注册方法得到受影响的行数
		int line = service.userRegister(u);
		obj = new JSONObject();
		// 判断返回的受影响行数，如果行数为1，则注册成功；行数为0则注册失败。
		if (line > 0) {
			obj.put("order", "success");
			obj.put("message", "恭喜您！注册成功！");
		} else {
			obj.put("order", "error");
			obj.put("message", "注册失败，请重新注册！");
		}
		writeAndFlush(context, obj.toString());
	}

	/**
	 * 用户登录的方法
	 * @param context
	 * @param obj
	 */
	@SuppressWarnings("deprecation")
	private void userLogin(ChannelHandlerContext context, JSONObject obj) {
		String name = obj.getString("name");
		
		String pass = obj.getString("pass");
		
		//调用service中的方法验证用户信息，并返回完整的用户信息
		User u = service.userLogin(name, pass);
		
		obj = new JSONObject();
		//判断是否是正确的User对象，如果为空则返回失败信息，否则返回正确信息以及端口号和用户对象
		if(u==null){
			obj.put("order", "error");
			obj.put("message", "登录失败，用户名或密码错误");
			writeAndFlush(context, obj.toString());
			return;
		}
		obj.put("order", "success");
		obj.put("lobbyPort", configProps.getProperty("lobbyPort"));
		obj.put("lobbyHost", configProps.getProperty("lobbyHost"));
		u.setPassword(null);
		String token = StringUtils.getToken4User();
		redis.saveToken(u.getId()+"",token);
		u.setToken(token);
		Date d = new Date();
		//判断年份月份和天数是否都一致
		if(!(u.getLastPlayTime().getDate()==d.getDate()&&u.getLastPlayTime().getMonth()==d.getMonth()&&u.getLastPlayTime().getYear()==d.getYear())) {//判断上一次游戏时间是否是上一天，如果超过一天则重置留机次数
			u.setStaySeatTime(3);
		}
		obj.put("user", u);
		service.updateuserIp(u, StringUtils.getIpFromRemoteIp(clientIp));
		writeAndFlush(context, obj.toString());
	}
	
	/**
	 * 客户端未传入数据时自动关闭
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			if(((IdleStateEvent) evt).state()==IdleState.READER_IDLE){
				ctx.close();
				logger.debug(clientIp+"连接超时，已断开链接");
			}
		}
	}
	
}
