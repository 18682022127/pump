package com.itouch8.wechat.api.bean.pay;

import java.util.Map;

import com.itouch8.wechat.api.bean.BaseResult;

public class OrderInfo extends BaseResult{
	
	private Map<String,String> order_info;

	public Map<String, String> getOrder_info() {
		return order_info;
	}

	public void setOrder_info(Map<String, String> orderInfo) {
		order_info = orderInfo;
	}
	
	
}