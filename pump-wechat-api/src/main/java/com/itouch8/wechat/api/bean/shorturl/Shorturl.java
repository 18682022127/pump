package com.itouch8.wechat.api.bean.shorturl;

import com.itouch8.wechat.api.bean.BaseResult;

public class Shorturl extends BaseResult{

	private String short_url;

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}
}