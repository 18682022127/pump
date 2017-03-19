package com.itouch8.pump.security.core.access.info;

import com.itouch8.pump.security.core.access.permission.IPermission;
import com.itouch8.pump.security.core.common.info.ISecurityInfo;


public interface IAuthorizationInfo extends ISecurityInfo {

    
    public IPermission getPermission();

    
    public void setPermission(IPermission permission);
}
