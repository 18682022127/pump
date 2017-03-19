package com.itouch8.pump.security.core.session.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;


public class MapSession implements ISession {

    private static final long serialVersionUID = -3034846963262198812L;

    
    private static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;// 默认30分钟

    
    private String id;

    
    private Map<String, Object> sessionAttrs = new LinkedHashMap<String, Object>();

    
    private IUser user;

    
    private long creationTime = System.currentTimeMillis();

    
    private long lastAccessedTime = creationTime;

    
    private int maxInactiveIntervalInSeconds = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

    public MapSession() {
        this(UUID.randomUUID().toString());
    }

    public MapSession(String id) {
        this.id = id;
    }

    public MapSession(ISession session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        this.id = session.getId();
        this.user = session.getUser();
        this.sessionAttrs = new HashMap<String, Object>(session.getAttributeNames().size());
        for (String attrName : session.getAttributeNames()) {
            Object attrValue = session.getAttribute(attrName);
            this.sessionAttrs.put(attrName, attrValue);
        }
        this.lastAccessedTime = session.getLastAccessedTime();
        this.creationTime = session.getCreationTime();
        this.maxInactiveIntervalInSeconds = session.getMaxInactiveIntervalInSeconds();
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
        this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
    }

    public int getMaxInactiveIntervalInSeconds() {
        return maxInactiveIntervalInSeconds;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    private boolean isExpired(long now) {
        if (maxInactiveIntervalInSeconds < 0) {
            return false;
        }
        return now - TimeUnit.SECONDS.toMillis(maxInactiveIntervalInSeconds) >= lastAccessedTime;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String attributeName) {
        return (T) sessionAttrs.get(attributeName);
    }

    public Set<String> getAttributeNames() {
        return sessionAttrs.keySet();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            sessionAttrs.put(attributeName, attributeValue);
        }
    }

    public void removeAttribute(String attributeName) {
        sessionAttrs.remove(attributeName);
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public boolean equals(Object obj) {
        return obj instanceof ISession && id.equals(((ISession) obj).getId());
    }

    public int hashCode() {
        return id.hashCode();
    }
}
