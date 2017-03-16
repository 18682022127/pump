package com.itouch8.wechat.api.bean.card;

import com.alibaba.fastjson.annotation.JSONField;
import com.itouch8.wechat.api.bean.card.AbstractCard;
import com.itouch8.wechat.api.bean.card.GeneralCoupon;

/**
 * 优惠券
 * 
 * @author Moyq5
 *
 */
public class GeneralCouponCard extends AbstractCard {

	@JSONField(name = "general_coupon")
	private GeneralCoupon generalCoupon;

	public GeneralCouponCard() {
		super("GENERAL_COUPON");
	}

	public GeneralCoupon getGeneralCoupon() {
		return generalCoupon;
	}

	public void setGeneralCoupon(GeneralCoupon generalCoupon) {
		this.generalCoupon = generalCoupon;
	}

}
