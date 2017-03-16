package com.itouch8.wechat.api.bean.card;

import com.itouch8.wechat.api.bean.card.AbstractCard;
import com.itouch8.wechat.api.bean.card.Gift;

/**
 * 兑换券
 * 
 * @author Moyq5
 *
 */
public class GiftCard extends AbstractCard {

	private Gift gift;

	public GiftCard() {
		super("GIFT");
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

}
