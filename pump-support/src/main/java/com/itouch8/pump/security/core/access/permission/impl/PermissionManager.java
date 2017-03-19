package com.itouch8.pump.security.core.access.permission.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itouch8.pump.security.core.access.permission.IMenuPermission;
import com.itouch8.pump.security.core.access.permission.IPermissionManager;
import com.itouch8.pump.util.tree.ITree;
import com.itouch8.pump.util.tree.impl.Tree;


public class PermissionManager implements IPermissionManager {

    
    private final Set<Integer> roleIds = new HashSet<Integer>();

    
    private final Set<Integer> permissionIds = new HashSet<Integer>();

    
    private final List<IMenuPermission> menuPermissions = new ArrayList<IMenuPermission>();

    
    private ITree<IMenuPermission> menuTree;

    @Override
    public Set<Integer> getRoleIds() {
        return Collections.unmodifiableSet(roleIds);
    }

    @Override
    public void addRoleIds(Collection<Integer> roleIds) {
        this.roleIds.addAll(roleIds);
    }

    @Override
    public Set<Integer> getPermissionIds() {
        return Collections.unmodifiableSet(permissionIds);
    }

    @Override
    public void addPermissionIds(Collection<Integer> permissionIds) {
        this.permissionIds.addAll(permissionIds);
    }

    @Override
    public void addMenuPermissions(Collection<? extends IMenuPermission> menuPermissions) {
        if (null != menuPermissions && !menuPermissions.isEmpty()) {
            if (null != this.menuTree) {
                throw new RuntimeException("the menu tree had built, so not allow add menuPermissions.");
            }
            Set<Integer> permissionIds = getMenuPermissionIds();// 原有菜单权限ID集合
            for (IMenuPermission mp : menuPermissions) {
                Integer permssionId = mp.getPermId();
                this.permissionIds.add(permssionId);
                if (!permissionIds.contains(permssionId)) {
                    permissionIds.add(permssionId);
                    this.menuPermissions.add(mp);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ITree<IMenuPermission> getMenuTree() {
        if (menuTree == null) {
            Collections.sort(this.menuPermissions, new Comparator<IMenuPermission>() {
                @Override
                public int compare(IMenuPermission o1, IMenuPermission o2) {
                    return o1.getSeqno() - o2.getSeqno();
                }
            });
            this.menuTree = new Tree(this.menuPermissions);
            this.menuPermissions.clear();
        }
        return menuTree;
    }

    public void rerfeshMenuTree(List<IMenuPermission> menuPermissions) {
        this.menuPermissions.clear();
        this.menuTree = null;
        this.addMenuPermissions(menuPermissions);
    }

    private Set<Integer> getMenuPermissionIds() {
        Set<Integer> permissionIds = new HashSet<Integer>();
        for (IMenuPermission mp : menuPermissions) {
            permissionIds.add(mp.getPermId());
        }
        return permissionIds;
    }
}
