package com.itouch8.pump.security.core.login.info.impl;

import com.itouch8.pump.security.core.common.info.impl.BaseSecurityInfo;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;


public class BaseAuthenticationInfo extends BaseSecurityInfo implements IAuthenticationInfo {

    
    private ISession session;

    
    private IUser user;

    public BaseAuthenticationInfo() {
        super(true);
    }

    @Override
    public ISession getSession() {
        return session;
    }

    @Override
    public void setSession(ISession session) {
        this.session = session;
        if (null != session) {
            if (null != user && null == session.getUser()) {
                session.setUser(user);
            } else if (null == user && null != session.getUser()) {
                this.user = session.getUser();
            } else if (null != user && null != session.getUser() && user.equals(session.getUser())) {
                throw new RuntimeException("the session user is not coherence");
            }
        }
    }

    
    @Override
    public IUser getUser() {
        if (null != user) {
            return user;
        } else if (null != session) {
            return session.getUser();
        }
        return null;
    }

    @Override
    public void setUser(IUser user) {
        this.user = user;
        if (null != session && null == session.getUser()) {
            session.setUser(user);
        }
    }
}
