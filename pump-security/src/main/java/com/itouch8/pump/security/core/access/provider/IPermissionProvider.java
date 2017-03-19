package com.itouch8.pump.security.core.access.provider;

import java.util.Collection;

import com.itouch8.pump.security.core.access.permission.IMenuPermission;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.user.IUser;


public interface IPermissionProvider {

    
    public Collection<Integer> getRoleIds(IUser user, IAuthenticationInfo info);

    
    public Collection<Integer> getPermissionIds(IUser user, IAuthenticationInfo info);

    
    public Collection<IMenuPermission> getMenuPermissions(IUser user, IAuthenticationInfo info);
}
