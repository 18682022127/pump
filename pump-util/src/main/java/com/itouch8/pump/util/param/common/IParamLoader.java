package com.itouch8.pump.util.param.common;

import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.param.IParam;


public interface IParamLoader<P extends IParam> {

    
    public Map<String, P> loadParams(List<String> names);
}
