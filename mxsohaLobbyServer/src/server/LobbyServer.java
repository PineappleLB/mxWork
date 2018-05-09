package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import client.TimeClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redisUtil.RoomUtil;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.Configs;
import utils.IcssStringDecoder;
import utils.IcssStringEncoder;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:29:36
 * @description 服务器端
 */
public class LobbyServer {
	
	public static Timer timer = new Timer();//创建一个线程池
	
	public static List<String> list = new ArrayList<>();
	
	/**
	 * 创建服务端线程组并绑定端口
	 * @param port 端口号
	 * @throws Exception 
	 */
	public void bind(int port) throws Exception {
		//用于监测客户端连接的线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作的线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
        	//将两个线程组绑定到引导程序并添加参数
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024*8)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                	@Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    	
                    	//添加心跳检测5秒未读5秒未写，10秒未读写
                    	ch.pipeline().addLast(new IdleStateHandler(10, 10, 10));
                    	//添加一个结尾分割字节，用于解决粘包问题
                    	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*8, false, new ByteBuf[] {
                                Unpooled.wrappedBuffer(new byte[]{03}) }));
                    	//给服务器添加编码解码器
                    	ch.pipeline().addLast(new IcssStringDecoder());
                    	ch.pipeline().addLast(new IcssStringEncoder());
                    	//添加服务端业务处理类
                        ch.pipeline().addLast(new LobbyServerHandler());
                    }
                });
            //绑定端口, 同步等待成功;
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭 线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
    	RoomUtil room = new RoomUtil();
    	room.initLobby();
    	UserService server = new UserServiceImpl();//
    	ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间   每隔5分钟扫描一次用户信息
        service.scheduleAtFixedRate(() -> {
        	server.updateTimeOutUser();	//定时执行更新用户信息操作
        	server.updateOutBoundsGameTime();
        }
        , 60*5, 60*5, TimeUnit.SECONDS);
        //定时更新奖励通知队列
        ScheduledExecutorService service2 = Executors
        		.newSingleThreadScheduledExecutor();
        service2.scheduleAtFixedRate(()->{
        	Jedis jedis = new Jedis();
        	long len = jedis.llen("notices");
        	if(len > 500) {
        		list = jedis.lrange("notices", len-10, -1);
        		jedis.del("notices");
        	}
        	else if(len != list.size()) {
        		String str = jedis.lindex("notices", 0);
        		list.add(str);
        	}
        	jedis.close();
        }, 10, 10, TimeUnit.SECONDS);
        
        server.updateControllerValue(1);//先更新数据库的更新IP状态，再发送数据
        TimeClient client = new TimeClient();
        JSONObject obj = new JSONObject();
        obj.put("order", "replaceHost");
        client.connect(7896, Configs.configProps.getProperty("serverIP"), obj.toString());
        
        int port = Integer.parseInt(Configs.configProps.getProperty("lobbyPort"));
        new LobbyServer().bind(port);
    }
}
