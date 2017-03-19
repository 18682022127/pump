package com.itouch8.pump.core.util.reflect.method;


public interface IMethodInvoker {

    
    public Object invoke(Object param);

    
    public Object invoke(Object param, IParamExtractor extractor);
}
