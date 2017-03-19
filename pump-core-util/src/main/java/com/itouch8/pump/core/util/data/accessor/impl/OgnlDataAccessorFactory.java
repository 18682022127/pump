package com.itouch8.pump.core.util.data.accessor.impl;

import java.util.Map;

import com.itouch8.pump.core.util.data.accessor.IDataAccessor;


public class OgnlDataAccessorFactory extends AbstractDataAccessorFactory {

    @Override
    public IDataAccessor newDataAccessor(Object root, Map<String, Object> vars) {
        return new OgnlDataAccessor(root, vars);
    }

}
