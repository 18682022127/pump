package com.itouch8.pump.security.core.access.handler;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;


public interface IAccessControlHandler {

    
    public void handler(IRequestInfo requestInfo, IAuthorizationInfo info);
}
