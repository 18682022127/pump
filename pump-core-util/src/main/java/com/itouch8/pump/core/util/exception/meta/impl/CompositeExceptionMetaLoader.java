package com.itouch8.pump.core.util.exception.meta.impl;

import java.util.List;

import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;
import com.itouch8.pump.core.util.exception.meta.IExceptionMetaLoader;


public class CompositeExceptionMetaLoader implements IExceptionMetaLoader {

    private List<IExceptionMetaLoader> loaders;

    public CompositeExceptionMetaLoader() {}

    public CompositeExceptionMetaLoader(List<IExceptionMetaLoader> loaders) {
        this.loaders = loaders;
    }

    public List<IExceptionMetaLoader> getLoaders() {
        return loaders;
    }

    public void setLoaders(List<IExceptionMetaLoader> loaders) {
        this.loaders = loaders;
    }

    
    @Override
    public IExceptionMeta lookup(String code, Throwable cause) {
        List<IExceptionMetaLoader> loaders = getLoaders();
        if (null != loaders) {
            for (IExceptionMetaLoader loader : loaders) {
                IExceptionMeta meta = loader.lookup(code, cause);
                if (null != meta) {
                    return meta;
                }
            }
        }
        return null;
    }
}
