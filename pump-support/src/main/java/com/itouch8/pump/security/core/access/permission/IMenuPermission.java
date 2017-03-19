package com.itouch8.pump.security.core.access.permission;

import com.itouch8.pump.util.tree.ITreeNode;


public interface IMenuPermission extends ITreeNode {

    
    public int getPermId();

    
    public String getPermAttr();

    
    public String getDisplayArea();

    
    public int getDependMenuId();

    
    public String getAuthorLevel();

    
    public String getMenuFlag();

    
    public String getPermTreeFlag();

    
    public String getTargetPage();

    
    public int getSeqno();
}
