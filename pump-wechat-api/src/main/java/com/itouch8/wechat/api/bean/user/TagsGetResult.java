package com.itouch8.wechat.api.bean.user;

import java.util.List;

import com.itouch8.wechat.api.bean.BaseResult;

/**
 * 标签
 * 
 * @author SLYH
 * 
 */
public class TagsGetResult extends BaseResult {

	private List<Tag> tags;

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
}
