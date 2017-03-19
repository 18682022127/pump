package com.itouch8.pump.security.core.access.info.impl;

import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.permission.IPermission;
import com.itouch8.pump.security.core.common.info.impl.BaseSecurityInfo;


public class BaseAuthorizationInfo extends BaseSecurityInfo implements IAuthorizationInfo {

    
    private IPermission permission;

    public BaseAuthorizationInfo() {
        super(true);
    }

    @Override
    public IPermission getPermission() {
        return this.permission;
    }

    @Override
    public void setPermission(IPermission permission) {
        this.permission = permission;
    }
}
