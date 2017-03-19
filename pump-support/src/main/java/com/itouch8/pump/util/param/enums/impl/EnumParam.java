package com.itouch8.pump.util.param.enums.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.param.enums.IEnumParam;
import com.itouch8.pump.util.param.enums.IEnumParamItem;


public class EnumParam implements IEnumParam {

    private static final long serialVersionUID = 1385030239468270262L;

    
    private String paramCode;

    
    private String paramName;

    
    private String paramGroup;

    
    private String paramAttr;

    
    private boolean editable;

    
    private int seqno;

    
    private String des;

    
    private List<IEnumParamItem> items;

    
    private Map<String, IEnumParamItem> map = new HashMap<String, IEnumParamItem>();

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

    @Override
    public List<IEnumParamItem> getItems() {
        return items;
    }

    public void setItems(List<IEnumParamItem> items) {
        this.items = items;
    }

    @Override
    public IEnumParamItem getItem(String code) {
        if (map.isEmpty() && items != null && !items.isEmpty()) {
            synchronized (map) {
                if (map.isEmpty()) {
                    for (IEnumParamItem item : items) {
                        this.map.put(item.getDataCode(), item);
                    }
                }
            }
        }
        return this.map.get(code);
    }

}
