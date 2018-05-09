package serial;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * 串口通信的帮助类
 * @author pineapple
 *
 */
public class SerialPortUtil {
	
	private static CommPortIdentifier portIdentifier;
	private static SerialPort port;
	public static List<SerialPort> ports;
	
	/**
	 * 关闭串口
	 */
	public static void closePort(SerialPort port) {
		port.close();
		port = null;
	}
	
	
	/**
	 * 设置波特率
	 */
	public static void setBountRote(SerialPort port,int baud){
		try {
			port.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭所有串口
	 */
	public static void closePort(List<SerialPort> ports) {
		for (SerialPort serialPort : ports) {
			serialPort.close();
			serialPort = null;
		}
	}
	
	/**
	 * 根据串口名称打开串口
	 */
	public static SerialPort openPort(String portName){
		try {
			portIdentifier=CommPortIdentifier.getPortIdentifier(portName);
			port = (SerialPort)portIdentifier.open(portName, 1000);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
		
		return port;
	}
	
	
	/**
	 * 查找所有串口
	 * @return
	 */
	public static List<String> getPortNames(){
		List<SerialPort> ports = getPorts();
		List<String> names = new ArrayList<>();
		
		for (SerialPort port : ports) {
			names.add(port.getName());
		}
		return names;
		
	}
	
	/**
	 * 
	 * @return 返回所有串口的集合
	 */
	public static List<SerialPort> getPorts(){
		
		List<SerialPort> ports = new ArrayList<>();
		
		//获取当前电脑所有可用的设备
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		while(portList.hasMoreElements()){
			portIdentifier = portList.nextElement();
			CommPort port = null;
			try {
				port = portIdentifier.open(portIdentifier.getName(),2000);
				
			} catch (PortInUseException e) {
				e.printStackTrace();
			}
			//判断是否是串口
			if(port instanceof SerialPort){

				ports.add((SerialPort)port);
			}
		}
		return ports;
	}


	
	
}
