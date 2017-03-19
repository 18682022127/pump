package com.itouch8.pump.security.core.access.listener;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;


public interface IAccessControlListener {

    
    public void beforePermitted(IRequestInfo requestInfo);

    
    public void onPermittedPass(IRequestInfo requestInfo, IAuthorizationInfo info);

    
    public void onPermittedDeny(IRequestInfo requestInfo, IAuthorizationInfo info);

    
    public void onPermittedException(IRequestInfo requestInfo, IAuthorizationInfo info, Exception exception);
}
