package com.itouch8.pump.security.core.login.listener.impl;

import com.itouch8.pump.security.SecurityUtils;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;


public class SessionCreateListener extends LoginListenerSupport {

    @Override
    public void onLoginSuccess(IAuthenticationToken authenticationToken, IAuthenticationInfo info) {
        IUser user = info.getUser();
        if (null != user) {
            ISession session = SecurityUtils.getSessionManager().createSession();
            session.setUser(user);
        }
    }
}
