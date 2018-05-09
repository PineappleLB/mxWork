package utils;

import java.security.MessageDigest;

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
	 * @throws Exception
	 */
	public static String encode(String str) throws Exception{
		MessageDigest md=MessageDigest.getInstance("MD5");
		byte[] b=str.getBytes();
		byte[] encode=md.digest(b);
		return new BASE64Encoder().encode(encode);
	}

}
