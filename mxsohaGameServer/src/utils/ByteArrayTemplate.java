package utils;

public class ByteArrayTemplate {
	
	private byte []message = new byte[]{(byte) 0xA3, 0x23, 0x00,0x01, 0x00,0x00,0x00,0x5E, 0x00,0x00,0x17,0x00, 0x00,0x00,0x02,0x49, 0x00,(byte) 0xC8, 0x00,
			0x00,0x17,0x00, 0x00,0x00,0x02,0x49, 0x62, 0x62, 0x00,0x32, 0x00,0x32, 0x08,0x09,0x0A,0x10, (byte) 0x8C, (byte) 0x99};
	
	/**
	 * 设置用户校验id
	 * @param b
	 */
	public void setUserCheckId(int i){
		byte[] bytes = parseByteArray(i);
		message[2]=bytes[2];
		message[3]=bytes[3];
	}

	
	/**
	 * 设置用户金币数量
	 * @param b
	 */
	public void setDoubleScore(int i){
		byte[] b = parseByteArray(i);
		message[4]=b[0];
		message[5]=b[1];
		message[6]=b[2];
		message[7]=b[3];
	}
	
	/**
	 * 设置用户总玩分，消耗的积分
	 * @param b
	 */
	public void setUserAllScore(int i){
		byte[] b = parseByteArray(i);
		message[8]=b[0];
		message[9]=b[1];
		message[10]=b[2];
		message[11]=b[3];
	}
	
	/**
	 * 设置用户总得分
	 * @param b
	 */
	public void setUserGotScore(int i){
		byte[] b = parseByteArray(i);
		message[12]=b[0];
		message[13]=b[1];
		message[14]=b[2];
		message[15]=b[3];
	}
	
	/**
	 * 设置用户本次押分
	 * @param b
	 */
	public void setUserScore(int i){
		byte[] b = parseByteArray(i);
		message[16]=b[2];
		message[17]=b[3];
	}
	
	/**
	 * 设置座位的总积分
	 * @param b
	 */
	public void setSeatAllScore(int i){
		byte[] b = parseByteArray(i);
		message[18]=b[0];
		message[19]=b[1];
		message[20]=b[2];
		message[21]=b[3];
	}
	
	/**
	 * 设置座位的总得分
	 * @param b
	 */
	public void setSeatGotScore(int i){
		byte[] b = parseByteArray(i);
		message[22]=b[0];
		message[23]=b[1];
		message[24]=b[2];
		message[25]=b[3];
	}
	
	/**
	 * 设置玩家难度
	 * @param b
	 */
	public void setUserHard(byte b){
		message[26]=b;
	}
	
	/**
	 * 设置玩家难度
	 * @param b
	 */
	public void setSeatHard(byte b){
		message[27]=b;
	}
	
	/**
	 * 设置玩家游戏局数
	 * @param b
	 */
	public void setUserPlayTime(int i){
		byte[] b = parseByteArray(i);
		message[28]=b[2];
		message[29]=b[3];
	}
	
	/**
	 * 设置玩家难度
	 * @param b
	 */
	public void setSeatPlayTime(byte b){
		message[30]=b;
	}
	
	public void setSeatListNum(byte b) {
		message[31]=b;
	}
	
	/**
	 * 设置随机数
	 * @param b
	 */
	public void setRandom(int i){
		byte[] b = parseByteArray(i);
		message[32]=b[2];
		message[33]=b[3];
	}
	
	/**
	 * 设置 重置位
	 * @param b
	 */
	public void setResetNum(byte b){
		message[34]=b;
	}
	
	/**
	 * 设置 控制位
	 * @param b
	 */
	public void setControlNum(byte b){
		message[35]=b;
	}
	
	
	public byte[] getArray(){
		byte b = message[2];
		for (int i = 3; i < 36; i++) {
			b ^= message[i];
		}
		message[36]=b;
		return message;
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
	 * 将int转换为byte数组
	 * 下标0为高位，3为最低位
	 * @param i
	 * @return
	 */
	public static byte[] parseByteArray(int i) {
		byte[] bs = new byte[4];
		bs[0] = (byte) ((i >> 24) & 0x000000FF);
		bs[1] = (byte) ((i >> 16) & 0x000000FF);
		bs[2] = (byte) ((i >> 8) & 0x000000FF);
		bs[3] = (byte) (i & 0x000000FF);
		return bs;
	}
	
//	public static void main(String[] args) {
////		int i = 4683;
////		byte[] bs = parseByteArray(i);
//		byte[] bs = {-27, 0};
//		System.out.println(Arrays.toString(bs));
//		System.out.println(bytesToInt(bs, 0));
////		int index = 0;
////		for (int i = 0; i < 65535; i++) {
////			index = i;
////			byte[] bs = parseByteArray(i);
////			
////			index = bytesToInt(bs, 2);
////			if(index != i) {
////				System.out.println("i="+i+"  arr="+Arrays.toString(bs)+"  index="+index);
////			}
////		}
////		System.out.println("success");
//		
//	
//		
//		
//		
//	}
	
//	public static int bytesToInt(byte[] src,int index) {
//	    int value = 0;
//	    if((src[index]>>7) == -1) {
////	    if(src[index]<0) {
//	    	value+=((256+src[index])<<8);
////	    	System.out.println("-"+value);
//	    }else {
//	    	value+=(src[index]<<8);
////	    	System.out.println(value);
//	    }
//	    index++;
//	    if((src[index]>>7) == -1) {
////	    if(src[index]<0) {
//	    	value+=256+src[index];
//	    }else {
//	    	value+=src[index];
//	    }
//	    return value; 
//	} 
}
