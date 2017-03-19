package com.itouch8.pump.util.tree;

import java.util.List;

public interface ITreeService<N extends ITreeNode> {

    
    public ITree<N> load();

    
    public List<N> getChildren(N parent);

    
    public N getNode(N node);

    
    public int delete(N node);

    
    public int insert(N node);

    
    public int update(N node);

    
    public int insertAtIndex(N parent, N node, int index);

}
