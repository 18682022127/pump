package com.itouch8.wechat.api.bean.card;

import com.itouch8.wechat.api.bean.card.AbstractCard;
import com.itouch8.wechat.api.bean.card.Groupon;

/**
 * 团购券
 * 
 * @author Moyq5
 *
 */
public class GrouponCard extends AbstractCard {

	private Groupon groupon;

	public GrouponCard() {
		super("GROUPON");
	}

	public Groupon getGroupon() {
		return groupon;
	}

	public void setGroupon(Groupon groupon) {
		this.groupon = groupon;
	}

}
