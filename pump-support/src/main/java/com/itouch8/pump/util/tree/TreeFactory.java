package com.itouch8.pump.util.tree;

import java.util.List;

import com.itouch8.pump.util.tree.impl.Tree;


public class TreeFactory {

    
    public static <E extends ITreeNode> ITree<E> builder(List<E> nodeList) {
        return new Tree<E>(nodeList);
    }

    
    public static <E extends ITreeNode> ITree<E> builder(List<E> nodeList, String rootCode, String rootText) {
        return new Tree<E>(nodeList, rootCode, rootText);
    }

    
    @SuppressWarnings("unchecked")
    public static <E extends ITreeNode> List<E> rootChildren(List<E> nodeList) {
        ITree<E> tree = builder(nodeList);
        E root = tree.getRoot();
        if (null != root) {
            return (List<E>) root.getChildren();
        }
        return null;
    }

    
    @SuppressWarnings("unchecked")
    public static <E extends ITreeNode> List<E> getChildren(E node) {
        if (null != node) {
            return (List<E>) node.getChildren();
        }
        return null;
    }

    
    @SuppressWarnings("unchecked")
    public static <E extends ITreeNode> List<E> getAllChildren(E node, boolean addSelf) {
        if (null != node) {
            return (List<E>) node.getAllChildren(addSelf);
        }
        return null;
    }
}
