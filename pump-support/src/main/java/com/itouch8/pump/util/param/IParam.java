package com.itouch8.pump.util.param;

import java.io.Serializable;


public interface IParam extends Serializable {

    
    public String getParamCode();

    
    public String getParamName();

    
    public String getParamAttr();

    
    public boolean isEditable();

    
    public String getParamGroup();

    
    public int getSeqno();

    
    public String getDes();
}
