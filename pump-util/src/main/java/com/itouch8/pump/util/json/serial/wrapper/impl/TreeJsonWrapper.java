package com.itouch8.pump.util.json.serial.wrapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;
import com.itouch8.pump.util.tree.ITree;
import com.itouch8.pump.util.tree.TreeFactory;
import com.itouch8.pump.util.tree.impl.TreeNode;


public class TreeJsonWrapper implements IJsonWrapper {

    
    private boolean returnArrayWhenOnlyOneNode = true;

    @SuppressWarnings("unchecked")
    @Override
    public Object wrap(Object original) {
        if (original == null) {
            return null;
        }

        List<?> rs = null;
        if (original instanceof ITree) {
            rs = ((ITree<?>) original).getRoot().getChildren();
        } else if (original instanceof Collection) {
            List<TreeNode> nodes = new ArrayList<TreeNode>();
            for (Iterator<TreeNode> i = ((Collection<TreeNode>) original).iterator(); i.hasNext();) {
                nodes.add(i.next());
            }
            rs = TreeFactory.rootChildren(nodes);
        }

        if (null != rs) {
            if (rs.size() == 1 && !returnArrayWhenOnlyOneNode) {
                return rs.get(0);
            } else {
                return rs;
            }
        } else {
            return original;
        }
    }

    public boolean isReturnArrayWhenOnlyOneNode() {
        return returnArrayWhenOnlyOneNode;
    }

    public void setReturnArrayWhenOnlyOneNode(boolean returnArrayWhenOnlyOneNode) {
        this.returnArrayWhenOnlyOneNode = returnArrayWhenOnlyOneNode;
    }
}
