package com.itouch8.pump.security.core.login.listener.impl;

import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.listener.ILoginListener;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;


public abstract class LoginListenerSupport implements ILoginListener {

    @Override
    public void beforeLogin(IAuthenticationToken authenticationToken) {

    }

    @Override
    public void onLoginSuccess(IAuthenticationToken authenticationToken, IAuthenticationInfo info) {

    }

    @Override
    public void onLoginFailure(IAuthenticationToken authenticationToken, IAuthenticationInfo info) {

    }

    @Override
    public void onLoginException(IAuthenticationToken authenticationToken, IAuthenticationInfo info, Exception exception) {

    }
}
