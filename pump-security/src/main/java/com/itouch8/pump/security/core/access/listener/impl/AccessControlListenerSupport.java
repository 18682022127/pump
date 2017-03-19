package com.itouch8.pump.security.core.access.listener.impl;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.listener.IAccessControlListener;


public abstract class AccessControlListenerSupport implements IAccessControlListener {

    @Override
    public void beforePermitted(IRequestInfo requestInfo) {

    }

    @Override
    public void onPermittedPass(IRequestInfo requestInfo, IAuthorizationInfo info) {

    }

    @Override
    public void onPermittedDeny(IRequestInfo requestInfo, IAuthorizationInfo info) {

    }

    @Override
    public void onPermittedException(IRequestInfo requestInfo, IAuthorizationInfo info, Exception exception) {

    }
}
