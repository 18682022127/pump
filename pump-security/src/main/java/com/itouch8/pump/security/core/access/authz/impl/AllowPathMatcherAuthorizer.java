package com.itouch8.pump.security.core.access.authz.impl;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.info.impl.BaseAuthorizationInfo;


public class AllowPathMatcherAuthorizer extends AbstractPathMatcherAuthorizer {

    @Override
    protected IAuthorizationInfo doPermitted(IRequestInfo requestInfo) {
        IAuthorizationInfo info = new BaseAuthorizationInfo();
        info.setSuccess(true);
        return info;
    }
}
