package com.itouch8.pump.core.util.data.accessor.impl;

import com.itouch8.pump.core.util.data.accessor.IDataAccessor;
import com.itouch8.pump.core.util.data.accessor.IDataAccessorFactory;


public abstract class AbstractDataAccessorFactory implements IDataAccessorFactory {

    @Override
    public IDataAccessor newDataAccessor() {
        return this.newDataAccessor(null, null);
    }

    @Override
    public IDataAccessor newDataAccessor(Object root) {
        return this.newDataAccessor(root, null);
    }
}
