package com.itouch8.pump.util.tree.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.util.tree.ITree;
import com.itouch8.pump.util.tree.ITreeNode;
import com.itouch8.pump.util.tree.ITreeNodeFilter;

public class Tree<E extends ITreeNode> implements ITree<E> {

    private static final long serialVersionUID = 4367174862859216659L;

    private E root; // 根节点

    private List<E> nodeList = new ArrayList<E>(); // 所有节点

    private Map<String, Integer> codeIdMapping = new HashMap<String, Integer>(); // 节点代码与ID的映射

    public Tree() {
        this(null);
    }

    public Tree(List<E> nodeList) {
        this(nodeList, "", "根节点");
    }

    public Tree(List<E> nodeList, String rootCode, String rootText) {
        build(nodeList, rootCode, rootText);
    }

    protected void build(List<E> nodeList, String rootCode, String rootText) {
        this.root = null;
        this.nodeList.clear();
        this.codeIdMapping.clear();
        try {
            if (null == nodeList || nodeList.isEmpty()) {
                // this.root = getRootNode(null, rootCode, rootText);
                // Throw.throwRuntimeException(ExceptionCodes.YT010305);
            } else {

                Set<String> codes = new HashSet<String>();
                List<E> nodes = new ArrayList<E>(nodeList.size());
                for (E node : nodeList) {
                    String code = node.getCode();
                    if (codes.contains(code)) {
                        throw new RuntimeException("there is a duplicate tree node --> [" + code + "]" + node.getText());
                    } else {
                        codes.add(code);
                        nodes.add(node);
                    }
                }
                this.root = getRootNode(nodeList.get(0), rootCode, rootText);
                setSiblingsChildren(this.root, nodes, 0, codes);
                if (nodes.size() != this.nodeList.size()) {// 如果不相等，说明存在孤立的节点
                    StringBuffer sb = new StringBuffer();
                    for (E node : nodes) {
                        if (!this.nodeList.contains(node)) {
                            sb.append(",[" + node.getCode() + "]" + node.getText() + "-->" + node.getParentCode());
                        }
                    }
                    throw new RuntimeException("there is isolated nodes or circular dependency --> " + sb.substring(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("there is a exception when build the tree", e);
        }
    }

    @Override
    public E getNode(String code) {
        Integer id = this.codeIdMapping.get(code);
        if (null == id) {
            throw new RuntimeException("not found the tree node --> [" + code + "]");
        } else {
            return this.getNode(id);
        }
    }

    @Override
    public E getNode(int id) {
        if (id == 0) {
            return getRoot();
        } else if (id < 0 || id > this.nodeList.size()) {
            throw new RuntimeException("Tree node index out of range: " + id + ", the valid range is [0, " + (this.nodeList.size() - 1) + "]");
        }
        return this.nodeList.get(id - 1);
    }

    @Override
    public E getRoot() {
        return this.root;
    }

    @Override
    public boolean containsCode(String code) {
        return null != codeIdMapping && codeIdMapping.containsKey(code);
    }

    @Override
    public List<E> getNodeList() {
        return Collections.unmodifiableList(nodeList);
    }

    @Override
    public List<E> getNodeList(ITreeNodeFilter filter) {
        if (null == nodeList) {
            return null;
        } else {
            List<E> list = new ArrayList<E>();
            for (E node : nodeList) {
                if (null == filter || filter.accept(node)) {
                    list.add(node);
                }
            }
            return list;
        }
    }

    @Override
    public ITree<E> getSubTree(String code) {
        return getSubTree(code, (ITreeNodeFilter) null, true);
    }

    @Override
    public ITree<E> getSubTree(final int depth) {
        return getSubTree(new ITreeNodeFilter() {
            public boolean accept(ITreeNode node) {
                return node.getDepth() <= depth;
            }
        }, true);
    }

    @Override
    public ITree<E> getSubTree(String code, int depth) {
        E node = getNode(code);
        final int childDepth = node.getDepth() + depth;
        return getSubTree(code, new ITreeNodeFilter() {
            public boolean accept(ITreeNode node) {
                return node.getDepth() <= childDepth;
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ITree<E> getSubTree(ITreeNodeFilter filter, boolean firstFilter) {
        E root = getRoot();
        List<E> newNodeList = new ArrayList<E>();
        List<ITreeNode> nodeList = root.getAllChildren(filter, false, firstFilter);
        if (null != nodeList && !nodeList.isEmpty()) {
            for (ITreeNode node : nodeList) {
                newNodeList.add((E) node.copyNodeData());
            }
        }
        return new Tree<E>(newNodeList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ITree<E> getSubTree(String code, ITreeNodeFilter filter, boolean firstFilter) {
        E root = getNode(code);
        List<E> newNodeList = new ArrayList<E>();
        newNodeList.add((E) root.copyNodeData());
        List<ITreeNode> nodeList = root.getAllChildren(filter, false, firstFilter);
        if (null != nodeList && !nodeList.isEmpty()) {
            for (ITreeNode node : nodeList) {
                newNodeList.add((E) node.copyNodeData());
            }
        }
        return new Tree<E>(newNodeList);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tree :\n");
        if (null != root) {
            toInnerString(sb, root);
        }
        sb.append("Tree End\n");
        return sb.toString();
    }

    private int setSiblingsChildren(E parentNode, List<E> nodes, int id, Set<String> codes) {
        boolean isRoot = (id == 0);
        for (E node : nodes) {
            if (this.nodeList.contains(node)) {
                continue;
            }

            if (node.isChild(parentNode) || isRoot && !codes.contains(node.getParentCode())) {
                id = id + 1;
                this.nodeList.add(node);
                this.codeIdMapping.put(node.getCode(), id);

                node.setId(id);
                node.setDepth(parentNode.getDepth() + 1);
                node.setPath(isRoot ? node.getText() : (parentNode.getPath() + "-->" + node.getText()));
                // System.out.println(id + "==> " + node.getPath());
                parentNode.addChild(node);
                id = setSiblingsChildren(node, nodes, id, codes);
            }
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    private E getRootNode(E node, String rootCode, String rootText) {
        E root = null;
        try {
            Class<E> cls = null;
            if (null != node) {
                cls = (Class<E>) node.getClass();
            } else {
                cls = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            }
            root = cls.newInstance();
            root.setId(ITreeNode.ROOT_ID);
            root.setDepth(ITreeNode.ROOT_DEPTH);
            root.setPath(rootCode);
            root.setText(rootText);
            return root;
        } catch (Exception e) {
            throw new RuntimeException("there is a exception when parse the virtual root node", e);
        }
    }

    private void toInnerString(StringBuilder sb, ITreeNode node) {
        sb.append(node.toString());
        if (null != node.getChildren()) {
            for (ITreeNode n : node.getChildren()) {
                toInnerString(sb, n);
            }
        }
    }
}
