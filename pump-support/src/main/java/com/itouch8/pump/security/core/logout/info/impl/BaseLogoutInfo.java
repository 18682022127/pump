package com.itouch8.pump.security.core.logout.info.impl;

import com.itouch8.pump.security.core.common.info.impl.BaseSecurityInfo;
import com.itouch8.pump.security.core.logout.info.ILogoutInfo;
import com.itouch8.pump.security.core.session.ISession;


public class BaseLogoutInfo extends BaseSecurityInfo implements ILogoutInfo {

    
    private final ISession session;

    public BaseLogoutInfo(ISession session) {
        super(true);
        this.session = session;
    }

    @Override
    public ISession getSession() {
        return session;
    }
}
