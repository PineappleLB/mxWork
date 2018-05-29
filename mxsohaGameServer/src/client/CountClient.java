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
import server.GameServerHandler;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:30:52
 * @description xxx
 */
public class CountClient {
	
	public void connect(int port, String host, GameServerHandler gameHandler) throws Exception{
        //配置客户端NIO 线程组
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        CountClientHandler handler = new CountClientHandler();
        ChannelFuture future = null;
        try {
            client.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, new ByteBuf[] {
                                    Unpooled.wrappedBuffer(new byte[]{(byte) 0x99}) }));
                            ch.pipeline().addLast(handler);
                        }
                    });
            gameHandler.handler = handler;
            //绑定端口, 异步连接操作
            future = client.connect(host, port).sync();
        } finally {
        	//等待客户端连接端口关闭
        	future.channel().closeFuture().sync();
            //优雅关闭 线程组
            group.shutdownGracefully();
        }
    }
	
}
