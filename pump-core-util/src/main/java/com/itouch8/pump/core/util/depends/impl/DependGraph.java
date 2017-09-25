package com.itouch8.pump.core.util.depends.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.core.util.depends.IDependGraph;
import com.itouch8.pump.core.util.depends.IDependNode;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class DependGraph<E extends IDependNode> implements IDependGraph<E> {

    private List<E> nodeList;

    private List<E> sortList;

    private boolean hasSort = false;

    
    public DependGraph() {}

    
    public DependGraph(List<E> nodeList) {
        this.setNodeList(nodeList);
    }

    
    public List<E> sort() {
        if (!hasSort) {
            this.sortList = sortNodeList();
            hasSort = true;
        }
        return sortList;
    }

    
    public List<E> getNodeList() {
        return nodeList;
    }

    
    public void setNodeList(List<E> nodeList) {
        if (null != nodeList && nodeList.size() >= 2) {
            List<String> keys = new ArrayList<String>();
            for (int i = 0; i < nodeList.size(); i++) {
                if (keys.contains(nodeList.get(i).getId())) {
                    nodeList.remove(i);
                } else {
                    keys.add(nodeList.get(i).getId());
                }
            }
        }
        this.nodeList = nodeList;
    }

    
    private List<E> sortNodeList() {
        List<E> nodeList = getNodeList();
        if (null == nodeList || nodeList.isEmpty()) {
            return nodeList;
        }
        
        Map<String, DependGraphNodeAdapter<E>> nodeMap = new LinkedHashMap<String, DependGraphNodeAdapter<E>>();
        for (E node : nodeList) {// 设置所有节点
            String id = node.getId();
            nodeMap.put(id, new DependGraphNodeAdapter<E>(node));
        }
        List<E> sortList = new ArrayList<E>();
        for (DependGraphNodeAdapter<E> node : nodeMap.values()) {// 分析依赖
            sortNodeList(node, nodeMap, sortList);
        }
        return sortList;
    }

    
    private void sortNodeList(DependGraphNodeAdapter<E> node, Map<String, DependGraphNodeAdapter<E>> nodeMap, List<E> sortList) {
        int status = node.nodeStatus;
        if (1 == status) {// 依赖已经分析完成，直接回溯
            goBackSortNodeList(node, nodeMap, sortList);
        } else if (2 == status) {// 正在分析依赖中，如果再次分析到该节点，说明存成循环依赖
            DependGraphNodeAdapter<E> srcNode = node.srcNode;
            String srcNodeId = srcNode.getId();
            String id = node.getId();
            StringBuffer sb = new StringBuffer(id);
            while (!srcNodeId.equals(id)) {
                sb.insert(0, srcNodeId + "-->");
                srcNode = srcNode.srcNode;// .getSrcNode();
                if (null == srcNode) {
                    break;
                }
                srcNodeId = srcNode.getId();
            }
            sb.insert(0, id + "-->");
            Throw.throwRuntimeException(ExceptionCodes.YT010001, sb);
        } else {
            node.nodeStatus = 2;
            int index = node.dependIndex;
            List<String> depends = node.getDepends();
            // 如果没有依赖或者依赖已经全部分析完成，设置为分析完成状态，然后再回溯分析
            if (null == depends || depends.isEmpty() || index == depends.size()) {
                sortList.add(node.node);
                node.nodeStatus = 1;
                goBackSortNodeList(node, nodeMap, sortList);
            } else {// 分析第index个依赖
                String depend = depends.get(index);
                DependGraphNodeAdapter<E> dependNode = nodeMap.get(depend);
                if (null == dependNode) {
                    depends.remove(depend);
                    CommonLogger.warn("没有找到" + node.getId() + "的依赖" + depend + "，将忽略该依赖");
                    if (index == depends.size()) {// 移除无效依赖之后，再检查是否分析完毕
                        sortList.add(node.node);
                        node.nodeStatus = 1;
                        goBackSortNodeList(node, nodeMap, sortList);
                    }
                } else {
                    dependNode.srcNode = node;// 设置分析的下一个节点的来源节点为当前节点
                    sortNodeList(dependNode, nodeMap, sortList);
                }
            }
        }
    }

    
    private void goBackSortNodeList(DependGraphNodeAdapter<E> node, Map<String, DependGraphNodeAdapter<E>> nodeMap, List<E> sortList) {
        DependGraphNodeAdapter<E> srcNode = node.srcNode;
        if (null != srcNode) {
            node.srcNode = null;
            srcNode.nodeStatus = 0;
            srcNode.dependIndex = srcNode.dependIndex + 1;
            sortNodeList(srcNode, nodeMap, sortList);
        }
    }

    private static class DependGraphNodeAdapter<E extends IDependNode> {
        
        private E node;
        
        private DependGraphNodeAdapter<E> srcNode;

        
        private int nodeStatus;

        
        private int dependIndex;

        private DependGraphNodeAdapter(E node) {
            this.node = node;
            this.nodeStatus = 0;
            this.dependIndex = 0;
        }

        private String getId() {
            return node.getId();
        }

        private List<String> getDepends() {
            return node.getDepends();
        }
    }
}
