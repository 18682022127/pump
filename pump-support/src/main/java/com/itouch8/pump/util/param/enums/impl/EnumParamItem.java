package com.itouch8.pump.util.param.enums.impl;

import com.itouch8.pump.util.param.enums.IEnumParamItem;


public class EnumParamItem implements IEnumParamItem {

    private static final long serialVersionUID = 3176179373282230884L;

    
    private String paramCode;

    
    private String dataCode;

    
    private String dataText;

    
    private String dataParam;

    
    private int seqno;

    
    private String des;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public String getDataParam() {
        return dataParam;
    }

    public void setDataParam(String dataParam) {
        this.dataParam = dataParam;
    }

    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
