package com.itouch8.pump.security.core.access.permission;


public interface IPermission {

    
    public int getPermId();

    
    public String getPermType();

    
    public String getPermTypeKey();

    
    public int getPermLevel();

    
    public String getPermUrl();

    
    public String getPermAttr();
}
