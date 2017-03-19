package com.itouch8.pump.security.core.login.handler;

import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;


public interface ILoginHandler {

    
    public void handler(IAuthenticationToken authenticationToken, IAuthenticationInfo info);
}
