package com.itouch8.pump.security.core.access.permission.impl;

import com.itouch8.pump.security.core.access.permission.IPermission;


public class Permission implements IPermission {

    
    private int permId;

    
    private String permType;

    
    private String permTypeKey;

    
    private int permLevel;

    
    private String permUrl;

    
    private String permAttr;

    @Override
    public int getPermId() {
        return permId;
    }

    public void setPermId(int permId) {
        this.permId = permId;
    }

    @Override
    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    @Override
    public String getPermTypeKey() {
        return permTypeKey;
    }

    public void setPermTypeKey(String permTypeKey) {
        this.permTypeKey = permTypeKey;
    }

    @Override
    public int getPermLevel() {
        return permLevel;
    }

    public void setPermLevel(int permLevel) {
        this.permLevel = permLevel;
    }

    @Override
    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }

    @Override
    public String getPermAttr() {
        return permAttr;
    }

    public void setPermAttr(String permAttr) {
        this.permAttr = permAttr;
    }
}
