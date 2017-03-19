package com.itouch8.pump.util.param;

import java.util.List;
import java.util.Map;


public interface IParamServiceApi<P extends IParam> {

    
    public P getParam(String name);

    
    public Map<String, P> getParams(List<String> names);

    
    public void removeParams(List<String> names);

    
    public boolean isLoaded(String name);

    
    public void clear();
}
