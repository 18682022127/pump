package com.itouch8.pump.security.core.access.authz;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;


public interface IAuthorizer {

    
    public IAuthorizationInfo isPermitted(IRequestInfo requestInfo);
}
