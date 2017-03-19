package com.itouch8.pump.util.tree;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


public interface ITreeNode extends Serializable {

    
    public int ROOT_ID = 0;

    
    public int ROOT_DEPTH = 0;

    
    public int STATUS_UNCHECKED = 0;

    
    public int STATUS_CHECKED = 1;

    
    public int STATUS_PART_CHECKED = 2;

    
    public int getId();

    
    public void setId(int id);

    
    public int getDepth();

    
    public void setDepth(int depth);

    
    public String getPath();

    
    public void setPath(String path);

    
    public ITreeNode getParent();

    
    public void setParent(ITreeNode parent);

    
    public boolean isLeaf();

    
    public int getCheckStatus();

    
    public String getCode();

    
    public void setCode(String code);

    
    public String getText();

    
    public void setText(String text);

    
    public String getParentCode();

    
    public String getIcon();

    
    public String getUrl();

    
    public String getDes();

    
    public int getSeqno();

    
    public boolean isChild(ITreeNode parent);

    
    public void addChild(ITreeNode child);

    
    public List<ITreeNode> getChildren();

    
    public List<ITreeNode> getChildren(ITreeNodeFilter filter);

    
    public List<ITreeNode> getAllChildren(boolean addSelf);

    
    public List<ITreeNode> getAllChildren(ITreeNodeFilter filter, boolean addSelf, boolean firstFilter);

    
    public Set<String> getAllChildrenCodes(boolean addSelf);

    
    public String getUserData();

    
    public void setUserData(String userData);

    
    public ITreeNode copyNodeData();
}
