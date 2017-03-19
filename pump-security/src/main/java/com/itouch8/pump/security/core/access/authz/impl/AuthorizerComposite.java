package com.itouch8.pump.security.core.access.authz.impl;

import java.util.List;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.authz.IAuthorizer;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;


public class AuthorizerComposite implements IAuthorizer {

    private List<IAuthorizer> authorizers;

    @Override
    public IAuthorizationInfo isPermitted(IRequestInfo requestInfo) {
        List<IAuthorizer> authorizers = getAuthorizers();
        if (null != authorizers && !authorizers.isEmpty()) {
            for (IAuthorizer authorizer : authorizers) {
                IAuthorizationInfo info = authorizer.isPermitted(requestInfo);
                if (null != info && info.isSuccess()) {// 只要一个成功就返回
                    return info;
                }
            }
        }
        return null;
    }

    public List<IAuthorizer> getAuthorizers() {
        return authorizers;
    }

    public void setAuthorizers(List<IAuthorizer> authorizers) {
        this.authorizers = authorizers;
    }
}
