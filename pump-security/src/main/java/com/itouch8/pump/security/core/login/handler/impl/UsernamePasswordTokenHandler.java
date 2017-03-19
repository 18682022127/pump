package com.itouch8.pump.security.core.login.handler.impl;

import com.itouch8.pump.security.core.login.handler.ILoginHandler;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;
import com.itouch8.pump.security.core.login.token.impl.UsernamePasswordToken;


public abstract class UsernamePasswordTokenHandler implements ILoginHandler {

    @Override
    public void handler(IAuthenticationToken authenticationToken, IAuthenticationInfo info) {
        if (authenticationToken instanceof UsernamePasswordToken) {
            UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
            this.doHandler(token, info);
        }
    }

    protected abstract void doHandler(UsernamePasswordToken token, IAuthenticationInfo info);
}
