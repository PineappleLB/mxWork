package service;

import java.util.List;

import net.sf.json.JSONObject;

public interface RedisService {
	
	int saveControllerValue(int val);

	/**
	 * 添加管理员申请记录
	 * @param promId
	 * @param money
	 * @param promName
	 */
	void addPromtersOrderRecords(int promId, int money, String promName);

	/**
	 * 执行推广员申请
	 * @param messageId
	 * @param sure
	 * @return
	 */
	JSONObject excutePromoterOrder(String messageId, int sure);

	/**
	 * 查询所有的推广员查询记录
	 * @return
	 */
	List<String> selectPromoterOrder();

	/**
	 * 查询消息记录条数
	 * @return
	 */
	int selectInfoSize();
	
}
