package com.itouch8.pump.security.core.login.authc.impl;

import java.util.List;

import com.itouch8.pump.security.core.login.authc.IAuthenticator;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;


public class AuthenticatorComposite implements IAuthenticator {

    private List<IAuthenticator> authenticators;

    @Override
    public IAuthenticationInfo login(IAuthenticationToken authenticationToken) {
        List<IAuthenticator> authenticators = getAuthenticators();
        if (null != authenticators && !authenticators.isEmpty()) {
            for (IAuthenticator authenticator : authenticators) {
                IAuthenticationInfo info = authenticator.login(authenticationToken);
                if (null != info && info.isSuccess()) {
                    return info;
                }
            }
        }
        return null;
    }

    public List<IAuthenticator> getAuthenticators() {
        return authenticators;
    }

    public void setAuthenticators(List<IAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }
}
