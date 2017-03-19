package com.itouch8.pump.util.param.enums;

import java.util.List;

import com.itouch8.pump.util.param.IParam;


public interface IEnumParam extends IParam {

    
    public List<IEnumParamItem> getItems();

    
    public IEnumParamItem getItem(String code);
}
