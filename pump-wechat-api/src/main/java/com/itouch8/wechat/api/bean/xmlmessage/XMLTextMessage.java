package com.itouch8.wechat.api.bean.xmlmessage;

import com.itouch8.wechat.api.bean.message.message.Message;
import com.itouch8.wechat.api.bean.message.message.TextMessage;

public class XMLTextMessage extends XMLMessage {

	private String content;

	public XMLTextMessage(String toUserName, String fromUserName, String content) {
		super(toUserName, fromUserName, "text");
		this.content = content;
	}

	@Override
	public String subXML() {
		return "<Content><![CDATA[" + content + "]]></Content>";
	}

	@Override
	public Message convert() {
		return new TextMessage(toUserName, content);
	}

}
