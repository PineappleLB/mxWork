package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import server.GameServerHandler;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:30:52
 * @description xxx
 */
public class CountClient {
	
	
	public CountClientHandler connect(int port, String host, GameServerHandler gameHandler) throws Exception{
        //配置客户端NIO 线程组
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        CountClientHandler handler = new CountClientHandler(gameHandler);
        try {
            client.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                        	ch.pipeline().addLast(new IdleStateHandler(5,5,10));
//                        	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*8, false, new ByteBuf[] {
//                                    Unpooled.wrappedBuffer(new byte[]{03}) }));
//                        	ch.pipeline().addLast(new ByteBuf);
//                        	ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(handler);
                        }
                    });
            //绑定端口, 异步连接操作
            ChannelFuture future = client.connect(host, port).sync();
//            JSONObject arr = new JSONObject();
            //等待客户端连接端口关闭
            future.channel().closeFuture().sync();
            return handler;
        } finally {
            //优雅关闭 线程组
            group.shutdownGracefully();
        }
    }

//    public static void main(String[] args) {
//    	
//        int port = 7888;
//        CountClient client = new CountClient();
//        try {
//            client.connect(port, "127.0.0.1");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	
}