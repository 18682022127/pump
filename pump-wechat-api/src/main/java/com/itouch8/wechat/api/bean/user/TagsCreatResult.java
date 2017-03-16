package com.itouch8.wechat.api.bean.user;

import com.itouch8.wechat.api.bean.BaseResult;

/**
 * 标签
 * 
 * @author SLYH
 * 
 */
public class TagsCreatResult extends BaseResult {

	private Tag tag;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
