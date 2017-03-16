package com.itouch8.wechat.api.bean.message.templatemessage;

import com.itouch8.wechat.api.bean.BaseResult;

public class TemplateMessageResult extends BaseResult{

	private Long msgid;

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}


}
