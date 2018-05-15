package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.json.JSONObject;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:31:13
 * @description xxx
 */
public class TimeClientHandler extends  SimpleChannelInboundHandler<String>{
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	System.out.println("我收到了服务器端发送的数据"+System.currentTimeMillis());
        System.out.println("NOW is: " + msg.toString());
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
    	if(evt instanceof IdleStateEvent){
    		if(((IdleStateEvent) evt).state()==IdleState.WRITER_IDLE){
    			
    			JSONObject obj = new JSONObject();
    			obj.put("order", "heart");    			
    			ctx.writeAndFlush(obj.toString());
    			System.out.println("client-heart");
    		}
    	}else{
    		super.userEventTriggered(ctx, evt);
    	}
    }
	
}
