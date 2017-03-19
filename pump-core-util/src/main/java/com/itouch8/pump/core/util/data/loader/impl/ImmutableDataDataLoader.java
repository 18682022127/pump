package com.itouch8.pump.core.util.data.loader.impl;

import com.itouch8.pump.core.util.data.loader.IDataLoader;


public class ImmutableDataDataLoader implements IDataLoader {

    private final Object data;

    public ImmutableDataDataLoader(Object data) {
        this.data = data;
    }

    @Override
    public Object load(Object param) {
        return data;
    }
}
