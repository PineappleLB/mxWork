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
            
        	arr.put("order", "login");
        	arr.put("name", "pineapple");
        	arr.put("pass", "123456");
//        	arr.put("host", "118.112.108.229");
        	//[1-9][0-9]{0,2}(.[0-9]{0,3}){3}
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
        int port = 7896;
        TimeClient client = new TimeClient();
        try {
        	//111.230.238.141云服务器IP
            client.connect(port, "pinea.club");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
