package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import sun.misc.BASE64Encoder;

/**
 * @author pineapple
 * @date 2017年12月21日 下午3:07:43
 * @description 字符串操作帮助类
 */
public class StringUtils {
	
	
	
	/**
	 * 密码加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 */
	public static String encode(String str) throws NoSuchAlgorithmException{
		MessageDigest md=MessageDigest.getInstance("MD5");
		byte[] b=str.getBytes();
		byte[] encode=md.digest(b);
		return new BASE64Encoder().encode(encode);
	}
	
	/**
	 * 将IP地址加端口号过滤，只获取ip部分
	 * @param remoteIp channel获得的IP地址
	 * @return 返回纯ipv4地址的字符串表示形式
	 */
	public static String getIpFromRemoteIp(String remoteIp){
		
		return remoteIp.substring(1, remoteIp.indexOf(":"));
		
	}
	
	public static String getToken4User() {
		String uid = UUID.randomUUID().toString().replace("-", "");
		return uid.substring(0, 6);
	}

}
