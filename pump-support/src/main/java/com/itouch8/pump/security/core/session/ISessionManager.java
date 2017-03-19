package com.itouch8.pump.security.core.session;


public interface ISessionManager {

    
    public ISession createSession();

    
    public void save(ISession session);

    
    public ISession getSession(String id);

    
    public void delete(String id);
}
