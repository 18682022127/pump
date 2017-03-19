package com.itouch8.pump.security.core.login.info;

import com.itouch8.pump.security.core.common.info.ISecurityInfo;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;


public interface IAuthenticationInfo extends ISecurityInfo {

    
    public IUser getUser();

    
    public void setUser(IUser user);

    
    public ISession getSession();

    
    public void setSession(ISession session);
}
