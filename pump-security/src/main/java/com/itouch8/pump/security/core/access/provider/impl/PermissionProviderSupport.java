package com.itouch8.pump.security.core.access.provider.impl;

import java.util.Collection;

import com.itouch8.pump.security.core.access.permission.IMenuPermission;
import com.itouch8.pump.security.core.access.provider.IPermissionProvider;
import com.itouch8.pump.security.core.login.info.IAuthenticationInfo;
import com.itouch8.pump.security.core.login.user.IUser;


public class PermissionProviderSupport implements IPermissionProvider {

    @Override
    public Collection<Integer> getRoleIds(IUser user, IAuthenticationInfo info) {
        return null;
    }

    @Override
    public Collection<Integer> getPermissionIds(IUser user, IAuthenticationInfo info) {
        return null;
    }

    @Override
    public Collection<IMenuPermission> getMenuPermissions(IUser user, IAuthenticationInfo info) {
        return null;
    }
}
