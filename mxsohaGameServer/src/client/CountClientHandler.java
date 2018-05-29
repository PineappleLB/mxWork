package client;

import java.util.Arrays;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import service.UserService;
import service.impl.UserServiceImpl;

/**
 * @author pineapple
 * @date 2017年12月26日 下午1:31:13
 * @description xxx
 */
public class CountClientHandler extends  ChannelInboundHandlerAdapter{
	
	private static final Logger logger = Logger.getLogger(CountClientHandler.class);//日志文件
	
	public CountClientHandler() {}
	
	private int sendTime;//接收到错误消息之后重发的次数
	
	private UserService service = new UserServiceImpl();
	
	private ChannelHandlerContext context;
	
	private boolean sendReady = false;
	
	private byte[] messageBytes;
	
	
	public int[] sendMessage(byte[] req, int id, int roomId, Integer seatId, int score) {
		ByteBuf buf = Unpooled.buffer(req.length);
		buf.writeBytes(req);
		if(!checkOrder(req)) {
			throw new IllegalArgumentException("发送数据错误，数据格式不正确！");
		}
		sendReady =false;
		byte[] bytes = null;
		try {
			logger.info("[serialPort]: id："+id+" roomid:"+roomId+" seatId:"+seatId+" sendMessage:"+Arrays.toString(req));
			context.writeAndFlush(buf);
			Thread.sleep(1);//发送结束之后先休眠一毫秒
			for (int i = 0; i < 100; i++) {
				if(sendReady){
					bytes = messageBytes;
					break;
				}else{
					Thread.sleep(1);
				}
				if(i == 99) {
					throw new IllegalArgumentException("等待超时。。。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
		if(!checkOrder(bytes)) {
			if(sendTime==0) {
				sendTime++;
				return sendMessage(req,id,roomId,seatId,score);
			}else {
				sendTime=0;
				throw new IllegalArgumentException("串口数据错误！");
			}
		}
		int checkId = bytesToInt(messageBytes,6);
		logger.info("[serialPort]: id："+id+" roomid:"+roomId+" seatId:"+seatId+" backMessage:"+Arrays.toString(bytes));
		new Thread(()->{
			service.updateUserAndSeatInfo(checkId,id,messageBytes[8], roomId, seatId,score,messageBytes[9]);
		}) .start();
		
		int message[] = new int[4];
		for (int i = 0; i < message.length; i++) {
			message[i] = bytes[i+2];
		}
		if(message[0]<0) {
			message[0] += 256; 
		}
		if(message[0] == 251) {
			message[0] = 500;
		}
		return message;
	}
	
	/**  
	    * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用 
	    * @param src  
	    *            byte数组  
	    * @param offset  
	    *            从数组的第offset位开始  
	    * @return int数值  
	    */    
	public int bytesToInt(byte[] src,int index) {
		int value = 0;
	    if((src[index]>>7) == -1) {
	    	value+=((256+src[index])<<8);
	    }else {
	    	value+=(src[index]<<8);
	    }
	    index++;
	    if((src[index]>>7) == -1) {
	    	value+=256+src[index];
	    }else {
	    	value+=src[index];
	    }
	    System.err.println("参数为："+Arrays.toString(src)+"转换之后的值为："+value+",16进制为："+Integer.toHexString(value));
	    return value;  
	} 
	
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.context = ctx;
	}
    
    /**
	 * 检查数据是否正确
	 * 
	 * @param b
	 *            检测的数据字节数组
	 * @return 是否是正确的数据
	 */
	public boolean checkOrder(byte[] b) {

		if (!(b.length > 0)) {
			return false;
		}
		else if (b[0] == -93 && b.length-3 == b[1] && b[b.length - 1] == -103) {
			byte bs = b[2];
			for (int i = 3; i < b.length-2; i++) {
				bs ^= b[i];
			}
			if(bs == b[b.length-2]){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
    
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	ByteBuf buf = (ByteBuf)msg;
    	if(!buf.hasArray()){
    	    int len = buf.readableBytes();   
    	    messageBytes = new byte[len];
    	    buf.getBytes(0, messageBytes);
    	}
    	sendReady = true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	
    	cause.printStackTrace();
    	System.out.println("出错了");
        ctx.close();
    }
    
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }
	
}
