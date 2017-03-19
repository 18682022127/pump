package com.itouch8.pump.security.core.login.listener;

import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.token.IAuthenticationToken;


public interface ILoginListener {

    
    public void beforeLogin(IAuthenticationToken authenticationToken);

    
    public void onLoginSuccess(IAuthenticationToken authenticationToken, IAuthenticationInfo info);

    
    public void onLoginFailure(IAuthenticationToken authenticationToken, IAuthenticationInfo info);

    
    public void onLoginException(IAuthenticationToken authenticationToken, IAuthenticationInfo info, Exception exception);

}
