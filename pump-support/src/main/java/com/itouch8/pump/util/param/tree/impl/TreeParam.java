package com.itouch8.pump.util.param.tree.impl;

import java.util.List;

import com.itouch8.pump.util.param.tree.ITreeParam;
import com.itouch8.pump.util.param.tree.ITreeParamNode;
import com.itouch8.pump.util.tree.impl.Tree;


public class TreeParam extends Tree<ITreeParamNode> implements ITreeParam {

    
    private static final long serialVersionUID = 1352641506604323455L;

    
    private String paramCode;

    
    private String paramName;

    
    private String paramGroup;

    
    private String paramAttr;

    
    private boolean editable;

    
    private int seqno;

    
    private String des;

    
    private List<ITreeParamNode> items;

    @Override
    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Override
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(String paramGroup) {
        this.paramGroup = paramGroup;
    }

    @Override
    public String getParamAttr() {
        return paramAttr;
    }

    public void setParamAttr(String paramAttr) {
        this.paramAttr = paramAttr;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }

    @Override
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<ITreeParamNode> getItems() {
        return items;
    }

    public void setItems(List<ITreeParamNode> items) {
        this.items = items;
    }

    @Override
    public void build() {
        super.build(getItems(), getParamCode(), getParamName());
    }
}
