package com.itouch8.pump.wechat.api.popular.bean.material;

import java.util.List;

import com.itouch8.pump.wechat.api.popular.bean.message.Article;

public class MaterialBatchgetResultItemContent {

	private List<Article> news_item;

	public List<Article> getNews_item() {
		return news_item;
	}

	public void setNews_item(List<Article> news_item) {
		this.news_item = news_item;
	}


}
