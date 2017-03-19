package com.itouch8.pump.security.core.access.permission;

import java.util.Collection;
import java.util.Set;

import com.itouch8.pump.util.tree.ITree;


public interface IPermissionManager {

    
    public Set<Integer> getRoleIds();

    
    public void addRoleIds(Collection<Integer> roleIds);

    
    public Set<Integer> getPermissionIds();

    
    public void addPermissionIds(Collection<Integer> permissionIds);

    
    public void addMenuPermissions(Collection<? extends IMenuPermission> menuPermissions);

    
    public ITree<IMenuPermission> getMenuTree();
}
