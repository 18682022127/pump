package com.itouch8.pump.security.core.access.handler.impl;

import java.util.Map;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.handler.IAccessControlHandler;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.common.pathmatcher.PathMatcherSupport;

public abstract class AbstractPathMatcherAccessControlHandler extends PathMatcherSupport implements IAccessControlHandler {

    @Override
    public void handler(IRequestInfo requestInfo, IAuthorizationInfo info) {
        String url = requestInfo.getRequestUrl();
        if (super.isMatcher(url)) {
            String pattern = super.getMatcherPattern(url);
            if (null != pattern) {
                Map<String, String> vs = super.extractUriTemplateVariables(pattern, url);
                if (null != vs) {
                    for (String name : vs.keySet()) {
                        info.setProperty(name, vs.get(name));
                    }
                }
            }
            this.doHandler(requestInfo, info);
        }
    }

    protected abstract void doHandler(IRequestInfo requestInfo, IAuthorizationInfo info);
}
