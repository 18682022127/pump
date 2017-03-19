package com.itouch8.pump.security.core.access.authz.impl;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.authz.IAuthorizer;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.info.impl.BaseAuthorizationInfo;
import com.itouch8.pump.security.core.common.pathmatcher.PathMatcherSupport;


public abstract class AbstractPathMatcherAuthorizer extends PathMatcherSupport implements IAuthorizer {

    
    private boolean permittedWhenNotMatcher;

    @Override
    public IAuthorizationInfo isPermitted(IRequestInfo requestInfo) {
        if (super.isMatcher(requestInfo.getRequestUrl())) {
            return this.doPermitted(requestInfo);
        } else if (isPermittedWhenNotMatcher()) {
            IAuthorizationInfo info = new BaseAuthorizationInfo();
            info.setSuccess(true);
            return info;
        } else {
            return null;
        }
    }

    protected abstract IAuthorizationInfo doPermitted(IRequestInfo requestInfo);

    public boolean isPermittedWhenNotMatcher() {
        return permittedWhenNotMatcher;
    }

    public void setPermittedWhenUrlMatcher(boolean permittedWhenNotMatcher) {
        this.permittedWhenNotMatcher = permittedWhenNotMatcher;
    }
}
