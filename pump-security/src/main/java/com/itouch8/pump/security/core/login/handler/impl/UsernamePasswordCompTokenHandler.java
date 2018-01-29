package com.itouch8.pump.security.core.login.handler.impl;

import com.itouch8.pump.security.core.login.handler.ILoginHandler;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;
import com.itouch8.pump.security.core.login.token.impl.UsernamePasswordCompToken;

public abstract class UsernamePasswordCompTokenHandler implements ILoginHandler {

    @Override
    public void handler(IAuthenticationToken authenticationToken, IAuthenticationInfo info) {
        if (authenticationToken instanceof UsernamePasswordCompToken) {
            UsernamePasswordCompToken token = (UsernamePasswordCompToken) authenticationToken;
            this.doHandler(token, info);
        }
    }

    protected abstract void doHandler(UsernamePasswordCompToken token, IAuthenticationInfo info);
}
