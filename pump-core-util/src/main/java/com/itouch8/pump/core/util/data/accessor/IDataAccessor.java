package com.itouch8.pump.core.util.data.accessor;

import java.util.Iterator;
import java.util.Map;


public interface IDataAccessor {

    
    public Object value(String property);

    
    public <T> T value(String property, Class<T> cls);

    
    public void set(String property, Object value);

    
    public void addVar(String key, Object value);

    
    public void addVars(Map<? extends String, ? extends Object> vars);

    
    public Iterator<Object> iterator(String property);

    
    public boolean match(String condition);

    
    public IDataAccessor derive(String property);

    
    public IDataAccessor getParent();

    
    public Object getRoot();
}
