package com.itouch8.pump.security.core.session.impl;

import java.util.Set;

import org.springframework.session.ExpiringSession;

import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;


public class SpringSessionProxy implements ISession {

    
    private static final long serialVersionUID = -3404909748158344058L;

    private static final String USER_KEY = SpringSessionProxy.class.getName() + ".USER_KEY";

    private final ExpiringSession session;

    public SpringSessionProxy(ExpiringSession session) {
        super();
        this.session = session;
    }

    ExpiringSession getExpiringSession() {
        return session;
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return session.getAttribute(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return session.getAttributeNames();
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        session.setAttribute(attributeName, attributeValue);
    }

    @Override
    public void removeAttribute(String attributeName) {
        session.removeAttribute(attributeName);
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        session.setLastAccessedTime(lastAccessedTime);
    }

    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {
        session.setMaxInactiveIntervalInSeconds(interval);
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return session.getMaxInactiveIntervalInSeconds();
    }

    @Override
    public boolean isExpired() {
        return session.isExpired();
    }

    @Override
    public IUser getUser() {
        return session.getAttribute(USER_KEY);
    }

    @Override
    public void setUser(IUser user) {
        session.setAttribute(USER_KEY, user);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((session == null) ? 0 : session.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpringSessionProxy other = (SpringSessionProxy) obj;
        if (session == null) {
            if (other.session != null)
                return false;
        } else if (!session.equals(other.session))
            return false;
        return true;
    }
}
