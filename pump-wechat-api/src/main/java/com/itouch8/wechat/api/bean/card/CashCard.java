package com.itouch8.wechat.api.bean.card;

import com.itouch8.wechat.api.bean.card.AbstractCard;
import com.itouch8.wechat.api.bean.card.Cash;

/**
 * 代金券
 * 
 * @author Moyq5
 *
 */
public class CashCard extends AbstractCard {

	private Cash cash;

	public CashCard() {
		super("CASH");
	}

	public Cash getCash() {
		return cash;
	}

	public void setCash(Cash cash) {
		this.cash = cash;
	}

}
