package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import net.sf.json.JSONObject;
import utils.IcssStringDecoder;
import utils.IcssStringEncoder;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:30:52
 * @description xxx
 */
public class TimeClient {
	
	
	public void connect(int port, String host) throws Exception{
        //配置客户端NIO 线程组
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        try {
            client.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        	
                        	ch.pipeline().addLast(new IdleStateHandler(5,5,10));
                        	
                        	
                        	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*8, false, new ByteBuf[] {
                                    Unpooled.wrappedBuffer(new byte[]{03}) }));
                        	ch.pipeline().addLast(new IcssStringDecoder());
                        	ch.pipeline().addLast(new IcssStringEncoder());
                        	
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            
            //绑定端口, 异步连接操作
            ChannelFuture future = client.connect(host, port).sync();
            JSONObject arr = new JSONObject();
            
        	arr.put("order", "gc");
        	arr.put("id", "100001");
        	arr.put("room", "1");
        	arr.put("score", "20");
//        	arr.put("score", "30");
//        	arr.put("room", "1");
//        	arr.put("card1", "55");
//        	arr.put("card2", "77");
        	System.out.println("开始写数据："+System.currentTimeMillis());
            future.channel().writeAndFlush(arr.toString());
            
            //等待客户端连接端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭 线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
    	
        int port = 7888;
        TimeClient client = new TimeClient();
        try {
            client.connect(port, "127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
