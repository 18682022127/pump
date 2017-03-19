package com.itouch8.pump.util.param;

import java.io.Serializable;


public interface IParamItem extends Serializable {

    
    public String getParamCode();

    
    public String getDataCode();

    
    public String getDataText();

    
    public String getDataParam();

    
    public int getSeqno();

    
    public String getDes();
}
