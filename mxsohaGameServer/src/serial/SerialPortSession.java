package serial;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.TooManyListenersException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import server.GameServer;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.Configs;

/**
 * 串口的会话操作类
 * @author pineapple
 * 
 */
public class SerialPortSession{
	public static int send_ready = 1;//控制消息发送
	private static SerialPort port;//串口对象
	private static final Logger logger = Logger.getLogger(SerialPortSession.class);//日志文件
	private static String serialPortName;//串口名称
	private static ExecutorService singlePool = Executors.newSingleThreadExecutor();//单线程线程池
	private static UserService service = new UserServiceImpl();
	private static int sendTime;//接收到错误消息之后重发的次数
	//静态加载串口配置信息
	static {
		serialPortName = Configs.configProps.getProperty("serialPortName");
		port = SerialPortUtil.openPort(serialPortName);
		SerialPortUtil.setBountRote(port,115200);
		try {
			//添加串口检测
			port.addEventListener((event) -> {
				try {
					if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
						byte[] b = readByte(port.getInputStream());
						if(checkOrder(b)){
							GameServer.messageBytes = b;
							send_ready = 1;							
						}
						GameServer.messageBytes = b;
						send_ready = 1;
					}
				} catch (Exception e) {
					logger.error("服务器请求串口数据异常："+e.getMessage());
					e.printStackTrace();
				}

			});
			port.notifyOnDataAvailable(true);//串口等待
			port.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 单线程串口发送消息
	 * @param bytes
	 * @throws IOException
	 */
	public static void sendSerialMessage(byte[] bytes) throws IOException {
		singlePool.execute(new WriteThread(bytes));
	}


	/**
	 * 写数据的线程
	 * 
	 * @author Pineapple
	 *
	 */
	private static class WriteThread extends Thread {
		private byte[] bytes;

		public WriteThread(byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		public void run() {
			synchronized (this) {
				try {
					while (true) {
						if (send_ready == 1) {
							send_ready = 0;
							port.getOutputStream().write(bytes);
							return;
						} else {
							Thread.sleep(5);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 检查数据是否正确
	 * 
	 * @param b
	 *            检测的数据字节数组
	 * @return 是否是正确的数据
	 */
	public static boolean checkOrder(byte[] b) {

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
	
	
	/**
	 * 在串口中读取字节数组
	 * 
	 * @param in 串口的输入流对象
	 */
	private static byte[] readByte(InputStream in) {
		byte[] b = null;
		try {
			int i = in.available();
			b = new byte[i];
			in.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 字节数组转换成16进制字符串
	 * @param byteArray
	 * @return
	 */
	public static String toHexString(byte[] byteArray) {
		if (byteArray == null || byteArray.length < 1)
		 throw new IllegalArgumentException("this byteArray must not be null or empty");
		final StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) < 0x10)// 0~F前面补零
				hexString.append("0");
			hexString.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return hexString.toString().toLowerCase();
	}


	/**
	 *  从串口获取需要的牌的类型
	 * @param req 请求发送到串口的数据
	 * @return
	 * @throws SQLException 
	 */
	public static int[] getCardMessage(byte[] req, int id, int roomid, int seatId,int score) throws SQLException {
		byte[] bytes = null;
		if(!checkOrder(req)) {
			throw new IllegalArgumentException("发送数据错误，数据格式不正确！");
		}
		try {
			logger.info("[serialPort]: id："+id+" roomid:"+roomid+" seatId:"+seatId+" sendMessage:"+Arrays.toString(req));
			sendSerialMessage(req);
			Thread.sleep(1);//发送结束之后先休眠一毫秒
			while(true){
				if(SerialPortSession.send_ready==1){
					bytes = GameServer.messageBytes;
					break;
				}else{
					Thread.sleep(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!checkOrder(bytes)) {
			if(sendTime==0) {
				sendTime++;
				return getCardMessage(req,id,roomid,seatId,score);
			}else {
				sendTime=0;
				throw new IllegalArgumentException("串口数据错误！");
			}
		}
		int checkId = bytesToInt(GameServer.messageBytes,6);
//		service.updateCheckId(checkId,id);
		logger.info("[serialPort]: id："+id+" roomid:"+roomid+" seatId:"+seatId+" backMessage:"+Arrays.toString(bytes));
//		service.updateSeatListNum(GameServer.messageBytes[8], roomid, seatId);
		
		service.updateUserAndSeatInfo(checkId,id,GameServer.messageBytes[8], roomid, seatId,score,GameServer.messageBytes[9]);
		
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
	public static int bytesToInt(byte[] src,int index) {
		int value = 0;
	    if((src[index]>>7) == -1) {
//	    if(src[index]<0) {
	    	value+=((256+src[index])<<8);
//	    	System.out.println("-"+value);
	    }else {
	    	value+=(src[index]<<8);
//	    	System.out.println(value);
	    }
	    index++;
	    if((src[index]>>7) == -1) {
//	    if(src[index]<0) {
	    	value+=256+src[index];
	    }else {
	    	value+=src[index];
	    }
	    System.err.println("参数为："+Arrays.toString(src)+"转换之后的值为："+value+",16进制为："+Integer.toHexString(value));
	    return value;  
	} 
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
