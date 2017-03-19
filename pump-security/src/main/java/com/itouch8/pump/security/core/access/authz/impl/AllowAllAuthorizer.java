package com.itouch8.pump.security.core.access.authz.impl;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.authz.IAuthorizer;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.info.impl.BaseAuthorizationInfo;


public class AllowAllAuthorizer implements IAuthorizer {

    @Override
    public IAuthorizationInfo isPermitted(IRequestInfo requestInfo) {
        IAuthorizationInfo info = new BaseAuthorizationInfo();
        info.setSuccess(true);
        return info;
    }
}
