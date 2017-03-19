package com.itouch8.pump.util.param.single.impl;

import java.io.Serializable;

import com.itouch8.pump.util.param.single.ISingleParam;


public class SingleParamEntity implements Serializable, ISingleParam {

    private static final long serialVersionUID = -2983610698747508553L;

    
    private String paramCode;

    
    private String paramName;

    
    private String storeType;

    
    private String paramGroup;

    
    private String dataType;

    
    private boolean editable;

    
    private String paramValue;

    
    private String defaultValue;

    
    private String valueRule;

    
    private String valueRuleParam;

    
    private int seqno;

    
    private String des;

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
    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
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
        return "SINGLE";
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getValueRule() {
        return valueRule;
    }

    public void setValueRule(String valueRule) {
        this.valueRule = valueRule;
    }

    @Override
    public String getValueRuleParam() {
        return valueRuleParam;
    }

    public void setValueRuleParam(String valueRuleParam) {
        this.valueRuleParam = valueRuleParam;
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
}
