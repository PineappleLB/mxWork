package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.json.JSONObject;
import server.GameServerHandler;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:31:13
 * @description xxx
 */
public class CountClientHandler extends  ChannelInboundHandlerAdapter{
	
	public CountClientHandler() {}
	public CountClientHandler(GameServerHandler handler) {
		this.gameHandler = handler;
	}
	
	private GameServerHandler gameHandler;
	
	private ChannelHandlerContext context;
	
	
	
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.context = ctx;
	}

    
    
    
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	ByteBuf buf = (ByteBuf)msg;
    	if(!buf.hasArray()){   
    	    int len = buf.readableBytes();   
    	    byte[] arr = new byte[len];   
    	    buf.getBytes(0, arr);
    	}  
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
