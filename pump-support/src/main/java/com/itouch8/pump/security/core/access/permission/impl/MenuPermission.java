package com.itouch8.pump.security.core.access.permission.impl;

import com.itouch8.pump.security.core.access.permission.IMenuPermission;
import com.itouch8.pump.util.tree.impl.TreeNode;


public class MenuPermission extends TreeNode implements IMenuPermission {

    private static final long serialVersionUID = -1425109593985494384L;

    
    private int permId;

    
    private String permAttr;

    
    private String displayArea;

    
    private int dependMenuId = -1;

    
    private String authorLevel;

    
    private String menuFlag;

    
    private String permTreeFlag;

    
    private String targetPage;

    @Override
    public int getPermId() {
        return permId;
    }

    public void setPermId(int permId) {
        this.permId = permId;
    }

    @Override
    public String getPermAttr() {
        return permAttr;
    }

    public void setPermAttr(String permAttr) {
        this.permAttr = permAttr;
    }

    @Override
    public String getDisplayArea() {
        return displayArea;
    }

    public void setDisplayArea(String displayArea) {
        this.displayArea = displayArea;
    }

    @Override
    public int getDependMenuId() {
        return dependMenuId;
    }

    public void setDependMenuId(int dependMenuId) {
        this.dependMenuId = dependMenuId;
    }

    @Override
    public String getAuthorLevel() {
        return authorLevel;
    }

    public void setAuthorLevel(String authorLevel) {
        this.authorLevel = authorLevel;
    }

    @Override
    public String getMenuFlag() {
        return menuFlag;
    }

    public void setMenuFlag(String menuFlag) {
        this.menuFlag = menuFlag;
    }

    @Override
    public String getPermTreeFlag() {
        return permTreeFlag;
    }

    public void setPermTreeFlag(String permTreeFlag) {
        this.permTreeFlag = permTreeFlag;
    }

    @Override
    public String getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }
}
