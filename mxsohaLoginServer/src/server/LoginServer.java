package server;

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
import utils.IcssStringDecoder;
import utils.IcssStringEncoder;

/**
 * @author pineapple
 * @date 2017年12月28日 上午9:27:01
 * @description 服务器端登录请求
 */
public class LoginServer {
	
	/**
	 * 绑定端口
	 * @param port 端口号
	 */
	private void bind(int port) {

		EventLoopGroup boss = new NioEventLoopGroup();
		
		EventLoopGroup worker = new NioEventLoopGroup();
		
		ServerBootstrap boot = new ServerBootstrap();
		
		try {
			
			boot.group(boss,worker)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					
					sc.pipeline().addLast(new IdleStateHandler(30, 0, 0));
					
					//添加一个结尾分割字节，用于解决粘包问题
		        	sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, new ByteBuf[] {
		                    Unpooled.wrappedBuffer(new byte[]{03}) }));
		        	
		        	//给服务器添加编码解码器
		        	sc.pipeline().addLast(new IcssStringDecoder());
		        	sc.pipeline().addLast(new IcssStringEncoder());
		        	//添加服务端业务处理类
		            sc.pipeline().addLast(new LoginServerHandler());
				}
			});
			
			//绑定端口, 同步等待成功;
            ChannelFuture future = boot.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			 //优雅关闭 线程组
            boss.shutdownGracefully();
            worker.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) {
		new LoginServer().bind(7896);
	}
	
	
}
