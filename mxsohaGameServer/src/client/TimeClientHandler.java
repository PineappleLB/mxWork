package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.json.JSONObject;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:31:13
 * @description xxx
 */
public class TimeClientHandler extends  SimpleChannelInboundHandler<String> {
	
	

    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	JSONObject arr = new JSONObject();
        
    	arr.put("order", "gc");
    	arr.put("id", "100001");
    	arr.put("room", "1");
    	arr.put("score", "20");
    	arr.put("seatId", 0);
    	ctx.writeAndFlush(arr.toString());
    	System.out.println("我向服务器发送了一条数据");
	}


	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	
    	cause.printStackTrace();
    	System.out.println("出错了");
        ctx.close();
    }
    
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
	}
	
}
