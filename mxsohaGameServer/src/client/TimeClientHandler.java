package client;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import utils.ByteArrayTemplate;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:31:13
 * @description xxx
 */
public class TimeClientHandler extends  ChannelInboundHandlerAdapter {
	



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	System.out.println(msg.getClass().getName());
    	ByteBuf buf = (ByteBuf)msg;
    	byte[] bytes = new byte[buf.readableBytes()];
    	buf.getBytes(0, bytes);
    	for (byte b : bytes) {
			System.out.print(Integer.toHexString(b) + " ");
		}
//    	System.out.println("我收到了服务器端发送的数据"+System.currentTimeMillis());
//        System.out.println("NOW is: " + msg.toString());
        ctx.writeAndFlush(new ByteArrayTemplate().getArray());
//        ctx.write();
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	
    	cause.printStackTrace();
    	System.out.println("出错了");
        ctx.close();
    }
    
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//    	if(evt instanceof IdleStateEvent){
//    		if(((IdleStateEvent) evt).state()==IdleState.WRITER_IDLE){
//    			
//    			JSONObject obj = new JSONObject();
//    			obj.put("order", "heart");    			
//    			ctx.writeAndFlush(obj.toString());
//    			System.out.println("client-heart");
//    		}
//    	}else{
    		super.userEventTriggered(ctx, evt);
//    	}
    }
	
}
