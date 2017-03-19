package com.itouch8.pump.core.dao.call;

import java.util.Iterator;


public interface ICallResult {

    
    public <T> T getOutputParam(String name);

    
    public Iterator<String> iterator();
}
