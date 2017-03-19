package com.itouch8.pump.security.core.logout.info;

import com.itouch8.pump.security.core.common.info.ISecurityInfo;
import com.itouch8.pump.security.core.session.ISession;


public interface ILogoutInfo extends ISecurityInfo {

    
    public ISession getSession();
}
