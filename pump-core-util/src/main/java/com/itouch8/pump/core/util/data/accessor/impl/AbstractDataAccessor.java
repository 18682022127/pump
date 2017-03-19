package com.itouch8.pump.core.util.data.accessor.impl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.data.accessor.IDataAccessor;

import ognl.EnumerationIterator;


public abstract class AbstractDataAccessor implements IDataAccessor {

    private final IDataAccessor parent;

    protected AbstractDataAccessor(IDataAccessor parent) {
        this.parent = parent == null ? this : parent;
    }

    
    @Override
    public Object value(String property) {
        return value(property, null);
    }

    
    @Override
    public void addVars(Map<? extends String, ? extends Object> vars) {
        if (null != vars && !vars.isEmpty()) {
            for (String key : vars.keySet()) {
                this.addVar(key, vars.get(key));
            }
        }
    }

    
    @Override
    public Iterator<Object> iterator(String property) {
        Object value = this.value(property);
        return convert2Iterator(value);
    }

    
    @Override
    public boolean match(String condition) {
        if (CoreUtils.isBlank(condition)) {
            return true;
        }
        Boolean value = this.value(condition, Boolean.class);
        return null != value && value.booleanValue();
    }

    
    @Override
    public IDataAccessor derive(String property) {
        if (CoreUtils.isBlank(property)) {
            return this;
        }
        Object value = this.value(property);
        return doDerive(value);
    }

    
    protected abstract IDataAccessor doDerive(Object value);

    
    @Override
    public IDataAccessor getParent() {
        return parent;
    }

    
    @SuppressWarnings("unchecked")
    private Iterator<Object> convert2Iterator(Object source) {
        Iterator<Object> iterator = null;
        if (null == source) {
            return null;
        } else if (source instanceof Enumeration) {
            iterator = new EnumerationIterator((Enumeration<Object>) source);
        } else if (source instanceof Iterator) {
            iterator = (Iterator<Object>) source;
        } else if (source instanceof Iterable) {
            iterator = ((Iterable<Object>) source).iterator();
        } else if (source instanceof Map) {
            iterator = (((Map<?, Object>) source).values()).iterator();
        } else if (source.getClass().isArray()) {
            if (source instanceof Object[]) {
                iterator = Arrays.asList((Object[]) source).iterator();
            } else {
                int length = Array.getLength(source);
                Class<?> wrapperType = Array.get(source, 0).getClass();
                Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
                for (int i = 0; i < length; i++) {
                    newArray[i] = Array.get(source, i);
                }
                iterator = Arrays.asList(newArray).iterator();
            }
        } else {
            iterator = Arrays.asList(source).iterator();
        }
        return iterator;
    }
}
