package com.itouch8.pump.core.util.data.accessor.impl;

import java.util.Map;

import com.itouch8.pump.core.util.data.accessor.IDataAccessor;
import com.itouch8.pump.core.util.spring.SpEL;


public class SpelDataAccessor extends AbstractDataAccessor {

    private final Object root;

    private final Map<String, Object> vars;

    public SpelDataAccessor() {
        this(null, null, null);
    }

    public SpelDataAccessor(Object root) {
        this(null, root, null);
    }

    public SpelDataAccessor(Object root, Map<String, Object> vars) {
        this(null, root, vars);
    }

    private SpelDataAccessor(IDataAccessor parent, Object root, Map<String, Object> vars) {
        super(parent);
        this.root = root;
        this.vars = vars;
    }

    
    @Override
    public <T> T value(String property, Class<T> cls) {
        return SpEL.getValue(root, property, vars, cls);
    }

    
    @Override
    public void set(String property, Object value) {
        SpEL.setValue(root, property, vars, value);
    }

    
    @Override
    public void addVar(String key, Object value) {
        SpEL.addVar(key, value);
    }

    
    @Override
    public Object getRoot() {
        return root;
    }

    
    @Override
    protected IDataAccessor doDerive(Object value) {
        return new SpelDataAccessor(this, value, vars);
    }
}
