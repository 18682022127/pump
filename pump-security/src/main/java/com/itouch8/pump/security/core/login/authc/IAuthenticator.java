package com.itouch8.pump.security.core.login.authc;

import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;


public interface IAuthenticator {

    
    public IAuthenticationInfo login(IAuthenticationToken authenticationToken);
}
