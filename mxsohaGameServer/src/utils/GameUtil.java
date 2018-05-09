package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameUtil {

	/**
	 * 游戏抽取的牌
	 */
	private List<Integer> cardsList;

	/**
	 * 当前玩家手中的牌
	 */
	private List<Integer> nowCardList;

	/**
	 * 玩家手牌的类型
	 */
	private int type;


	public GameUtil() {
		cardsList = new ArrayList<>();
		nowCardList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int value = ((int) (Math.random() * 54));
			cardsList.add(value);
			if (i < 5)
				nowCardList.add(value);
		}
	}

	/**
	 * 创建游戏对局的时候就传入类型以生成对应的牌型
	 * 
	 * @param type
	 */
	public GameUtil(int type) {
		Integer [] cards = null;//暂时用于保存第一次生成数组的对象
		Integer [][]tempCards = null;//保存返回的二维数组的对象
		switch (type) {//根据type生成不同的牌型
		case 0:
			if(Math.random()*5 < 1){
				cards = CardUtil.create_SAME_NO_TYPE_Cards();
			}else{
				cards = CardUtil.createNO_TYPE_Cards();
			}
			tempCards = CardUtil.distributionCards(cards,0);
			break;
		case 1:
			cards = CardUtil.createTYPE_DOUBLE_Cards();
			tempCards = CardUtil.distributionCards(cards,2);
			break;
		case 2:
			cards = CardUtil.createTYPE_TOW_DOUBLE_Cards();
			tempCards = CardUtil.distributionCards(cards,4);
			break;
		case 3:
			cards = CardUtil.createTYPE_THIRD_Cards();
			tempCards = CardUtil.distributionCards(cards,3);
			break;
		case 4:
			cards = CardUtil.createTYPE_STR_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		case 6:
			cards = CardUtil.createTYPE_T_D_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		case 5:
			cards = CardUtil.createTYPE_SAME_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		case 7:
			cards = CardUtil.createTYPE_FOUR_Cards();
			tempCards = CardUtil.distributionCards(cards,4);
			break;
		case 8:
			cards = CardUtil.createTYPE_SAME_STR_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		case 9:
			cards = CardUtil.createTYPE_SAME_STR_BIG_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		case 10:
			cards = CardUtil.createTYPE_FIVE_Cards();
			tempCards = CardUtil.distributionCards(cards,5);
			break;
		}
		
		cardsList = Arrays.asList(tempCards[1]);
		nowCardList = Arrays.asList(tempCards[0]);
	}

	public List<Integer> getCardsList() {
		return cardsList;
	}

	public void setCardsList(List<Integer> cardsList) {
		this.cardsList = cardsList;
	}

	public List<Integer> getNowCardList() {
		return nowCardList;
	}

	public void setNowCardList(List<Integer> list) {
		this.nowCardList = list;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
