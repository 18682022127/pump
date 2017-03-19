package com.itouch8.pump.security.core.login.token;


public interface IAuthenticationToken {

    
    public Object getPrincipal();

    
    public Object getCredentials();

}
