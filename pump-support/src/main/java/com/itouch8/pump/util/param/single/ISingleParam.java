package com.itouch8.pump.util.param.single;

import com.itouch8.pump.util.param.IParam;


public interface ISingleParam extends IParam {

    
    public String getStoreType();

    
    public String getDataType();

    
    public String getParamValue();

    
    public void setParamValue(String paramValue);

    
    public String getDefaultValue();

    
    public void setDefaultValue(String defaultValue);

    
    public String getValueRule();

    
    public String getValueRuleParam();
}
