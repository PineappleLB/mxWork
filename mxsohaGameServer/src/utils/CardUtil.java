package utils;

import java.util.Random;

import net.sf.json.JSONArray;
import redis.RedisUtil;
import redis.clients.jedis.Jedis;

public class CardUtil {
	
	 //随机数
	 private static Random random = new Random();
	 //花色
	 private static int floor;
	 //数值
	 private static int num;
	 
	 /**
	  * redis处理对象
	  */
	 private static RedisTemplate template = new RedisTemplate(RedisUtil.getPool());
	 
	 
	/**
	 * 分数集合
	 */
	public static final int[][] scores = { 
			{ 0, 1, 2, 3, 6, 8, 12, 60, 140, 250, 500 },
			{ 0, 1, 2, 3, 5, 7, 10, 50, 120, 200, 400 },
			{ 0, 1, 2, 3, 4, 6, 8, 40, 100, 150, 300 } 
	};
	
	/**
	 * 比倍的倍率
	 */
	public static final int[] doubleScore = { 1, 2, 5, 15, 50, 200 };

	/**
	 * 所有牌组合
	 */
	public static final int[][] cards = { 
			{  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12},
			{ 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25},
			{ 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38},
			{ 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56}
	};
	
	
	public static final int[] allCards = { 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 
			11, 12, 52, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 53, 26, 27, 
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 54, 39, 40, 41, 42, 43, 44, 45,
			46, 47, 48, 49, 50, 51, 55, 56 };
	
	/**
	 * 将数组乱序
	 * @param arr 需要打乱顺序的数组
	 * @return
	 */
	public static Integer[] randomArray(Integer arr[]){
		if(arr==null || arr.length == 0){
			throw new IllegalArgumentException("不可遍历的数组");
		}
		for(int i = 0; i < arr.length; i++){
			 int p = random.nextInt(arr.length);
			int tmp = arr[i];
			arr[i] = arr[p];
			arr[p] = tmp;
		}
		return arr;
	}

	/**
	 * 将扑克牌分配到两个数组中，第一个数组为发牌数组，第二个数组为所有牌数组
	 * @param cards 需要分配的牌
	 * @param cardNum 完整牌需要的张数，用于随机打乱
	 * @return
	 */
	public static Integer[][] distributionCards(Integer cards[],int cardNum){
		if(cards.length!=10){
			throw new IllegalArgumentException("不可分配的数组，长度必须为10");
		}
		Integer []temp = new Integer[5];//创建一个长度为5的数组存储要发的牌
		Integer []allTemp = new Integer[10];//创建一个长度为10的数组存储所有的牌
		int max = 0;//随机一个数量用来判断拆分牌类型所必须的牌
		if(cardNum<=0){//判断牌类型是否不需要做随机牌
			cards = randomArray(cards);//直接打乱顺序
			for (int i = 0; i < temp.length; i++) {
				//[6, 42, 38, 8, 7, 37, 41, 43, 26, 9]
				temp[i] = cards[i];
			}
			return new Integer[][]{temp,cards};
		}
		else if(cardNum == 5) {
			max = random.nextInt(cardNum-2)+3;
			for (int i = 0; i < max; i++) {//将拆分前面的牌放入第一个数组
				temp[i] = cards[i];
			}
			for (int i = 0; i < temp.length-max; i++) {//将数组差的牌补足，反向补牌
				temp[max+i] = cards[cards.length-1-i];
			}
		}
		else if(cardNum == 4) {
			max = random.nextInt(cardNum-2)+3;
			for (int i = 0; i < max; i++) {//将拆分前面的牌放入第一个数组
				temp[i] = cards[i];
			}
			for (int i = 0; i < temp.length-max; i++) {//将数组差的牌补足，反向补牌
				temp[max+i] = cards[cards.length-1-i];
			}
		}
		else{
			max = random.nextInt(cardNum)+1;
			for (int i = 0; i < max; i++) {//将拆分前面的牌放入第一个数组
				temp[i] = cards[i];
			}
			for (int i = 0; i < temp.length-max; i++) {//将数组差的牌补足，反向补牌
				temp[max+i] = cards[cards.length-1-i];
			}
		}
		
		temp = randomArray(temp);
		
		//将新牌放入新的数组
		System.arraycopy(temp, 0, allTemp, 0, temp.length);
		//将剩下的几张牌放入数组后面
		System.arraycopy(cards, max, allTemp, temp.length, temp.length);
		return new Integer[][]{temp,allTemp};
	}

	
	/**
	 * 生成相同花色的牌没有牌型的牌
	 * @return
	 */
	public static Integer[] create_SAME_NO_TYPE_Cards(){
		Integer arr[] = getRandomNoTypeCardFromRedis();
		floor = random.nextInt(cards.length);
		for (int i = 0; i < arr.length; i++) {
			if(floor>=cards.length){
				floor=0;
			}
			arr[i] = cards[floor][arr[i]];
			if(i%4==0&&i<9){
				floor++;
			}
		}
		return arr;
	}
	

	/**
	 * 随机生成没有任何牌型的牌
	 * @return
	 */
	public static Integer[] createNO_TYPE_Cards(){
		Integer arr[] = getRandomNoTypeCardFromRedis();
		floor = random.nextInt(cards.length);
		for (int i = 0; i < arr.length; i++) {
			if(floor>=cards.length){
				floor=0;
			}
			arr[i] = cards[floor++][arr[i]];
		}
		return arr;
	}
	
	/**
	 * 生成随机一队牌
	 * @return
	 */
	public static Integer[] createTYPE_DOUBLE_Cards(){
		Integer arr[] = getRandomNoTypeCardFromRedis();
		arr = randomArray(arr);
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] > 8 || arr[i] == 0){
				int temp = arr[i];
				arr[i] = arr[0];
				arr[0] = temp;
				break;
			}
		}
		arr[1] = arr[0];
		floor = random.nextInt(cards.length);
		for (int i = 0; i < arr.length; i++) {
			if(floor>=cards.length){
				floor=0;
			}
			arr[i] = cards[floor++][arr[i]];
		}
		//随机添加王
		if(random.nextInt()%10==0) {
			arr[1] = random.nextInt(5)+52;
		}
		return arr;
	}
	
	/**
	 * 生成随机两队牌
	 * @return
	 */
	public static Integer[] createTYPE_TOW_DOUBLE_Cards(){
		Integer arr[] = getRandomNoTypeCardFromRedis();
		arr = randomArray(arr);
		for (int i = 0,j = 0; i < arr.length; i++) {
			arr[i+1] = arr[i++];
			j++;
			if(j==2){
				break;
			}
		}
		floor = random.nextInt(cards.length);
		for (int i = 0; i < arr.length; i++) {
			if(floor>=cards.length){
				floor=0;
			}
			arr[i] = cards[floor++][arr[i]];
		}
		//随机添加王牌
		if(random.nextInt()%10==0) {
			arr[random.nextInt(4)] = random.nextInt(5)+52;
		}
		return arr;
	}
	
	/**
	 * 随机生成不同花色的同样的牌
	 * @param i 传入的牌
	 * @return 不同花色的牌
	 */
	private static int getDoubleNum(int i) {
		floor = i / 13;
		int floor = CardUtil.floor;
		floor = random.nextInt(4);
		if(floor == CardUtil.floor) {
			return getDoubleNum(i);
		}
		return cards[floor][i%13];
	}
	
	
	/**
	 * 生成随机三张牌
	 * @return
	 */
	public static Integer[] createTYPE_THIRD_Cards(){
		Integer arr[] = getRandomNoTypeCardFromRedis();
		arr = randomArray(arr);
		arr[1] = arr[0];
		arr[2] = arr[0];
		floor = random.nextInt(cards.length);
		for (int i = 0; i < arr.length; i++) {
			if(floor>=cards.length){
				floor=0;
			}
			arr[i] = cards[floor++][arr[i]];
		}
		//随机添加王
		if(random.nextInt()%10==0) {
			arr[random.nextInt(3)] = random.nextInt(5)+52;
		}
		
		return arr;
	}
	

	/**
	 * TYPE_STR顺子
	 * @return
	 */
	public static Integer[] createTYPE_STR_Cards(){
		
		Integer arr[] = new Integer[10];
		num = random.nextInt(13);
		int time = 3;//记录次数，第三次空一位
		int i;
		if (num > 10) {
			for (i = 0, floor = 0; i < arr.length; num--, i++, floor++) {
				if( i > 4 ){
					if (time++ == 3) {
						time = 0;
						floor--;
						i--;
						continue;
					}
				}
				if (floor == cards.length) {
					floor = 0;
				}
				if (num <= 0) {
					num = cards[0].length-1;
				}
				arr[i] = cards[floor][num];
			}
		} else {
			for (i = 0, floor = 0; i < arr.length; num++, i++, floor++) {
				if( i > 4 ){
					if (time++ == 3) {
						time = 0;
						floor--;
						i--;
						continue;
					}
				}
				if (floor == cards.length) {
					floor = 0;
				}
				if (num >= cards[0].length) {
					num = 0;
				}
				arr[i] = cards[floor][num];
			}
		}
		return arr;
	}
	
	/**
	 * 生成随机葫芦牌
	 * @return
	 */
	public static Integer[] createTYPE_T_D_Cards(){
		Integer arr[] = createNO_TYPE_Cards();
		arr = randomArray(arr);
		num = arr[0]%13;
		floor = arr[0]/13;
		for(int i = 0; i < 2; i++){
			floor ++;
			if(floor>=4){
				floor = 0;
			}
			arr[i+1] = cards[floor][num];
		}
		//随机生成王
		if(random.nextInt()%10==0) {
			arr[random.nextInt(3)] = random.nextInt(5)+52;
		}
		arr[4] = getDoubleNum(arr[3]);
		return arr;
	}

	/**
	 * 生成随机同花牌
	 * @return
	 */
	public static Integer[] createTYPE_SAME_Cards(){
		Integer arr[] = createNO_TYPE_Cards();
		arr = randomArray(arr);
		floor = arr[0]/13;
		for(int i = 1;i <5;i++){
			arr[i] = cards[floor][arr[i]%13];
		}
		return arr;
	}
	
	
	/**
	 * 生成随机四张牌
	 * @return
	 */
	public static Integer[] createTYPE_FOUR_Cards(){
		Integer arr[] = createNO_TYPE_Cards();
		arr = randomArray(arr);
		num = arr[0]%13;
		floor = arr[0]/13;
		for(int i = 1;i < 4;i++){
			floor++;
			if(floor==cards.length){
				floor=0;
			}
			arr[i] = cards[floor][num];
		}
		
		if(random.nextInt(5)>2){
			int size = random.nextInt(3);
			int king = -1;
			for (int i = 0; i < size; i++) {
				king = randomNum(king, 5);
				arr[random.nextInt(4)] = 52+king;
			}
		}
		return arr;
	}	

	public static Integer[] createTYPE_SAME_STR_Cards(){
		Integer arr[] = new Integer[10];
		num = random.nextInt(13);
		int time = 3;//记录次数，第三次空一位
		int i;
		if (num >= 10) {
			for (i = 0, floor = 0; i < arr.length; num--, i++) {
				if( i > 4 ){
					floor++;
					if (time++ == 3) {
						time = 0;
						floor--;
						i--;
						continue;
					}
				}
				if (floor >= cards.length) {
					floor = 0;
				}
				if (num <= 0) {
					num = cards[0].length-1;
				}
				arr[i] = cards[floor][num];
			}
		} else {
			for (i = 0, floor = 0; i < arr.length; num++, i++) {
				if( i > 4 ){
					floor++;
					if (time++ == 3) {
						time = 0;
						floor--;
						i--;
						continue;
					}
				}
				if (floor >= cards.length) {
					floor = 0;
				}
				if (num >= cards[0].length) {
					num = 0;
				}
				arr[i] = cards[floor][num];
			}
		}
		return arr;
	}
	/**
	 * 随机生成一个数字，不能与参数execept相同
	 * @param execept 排除的参数
	 * @param bound 取值范围
	 * @return
	 */
	private static int randomNum(int execept, int bound) {
		int result = random.nextInt(bound);
		if(result == execept) {
			return randomNum(execept,bound);
		}
		return result;
	}
	
	/**
	 * 生成随机同花大顺牌
	 * @return
	 */
	public static Integer[] createTYPE_SAME_STR_BIG_Cards(){
		Integer arr[] = new Integer[10];
		num = 9;
		int time = 3;//记录次数，第三次空一位
		floor = random.nextInt(4);
		for (int i = 0; i < arr.length; i++,num++) {
			if(i==5){
				floor = randomNum(floor, 4);
			}
			if(num>=cards[0].length){
				num = 0;
			}
			if(i>4){
				floor++;
				if (time++ == 3) {
					time = 0;
					floor--;
					i--;
					continue;
				}
			}
			if(floor == cards.length){
				floor = 0;
			}
			arr[i] = cards[floor][num];
		}
		
		return arr;
	}
	
	/**
	 * 生成随机五张牌
	 * @return
	 */
	public static Integer[] createTYPE_FIVE_Cards(){
		Integer arr[] = createNO_TYPE_Cards();
		arr = randomArray(arr);
		floor = arr[0]/13;
		num = arr[0]%13;
		arr[1] = cards[3][random.nextInt(5)+13];
		for (int i = 2; i < 5; i++) {
			++floor;
			if(floor >= cards.length){
				floor = 0;
			}
			arr[i] = cards[floor][num];
		}
		
		int boss = random.nextInt(4);
		num = random.nextInt(5)+13;
		for (int i = 1,j=0; j<boss ; i = random.nextInt(5),j++) {
			num++;
			if(num >= cards[3].length){
				num = 13;
			}
			arr[i] = cards[3][num];
		}
		return arr;
	}

	/**
	 * 根据牌倍率判断牌类型
	 * @param i
	 * @return
	 */
	public static int selectCardTypeByPower(int i) {
		for (int j = 0; j < scores[0].length; j++) {
			if(i==scores[0][j]){
				return j;
			}
		}
		return 0;
	}
	
	/**
	 * 随机从数据库中取出一个数组
	 * @return
	 */
	private static Integer[] getRandomNoTypeCardFromRedis(){
		Integer[] cards = new Integer[10];
		String jsonarr = template.execute(new RedisCallback<String>() {
			@Override
			public String handle(Jedis jedis) {
				int index = random.nextInt(50);
				return jedis.lindex("cards", index);
			}
		});
		JSONArray arr = JSONArray.fromObject(jsonarr);
		cards = (Integer[]) arr.toArray(cards);
		return cards;
	}
	
}
