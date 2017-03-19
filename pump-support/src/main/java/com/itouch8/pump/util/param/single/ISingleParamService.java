package com.itouch8.pump.util.param.single;

import com.itouch8.pump.util.param.IParamServiceApi;


public interface ISingleParamService extends IParamServiceApi<ISingleParam> {

    
    public String get(String name);

    
    public <E> E get(String name, Class<E> cls);

    
    public <E> E get(String name, E defaultValue, Class<E> cls);

    
    public void refresh();
}
