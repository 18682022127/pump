/**
 * 
 */
package com.itouch8.wechat.api.bean.card.update;

import com.itouch8.wechat.api.bean.card.Gift;


/**
 * 微信卡券－卡券管理－更改卡券信息接口（兑换券）－请求参数 
 * @author Moyq5
 * @date 2016年7月30日
 */
public class UpdateGift extends AbstractUpdate {
	
	/**
	 * 兑换券
	 */
	private Gift gift;

	/**
	 * @return 兑换券
	 */
	public Gift getGift() {
		return gift;
	}

	/**
	 * @param gift 兑换券
	 */
	public void setGift(Gift gift) {
		this.gift = gift;
	}

}
