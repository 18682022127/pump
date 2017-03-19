package com.itouch8.pump.security.core.session;

import java.io.Serializable;
import java.util.Set;

import com.itouch8.pump.security.core.login.user.IUser;


public interface ISession extends Serializable {

    
    public String getId();

    
    public <T> T getAttribute(String attributeName);

    
    public Set<String> getAttributeNames();

    
    public void setAttribute(String attributeName, Object attributeValue);

    
    public void removeAttribute(String attributeName);

    
    public long getCreationTime();

    
    public void setLastAccessedTime(long lastAccessedTime);

    
    public long getLastAccessedTime();

    
    public void setMaxInactiveIntervalInSeconds(int interval);

    
    public int getMaxInactiveIntervalInSeconds();

    
    public boolean isExpired();

    
    public IUser getUser();

    
    public void setUser(IUser user);
}
