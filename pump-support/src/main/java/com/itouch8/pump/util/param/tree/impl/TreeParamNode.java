package com.itouch8.pump.util.param.tree.impl;

import com.itouch8.pump.util.param.tree.ITreeParamNode;
import com.itouch8.pump.util.tree.impl.TreeNode;


public class TreeParamNode extends TreeNode implements ITreeParamNode {

    
    private static final long serialVersionUID = -2764873861475501074L;

    private String paramCode;

    @Override
    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Override
    public String getDataCode() {
        return getCode();
    }

    @Override
    public String getDataText() {
        return getText();
    }

    @Override
    public String getDataParam() {
        return getUserData();
    }
}
