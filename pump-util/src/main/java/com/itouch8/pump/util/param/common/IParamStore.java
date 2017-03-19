package com.itouch8.pump.util.param.common;

import com.itouch8.pump.util.param.IParam;


public interface IParamStore<P extends IParam> {

    
    public P get(String name);

    
    public void remove(String name);

    
    public void save(String name, P value);

    
    public boolean contains(String name);

    
    public void clear();
}
