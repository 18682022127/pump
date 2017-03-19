package com.itouch8.pump.util.tree.impl;

import java.util.List;

import com.itouch8.pump.util.tree.ITreeNode;

public class TreeServiceSupport<N extends ITreeNode> extends AbstractTreeService<N> {

    @Override
    public List<N> getChildren(N parent) {
        return null;
    }

    @Override
    public N getNode(N node) {
        return null;
    }

    @Override
    public int insert(N node) {
        return 0;
    }

    @Override
    public int update(N node) {
        return 0;
    }

    @Override
    protected List<N> loadAllNodes() {
        return null;
    }

    @Override
    protected int deleteNode(N node) {
        return 0;
    }

    @Override
    protected int deleteInvalidNodes(N node) {
        return 0;
    }

    @Override
    protected void updateParentAndSeqnos(List<N> nodes) {

    }
}
