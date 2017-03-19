package com.itouch8.pump.security.core.logout.provider;

import java.util.Set;

import com.itouch8.pump.security.core.session.ISession;


public interface IExpiredSessionProvider {

    public Set<ISession> getExpiredSessions();
}
