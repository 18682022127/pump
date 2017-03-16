package com.itouch8.wechat.api.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import com.itouch8.wechat.api.bean.token.Token;
import com.itouch8.wechat.api.client.LocalHttpClient;

/**
 * ACCESS_TOKEN API
 * @author LiYi
 *
 */
public class TokenAPI extends BaseAPI{

	/**
	 * 获取access_token
	 * @param appid appid
	 * @param secret secret
	 * @return Token
	 */
	public static Token token(String appid,String secret){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri(BASE_URI + "/cgi-bin/token")
				.addParameter("grant_type","client_credential")
				.addParameter("appid", appid)
				.addParameter("secret", secret)
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,Token.class);
	}

}
