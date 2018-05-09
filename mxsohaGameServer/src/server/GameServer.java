package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import utils.Configs;
import utils.IcssStringDecoder;
import utils.IcssStringEncoder;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:29:36
 * @description 服务器端
 */
public class GameServer {
	
	public GameServer(){
		initGameCards();
	}
	
	/**
	 * 初始化卡牌信息
	 */
	private void initGameCards() {
		BufferedReader reader;
		String cardJson = "";
		try {
			//解析文件中的json字符串
			reader = new BufferedReader(new FileReader(new File(Configs.jsonCardFileDir)));
			while(reader.ready()){
				cardJson+=reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//将json字符串转换为json对象，并存储到缓存中
		JSONObject obj = JSONObject.fromObject(cardJson);
		JSONArray arr = obj.getJSONArray("cards");
		Jedis jedis = new Jedis();
		jedis.del("cards");
		for (int i = 0; i < arr.size(); i++) {
			jedis.lpush("cards", arr.get(i).toString());
		}
		//游戏初始化的时候监测奖池是否初始化
		List<String> strs = jedis.lrange("poolScore", 0, -1);
		if(strs==null || strs.size()!=90) {
			jedis.del("poolScore");
			for (int i = Configs.seatNum*Configs.roomNum - 1; i >= 0; i--) {
				obj = new JSONObject();
				obj.put("seatId", i);
				obj.put("4K", Configs.min_4K);
				obj.put("SF", Configs.min_SF);
				obj.put("RS", Configs.min_RS);
				obj.put("5K", Configs.min_5K);
				jedis.lpush("poolScore", obj.toString());
			}
			
		}
		jedis.close();
	}

	/**
	 * 消息数组
	 */
	public static byte[] messageBytes;
	
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
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());
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

	/**
	 * 内部类创建ChannelHandler对象初始化channel管道
	 * @author Pineapple
	 *
	 */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
        	
        	//添加心跳检测5秒未读5秒未写，10秒未读写
        	ch.pipeline().addLast(new IdleStateHandler(10, 10, 0));
        	//添加一个结尾分割字节，用于解决粘包问题
        	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, new ByteBuf[] {
                    Unpooled.wrappedBuffer(new byte[]{03}) }));
        	//给服务器添加编码解码器
        	ch.pipeline().addLast(new IcssStringDecoder());
        	ch.pipeline().addLast(new IcssStringEncoder());
        	//添加服务端业务处理类
            ch.pipeline().addLast(new GameServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 7888;
        new GameServer().bind(port);
        
    }
}
