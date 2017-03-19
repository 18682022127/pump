package com.itouch8.pump.util.tree;

import java.io.Serializable;
import java.util.List;


public interface ITree<E extends ITreeNode> extends Serializable {

    
    public E getRoot();

    
    public E getNode(int id);

    
    public E getNode(String code);

    
    public boolean containsCode(String node);

    
    public List<E> getNodeList();

    
    public List<E> getNodeList(ITreeNodeFilter filter);

    
    public ITree<E> getSubTree(String code);

    
    public ITree<E> getSubTree(int depth);

    
    public ITree<E> getSubTree(String code, int depth);

    
    public ITree<E> getSubTree(ITreeNodeFilter filter, boolean firstFilter);

    
    public ITree<E> getSubTree(String code, ITreeNodeFilter filter, boolean firstFilter);
}
