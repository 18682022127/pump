package com.itouch8.pump.security.core.session.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itouch8.pump.security.core.session.ISession;
import com.itouch8.pump.security.core.session.ISessionManager;


public class MapSessionManager implements ISessionManager {

    
    private Integer defaultMaxInactiveInterval;

    
    private final Map<String, ISession> sessions;

    public MapSessionManager() {
        this(new ConcurrentHashMap<String, ISession>());
    }

    public MapSessionManager(Map<String, ISession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        }
        this.sessions = sessions;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = Integer.valueOf(defaultMaxInactiveInterval);
    }

    public void save(ISession session) {
        sessions.put(session.getId(), new MapSession(session));
    }

    public ISession getSession(String id) {
        ISession saved = sessions.get(id);
        if (saved == null) {
            return null;
        }
        if (saved.isExpired()) {
            delete(saved.getId());
            return null;
        }
        return new MapSession(saved);
    }

    public void delete(String id) {
        sessions.remove(id);
    }

    public ISession createSession() {
        ISession result = new MapSession();
        if (defaultMaxInactiveInterval != null) {
            result.setMaxInactiveIntervalInSeconds(defaultMaxInactiveInterval);
        }
        return result;
    }
}
